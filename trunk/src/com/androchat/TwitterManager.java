package com.androchat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Message;
import winterwell.jtwitter.Twitter.User;

public class TwitterManager {

	private static TwitterManager m_Instance;
	private Twitter m_Twitter;
	private String m_strUserName;
	private String m_strPassword;
	private int m_nInterval;
	private boolean m_bSound;
	private boolean m_bVibration;
	private boolean m_bConnected;
	private long m_nMaxMsgNum;
	private HashMap<String, ArrayList<Message>> m_hashMessages;
	private List<User> m_arrUsers;
	private Timer m_Timer;
	
	private TwitterManager(){
		m_hashMessages = new HashMap<String, ArrayList<Message>>();
		m_nMaxMsgNum = 1;
		m_Timer = new Timer();
		m_nInterval = 0;
	}
	
	public static TwitterManager getInstance(){
		if(m_Instance == null)
			m_Instance = new TwitterManager();
		return m_Instance;
	}
	
	public void Connect(String userName, String passWord) throws TwitterException{
		m_bConnected = false;
		m_strUserName = userName;
		m_strPassword = passWord;
		m_Twitter = new Twitter(m_strUserName,m_strPassword);

		SyncMessages();
        m_bConnected = true;
	}
	
	public void Disconnect(){
		m_bConnected = false;
		m_Timer.cancel();
	}
		
	public void setInterval(int interval){
		m_nInterval = interval;
	}
	
	public int getInterval(){
		return m_nInterval;
	}
	
	public void setVibration(boolean vibration) {
		m_bVibration = vibration;
	}

	public boolean isVibration() {
		return m_bVibration;
	}

	public void setSound(boolean sound) {
		m_bSound = sound;
	}

	public boolean isSound() {
		return m_bSound;
	}

	public boolean isConected() {
		return m_bConnected;
	}

	public List<User> GetAllContacts(boolean useCache) throws TwitterException{
		if(!useCache || m_arrUsers == null){
			//TODO: paging, caching
			m_arrUsers = m_Twitter.getFollowers();
		}
		return m_arrUsers;
	}
	
	public ArrayList<Message> GetMessagesForContact(String strUserName) throws TwitterException{
		if(!m_hashMessages.containsKey(strUserName)){
			m_hashMessages.put(strUserName, new ArrayList<Message>());
		}
		return m_hashMessages.get(strUserName);
	}
	
	public void SendMessage(String strUserName, String strMsg) throws TwitterException{
		GetMessagesForContact(strUserName).add(m_Twitter.sendMessage(strUserName, strMsg));
	}
	
	private void SyncMessages() throws TwitterException{
		//TODO: paging, caching
		List<Message> messages = m_Twitter.getDirectMessages();
        for (Message message : messages) {
        	String strSenderScreenName = message.getSender().screenName;
        	if(!m_hashMessages.containsKey(strSenderScreenName)){
        		m_hashMessages.put(strSenderScreenName, new ArrayList<Message>());
        	}
        	if(message.getId() > m_nMaxMsgNum){
        		m_nMaxMsgNum = message.getId();
        	}
        	m_hashMessages.get(strSenderScreenName).add(message);
        }
        messages = m_Twitter.getDirectMessagesSent();
        for (Message message : messages) {
        	String strRecipientScreenName = message.getRecipient().getScreenName();
        	if(!m_hashMessages.containsKey(strRecipientScreenName)){
        		m_hashMessages.put(strRecipientScreenName, new ArrayList<Message>());
        	}
        	if(message.getId() > m_nMaxMsgNum){
        		m_nMaxMsgNum = message.getId();
        	}
        	m_hashMessages.get(strRecipientScreenName).add(message);
        }
        for(ArrayList<Message> arr : m_hashMessages.values()){
        	Collections.sort(arr, new Comparator<Message>() {
				public int compare(Message object1, Message object2) {
					return object1.getCreatedAt().compareTo(object2.getCreatedAt());
				}
        	});
        }
	}
}
