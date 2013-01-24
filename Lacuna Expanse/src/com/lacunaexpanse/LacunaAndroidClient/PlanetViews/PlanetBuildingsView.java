package com.lacunaexpanse.LacunaAndroidClient.PlanetViews;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lacunaexpanse.LacunaAndroidClient.R;
import com.lacunaexpanse.LacunaAndroidClient.PlanetViews.Buildings.BuildingsWrapper;
import com.lacunaexpanse.LacunaAndroidClient.lib.Client;
import com.lacunaexpanse.LacunaAndroidClient.lib.JsonParser;

public class PlanetBuildingsView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_buildings_view);
        
        final Bundle EXTRAS = getIntent().getExtras();
        
        String planetId = null;
        
        if (EXTRAS != null) {
        	planetId = EXTRAS.getString("planetId");
        }
        final String PLANET_ID = planetId;
        
        Client.setContext(PlanetBuildingsView.this);
        JSONObject result         = Client.send(new String[]{PLANET_ID}, "/body", "get_buildings");
        ListView buildingsList    = (ListView) findViewById(R.id.buildings);
        List<String> buildingIds  = new ArrayList<String>();
        List<String> buildingUrls = new ArrayList<String>();
        List<Map<String, String>> buildingList = new ArrayList<Map<String, String>>();
        
        if (result != null) {
        	// Get the JSONArray of buildings first.
        	JSONObject buildings = JsonParser.getJO(result, "buildings");
        	
        	// Set up some variables.
        	Iterator<?> iter                       = buildings.keys();
        	
        	while (iter.hasNext()) {
        		String id           = (String) iter.next();
        		JSONObject building = JsonParser.getJO(buildings, id);
        		
        		String name = JsonParser.getS(building, "name") + " " + JsonParser.getL(building, "level");
        		
        		// Push them into the array.
        		buildingUrls.add(JsonParser.getS(building, "url"));
        		buildingIds.add(id);
        		
        		// Check for damage first as nothing can happen if it's damaged.
        		long efficiency = JsonParser.getL(building, "efficiency");
        		if (efficiency != 100) {
        			Map<String, String> datum = new HashMap<String, String>(2);
        			
        			datum.put("name", name);
        			datum.put("status", efficiency + "% damaged!!");
        			// TODO change this text colour to Red.
        			
        			buildingList.add(datum);
        		}
        		else if (building.has("pending_build")) {
        			JSONObject pendingBuild = JsonParser.getJO(building, "pending_build");
        			String end = JsonParser.getS(pendingBuild, "end");
        			
        			Map<String, String> datum = new HashMap<String, String>(2);
        			datum.put("name", name);
        			datum.put("status", "Building - completes on: " + end);
        			
        			buildingList.add(datum);
        		}
        		else {
        			Map<String, String> datum = new HashMap<String, String>(2);
        			
        			datum.put("name", name);
        			datum.put("status", "");
        			
        			buildingList.add(datum);
        		}
        	}
        }
        
        final Object[] BUILDING_IDS  = buildingIds.toArray();
    	final Object[] BUILDING_URLS = buildingUrls.toArray();
    	
    	SimpleAdapter adapter = new SimpleAdapter(this, buildingList, android.R.layout.simple_list_item_2, new String[]{"name", "status"}, new int[]{android.R.id.text1, android.R.id.text2});
    	buildingsList.setAdapter(adapter);
    	
    	buildingsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> whatThe, View view, int pos, long iHaveNoIdea) {
				String selectedId  = BUILDING_IDS[pos].toString();
				String selectedUrl = BUILDING_URLS[pos].toString();
				
				Intent intent = new Intent(PlanetBuildingsView.this, BuildingsWrapper.class);
				intent.putExtra("id",  selectedId);
				intent.putExtra("url", selectedUrl);
				
				PlanetBuildingsView.this.startActivity(intent);
			}
    	});
    }
    
    public void onResume(Bundle savedInstanceState) {
    	super.onResume();
    	// TODO implement refreshBuidings() 
    	// refreshBuildings();
    }
}