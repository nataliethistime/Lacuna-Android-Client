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
import org.json.JSONArray;
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
		
		if (result != null) {
			SESSION_ID = JsonParser.getS(result, "session_id");
		}
		else {
			SESSION_ID = null;
		}
	}
	
	public static JSONObject send(boolean includeSessionId, String[] paramsArray, String module, String method) {
		
		JSONArray paramsJA = new JSONArray();
		
		// If needed, add the session Id.
		if (includeSessionId == true) {
			paramsJA.put(SESSION_ID);
		}
		
		// Put the rest of the params.
		for (int i = 0; i < paramsArray.length; i++) {
			paramsJA.put(paramsArray[i]);
		}
		
		// Finish it off.
		String parsedParams = paramsJA.toString();
		
		// Set the timeout values on the request.
		HttpParams httpParameters = new BasicHttpParams();
		int timeout = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout); // Timeout for connection to be established.
		HttpConnectionParams.setSoTimeout(httpParameters, timeout); // Timeout for data to arrive.
		
		// Create a new HttpClient and add URL;
	    HttpClient httpclient = new DefaultHttpClient(httpParameters);
	    HttpPost httppost = new HttpPost("https://" + SERVER + ".lacunaexpanse.com/" + module);

	    try {
	    	// Building the JSON String manually seems like the best way to do this.
	    	String params = "{\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"" + method + "\",\"params\":" + parsedParams + "}";
	        httppost.setEntity(new ByteArrayEntity(params.getBytes("UTF8")));
	        
	        // Execute HTTP Post Request.
	        HttpResponse serverResponse = httpclient.execute(httppost);
	        
	        // Get the String response.
	        BufferedReader in = new BufferedReader(
	        		new InputStreamReader(
	        				serverResponse.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			JSONObject response = new JSONObject(sb.toString());
			in.close();
			
			// Log the response.
			Log.d("Lacuna Expanse - Debug", response.toString());
			
			// Now check if the server returned a result or an error.
			if (response.has("result")) {
				// If the server doesn't return the result in JSONObject form, just return the
				// whole thing.
				if (method == "logout") {
					return response;
				}
				
				JSONObject result   = JsonParser.getJO(response, "result");
				
				// If we're calling get_status() directly, the result won't have a separate status
				// block.
				if (method == "get_status") {
					STATUS = result;
				}
				else {
					JSONObject status = JsonParser.getJO(result, "status");
					STATUS = status;
				}
				
				// Now we just return the result. Nothing fancy. :)
				return result;
			}
			else if (response.has("error")) {
				JSONObject error = JsonParser.getJO(response, "error");
				// I don't know weather I should put this into a separate method or not.
				long code = JsonParser.getL(error, "code");
				String message = JsonParser.getS(error, "message");
				
				// Fall back to the login screen if the session expired.
				if (code == 1006) {
					// Print the error message to the screen. Inside the context, before it changes.
					Toast.makeText(CONTEXT, message, Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(CONTEXT, Login.class);
					CONTEXT.startActivity(intent);
					return null;
				}
				
				// Print the error message to the screen.
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