package com.arismore.poste.storm.bolts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class UriToFileBolt extends BaseRichBolt {

	private static final long serialVersionUID = 2222111114444444444L;
	static String STREAMING_API_URL = "http://national.cpn.prd.sie.courrier.intra.laposte.fr/National/enveloppes/v1/externe?";
	private HttpClient client;
	private OutputCollector collector;
	static Logger LOG = Logger.getLogger(UriToFileBolt.class);
	private static String FILE_RECOVERY_SLIDING_WINDOWS = "/svdb/POC/_file_recovery_sliding_window";

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("content"));
	}

	public void execute(Tuple tuple) {

		String url = (String) tuple.getValue(0);

		Long currentTimestamp = (new Date()).getTime();
		HttpGet get = new HttpGet(STREAMING_API_URL + url);

		HttpResponse response;

		// Execute
		try {

			response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {
				InputStream inputStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String in;

				PrintWriter writer = new PrintWriter("/svdb/POC/_file_" + url,
						"UTF-8");
				
				while ((in = reader.readLine()) != null) {
					//for (int i = 0; i < 200; i++) {
						writer.println(in);
					//}
				}
				
				writer.close();
				this.collector.ack(tuple);
				LOG.error("Http get took: "
						+ ((new Date()).getTime() - currentTimestamp));

			} else {
				try {
					PrintWriter out = new PrintWriter(new FileWriter(
							FILE_RECOVERY_SLIDING_WINDOWS, true));
					out.println(url);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error("Error in communication with the OREST TAE api ["
					+ get.getURI().toString() + "]");
			try {
				PrintWriter out = new PrintWriter(new FileWriter(
						FILE_RECOVERY_SLIDING_WINDOWS, true));
				out.println(STREAMING_API_URL + url);
				out.close();
			} catch (IOException a) {
				a.printStackTrace();
			}
		}
	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		client = HttpClientBuilder.create().build();
	
	}
}