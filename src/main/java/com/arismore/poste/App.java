package com.arismore.poste;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.arismore.poste.data.ParcelData;

public class App {
 
    public static void main(String[] args) throws Exception {
 
 
        
        try {
		
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			//System.out.println(gson.toJson(parcelData));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
} 