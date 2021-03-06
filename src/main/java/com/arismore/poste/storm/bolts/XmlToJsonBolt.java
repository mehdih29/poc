package com.arismore.poste.storm.bolts;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

import com.arismore.poste.data.ParcelData;
import com.arismore.poste.data.QueryEntry;
import com.arismore.poste.data.UniversalNamespaceCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class XmlToJsonBolt extends BaseRichBolt {

	private static final long serialVersionUID = 222111114L;
	private OutputCollector collector;
	static Logger LOG = Logger.getLogger(XmlToJsonBolt.class);
	static private Gson gson = null;

	XPath xpath = null;
	DocumentBuilder builder;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("Id", "Json"));// new Fields("Parcel"));
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub

		// don't need the first component of the tuple in this bolt
		// String url = (String) tuple.getValue(0);

		String content = (String) tuple.getValue(1);
		ParcelData parcel;
		String id;

		InputSource xmlString = new InputSource(new StringReader(content));
		Document document;
		try {
			document = builder.parse(xmlString);

			xpath.setNamespaceContext(new UniversalNamespaceCache(document,
					true));

			NodeList entries = (NodeList) xpath.compile("/a:feed/a:entry")
					.evaluate(document, XPathConstants.NODESET);
			QueryEntry entry;

			if (entries.getLength() > 0) {
				for (int i = 1; i <= entries.getLength(); i++) {
					entry = new QueryEntry(document, i, xpath);
					for (int j = 0; j < entry.getParcels().size(); j++) {
						parcel = (ParcelData) entry.getParcels().get(j);
						id = parcel.getIsie() + "|"
								+ parcel.getTraitement().getId();
						this.collector.emit(tuple, new Values(id,
								XmlToJsonBolt.gson.toJson(parcel)));
					}
				}
				this.collector.ack(tuple);
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
		XmlToJsonBolt.gson = new GsonBuilder().create();
	}
}