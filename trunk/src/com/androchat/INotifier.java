package com.androchat;

import java.util.List;

import winterwell.jtwitter.Twitter.Message;

public interface INotifier {
	void Notify(List<Message> lstMsg); 
}
