package com.arismore.poste.storm.bolts;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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
import backtype.storm.tuple.Values;

public class UriGetBolt extends BaseRichBolt {

	private static final long serialVersionUID = 222111112L;
	static String STREAMING_API_URL = "http://national.cpn.prd.sie.courrier.intra.laposte.fr/National/enveloppes/v1/externe?";
	private static String SEP = "&";
	private static String BEGINDATE = "dateDebut=";
	private static String ENDDATE = "dateFin=";
	private static String STARTINDEX = "startIndex=";
	private static String COUNT = "count=";
	private HttpClient client;
	private OutputCollector collector;
	static Logger LOG = Logger.getLogger(UriGetBolt.class);
	private static String FILE_RECOVERY_SLIDING_WINDOWS = "/svdb/POC/_file_recovery_sliding_window";

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("URI", "content"));
	}

	public void execute(Tuple tuple) {

		String dateDebut = (String) tuple.getValue(0);
		String dateFin = (String) tuple.getValue(1);
		String startIndex = (String) tuple.getValue(2);
		String count = (String) tuple.getValue(3);

		Long currentTimestamp = (new Date()).getTime();
		String url = STREAMING_API_URL + BEGINDATE + dateDebut
				+ SEP + ENDDATE + dateFin + SEP + STARTINDEX + startIndex + SEP
				+ COUNT + count; 
		HttpGet get = new HttpGet(url);

		HttpResponse response;

		// Execute
		try {

			response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {
				InputStream inputStream = response.getEntity().getContent();

				collector.emit(tuple, new Values(url, IOUtils.toString(inputStream, "utf-8")));
				collector.ack(tuple);
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