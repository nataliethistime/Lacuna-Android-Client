package com.lacunaexpanse.LacunaAndroidClient.Mail;

// TODO Create refreshMail() method.
// TODO TEST!!
// TODO implement the populating of the page num & tag Spinners.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.lacunaexpanse.LacunaAndroidClient.R;
import com.lacunaexpanse.LacunaAndroidClient.lib.Client;
import com.lacunaexpanse.LacunaAndroidClient.lib.JsonParser;

public class Mail extends Activity {
	// So that there's a central "Currently selected page number and tag"
	private static int pageNum      = 1;
	private static String filterTag = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        
        refreshMail();
        
        // Populate the tags Spinner.
        ArrayAdapter<String> tagsSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
        tagsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner tagSelector = (Spinner) findViewById(R.id.mailFilter);
		
		// TODO add all the tags.
		tagsSpinnerAdapter.add("Select Tag");
		tagsSpinnerAdapter.add("Tutorial");
		tagsSpinnerAdapter.add("Correspondence");
		tagsSpinnerAdapter.add("Alerts");
		tagsSpinnerAdapter.add("Attacks");
		tagsSpinnerAdapter.add("Colonization");
		tagsSpinnerAdapter.add("Complaint");
		tagsSpinnerAdapter.add("Excavator");
		tagsSpinnerAdapter.add("Mission");
		tagsSpinnerAdapter.add("Parliament");
		tagsSpinnerAdapter.add("Probe");
		tagsSpinnerAdapter.add("Spies");
		tagsSpinnerAdapter.add("Trade");
		
		tagSelector.setAdapter(tagsSpinnerAdapter);
        
        tagSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent,View view,int pos,long id) {
        		Spinner tagSpinner = (Spinner) findViewById(R.id.mailFilter);
        		String selectedTag = tagSpinner.getSelectedItem().toString();
        		
        		// Only refresh if a tag was actually selected.
        		if (selectedTag != "Select Tag") {
        			// I think the server only accepts lowercase.
        			filterTag = "[" + selectedTag.toLowerCase() + "]"; // Need to send to the server in array form.
        		
        			refreshMail();
        		}
        	}
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nothing! Amazing, eh?
			}
        });
        
        Spinner pageNumSelector = (Spinner) findViewById(R.id.mailPage);
        pageNumSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,View view,int pos,long id) {
				pageNum = pos+1;
				
				refreshMail();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nothing! Amazing, eh?
			}
        });
    }
    
    public void refreshMail() {
    	
    	// So that Java knows how many items will get put()ed into hashOptions.
    	int hashNum = 0;
    	if (pageNum   != 1)  hashNum++;
    	if (filterTag != "") hashNum++;
    	
    	Map<String, Object>  hashOptions = new HashMap<String, Object>(hashNum);
    	if (pageNum   != 1)  hashOptions.put("page_number", pageNum);
    	if (filterTag != "") hashOptions.put("tags", filterTag);
        
        Client.setContext(Mail.this);
        JSONObject result = Client.send(true, new Object[]{hashOptions}, "inbox", "view_inbox");
        
        if (result != null) {
        	JSONArray messages = JsonParser.getJA(result, "messages");
            List<Map<String, String>> mailList = new ArrayList<Map<String, String>>();
            ArrayList<String> mailIds = new ArrayList<String>(24); // Max numbers on each page is 25.
            //String[] mailIds = new String[24];
            
            for (int i = 0; i < messages.length(); i++) {
            	JSONObject message = JsonParser.getJO(messages, i);
            	
            	Map<String, String> datum = new HashMap<String, String>(2);
                datum.put("from", JsonParser.getS(message, "from"));
                datum.put("subject", JsonParser.getS(message, "subject"));
                mailList.add(datum);
            	
                mailIds.add(JsonParser.getS(message, "id"));
                //mailIds[i] = JsonParser.getS(message, "id");
            }
            
            //final String[] MAIL_IDS = mailIds;
            final Object[] MAIL_IDS = mailIds.toArray();
            
            ListView mails = (ListView) findViewById(R.id.mails);
            SimpleAdapter adapter = new SimpleAdapter(this, mailList, android.R.layout.simple_list_item_2, new String[] {"from", "subject"}, new int[] {android.R.id.text1, android.R.id.text2});
            mails.setAdapter(adapter);
            
            mails.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
                	String mailId = MAIL_IDS[position].toString();
                	
                	Intent intent = new Intent(Mail.this, ViewMail.class);
                	intent.putExtra("mailId", mailId);
                	
                	Mail.this.startActivity(intent);
                }
            });
            
            // Populate the pages Spinner.
            ArrayAdapter<String> pagesSpinnerAdapter = new ArrayAdapter<String>(this,
    				android.R.layout.simple_spinner_item);
    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner mailPages = (Spinner) findViewById(R.id.mailPage);
            double pages = (double) Math.ceil(JsonParser.getL(result, "message_count") / 25);
            
            for (int i = 1; i < pages; i++) {
            	pagesSpinnerAdapter.add("" + i);
            }
            
            mailPages.setAdapter(pagesSpinnerAdapter);
        }
    }
}
