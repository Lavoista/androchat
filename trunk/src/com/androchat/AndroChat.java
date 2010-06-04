package com.androchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class AndroChat extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.main);
    	
		// Trigger the AlarmReceiver instead of starting the service
		// directly so that the wake lock gets acquired.
		// The service will take care of rescheduling itself
		// appropriately.
		sendBroadcast(new Intent(this, AlarmReceiver.class));
		
        final ProgressDialog dialogStartingApplication = ProgressDialog.show(AndroChat.this, "", 
                "Starting AndroChat.\nChecking latest settings.\nPlease wait...",true);
        dialogStartingApplication.setCancelable(true);
        dialogStartingApplication.setCanceledOnTouchOutside(false);
    	
    	//dialog.show();
        
		new Thread() 
		{
			public void run() 
			{
				try
				{
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
				catch (Exception ex){
    				Editor e = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
					e.remove("token");
					e.remove("tokensecret");
					e.commit();
    				new AlertDialog.Builder(AndroChat.this)
    				.setMessage(ex.getMessage())
    				.show();
    			}
				dialogStartingApplication.dismiss();
			}
		}.start();
    }

}