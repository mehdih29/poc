package com.arismore.poste.storm.bolts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class JobStarterBolt extends BaseRichBolt {

	static String STREAMING_API_URL = "http://national.cpn.prd.sie.courrier.intra.laposte.fr/National/enveloppes/v1/externe?";
	private static String SEP = "&";
	private static String BEGINDATE = "dateDebut=";
	private static String ENDDATE = "dateFin=";
	private static String STARTINDEX = "startIndex=";
	private static String COUNT = "count=";// 2010-11-08T08:00:00Z&dateFin=2010-11-08T08:01:00Z&startIndex=1&count=1";
	private HttpClient client;
	private OutputCollector collector;
	// LinkedBlockingQueue<String> tweets = new LinkedBlockingQueue<String>();
	static Logger LOG = Logger.getLogger(JobStarterBolt.class);
	private SimpleDateFormat formater = null;

	public void declareOutputFields(OutputFieldsDeclarer ofd) {
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub

		Date date = (Date) tuple.getValue(0);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, -6);
		Date start = cal.getTime();

		cal.setTime(date);
		cal.add(Calendar.MINUTE, -5);
		Date end = cal.getTime();

		HttpGet get = new HttpGet(
				"https://api.twitter.com/1.1/search/tweets.json");
		HttpResponse response;

		// Execute
		try {
			PrintWriter writer = new PrintWriter("_file_"
					+ formater.format(date), "UTF-8");
			writer.write("start");
			writer.write(formater.format(start));
			writer.write("end");
			writer.write(formater.format(end));
			writer.write(STREAMING_API_URL + BEGINDATE + formater.format(start)
					+ SEP + ENDDATE + formater.format(end) + SEP + STARTINDEX
					+ "1" + SEP + COUNT + "1");

			response = client.execute(get);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == 200) {
				InputStream inputStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String in;
				// Read line by line
				while ((in = reader.readLine()) != null) {
					writer.write(in);
				}
			}

			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error("Error in communication with twitter api ["
					+ get.getURI().toString() + "]");
		}

	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.formater = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm':00Z'");
		client = HttpClientBuilder.create().build();

	}

}