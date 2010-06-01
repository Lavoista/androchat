package com.androchat;

import java.util.List;

import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ContactList extends Activity {

	private TableLayout _tblContacts;
	private Button _btnNewMessage;
	private Button _btnSettings;
	
	@Override
	public void onCreate (Bundle savedInstanceState ){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contactlist);

		this._tblContacts = (TableLayout)this.findViewById(R.id.tblContacts);
		this._btnNewMessage = (Button)this.findViewById(R.id.btnComposeNew);
		this._btnSettings = (Button)this.findViewById(R.id.btnSettings);
		
        // Define events to the btnNewMessage
        this._btnNewMessage.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  Intent iNewMessage = new Intent(ContactList.this, NewMessage.class);
		          startActivity(iNewMessage);
	          }
        });
        
        // Define events to the btnSettings
        this._btnSettings.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  Intent iSettings = new Intent(ContactList.this, LoginSettings.class);
		          startActivity(iSettings);
	          }
        });
		
        try
        {
			final List<User> followers = TwitterManager.getInstance().GetAllContacts(true);
	
			if (followers!=null && followers.size()>0)
			{
				// Go through each item in the array
		        for (int iCurrent = 0; iCurrent < followers.size(); iCurrent++)
		        {
		        	final int currentIndex = iCurrent;
		        	
		            // Create a Outer TableRow and give it an ID
		            TableRow trOuter = new TableRow(this);
		            trOuter.setId(1000+iCurrent);
		            trOuter.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		            trOuter.setMinimumHeight(50);
		            
		            trOuter.setClickable(true);

		            
		            trOuter.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							
				        	Intent iConversation = new Intent(ContactList.this, Conversation.class);
				        	iConversation.putExtra(TwitterManager.getInstance().USER_NAME, followers.get(currentIndex).name);
				        	iConversation.putExtra(TwitterManager.getInstance().SCREEN_NAME, followers.get(currentIndex).screenName);
				            startActivity(iConversation);
						}
					});
		            
	//	            ImageView imageContact = new ImageView(this);
	//	            imageContact.setId(2000+iCurrent);
	//	            imageContact.setImageResource(R.drawable.notification_icon_status_bar);
		            
		            TableLayout tlInner = new TableLayout(this);
		            tlInner.setId(2000 + iCurrent);
		            tlInner.setLayoutParams(new LayoutParams(
		                    LayoutParams.WRAP_CONTENT,
		                    LayoutParams.FILL_PARENT));
		            
		         // Create a TableRow and give it an ID
		            TableRow trInner_Name = new TableRow(this);
		            trInner_Name.setId(3000+iCurrent);
		            trInner_Name.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		            //trInner_Name.setMinimumHeight(30);
		            
		            TextView labelTV_Name = new TextView(this);
		            labelTV_Name.setId(4000+iCurrent);
		            labelTV_Name.setTextSize((float) 25.0);
		            labelTV_Name.setLayoutParams(new LayoutParams(
						                    LayoutParams.WRAP_CONTENT,
						                    LayoutParams.WRAP_CONTENT));
		            labelTV_Name.setGravity(Gravity.LEFT);
		            labelTV_Name.setText( followers.get(iCurrent).name );
		            labelTV_Name.setPadding(5,0,0,0);
		            

		            TableRow trInner_ScreenName = new TableRow(this);
		            trInner_ScreenName.setId(5000+iCurrent);
		            trInner_ScreenName.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		            //trInner_ScreenName.setMinimumHeight(30);
		            
		            TextView labelTV_ScreenName = new TextView(this);
		            labelTV_ScreenName.setId(6000+iCurrent);
		            labelTV_ScreenName.setTextSize((float) 15.0);
		            labelTV_ScreenName.setLayoutParams(new LayoutParams(
						                    LayoutParams.WRAP_CONTENT,
						                    LayoutParams.WRAP_CONTENT));
		            labelTV_ScreenName.setGravity(Gravity.LEFT);
		        	labelTV_ScreenName.setText( "(" + followers.get(iCurrent).screenName + ")");
		        	labelTV_ScreenName.setPadding(5,0,0,0);		        	
		
		        	
	//	        	tr.addView(imageContact);
		        	trInner_Name.addView(labelTV_Name);
		        	trInner_ScreenName.addView(labelTV_ScreenName);

		        	tlInner.addView(trInner_Name);
		        	tlInner.addView(trInner_ScreenName);
		        	
		        	trOuter.addView(tlInner);
		
		            // Add the TableRow to the TableLayout
		            _tblContacts.addView(trOuter, new TableLayout.LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		        }
			
			}
			else
			{
				TableRow tr = new TableRow(this);
	            tr.setId(99998);
	            tr.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	            tr.setMinimumHeight(30);
	            
	            TextView labelTV = new TextView(this);
	            labelTV.setId(99999);
	            labelTV.setTextSize((float) 25.0);
	            labelTV.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	        	labelTV.setGravity(Gravity.LEFT);
	            labelTV.setText( "There are no contacts for current user." );
	        	labelTV.setPadding(5,0,0,0);
	
	            tr.addView(labelTV);
	
	            // Add the TableRow to the TableLayout
	            _tblContacts.addView(tr, new TableLayout.LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
			}
        }
        catch (TwitterException e) {

    		new AlertDialog.Builder(ContactList.this)
    	      .setMessage(e.getMessage())
    	      .show();
		}
	}

}
