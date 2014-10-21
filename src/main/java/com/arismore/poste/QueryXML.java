package com.arismore.poste;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.arismore.poste.data.ParcelData;
import com.arismore.poste.data.Traitement;
import com.arismore.poste.data.UniversalNamespaceCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class QueryXML {
  public void query() throws ParserConfigurationException, SAXException,
      IOException, XPathExpressionException {
    // standard for reading an XML file
	  
	  
	XPath xpath = XPathFactory.newInstance().newXPath();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder;
    Document doc = null;
    XPathExpression expr = null;
    builder = factory.newDocumentBuilder();
    
    
 // load the Document
    Document document = builder.parse(new FileInputStream("/home/areva/laposte.xml"));;
    xpath.setNamespaceContext(new UniversalNamespaceCache(document, true));
    
    NodeList nodes1 = (NodeList) xpath.compile("/a:entry/a:title").evaluate(document, XPathConstants.NODESET);;
    for (int i=0; i<nodes1.getLength();i++){
        System.out.println(nodes1.item(i).getTextContent());
      }
    ParcelData parcel = null;
	try {
		parcel = new ParcelData(document, xpath);
	} catch (XPathExpressionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    GsonBuilder builder1 = new GsonBuilder();
	Gson gson = builder1.create();
	System.out.println(gson.toJson(parcel));
  }

  public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    QueryXML process = new QueryXML();
    process.query();
  }
} 