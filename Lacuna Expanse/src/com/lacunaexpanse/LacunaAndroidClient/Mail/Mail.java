package com.lacunaexpanse.LacunaAndroidClient.Mail;

// TODO Create refreshMail() method.
// TODO TEST!!

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lacunaexpanse.LacunaAndroidClient.R;
import com.lacunaexpanse.LacunaAndroidClient.lib.Client;
import com.lacunaexpanse.LacunaAndroidClient.lib.JsonParser;

public class Mail extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        
        // This seems like the simplest way to build the hash that the server requires.
        JSONArray hashOptions = new JSONArray();
        hashOptions.put("1");
        
        Client.setContext(Mail.this);
        JSONObject result = Client.send(true, new String[]{hashOptions.toString()}, "inbox", "view_inbox");
        Log.d("Lacuna Expanse - Debug", result.toString());
        
        JSONArray messages = JsonParser.getJA(result, "messages");
        List<Map<String, String>> mailList = new ArrayList<Map<String, String>>();
        String[] mailIds = {};
        
        for (int i = 0; i < messages.length(); i++) {
        	JSONObject message = JsonParser.getJA(messages, i);
        	
        	
        	Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("from", JsonParser.getS(message, "from"));
            datum.put("subject", JsonParser.getS(message, "subject"));
            mailList.add(datum);
        	
            mailIds[i] = JsonParser.getS(message, "id");
        }
        
        final String[] MAIL_IDS = mailIds;
        
        ListView mails = (ListView) findViewById(R.id.mails);
        SimpleAdapter adapter = new SimpleAdapter(this, mailList, android.R.layout.simple_list_item_2, new String[] {"from", "subject"}, new int[] {android.R.id.text1, android.R.id.text2});
        mails.setAdapter(adapter);
        
        mails.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
            	String mailId = MAIL_IDS[position].toString();
            	
            	Toast.makeText(Mail.this, mailId, Toast.LENGTH_LONG).show();
            }
        });
    }
}
