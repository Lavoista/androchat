package com.androchat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.User;
import android.app.Activity;
import android.app.AlertDialog;
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
	
	private Spinner					_spnrAddresseeName;
	private Button					_btnSendMessage;
	private EditText 				_txtMessageBody;
	private ArrayAdapter<String> 	adapter;					
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.newmessage);
		
		_btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
		_txtMessageBody = (EditText) findViewById(R.id.txtMessageBody);
		_spnrAddresseeName = (Spinner) findViewById(R.id.spnrAddresseeName);

			List<String> listUsers = new ArrayList<String>();
			List<User> followers = TwitterManager.getInstance().GetAllContacts(true);
			 
			class ComparatorUser_ScreenName implements Comparator<User>
			{
			    public int compare(User u1, User u2) {
			        return u1.screenName.compareToIgnoreCase( u2.screenName );
			    }
			}
			
			ComparatorUser_ScreenName cmprScreenName = new ComparatorUser_ScreenName();
			
			Collections.sort(followers, cmprScreenName);
			
			for(User u : followers){
				listUsers.add(u.screenName);
			}
			
			
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listUsers);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    _spnrAddresseeName.setAdapter(adapter);
	        
			// On send message :
			_btnSendMessage.setOnClickListener( new View.OnClickListener()
				{
				public void onClick(View v) {
					if (_spnrAddresseeName.getSelectedItemPosition()>0)
					{
						boolean bMsgSentSuccess = false;
						try
						{
							TwitterManager.getInstance().SendMessage(_spnrAddresseeName.getSelectedItem().toString(), _txtMessageBody.getText().toString());
							bMsgSentSuccess = true;	
						}
						catch (TwitterException ex)
						{
			        		new AlertDialog.Builder(NewMessage.this)
			        	      .setMessage("Error Ocoured :\n" + ex.getMessage())
			        	      .show();		
						}
						
						if (bMsgSentSuccess)
						{
							Intent iSettings = new Intent(NewMessage.this, ContactList.class);
							startActivity(iSettings);
							finish();

			        		new AlertDialog.Builder(NewMessage.this)
			        	      .setMessage("Message was sent !")
			        	      .show();	
						}
					}
					else
					{
		        		new AlertDialog.Builder(NewMessage.this)
		        	      .setMessage("Please choose a contact")
		        	      .show();
					}
				}
			});
	}
	
}
