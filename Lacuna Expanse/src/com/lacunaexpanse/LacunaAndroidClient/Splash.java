/*
 * TODO:
 * 		-Get a higher resolution image. The current one is sometimes not high enough.
 */

package com.lacunaexpanse.LacunaAndroidClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Create and start the Thread which sleeps and starts the Login Activity.
        Thread splashTimer = new Thread(new Runnable() {
        	public void run() {
        		try {
        			Thread.sleep(5000);
        		}
        		catch(InterruptedException e) {
        			e.printStackTrace();
        		}
        		finally {
                	Intent intent = new Intent(Splash.this, Login.class);
                	
                	Splash.this.startActivity(intent);
                	finish();
                }
        	}
        });
        
        splashTimer.start();
    }
}
