package com.arismore.poste;

import java.io.FileInputStream;
import java.io.IOException;
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

import com.arismore.poste.data.Traitement;
import com.arismore.poste.data.UniversalNamespaceCache;

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
    Document document = builder.parse(new FileInputStream("/home/mehdi/laposte.xml"));;
    xpath.setNamespaceContext(new UniversalNamespaceCache(document, true));
    
    NodeList nodes1 = (NodeList) xpath.compile("/a:entry/a:title").evaluate(document, XPathConstants.NODESET);;
    for (int i=0; i<nodes1.getLength();i++){
        System.out.println(nodes1.item(i).getTextContent());
      }
    
    String isie = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='id']/text()").evaluate(document, XPathConstants.STRING);
    System.out.println(isie);
    
    Boolean image = (Boolean) xpath.compile("/a:entry/a:id/image?format=jpeg/text()").evaluate(document, XPathConstants.BOOLEAN);
    System.out.println(image);
    
    String format = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='format']/text()").evaluate(document, XPathConstants.STRING);
    System.out.println(format);

    String priorite = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='priorite']/text()").evaluate(document, XPathConstants.STRING);
    System.out.println(priorite);

    String etat = (String) xpath.compile("/a:entry/a:title/:div/:span[@class='etat']/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(etat);
	
	String plateformTri = (String) xpath.compile("/a:entry/a:author/a:name/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(plateformTri);
	
	String envoloppe_expire = (String) xpath.compile("/a:entry/a:title/:div/:div[@class='expires']/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(envoloppe_expire);
	
	String cle_externe = (String) xpath.compile("/a:entry/a:title/div/div[@class='client']/span[@class='cleExterne']/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(cle_externe);
	
	String updated = (String) xpath.compile("/a:entry/a:updated/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(updated);
	
	String published = (String) xpath.compile("/a:entry/a:published/text()").evaluate(document, XPathConstants.STRING);
	System.out.println(published);
	
	NodeList traitement = (NodeList) xpath.compile("/a:entry/a:content/:div/:ul[@class='traitement']/:li").evaluate(document, XPathConstants.NODESET);
	List<Traitement> traitements = null;
	System.out.println(traitement.getLength());
	for (int i = 1; i <= traitement.getLength(); i++){
		//System.out.println(traitement.item(i).getTextContent());
		//traitements.add(
				new Traitement(i, document, xpath);
		System.out.println("finished");
	}
	
	
	
	//String p = (String) xpath.compile("/a:entry/a:content/:div/:ul[@class='traitement']/:li[0]/:span[@class='dateDebut']/text()").evaluate(document, XPathConstants.STRING);
	//System.out.println(p);
   
  }

  public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    QueryXML process = new QueryXML();
    process.query();
  }
} 