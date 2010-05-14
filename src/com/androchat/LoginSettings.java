package com.androchat;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore.Audio;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.Notification;
import java.lang.*;


public class LoginSettings extends Activity {
	
	
	// Creating controls that need implementation:
	private Button btnLogIn;
	private Button btnLogOut;
	private CheckBox chkSound; 
	private CheckBox chkVibaration;
	private EditText txtUserName;
	private EditText txtPassWord;
	private EditText txtTimeInterval;
	
	public void onCreate(Bundle savedInstanceState)	{
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

        // Get controls from the layout
        this.btnLogIn = (Button)this.findViewById(R.id.btnLogin);
        this.btnLogOut = (Button)this.findViewById(R.id.btnLogOut);
        this.chkSound = (CheckBox)this.findViewById(R.id.chkSound);
        this.chkVibaration = (CheckBox)this.findViewById(R.id.chkVibaration);
        this.txtUserName = (EditText)this.findViewById(R.id.txtUserName);
        this.txtPassWord = (EditText)this.findViewById(R.id.txtPassWord);
        this.txtTimeInterval = (EditText)this.findViewById(R.id.txtInterval);
        
        // Define events to the btnLogin
        this.btnLogIn.setOnClickListener(new OnClickListener() 
        {	 
	          public void onClick(View v) 
	          {
	        	// TODO : DOV - log in to twitter
	        	String strPassword = txtPassWord.getText().toString();
	        	String strUserName = txtUserName.getText().toString();
	        	String strInterval = txtTimeInterval.getText().toString();

	        	// TODO: can not succeed to open new window
	            finish();
	            Intent iMessagesList = new Intent(LoginSettings.this, MessagesList.class);
	            startActivity(iMessagesList);
	          }
        });
        
        // Define events to chkSound
        this.chkSound.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  if (chkSound.isChecked()) 
	        	  {
	        		  // TODO : OR
	        		  
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
	        		  // TODO : OR
				  }
	          }
        });
        
        // Define events to the btnLogout
        this.btnLogOut.setOnClickListener(new OnClickListener() 
        {	 
	          public void onClick(View v) 
	          {
	            // TODO: DOV
	          }
        });
	
	}

}
