package com.arismore.poste;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.arismore.poste.data.ParcelData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
 
public class App {
 
    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(ParcelData.class);
 
        
        try {
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			ParcelData parcelData = (ParcelData) unmarshaller.unmarshal(new File("/home/areva/laposte.xml"));
 
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			System.out.println(gson.toJson(parcelData));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        /* Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty("eclipselink.media-type", "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
        
        System.out.println(parcelData.toString());
        marshaller.marshal(parcelData, System.out);*/
        
       /* Map<String, Object> properties = new HashMap<String, Object>(3);
        properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, "/home/mehdi/input.xml");
        properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
        properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        JAXBContext jsonJC = JAXBContext.newInstance(new Class[] {ParcelData.class}, properties);
        Marshaller jsonMarshaller = jsonJC.createMarshaller();
        jsonMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jsonMarshaller.marshal(parcelData, System.out);*/
    }
 
} 