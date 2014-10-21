package com.arismore.poste.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ParcelData {

	private String isie;
	private Boolean image;
	private String format;
	private String priorite;
	private String etat;
	private String plateformTri;
	private String envoloppe_expire;
	private String cle_externe;
	private String updated;
	private String published;
	private ArrayList<Traitement> traitements;
	
	public ParcelData(Document document, XPath xpath) throws XPathExpressionException{
    this.isie = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='id']/text()").evaluate(document, XPathConstants.STRING);
    System.out.println(isie);
    
    this.image = (Boolean) xpath.compile("/a:entry/a:id/image?format=jpeg/text()").evaluate(document, XPathConstants.BOOLEAN);
    System.out.println(image);
    
    this.format = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='format']/text()").evaluate(document, XPathConstants.STRING);
    System.out.println(format);

    this.priorite = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='priorite']/text()").evaluate(document, XPathConstants.STRING);
    System.out.println(priorite);

    this.etat = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='etat']/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(etat);
	
	this.plateformTri = (String) xpath.compile("/a:entry/a:author/a:name/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(plateformTri);
	
	this.envoloppe_expire = (String) xpath.compile("/a:entry/a:title/:div/:div[@class='expires']/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(envoloppe_expire);
	
	this.cle_externe = (String) xpath.compile("/a:entry/a:title/div/div[@class='client']/span[@class='cleExterne']/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(cle_externe);
	
	this.updated = (String) xpath.compile("/a:entry/a:updated/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(updated);
	
	this.published = (String) xpath.compile("/a:entry/a:published/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(published);
	
	NodeList traitement = (NodeList) xpath.compile("/a:entry/a:content/:div/:ul[@class='traitement']/:li").evaluate(document, XPathConstants.NODESET);
	this.traitements = new ArrayList<Traitement>();
	System.out.println(traitement.getLength());
	for (int i = 1; i <= traitement.getLength(); i++){
		//System.out.println(traitement.item(i).getTextContent());
		this.traitements.add(new Traitement(i, document, xpath));
	}
	}
    
    @Override
    public String toString(){
    	return "";
    }

}
