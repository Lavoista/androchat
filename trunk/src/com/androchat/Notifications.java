package com.androchat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import winterwell.jtwitter.Twitter.Message;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;


public class Notifications extends Service implements INotifier{

	private static final String TWEET_SOUND_FILENAME = "tweet.ogg";
	private static final long[] VIBRATION_PATTERN = new long[] { 0, 100, 60, 100 };
	private NotificationManager mNotificationManager;

	public IBinder onBind(Intent intent) {
	  return null;
	}

	@Override
	public void onCreate() {
	  super.onCreate();
	  TwitterManager.getInstance().setNotifier(this);
	}

	@Override
	public void onDestroy() {
	  super.onDestroy();

	}
	
	public void Notify(List<Message> lstMsg)
	{
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		for ( int i=0; i<lstMsg.size(); i++ )
		{
			// TODO : Should check here if the message wasn't by the connected user. 
			Message msg = lstMsg.get(i);
			
			//Instantiate the Notification
			int icon = R.drawable.notification_icon_status_bar;
			CharSequence tickerText = "AndroChat - New message";
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon, tickerText, when);
			
			//Define the Notification's expanded message and Intent
			Context context = getApplicationContext();
			CharSequence contentTitle = "AndroChat - New Message";
			CharSequence contentText = "Message from " + msg.getSender().getScreenName();
			
			// Declare intent to the notification
			Intent conversationIntent = new Intent(Notifications.this, Conversation.class);
			conversationIntent.putExtra(TwitterManager.getInstance().SCREEN_NAME, msg.getSender().getScreenName());
			conversationIntent.putExtra(TwitterManager.getInstance().USER_NAME, msg.getSender().getName()); 
			Intent notificationIntent = conversationIntent;
			
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			
			// Set vibrate and sound if necessary
			if (TwitterManager.getInstance().isSound())
			{
				notification.audioStreamType = AudioManager.STREAM_RING;
				Uri notificationSound = null;
				notificationSound = Uri.fromFile(getFileStreamPath(TWEET_SOUND_FILENAME));
				
				// Check if succeed opening the file
				boolean exists = false;
				for (String i1 : fileList()) {
					if (i1.equals(TWEET_SOUND_FILENAME)) {
						exists = true;
						break;
					}
				}
				if (!exists) 
				{
					// This should only ever have to be done once per
					// installation. If the file is ever changed then the
					// filename must be changed ("tweet2.ogg" or whatever), and
					// you must delete the files from old versions here.
					try {
						InputStream ris = getResources().openRawResource(
							R.raw.tweet);
						FileOutputStream fos = openFileOutput(
							TWEET_SOUND_FILENAME, MODE_WORLD_READABLE);
						byte[] buffer = new byte[8192];
						int read;
						while ((read = ris.read(buffer)) > 0) {
							fos.write(buffer, 0, read);
						}
						ris.close();
						fos.close();
					} catch (IOException e) {
						deleteFile(TWEET_SOUND_FILENAME);
					}
				}
				
				notification.sound = notificationSound;	
			}
			if (TwitterManager.getInstance().isVibration()) 
			{
				notification.vibrate = VIBRATION_PATTERN;
			}
			
			//Pass the Notification to the NotificationManager 
			mNotificationManager.notify(i, notification);
		}
	}
}


