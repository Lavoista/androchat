package com.androchat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
		Date dtNow = new Date();
	
		Date dtDiffNow = getDateDiff(dt);

		if (dtDiffNow.getDay() == 0)
		{
			if (dtDiffNow.getHours() < 1)
			{
				if (dtDiffNow.getMinutes()==0)
				{
					strDate = "("+dtDiffNow.getSeconds() + " seconds ago)";
				}
				else
				{
					strDate = "("+dtDiffNow.getMinutes() + " minutes ago)";
				}
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
					strDate = "("+sDay + "/" + sMonth + " on " + dt.getHours() + ":" + dt.getMinutes()  + ")";
				}
				else
				{
					strDate = "("+sDay + "/" + sMonth + "/" + sYear+ " on " + dt.getHours() + ":" + dt.getMinutes()  + ")";	
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

			try
			{
				ArrayList<Message> conversionMsgs = TwitterManager.getInstance().GetMessagesForContact(m_strScreenName);
				
				if (conversionMsgs.size()>0)
				{
					_tvConversationName.setText( "Showing Conversation with " + m_strUserName + " :" );
					
					// Go through each item in the array
			        for (int iCurrent = 0; iCurrent < conversionMsgs.size(); iCurrent++)
			        {
			            // Create an Outer TableRow to contain all inner texts
			            TableRow trOuter = new TableRow(this);
			            trOuter.setId(1000+iCurrent);
			            trOuter.setLayoutParams(new LayoutParams(
			                    LayoutParams.FILL_PARENT,
			                    LayoutParams.FILL_PARENT));
			            trOuter.setMinimumHeight(30);
			            
			            
			            // Create an Inner TableLayout to hold two table rows.
			            TableLayout tlInner = new TableLayout(this);
			            tlInner.setId(2000 + iCurrent);
			            tlInner.setLayoutParams(new LayoutParams(
			                    LayoutParams.WRAP_CONTENT,
			                    LayoutParams.FILL_PARENT));
			            
			            
			            // Create a TableRows, one for user name and one for the message body
			            TableRow trInner_username_date = new TableRow(this);
			            trInner_username_date.setId(3000+iCurrent);
			            trInner_username_date.setLayoutParams(new LayoutParams(
			                    LayoutParams.FILL_PARENT,
			                    LayoutParams.FILL_PARENT));

			            TableRow trInner_Message = new TableRow(this);
			            trInner_Message.setId(4000+iCurrent);
			            trInner_Message.setLayoutParams(new LayoutParams(
			                    LayoutParams.FILL_PARENT,
			                    LayoutParams.FILL_PARENT));
			            
				        
			            // Create a two TextView to show the sender, date and message  
			            TextView tvLabel_username_date = new TextView(this);
			            tvLabel_username_date.setId(5000+iCurrent);
			            tvLabel_username_date.setTextSize((float) 13.0);
			            tvLabel_username_date.setLayoutParams(new LayoutParams(
			                    LayoutParams.FILL_PARENT,
			                    LayoutParams.FILL_PARENT));
			            tvLabel_username_date.setWidth(270);
			            
			            TextView tvLabel_Message = new TextView(this);
			            tvLabel_Message.setId(6000+iCurrent);
			            tvLabel_Message.setTextSize((float) 20.0);
			            tvLabel_Message.setLayoutParams(new LayoutParams(
			                    LayoutParams.FILL_PARENT,
			                    LayoutParams.FILL_PARENT));
			            tvLabel_Message.setWidth(270);
			            
			            
			            // Create a row seperator
			            View lineSepartor = new View(this);
			            lineSepartor.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));

			            // Set the label's texts
			            tvLabel_username_date.setText( 	conversionMsgs.get(iCurrent).getSender().screenName + " " +
			            					getMsgDate(conversionMsgs.get(iCurrent).getCreatedAt()) + ":" );
			            
			            tvLabel_Message.setText(conversionMsgs.get(iCurrent).getText());
			            
			            
			            // Insert each label into each table row
			            trInner_username_date.addView(tvLabel_username_date);
			            trInner_Message.addView(tvLabel_Message);
			            
			            // Insert both table rows into inner table
			            tlInner.addView(trInner_username_date);
			            tlInner.addView(trInner_Message);
			            
			
			            // Check who is the sender
			            if (conversionMsgs.get(iCurrent).getSender().screenName.toLowerCase().equals(m_strScreenName.toLowerCase()))
			            {
			            	// If the logged user is not the sender then make the TableRow aligned to left
			            	// and colored at light-gray.
			            	trOuter.setBackgroundColor(Color.LTGRAY);
			            	trOuter.setGravity(Gravity.RIGHT);

				            lineSepartor.setBackgroundColor(Color.BLACK);
			            	
			            	tvLabel_username_date.setTextColor(Color.BLACK);
			            	tvLabel_username_date.setGravity(Gravity.RIGHT);
			            	tvLabel_username_date.setPadding(0,0,8,0);
			            	
			            	tvLabel_Message.setTextColor(Color.BLACK);
			            	tvLabel_Message.setGravity(Gravity.RIGHT);
			            	tvLabel_Message.setPadding(0,0,8,0);
			            }
			            else
			            {
				            lineSepartor.setBackgroundColor(Color.LTGRAY);
				            
			            	tvLabel_username_date.setGravity(Gravity.LEFT);
			            	tvLabel_username_date.setPadding(5,0,0,0);

			            	tvLabel_Message.setGravity(Gravity.LEFT);
			            	tvLabel_Message.setPadding(5,0,0,0);
			            }


			            // Add the inner table into the outer TableRow
			            trOuter.addView(tlInner);
			
			            // Add the outer TableRow to the outer TableLayout
			            _tblMessages.addView(trOuter, new TableLayout.LayoutParams(
			                    LayoutParams.FILL_PARENT,
			                    LayoutParams.FILL_PARENT));
			            
			            _tblMessages.addView(lineSepartor);
			        }
				}
		        else
		        {
		        	// Create and show message for no conversation messages
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
		            labelTV.setText( R.string.error_no_messages );
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
		}
		else
		{
    		new AlertDialog.Builder(Conversation.this)
  	      	.setMessage("Missing user name.")
  	      	.show();
		}
		
		_btnConverstionSend.setOnClickListener( new View.OnClickListener()
		{
		public void onClick(View v) {
			if (m_strScreenName!=null && m_strScreenName!="")
			{
				if (_txtConversationMessageBody.getText().length() > 140)
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
		        	      .setMessage(R.string.error_common_prefix + " :\n" + ex.getMessage())
		        	      .show();		
					}
					
					if (bMsgSentSuccess)
					{
		        		new AlertDialog.Builder(Conversation.this)
		        	      .setMessage(R.string.message_send_success)
		        	      .show();	
		        		
						Intent iSettings = new Intent(Conversation.this, ContactList.class);
						startActivity(iSettings);
						finish();
					}
				}
				else
				{
	        		new AlertDialog.Builder(Conversation.this)
	        	      .setMessage(R.string.error_message_too_long)
	        	      .show();
				}
			}
			else
			{
	    		new AlertDialog.Builder(Conversation.this)
	    	      .setMessage(R.string.error_choose_contact)
	    	      .show();
			}
		}
	});
		
	}

}
