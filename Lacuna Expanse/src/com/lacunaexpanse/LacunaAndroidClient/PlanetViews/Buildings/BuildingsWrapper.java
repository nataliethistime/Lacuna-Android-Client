package com.lacunaexpanse.LacunaAndroidClient.PlanetViews.Buildings;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.lacunaexpanse.LacunaAndroidClient.R;
import com.lacunaexpanse.LacunaAndroidClient.lib.Client;
import com.lacunaexpanse.LacunaAndroidClient.lib.JsonParser;
import com.lacunaexpanse.LacunaAndroidClient.lib.Library;

public class BuildingsWrapper extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings_wrapper);
        
        final Bundle EXTRAS = getIntent().getExtras();
        String url = "";
        String id  = "";
        if (EXTRAS != null) {
        	url = EXTRAS.getString("url");
        	id  = EXTRAS.getString("id");
        }
        
        final String URL = url;
        final String ID  = id;
        
        Client.setContext(BuildingsWrapper.this);
        JSONObject result = Client.send(new String[]{ID}, URL, "view");
        
        if (result != null) {
        	
        	// Get all the production numbers.
        	JSONObject building = JsonParser.getJO(result, "building");
        	
        	String foodHourBuilding   = Library.formatBigNumbers(JsonParser.getL(building, "food_hour"));
        	String oreHourBuilding    = Library.formatBigNumbers(JsonParser.getL(building, "ore_hour"));
        	String waterHourBuilding  = Library.formatBigNumbers(JsonParser.getL(building, "water_hour"));
        	String energyHourBuilding = Library.formatBigNumbers(JsonParser.getL(building, "energy_hour"));
        	String wasteHourBuilding  = Library.formatBigNumbers(JsonParser.getL(building, "waste_hour"));
        	
        	JSONObject status = JsonParser.getJO(result, "status");
        	JSONObject body   = JsonParser.getJO(status, "body");
        	
        	JSONObject upgrade    = JsonParser.getJO(building, "upgrade");
        	JSONObject production = JsonParser.getJO(upgrade, "production");
        	
        	long rawUpgradeFoodHourBuilding   = JsonParser.getL(production, "food_hour");
        	long rawUpgradeOreHourBuilding    = JsonParser.getL(production, "ore_hour");
        	long rawUpgradeWaterHourBuilding  = JsonParser.getL(production, "water_hour");
        	long rawUpgradeEnergyHourBuilding = JsonParser.getL(production, "energy_hour");
        	long rawUpgradeWasteHourBuilding  = JsonParser.getL(production, "waste_hour");
        	
        	JSONObject cost = JsonParser.getJO(upgrade, "cost");
        	
        	String upgradeFoodHourBuilding   = Library.formatBigNumbers(rawUpgradeFoodHourBuilding);
        	String upgradeOreHourBuilding    = Library.formatBigNumbers(rawUpgradeOreHourBuilding);
        	String upgradeWaterHourBuilding  = Library.formatBigNumbers(rawUpgradeWaterHourBuilding);
        	String upgradeEnergyHourBuilding = Library.formatBigNumbers(rawUpgradeEnergyHourBuilding);
        	String upgradeWasteHourBuilding = Library.formatBigNumbers(rawUpgradeWasteHourBuilding);
        	
        	long rawFoodHourPlanet   = JsonParser.getL(body, "food_hour");
        	long rawOreHourPlanet    = JsonParser.getL(body, "ore_hour");
        	long rawWaterHourPlanet  = JsonParser.getL(body, "water_hour");
        	long rawEnergyHourPlanet = JsonParser.getL(body, "energy_hour");
        	
        	long rawPlanetFoodStorage   = JsonParser.getL(body, "food_stored");
        	long rawPlanetOreStorage    = JsonParser.getL(body, "ore_stored");
        	long rawPlanetWaterStorage  = JsonParser.getL(body, "water_stored");
        	long rawPlanetEnergyStorage = JsonParser.getL(body, "energy_stored");
        	
        	long rawFoodCost   = JsonParser.getL(cost, "food");
        	long rawOreCost    = JsonParser.getL(cost, "ore");
        	long rawWaterCost  = JsonParser.getL(cost, "water");
        	long rawEnergyCost = JsonParser.getL(cost, "energy");
        	long rawWasteCost  = JsonParser.getL(cost, "waste");
        	
        	String foodCost = Library.formatBigNumbers(rawFoodCost);
        	String oreCost = Library.formatBigNumbers(rawOreCost);
        	String waterCost = Library.formatBigNumbers(rawWaterCost);
        	String energyCost = Library.formatBigNumbers(rawEnergyCost);
        	String wasteCost = Library.formatBigNumbers(rawWasteCost);
        	
        	// Find the TextViews
        	TextView foodHourCurrent    = (TextView) findViewById(R.id.foodHourCurrent);
        	TextView oreHourCurrent     = (TextView) findViewById(R.id.oreHourCurrent);
        	TextView waterHourCurrent   = (TextView) findViewById(R.id.waterHourCurrent);
        	TextView energyHourCurrent  = (TextView) findViewById(R.id.energyHourCurrent);
        	TextView wasteHourCurrent   = (TextView) findViewById(R.id.wasteHourCurrent);
        	
        	TextView foodHourUpgrade    = (TextView) findViewById(R.id.foodHourUpgrade);
        	TextView oreHourUpgrade     = (TextView) findViewById(R.id.oreHourUpgrade);
        	TextView waterHourUpgrade   = (TextView) findViewById(R.id.waterHourUpgrade);
        	TextView energyHourUpgrade  = (TextView) findViewById(R.id.energyHourUpgrade);
        	TextView wasteHourUpgrade   = (TextView) findViewById(R.id.wasteHourUpgrade);
        	
        	TextView foodCostView   = (TextView) findViewById(R.id.foodHourCost);
        	TextView oreCostView    = (TextView) findViewById(R.id.oreHourCost);
        	TextView waterCostView  = (TextView) findViewById(R.id.waterHourCost);
        	TextView energyCostView = (TextView) findViewById(R.id.energyHourCost);
        	TextView wasteCostView = (TextView) findViewById(R.id.wasteHourCost);
        	
        	foodHourCurrent.setText(foodHourBuilding + "/hr");
        	oreHourCurrent.setText(oreHourBuilding + "/hr");
        	waterHourCurrent.setText(waterHourBuilding + "/hr");
        	energyHourCurrent.setText(energyHourBuilding + "/hr");
        	wasteHourCurrent.setText(wasteHourBuilding + "/hr");
        	
        	foodHourUpgrade.setText(upgradeFoodHourBuilding + "/hr");
        	oreHourUpgrade.setText(upgradeOreHourBuilding + "/hr");
        	waterHourUpgrade.setText(upgradeWaterHourBuilding + "/hr");
        	energyHourUpgrade.setText(upgradeEnergyHourBuilding + "/hr");
        	wasteHourUpgrade.setText(upgradeWasteHourBuilding + "/hr");
        	
        	foodCostView.setText(foodCost);
        	oreCostView.setText(oreCost);
        	waterCostView.setText(waterCost);
        	energyCostView.setText(energyCost);
        	wasteCostView.setText(wasteCost);
        	
        	
        	boolean canUpgrade = true;
        	String message     = "yOu fAil!";
        	if (rawFoodHourPlanet - rawUpgradeFoodHourBuilding <= 0) {
        		foodHourUpgrade.setTextColor(Color.RED);
        		
        		canUpgrade = false;
        		message    = "Not enough Food production to upgrade.";
        	}
        	if (rawOreHourPlanet - rawUpgradeOreHourBuilding <= 0) {
        		oreHourUpgrade.setTextColor(Color.RED);
        		
        		canUpgrade = false;
        		message    = "Not enough Ore production to upgrade.";
        	}
        	if (rawWaterHourPlanet - rawUpgradeWaterHourBuilding <= 0) {
        		waterHourUpgrade.setTextColor(Color.RED);

        		canUpgrade = false;
        		message    = "Not enough Water production to upgrade.";
        	}
        	if (rawEnergyHourPlanet - rawUpgradeEnergyHourBuilding <= 0) {
        		energyHourUpgrade.setTextColor(Color.RED);

        		canUpgrade = false;
        		message    = "Not enough Energy production to upgrade.";
        	}
        	if (rawPlanetFoodStorage - rawFoodCost < 0) {
        		foodCostView.setTextColor(Color.RED);
        		
        		canUpgrade = false;
        		message    = "Not enough Food in storage to upgrade.";
        	}
        	if (rawPlanetOreStorage - rawOreCost < 0) {
        		oreCostView.setTextColor(Color.RED);

        		canUpgrade = false;
        		message    = "Not enough Ore in storage to upgrade.";
        	}
        	if (rawPlanetWaterStorage - rawWaterCost < 0) {
        		waterCostView.setTextColor(Color.RED);

        		canUpgrade = false;
        		message    = "Not enough Water in storage to upgrade.";
        	}
        	if (rawPlanetEnergyStorage - rawEnergyCost < 0) {
        		energyCostView.setTextColor(Color.RED);

        		canUpgrade = false;
        		message    = "Not enough Energy in storage to upgrade.";
        	}
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        }
    }
}
