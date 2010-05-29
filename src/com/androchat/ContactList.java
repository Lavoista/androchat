package com.androchat;

import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ContactList extends Activity {
	
	private ScrollView _scvMain;
	private TableLayout _tblContacts;
	private Button _btnNewMessage;
	private Button _btnSettings;
	
	@Override
	public void onCreate (Bundle savedInstanceState ){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contactlist);

		this._scvMain = (ScrollView)this.findViewById(R.id.scvContactsMain);
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
		          finish();
	          }
        });
        
        // Define events to the btnSettings
        this._btnSettings.setOnClickListener(new OnClickListener() 
        {
	          public void onClick(View v) 
	          {
	        	  Intent iSettings = new Intent(ContactList.this, LoginSettings.class);
		          startActivity(iSettings);
		          finish();
	          }
        });
		
        
//		List<String> listMsgs = new ArrayList<String>();
//		listMsgs.add("Shauli1");
//		listMsgs.add("Dov1");
//		listMsgs.add("Or1");
//		listMsgs.add("Shauli2");
//		listMsgs.add("Dov2");
//		listMsgs.add("Or2");
//		listMsgs.add("Shauli3");
//		listMsgs.add("Dov3");
//		listMsgs.add("Or3");
//		listMsgs.add("Shauli4");
		
		
		List<User> followers = TwitterManager.getInstance().GetAllContacts(true);

		if (followers!=null && followers.size()>0)
		{
			// Go through each item in the array
	        for (int iCurrent = 0; iCurrent < followers.size(); iCurrent++)
	        {
	            // Create a TableRow and give it an ID
	            TableRow tr = new TableRow(this);
	            tr.setId(1000+iCurrent);
	            tr.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	            tr.setMinimumHeight(30);
	            
//	            ImageView imageContact = new ImageView(this);
//	            imageContact.setId(2000+iCurrent);
//	            imageContact.setImageResource(R.drawable.notification_icon_status_bar);
	            
	            // Create a TextView to house the name of the province
	            TextView labelTV = new TextView(this);
	            labelTV.setId(3000+iCurrent);
	            labelTV.setTextSize((float) 20.0);
	            labelTV.setLayoutParams(new LayoutParams(
					                    LayoutParams.FILL_PARENT,
					                    LayoutParams.FILL_PARENT));
	        	labelTV.setGravity(Gravity.LEFT);
	            labelTV.setText( followers.get(iCurrent).name + "(" + followers.get(iCurrent).screenName + ")");
	        	//labelTV.setText( followers.get(iCurrent));
	        	labelTV.setPadding(5,0,0,0);
	
//	        	tr.addView(imageContact);
	            tr.addView(labelTV);
	
	            // Add the TableRow to the TableLayout
	            _tblContacts.addView(tr, new TableLayout.LayoutParams(
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
            labelTV.setTextSize((float) 20.0);
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

}
