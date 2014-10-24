package com.arismore.poste;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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

import com.arismore.poste.data.ParcelData;
import com.arismore.poste.data.TraitementMachine;
import com.arismore.poste.data.TraitementManuel;
import com.arismore.poste.data.UniversalNamespaceCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class QueryXML {

	private ArrayList<ParcelData> parcels = new ArrayList<ParcelData>();

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
				"/home/mehdi/laposte.xml"));

		xpath.setNamespaceContext(new UniversalNamespaceCache(document, true));

		try {

			NodeList traitement = (NodeList) xpath.compile(
					"/a:entry/a:content/:div/:ul[@class='traitement']/:li")
					.evaluate(document, XPathConstants.NODESET);
			if (traitement.getLength() > 0) {
				for (int i = 1; i <= traitement.getLength(); i++) {
					this.parcels.add(new ParcelData(document, xpath,
							new TraitementMachine(i, document, xpath)));
				}
			}
			NodeList traitementManuel = (NodeList) xpath
					.compile(
							"/a:entry/a:content/:div/:ul[@class='traitementManuel']/:li")
					.evaluate(document, XPathConstants.NODESET);
			if (traitementManuel.getLength() > 0) {
				for (int i = 1; i <= traitementManuel.getLength(); i++) {
					this.parcels.add(new ParcelData(document, xpath,
							new TraitementManuel(i, document, xpath)));
				}
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GsonBuilder builder1 = new GsonBuilder();
		Gson gson = builder1.create();
		System.out.println(gson.toJson(parcels));

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