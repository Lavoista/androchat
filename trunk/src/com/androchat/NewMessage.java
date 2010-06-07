package com.androchat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
		
		try
		{
			List<User> followers = TwitterManager.getInstance().GetAllFollowers(true);
			 
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
		}
		finally
		{
			if (listUsers.size()>0)
			{
				adapter = new ArrayAdapter<String>(NewMessage.this ,android.R.layout.simple_spinner_item, listUsers);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				_spnrAddresseeName.setAdapter(adapter);
			}
			else
			{
				new AlertDialog.Builder(NewMessage.this)
			      .setMessage("Error on getting followers")
			      .show();				
			}
		}

		// On send message :
		_btnSendMessage.setOnClickListener( new View.OnClickListener()
		{
			public void onClick(View v) {
				new SendMessageDataTask().execute();
			}
			
		});
	}
	
	private class SendMessageDataTask extends AsyncTask<String, Void, Void>
	{		
		ProgressDialog dialogSendMessage = new ProgressDialog(NewMessage.this);
        boolean bEx_Exception = false;
        boolean bEx_TwitterException = false;

		boolean bMsgSentSuccess = false;
		boolean bValidParams = false;


    	private String msgBody = "";
    	private String msgAddress = "";
		
		
		@Override
		protected void onPreExecute() {		

			if (_spnrAddresseeName.getSelectedItemPosition()>-1)
			{
				if (_txtMessageBody.getText().length() > 0)
				{
					if (_txtMessageBody.getText().length() <= 140)
					{
						msgBody = _txtMessageBody.getText().toString();
						msgAddress = _spnrAddresseeName.getSelectedItem().toString();
						bValidParams = true;

						this.dialogSendMessage.setMessage("Sending Message...");
						this.dialogSendMessage.setCancelable(true);
						this.dialogSendMessage.setCanceledOnTouchOutside(false);
						this.dialogSendMessage.show();	
					}
					else
					{
						bValidParams = false;
						new AlertDialog.Builder(NewMessage.this)
					      .setMessage(getString(R.string.error_message_too_long))
					      .show();
					}
				}
				else
				{
					bValidParams = false;
					new AlertDialog.Builder(NewMessage.this)
				      .setMessage(getString(R.string.error_message_empty))
				      .show();
				}
			}
			else
			{
				bValidParams = false;
				new AlertDialog.Builder(NewMessage.this)
			      .setMessage(getString(R.string.error_choose_contact))
			      .show();
			}
		}
		
		
		@Override
		protected Void doInBackground(String... params) {
			
			try
			{
				if (bValidParams)
				{
//					boolean isConnected = false;
//					
//					try
//					{
//						ConnectivityManager cm = (ConnectivityManager) NewMessage.this.getSystemService(Context.CONNECTIVITY_SERVICE);		
//						NetworkInfo ni = cm.getActiveNetworkInfo();
//						isConnected = ni.isConnected();
//					}
//					catch (Exception e)
//					{}
//					finally
//					{
//						if (isConnected)
//						{
							TwitterManager.getInstance().SendMessage(msgAddress, msgBody);
//						}
//						else
//						{
//				    		new AlertDialog.Builder(NewMessage.this)
//				  	      	.setMessage("No internet conectivity.\nCannot send message.")
//				  	      	.setTitle("Error")
//				  	      	.setNegativeButton("Exit", new OnClickListener() {
//								
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									finish();
//								}
//							})
//				  	      	.show();
//						}
//					}
					bMsgSentSuccess = true;
				}
				else
				{
					bMsgSentSuccess = false;					
				}
			}
			catch (TwitterException ex)
			{
				bMsgSentSuccess = false;
				bEx_TwitterException = false;
			}
			catch (Exception ex)
			{
				bMsgSentSuccess = false;
				bEx_Exception = false;				
			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {

			if (bValidParams)
			{
				if (bMsgSentSuccess)
				{
	        		new AlertDialog.Builder(NewMessage.this)
	        	      .setMessage(getString(R.string.message_send_success))
	        	      .show();	
	        		
					Intent iContactList = new Intent(NewMessage.this, ContactList.class);
					iContactList.putExtra(TwitterManager.getInstance().MESSAGE_SUCCESS, msgAddress);
					startActivity(iContactList);
					finish();
				}
				else
				{
					if (bEx_Exception)
					{
		        		new AlertDialog.Builder(NewMessage.this)
		        	      .setMessage("General error on send message.\nTry Again..")
		        	      .show();						
					}
					else
					{
						if ( bEx_TwitterException)
						{
			        		new AlertDialog.Builder(NewMessage.this)
			        	      .setMessage("Send message failed.\nTry Again..")
			        	      .show();							
						}
					}
				}
			}
		}
		
		
		@Override
		protected void onCancelled() {

    		new AlertDialog.Builder(NewMessage.this)
    	      .setMessage("Send message canceled")
    	      .show();
		}
	}

	
}
