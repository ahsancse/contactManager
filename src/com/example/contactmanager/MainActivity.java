package com.example.contactmanager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Button btn_login;
	private EditText edt_username;
	private EditText edt_password;
	private TextView txt_login_error;
	private String username;
	private String password;

	@Override
     protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		 btn_login= (Button) findViewById(R.id.btn_login);
		 edt_username=(EditText)findViewById(R.id.sw2EditAccountUserName);
		 edt_password=(EditText)findViewById(R.id.sw2EditAccountServerPassword);
		 txt_login_error=(TextView)findViewById(R.id.loginError);
		 txt_login_error.setVisibility(View.INVISIBLE);
	        
        
         // login click event
		 btn_login.setOnClickListener(new View.OnClickListener(){
 
            @Override
            public void onClick(View view) {
            	username=edt_username.getText().toString().trim();
            	password=edt_password.getText().toString().trim();
            	
            	if(username.equalsIgnoreCase("ahsan")){
            		Intent i = new Intent(getApplicationContext(), SearchActivity.class);
               		startActivity(i);
            	}
            	
				
            	
            	
 
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
