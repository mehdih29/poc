package com.arismore.poste.storm.bolts;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

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
	private static String BEGINDATE = "dateDebut=";
	private static String ENDDATE = "dateFin=";
	private static String STARTINDEX = "startIndex=";
	private static String COUNT = "count=";
	private HttpClient client;
	private OutputCollector collector;
	private static int STEP = 1000;
	static Logger LOG = Logger.getLogger(JobStarterBolt.class);
	private SimpleDateFormat formater = null;
	private static String FILE_RECOVERY_WINDOWS = "/dev/shm/_file_recovery_window";
	Calendar cal = null;
	XPath xpath = null;
	DocumentBuilder builder;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("url"));
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub

		Date date = (Date) tuple.getValue(0);

		cal.setTime(date);
		cal.add(Calendar.MINUTE, -6);
		Date start = cal.getTime();

		cal.setTime(date);
		cal.add(Calendar.MINUTE, -5);
		Date end = cal.getTime();

		HttpGet get = new HttpGet("http://10.192.6.7:8000/out2");
		HttpResponse response;

		// Execute
		try {
			String url = null;

			response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {

				// TODO remove this lines for the write in the files

				PrintWriter writer = new PrintWriter("/dev/shm/_file_"
						+ formater.format(date), "UTF-8");

				InputStream inputStream = response.getEntity().getContent();

				Document doc = builder.parse(new InputSource(inputStream));
				xpath.setNamespaceContext(new UniversalNamespaceCache(doc, true));

				int number = Integer.parseInt((String) xpath.compile(
						"/a:feed/openSearch:totalResults").evaluate(doc,
						XPathConstants.STRING));
				writer.write(number);
				for (int i = STEP; i < number; i += STEP) {
					url = STREAMING_API_URL + BEGINDATE
							+ formater.format(start) + SEP + ENDDATE
							+ formater.format(end) + SEP + STARTINDEX + i + SEP
							+ COUNT + STEP;
					collector.emit(new Values(url));
					collector.ack(tuple);
					writer.write(url);
				}
				writer.close();
			} else {
				try {
					PrintWriter out = new PrintWriter(new FileWriter(
							FILE_RECOVERY_WINDOWS, true));
					out.println(STREAMING_API_URL + BEGINDATE
							+ formater.format(start) + SEP + ENDDATE
							+ formater.format(end) + SEP + STARTINDEX + "1"
							+ SEP + COUNT + "1");
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
				out.println(STREAMING_API_URL + BEGINDATE
						+ formater.format(start) + SEP + ENDDATE
						+ formater.format(end) + SEP + STARTINDEX + "1" + SEP
						+ COUNT + "1");
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

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.formater = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm':00Z'");
		client = HttpClientBuilder.create().build();
		cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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