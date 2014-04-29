package com.wso2.contactmanager;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import com.wso2.contactmanager.R;
import com.wso2.contactmanager.SearchActivity.AddAllContacts;
import com.wso2.contactmanager.SearchActivity.LoadAllContacts;
import com.wso2.contactmanager.helper.SharedPreferenceHandler;
import com.wso2.contactmanager.util.Constants;
import com.wso2.contactmanager.util.EncryptionHandler;
import com.wso2.contactmanager.util.LoginHandler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.NameValuePair;

public class MainActivity extends Activity  {
	
	private Button btn_login;
	private EditText edt_username;
	private EditText edt_password;
	private TextView txt_login_error;
	private CheckBox save_credentials;
	private String username;
	private String password;
	private boolean authenticated=false;
	private static String login_url="http://wso2.com/services/rest/ws/login.xml";
	
	@Override
     protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		 btn_login= (Button) findViewById(R.id.btn_login);
		 edt_username=(EditText)findViewById(R.id.sw2EditAccountUserName);
		 edt_password=(EditText)findViewById(R.id.sw2EditAccountServerPassword);
		 txt_login_error=(TextView)findViewById(R.id.loginError);
		 save_credentials=(CheckBox)findViewById(R.id.saveCredentials);
		 txt_login_error.setVisibility(View.INVISIBLE);
	     Constants.sharedPreferenceshandler=new SharedPreferenceHandler();   
        
         // login click event
		btn_login.setOnClickListener(new View.OnClickListener(){
 
            @Override
            public void onClick(View view) {
            	
            	username=edt_username.getText().toString().trim();
            	password=edt_password.getText().toString().trim();
            	LoginValidator loginvalidator=new LoginValidator();
            	//System.out.println(username);
            	//System.out.println(password);
            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                if(prefs.getBoolean("firstRun", true)) {            
                	loginvalidator.execute();
                    prefs.edit().putBoolean("firstRun", false).commit();
                }
                else{
                	try {
                		String user=Constants.sharedPreferenceshandler.getUserName();
                    	String pass=Constants.sharedPreferenceshandler.getPassword();
                    	if(username.equals(user) && password.equals(pass)){
                    		Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                       		startActivity(i);
                    	}
                    	else{
                    		loginvalidator.execute();
                    	}
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
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
	
	class LoginValidator extends AsyncTask<String, String, String> {
    	/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
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
                authenticated=login.makeHttpRequest(login_url, "POST", params);
                System.out.println(authenticated);
                if(authenticated){
                	Constants.sharedPreferenceshandler.saveUserCredentials(MainActivity.this, username, password);
                	Intent i = new Intent(getApplicationContext(), SearchActivity.class);
               		startActivity(i);
                	//startSearchActivity();
            	}
            	else{
            		txt_login_error.setVisibility(View.VISIBLE);
            		edt_username.setText(null);
            		edt_password.setText(null);
            	}
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	return null;
        }
        
       
    }
	
	
	//check internet connectivity
	private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {
		   // There are no active networks.
		   return false;
		  } else
		   return true;
	}
	
	private void startSearchActivity(){
		Intent i = new Intent(getApplicationContext(), SearchActivity.class);
   		startActivity(i);
	}
}
