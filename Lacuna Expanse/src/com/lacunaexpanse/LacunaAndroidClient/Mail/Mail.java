package com.lacunaexpanse.LacunaAndroidClient.Mail;

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
	private static long spinnerReset     = 0;

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
        
		spinnerReset = System.currentTimeMillis();
        tagSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent,View view,int pos,long id) {
        		if (System.currentTimeMillis() - spinnerReset < 1000) {
					//Nothing!
				}
        		else if (System.currentTimeMillis() - spinnerReset > 1000) {
        			Spinner tagSpinner = (Spinner) findViewById(R.id.mailFilter);
        			String selectedTag = tagSpinner.getSelectedItem().toString();
        		
        			// Only refresh if a tag was actually selected.
        			if (selectedTag != "Select Tag") {
        				// Just in case the server only accepts lowercase.
        				filterTag = selectedTag.toLowerCase();
        				pageNum   = 0; // Reset the page number.
        		
        				refreshMail();
        			}
        		}
        	}
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nothing! Amazing, eh?
			}
        });
        
        Spinner pageSelector = (Spinner) findViewById(R.id.mailPage);
        spinnerReset = System.currentTimeMillis();
        pageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent,View view,int pos,long id) {
        		if (System.currentTimeMillis() - spinnerReset < 1000) {
					//Nothing!
				}
        		else if (System.currentTimeMillis() - spinnerReset > 1000) {
        			pageNum = pos+1;
    				
    				refreshMail();
        		}
        	}
			public void onNothingSelected(AdapterView<?> arg0) {
				// Nothing! Amazing, eh?
			}
        });
    }
    
    public void refreshMail() {
    	
    	JSONObject hashOptions = new JSONObject();
    	if (pageNum   != 1)  JsonParser.put(hashOptions, "page_number", pageNum);
    	if (filterTag != "") JsonParser.put(hashOptions, "tags", filterTag);
    	
        Client.setContext(Mail.this);
        JSONObject result = Client.send(new Object[]{hashOptions}, "/inbox", "view_inbox");
        
        if (result != null) {
        	JSONArray messages = JsonParser.getJA(result, "messages");
            List<Map<String, String>> mailList = new ArrayList<Map<String, String>>();
            ArrayList<String> mailIds = new ArrayList<String>(24); // Max numbers on each page is 25.
            
            boolean hasMail = false; // By default, so that there's no way to screw up.
            if (messages.length() > 0) {
            	for (int i = 0; i < messages.length(); i++) {
            		JSONObject message = JsonParser.getJO(messages, i);
            	
            		Map<String, String> datum = new HashMap<String, String>(2);
            		datum.put("from", JsonParser.getS(message, "from"));
            		datum.put("subject", JsonParser.getS(message, "subject"));
            		mailList.add(datum);
            	
            		mailIds.add(JsonParser.getS(message, "id"));
            		
            		hasMail = true;
            	}
            }
            else {
            	Map<String, String> datum = new HashMap<String, String>(2);
        		datum.put("from", "No new Mail available.");
        		datum.put("subject", "Sorry. :(");
        		mailList.add(datum);
        		
        		hasMail = false;
            }
            
            // Let the UI refresh when there's no mails.
            ListView mails = (ListView) findViewById(R.id.mails);
        	SimpleAdapter adapter = new SimpleAdapter(this, mailList, android.R.layout.simple_list_item_2, new String[] {"from", "subject"}, new int[] {android.R.id.text1, android.R.id.text2});
        	mails.setAdapter(adapter);
            
            if (hasMail == true) {
            	final Object[] MAIL_IDS = mailIds.toArray();
            
            	//spinnerReset = System.currentTimeMillis();
            	mails.setOnItemClickListener(new OnItemClickListener() {
            		public void onItemClick(AdapterView<?> a, View v, int pos, long id) { 
                		//if (System.currentTimeMillis() - spinnerReset < 1000) {
    						//Nothing!
    					//}
            			//else if (System.currentTimeMillis() - spinnerReset > 1000) {
            				String mailId = MAIL_IDS[pos].toString();
                    	
                    		Intent intent = new Intent(Mail.this, ViewMail.class);
                    		intent.putExtra("mailId", mailId);
                    	
                    		Mail.this.startActivity(intent);
            			//}
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
            	mailPages.recomputeViewAttributes(mailPages);
            	mailPages.setSelection(pageNum-1);
            }
        }
    }
}
