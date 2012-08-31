package com.lacunaexpanse.LacunaAndroidClient;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PlanetResourceView extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_resource_view);
        
        final long spinnerReset = System.currentTimeMillis();
        
        ProgressDialog loadingDialog = new ProgressDialog(PlanetResourceView.this);
        loadingDialog.setMessage("Loading...");
        
        final ProgressDialog LOADING_DIALOG = loadingDialog;
        LOADING_DIALOG.show();
        
        String sessionId = null;
        String serverResponseFromPreviousActivity = null;
        String selectedServer = null;
        
        final Bundle EXTRAS = getIntent().getExtras(); 
        
        if (EXTRAS != null) {
        	sessionId = EXTRAS.getString("sessionId");
        	serverResponseFromPreviousActivity = EXTRAS.getString("serverResponse");
        	selectedServer = EXTRAS.getString("selectedServer");
        }
        
        final String SESSION_ID = sessionId;
        final String SERVER_RESPONSE_FROM_PREVIOUS_ACTIVITY = serverResponseFromPreviousActivity;
        final String SELECTED_SERVER = selectedServer;

		// Get home_planet_id
		String homePlanetId = null;
		try {
			JSONObject jObject = new JSONObject(SERVER_RESPONSE_FROM_PREVIOUS_ACTIVITY);
			JSONObject result = jObject.getJSONObject("result");
			JSONObject status = result.getJSONObject("status");
			JSONObject empire = status.getJSONObject("empire");

			homePlanetId = empire.getString("home_planet_id");
		}
		catch (JSONException e) {
			LOADING_DIALOG.dismiss();
			Library.handleError(PlanetResourceView.this, SERVER_RESPONSE_FROM_PREVIOUS_ACTIVITY, LOADING_DIALOG);
		}
		final String HOME_PLANET_ID = homePlanetId;
		
		String serverResponse = refreshResources(SESSION_ID,HOME_PLANET_ID,SELECTED_SERVER);


		// Parse the JSON for getting the list of planets
		Object[] planetIds = null;
		try {
			JSONObject jObject = new JSONObject(SERVER_RESPONSE_FROM_PREVIOUS_ACTIVITY);
			JSONObject result = jObject.getJSONObject("result");
			JSONObject status = result.getJSONObject("status");
			JSONObject empire = status.getJSONObject("empire");
			JSONObject planets = empire.getJSONObject("planets");

			ArrayList<String> arrayOne = new ArrayList<String>();
			ArrayList<String> arrayTwo = new ArrayList<String>();
	        Iterator<?> iter = planets.keys();

	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        Spinner selectPlanetSpinner = (Spinner) findViewById(R.id.selectPlanet);

	        while(iter.hasNext()){
	            String key = (String)iter.next();
	            String value = planets.getString(key);

	            arrayTwo.add(key);
	            arrayOne.add(value);
	        }

	        Object[] planetNames = arrayOne.toArray();
			/*Object[]*/ planetIds = arrayTwo.toArray();

	        for (int i = 0; i < planetNames.length; i++) {
	        	adapter.add(planetNames[i].toString());
	        }

	        selectPlanetSpinner.setAdapter(adapter);

		}
		catch (JSONException e) {
			LOADING_DIALOG.dismiss();
			Library.handleError(PlanetResourceView.this, serverResponse, LOADING_DIALOG);
		}
		LOADING_DIALOG.dismiss();

		final Object[] PLANET_IDS = planetIds;

		Spinner spinner = (Spinner) findViewById(R.id.selectPlanet);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,View view,int pos,long id) {
				if (System.currentTimeMillis() - spinnerReset < 2500) {
					//Nothing!
				}
				else if (System.currentTimeMillis() - spinnerReset > 2500) {
					String selectedBodyId = PLANET_IDS[pos].toString();

					refreshResources(SESSION_ID,selectedBodyId,SELECTED_SERVER);
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {
				// Nothing!
			}
		});

		// Handle clicking of the "Logout" button.
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LOADING_DIALOG.show();

				String[] paramsBuilder = {SESSION_ID};
				String params = Library.parseParams(paramsBuilder);
				String serverUrl = Library.assembleGetUrl(SELECTED_SERVER, "empire", "logout", params);

				String serverResponse = Library.sendServerRequest(serverUrl);

				//Parse JSON
				int result = 0;
				try {
					JSONObject jObject = new JSONObject(serverResponse);
					result = jObject.getInt("result");
				}
				catch (JSONException e) {
					LOADING_DIALOG.dismiss();
					Library.handleError(PlanetResourceView.this, serverResponse, LOADING_DIALOG);
				}

				if (result == 1) {
					LOADING_DIALOG.dismiss();
					Intent intent = new Intent(PlanetResourceView.this,Login.class);
					PlanetResourceView.this.startActivity(intent);
					finish();
				}
				else {
					LOADING_DIALOG.dismiss();
					Toast.makeText(PlanetResourceView.this, "Something stupido has happened while the logout request was being made...", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		Button viewBuildingsButton = (Button) findViewById(R.id.viewPlanetButton);
		viewBuildingsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Spinner spinner = (Spinner) findViewById(R.id.selectPlanet);
				int selectedIndex = spinner.getSelectedItemPosition();
				
				Intent intent = new Intent(PlanetResourceView.this,PlanetBuildingsView.class);
				intent.putExtra("selectedServer", SELECTED_SERVER);
				intent.putExtra("sessionId", SESSION_ID);
				intent.putExtra("selectedBodyId", PLANET_IDS[selectedIndex].toString());
				PlanetResourceView.this.startActivity(intent);
			}
		});
    }
    
    public String refreshResources(String sessionId,String homePlanetId,String selectedServer) {
    	ProgressDialog loadingDialog = new ProgressDialog(PlanetResourceView.this);
        loadingDialog.setMessage("Loading...");
        
        final ProgressDialog LOADING_DIALOG = loadingDialog;
        LOADING_DIALOG.show();
    	
    	String[] paramsBuilder = {sessionId,homePlanetId};
		String params = Library.parseParams(paramsBuilder);
		String serverUrl = Library.assembleGetUrl(selectedServer, "body", "get_status", params);

		String serverResponse = Library.sendServerRequest(serverUrl);

		TextView planetNameOutput = (TextView) findViewById(R.id.planetName);
		TextView foodInformationOutput = (TextView) findViewById(R.id.foodProductionInfo);
		TextView oreInformationOutput = (TextView) findViewById(R.id.oreProductionInfo);
		TextView waterInformationOutput = (TextView) findViewById(R.id.waterProductionInfo);
		TextView energyInformationOutput = (TextView) findViewById(R.id.energyProductionInfo);
		TextView wasteInformationOutput = (TextView) findViewById(R.id.wasteProductionInfo);

		// Parse JSON from server getting production/resource information
		long foodProduction   = 0;
		long foodStorage      = 0;
		long foodStored       = 0;

		long oreProduction    = 0;
		long oreStorage       = 0;
		long oreStored        = 0;

		long waterProduction  = 0;
		long waterStorage     = 0;
		long waterStored      = 0;

		long energyProduction = 0;
		long energyStorage    = 0;
		long energyStored     = 0;

		long wasteProduction  = 0;
		long wasteStorage     = 0;
		long wasteStored      = 0;

		String planetName       = null;
		try {
			JSONObject jObject = new JSONObject(serverResponse);
			JSONObject result = jObject.getJSONObject("result");
			JSONObject body = result.getJSONObject("body");

			// Get food information
			foodProduction = body.getLong("food_hour");
			foodStorage = body.getLong("food_capacity");
			foodStored = body.getLong("food_stored");

			// Get ore information
			oreProduction = body.getLong("ore_hour");
			oreStorage = body.getLong("ore_capacity");
			oreStored = body.getLong("ore_stored");

			// Get water information
			waterProduction = body.getLong("water_hour");
			waterStorage = body.getLong("water_capacity");
			waterStored = body.getLong("water_stored");

			// Get energy information
			energyProduction = body.getLong("energy_hour");
			energyStorage = body.getLong("energy_capacity");
			energyStored = body.getLong("energy_stored");

			// Get waste information
			wasteProduction = body.getLong("waste_hour");
			wasteStorage = body.getLong("waste_capacity");
			wasteStored = body.getLong("waste_stored");

			planetName = body.getString("name");
		}
		catch (JSONException e) {
			LOADING_DIALOG.dismiss();
			Library.handleError(PlanetResourceView.this, serverResponse, LOADING_DIALOG);
		}
		
		// Organize the big numbers the server has returned
		String miniFoodStored = Library.formatBigNumbers(foodStored);
		String miniFoodStorage = Library.formatBigNumbers(foodStorage);
		String miniFoodProduction = Library.formatBigNumbers(foodProduction);
		
		String miniOreStored = Library.formatBigNumbers(oreStored);
		String miniOreStorage = Library.formatBigNumbers(oreStorage);
		String miniOreProduction = Library.formatBigNumbers(oreProduction);
		
		String miniWaterStored = Library.formatBigNumbers(waterStored);
		String miniWaterStorage = Library.formatBigNumbers(waterStorage);
		String miniWaterProduction = Library.formatBigNumbers(waterProduction);
		
		String miniEnergyStored = Library.formatBigNumbers(energyStored);
		String miniEnergyStorage = Library.formatBigNumbers(energyStorage);
		String miniEnergyProduction = Library.formatBigNumbers(energyProduction);
		
		String miniWasteStored = Library.formatBigNumbers(wasteStored);
		String miniWasteStorage = Library.formatBigNumbers(wasteStorage);
		String miniWasteProduction = Library.formatBigNumbers(wasteProduction);
		
		planetNameOutput.setText("Planet: " + planetName);
		foodInformationOutput.setText("Food: " + miniFoodStored + "/" + miniFoodStorage + " @ " + miniFoodProduction + "/hr");
		oreInformationOutput.setText("Ore: " + miniOreStored + "/" + miniOreStorage + " @ " + miniOreProduction + "/hr");
		waterInformationOutput.setText("Water: " + miniWaterStored + "/" + miniWaterStorage + " @ " + miniWaterProduction + "/hr");
		energyInformationOutput.setText("Energy: " + miniEnergyStored + "/" + miniEnergyStorage + " @ " + miniEnergyProduction + "/hr");
		wasteInformationOutput.setText("Waste: " + miniWasteStored + "/" + miniWasteStorage + " @ " + miniWasteProduction + "/hr");
		
		Button viewBuildingsButton = (Button) findViewById(R.id.viewPlanetButton);
		viewBuildingsButton.setText("View Buildings on " + planetName);

		LOADING_DIALOG.dismiss();

		//For when the planets spinner needs updating
		return serverResponse;
    }
}