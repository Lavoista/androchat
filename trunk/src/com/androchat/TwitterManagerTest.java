package com.androchat;


import java.util.Date;
import java.util.List;

import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Message;
import winterwell.jtwitter.Twitter.User;

public class TwitterManagerTest implements INotifier{
	private int n =0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Test");
		try {
			TwitterManager twitter = TwitterManager.getInstance();		
			twitter.setNotifier(new TwitterManagerTest());
			twitter.ConnectAuth("96330083-MIMDo1L2EtsOU1rymXtmPwaxLFrVc7i55PXMavfGY","pxKCw9vWDkNShbaII8wrOeQVIebKJPg6FJ1UZSEsqqw");
			System.out.println(new Date().toString());
			twitter.setInterval(1);
			
			System.out.println("Connected User: " + twitter.getConnectedUserName());
			List<User> arr = twitter.GetAllContacts(false);
			for(User u : arr){
				System.out.println("msg for " + u.getScreenName() + ":");
				//twitter.SendMessage(u.getScreenName(), "Test");
				for(Message msg : twitter.GetMessagesForContact(u.getScreenName())){
					System.out.println(msg.getSender().getScreenName() + " " + msg.getCreatedAt().toString() + " : " + msg.getText());
				}
			}
			//System.out.println("End Test");
			//System.exit(0);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	@Override
	public void Notify(List<Message> lstMsg) {
		System.out.println();
		System.out.println("Sync");
		System.out.println(new Date().toString());
		TwitterManager twitter = TwitterManager.getInstance();		
		for(Message msg : lstMsg){
			System.out.println(msg.getSender().getScreenName() + " " + msg.getCreatedAt().toString() + " : " + msg.getText());
		}
		if(n == 3){
			System.out.println("End Test");
			System.exit(0);
		}else if(n == 2){
			twitter.setInterval(1);
		}else if(n == 1){
			twitter.setInterval(2);
		}
		n++;
		
	}

}
