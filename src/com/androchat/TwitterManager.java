package com.androchat;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Message;
import winterwell.jtwitter.Twitter.User;
import android.util.Log;

public class TwitterManager {

	// Consts
	public  final String USER_NAME = "UserName"; 
	public  final String SCREEN_NAME = "ScreenName"; 
	public  final String MESSAGE_SUCCESS = "MessageSuccess"; 
	private static final String JTWITTER_OAUTH_KEY = "ZWSmAuCT1ycu3mItLAD4A";
	private static final String JTWITTER_OAUTH_SECRET = "NQg05yLDD6mnLSaVHUatXwDSSO0MDbb590s9dTVb8z8";
	private static final String JTWITTER_CALLBACK_URL = "androchat://twitt";
	
	private static TwitterManager m_Instance;
	private Twitter m_Twitter;
	private int m_nInterval;
	private boolean m_bSound;
	private boolean m_bVibration;
	private boolean m_bConnected;
	private long m_nMaxMsgNum;
	private long m_nMaxSentMsgNum;
	private HashMap<String, ArrayList<Message>> m_hashMessages;
	private List<User> m_arrFollowers;
	private List<User> m_arrContacts;
	private Timer m_Timer;
	private INotifier m_Notifier;
	private OAuthSignpostClient m_OAuthClient;
	private String m_strUserName;
	
	private TwitterManager(){
		m_hashMessages = new HashMap<String, ArrayList<Message>>();
		m_arrContacts = new ArrayList<User>();
		m_nMaxMsgNum = 1;
		m_nMaxSentMsgNum = 1;
		m_nInterval = 0;
		m_strUserName = "";
		m_bConnected = false;
	}
	
	public static TwitterManager getInstance(){
		if(m_Instance == null)
			m_Instance = new TwitterManager();
		return m_Instance;
	}
	
	public void Connect(String userName, String passWord) throws TwitterException{
		if(!m_bConnected){
			m_Twitter = new Twitter(userName, passWord);
	
			SyncMessages(true);
	        m_bConnected = true;
		}
	}
	
	public String GetAuthUrl(){
		m_OAuthClient = new OAuthSignpostClient(JTWITTER_OAUTH_KEY, JTWITTER_OAUTH_SECRET, JTWITTER_CALLBACK_URL);
		return m_OAuthClient.authorizeUrl();
	}
	
	public void ConnectAuth(String accessToken, String accessTokenSecret) throws TwitterException{
		if(!m_bConnected){
			m_OAuthClient = new OAuthSignpostClient(JTWITTER_OAUTH_KEY, JTWITTER_OAUTH_SECRET, accessToken, accessTokenSecret);
			m_Twitter = new Twitter(null,m_OAuthClient);
			SyncMessages(true);
	        m_bConnected = m_OAuthClient.canAuthenticate();
		}
	}
	
	public void ConnectAuth(String verifier) throws TwitterException{
		if(!m_bConnected){
			m_OAuthClient.setAuthorizationCode(verifier);
			m_Twitter = new Twitter(null,m_OAuthClient);
			SyncMessages(true);
	        m_bConnected = m_OAuthClient.canAuthenticate();
		}
	}
	
	public String getAccessToken(){	
		return m_OAuthClient.getAccessToken();
	}
	
	public String getAccessTokenSecret(){
		return m_OAuthClient.getAccessTokenSecret();
	}

	/**
	 * @return Connected UserName screen name (not working if the user don't have messages)
	 */
	public String getConnectedUserName(){
		return m_strUserName;
	}
	
	public void Disconnect(){
		m_bConnected = false;
		m_strUserName = "";
		m_hashMessages = new HashMap<String, ArrayList<Message>>();
		m_arrFollowers = null;
		m_arrContacts = new ArrayList<User>();
		m_nMaxMsgNum = 1;
		m_nMaxSentMsgNum = 1;
		if(m_nInterval != 0){
			m_Timer.cancel();
			m_Timer.purge();
			m_nInterval = 0;
		}
	}
	
	public void setNotifier(INotifier notifier){
		m_Notifier = notifier;
	}
	
