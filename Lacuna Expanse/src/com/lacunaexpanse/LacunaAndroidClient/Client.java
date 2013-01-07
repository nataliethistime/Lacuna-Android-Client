package com.lacunaexpanse.LacunaAndroidClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Client {
	// Declare some variables.
	public static String  EMPIRE_NAME;
	public static String  EMPIRE_PASS;
	public static String  SERVER;
	public static String  API_KEY;
	public static Context CONTEXT;
	public static String  SESSION_ID;
	public static String TAG = "Lacuna Expanse - Debug";
	
	// Some items to cache.
	public static JSONObject STATUS;
	
	public static void login(String empireName, String empirePass, String server, String apiKey) {
		// Set the variables.
		EMPIRE_NAME = empireName;
		EMPIRE_PASS = empirePass;
		SERVER      = server;
		API_KEY     = apiKey;
		
		JSONObject result = send(false, new String[]{EMPIRE_NAME, EMPIRE_PASS, API_KEY}, "empire", "login");
		//SESSION_ID = JsonParser.getS(result, "session_id");
		
		// Verify that all this works.
		//Toast.makeText(CONTEXT, "Session ID: " + SESSION_ID, Toast.LENGTH_LONG).show();
		
	}
	
	public static JSONObject send(boolean includeSessionId, String[] paramsArray, String module, String method) {
		// Parse the String[] into one string.
		String parsedParams = "[";
		
		for (int i = 0; i < paramsArray.length; i++) {
			parsedParams += "\"" + paramsArray[i] + "\",";
		}
		
		// Remove the comma at the end.
		parsedParams = parsedParams.substring(0, parsedParams.length()-1);
		
		// Close off the params block.
		parsedParams += "]";
		
		Log.d(TAG, parsedParams);
		
		
		// Set the timeout values on the request.
		HttpParams httpParameters = new BasicHttpParams();
		int timeout = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout); // Timeout for connection to be established.
		HttpConnectionParams.setSoTimeout(httpParameters, timeout); // Timeout for data to arrive.
		
		Log.d(TAG, "Created HttpParamaters.");
		
		// Create a new HttpClient and add URL;
	    HttpClient httpclient = new DefaultHttpClient(httpParameters);
	    Log.d(TAG, "Created HTTPClient");
	    
	    HttpPost httppost = new HttpPost("https://" + SERVER + ".lacunaexpanse.com/" + module);
	    Log.d(TAG, "Created httppost variable");

	    try {
	    	/*
	    	String params = "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"" + method + "\",\"params\":\"" + parsedParams + "\"}";													
	        // Add the data to the POST body.
	        ArrayList<NameValuePair> paramPairs = new ArrayList<NameValuePair>(4);
	        paramPairs.add(new BasicNameValuePair("jsonrpc", "2.0"));
	        paramPairs.add(new BasicNameValuePair("id",      "1"));
	        paramPairs.add(new BasicNameValuePair("method",  method));
	        paramPairs.add(new BasicNameValuePair("params",  parsedParams));
	        */
	    	JSONObject params = new JSONObject();
	    	JsonParser.put(params, "jsonrpc", "2.0");
	    	JsonParser.put(params, "id",      "1");
	    	JsonParser.put(params, "method",  method);
	    	JsonParser.put(params, "params",  parsedParams);
	        httppost.setEntity(new ByteArrayEntity(params.toString().getBytes("UTF8")));
	        
	        String test = httppost.getEntity().toString();
	        Log.d(TAG, test);
	        
	        Log.d(TAG, "Created parameters");
	        
	        // Execute HTTP Post Request.
	        HttpResponse serverResponse = httpclient.execute(httppost);
	        
	        Log.d(TAG, "Executed request");
	        
	        // Get the String response.
	        BufferedReader in = new BufferedReader(new InputStreamReader(serverResponse.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			Log.d(TAG, "Response: " + sb.toString());
			JSONObject response = new JSONObject(sb.toString());
			in.close();
			
			// Now check if the server returned a result or an error.
			if (response.has("result")) {
				// If the server doesn't return the result in JSONObject form, just return the
				// whole thing.
				if (method == "login") {
					return response;
				}
				JSONObject result   = JsonParser.getJO(response, "result");
				
				// Store the status in cache.
				JSONObject status = JsonParser.getJO(result, "status");
				STATUS = status;
				
				// Now we just return the result. Nothing fancy. :)
				return result;
			}
			else if (response.has("error")) {
				JSONObject error = JsonParser.getJO(response, "error");
				// I don't know weather I should put this into a separate method or not.
				long code = JsonParser.getL(error, "code");
				String message = JsonParser.getS(error, "message");
				
				Log.d("Lacuna Expanse - Debug", "Error " + code + ": " + message);
				// Fall back to the login screen if the session expired.
				if (code == 1006) {
					// Print the error message to the screen. Inside the context, before it changes.
					Toast.makeText(CONTEXT, message, Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(CONTEXT, Login.class);
					CONTEXT.startActivity(intent);
					return null;
				}
				
				// Print the error message to the screen.
				Log.d(TAG, "Returning null");
				Toast.makeText(CONTEXT, message, Toast.LENGTH_LONG).show();
				return null;
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
	    
	    // So that Eclipse is happy. :)
	    return null;
	}
	
	// For when we need to Toast messages from this class.
	public static boolean setContext(Context context) {
		CONTEXT = context;
		return true;
	}
}