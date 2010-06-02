package com.androchat;

import java.util.List;

import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Message;
import winterwell.jtwitter.Twitter.User;

public class TwitterManagerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Test");
		try {
			TwitterManager twitter = TwitterManager.getInstance();		
			twitter.ConnectAuth("96330083-MIMDo1L2EtsOU1rymXtmPwaxLFrVc7i55PXMavfGY","pxKCw9vWDkNShbaII8wrOeQVIebKJPg6FJ1UZSEsqqw");
			System.out.println("Connected User: " + twitter.getConnectedUserName());
			List<User> arr = twitter.GetAllContacts(false);
			for(User u : arr){
				System.out.println("msg for " + u.getScreenName() + ":");
				//twitter.SendMessage(u.getScreenName(), "Test");
				for(Message msg : twitter.GetMessagesForContact(u.getScreenName())){
					System.out.println(msg.getSender().getScreenName() + " " + msg.getCreatedAt().toString() + " : " + msg.getText());
				}
			}
			System.out.println("End Test");
			System.exit(0);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
