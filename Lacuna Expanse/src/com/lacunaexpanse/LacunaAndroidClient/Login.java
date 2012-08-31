package com.lacunaexpanse.LacunaAndroidClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
				ProgressDialog loadingDialog = Library.loadingDialog(Login.this,"Loading...");
				loadingDialog.show();

				// Get entered empire name and password
				EditText empireNameField = (EditText) findViewById(R.id.empireNameField);
				String empireName = empireNameField.getText().toString();
				EditText passWordField = (EditText) findViewById(R.id.passWordField);
				String empirePassword = passWordField.getText().toString();

				// Get selected item from Spinner
				Spinner selectServerSpinner = (Spinner) findViewById(R.id.selectServer);
				int indexValue = selectServerSpinner.getSelectedItemPosition();

				if (empireName.length() <= 0) {
					loadingDialog.dismiss();
					Toast.makeText(Login.this,"Please enter your empire name",Toast.LENGTH_SHORT).show();
				}
				else if (empirePassword.length() <= 0) {
					loadingDialog.dismiss();
					Toast.makeText(Login.this,"Please enter your empire password",Toast.LENGTH_SHORT).show();
					empireNameField.setText("");
				}
				else {
					// Set selectedServer and apiKey based on selected item in Spinner. Server defaults to US1.
					String selectedServer = null;
					String apiKey = null;
					if (indexValue == 0) {
						loadingDialog.dismiss();
						Toast.makeText(Login.this,"Please select a server",Toast.LENGTH_SHORT).show();
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

					// Doing this stops it from crashing randomly when no server is selected
					if (selectedServer != null && apiKey != null) {
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
							Library.handleError(Login.this,serverResponse,loadingDialog);
						}
						loadingDialog.dismiss();

						// Need to do this outside the try statement
						if (sessionId != null) {
							// Load session id and server response into an intent for passing into the next Activity
							Intent intent = new Intent(Login.this,PlanetResourceView.class);
							intent.putExtra("selectedServer", selectedServer);
							intent.putExtra("sessionId", sessionId);
							intent.putExtra("serverResponse", serverResponse); // So we can get the home_planet_id

							// Start PlanetView Activity
							Login.this.startActivity(intent);
							finish();
						}
					}
				}
			}
		});
	}
}