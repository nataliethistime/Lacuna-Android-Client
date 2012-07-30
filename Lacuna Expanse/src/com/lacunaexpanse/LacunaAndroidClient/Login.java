package com.lacunaexpanse.LacunaAndroidClient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Login extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        /*I need help with this...
        *ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.serverOptions, android.R.layout.simple_spinner_item );
        *adapter.setDropDownViewResource(R.layout.selectServer);
        *Spinner spinner = (Spinner) findViewById(R.id.selectServer);
        *spinner.setAdapter(adapter);*/
        		
    }
}
