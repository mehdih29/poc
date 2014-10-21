package com.arismore.poste;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
    doc = builder.parse("/home/areva/laposte.xml");

 // get the first element
    Element element = doc.getDocumentElement();

    // get all child nodes
    NodeList nodes = element.getChildNodes();

    // print the text content of each child
    for (int i = 0; i < nodes.getLength(); i++) {
       //System.out.println("" + nodes.item(i).getTextContent());
    }
    
    // create an XPathFactory
    //XPathFactory xFactory = XPathFactory.newInstance();

    // create an XPath object
    //XPath xpath = xFactory.newXPath();
    
    NodeList nodes1 = (NodeList) xpath.compile("/a:entry/a:title").evaluate(doc, XPathConstants.NODESET);


    
    for (int i=0; i<nodes1.getLength();i++){
      System.out.println(nodes1.item(i).getNodeValue());
    }
    System.out.println("here we go");

    System.out.println(doc.toString());
   /* 
    expr = xpath.compile("/a:entry/a:title/div/span[@class='id']");
	String isie;
	
    
    String result = (String) expr.evaluate(doc, XPathConstants.STRING);

    System.out.println(result);

    // cast the result to a DOM NodeList
    System.out.println("here we go");
    
    expr = xpath.compile("/a:entry/a:id/image?format=jpeg/text()");
	String image;
	
    Boolean check = (Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
    System.out.println(check);
    
    System.out.println("here we go");
    
    expr = xpath.compile("/a:entry/a:title/div/span[@class='format']/text()");
    String format;
    
    result = (String) expr.evaluate(doc, XPathConstants.STRING);

    System.out.println(result);

    System.out.println("here we go");
    
    
    expr = xpath.compile("/a:entry/a:title/div/span[@class='priorite']/text()");
	String priorite;
	
    Object result1 = expr.evaluate(doc, XPathConstants.NODESET);

    // cast the result to a DOM NodeList
    NodeList nodes1 = (NodeList) result1;
    for (int i=0; i<nodes1.getLength();i++){
      System.out.println(nodes1.item(i).getNodeValue());
    }
    System.out.println("here we go");
    expr = xpath.compile("/a:entry/a:title/div/span[attribute::class='etat']/text()");
	String etat;
    
	result1 = expr.evaluate(doc, XPathConstants.NODESET);

    // cast the result to a DOM NodeList
    nodes1 = (NodeList) result1;
    for (int i=0; i<nodes1.getLength();i++){
      System.out.println(nodes1.item(i).getNodeValue());
    }*/
    
 /*   
    
    // compile the XPath expression
    expr = xpath.compile("//person[firstname='Lars']/lastname/text()");
    // run the query and get a nodeset
    Object result = expr.evaluate(doc, XPathConstants.NODESET);

    // cast the result to a DOM NodeList
    NodeList nodes = (NodeList) result;
    for (int i=0; i<nodes.getLength();i++){
      System.out.println(nodes.item(i).getNodeValue());
    }
    System.out.println("here we go");

    // new XPath expression to get the number of people with name Lars
    expr = xpath.compile("count(//person[firstname='Lars'])");
    // run the query and get the number of nodes
    Double number = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
    System.out.println("Number of objects " +number);

    // do we have more than 2 people with name Lars?
    expr = xpath.compile("count(//person[firstname='Lars']) >2");
    // run the query and get the number of nodes
    Boolean check = (Boolean) expr.evaluate(doc, XPathConstants.BOOLEAN);
    System.out.println(check);*/
  }

  public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    QueryXML process = new QueryXML();
    process.query();
  }
} 