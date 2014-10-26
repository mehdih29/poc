package com.arismore.poste;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.arismore.poste.data.QueryEntry;
import com.arismore.poste.data.UniversalNamespaceCache;

public class QueryXML {

	public void query() throws ParserConfigurationException, SAXException,
			IOException, XPathExpressionException {
		// standard for reading an XML file

		XPath xpath = XPathFactory.newInstance().newXPath();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();

		// load the Document
		Document document = builder.parse(new FileInputStream(
				"/home/areva/POC/out"));

		xpath.setNamespaceContext(new UniversalNamespaceCache(document, true));

		
		NodeList entries = (NodeList) xpath.compile("/a:feed/a:entry")
				.evaluate(document, XPathConstants.NODESET);
		QueryEntry entry;
		
		if (entries.getLength() > 0) {
			for (int i = 1; i <= entries.getLength(); i++) {
				 entry = new QueryEntry(document, i, xpath);
				
				// new ParcelData(document, xpath, new TraitementMachine(i,
				// document, xpath)));
				 
					//TODO leave this close
					if(i == 100){
						break;
					}
			}
		}

		/*
		 * JSONArray array = obj.getJSONArray("interests"); for(int i = 0 ; i <
		 * array.length() ; i++){
		 * list.add(array.getJSONObject(i).getString("interestKey")); }
		 */

	}

	public static void main(String[] args) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		QueryXML process = new QueryXML();
		process.query();
	}
}