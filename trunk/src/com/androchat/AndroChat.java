package com.androchat;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AndroChat extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//TODO: (Shauli :)
    	// Here we need to check if the user already inserted his
    	// twitter account and password.
    	// 		If yes , than try to login:
    	//				If login success, set content view as R.layout.messageList
    	//				If login fails, set content view as R.layout.login
    	//		If No, than set content view as R.layout.login
    	
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
	        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
	        String username =  pref.getString("username", "").trim();
	        String password = pref.getString("password", "").trim();
	        
	        if (username!=null &&  username!="" &&
	        	password!=null &&  password!="")
	        {
				int interval = pref.getInt("interval", LoginSettings.DEFAULT_INTERVAL_INDEX);
		        boolean isSoundEnabled = pref.getBoolean("sound", true);
		        boolean isVibarationEnabled = pref.getBoolean("vibaration", true);
		        
		        try{
		        	TwitterManager.getInstance().Connect(username, password);
		        	TwitterManager.getInstance().setInterval(interval);
		        	TwitterManager.getInstance().setSound(isSoundEnabled);
		        	TwitterManager.getInstance().setVibration(isVibarationEnabled);
		        	
		        	isUserAlreadySignedIn = true;
	        	}
	        	catch(TwitterException ex){
		        	isUserAlreadySignedIn = false;
	        	}
	        }
	    }
        
        if ( isUserAlreadySignedIn ){
        	// Open the messageList.
            Intent iMessagesList = new Intent(AndroChat.this, ContactList.class);
            startActivity(iMessagesList);

    		//this.setContentView(R.layout.messageslist);
        }
        else {
        	// Open the login and settings screen.
            Intent iLogin = new Intent(AndroChat.this, LoginSettings.class);
            startActivity(iLogin);
        }

        finish();
    }

}