	public void setInterval(int interval){
		if(m_nInterval != 0){
			m_Timer.cancel();
			m_Timer.purge();
		}
		m_nInterval = interval;
		m_Timer = new Timer();
		m_Timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try{
					List<Message> lstMsg = SyncMessages(false);
					if(!lstMsg.isEmpty() && m_Notifier != null){
						m_Notifier.Notify(lstMsg);
					}
				}
				catch(TwitterException e){
					Log.e("SyncMessage",e.getMessage());
				}
			}
		}, m_nInterval*60000, m_nInterval*60000);
	
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

	public List<User> GetAllFollowers(boolean useCache) throws TwitterException{
		if(!useCache || m_arrFollowers == null){
			//TODO: caching
			m_arrFollowers = m_Twitter.getFollowers();
		}
		return m_arrFollowers;
	}
	
	public List<User> GetAllContacts(){
		return m_arrContacts;
	}
	
	public ArrayList<Message> GetMessagesForContact(String strUserName) throws TwitterException{
		if(!m_hashMessages.containsKey(strUserName)){
			m_hashMessages.put(strUserName, new ArrayList<Message>());
		}
		return m_hashMessages.get(strUserName);
	}
	
	public void SendMessage(String strUserName, String strMsg) throws TwitterException{
		Message msg = m_Twitter.sendMessage(strUserName, strMsg);
		m_nMaxSentMsgNum = msg.getId();
		if(!m_hashMessages.containsKey(strUserName)){
			m_hashMessages.put(strUserName, new ArrayList<Message>());
			try{
				m_arrContacts.add(m_Twitter.getUser(strUserName));
			}
			catch(Exception e){
				Log.e("SendMessage", e.getMessage());
			}
		}
		m_hashMessages.get(strUserName).add(msg);
	}
	
	private List<Message> SyncMessages(boolean bFirstSync) throws TwitterException{
		List<Message> lstMsg = new ArrayList<Message>();
		long nMaxMsgNum = m_nMaxMsgNum;
		long nMaxSentMsgNum = m_nMaxSentMsgNum;
		
		//TODO: caching
		m_Twitter.setCount(200);
		
		//TODO: more than 200 messages
		//m_Twitter.setMaxResults(1000);
		
		m_Twitter.setSinceId(m_nMaxMsgNum);
		List<Message> messages;
		try{
			messages = m_Twitter.getDirectMessages();
			lstMsg.addAll(messages);
	        for (Message message : messages) {
	        	String strSenderScreenName = message.getSender().screenName;
	        	if(!m_hashMessages.containsKey(strSenderScreenName)){
	        		m_hashMessages.put(strSenderScreenName, new ArrayList<Message>());
	        		m_arrContacts.add(message.getSender());
	        	}
	        	if(message.getId() > nMaxMsgNum){
	        		nMaxMsgNum = message.getId();
	        	}
	        	//HACK: that's the only way to get current user
	        	if(m_strUserName == ""){
	        		m_strUserName = message.getRecipient().getScreenName();
	        	}
	        	m_hashMessages.get(strSenderScreenName).add(message);
	        }
	        m_nMaxMsgNum = nMaxMsgNum;
	        m_Twitter.setSinceId(m_nMaxSentMsgNum);
	        messages = m_Twitter.getDirectMessagesSent();
	        
	        for (Message message : messages) {
	        	String strRecipientScreenName = message.getRecipient().getScreenName();
	        	if(!m_hashMessages.containsKey(strRecipientScreenName)){
	        		m_hashMessages.put(strRecipientScreenName, new ArrayList<Message>());
	        		m_arrContacts.add(message.getRecipient());
	        	}
	        	if(message.getId() > nMaxSentMsgNum){
	        		nMaxSentMsgNum = message.getId();
	        	}
	        	//HACK: that's the only way to get current user, in case user only have sent messages
	        	if(m_strUserName == ""){
	        		m_strUserName = message.getSender().getScreenName();
	        	}
	        	m_hashMessages.get(strRecipientScreenName).add(message);
	        }
	        m_nMaxSentMsgNum = nMaxSentMsgNum;
        }
        catch(TwitterException e){
        	Log.e("SyncMessage", e.getMessage());
        	if(bFirstSync){
        		throw e;
        	}
        }
        for(ArrayList<Message> arr : m_hashMessages.values()){
        	Collections.sort(arr, new Comparator<Message>() {
				public int compare(Message object1, Message object2) {
					return object1.getCreatedAt().compareTo(object2.getCreatedAt());
				}
        	});
        }
        
        return lstMsg;
	}
}
