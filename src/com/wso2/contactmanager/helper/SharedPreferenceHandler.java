package com.wso2.contactmanager.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreferenceHandler{
	
	private SharedPreferences sharedPreferences ;
	
	public String getSharedClientID(){
		
		String client_id=sharedPreferences.getString("clientId", null); // getting String
		return client_id;
	} 
	
	public String getSharedClientSecret(){
		
		String client_secret=sharedPreferences.getString("clientSecret", null); // getting String
		return client_secret;
	}
	
	public void saveSharedPreference(Context context){
		sharedPreferences = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("clientId","xublT5nkyRqsRRh");
		editor.putString("clientSecret","TnLQPaNg6nv1eaP");
		editor.commit();
	}
	
	public void saveUserCredentials(Context context, String username, String password){
		sharedPreferences = context.getSharedPreferences("preference",Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("username", username);
		editor.putString("password",password);
	    editor.commit();
	}
	
	public String getUserName(){
		String username=sharedPreferences.getString("username", null); // getting String
		return username;
	}
	
	public String getPassword(){
		String password=sharedPreferences.getString("password", null); // getting String
		return password;
	}
	

}
