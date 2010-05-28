package com.androchat;

import winterwell.jtwitter.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


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
        
        // Get Preferences
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        txtUserName.setText(pref.getString("username", ""));
        txtPassWord.setText(pref.getString("password", ""));
        txtTimeInterval.setText(Integer.toString(pref.getInt("interval", 10)));
        chkSound.setChecked(pref.getBoolean("sound", true));
        chkVibaration.setChecked(pref.getBoolean("vibaration", true));
        
        // Define events to the btnLogin
        this.btnLogIn.setOnClickListener(new OnClickListener() 
        {	 
	          public void onClick(View v) 
	          {
	        	String strUserName = txtUserName.getText().toString();
	        	String strPassword = txtPassWord.getText().toString();
	        	int nInterval =  Integer.parseInt(txtTimeInterval.getText().toString());
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
		        	e.putInt("interval", nInterval);
		        	e.putBoolean("sound", bSound);
		        	e.putBoolean("vibaration", bVibaration);
		        	e.commit();
		        	
		        	Intent iMessagesList = new Intent(LoginSettings.this, MessagesList.class);
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
	            TwitterManager.getInstance().Disconnect();
	          }
        });
	
	}

}
