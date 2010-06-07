package com.androchat;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
		
		
			new AndroChatStartDataTask().execute();
    }
    
    private class AndroChatStartDataTask extends AsyncTask<String, Void, Void>
    {
    	boolean isUserAlreadySignedIn = false;

		ProgressDialog dialogStartAndroChat = new ProgressDialog(AndroChat.this);
        boolean bEx_Exception = false;
        boolean bEx_TwitterException = false;
        TwitterException tex = null;
        Exception gex = null;
    	
    	@Override
    	protected void onPreExecute() {

			this.dialogStartAndroChat.setTitle("Starting AndroChat");
			this.dialogStartAndroChat.setMessage("Checking latest settings.\nPlease wait...");
			this.dialogStartAndroChat.setCancelable(true);
			this.dialogStartAndroChat.setCanceledOnTouchOutside(false);
			this.dialogStartAndroChat.show();
    	}
    	
    	@Override
    	protected Void doInBackground(String... params) {

    		try
			{
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
		    		if(strToken != "" && strTokenSecret != "")
		    		{
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
			}
    		catch (TwitterException ex)
    		{
    			tex = ex;
    			bEx_TwitterException = true;
    			isUserAlreadySignedIn = false;
    		}
			catch (Exception ex)
			{
				gex = ex;
    			bEx_Exception = true;
    			isUserAlreadySignedIn = false;
			}
    		
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void result) {
    		
			if (bEx_Exception == false && bEx_TwitterException == false)
			{
		        if ( isUserAlreadySignedIn ){
		        	// Open the messageList.
		        	Intent inContactList = new Intent(AndroChat.this, ContactList.class);
		            startActivity(inContactList);
		        }
		        else
		        {
		        	// As default open the login and settings screen.
		        	Intent inLogin = new Intent(AndroChat.this, LoginSettings.class);
		            startActivity(inLogin);		        
		        }
			}
			else
			{	
				Editor e = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
				e.remove("token");
				e.remove("tokensecret");
				e.commit();
				
	        	// As default open the login and settings screen.
	        	Intent inLogin = new Intent(AndroChat.this, LoginSettings.class);
	            startActivity(inLogin);
			}

    		if (this.dialogStartAndroChat.isShowing()) {
				this.dialogStartAndroChat.dismiss();
			}   
	        finish(); 		
    	}
    }

}