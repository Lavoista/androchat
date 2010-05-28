package com.androchat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import winterwell.jtwitter.*;
import winterwell.jtwitter.Twitter.*;

public class TwitterManagerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
            System.out.println("No TwitterID/Password specified.");
            System.out.println(
                "Usage: java com.androchat.TwitterManagerTest ID Password");
            System.exit( -1);
        }
		System.out.println("Test");
		try {
			TwitterManager twitter = TwitterManager.getInstance();		
			twitter.Connect(args[0], args[1], 10);
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
