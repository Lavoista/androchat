package com.androchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ContactList extends Activity {
	
	private ScrollView _scvMain;
	private TableLayout _tblContacts;
	
	@Override
	public void onCreate (Bundle savedInstanceState ){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contactlist);

		this._scvMain = (ScrollView)this.findViewById(R.id.scvContactsMain);
		this._tblContacts = (TableLayout)this.findViewById(R.id.tblContacts);
		
		List<String> listMsgs = new ArrayList<String>();

		listMsgs.add("Shauli1");
		listMsgs.add("Dov1");
		listMsgs.add("Or1");
		listMsgs.add("Shauli2");
		listMsgs.add("Dov2");
		listMsgs.add("Or2");
		listMsgs.add("Shauli3");
		listMsgs.add("Dov3");
		listMsgs.add("Or3");
		listMsgs.add("Shauli4");
		listMsgs.add("Dov4");
		listMsgs.add("Or4");
		listMsgs.add("Shauli1");
		listMsgs.add("Dov1");
		listMsgs.add("Or1");
		listMsgs.add("Shauli2");
		listMsgs.add("Dov2");
		listMsgs.add("Or2");
		listMsgs.add("Shauli3");
		listMsgs.add("Dov3");
		listMsgs.add("Or3");
		listMsgs.add("Shauli4");
		listMsgs.add("Dov4");
		listMsgs.add("Or4");
		listMsgs.add("Shauli1");
		listMsgs.add("Dov1");
		listMsgs.add("Or1");
		listMsgs.add("Shauli2");
		listMsgs.add("Dov2");
		listMsgs.add("Or2");
		listMsgs.add("Shauli3");
		listMsgs.add("Dov3");
		listMsgs.add("Or3");
		listMsgs.add("Shauli4");
		listMsgs.add("Dov4");
		listMsgs.add("Or4");

		
		// Go through each item in the array
        for (int iCurrent = 0; iCurrent < listMsgs.size(); iCurrent++)
        {
            // Create a TableRow and give it an ID
            TableRow tr = new TableRow(this);
            tr.setId(1000+iCurrent);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
            tr.setMinimumHeight(30);
            
            ImageView imageContact = new ImageView(this);
            imageContact.setId(2000+iCurrent);
            imageContact.setImageResource(R.drawable.notification_icon_status_bar);
            
            // Create a TextView to house the name of the province
            TextView labelTV = new TextView(this);
            labelTV.setId(3000+iCurrent);
            labelTV.setTextSize((float) 20.0);
            labelTV.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        	labelTV.setGravity(Gravity.LEFT);
            labelTV.setText("User: " + listMsgs.get(iCurrent));
        	labelTV.setPadding(5,0,0,0);

        	tr.addView(imageContact);
            tr.addView(labelTV);

            // Create a TextView to house the value of the after-tax income
            //TextView valueTV = new TextView(this);
            //valueTV.setId(current);
            //valueTV.setText("$0");
            //valueTV.setTextColor(Color.BLACK);
            //valueTV.setLayoutParams(new LayoutParams(
            //        LayoutParams.FILL_PARENT,
            //        LayoutParams.WRAP_CONTENT));
            //tr.addView(valueTV);

            // Add the TableRow to the TableLayout
            _tblContacts.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        }
		
	}

}
