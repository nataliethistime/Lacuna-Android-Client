package com.lacunaexpanse.LacunaAndroidClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
				// Get entered empire name and password
				EditText empireNameField = (EditText) findViewById(R.id.empireNameField);
				String empireName = empireNameField.getText().toString();
				EditText passWordField = (EditText) findViewById(R.id.passWordField);
				String empirePassword = passWordField.getText().toString();
				
				
				
				if (empireName.length() <= 0) {
					Toast.makeText(getApplicationContext(),"Please enter your empire name",Toast.LENGTH_SHORT).show();
				}
				else if (empirePassword.length() <= 0) {
					Toast.makeText(getApplicationContext(),"Please enter your empire password",Toast.LENGTH_SHORT).show();
				}
				else {
					// Get selected item from Spinner
					Spinner selectServerSpinner = (Spinner) findViewById(R.id.selectServer);
					int indexValue = selectServerSpinner.getSelectedItemPosition();
					
					// Set selectedServer and apiKey based on selected item in Spinner. Server defaults to US1.
					String selectedServer = null;
					String apiKey = null;
					if (indexValue == 0) {
						Toast.makeText(getApplicationContext(),"Please select a server",Toast.LENGTH_SHORT).show();
					}
					else if (indexValue == 1) {
						selectedServer = "us1";
						apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
					}
					else if (indexValue == 2) {
						selectedServer = "pt";
						apiKey = "a6f619a8-1cd7-429b-8fbf-83ede625612c";
					}
					else {
						selectedServer = "us1";
						apiKey = "01420b89-22d4-437f-b355-b99df1f4c8ea";
					}
					
					// Create params and create GET URL.
					String[] paramsBuilder = {empireName,empirePassword,apiKey};
					String params = Library.parseParams(paramsBuilder);
					String serverUrl = Library.assembleGetUrl(selectedServer,"empire","login",params);
					
					// Send to server.
					String serverResponse = Library.sendServerRequest(serverUrl);
					
					// Parse received JSON data
					String sessionId = null;
					try {
						JSONObject jObject = new JSONObject(serverResponse);
						JSONObject result = jObject.getJSONObject("result");
						
						sessionId = result.getString("session_id");
					}
					catch(JSONException e) {
						if (sessionId == null) {
							try {
								JSONObject jObject = new JSONObject(serverResponse);
								JSONObject error = jObject.getJSONObject("error");
							
								int errorCode = error.getInt("code");
								String errorMessage = error.getString("message");
							
								Toast.makeText(getApplicationContext(),"Error " + errorCode + ": " + errorMessage,Toast.LENGTH_SHORT).show();
							}
							catch (JSONException ex) {
								Toast.makeText(getApplicationContext(),"Error interpreting server response: " + ex.toString(),Toast.LENGTH_SHORT).show();
							}
						}
					}
					
					// Need to do this outside the try statement
					if (sessionId != null) {
						// Call the planet view activity, (Which hasn't been created), 
						// passing in the sessionId as a 'parameter'
						Toast.makeText(getApplicationContext(),"Will move on to the planet view now...",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}