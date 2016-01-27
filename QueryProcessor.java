package com.talentica.cube.qEngine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class QueryProcessor {

	static String delim = "#&#abz#&#";
	static String apikey = "2ABRKGHYY5RQYA9HZ03527G8A";
	static String apisecret = "UPBY1Y5AI0iTlc+gzJenE7FBg5zMBBePRQ3f34blb8A";
	static String annotation = "pos";
	static String domain = "";
	static String subject = "";
	static String feedback = "send Rs 1000 to Sumit";
	static String endpoint = "http://xpresso.abzooba.com:9090/abzooba/engine/result/";
	static String reqType = "POST";
	static String contentType = "text/plain";
	
	public static void main(String[] args) {

		  try {

			URL url = new URL(endpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(reqType);
			conn.setRequestProperty("Content-Type", contentType);

			String input = "apikey="+apikey+delim+
						   "apisecret="+apisecret+delim+
						   "annotation="+annotation+delim+
						   "domain="+domain+delim+
						   "subject="+subject+delim+
						   "feedback="+feedback;
			
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String response;
			System.out.println("Response from Server .... \n");
			while ((response = br.readLine()) != null) {
				parseResponse(response);
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }

	}
	
	public static String parseResponse(String response){
		String str = response;
		String action = "";
		String from = "root";
		String amount = "";
		String to = "";
		
		str = str.substring(str.indexOf("{")+1, str.lastIndexOf("}"));
		String arr[] = str.split(",");
		String arr2[] = arr[0].split(":");
		str = arr2[1].substring(arr2[1].indexOf("\"")+1,arr2[1].lastIndexOf("\""));
		
		String arr3[] = str.split(" ");
		
		for (int i=0;i<arr3.length;i++) {
			if (arr3[i].matches(".*\\/[V].*")) {
				action = arr3[i].split("\\\\")[0];
			} else if(arr3[i].matches(".*\\/[CD].*")) {
				amount = arr3[i].split("\\\\")[0];
			} else if(arr3[i].matches(".*\\/[NN].*")) {
				to = arr3[i].split("\\\\")[0];
			}
		}
		
		String json = "{" +
							"action:"+action+","+
							"from:"+from+","+
							"to:"+to+","+
							"amount:"+amount+""+
					  "}";
	
				
		System.out.println(json);
		return json;
		
	}
}

