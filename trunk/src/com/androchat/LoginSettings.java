package com.androchat;

import java.text.ChoiceFormat;
import java.text.MessageFormat;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;


public class LoginSettings extends Activity {
	
	final static int[] INTERVALS = { 3, 5, 10, 15, 30, 60, 120 };
	final static long[] VIBRATION_PATTERN = new long[] { 0, 100, 60, 100 };
	final static int DEFAULT_INTERVAL_INDEX = 4;
	
	// Creating controls that need implementation:
	private Button btnLogIn;
	private Button btnLogOut;
	private CheckBox chkSound; 
	private CheckBox chkVibaration;
	private EditText txtUserName;
	private EditText txtPassWord;
	private Spinner Interval;
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

    	// Trigger the AlarmReceiver instead of starting the service
		// directly so that the wake lock gets acquired.
		// The service will take care of rescheduling itself
		// appropriately.
		sendBroadcast(new Intent(LoginSettings.this, AlarmReceiver.class));
		
        // Get controls from the layout
        this.btnLogIn = (Button)this.findViewById(R.id.btnLogin);
        this.btnLogOut = (Button)this.findViewById(R.id.btnLogOut);
        this.chkSound = (CheckBox)this.findViewById(R.id.chkSound);
        this.chkVibaration = (CheckBox)this.findViewById(R.id.chkVibaration);
        this.txtUserName = (EditText)this.findViewById(R.id.txtUserName);
        this.txtPassWord = (EditText)this.findViewById(R.id.txtPassWord);
        this.Interval = (Spinner)this.findViewById(R.id.interval);
        
		// Set up the interval choices:
        final String[] intervalChoiceText = new String[INTERVALS.length];

		for (int i = 0; i < INTERVALS.length; ++i) {
			int option = INTERVALS[i];
			intervalChoiceText[i] = MessageFormat.format(
				new ChoiceFormat(getString(R.string.interval_option))
					.format(option), option);
		}
		
		ArrayAdapter<String> a = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, intervalChoiceText);
		a.setDropDownViewResource(
			android.R.layout.simple_spinner_dropdown_item);
		Interval.setAdapter(a);
		
        // Get Preferences
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        txtUserName.setText(pref.getString("username", ""));
        txtPassWord.setText(pref.getString("password", ""));
		Interval.setSelection(pref.getInt("interval", DEFAULT_INTERVAL_INDEX));
        chkSound.setChecked(pref.getBoolean("sound", true));
        chkVibaration.setChecked(pref.getBoolean("vibaration", true));
        
        // Define events to the btnLogin
        this.btnLogIn.setOnClickListener(new OnClickListener() 
        {	 
	          public void onClick(View v) 
	          {	
	        	String strUserName = txtUserName.getText().toString();
	        	String strPassword = txtPassWord.getText().toString();
	        	int nInterval =  INTERVALS[Interval.getSelectedItemPosition()];
	        	int nIntervalSelectedPosition = Interval.getSelectedItemPosition();
	        	boolean bSound = chkSound.isChecked();
	        	boolean bVibaration = chkVibaration.isChecked();
	        	
	        	try{
		        	TwitterManager.getInstance().Connect(strUserName, strPassword);
		        	TwitterManager.getInstance().setInterval(nInterval);
		        	TwitterManager.getInstance().setSound(bSound);
		        	TwitterManager.getInstance().setVibration(bVibaration);
		        	
		        	// Save Preferences
		        	Editor e = LoginSettings.this.getPreferences(Context.MODE_PRIVATE).edit();
		        	e.putString("username", strUserName);
		        	e.putString("password", strPassword);
		        	e.putInt("interval", nIntervalSelectedPosition);
		        	e.putBoolean("sound", bSound);
		        	e.putBoolean("vibaration", bVibaration);
		        	e.commit();
		        	
		        	Intent iMessagesList = new Intent(LoginSettings.this, ContactList.class);
		            startActivity(iMessagesList);
		            finish();
	        	}
	        	catch(TwitterException ex){
	        		new AlertDialog.Builder(LoginSettings.this)
	        	      .setMessage(ex.getMessage())
	        	      .show();
	        	}
	          }
        });
        
        // Define events to chkSound
        this.chkSound.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  if (chkSound.isChecked()) 
	        	  {
  					MediaPlayer mp = MediaPlayer.create(
  						LoginSettings.this, R.raw.tweet);
  					mp.setOnCompletionListener(new OnCompletionListener() 
  					{
  						public void onCompletion(MediaPlayer mp) {
  							mp.release();
  						}
  					});
  					mp.start();
	        		  
	        	  }
	          }
        });
        
        // Define events to chkVibration
        this.chkVibaration.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  if (chkVibaration.isChecked()) 
	        	  {
	        		  ((Vibrator) getSystemService(VIBRATOR_SERVICE))
						.vibrate(VIBRATION_PATTERN, -1);				  }
	          }
        });
        
        // Define events to the btnLogout
        this.btnLogOut.setOnClickListener(new OnClickListener() 
        {	 
	          public void onClick(View v) 
	          {
	            TwitterManager.getInstance().Disconnect();
	          }
        });
	
	}

}
