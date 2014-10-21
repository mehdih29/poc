package com.arismore.poste;

import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;

public class XMLtoJSON {
    private URL url = null;
    private InputStream inputStream = null;
    
   
    public void getXMLfromJson() {
        try {
           
            FileInputStream inputStream = new FileInputStream("/home/areva/laposte.xml"); 
            // Session IOUtils;
            String xml = IOUtils.toString(inputStream);

            // String xml = Files.toString("/home/mehdi/laposte.xml", Charset.UTF_8);

            //FileInputStream file = new FileInputStream(new File("/home/mehdi/laposte.xml"));
            
            System.out.println("XML data : " + xml);

            // JSON objJson = new XMLSerializer().read(xml);

            org.json.JSONObject objJson = XML.toJSONObject(xml);

            System.out.println("JSON data : " + objJson);
            System.out.println(objJson);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                url = null;
            } catch (IOException ex) {
            }
        }
    }

    public static void main(String[] args) {
        new XMLtoJSON().getXMLfromJson();
    }
}