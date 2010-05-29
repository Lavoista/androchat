package com.androchat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import winterwell.jtwitter.Twitter.Message;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Conversation extends Activity {

	private TableLayout _tblMessages;

	private String getMsgDate( Date dt )
	{
		String strDate;
		Date dtNow = new Date();
	
		Date dtDiffNow = getDateDiff(dt);

		if (dtDiffNow.getDay() == 0)
		{
			if (dtDiffNow.getHours() < 1)
			{
				strDate = "("+dtDiffNow.getMinutes() + " minutes ago)";
			}
			else
			{
				strDate = "("+dtDiffNow.getHours() + " hours ago)";
			}
		}
		else
		{
			if (dtDiffNow.getDay() < 30)
			{
				strDate = "("+dtDiffNow.getDate() + " days ago)";
			}
			else
			{
				int nDay = dt.getDay() + 1;
				int nMonth = dt.getMonth() + 1;
				int nYear = dt.getYear() + 1;
				String sDay;
				String sMonth;
				String sYear;

				if (nDay<10)
					sDay = "0" + nDay;
				else
					sDay = ""+nDay;

				if (nMonth<10)
					sMonth = "0" + nMonth;
				else
					sMonth = ""+nMonth;

				if (nYear<10)
					sYear = "0" + nYear;
				else
					sYear = ""+nYear;
					
				
				if (dtDiffNow.getYear() < 1)
				{
					strDate = "("+sDay + "/" + sMonth+")";
				}
				else
				{
					strDate = "("+sDay + "/" + sMonth + "/" + sYear+")";	
				}
			}
		}
		
		return strDate;
		
	}

	private Date getDateDiff(Date dt) {
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.YEAR, (-1*(dt.getYear()) ));
		cal.add(Calendar.MONTH, (-1*(dt.getMonth())) );
		cal.add(Calendar.DATE, (-1*(dt.getDate())) );
		cal.add(Calendar.HOUR, (-1*(dt.getHours()) ));
		cal.add(Calendar.MINUTE, (-1*(dt.getMinutes())) );
		cal.add(Calendar.SECOND, (-1*(dt.getSeconds())) );

		Date dtDiffNow = new Date(cal.getTimeInMillis());
		
		return dtDiffNow;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.conversation);

		this._tblMessages = (TableLayout)this.findViewById(R.id.tblMessages);
		
		String currContact = "d_itskovich";
		
		ArrayList<Message> conversionMsgs = TwitterManager.getInstance().GetMessagesForContact(currContact);
		
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
        for (int iCurrent = 0; iCurrent < conversionMsgs.size(); iCurrent++)
		//for (int iCurrent = 0; iCurrent < listMsgs.size(); iCurrent++)
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
            labelTV.setTextSize((float) 20.0);
            labelTV.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
            labelTV.setWidth(270);

            if (conversionMsgs.get(iCurrent).getSender().screenName.toLowerCase().equals(currContact.toLowerCase())){
            //if (listMsgs.get(iCurrent).contains("Shauli")){
            	tr.setBackgroundColor(Color.LTGRAY);
            	tr.setGravity(Gravity.RIGHT);
            	labelTV.setTextColor(Color.BLACK);
            	labelTV.setGravity(Gravity.RIGHT);
            	labelTV.setPadding(0,0,8,0);
            }
            else{
            	labelTV.setGravity(Gravity.LEFT);
            	labelTV.setPadding(5,0,0,0);
            }
            

        	//labelTV.setText( listMsgs.get(iCurrent));
            labelTV.setText( 	conversionMsgs.get(iCurrent).getSender().screenName + " " +
            					getMsgDate(conversionMsgs.get(iCurrent).getCreatedAt()) + ":\n" + 
            					conversionMsgs.get(iCurrent).getText() );
            
            //labelTV.setText(labelTV.getText() + String.valueOf(labelTV.getWidth()) );
            
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
        
        //_tblMessages.scrollBy(0, 1000);
        

		
	}
	

	
}
