package com.androchat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Conversation extends Activity {

	// Data Members
	private TableLayout _tblMessages;
	private TextView 	_tvConversationName;
	private Button		_btnConverstionSend;
	private EditText	_txtConversationMessageBody;
	
	private String m_strUserName;
	private String m_strScreenName;
	
	private String getMsgDate( Date dt )
	{
		String strDate;
	
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
		this._tvConversationName = (TextView)this.findViewById(R.id.tvConversationName);

		this._btnConverstionSend = (Button)this.findViewById(R.id.btnConverstionSend);
		this._txtConversationMessageBody = (EditText)this.findViewById(R.id.txtConversationMessageBody);
		
		// TODO : Shauli this is how you get parameters
		// Use the const below and look for reference in Notifications.java
		Bundle extra = getIntent().getExtras();
		if (extra != null)
		{
			m_strScreenName = extra.getString(TwitterManager.getInstance().SCREEN_NAME).trim();
			m_strUserName = extra.getString(TwitterManager.getInstance().USER_NAME).trim();
		}
		else
		{
			m_strScreenName = "d_itskovich";
			m_strUserName = "Dovi Doverman";
		}
		

		try
		{
			ArrayList<Message> conversionMsgs = TwitterManager.getInstance().GetMessagesForContact(m_strScreenName);
			
			if (conversionMsgs.size()>0)
			{
				_tvConversationName.setText( "Showing Conversation with " + m_strUserName + " :" );
				
				// Go through each item in the array
		        for (int iCurrent = 0; iCurrent < conversionMsgs.size(); iCurrent++)
		        {
		            // Create a TableRow and give it an ID
		            TableRow tr = new TableRow(this);
		            tr.setId(1000+iCurrent);
		            tr.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		            tr.setMinimumHeight(30);
		            
		            
		            // Create a TextView to show the sender, date and message  
		            TextView labelTV = new TextView(this);
		            labelTV.setId(2000+iCurrent);
		            labelTV.setTextSize((float) 20.0);
		            labelTV.setLayoutParams(new LayoutParams(
		                    LayoutParams.FILL_PARENT,
		                    LayoutParams.FILL_PARENT));
		            labelTV.setWidth(270);
		
		            // Check who is the sender
		            if (conversionMsgs.get(iCurrent).getSender().screenName.toLowerCase().equals(m_strScreenName.toLowerCase()))
		            {
		            	// If the logged user is not the sender then make the TableRow aligned to left
		            	// and colored at light-gray.
		            	tr.setBackgroundColor(Color.LTGRAY);
		            	tr.setGravity(Gravity.RIGHT);
		            	labelTV.setTextColor(Color.BLACK);
		            	labelTV.setGravity(Gravity.RIGHT);
		            	labelTV.setPadding(0,0,8,0);
		            }
		            else
		            {
		            	labelTV.setGravity(Gravity.LEFT);
		            	labelTV.setPadding(5,0,0,0);
		            }
		
		            // Set the text of label
		            labelTV.setText( 	conversionMsgs.get(iCurrent).getSender().screenName + " " +
		            					getMsgDate(conversionMsgs.get(iCurrent).getCreatedAt()) + ":\n" + 
		            					conversionMsgs.get(iCurrent).getText() );
		                        
		            // Add the label into the TableRow
		            tr.addView(labelTV);
		
		            // Add the TableRow to the TableLayout
		            _tblMessages.addView(tr, new TableLayout.LayoutParams(
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
	            labelTV.setText( "There are no message between you and the viewed user." );
	        	labelTV.setPadding(5,0,0,0);
	            labelTV.setWidth(270);
	
	            tr.addView(labelTV);
	
	            // Add the TableRow to the TableLayout
	            _tblMessages.addView(tr, new TableLayout.LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	        }
			
	        
	        //_tblMessages.scrollBy(0, 1000);
		}
		catch (TwitterException e) {
    		new AlertDialog.Builder(Conversation.this)
  	      	.setMessage(e.getMessage())
  	      	.show();
		}
		
		_btnConverstionSend.setOnClickListener( new View.OnClickListener()
		{
		public void onClick(View v) {
			if (m_strScreenName!=null && m_strScreenName!="")
			{
				boolean bMsgSentSuccess = false;
				try
				{
					TwitterManager.getInstance().SendMessage(m_strScreenName, _txtConversationMessageBody.getText().toString());
					bMsgSentSuccess = true;	
				}
				catch (TwitterException ex)
				{
	        		new AlertDialog.Builder(Conversation.this)
	        	      .setMessage("Error Ocoured :\n" + ex.getMessage())
	        	      .show();		
				}
				
				if (bMsgSentSuccess)
				{
					Intent iSettings = new Intent(Conversation.this, ContactList.class);
					startActivity(iSettings);
					finish();

	        		new AlertDialog.Builder(Conversation.this)
	        	      .setMessage("Message was sent !")
	        	      .show();	
				}
			}
			else
			{
	    		new AlertDialog.Builder(Conversation.this)
	    	      .setMessage("Please choose a contact")
	    	      .show();
			}
		}
	});
		
	}

}
