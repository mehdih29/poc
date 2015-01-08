package com.arismore.poste;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HTTP {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		RequestConfig rc;
		HttpClient client;
		client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet("http://localhost:8000");
		// HttpGet get = new HttpGet(STREAMING_API_URL + url);

		

		HttpResponse response;

		// Execute

			response = client.execute(get);
			StatusLine status = response.getStatusLine();

			if (status.getStatusCode() == 200) {
				System.out.println(client.toString());
			}

	}

}
