package com.lacunaexpanse.LacunaAndroidClient.lib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	/*
	 * 
	 * This is just a short-cut class so that parsing JSON
	 * without a try-catch statement is possible.
	 * 
	 */
	
	public static JSONArray getJA(JSONObject jObject, String name) {
		JSONArray result = null;
		try {
			result = jObject.getJSONArray(name);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static JSONObject getJO(JSONObject jObject, String name) {
		JSONObject result = null;
		try {
			result = jObject.getJSONObject(name);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static JSONObject getJA(JSONArray jArray, int pos) {
		JSONObject result = null;
		try {
			result = jArray.getJSONObject(pos);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String getS(JSONObject jObject, String name) {
		String result = "";
		try {
			result = jObject.getString(name);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static long getL(JSONObject jObject, String name) {
		long result = 0;
		try {
			result = jObject.getLong(name);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void put(JSONObject jObject, String name, String value) {
		try {
			jObject.put(name, value);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
