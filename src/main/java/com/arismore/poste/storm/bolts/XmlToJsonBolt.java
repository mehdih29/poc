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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.arismore.poste.data.QueryEntry;
import com.arismore.poste.data.Singleton;
import com.arismore.poste.data.UniversalNamespaceCache;

public class XmlToJsonBolt extends BaseRichBolt {

	private static final long serialVersionUID = 111113L;
	private HttpClient client;
	private OutputCollector collector;
	static Logger LOG = Logger.getLogger(XmlToJsonBolt.class);
	private static String FILE_RECOVERY_WINDOWS = "/dev/shm/_file_recovery_window";
	XPath xpath = null;
	DocumentBuilder builder;
	Singleton instance = null;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("Parcel"));
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String url = (String) tuple.getValue(0);

		HttpGet get = new HttpGet(url);
		HttpResponse response;

		// Execute
		try {

			response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {

				InputStream inputStream = response.getEntity().getContent();

				Document document = builder.parse(new InputSource(inputStream));
				xpath.setNamespaceContext(new UniversalNamespaceCache(document,
						true));

				NodeList entries = (NodeList) xpath.compile("/a:feed/a:entry")
						.evaluate(document, XPathConstants.NODESET);
				QueryEntry entry;

				if (entries.getLength() > 0) {
					for (int i = 1; i <= entries.getLength(); i++) {
						entry = new QueryEntry(document, i, xpath);
						collector.emit(new Values(entry));
						/*for (int j = 0; j < entry.getParcels().size(); j++) {
							collector.emit(new Values(instance.getGson()
									.toJson(entry.getParcels().get(j))));
						}*/
					}
					collector.ack(tuple);
				}
			} else {
				try {
					PrintWriter out = new PrintWriter(new FileWriter(
							FILE_RECOVERY_WINDOWS, true));
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
						FILE_RECOVERY_WINDOWS, true));
				out.println(url);
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
		this.client = HttpClientBuilder.create().build();
		this.xpath = XPathFactory.newInstance().newXPath();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			this.builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.instance = Singleton.getInstance();
	}
}