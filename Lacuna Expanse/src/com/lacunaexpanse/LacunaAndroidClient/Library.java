package com.lacunaexpanse.LacunaAndroidClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Library {
	public static String parseParams(String[] target) {
		StringBuilder sbOne = new StringBuilder();
		sbOne.append("[");

		for (int i = 0; i < target.length; i++) {
			sbOne.append("\"" + target[i] + "\",");
		}

		String resultOne = sbOne.toString();
		resultOne = resultOne.substring(0, resultOne.length()-1);

		StringBuilder sbTwo = new StringBuilder(resultOne);
		sbTwo.append("]");
		String result = sbTwo.toString();

		return result;
	}

	public static String assembleGetUrl(String selectedServer,String module,String method,String params) {
		String encodedParams = null;
		try {
			encodedParams = URLEncoder.encode(params,"utf-8");
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		sb.append("http://" + selectedServer + ".lacunaexpanse.com/" + module + "?jsonrpc=2.0&id=1&method=" + method + "&params=" + encodedParams);

		String finalURL = sb.toString();

		return finalURL;
	}

	public static String sendServerRequest(String serverUrl) {
		URI uri = null;
		String receivedData = null;
		String line = null;

		try {
			HttpClient client = new DefaultHttpClient();
			uri = new URI(serverUrl);
			HttpGet request = new HttpGet();
			request.setURI(uri);
			HttpResponse response = client.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}

			receivedData = sb.toString();
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return receivedData;
	}

	public static ProgressDialog loadingDialog(Context context,String message) {
		ProgressDialog loadingDialog = new ProgressDialog(context);
		loadingDialog.setMessage(message);
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		return loadingDialog;
	}

	public static void handleError(Context context,String jsonData,ProgressDialog loadingDialog) {
		try {
			JSONObject jObject = new JSONObject(jsonData);
			JSONObject error = jObject.getJSONObject("error");

			int errorCode = error.getInt("code");
			String errorMessage = error.getString("message");

			loadingDialog.dismiss();
			Toast.makeText(context,"Error " + errorCode + ": " + errorMessage,Toast.LENGTH_LONG).show();
		}
		catch (JSONException ex) {
			loadingDialog.dismiss();
			Toast.makeText(context,"Error interpreting server response: " + ex.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	public static String formatBigNumbers(long bigNumber) {
		
		/*
		
		This stuff doesn't work as Ecipse says that numbers above 100 billion are "Out of int range".
		Need to find a way to fix this...
		
		if(bigNumber >= 100000000000000000 || bigNumber <= -100000000000000000) {
            //101Q
            return bigNumber/1000000000000000 + "Q";
        }
        else if(bigNumber >= 1000000000000000 || bigNumber <= -1000000000000000) {
            //75.3Q
            return bigNumber/100000000000000 / 10 + "Q";
        }
        else if(bigNumber >= 100000000000000 || bigNumber <= -100000000000000) {
            //101T
            return bigNumber/1000000000000) + "T";
        }
        else if(bigNumber >= 1000000000000 || bigNumber <= -1000000000000) {
            //75.3T
            return bigNumber/100000000000 / 10 + "T";
        }
        else if(bigNumber >= 100000000000 || bigNumber <= -100000000000) {
            //101B
            return bigNumber/1000000000 + "B";
        }
        */
		
        if (bigNumber >= 1000000000 || bigNumber <= -1000000000) {
            return bigNumber/100000000 / 10+ "B";
        }
        else if (bigNumber >= 100000000 || bigNumber <= -100000000) {
            return bigNumber/1000000 + "M";
        }
        else if (bigNumber >= 1000000 || bigNumber <= -1000000) {
            return bigNumber/100000 / 10 + "M";
        }
        else if (bigNumber >= 10000 || bigNumber <= -10000) {
            return bigNumber/1000 + "k";
        }
        else {
            return "" + bigNumber;
        }
	}
}