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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button loginButton = (Button) findViewById(R.id.loginButton);

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Get entered empire name
				EditText empireNameField = (EditText) findViewById(R.id.empireNameField);
				String empireName = empireNameField.getText().toString();

				EditText passWordField = (EditText) findViewById(R.id.passWordField);
				String empirePassword = passWordField.getText().toString();
				
				// Get selected item from Spinner
				Spinner MySpinner = (Spinner) findViewById(R.id.selectServer);
				int indexValue = MySpinner.getSelectedItemPosition();
				
				if (empireName.length() <= 0) {
					toast("Please enter your empire name",Toast.LENGTH_SHORT);
				}
				else if (empirePassword.length() <= 0) {
					toast("Please enter your empire password",Toast.LENGTH_SHORT);
				}
				else {
					// Set selectedServer and apiKey based on selected item in Spinner. Server defaults to US1
					String selectedServer = "";
					String apiKey = "";
					if (indexValue == 0) {
						toast("Please select a server",Toast.LENGTH_SHORT);
					}
					else if (indexValue == 1) {
						selectedServer = "us1";
						apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
						toast("US1",Toast.LENGTH_SHORT);
					}
					else if (indexValue == 2) {
						selectedServer = "pt";
						apiKey = "a6f619a8-1cd7-429b-8fbf-83ede625612c";
						toast("PT",Toast.LENGTH_SHORT);
					}
					else {
						selectedServer = "us1";
						apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
						toast("US1",Toast.LENGTH_SHORT);
					}
					
					// Create params and create GET url
					String[] paramsBuilder = {empireName,empirePassword,apiKey};
					String serverUrl = assembleGetUrl(selectedServer,"empire","login",convertToString(paramsBuilder));
					
					// Send to server and toast result
					String serverResponse = sendServerRequest(serverUrl);
					toast(serverResponse,Toast.LENGTH_LONG);
					
					
					
					// "What's next?" I hear you ask, parsing JSON and handling errors...
				}
			}
		});

	}

	
	// Extra methods I don't know how to put in a separate class...
	private void toast(String message, int duration) {
		Context context = getApplicationContext();
		CharSequence text = message;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	private static String convertToString(String[] target) {
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
	
	private static String assembleGetUrl(String selectedServer,String module,String method,String params) {
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
	private String sendServerRequest(String serverUrl) {
		URI uri = null;
		String receivedData = null;
		
		try {
			HttpClient client = new DefaultHttpClient();
			uri = new URI(serverUrl);
			HttpGet request = new HttpGet();
			request.setURI(uri);
			HttpResponse response = client.execute(request);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			
			String line = "";
			
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			receivedData = sb.toString();
			in.close();
		}
		catch (Exception e) {
			toast("Error communicating with the server: " + e.toString(),Toast.LENGTH_LONG);
		}
		
		return receivedData;
	}
}