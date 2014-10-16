package com.arismore.poste.XML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
 
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
 
public class Main {
    public static void main(String[] args) {
 
        try {
            FileInputStream file = new FileInputStream(new File("/home/mehdi/laposte.xml"));
                 
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
             
            DocumentBuilder builder =  builderFactory.newDocumentBuilder();
             
            Document xmlDocument = builder.parse(file);
 
            XPath xPath =  XPathFactory.newInstance().newXPath();
 
            System.out.println("*************************");
            String expression = "/a:entry/a:author/a:name";
            System.out.println(expression);
            String email = xPath.compile(expression).evaluate(xmlDocument);
            System.out.println(email);
 
            System.out.println("*************************");
            expression = "/a:entry/a:title/div/span[@class='id']";
            System.out.println(expression);
            String nodeList = xPath.compile(expression).evaluate(xmlDocument);
             
            //for (int i = 0; i < nodeList.getLength(); i++) {
            //    System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
            //}
 
            System.out.println(nodeList);
            
            System.out.println("*************************");
            expression = "/a:entry/a:id/image?format=jpeg";
            System.out.println(expression);
            nodeList = xPath.compile(expression).evaluate(xmlDocument); //, XPathConstants.NODESET);
            //for (int i = 0; i < nodeList.getLength(); i++) {
            //    System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
            //}
            System.out.println(nodeList);
  
            System.out.println("*************************");
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }      
    }
}
