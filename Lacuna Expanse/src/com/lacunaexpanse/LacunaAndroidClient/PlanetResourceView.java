package com.lacunaexpanse.LacunaAndroidClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
        
        Client.setContext(PlanetResourceView.this);
        
        String planetId = null;
        final Bundle EXTRAS = getIntent().getExtras(); 
        if (EXTRAS != null) {
        	planetId = EXTRAS.getString("planetId");
        }
        
        final String PLANET_ID = planetId;
        
		refreshResources(PLANET_ID);
		
		// Handle clicking of the "Logout" button.
		Button logoutButton = (Button) findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Client.logout();
				
				Intent intent = new Intent(PlanetResourceView.this,Login.class);
				PlanetResourceView.this.startActivity(intent);
				finish();
			}
		});
    }
    
    public void refreshResources(final String planetId) {
		JSONObject result = Client.send(true, new String[]{planetId}, "body", "get_status");
		JSONObject body   = JsonParser.getJO(result, "body");
		
		TextView planetNameOutput = (TextView) findViewById(R.id.planetName);
		TextView foodInformationOutput = (TextView) findViewById(R.id.foodProductionInfo);
		TextView oreInformationOutput = (TextView) findViewById(R.id.oreProductionInfo);
		TextView waterInformationOutput = (TextView) findViewById(R.id.waterProductionInfo);
		TextView energyInformationOutput = (TextView) findViewById(R.id.energyProductionInfo);
		TextView wasteInformationOutput = (TextView) findViewById(R.id.wasteProductionInfo);

		// Parse and round the numbers.
		String foodProduction   = Library.formatBigNumbers(JsonParser.getL(body, "food_hour"));
		String foodStorage      = Library.formatBigNumbers(JsonParser.getL(body, "food_capacity"));
		String foodStored       = Library.formatBigNumbers(JsonParser.getL(body, "food_stored"));

		String oreProduction    = Library.formatBigNumbers(JsonParser.getL(body, "ore_hour"));
		String oreStorage       = Library.formatBigNumbers(JsonParser.getL(body, "ore_capacity"));
		String oreStored        = Library.formatBigNumbers(JsonParser.getL(body, "ore_stored"));

		String waterProduction  = Library.formatBigNumbers(JsonParser.getL(body, "water_hour"));
		String waterStorage     = Library.formatBigNumbers(JsonParser.getL(body, "water_capacity"));
		String waterStored      = Library.formatBigNumbers(JsonParser.getL(body, "water_stored"));

		String energyProduction = Library.formatBigNumbers(JsonParser.getL(body, "energy_hour"));
		String energyStorage    = Library.formatBigNumbers(JsonParser.getL(body, "energy_capacity"));
		String energyStored     = Library.formatBigNumbers(JsonParser.getL(body, "energy_stored"));

		String wasteProduction  = Library.formatBigNumbers(JsonParser.getL(body, "waste_hour"));
		String wasteStorage     = Library.formatBigNumbers(JsonParser.getL(body, "waste_capacity"));
		String wasteStored      = Library.formatBigNumbers(JsonParser.getL(body, "waste_stored"));

		String planetName = JsonParser.getS(body, "name");
		
		planetNameOutput.setText(planetName);
		foodInformationOutput.setText(foodStored + "/" + foodStorage + " @ " + foodProduction + "/hr");
		oreInformationOutput.setText(oreStored + "/" + oreStorage + " @ " + oreProduction + "/hr");
		waterInformationOutput.setText(waterStored + "/" + waterStorage + " @ " + waterProduction + "/hr");
		energyInformationOutput.setText(energyStored + "/" + energyStorage + " @ " + energyProduction + "/hr");
		wasteInformationOutput.setText(wasteStored + "/" + wasteStorage + " @ " + wasteProduction + "/hr");
		
		// Parse the JSON for getting the list of planets
		JSONObject reversedPlanets = new JSONObject();
		JSONObject empire          = JsonParser.getJO(result, "empire");
		JSONObject planets         = JsonParser.getJO(empire, "planets");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner selectPlanetSpinner = (Spinner) findViewById(R.id.selectPlanet);
		
		ArrayList<String> planetNames = new ArrayList<String>();
		planetNames.add("11111AAAAAaaaaa"); // Make sure this one stays at the top as a placeholder for the "Select Planet" option.
		Iterator<?> iter = planets.keys();
		
		while(iter.hasNext()) {
		    String key   = (String) iter.next();
		    String value = JsonParser.getS(planets, key);

		    JsonParser.put(reversedPlanets, value, key);
		    planetNames.add(value);
		}
		
		Collections.sort(planetNames);
		
		for (int i = 0; i < planetNames.size(); i++) {
			if (planetNames.get(i) == "11111AAAAAaaaaa") {
				adapter.add("Select Planet");
			}
			else {
				adapter.add(planetNames.get(i));
			}
		}
		selectPlanetSpinner.setAdapter(adapter);
		
		Button viewBuildingsButton = (Button) findViewById(R.id.viewPlanetButton);
		viewBuildingsButton.setText("View Buildings on " + planetName);
		
		viewBuildingsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlanetResourceView.this, PlanetBuildingsView.class);
				intent.putExtra("planetId", planetId);
				
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
						String selectedId = JsonParser.getS(REVERSED_PLANETS, selectedPlanetName);
						refreshResources(selectedId);
					}
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {
				// Nothing! Amazing, eh?
			}
		});
    }
}