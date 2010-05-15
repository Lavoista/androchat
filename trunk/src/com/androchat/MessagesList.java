package com.androchat;

import android.app.Activity;
import android.os.Bundle;

public class MessagesList extends Activity {
	
	@Override
	public void onCreate (Bundle savedInstanceState ){
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.newmessage);
	}

}
