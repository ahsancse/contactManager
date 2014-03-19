/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 *       Copyright 2013 ForgeRock AS.
 */

package com.example.contactmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.contactmanager.helpers.SqliteDatabaseHandler;
//import com.example.contactmanager.model.Contact;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class SearchActivity extends Activity{
	
	
 
    // Progress Dialog
    private ProgressDialog pDialog;
   // private List<Contact> contacts;
    private Button btn_next;
    // Creating JSON Parser object
    //JSONParser jParser = new JSONParser();
 
    //ArrayList<HashMap<String, String>> productsList;
    //private TextView text1;
    //private TextView text2;
    //private Context context;
    private SQLiteDatabase database;
    
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test1);
        btn_next=(Button) findViewById(R.id.btn_next);
        //SqliteDatabaseHandler db=new SqliteDatabaseHandler(this);
        //db.onCreate(database);
        
        //text1=(TextView)findViewById(R.id.textView1);
        //text2=(TextView)findViewById(R.id.textView2);
 
        // Loading products in Background Thread
        new AddAllContacts().execute();
    
       btn_next.setOnClickListener(new View.OnClickListener(){
    	 
        @Override
        public void onClick(View view) {
        	
        	//System.out.println(username);
        	//System.out.println(password);
        	
        		Intent i = new Intent(getApplicationContext(), TestActivity.class);
       		    startActivity(i);
			
        	
        	

        }
    });
       
    }
 
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background Async Task to Load all contacts by making sqlite db Request
     * */
    class AddAllContacts extends AsyncTask<String, String, String> {
    	//private ProgressDialog pDialog;
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading contacts. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	try {
        		SqliteDatabaseHandler db=new SqliteDatabaseHandler(SearchActivity.this);
        		//db.onUpgrade(database,1,2);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date1= dateFormat.format(new GregorianCalendar().getTime());
                Log.d("Insert: ", "Inserting ..");
                db.addContact(new Contact(5,"ahsan", "0715444454","0342270819","ahsan@wso2.com",date1,date1));
                db.addContact(new Contact(6,"Ravi", "0775444454","0442270819","ravi@wso2.com",date1,date1));
                //db.addContact(new Contact("Srinivas", "9199999999"));
                //db.addContact(new Contact("Tommy", "9522222222"));
                //db.addContact(new Contact("Karthik", "9533333333"));
                
                 
                // Reading all contacts
                /*Log.d("Reading: ", "Reading all contacts..");
                if(contacts.size()>0){
                	contacts = db.getAllContacts(); 
                }
                else{
                	System.out.println("no contacts");
                	
                }*/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        	 
             
             
        	return null;
           
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            
            
            // updating UI from Background Thread
            
 
        }
 
    }
}