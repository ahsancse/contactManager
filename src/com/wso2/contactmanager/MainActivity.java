package com.wso2.contactmanager;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import com.wso2.contactmanager.R;
import com.wso2.contactmanager.util.LoginHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.NameValuePair;

public class MainActivity extends Activity  {
	
	private Button btn_login;
	private EditText edt_username;
	private EditText edt_password;
	private TextView txt_login_error;
	private String username;
	private String password;
	private boolean authenticated;
	private ProgressDialog pdialog;
	
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
            	//System.out.println(username);
            	//System.out.println(password);
            	
            	//new LoginValidator().execute();
            	Intent i = new Intent(getApplicationContext(), SearchActivity.class);
           		startActivity(i);
           
            	
            	/*if(authenticated){
            		Intent i = new Intent(getApplicationContext(), SearchActivity.class);
               		startActivity(i);
            	}
            	else{
            		txt_login_error.setVisibility(View.VISIBLE);
            		edt_username.setText(null);
            		edt_password.setText(null);
            	}*/
            	
           }
         });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class LoginValidator extends AsyncTask<String, String, String> {
    	/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Login Validation...");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(false);
            pdialog.show();
        }
 
        /**
         * Validate login credentials
         * */
        protected String doInBackground(String... args) {
        	try {
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("mail",username));
        		params.add(new BasicNameValuePair("password",password));
        		LoginHandler login=new LoginHandler();
                // getting login  details by making HTTP request
                authenticated=login.makeHttpRequest(
                        "http://wso2.com/services/rest/ws/login.xml", "POST", params);
                System.out.println(authenticated);
                if(authenticated){
                	System.out.println("authenticated");
                	
                	
                }
                else{
                	System.out.println("problem");
                }
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	return null;
        }
        
        protected void onPostExecute() {
            // dismiss the dialog once got all details
            pdialog.dismiss();
            if(authenticated){
            	Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            	startActivity(i);
            }
            else{
            	txt_login_error.setVisibility(View.VISIBLE);
            	edt_username.setText(null);
            	edt_password.setText(null);
            }
        }
    }
	
	
	
	
	
	

}
