package com.androchat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewMessage extends Activity{
	

	private Button		_btnAddContact;
	private Button		_btnSendMessage;
	private EditText 	_txtAddressName;
	private EditText 	_txtMessageBody;
	
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.newmessage);
		
		_btnAddContact = (Button) findViewById(R.id.btnAddContact);
		_btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
		_txtAddressName = (EditText) findViewById(R.id.txtAddresseeName);
		set_txtMessageBody((EditText) findViewById(R.id.txtMessageBody));
		
		
		_btnAddContact.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO : Open the the Contact List
		         Intent i = new Intent(NewMessage.this,AndroChat.class);
		         startActivity(i);
				
			}
		});
		
		_btnSendMessage.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO : Start send message process
				
			}
		});
		
		_txtAddressName.addTextChangedListener( new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO : When user enters his contact name
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO : When user enters his contact name
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO : When user enters his contact name
				
			}
			
		});
		
		
	}

	public void set_txtMessageBody(EditText _txtMessageBody) {
		this._txtMessageBody = _txtMessageBody;
	}

	public EditText get_txtMessageBody() {
		return _txtMessageBody;
	}
	
}
