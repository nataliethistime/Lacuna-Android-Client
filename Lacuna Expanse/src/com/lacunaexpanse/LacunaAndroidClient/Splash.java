package com.lacunaexpanse.LacunaAndroidClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Sleep for 5 seconds.
        try {
        	Thread.sleep(5000);
        }
        catch (InterruptedException e) {
        	e.printStackTrace();
        }
        // Start the Login Activity.
        finally {
        	Intent intent = new Intent(Splash.this, Login.class);
        	
        	Splash.this.startActivity(intent);
        	finish();
        }
    }
}
