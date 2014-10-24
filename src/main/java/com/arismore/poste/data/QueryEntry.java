package com.arismore.poste.data;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class QueryEntry {
	
	//private XPath xpath;
	//private Document document;
	private ArrayList<ParcelData> parcels = null;

	public QueryEntry(Document document, XPath xpath){
		
		try {
			
			parcels = new ArrayList<ParcelData>();
			ParcelData parcel = new ParcelData(document, xpath);
			ParcelData clone = null;

			NodeList traitement = (NodeList) xpath.compile(
					"/a:feed/a:entry/a:content/:div/:ul[@class='traitement']/:li")
					.evaluate(document, XPathConstants.NODESET);
			if (traitement.getLength() > 0) {
				for (int i = 1; i <= traitement.getLength(); i++) {
					clone = (ParcelData) parcel.clone();
					clone.setTraitement(new TraitementMachine(i, document,
							xpath));
					this.parcels.add(clone);
					// new ParcelData(document, xpath, new TraitementMachine(i,
					// document, xpath)));
				}
			}
			NodeList traitementManuel = (NodeList) xpath
					.compile(
							"/a:feed/a:entry/a:content/:div/:ul[@class='traitementManuel']/:li")
					.evaluate(document, XPathConstants.NODESET);
			if (traitementManuel.getLength() > 0) {
				for (int i = 1; i <= traitementManuel.getLength(); i++) {
					clone = (ParcelData) parcel.clone();
					clone.setTraitement(new TraitementManuel(i, document,
							xpath));
					System.out.println(clone.getAdresses());
					this.parcels.add(clone);
					// this.parcels.add(new ParcelData(document, xpath, new
					// TraitementManuel(i, document, xpath)));
				}
			}
			
			GsonBuilder builder1 = new GsonBuilder();
			Gson gson = builder1.create();
			System.out.println(gson.toJson(parcels));

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
