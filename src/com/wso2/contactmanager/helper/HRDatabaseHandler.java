package com.wso2.contactmanager.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.content.Context;
import android.util.Log;

import com.wso2.contactmanager.SearchActivity;
import com.wso2.contactmanager.parser.JSONParser;
import com.wso2.contactmanager.util.MyRedirectHandler;
 
public class HRDatabaseHandler {
 
    private String authorization_code;
    private String access_token;
    private static String url1="https://hr.wso2.com/hcmapi/v1/Authorization" ; 
    private static String url2="https://hr.wso2.com/hcmapi/v1/AccessTokens";
    private static String url3="https://hr.wso2.com/hcmapi/v1/Employees";
    private String json1;
    private String json2;
    private JSONArray jarray;
    private String client_id;
    private String client_secret;
    // constructor
    public HRDatabaseHandler() {
 
    }
 
    // function get json from url by making HTTP GET request
    public String makeHttpRequest(String url, String method,List<NameValuePair> params) {
 
        try {                                 // check for request method
            if(method.equalsIgnoreCase("GET")){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                
                MyRedirectHandler handler = new MyRedirectHandler();
                httpClient.setRedirectHandler(handler);

                HttpGet get = new HttpGet(url);

                HttpResponse response = httpClient.execute(get);

                HttpEntity entity = response.getEntity();
                String lastUrl=null ;
                if(handler.lastRedirectedUri != null){
                    lastUrl = handler.lastRedirectedUri.toString();
                }
                System.out.println(lastUrl);
                getAuthorizationCode(lastUrl);
                
           }         
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        
        return "ok";
        
    }
    
    public void HRAPIHandler(Context context){
    	try {
    		SharedPreferenceHandler sharedPreferences=new SharedPreferenceHandler();
    		sharedPreferences.saveSharedPreference(context);
    		client_id=sharedPreferences.getSharedClientID();
    		client_secret=sharedPreferences.getSharedClientID();
    		List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            // add parameters to the request 1
            params1.add(new BasicNameValuePair("clientId",client_id));
            params1.add(new BasicNameValuePair("responseType","code"));
            params1.add(new BasicNameValuePair("state","STATE"));
            params1.add(new BasicNameValuePair("scope","SCOPE"));
            params1.add(new BasicNameValuePair("redirectUri","https://wso2.com"));
            // getting response string from URL 1
            json1 = makeHttpRequest(url1 ,"GET", params1);
            
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            // add parameters to the request 2
            params2.add(new BasicNameValuePair("grantType","authorization_code"));
            params2.add(new BasicNameValuePair("code",authorization_code));
            params2.add(new BasicNameValuePair("redirectUri","http://www.moraspirit.com"));
            params2.add(new BasicNameValuePair("clientId",client_id));
            params2.add(new BasicNameValuePair("clientSecret",client_secret));
            
            // getting response string from URL 2
            if(json1.equalsIgnoreCase("ok")){
            	json2 = makeHttpRequest(url2 ,"GET", params2);
            }
            else{
            	System.out.println("Something wrong with URL 1");
            }
            
            
            List<NameValuePair> params3 = new ArrayList<NameValuePair>();
         // add parameters to the request 3
            params3.add(new BasicNameValuePair("accessToken",access_token));
            // create a json parser object
            JSONParser jparser=new JSONParser();
            if(json2.equalsIgnoreCase("ok")){
            	jarray=jparser.makeHttpRequest(url3,"GET", params3);
            }
            else{
            	System.out.println("Something wrong with URL 2");
            }
            
            if(jarray!=null){
            	System.out.println("JSON ARRAY IS NOT NULL");
            	SqliteUpdateHandler handler=new SqliteUpdateHandler();
                handler.updateDatabase(jarray);
            }
            
            
		} catch (Exception e) {
			Log.e("error occuured", e.toString());
			// TODO: handle exception
		}
    	
    	
        
        
    }
    // method to get authorization code and acess token from url
    private void getAuthorizationCode(String lastUrl){
    	if(lastUrl.contains("code")){
        	String[] parts = lastUrl.split("=");
        	System.out.println(parts[1]);
        	String[] parts1 =parts[1].split("&");
        	System.out.println(parts1[0]);
        	authorization_code=parts1[0];
        	
        }
    	
    	if(lastUrl.contains("expires_in") || lastUrl.contains("access_token") ){
        	String[] parts = lastUrl.split("=");
        	System.out.println(parts[2]);
        	access_token=parts[2];
        }
    	
    }
}

