package com.androchat;

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
			
			// Set vibrate and sound if neccesary
			if (TwitterManager.getInstance().isSound())
			{
				notification.audioStreamType = AudioManager.STREAM_RING;
				Uri notificationSound = null;
				notificationSound = Uri.fromFile(getFileStreamPath(TWEET_SOUND_FILENAME));
				String str = notificationSound.getPath();
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


