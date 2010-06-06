package com.androchat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ContactList extends Activity {

	private Button _btnNewMessage;
	private Button _btnSettings;
	
	@Override
	public void onCreate (Bundle savedInstanceState ){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contactlist);

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

        new GetContactsDataTask().execute();
        
	}
	
	
	public void publishContacts (TableLayout _tblContacts, final List<User> followers)
	{		
		if (followers!=null && followers.size()>0)
		{
			// Go through each item in the array
	        for (int iCurrent = 0; iCurrent < followers.size(); iCurrent++)
	        {
	        	final int currentIndex = iCurrent;
	        	
	            // Create a Outer TableRow and give it an ID
	            TableRow trOuter = new TableRow(ContactList.this);
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
	            
	            ImageView imageContact = new ImageView(ContactList.this);
	            imageContact.setId(7000+iCurrent);
	            imageContact.setLayoutParams(new LayoutParams(
	                    LayoutParams.WRAP_CONTENT,
	                    LayoutParams.FILL_PARENT));
	            imageContact.setImageResource(R.drawable.notification_icon_status_bar);
	            imageContact.setPadding(5,2,0,0);
	            
	            
	            TableLayout tlInner = new TableLayout(ContactList.this);
	            tlInner.setId(2000 + iCurrent);
	            tlInner.setLayoutParams(new LayoutParams(
	                    LayoutParams.WRAP_CONTENT,
	                    LayoutParams.FILL_PARENT));
	            
	            // Create a TableRow and give it an ID
	            TableRow trInner_Name = new TableRow(ContactList.this);
	            trInner_Name.setId(3000+iCurrent);
	            trInner_Name.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	            //trInner_Name.setMinimumHeight(30);
	            
	            TextView labelTV_Name = new TextView(ContactList.this);
	            labelTV_Name.setId(4000+iCurrent);
	            labelTV_Name.setTextSize((float) 23.0);
	            labelTV_Name.setLayoutParams(new LayoutParams(
					                    LayoutParams.WRAP_CONTENT,
					                    LayoutParams.WRAP_CONTENT));
	            labelTV_Name.setGravity(Gravity.LEFT);
	            labelTV_Name.setText( followers.get(iCurrent).name );
	            labelTV_Name.setPadding(5,0,0,0);
	            

	            TableRow trInner_ScreenName = new TableRow(ContactList.this);
	            trInner_ScreenName.setId(5000+iCurrent);
	            trInner_ScreenName.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	            //trInner_ScreenName.setMinimumHeight(30);
	            
	            TextView labelTV_ScreenName = new TextView(ContactList.this);
	            labelTV_ScreenName.setId(6000+iCurrent);
	            labelTV_ScreenName.setTextSize((float) 13.0);
	            labelTV_ScreenName.setTextColor(Color.DKGRAY);
	            labelTV_ScreenName.setLayoutParams(new LayoutParams(
					                    LayoutParams.WRAP_CONTENT,
					                    LayoutParams.WRAP_CONTENT));
	            labelTV_ScreenName.setGravity(Gravity.LEFT);
	        	labelTV_ScreenName.setText( "(" + followers.get(iCurrent).screenName + ")");
	        	labelTV_ScreenName.setPadding(5,0,0,3);		        	
	
	            // Create a row seperator
	            View lineSepartor = new View(ContactList.this);
	            lineSepartor.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
	            lineSepartor.setBackgroundColor(Color.LTGRAY);
	        	
	        	trInner_Name.addView(labelTV_Name);
	        	trInner_ScreenName.addView(labelTV_ScreenName);

	        	tlInner.addView(trInner_Name);
	        	tlInner.addView(trInner_ScreenName);

	        	trOuter.addView(imageContact);
	        	trOuter.addView(tlInner);
	
	            // Add the TableRow to the TableLayout
	            _tblContacts.addView(trOuter, new TableLayout.LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	            
	            if (iCurrent+1 < followers.size())
	            {
	            	_tblContacts.addView(lineSepartor);
	            }
	        }
		
		}
		else
		{
			TableRow tr = new TableRow(ContactList.this);
            tr.setId(99998);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
            tr.setMinimumHeight(30);
            
            TextView labelTV = new TextView(ContactList.this);
            labelTV.setId(99999);
            labelTV.setTextSize((float) 25.0);
            labelTV.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        	labelTV.setGravity(Gravity.LEFT);
            labelTV.setText( R.string.error_no_contacts );
        	labelTV.setPadding(5,0,0,0);

            tr.addView(labelTV);

            // Add the TableRow to the TableLayout
            _tblContacts.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
		}
	}


	private class GetContactsDataTask extends AsyncTask<String, Void, Void> {
      
		ProgressDialog dialogGetContacts = new ProgressDialog(ContactList.this);
        List<User> followers = null;
        boolean bEx_Exception = false;
        boolean bEx_TwitterException = false;

		
		// can use UI thread here
		protected void onPreExecute() {
			this.dialogGetContacts.setMessage("Getting all Contacts.\nPlease wait...");
			this.dialogGetContacts.setCancelable(true);
			this.dialogGetContacts.setCanceledOnTouchOutside(false);
			this.dialogGetContacts.show();
		}
		
		// automatically done on worker thread (separate from UI thread)
		protected Void doInBackground(final String... args) {

			try
	        {
	    		
				followers = TwitterManager.getInstance().GetAllContacts(true);
	
				class ComparatorUser_ScreenName implements Comparator<User>
				{
				    public int compare(User u1, User u2) {
				        return u1.name.compareToIgnoreCase( u2.name);
				    }
				}
				
				ComparatorUser_ScreenName cmprScreenName = new ComparatorUser_ScreenName();
				
				Collections.sort(followers, cmprScreenName);

	
	        }
	        catch (TwitterException e) {
	        	
	        	bEx_TwitterException = true;
			}
	        catch (Exception e) {
	        	
	        	bEx_Exception = true;
			}
			
			return null;
		}
		
		// can use UI thread here
		protected void onPostExecute(final Void unused) {

			if (bEx_Exception == false && bEx_TwitterException == false)
			{
				try
				{
		    		TableLayout _tblContacts;
		    		_tblContacts = (TableLayout)ContactList.this.findViewById(R.id.tblContacts);
			        publishContacts(_tblContacts, followers);
				}
				catch (Exception e)
				{
		    		new AlertDialog.Builder(ContactList.this)
		    	      .setMessage("Error in parsing table.\n" + e.getMessage())
		    	      .show();
				}
				if (this.dialogGetContacts.isShowing()) {
					this.dialogGetContacts.dismiss();
				}
			}
			else
			{
				if (this.dialogGetContacts.isShowing()) {
					this.dialogGetContacts.dismiss();
				}
				
				if (bEx_TwitterException == true)
				{
		    		new AlertDialog.Builder(ContactList.this)
		    	      .setMessage("Error in getting contacts.\nPlease try login again.")
		    	      .show();
				}
				else
				{
					 if (bEx_Exception == true)
					 {
				    		new AlertDialog.Builder(ContactList.this)
				    	      .setMessage("General error.")
				    	      .show();
					 }
				}
			}
		}
	}

}
