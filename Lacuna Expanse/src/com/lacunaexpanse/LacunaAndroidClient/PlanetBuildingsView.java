package com.lacunaexpanse.LacunaAndroidClient;

import android.app.Activity;
import android.os.Bundle;

public class PlanetBuildingsView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_buildings_view);
        
        final Bundle EXTRAS = getIntent().getExtras();
        
        String sessionId = null;
        String selectedServer = null;
        String selectedBodyId = null;
        if (EXTRAS != null) {
        	sessionId = EXTRAS.getString("sessionId");
        	selectedServer = EXTRAS.getString("selectedServer");
        	selectedBodyId = EXTRAS.getString("selectedBodyId");
        }
        
        //Declare our finals
        final String SESSION_ID = sessionId;
        final String SELECTED_SERVER = selectedServer;
        final String SELECTED_BODY_ID = selectedBodyId;
    }
    /*
     * The plan from here on out, for the moment, 
     * is to do a lot of code cleanup and make things more streamlined before moving on to other things.
     */
}