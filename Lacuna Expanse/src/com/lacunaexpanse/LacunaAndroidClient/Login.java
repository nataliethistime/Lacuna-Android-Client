/*PLEASE NOTE:
 *This is a test to see if JSONRPC 2.0 requests (And the rest) will work!
 *It will (Most likely) end up changing or even re-written.
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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

				if (empireName.length() <= 0) {
					toast("Please enter your Empire Name", Toast.LENGTH_SHORT);
				} 
				else if (empirePassword.length() <= 0) { 
					toast("Please enter you empire password", Toast.LENGTH_SHORT);
				} 
				else {
					// Doing this to make sure things are working properly...
					toast("The empire name and password you entered is " + empireName + " " + empirePassword, Toast.LENGTH_LONG);
					
					// Right, let's get started with the server request and whatever...
					String result = "";
					// Don't know what the API key is as yet, need to create one still...
					String apiKey = "";
					
					// Create JSON data for sending
					JSONObject requestBuilder = new JSONObject();
					requestBuilder.put("jsonrpc", "2.0");
					requestBuilder.put("id", "1");
					requestBuilder.put("method","login");
					requestBuilder.put("params","[\"" + empireName + "\",\"" + empirePassword + "\",\"" + apiKey + "\"]");
					
					String request = requestBuilder.toString();
					
					/*ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					
					nameValuePairs.add(new BasicNameValuePair("jsonrpc", "2.0"));
					nameValuePairs.add(new BasicNameValuePair("id", "1"));
					nameValuePairs.add(new BasicNameValuePair("method", "login"));
					nameValuePairs.add(new BasicNameValuePair("params", "[\"" + empireName + "\",\"" + empirePassword + "\",\"" + apiKey + "\"]"));
					*/
					
					// Create and send the HTTP POST request
					try {
						HttpClient client = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost("http://us1.lacunaexpanse.com/empire");
						httpPost.setEntity(new UrlEncodedFormEntity(request));
						HttpResponse response = client.execute(httpPost);
						HttpEntity entity = response.getEntity();
						InputStream is = entity.getContent();
					}
					catch (Exception e) {
						toast("Error in HTTP connection: " + e.toString(), Toast.LENGTH_LONG);
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
}