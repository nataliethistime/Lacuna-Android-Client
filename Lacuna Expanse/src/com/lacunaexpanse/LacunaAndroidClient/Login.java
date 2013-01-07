package com.lacunaexpanse.LacunaAndroidClient;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		/*
		final int APP_VERSION = 005;
		final String CURRENT_ONLINE_VERSION_JSON = Library.sendServerRequest("http://www.lacuna-android-client.webs.com/current_version/current_version.txt");
		TextView versionInformation = (TextView) findViewById(R.id.versionInformation);
		
		int CURRENT_ONLINE_VERSION = 0;
		try {
			JSONObject jObject = new JSONObject(CURRENT_ONLINE_VERSION_JSON);
			CURRENT_ONLINE_VERSION = jObject.getInt("version_number");
		}
		catch (JSONException e) {
			versionInformation.setText("Couldn't determine weather Lacuna Expanse needs updating or not: " + e.toString());
		}
			
		if (APP_VERSION == CURRENT_ONLINE_VERSION) {
			versionInformation.setText("Your version of Lacuna Expanse is up-to-date!");
		}
		else {
			versionInformation.setText("Your version of Lacuna Expanse is out of date.\nPlease go to www.lacuna-android-client.webs.com and update the application.");
		}
		*/

		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Get entered empire name and password
				EditText empireNameField = (EditText) findViewById(R.id.empireNameField);
				String empireName = empireNameField.getText().toString();
				EditText passWordField = (EditText) findViewById(R.id.passWordField);
				String empirePassword = passWordField.getText().toString();

				// Get selected item from Spinner
				Spinner selectServerSpinner = (Spinner) findViewById(R.id.selectServer);
				int indexValue = selectServerSpinner.getSelectedItemPosition();

				if (empireName.length() <= 0 || empireName == "") {
					Toast.makeText(Login.this,"Please enter your empire name.",Toast.LENGTH_SHORT).show();
				}
				else if (empirePassword.length() <= 0 || empirePassword == "") {
					Toast.makeText(Login.this,"Please enter your empire password.",Toast.LENGTH_SHORT).show();
				}
				else {
					// Set selectedServer and apiKey based on selected item in Spinner. Server defaults to US1.
					String selectedServer = null;
					String apiKey = null;
					if (indexValue == 0) {
						Toast.makeText(Login.this,"Please select a server.",Toast.LENGTH_SHORT).show();
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
					
					final String SELECTED_SERVER = selectedServer;
					final String API_KEY = apiKey;

					// Doing this stops it from crashing randomly when no server is selected.
					if (selectedServer != null && apiKey != null) {
						
						// Initialize the Client class.
						Client.setContext(Login.this);
						Client.login(empireName, empirePassword, SELECTED_SERVER, API_KEY);
						
						// Get the home planet id.
						//JSONObject status   = Client.STATUS;
						//JSONObject empire   = JsonParser.getJO(status, "empire");
						//String homePlanetId = JsonParser.getS(empire, "home_planet_id");
						// TODO remove debug lines.
						/*
						final String SESSION_ID = Client.SESSION_ID;
						if (SESSION_ID != null) {
							Intent intent = new Intent(Login.this,PlanetResourceView.class);
							intent.putExtra("planetId", homePlanetId);

							Login.this.startActivity(intent);
							finish();
						}
						*/
					}
				}
			}
		});
	}
}