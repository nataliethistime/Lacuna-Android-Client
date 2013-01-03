package com.lacunaexpanse.LacunaAndroidClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
        
        String sessionId = null;
        String selectedServer = null;
        String planetId = null;
        
        final Bundle EXTRAS = getIntent().getExtras(); 
        
        if (EXTRAS != null) {
        	sessionId = EXTRAS.getString("sessionId");
        	selectedServer = EXTRAS.getString("selectedServer");
        	planetId = EXTRAS.getString("planetId");
        }
        
        final String SESSION_ID = sessionId;
        final String PLANET_ID = planetId;
        final String SELECTED_SERVER = selectedServer;
        
		refreshResources(SESSION_ID,PLANET_ID,SELECTED_SERVER);
		
		// Handle clicking of the "Logout" button.
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

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
					Library.handleError(PlanetResourceView.this, serverResponse);
				}

				if (result == 1) {
					Intent intent = new Intent(PlanetResourceView.this,Login.class);
					PlanetResourceView.this.startActivity(intent);
					finish();
				}
				else {
					Toast.makeText(PlanetResourceView.this, "Something stupido has happened while the logout request was being made...", Toast.LENGTH_LONG).show();
				}
			}
		});
    }
    
    public void refreshResources(final String SESSION_ID,final String PLANET_ID,final String SELECTED_SERVER) {
    	String[] paramsBuilder = {SESSION_ID,PLANET_ID};
		String params = Library.parseParams(paramsBuilder);
		String serverUrl = Library.assembleGetUrl(SELECTED_SERVER, "body", "get_status", params);

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

		String planetName     = null;
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
			Library.handleError(PlanetResourceView.this, serverResponse);
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
		
		planetNameOutput.setText(planetName);
		foodInformationOutput.setText("Food: " + miniFoodStored + "/" + miniFoodStorage + " @ " + miniFoodProduction + "/hr");
		oreInformationOutput.setText("Ore: " + miniOreStored + "/" + miniOreStorage + " @ " + miniOreProduction + "/hr");
		waterInformationOutput.setText("Water: " + miniWaterStored + "/" + miniWaterStorage + " @ " + miniWaterProduction + "/hr");
		energyInformationOutput.setText("Energy: " + miniEnergyStored + "/" + miniEnergyStorage + " @ " + miniEnergyProduction + "/hr");
		wasteInformationOutput.setText("Waste: " + miniWasteStored + "/" + miniWasteStorage + " @ " + miniWasteProduction + "/hr");
		
		// Parse the JSON for getting the list of planets
		JSONObject reversedPlanets = new JSONObject();
		try {
			JSONObject jObject = new JSONObject(serverResponse);
			JSONObject result = jObject.getJSONObject("result");
			JSONObject empire = result.getJSONObject("empire");
			JSONObject planets = empire.getJSONObject("planets");
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			Spinner selectPlanetSpinner = (Spinner) findViewById(R.id.selectPlanet);
			
			ArrayList<String> planetNames = new ArrayList<String>();
			planetNames.add("AAAAAAAAAAAAAAAA"); // Make sure this one stays at the top as a placeholder for the "Select Planet" option.
			Iterator<?> iter = planets.keys();
			
			while(iter.hasNext()) {
			    String key = (String) iter.next();
			    String value = planets.getString(key);

			    reversedPlanets.put(value, key);
			    planetNames.add(value);
			}
			Collections.sort(planetNames);
			for (int i = 0; i < planetNames.size(); i++) {
				if (planetNames.get(i) == "AAAAAAAAAAAAAAAA") {
				}
				else {
					adapter.add(planetNames.get(i));
				}
			}
			selectPlanetSpinner.setAdapter(adapter);
		}
		catch (JSONException e) {
			Library.handleError(PlanetResourceView.this, serverResponse);
		}
		
		Button viewBuildingsButton = (Button) findViewById(R.id.viewPlanetButton);
		viewBuildingsButton.setText("View Buildings on " + planetName);
		
		viewBuildingsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlanetResourceView.this,PlanetBuildingsView.class);
				
				intent.putExtra("sessionId", SESSION_ID);
				intent.putExtra("selectedServer", SELECTED_SERVER);
				intent.putExtra("planetId", PLANET_ID);
				
				PlanetResourceView.this.startActivity(intent);
				finish();
			}
		});
		
		final long SPINNER_RESET = System.currentTimeMillis();
		final JSONObject REVERSED_PLANETS = reversedPlanets;
		
		Spinner selectPlanet = (Spinner) findViewById(R.id.selectPlanet);
		selectPlanet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,View view,int pos,long id) {
				if (System.currentTimeMillis() - SPINNER_RESET < 1000) {
					//Nothing!
				}
				else if (System.currentTimeMillis() - SPINNER_RESET > 1000) {
					Spinner selectedPlanetSpinner = (Spinner) findViewById(R.id.selectPlanet);
					String selectedPlanetName = selectedPlanetSpinner.getSelectedItem().toString();
					
					// Just in case something screws up.
					// I don't know weather this'll actually ever get called;
					// It's just here as a precaution.
					if (selectedPlanetName == "Select Planet") {
						Toast.makeText(PlanetResourceView.this, "Please actually select a planet.", Toast.LENGTH_LONG).show();
					}
					else {
						String selectedId = "";
						try {
							selectedId = REVERSED_PLANETS.getString(selectedPlanetName);
						}
						catch (JSONException e) {
							e.printStackTrace();
						}
						refreshResources(SESSION_ID,selectedId,SELECTED_SERVER);
					}
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {
				// Nothing!
			}
		});
    }
}