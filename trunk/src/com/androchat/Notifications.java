package com.androchat;

import java.util.List;

import winterwell.jtwitter.Twitter.Message;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


public class Notifications extends Service implements INotifier{

	//public static ServiceUpdateUIListener UI_UPDATE_LISTENER;
	
	//private static MainActivity MAIN_ACTIVITY;

	private NotificationManager mNotificationManager;

	//private Timer timer = new Timer();
	
	//private int nNotificationCounter = 1000000;
	
	//private static final long UPDATE_INTERVAL = 50000;

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

	  //_shutdownService();

	}
	
	/*private void _startService()
	 {

	  timer.scheduleAtFixedRate(
	      new TimerTask() {
	        public void run() {

	        	// Check for new msgs
	        	
	        	// If there are new message, 
	        	// for each msg show new notification.
	        	// INCREASE the notification id.
	        	Notify();
	        }
	      },
	      0,
	      UPDATE_INTERVAL);
	  //Log.i(getClass().getSimpleName(), "Timer started!!!");
	}
	
	private void _shutdownService() {
		  if (timer != null) timer.cancel();
		  //Log.i(getClass().getSimpleName(), "Timer stopped!!!");
	}
	*/
	
	public void Notify(List<Message> lstMsg)
	{
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		//Instantiate the Notification
		int icon = R.drawable.notification_icon_status_bar;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		
		//Define the Notification's expanded message and Intent
		Context context = getApplicationContext();
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		Intent notificationIntent = new Intent(this, NewMessage.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	
		//Pass the Notification to the NotificationManager 
		mNotificationManager.notify(1, notification);
	}
}
























/*
public class Notifications1 extends Service 
{
	
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		Notify();
	}

	private static final int HELLO_ID = 1;
	
	public void Notify()
	{
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		//Instantiate the Notification
		int icon = R.drawable.notification_icon_status_bar;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		
		//Define the Notification's expanded message and Intent
		Context context = getApplicationContext();
		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		Intent notificationIntent = new Intent(this, NewMessage.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	
		//Pass the Notification to the NotificationManager 
		mNotificationManager.notify(HELLO_ID, notification);	
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
*/
