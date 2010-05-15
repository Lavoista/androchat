package com.androchat;

import java.util.ArrayList;
import java.util.Date;

import twitter4j.*;

public class TwitterManager {

	private static TwitterManager m_Instance;
	private Twitter m_Twitter;
	private String m_UserName;
	private String m_Password;
	private int m_Interval;
	private boolean m_Sound;
	private boolean m_Vibration;
	private Date m_LastSync;
	
	private TwitterManager(){
	
	}
	
	public static TwitterManager getInstance(){
		if(m_Instance == null)
			m_Instance = new TwitterManager();
		return m_Instance;
	}
	
	public void Connect(String userName, String passWord){
		m_UserName = userName;
		m_Password = passWord;
		m_Twitter = new TwitterFactory().getInstance(m_UserName,m_Password);
		//TODO: load contacts and messages
	}
	
	public void setInterval(int interval){
		
	}
	
	public int getInterval(){
		return m_Interval;
	}
	
	public void setVibration(boolean vibration) {
		this.m_Vibration = vibration;
	}

	public boolean isVibration() {
		return m_Vibration;
	}

	public void setSound(boolean sound) {
		this.m_Sound = sound;
	}

	public boolean isSound() {
		return m_Sound;
	}

	public void Disconnect(){
		
	}
	
	public ArrayList<Contact> GetAllContacts() throws TwitterException{
		return null;
	}
	
	public void AddContact(Contact c) throws TwitterException{
		
	}
	
	public void RemoveContact(Contact c) throws TwitterException{
		
	}
	
	public ArrayList<DirectMessage> GetMessagesForContact(Contact c) throws TwitterException{
		return new ArrayList<DirectMessage>();
	}
	
	public void SendMessage(Contact contact, DirectMessage msg) throws TwitterException{
	
	}
	
	/**
	 * @return New messages since last Sync
	 */
	public ArrayList<DirectMessage> SyncMessages() throws TwitterException{
		return null;
	}
}
