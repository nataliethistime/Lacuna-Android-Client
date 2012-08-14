package com.lacunaexpanse.LacunaAndroidClient;

import org.json.JSONException;
import org.json.JSONObject;

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
		
		//final static Library library;

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
					String selectedServer = null;
					String apiKey = null;
					if (indexValue == 0) {
						toast("Please select a server",Toast.LENGTH_SHORT);
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
					
					// Create params and create GET URL
					String[] paramsBuilder = {empireName,empirePassword,apiKey};
					String params = Library.parseParams(paramsBuilder);
					String serverUrl = Library.assembleGetUrl(selectedServer,"empire","login",params);
					
					// Send to server and toast result
					String serverResponse = Library.sendServerRequest(serverUrl);
					toast(serverResponse,Toast.LENGTH_LONG);
					
					
					
					// "What's next?" I hear you ask, parsing JSON and handling errors...
					String sessionId= null;
					try {
						JSONObject jObject = new JSONObject(serverResponse);
						JSONObject result = jObject.getJSONObject("result");
						
						sessionId  = result.getString("session_id");
					}
					catch(JSONException e) {
						toast("Error interpereting server response: " + e.toString(),Toast.LENGTH_LONG);
					}
					
					if (sessionId != null) {
						toast("Returned session id is: " + sessionId,Toast.LENGTH_LONG);
					}
					else {
						toast("Something screwed up, returned session id is null",Toast.LENGTH_LONG);
					}
				}
			}
		});
	}

	
	// Toast method which I can't implement in the library
	private void toast(String message, int duration) {
		Context context = getApplicationContext();
		CharSequence text = message;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}