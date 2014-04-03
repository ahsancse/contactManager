package com.example.contactmanager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

 
public class UpdateDatabase extends Activity {
 
    // Progress Dialog
    //private ProgressDialog pDialog;
    private SqliteDatabaseHandler db;
 // url to get updated contact list
    private static String url_update_contacts = "http://10.0.2.2:80/test/check.php";
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
   
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_CONTACT_ID = "contact_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MOBILE = "mobile_no";
    private static final String TAG_HOME = "home_no";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_CREATED = "created_at";
    private static final String TAG_UPDATED = "updated_at";
    // products JSONArray
    private JSONArray contacts = null;
    private Bitmap bit;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.test1);
       // Hashmap for ListView
       
       bit=BitmapFactory.decodeResource(getResources(), R.drawable.forgerock_logo);
          // Loading products in Background Thread
        new UpdateContacts().execute();
 
        // Get listview
        
 
        // on seleting single product
        // launching Edit Product Screen
        
 
    }
 
    
    /**
     * Background Async Task to update contacts by making HTTP Request
     * */
    class UpdateContacts extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(UpdateDatabase .this);
            pDialog.setMessage("Loading contacts. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	db=new SqliteDatabaseHandler(UpdateDatabase.this);
        	//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // Building Parameters
            String last_updated_time=db.getLastUpdatedTime();
            System.out.println(last_updated_time);
            //Date date= dateFormat.parse(last_updated_time);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("created_at_sqlite",last_updated_time));
            
           // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_update_contacts , "GET", params);
 
            // Check your log cat for JSON reponse
           // Log.d("All contacts: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // contacts found
                    // Getting Array of contacts
                    contacts = json.getJSONArray(TAG_CONTACTS);
                    System.out.println(contacts);
                    // looping through All Products
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
 
                        // Storing each json item in  a variable
                        String id = c.getString(TAG_CONTACT_ID);
                        String name = c.getString(TAG_NAME);
                        String mobile=c.getString(TAG_MOBILE);
                        String home=c.getString(TAG_HOME);
                        String email=c.getString(TAG_EMAIL);
                        String created=c.getString(TAG_CREATED);
                        String updated=c.getString(TAG_UPDATED);
                        
                        // add updated contact details to local db
                        
                        Log.d("Insert: ", "Inserting updated contacts..");
                        db.addContact(new Contact(name,mobile,home,email,created,updated,bit));
                        
                        
                    }
                    System.out.println(db.getAllContacts());
                } else {
                    // no updated contacts found
                    Log.d("database is upto date","now");
                	
                    
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            //pDialog.dismiss();
        }
 
    }
}
