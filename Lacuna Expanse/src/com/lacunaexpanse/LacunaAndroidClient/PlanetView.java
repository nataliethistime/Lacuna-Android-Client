package com.lacunaexpanse.LacunaAndroidClient;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

public class PlanetView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_view);
        
        ProgressDialog loadingDialog = Library.loadingDialog(PlanetView.this, "Loading...");
        loadingDialog.show();
        
        String sessionId = null;
        String serverResponseFromPreviousActivity = null;
        String selectedServer = null;
        Bundle extras = getIntent().getExtras(); 
        if (extras != null) {
        	sessionId = extras.getString("sessionId");
        	serverResponseFromPreviousActivity = extras.getString("serverResponse");
        	selectedServer = extras.getString("selectedServer");
        }
		
		// Get home_planet_id
		String homePlanetId = null;
		try {
			JSONObject jObject = new JSONObject(serverResponseFromPreviousActivity);
			JSONObject result = jObject.getJSONObject("result");
			JSONObject status = result.getJSONObject("status");
			JSONObject empire = status.getJSONObject("empire");
			
			homePlanetId = empire.getString("home_planet_id");
		}
		catch (JSONException e) {
			loadingDialog.dismiss();
		}
		
		String[] paramsBuilder = {sessionId,homePlanetId};
		String params = Library.parseParams(paramsBuilder);
		String serverUrl = Library.assembleGetUrl(selectedServer, "body", "get_buildings", params);
		
		String serverResponse = Library.sendServerRequest(serverUrl);
		
		// Time to parse the big load of bullcrap the server has given us...
		
		
		// For the sake of it, we're going to get the production levels and resource storage levels
		TextView planetNameOutput = (TextView) findViewById(R.id.planetName);
		TextView foodInformationOutput = (TextView) findViewById(R.id.foodProductionInfo);
		TextView oreInformationOutput = (TextView) findViewById(R.id.oreProductionInfo);
		TextView waterInformationOutput = (TextView) findViewById(R.id.waterProductionInfo);
		TextView energyInformationOutput = (TextView) findViewById(R.id.energyProductionInfo);
		TextView wasteInformationOutput = (TextView) findViewById(R.id.wasteProductionInfo);
		
		// Parse JSON from server getting production/resource information
		String foodProduction   = null;
		String foodStorage      = null;
		String foodStored       = null;
		
		String oreProduction    = null;
		String oreStorage       = null;
		String oreStored        = null;
		
		String waterProduction  = null;
		String waterStorage     = null;
		String waterStored      = null;
		
		String energyProduction = null;
		String energyStorage    = null;
		String energyStored     = null;
		
		String wasteProduction  = null;
		String wasteStorage     = null;
		String wasteStored      = null;
		
		String planetName = null;
		try {
			JSONObject jObject = new JSONObject(serverResponse);
			JSONObject result = jObject.getJSONObject("result");
			JSONObject status = result.getJSONObject("status");
			JSONObject body = status.getJSONObject("body");
			
			// Get food information
			foodProduction = body.getString("food_hour");
			foodStorage = body.getString("food_capacity");
			foodStored = body.getString("food_stored");
			
			// Get ore information
			oreProduction = body.getString("ore_hour");
			oreStorage = body.getString("ore_capacity");
			oreStored = body.getString("ore_stored");
			
			// Get water information
			waterProduction = body.getString("water_hour");
			waterStorage = body.getString("water_capacity");
			waterStored = body.getString("water_stored");
			
			// Get energy information
			energyProduction = body.getString("energy_hour");
			energyStorage = body.getString("energy_capacity");
			energyStored = body.getString("energy_stored");
			
			// Get waste information
			wasteProduction = body.getString("waste_hour");
			wasteStorage = body.getString("waste_capacity");
			wasteStored = body.getString("waste_stored");
			
			planetName = body.getString("name");
		}
		catch (JSONException e) {
			loadingDialog.dismiss();
			Library.handleError(PlanetView.this, serverResponse, loadingDialog);
		}
		
		planetNameOutput.setText("Planet: " + planetName);
		foodInformationOutput.setText("Food: " + foodStored + "/" + foodStorage + " @ " + foodProduction + "/hr");
		oreInformationOutput.setText("Ore: " + oreStored + "/" + oreStorage + " @ " + oreProduction + "/hr");
		waterInformationOutput.setText("Water: " + waterStored + "/" + waterStorage + " @ " + waterProduction + "/hr");
		energyInformationOutput.setText("Energy: " + energyStored + "/" + energyStorage + " @ " + energyProduction + "/hr");
		wasteInformationOutput.setText("Waste: " + wasteStored + "/" + wasteStorage + " @ " + wasteProduction + "/hr");
		
		loadingDialog.dismiss();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
    }
}