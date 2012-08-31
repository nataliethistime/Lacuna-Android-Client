package com.lacunaexpanse.LacunaAndroidClient;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PlanetBuildingsView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_buildings_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_planet_buildings_view, menu);
        return true;
    }
}
