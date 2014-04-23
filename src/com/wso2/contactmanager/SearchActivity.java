package com.wso2.contactmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.wso2.contactmanager.R;
import com.wso2.contactmanager.helper.HRDatabaseHandler;
import com.wso2.contactmanager.helper.SqliteDatabaseHandler;
import com.wso2.contactmanager.model.Contact;
import com.wso2.contactmanager.util.Constants;
import com.wso2.contactmanager.util.EncryptionHandler;
import com.wso2.contactmanager.util.MapComparator;
import com.wso2.contactmanager.util.Utility;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
 
public class SearchActivity extends ListActivity {
	
	private ProgressDialog pDialog1;
	private ProgressDialog pDialog2;
	
	private List<Contact> contacts;
	private ArrayList<HashMap<String, String>> contactList;
	//public static SqliteDatabaseHandler db;
	private EditText searchbox;
	private Bitmap bit;
	
	//public static EncryptionHandler enc;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_search);
        contactList= new ArrayList<HashMap<String,String>>();
        searchbox=(EditText)findViewById(R.id.list_search_text);
        bit=BitmapFactory.decodeResource(getResources(), R.drawable.avatar_male_gray_frame_200x200);
        
        //generate a secret key for encryption
        Constants.enc=new EncryptionHandler();
        Constants.enc.createSecretKey();
        //create a shared prefe
        
        // Adding contacts in  Background Thread
        new AddAllContacts().execute();
        
        //getting contacts in Background Thread
        new LoadAllContacts().execute();
        callAsynchronousUpdateTask();
        // listview for load contacts
        ListView lview = getListView();    
        
        //this.finish() ;
        // on seleting single contact
        lview.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // getting values from selected ListItem
                String contact_id = ((TextView) view.findViewById(R.id.contact_id)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),ContactDetailsActivity.class);
                // sending contact id, photo to next activity
                in.putExtra("contact_id",contact_id);
                in.putExtra("contact_photo",Utility.getBytes(bit));
                startActivity(in);
                //reset the searchbox
                searchbox.setText(null);                   
            }
        });
       
    }
	
	
 
    /**
     * Background Async Task to Add contacts by making http request to remote HR API
     * */
    class AddAllContacts extends AsyncTask<String, String, String> {
    	/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        /**
         * getting all contacts from HR API
         * */
        protected String doInBackground(String... args) {
        	try {
        		Constants.db=new SqliteDatabaseHandler(SearchActivity.this);
        		HRDatabaseHandler hr=new HRDatabaseHandler();
        		hr.HRAPIHandler(SearchActivity.this);
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	return null;
        }
    }
    
    
    // backgroud Async Task for load contacts from Sqlite database
    
    class LoadAllContacts extends AsyncTask<String, String, String> {
     /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(SearchActivity.this);
            pDialog2.setMessage("Loading contacts. Please wait...");
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(false);
            pDialog2.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	try {
        		Constants.db=new SqliteDatabaseHandler(SearchActivity.this);
                // Reading all contacts
                Log.d("Reading: ", "Reading all contacts..");
                
                	contacts = Constants.db.getAllContacts(); 
                    // adding each child node to HashMap key => value
                	for(int i=0;i<contacts.size();i++){
                		 HashMap<String, String> map = new HashMap<String, String>();
                	     map.put("id",""+Constants.enc.decrypt(contacts.get(i).getEMPLOYEENO()));
                         map.put("name",Constants.enc.decrypt(contacts.get(i).getName()).trim());
                         // adding HashList to ArrayList
                         contactList.add(map);
                    }
                	
                	//System.out.println(contactList);
                	//sort the contacts in alphabetical order using Map comparator class
                	Collections.sort(contactList, new MapComparator("name"));
                	
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
            pDialog2.dismiss();
            // set the contacts to contact search layout
            runOnUiThread(new Runnable() {
                public void run() {
                	final ListAdapter adapter = new SimpleAdapter(SearchActivity.this, contactList,
                            R.layout.list_items, new String[] {"id","name"},
                            new int[] { R.id.contact_id, R.id.contact_name});
                	
                	setListAdapter(adapter);
                	searchbox.addTextChangedListener(new TextWatcher() {

                	    @Override
                	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                	        // When user changed the Text
                	        ((SimpleAdapter) adapter).getFilter().filter(cs);
                	    }

                	    @Override
                	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

                	    @Override
                	    public void afterTextChanged(Editable arg0) {}
                	});
               }
            });
       }
 
    }
    


public void callAsynchronousUpdateTask() {
	System.out.print("running thread");
    final Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask doAsynchronousTask = new TimerTask() {       
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {       
                    try {                        
                    	new AddAllContacts().execute();
                    	new LoadAllContacts().execute();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }
                }
            });
        }
    };
    timer.schedule(doAsynchronousTask,60000*60*24); //execute in ev
}


    
    
}