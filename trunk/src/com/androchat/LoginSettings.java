package com.androchat;

import java.text.ChoiceFormat;
import java.text.MessageFormat;
import java.util.List;

import oauth.signpost.OAuth;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;


public class LoginSettings extends Activity {

	public final static int[] INTERVALS = { 1, 2, 3, 4, 5, 10, 15 };
	final static long[] VIBRATION_PATTERN = new long[] { 0, 100, 60, 100 };
	final static int DEFAULT_INTERVAL_INDEX = 2;

	// Creating controls that need implementation:
	private CheckBox chkSound; 
	private CheckBox chkVibaration;
	private Button btnLogIn;
	private Button btnSave;
	private Spinner Interval;
	private TextView lblConnectedUser;

	public void onCreate(Bundle savedInstanceState)	{

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

		// Get controls from the layout
		this.btnLogIn = (Button)this.findViewById(R.id.btnLogin);
		this.chkSound = (CheckBox)this.findViewById(R.id.chkSound);
		this.chkVibaration = (CheckBox)this.findViewById(R.id.chkVibaration);
		this.Interval = (Spinner)this.findViewById(R.id.interval);
		this.btnSave = (Button)this.findViewById(R.id.btnSave);
		this.lblConnectedUser = (TextView)this.findViewById(R.id.lblConnectedUser);

		// Set up the interval choices:
		final String[] intervalChoiceText = new String[INTERVALS.length];

		for (int i = 0; i < INTERVALS.length; ++i) {
			int option = INTERVALS[i];
			intervalChoiceText[i] = MessageFormat.format(
					new ChoiceFormat(getString(R.string.interval_option))
					.format(option), option);
		}

		ArrayAdapter<String> a = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, intervalChoiceText);
		a.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		Interval.setAdapter(a);

		// Get Preferences
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Interval.setSelection(pref.getInt("interval", DEFAULT_INTERVAL_INDEX));
		chkSound.setChecked(pref.getBoolean("sound", true));
		chkVibaration.setChecked(pref.getBoolean("vibaration", true));
		if(TwitterManager.getInstance().isConected()){
			lblConnectedUser.setText( MessageFormat.format(getString(R.string.connected_user), TwitterManager.getInstance().getConnectedUserName()));
			btnSave.setEnabled(true);
		}
		else{
			lblConnectedUser.setText("Not Connected");
			btnSave.setEnabled(false);
		}
		

		// Define events to chkSound
		this.chkSound.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (chkSound.isChecked()) {
					MediaPlayer mp = MediaPlayer.create(
							LoginSettings.this, R.raw.tweet);
					mp.setOnCompletionListener(new OnCompletionListener() {
						public void onCompletion(MediaPlayer mp) {
							mp.release();
						}
					});
					mp.start();

				}
			}
		});

		// Define events to chkVibration
		this.chkVibaration.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (chkVibaration.isChecked()) {
					((Vibrator) getSystemService(VIBRATOR_SERVICE))
					.vibrate(VIBRATION_PATTERN, -1);
				}
			}
		});

		
		
		// Define events to the btnLogin
		this.btnLogIn.setOnClickListener(new OnClickListener() {	 
			public void onClick(View v) {

		    	final ProgressDialog dialogLogIn = ProgressDialog.show(LoginSettings.this, "", 
		                "Starting Browser.\nPlease wait...",true);
		    	dialogLogIn.setCancelable(true);
		    	dialogLogIn.setCanceledOnTouchOutside(false);
	
		    	//Handler handler=new Handler();

		    	//handler.post(new Runnable(){public void run(){dialogDisconnect.dismiss();}});
		    	
		    	new Thread()
		    	{
	                public void run() 
	                {
                        try
                        {
							if(TwitterManager.getInstance().isConected()){
								TwitterManager.getInstance().Disconnect();
								Editor e = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
								e.remove("token");
								e.remove("tokensecret");
								e.commit();
								lblConnectedUser.setText("Not Connected");
								btnSave.setEnabled(false);
							}
							LoginSettings.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterManager.getInstance().GetAuthUrl())));
                        }
                        catch (Exception e) { }
                        // Dismiss the Dialog
                        dialogLogIn.dismiss();
	                }
		    	}.start();
			}
		});

		// Define events to btnSave
		this.btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				new LoginAndSaveDataTask().execute();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		Uri uri = this.getIntent().getData();  
		if (uri != null && uri.toString().startsWith("androchat://twitt")) {
			try{
				TwitterManager.getInstance().ConnectAuth(uri.getQueryParameter(OAuth.OAUTH_VERIFIER));
				lblConnectedUser.setText( MessageFormat.format(getString(R.string.connected_user), TwitterManager.getInstance().getConnectedUserName()));
				btnSave.setEnabled(true);
			}
			catch(TwitterException e){
				new AlertDialog.Builder(LoginSettings.this)
				.setMessage(e.getMessage())
				.show();				
			}
		}
	}

	
	

	private class LoginAndSaveDataTask extends AsyncTask<String, Void, Void> {

		ProgressDialog dialogGetContacts = new ProgressDialog(LoginSettings.this);
        boolean bEx_Exception = false;
        boolean bEx_TwitterException = false;
        
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			this.dialogGetContacts.setMessage("Connecting to Twitter.\nPlease wait...");
			this.dialogGetContacts.setCancelable(true);
			this.dialogGetContacts.setCanceledOnTouchOutside(false);
			this.dialogGetContacts.show();
		}
		
		@Override
		protected Void doInBackground(final String... params) {
			
			try
            {
				int nInterval =  INTERVALS[Interval.getSelectedItemPosition()];
				int nIntervalSelectedPosition = Interval.getSelectedItemPosition();
				boolean bSound = chkSound.isChecked();
				boolean bVibaration = chkVibaration.isChecked();

				TwitterManager.getInstance().setInterval(nInterval);
				TwitterManager.getInstance().setSound(bSound);
				TwitterManager.getInstance().setVibration(bVibaration);

				// Save Preferences
				Editor e = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
				e.putString("token", TwitterManager.getInstance().getAccessToken());
				e.putString("tokensecret", TwitterManager.getInstance().getAccessTokenSecret());
				e.putInt("interval", nIntervalSelectedPosition);
				e.putBoolean("sound", bSound);
				e.putBoolean("vibaration", bVibaration);
				e.commit();
            }
			catch (TwitterException te)
			{
				bEx_TwitterException = true;
			}
            catch (Exception e) 
            {
            	bEx_Exception = true;
            }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(final Void result) {
			
			super.onPostExecute(result);

			if (this.dialogGetContacts.isShowing()) {
				this.dialogGetContacts.dismiss();
			}
			
			if (bEx_Exception == false && bEx_TwitterException == false)
			{
				Intent iContactList = new Intent(LoginSettings.this, ContactList.class);
				startActivity(iContactList);
				finish();
			}
			else
			{				
				if (bEx_TwitterException == true)
				{
		    		new AlertDialog.Builder(LoginSettings.this)
		    	      .setMessage("Error on connecting to Twitter.\nPlease close application and try to login again.")
		    	      .show();
				}
				else
				{
					 if (bEx_Exception == true)
					 {
				    		new AlertDialog.Builder(LoginSettings.this)
				    	      .setMessage("General error.")
				    	      .show();
					 }
				}
			}
		}
	
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
    		new AlertDialog.Builder(LoginSettings.this)
    	      .setMessage("Operation Canceled.")
    	      .show();
		}
	}
}
