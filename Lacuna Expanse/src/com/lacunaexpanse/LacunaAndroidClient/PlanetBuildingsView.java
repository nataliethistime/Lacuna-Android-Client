package com.lacunaexpanse.LacunaAndroidClient;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

class PlanetBuildingsView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_buildings_view);
        
        final Bundle EXTRAS = getIntent().getExtras();
        
        String sessionId = null;
        String selectedServer = null;
        String planetId = null;
        
        if (EXTRAS != null) {
        	sessionId = EXTRAS.getString("sessionId");
        	selectedServer = EXTRAS.getString("selectedServer");
        	planetId = EXTRAS.getString("planetId");
        }
        
        final String SESSION_ID = sessionId;
        final String SELECTED_SERVER = selectedServer;
        final String PLANET_ID = planetId;
        
        /*--------------------------*/
        
        
        
        
        /*Dear followers,
         * 
         * I am unable to continue with this class. My computer takes 20 minutes to compile 
         * the app when all the building assets are imported; much too long. So, I have to leave
         * it for the time being. I am planning to spend my Christmas money on a new computer.
         * But such a thing is still in the planning stage.
         * 
         * -The Vasari
         */
        
        
        
        
        /*--------------------------*/
        
        
        /*
        
        String[] paramsBuilder = {SESSION_ID,PLANET_ID};
        String params = Library.parseParams(paramsBuilder);
        String serverUrl = Library.assembleGetUrl(SELECTED_SERVER, "body", "get_buildings", params);
        
        final String serverResponse = Library.sendServerRequest(serverUrl);
        
        /*
         * 
         * If you are reading this and and think this is crazy . . . never fear; 
         * I doubt that half of it will work myself. :)
         * 
         * NOTE TO SELF: All Image names with hyphens in them have been removed!!!!
         * MAKE SURE TO RE ADD WHEN A FIX IS FOUND!
        
        Log.d("This is a tag.", "Made it to declaring the planetPlots[]");
        
        // This will be fun...
        ImageView[] planetPlots = {};
        planetPlots[0] = (ImageView) findViewById(R.id.plot_negative5_5);
        planetPlots[1] = (ImageView) findViewById(R.id.plot_negative5_4);
        planetPlots[2] = (ImageView) findViewById(R.id.plot_negative5_3);
        planetPlots[3] = (ImageView) findViewById(R.id.plot_negative5_2);
        planetPlots[4] = (ImageView) findViewById(R.id.plot_negative5_1);
        planetPlots[5] = (ImageView) findViewById(R.id.plot_negative5_0);
        planetPlots[6] = (ImageView) findViewById(R.id.plot_negative5_negative1);
        planetPlots[7] = (ImageView) findViewById(R.id.plot_negative5_negative2);
        planetPlots[8] = (ImageView) findViewById(R.id.plot_negative5_negative3);
        planetPlots[9] = (ImageView) findViewById(R.id.plot_negative5_negative4);
        planetPlots[10] = (ImageView) findViewById(R.id.plot_negative5_negative5);
        
        planetPlots[11] = (ImageView) findViewById(R.id.plot_negative4_5);
        planetPlots[12] = (ImageView) findViewById(R.id.plot_negative4_4);
        planetPlots[13] = (ImageView) findViewById(R.id.plot_negative4_3);
        planetPlots[14] = (ImageView) findViewById(R.id.plot_negative4_2);
        planetPlots[15] = (ImageView) findViewById(R.id.plot_negative4_1);
        planetPlots[16] = (ImageView) findViewById(R.id.plot_negative4_0);
        planetPlots[17] = (ImageView) findViewById(R.id.plot_negative4_negative1);
        planetPlots[18] = (ImageView) findViewById(R.id.plot_negative4_negative2);
        planetPlots[19] = (ImageView) findViewById(R.id.plot_negative4_negative3);
        planetPlots[20] = (ImageView) findViewById(R.id.plot_negative4_negative4);
        planetPlots[21] = (ImageView) findViewById(R.id.plot_negative4_negative5);
        
        planetPlots[22] = (ImageView) findViewById(R.id.plot_negative3_5);
        planetPlots[23] = (ImageView) findViewById(R.id.plot_negative3_4);
        planetPlots[24] = (ImageView) findViewById(R.id.plot_negative3_3);
        planetPlots[25] = (ImageView) findViewById(R.id.plot_negative3_2);
        planetPlots[26] = (ImageView) findViewById(R.id.plot_negative3_1);
        planetPlots[27] = (ImageView) findViewById(R.id.plot_negative3_0);
        planetPlots[28] = (ImageView) findViewById(R.id.plot_negative3_negative1);
        planetPlots[29] = (ImageView) findViewById(R.id.plot_negative3_negative2);
        planetPlots[30] = (ImageView) findViewById(R.id.plot_negative3_negative3);
        planetPlots[31] = (ImageView) findViewById(R.id.plot_negative3_negative4);
        planetPlots[32] = (ImageView) findViewById(R.id.plot_negative3_negative5);
        
        planetPlots[33] = (ImageView) findViewById(R.id.plot_negative2_5);
        planetPlots[34] = (ImageView) findViewById(R.id.plot_negative2_4);
        planetPlots[35] = (ImageView) findViewById(R.id.plot_negative2_3);
        planetPlots[36] = (ImageView) findViewById(R.id.plot_negative2_2);
        planetPlots[37] = (ImageView) findViewById(R.id.plot_negative2_1);
        planetPlots[38] = (ImageView) findViewById(R.id.plot_negative2_0);
        planetPlots[39] = (ImageView) findViewById(R.id.plot_negative2_negative1);
        planetPlots[40] = (ImageView) findViewById(R.id.plot_negative2_negative2);
        planetPlots[41] = (ImageView) findViewById(R.id.plot_negative2_negative3);
        planetPlots[42] = (ImageView) findViewById(R.id.plot_negative2_negative4);
        planetPlots[43] = (ImageView) findViewById(R.id.plot_negative2_negative5);
        
        planetPlots[44] = (ImageView) findViewById(R.id.plot_negative1_5);
        planetPlots[45] = (ImageView) findViewById(R.id.plot_negative1_4);
        planetPlots[46] = (ImageView) findViewById(R.id.plot_negative1_3);
        planetPlots[47] = (ImageView) findViewById(R.id.plot_negative1_2);
        planetPlots[48] = (ImageView) findViewById(R.id.plot_negative1_1);
        planetPlots[49] = (ImageView) findViewById(R.id.plot_negative1_0);
        planetPlots[50] = (ImageView) findViewById(R.id.plot_negative1_negative1);
        planetPlots[51] = (ImageView) findViewById(R.id.plot_negative1_negative2);
        planetPlots[52] = (ImageView) findViewById(R.id.plot_negative1_negative3);
        planetPlots[53] = (ImageView) findViewById(R.id.plot_negative1_negative4);
        planetPlots[54] = (ImageView) findViewById(R.id.plot_negative1_negative5);
        
        planetPlots[55] = (ImageView) findViewById(R.id.plot_0_5);
        planetPlots[56] = (ImageView) findViewById(R.id.plot_0_4);
        planetPlots[57] = (ImageView) findViewById(R.id.plot_0_3);
        planetPlots[58] = (ImageView) findViewById(R.id.plot_0_2);
        planetPlots[59] = (ImageView) findViewById(R.id.plot_0_1);
        planetPlots[60] = (ImageView) findViewById(R.id.plot_0_0);
        planetPlots[61] = (ImageView) findViewById(R.id.plot_0_negative1);
        planetPlots[62] = (ImageView) findViewById(R.id.plot_0_negative2);
        planetPlots[63] = (ImageView) findViewById(R.id.plot_0_negative3);
        planetPlots[64] = (ImageView) findViewById(R.id.plot_0_negative4);
        planetPlots[65] = (ImageView) findViewById(R.id.plot_0_negative5);
        
        planetPlots[66] = (ImageView) findViewById(R.id.plot_negative1_5);
        planetPlots[67] = (ImageView) findViewById(R.id.plot_negative1_4);
        planetPlots[68] = (ImageView) findViewById(R.id.plot_negative1_3);
        planetPlots[69] = (ImageView) findViewById(R.id.plot_negative1_2);
        planetPlots[70] = (ImageView) findViewById(R.id.plot_negative1_1);
        planetPlots[71] = (ImageView) findViewById(R.id.plot_negative1_0);
        planetPlots[72] = (ImageView) findViewById(R.id.plot_negative1_negative1);
        planetPlots[73] = (ImageView) findViewById(R.id.plot_negative1_negative2);
        planetPlots[74] = (ImageView) findViewById(R.id.plot_negative1_negative3);
        planetPlots[75] = (ImageView) findViewById(R.id.plot_negative1_negative4);
        planetPlots[76] = (ImageView) findViewById(R.id.plot_negative1_negative5);
        
        planetPlots[77] = (ImageView) findViewById(R.id.plot_negative2_5);
        planetPlots[78] = (ImageView) findViewById(R.id.plot_negative2_4);
        planetPlots[79] = (ImageView) findViewById(R.id.plot_negative2_3);
        planetPlots[80] = (ImageView) findViewById(R.id.plot_negative2_2);
        planetPlots[81] = (ImageView) findViewById(R.id.plot_negative2_1);
        planetPlots[82] = (ImageView) findViewById(R.id.plot_negative2_0);
        planetPlots[83] = (ImageView) findViewById(R.id.plot_negative2_negative1);
        planetPlots[84] = (ImageView) findViewById(R.id.plot_negative2_negative2);
        planetPlots[85] = (ImageView) findViewById(R.id.plot_negative2_negative3);
        planetPlots[86] = (ImageView) findViewById(R.id.plot_negative2_negative4);
        planetPlots[87] = (ImageView) findViewById(R.id.plot_negative2_negative5);
        
        planetPlots[88] = (ImageView) findViewById(R.id.plot_negative3_5);
        planetPlots[89] = (ImageView) findViewById(R.id.plot_negative3_4);
        planetPlots[90] = (ImageView) findViewById(R.id.plot_negative3_3);
        planetPlots[91] = (ImageView) findViewById(R.id.plot_negative3_2);
        planetPlots[92] = (ImageView) findViewById(R.id.plot_negative3_1);
        planetPlots[93] = (ImageView) findViewById(R.id.plot_negative3_0);
        planetPlots[94] = (ImageView) findViewById(R.id.plot_negative3_negative1);
        planetPlots[95] = (ImageView) findViewById(R.id.plot_negative3_negative2);
        planetPlots[96] = (ImageView) findViewById(R.id.plot_negative3_negative3);
        planetPlots[97] = (ImageView) findViewById(R.id.plot_negative3_negative4);
        planetPlots[98] = (ImageView) findViewById(R.id.plot_negative3_negative5);
        
        planetPlots[100] = (ImageView) findViewById(R.id.plot_negative4_5);
        planetPlots[101] = (ImageView) findViewById(R.id.plot_negative4_4);
        planetPlots[102] = (ImageView) findViewById(R.id.plot_negative4_3);
        planetPlots[103] = (ImageView) findViewById(R.id.plot_negative4_2);
        planetPlots[104] = (ImageView) findViewById(R.id.plot_negative4_1);
        planetPlots[105] = (ImageView) findViewById(R.id.plot_negative4_0);
        planetPlots[106] = (ImageView) findViewById(R.id.plot_negative4_negative1);
        planetPlots[107] = (ImageView) findViewById(R.id.plot_negative4_negative2);
        planetPlots[108] = (ImageView) findViewById(R.id.plot_negative4_negative3);
        planetPlots[109] = (ImageView) findViewById(R.id.plot_negative4_negative4);
        planetPlots[110] = (ImageView) findViewById(R.id.plot_negative4_negative5);
        
        planetPlots[111] = (ImageView) findViewById(R.id.plot_negative5_5);
        planetPlots[112] = (ImageView) findViewById(R.id.plot_negative5_4);
        planetPlots[113] = (ImageView) findViewById(R.id.plot_negative5_3);
        planetPlots[114] = (ImageView) findViewById(R.id.plot_negative5_2);
        planetPlots[115] = (ImageView) findViewById(R.id.plot_negative5_1);
        planetPlots[116] = (ImageView) findViewById(R.id.plot_negative5_0);
        planetPlots[117] = (ImageView) findViewById(R.id.plot_negative5_negative1);
        planetPlots[118] = (ImageView) findViewById(R.id.plot_negative5_negative2);
        planetPlots[119] = (ImageView) findViewById(R.id.plot_negative5_negative3);
        planetPlots[120] = (ImageView) findViewById(R.id.plot_negative5_negative4);
        planetPlots[121] = (ImageView) findViewById(R.id.plot_negative5_negative5);
        
        try {
        	JSONObject jObject = new JSONObject(serverResponse);
        	JSONObject result = jObject.getJSONObject("result");
        	JSONObject buildings = result.getJSONObject("buildings");
        	
        	Iterator<?> iter = buildings.keys();
        	ArrayList<String> buildingIds = new ArrayList<String>();
        	
        	while (iter.hasNext()) {
        		String key = (String) iter.next();
        		buildingIds.add(key);
        	}
        	
        	final Object[] BUILDING_IDS = buildingIds.toArray();
        	
        	
        	JSONObject[] buildingInfo = {};
        	for (int i = 0; i < buildings.length(); i++) {
        		buildingInfo[i] = buildings.getJSONObject(BUILDING_IDS.toString());
        	}
        	
        	final JSONObject[] BUILDING_INFO = buildingInfo;
        	
        	String[] xCoords = {};
        	String[] yCoords = {};
        	String[] buildingUrl = {};
        	String[] buildingLevel = {};
        	String[] buildingImage = {};
        	String[] buildingEfficiency = {};
        	
        	for (int i = 0; i < buildings.length(); i++) {
        		xCoords[i] = BUILDING_INFO[i].getString("x");
        		yCoords[i] = BUILDING_INFO[i].getString("y");
            	buildingUrl[i] = BUILDING_INFO[i].getString("url");
            	buildingLevel[i] = BUILDING_INFO[i].getString("level");
            	buildingImage[i] = BUILDING_INFO[i].getString("image");
            	buildingEfficiency[i] = BUILDING_INFO[i].getString("efficiency");
            	
            	// In the grand scheme of things, this is the most worrying thing for me.
            	Resources resources = getResources();
            	if (xCoords[i] == "-5" && yCoords[i] == "5") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[0].setImageDrawable(drawable);
            		
            		planetPlots[0].setTag(0,BUILDING_IDS[i]);
            		planetPlots[0].setTag(1,buildingUrl[i]);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "4") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[1].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "3") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[2].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "2") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[3].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "1") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[4].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "0") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[5].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "-1") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[6].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "-2") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[7].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "-3") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[8].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "-4") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[9].setImageDrawable(drawable);
				}
            	else if (xCoords[i] == "-5" && yCoords[i] == "-5") {
            		String drawableName = buildingImage[i];
            		int resId = resources.getIdentifier(drawableName , "drawable", getPackageName());
            		Drawable drawable = resources.getDrawable(resId);
            		planetPlots[10].setImageDrawable(drawable);
				}
            	
        	}
        	
        	// Set the click listeners for all the Images.
        	for (int i = 0; i < planetPlots.length; i++) {
        		planetPlots[i].setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						
						// LOL
						
					}
				});
        	}
        }
        catch (JSONException e) {
        	Toast.makeText(PlanetBuildingsView.this, "Error interpereting server response: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        
        */
        
        Button goBackButton = (Button) findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent(PlanetBuildingsView.this,PlanetResourceView.class);
				intent.putExtra("sessionId",SESSION_ID);
				intent.putExtra("selectedServer",SELECTED_SERVER);
				intent.putExtra("planetId",PLANET_ID);
				
				PlanetBuildingsView.this.startActivity(intent);
				finish();
			}
		});
    }
}