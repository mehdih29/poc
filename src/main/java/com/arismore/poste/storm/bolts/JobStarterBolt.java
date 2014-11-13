package com.arismore.poste.storm.bolts;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.arismore.poste.data.UniversalNamespaceCache;

public class JobStarterBolt extends BaseRichBolt {

	private static final long serialVersionUID = 111111L;
	static String STREAMING_API_URL = "http://national.cpn.prd.sie.courrier.intra.laposte.fr/National/enveloppes/v1/externe?";
	private static String SEP = "&";
	private static String STARTINDEX = "startIndex=";
	private static String COUNT = "count=";
	private HttpClient client;
	private OutputCollector collector;
	private static int STEP = 1000;
	static Logger LOG = Logger.getLogger(JobStarterBolt.class);
	private static String FILE_RECOVERY_WINDOWS = "/svdb/POC/_file_recovery_window";
	XPath xpath = null;
	DocumentBuilder builder;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("url"));
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub

		String slidingWindow = (String) tuple.getValue(0);

		LOG.debug("processing " + slidingWindow);

		HttpGet get = new HttpGet(STREAMING_API_URL + slidingWindow + SEP
				+ STARTINDEX + "1" + SEP + COUNT + "1");
		HttpResponse response;

		try {
			String url = null;

			response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {

				InputStream inputStream = response.getEntity().getContent();

				Document doc = builder.parse(new InputSource(inputStream));
				xpath.setNamespaceContext(new UniversalNamespaceCache(doc, true));

				int number = Integer.parseInt((String) xpath.compile(
						"/a:feed/openSearch:totalResults").evaluate(doc,
						XPathConstants.STRING));
				for (int i = 1; i < number; i += STEP) {
					url = slidingWindow + SEP + STARTINDEX + i + SEP + COUNT
							+ STEP;
					collector.emit(tuple, new Values(url));
				}
				collector.ack(tuple);
			} else {
				try {
					PrintWriter out = new PrintWriter(new FileWriter(
							FILE_RECOVERY_WINDOWS, true));
					out.println(STREAMING_API_URL + slidingWindow + SEP
							+ STARTINDEX + "1" + SEP + COUNT + "1");
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
						FILE_RECOVERY_WINDOWS, true));
				out.println(STREAMING_API_URL + slidingWindow + SEP
						+ STARTINDEX + "1" + SEP + COUNT + "1");
				out.close();
			} catch (IOException a) {
				a.printStackTrace();
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public void prepare(Map stormConf, TopologyContext context) {
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		client = HttpClientBuilder.create().build();
		xpath = XPathFactory.newInstance().newXPath();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}