package com.lacunaexpanse.LacunaAndroidClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Client {
	// Declare some variables.
	static String  EMPIRE_NAME;
	static String  EMPIRE_PASS;
	static String  SERVER;
	static String  API_KEY;
	static Context CONTEXT;
	static String  SESSION_ID;
	
	// Some items to cache.
	static JSONObject STATUS;
	
	public Client(String empireName, String empirePass, String server, String apiKey, Context context) {
		// Set the variables.
		EMPIRE_NAME = empireName;
		EMPIRE_PASS = empirePass;
		SERVER      = server;
		API_KEY     = apiKey;
		CONTEXT     = context;
		
		
		JSONObject result = send(false, new String[]{EMPIRE_NAME, EMPIRE_PASS, API_KEY}, "empire", "login");
		
		Object[] sessionIdObject = get(result, "session_id", 1);
		SESSION_ID               = sessionIdObject.toString();
		
	}
	
	public static JSONObject send(boolean includeSessionId, String[] params, String module, String method) {
		// Parse the String[] into one string.
		String parsedParams = "[";
		
		for (int i = 0; i < params.length; i++) {
			parsedParams += "\"" + params[i] + "\",";
		}
		
		// Remove the comma at the end.
		parsedParams.substring(0, parsedParams.length()-1);
		
		// Close off the params block.
		parsedParams += "]";
		
		
		// Set the timeout values on the request.
		HttpParams httpParameters = new BasicHttpParams();
		int timeout = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout); // Timeout for connection to be established.
		HttpConnectionParams.setSoTimeout(httpParameters, timeout); // Timeout for data to arrive.
		
		// Create a new HttpClient and add URL;
	    HttpClient httpclient = new DefaultHttpClient(httpParameters);
	    HttpPost httppost = new HttpPost("https://" + SERVER + ".lacunaexpanse.com/" + module);

	    try {
	        // Add the data to the POST body.
	        ArrayList<NameValuePair> paramPairs = new ArrayList<NameValuePair>(4);
	        paramPairs.add(new BasicNameValuePair("jsonrpc", "2.0"));
	        paramPairs.add(new BasicNameValuePair("id",      "1"));
	        paramPairs.add(new BasicNameValuePair("method",  method));
	        paramPairs.add(new BasicNameValuePair("params",  parsedParams));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(paramPairs));

	        // Execute HTTP Post Request.
	        HttpResponse serverResponse = httpclient.execute(httppost);
	        
	        // Get the String response.
	        BufferedReader in = new BufferedReader(new InputStreamReader(serverResponse.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}

			JSONObject response = new JSONObject(sb.toString());
			in.close();
			
			// Now check if the server returned a result or an error.
			if (response.has("result")) {
				Object[] getObject = get(response, "result", 1);
				JSONObject result  = new JSONObject(getObject[0].toString());
				
				// Store the status in cache.
				Object[] getObject1 = get(result, "status", 1);
				JSONObject status   = new JSONObject(getObject1[0].toString());
				STATUS = status;
				
				// Now we just return the result.
				return result;
			}
			else if (response.has("error")) {
				Object[] getObject = get(response, "result", 1);
				JSONObject result  = new JSONObject(getObject[0].toString());
				
				// If it's an error, I don't think there's any point in caching the status.
				//Object[] getObject1 = get(result, "status", 1);
				//JSONObject status   = new JSONObject(getObject1[0].toString());
				//STATUS = status;
				
				// Now we just return the result.
				return result;
			}

	    }
	    catch (ClientProtocolException e) {
	        e.printStackTrace();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    catch (JSONException e) {
	    	e.printStackTrace();
	    }
	    
		return new JSONObject(); // Just to keep the page "error-free".
	}
	
	public void setContext(Context context) {
		CONTEXT = context;
	}
	
	/*
	 * 
	 * The idea behind this method is to avoid needing to use a try-catch
	 * statement every time we parse data. The int option is used to define 
	 * the method to run.
	 * 
	 * 0 = getJSONArray()
	 * 1 = getJSONObject()
	 * 2 = getString()
	 * 3 = getLong()
	 * 
	 * To get around the issue of having to set a return type of this method,
	 * I return the value in an Object[] at position 0.
	 * 
	 * Annoyingly this produces somewhat ugly usages, such as:
	 * 
	 * 		Object[] getObject = get(response, "planets", 1);
	 *	    JSONObject planets = new JSONObject(getObject[0].toString());
	 *
	 * But I think this is easier that using a full try-catch statement.
	 * 
	 */
	
	public static Object[] get(JSONObject jObject, String targetName, int option) {
		if (option == 0) {
			try {
				JSONArray value = jObject.getJSONArray(targetName);
				Object[] object = {value};
				return object;
			}
			catch (JSONException e){
				e.printStackTrace();
			}
		}
		else if (option == 1) {
			try {
				JSONObject value = jObject.getJSONObject(targetName);
				Object[] object = {value};
				return object;
			}
			catch (JSONException e){
				e.printStackTrace();
			}
		}
		else if (option == 2) {
			try {
				String value = jObject.getString(targetName);
				Object[] object = {value};
				return object;
			}
			catch (JSONException e){
				e.printStackTrace();
			}
		}
		else if (option == 3) {
			try {
				long value = jObject.getLong(targetName);
				Object[] object = {value};
				return object;
			}
			catch (JSONException e){
				e.printStackTrace();
			}
		}
		// Don't need an else{}, if something was returned above, this code won't run.
		Object[] object = {null};
		return object;
	}
}