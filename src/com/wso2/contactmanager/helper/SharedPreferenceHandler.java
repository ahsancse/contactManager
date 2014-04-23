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
		sharedPreferences = context.getSharedPreferences("preference",0);
		Editor editor = sharedPreferences.edit();
		editor.putString("clientId","xublT5nkyRqsRRh");
		editor.putString("clientSecret","TnLQPaNg6nv1eaP");
		editor.commit();
	}
	
	public void loadData(Context context){
		sharedPreferences = context.getSharedPreferences("preference",0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("dataLoaded", true);
	    editor.commit();
	}
	
	public boolean getLoadedData(){
		boolean preferences=sharedPreferences.getBoolean("dataLoaded",false);
		return preferences;
		
	}
	

}
