package com.lacunaexpanse.LacunaAndroidClient.Mail;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lacunaexpanse.LacunaAndroidClient.R;

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
        
        TextView messageBody = (TextView) findViewById(R.id.messageBody);
        messageBody.setText(mailId);
    }
}
