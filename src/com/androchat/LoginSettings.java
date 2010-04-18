package com.androchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class LoginSettings extends Activity {
	
	// TODO: Match to the project
	final static int[] INTERVALS = { 3, 5, 10, 15, 30, 60, 120 };
	final static int DEFAULT_INTERVAL_INDEX = 4;
	final static long[] VIBRATION_PATTERN = new long[] { 0, 100, 60, 100 };
	
	// Creating controls that need implementation:
	private Button btnLogin;
	private CheckBox chkSound; 
	private CheckBox chkVibaration;
	
	@Override
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
	          @Override
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
	          @Override
	          public void onClick(View v) 
	          {
	        	  if (chkSound.isChecked()) {
						
					}
	          }
        });
        
        // Define events to chkVibration
        this.chkVibaration.setOnClickListener(new OnClickListener() 
        {
	          @Override
	          public void onClick(View v) 
	          {
	        	  if (chkVibaration.isChecked()) {
						((Vibrator) getSystemService(VIBRATOR_SERVICE))
						.vibrate(VIBRATION_PATTERN, -1);
					}
	          }
        });
	
	}

}
