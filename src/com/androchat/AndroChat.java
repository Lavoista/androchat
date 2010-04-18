package com.androchat;

import android.app.Activity;
import android.os.Bundle;

public class AndroChat extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//TODO: (Shauli :)
    	// Here we need to check if the user already inserted his
    	// twitter account and password.
    	// 		If yes , than try to login:
    	//				If login success, set content view as R.layout.messageList
    	//				If login fails, set content view as R.layout.login
    	//		If No, than set content view as R.layout.login
    	
        super.onCreate(savedInstanceState);
        
        boolean isUserAlreadySignedIn = false;
        
        if ( isUserAlreadySignedIn ){
        	// Open the messageList.
            //Intent iMessagesList = new Intent(AndroChat.this, MessagesList.class);
            //startActivity(iMessagesList);

    		this.setContentView(R.layout.messageslist);
        }
        else {
        	// Open the login and settings screen.
            //Intent iLogin = new Intent(AndroChat.this, LoginSettings.class);
            //startActivity(iLogin);
            
    		this.setContentView(R.layout.login);
        	
        }
        
        
        
    }

}