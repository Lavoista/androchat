package com.androchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore.Audio;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.app.Notification;


public class LoginSettings extends Activity {
	
	// TODO: Match to the project
	final static int[] INTERVALS = { 3, 5, 10, 15, 30, 60, 120 };
	final static int DEFAULT_INTERVAL_INDEX = 4;
	final static long[] VIBRATION_PATTERN = new long[] { 0, 100, 60, 100 };
	
	// Creating controls that need implementation:
	private Button btnLogin;
	private CheckBox chkSound; 
	private CheckBox chkVibaration;
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

        // Get controls from the layout
        this.btnLogin = (Button)this.findViewById(R.id.btnLogin);
        this.chkSound = (CheckBox)this.findViewById(R.id.chkSound);
        this.chkVibaration = (CheckBox)this.findViewById(R.id.chkVibaration);
        
        
        // Define events to the btnLogin
        this.btnLogin.setOnClickListener(new OnClickListener() 
        {	 
	          public void onClick(View v) 
	          {
	            Intent iMessagesList = new Intent(LoginSettings.this, MessagesList.class);
	            startActivity(iMessagesList);
	            finish();
	          }
        });
        
        // Define events to chkSound
        this.chkSound.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  if (chkSound.isChecked()) 
	        	  {
	        		  Notification soundnotification = new Notification();
	        		  soundnotification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
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

				  }
	          }
        });
	
	}

}
