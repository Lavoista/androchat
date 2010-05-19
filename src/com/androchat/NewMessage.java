package com.androchat;
import java.util.ArrayList;
import java.util.List;

import twitter4j.TwitterException;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NewMessage extends Activity{
	

	//private Button				_btnAddContact;
	private Spinner					_spnrAddresseeName;
	private Button					_btnSendMessage;
	private EditText 				_txtAddressName;
	private EditText 				_txtMessageBody;
	private TwitterManager 			twitter;
	private ArrayAdapter<String> 	adapter;
	private	boolean					isFirstRun = true;					
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.newmessage);
		
		//_btnAddContact = (Button) findViewById(R.id.btnAddContact);
		_btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
		_txtAddressName = (EditText) findViewById(R.id.txtAddresseeName);
		_txtMessageBody = (EditText) findViewById(R.id.txtMessageBody);
		_spnrAddresseeName = (Spinner) findViewById(R.id.spnrAddresseeName);

		// get twitter singleton instance
		twitter = TwitterManager.getInstance();
		 
		 
		try {
			twitter.Connect("shauliant", "sasbRinat19", 10);
		} catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//return;
		}

		//try
		{
			//ArrayList<User> arr = twitter.GetAllContacts();
			List<String> listUsers = new ArrayList<String>();

			listUsers.add("Shauli1");
			listUsers.add("Dov1");
			listUsers.add("Or1");
			listUsers.add("Shauli2");
			listUsers.add("Dov2");
			listUsers.add("Or2");
			listUsers.add("Shauli3");
			listUsers.add("Dov3");
			listUsers.add("Or3");
			listUsers.add("Shauli4");
			listUsers.add("Dov4");
			listUsers.add("Or4");

			//for(User u : arr){
			//	listUsers.add(u.getName());
			//}
			
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listUsers);
		    
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
		    _spnrAddresseeName.setAdapter(adapter);
		    
		    _spnrAddresseeName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    /*  The selected value to mySpinner  myTextView  In  */

            		if (isFirstRun)
            		{
            			isFirstRun = !isFirstRun;
            		}
            		else
            		{
	                	if (_txtAddressName.getText().toString().contains(adapter.getItem(arg2)) )
	                	{
	                		String cotanctsList = _txtAddressName.getText().toString();
	                		cotanctsList = cotanctsList.replaceAll(adapter.getItem(arg2), "");
	                		cotanctsList = cotanctsList.replaceAll(";;", ";");

	                		if (cotanctsList.startsWith(";"))
	                		{
	                			cotanctsList = cotanctsList.substring(1);
	                		}
	                		
	                		if (cotanctsList.endsWith(";"))
	                		{
	                			cotanctsList = cotanctsList.substring(0,cotanctsList.length()-2);
	                		}
	                		
	                		_txtAddressName.setText( cotanctsList );
	                	}
                		else
                		{
                			if (_txtAddressName.getText().toString().length()>0)
                			{
                				_txtAddressName.append(";" + adapter.getItem(arg2));
                			}
                			else
                			{
                    			_txtAddressName.append(adapter.getItem(arg2));
                			}
                		}
            		}
                    /*  The mySpinner show  */
                    //arg0.setVisibility(View.VISIBLE);
                }
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                	//_txtAddressName.setText("NONE");
                    //arg0.setVisibility(View.VISIBLE);
                }
	        });
		    
		    
	        /* The drop-down menu popup content options touch event handling  */
			_spnrAddresseeName.setOnTouchListener(new Spinner.OnTouchListener(){
	                public boolean onTouch(View v, MotionEvent event) {
	                        // TODO Auto-generated method stub
	                        /*  Hide the mySpinner  , Do not hide or, to see their hobbies  */
	                        //v.setVisibility(View.INVISIBLE);
	                        return false;
	                }
	        });
	        
			
			/* The drop-down menu popup content options focus change event handler  */
			_spnrAddresseeName.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
	        public void onFocusChange(View v, boolean hasFocus) {
	        // TODO Auto-generated method stub
	                //v.setVisibility(View.VISIBLE);
	        }
	        });
		}
		//catch ( TwitterException e)
		{
			
		}
		
		_btnSendMessage.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO : Start send message process
				
				
				// verify login	
				try {
					twitter.Connect( "", "", 10);
				}
				catch (TwitterException e) {
					// handle wrong login
					// notify user for problem
				}
				
				
				// verify contact
				//		1. verify that the logged-in user is following the contact
				//		2. verify that the contact is following on me
				
			}
		});
		
		_txtAddressName.addTextChangedListener( new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO : When user enters his contact name
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO : When user enters his contact name
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO : When user enters his contact name
				
			}
			
		});
		
		
	}
	
}
