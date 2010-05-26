package com.androchat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera.Size;
//import android.graphics.Color;
import android.os.Bundle;
//import android.widget.ScrollView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Conversion extends Activity {

	
	//private ScrollView _scvMain;
	private TableLayout _tblMessages;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.conversion);

		//this._scvMain = (ScrollView)this.findViewById(R.id.scvMain);
		this._tblMessages = (TableLayout)this.findViewById(R.id.tblMessages);
		
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
            
            
            // Create a TextView to house the name of the province
            TextView labelTV = new TextView(this);
            labelTV.setId(2000+iCurrent);
            labelTV.setText(listMsgs.get(iCurrent));
            labelTV.setTextSize((float) 25.0);
            labelTV.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
            labelTV.setWidth(100);

            if (iCurrent%3==0){
            	tr.setBackgroundColor(Color.LTGRAY);
            	labelTV.setTextColor(Color.BLACK);
            	labelTV.setGravity(Gravity.RIGHT);
            }
            else{
            	labelTV.setGravity(Gravity.LEFT);
            }
            
            labelTV.setText(labelTV.getText() + String.valueOf(labelTV.getWidth()) );
            
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
            _tblMessages.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        }
		
		
	}
	
}
