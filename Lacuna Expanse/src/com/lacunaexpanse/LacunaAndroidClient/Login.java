/*PLEASE NOTE:
 *This is a test to see if JSONRPC 2.0 requests (And the rest) will work!
 *It will (Most likely) end up changing or even re-written.
 * Also, there will probably be a lot of code that has been commented out, put simply, I use a lot of trial and error...
 */

package com.lacunaexpanse.LacunaAndroidClient;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
//import java.net.URISyntaxException;
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
import android.widget.Toast;
// import java.net.MalformedURLException;
// import java.net.URL;
// import net.minidev.json.JSONObject;

// import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
// import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
// import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
// import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

public class Login extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Check if we can get the selected item from the spinner on the click
		// of the button.

		Button loginButton = (Button) findViewById(R.id.loginButton);

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Get entered empire name
				EditText empireNameField = (EditText) findViewById(R.id.empireNameField);
				String empireName = empireNameField.getText().toString();

				EditText passWordField = (EditText) findViewById(R.id.passWordField);
				String empirePassword = passWordField.getText().toString();

				/*Not implemented yet:
				if (selectedServer == "Select server") {
					toast("Please select a server",Toast.LENGTH_SHORT);
				}
				else */if (empireName.length() <= 0) {
					toast("Please enter your Empire Name", Toast.LENGTH_SHORT);
				} 
				else if (empirePassword.length() <= 0) { 
					toast("Please enter you empire password", Toast.LENGTH_SHORT);
				} 
				else {
					// Doing this to make sure things are working properly...
					toast("The empire name and password you entered is " + empireName + " " + empirePassword, Toast.LENGTH_LONG);
					
					String selectedServer = "us1";
					String apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
					String[] paramsBuilder = {empireName,empirePassword,apiKey};
					String serverUrl = assembleGetUrl(selectedServer,"empire","login",convertToString(paramsBuilder));
					
					/*Just a few things to note from here on out:
					 * US1 api keys:
					 * Public Key:
					 * 01420b89-22d4-437f-b355-b99df1f4c8ea 
					 * Private Key:
					 * 146abedc-70a3-4671-b0cd-a8abf6bf522f
					 * 
					 * PT api keys:
					 * Public Key:
					 * a6f619a8-1cd7-429b-8fbf-83ede625612c
					 * Private Key:
					 * 42165f49-a948-4192-a86b-28e8a04dfd1e 
					 * 
					 * At the moment I'm going to default the selected server to us1 (And the api key). 
					 * I haven't worked out how to get the selected option from a Spinner
					 */
					
					// Right, let's begin!
					URI uri = null;
					String returnedData = null;
					
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
						
						returnedData = sb.toString();
						in.close();
					}
					catch (Exception e) {
						toast("Error communicating with the server: " + e.toString(),Toast.LENGTH_LONG);
					}
					
					toast(returnedData,Toast.LENGTH_LONG);
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
}