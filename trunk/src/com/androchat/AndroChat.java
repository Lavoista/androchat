package com.androchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class AndroChat extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.main);
        
        boolean isUserAlreadySignedIn = false;
        
        if (TwitterManager.getInstance().isConected())
        {
        	isUserAlreadySignedIn = true;
        }
        else
        {   
        	// Get Preferences
    		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    		String strToken = pref.getString("token", "");
    		String strTokenSecret = pref.getString("tokensecret", "");
    		if(strToken != "" && strTokenSecret != ""){
    			TwitterManager.getInstance().ConnectAuth(strToken, strTokenSecret);
				int interval = pref.getInt("interval", LoginSettings.DEFAULT_INTERVAL_INDEX);
		        boolean isSoundEnabled = pref.getBoolean("sound", true);
		        boolean isVibarationEnabled = pref.getBoolean("vibaration", true);
		        TwitterManager.getInstance().setInterval(interval);
		        TwitterManager.getInstance().setSound(isSoundEnabled);
		        TwitterManager.getInstance().setVibration(isVibarationEnabled);

		        isUserAlreadySignedIn = true;
	        }
	    }
        
        if ( isUserAlreadySignedIn ){
        	// Open the messageList.
            Intent iMessagesList = new Intent(AndroChat.this, ContactList.class);
            startActivity(iMessagesList);

        }
        else {
        	// Open the login and settings screen.
            Intent iLogin = new Intent(AndroChat.this, LoginSettings.class);
            startActivity(iLogin);
        }

        finish();
    }

}