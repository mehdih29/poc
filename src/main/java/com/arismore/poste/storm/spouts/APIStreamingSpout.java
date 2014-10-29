package com.arismore.poste.storm.spouts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class APIStreamingSpout extends BaseRichSpout {
	static String STREAMING_API_URL = "http://national.cpn.prd.sie.courrier.intra.laposte.fr/National/enveloppes/v1/externe?";
	static private String SEP = "&";
	static private String dateDebut = "dateDebut=";
	static private String dateFin = "dateFin=";
	static private String startIndex = "startIndex=";
	static private String count = "count=";// 2010-11-08T08:00:00Z&dateFin=2010-11-08T08:01:00Z&startIndex=1&count=1";
	private String track;
	private HttpClient client;
	private SpoutOutputCollector collector;
	LinkedBlockingQueue<String> tweets = new LinkedBlockingQueue<String>();
	static Logger LOG = Logger.getLogger(APIStreamingSpout.class);
	static JSONParser jsonParser = new JSONParser();

	public void nextTuple() {
		/*
		 * Create the client call
		 */
		client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(STREAMING_API_URL + track);
		HttpResponse response;
		try {
			// Execute
			response = client.execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == 200) {
				InputStream inputStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String in;
				// Read line by line
				while ((in = reader.readLine()) != null) {
					try {
						// Parse and emit
						Object json = jsonParser.parse(in);
						collector.emit(new Values(track, json));
					} catch (ParseException e) {
						LOG.error("Error parsing message from twitter", e);
					}
				}
			}
		} catch (IOException e) {
			LOG.error("Error in communication with twitter api ["
					+ get.getURI().toString() + "]");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
			}
		}
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		int spoutsSize = context
				.getComponentTasks(context.getThisComponentId()).size();
		int myIdx = context.getThisTaskIndex();
		String[] tracks = ((String) conf.get("track")).split(",");
		StringBuffer tracksBuffer = new StringBuffer();
		for (int i = 0; i < tracks.length; i++) {
			if (i % spoutsSize == myIdx) {
				tracksBuffer.append(",");
				tracksBuffer.append(tracks[i]);
			}
		}
		if (tracksBuffer.length() == 0)
			throw new RuntimeException("No track found for spout"
					+ " [spoutsSize:" + spoutsSize + ", tracks:"
					+ tracks.length + "] the amount"
					+ " of tracks must be more then the spout paralellism");
		this.track = tracksBuffer.substring(1).toString();
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("criteria", "tweet"));
	}
}
