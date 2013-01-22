package com.lacunaexpanse.LacunaAndroidClient.Mail;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lacunaexpanse.LacunaAndroidClient.R;
import com.lacunaexpanse.LacunaAndroidClient.lib.Client;
import com.lacunaexpanse.LacunaAndroidClient.lib.JsonParser;

public class ViewMail extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mail);
        
        final Bundle EXTRAS = getIntent().getExtras();
        String mailId = "";
        if (EXTRAS != null) {
        	mailId = EXTRAS.getString("mailId");
        }
        
        Client.setContext(ViewMail.this);
        JSONObject result = Client.send(true, new String[]{mailId}, "inbox", "read_message");
        
        if (result != null) {
        	JSONObject message = JsonParser.getJO(result, "message");
        	String messageText = JsonParser.getS(message, "body");
        	
        	TextView messageBody = (TextView) findViewById(R.id.messageBody);
        	messageBody.setText(messageText);
        } 
    }
}
