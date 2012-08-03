/*PLEASE NOTE:
 *This is a test to see if JSONRPC 2.0 requests (And the rest) will work!
 *It will (Most likely) end up changing or even re-written.
 * Also, there will probably be a lot of code that has been commented out, put simply, I use a lot of trial and error...
 */

package com.lacunaexpanse.LacunaAndroidClient;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thetransactioncompany.jsonrpc2.client.*;
import com.thetransactioncompany.jsonrpc2.*;
import net.minidev.json.*;
import java.net.*;

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
					
					URL serverURL = null;
					String selectedServer = "us1";
					
					try {
						serverURL = new URL("http://" + selectedServer + ".lacunaexpanse.com/empire");// Not finished yet
					}
					catch (MalformedURLException e) {
						toast("Error connecting to server: " + e.toString(),Toast.LENGTH_LONG);
					}
					String apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
					
					// Construct new HTTP POST request
					String jsonrpc = "2.0";
					String id = "1";
					String method = "login";
					String[] paramsBuilder = {empireName,empirePassword,apiKey};
					String params = convertToString(paramsBuilder);
					
					toast(params,Toast.LENGTH_LONG);
					
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
					
					JSONRPC2Session serverSession = new JSONRPC2Session(serverURL);
					JSONRPC2Request request = new JSONRPC2Request(method,params);
					
					// Send request
					JSONRPC2Response response = null;

					try {
						response = serverSession.send(request);
					}
					catch (JSONRPC2SessionException e) {
						toast("Error in comminication with server: " + e.getMessage().toString(),Toast.LENGTH_SHORT);
					}
					// Print response result / error
					 if (response.indicatesSuccess()) {
						toast(response.getResult().toString(),Toast.LENGTH_LONG);
					 }
					else {
						toast(response.getError().getMessage(),Toast.LENGTH_LONG);
					}
				}
			}
		});

	}

	
	// Extra methods I don't know how to put in a separate class...
	private boolean toast(String message, int duration) {
		Context context = getApplicationContext();
		CharSequence text = message;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

		return true;
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
}