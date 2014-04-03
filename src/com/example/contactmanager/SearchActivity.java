package com.example.contactmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class SearchActivity extends ListActivity {
	
	private ProgressDialog pDialog1;
	private ProgressDialog pDialog2;
	private ProgressDialog pDialog3;
	private List<Contact> contacts;
	private ArrayList<HashMap<String, String>> contactList;
	private SqliteDatabaseHandler db;
	private EditText searchbox;
	private ImageView contact_photo;
	private Bitmap bit;
	
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
		private JSONArray contact_array = null;
		//private Bitmap bit; 
		private Handler handler;
		private Timer timer;
		private TimerTask task;
		
	    // Creating JSON Parser object
		JSONParser jParser = new JSONParser();
		// url to get updated contact list
	    private static String url_update_contacts = "http://10.0.2.2:80/test/check.php";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_search);
        contactList= new ArrayList<HashMap<String,String>>();
        searchbox=(EditText)findViewById(R.id.list_search_text);
        contact_photo = (ImageView) findViewById(R.drawable.images);
        bit=BitmapFactory.decodeResource(getResources(), R.drawable.images);
       // contact_photo.setImageBitmap(bm);
         // Adding in Background Thread
        //new AddAllContacts().execute();
        // update contacts
       new UpdateContacts().execute();
        //getting contacts in Background Thread
        //new LoadAllContacts().execute();
        
        ListView lview = getListView();
        
        // on seleting single product
        // launching Edit Product Screen
        lview.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // getting values from selected ListItem
                String contact_id = ((TextView) view.findViewById(R.id.contact_id)).getText().toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),ContactDetailsActivity.class);
                // sending contact id to next activity
                in.putExtra("contact_id",contact_id);
                in.putExtra("contact_photo",Utility.getBytes(bit));
                //in.putExtra(name, value);
                startActivity(in);
 
                // starting new activity and expecting some response back
               // startActivityForResult(in, 100);
            }
        });
       
    }
 
    /**
     * Background Async Task to Load all contacts by making sqlite db Request
     * */
    class AddAllContacts extends AsyncTask<String, String, String> {
    	/**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(SearchActivity.this);
            pDialog1.setMessage("Adding contacts. Please wait...");
            pDialog1.setIndeterminate(false);
            pDialog1.setCancelable(false);
            pDialog1.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	try {
        		db=new SqliteDatabaseHandler(SearchActivity.this);
        		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date1= dateFormat.format(new GregorianCalendar().getTime());
                Log.d("Insert: ", "Inserting ..");
                db.addContact(new Contact("ahsan", "0715444454","0342270819","ahsan@wso2.com",date1,date1,bit));
                db.addContact(new Contact("Ravi", "0775444454","0442270819","ravi@wso2.com",date1,date1,bit));
                db.addContact(new Contact("sudheera", "0715444454","0342270819","sudhee@wso2.com",date1,date1,bit));
                db.addContact(new Contact("dammina", "0775444454","0442270819","dammina@wso2.com",date1,date1,bit));
                db.addContact(new Contact("sasikala", "0715444454","0342270819","sasaikala@wso2.com",date1,date1,bit));
                db.addContact(new Contact("zamrath", "0775444454","0442270819","zami@wso2.com",date1,date1,bit));
                db.addContact(new Contact("upeksha", "0715444454","0342270819","upeksha@wso2.com",date1,date1,bit));
                db.addContact(new Contact("chamila", "0775444454","0442270819","chamila@wso2.com",date1,date1,bit));
              } catch (Exception e) {
				e.printStackTrace();
			}
        	return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog1.dismiss();
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
        		db=new SqliteDatabaseHandler(SearchActivity.this);
                // Reading all contacts
                Log.d("Reading: ", "Reading all contacts..");
                
                	contacts = db.getAllContacts(); 
                    //Collections.sort(list, comparator);
                    // adding each child node to HashMap key => value
                	for(int i=0;i<contacts.size();i++){
                		 HashMap<String, String> map = new HashMap<String, String>();
                	     map.put("id",""+contacts.get(i).getID());
                         map.put("name",contacts.get(i).getName());
                         
                         //map.put("image",R.id.img);
                        // adding HashList to ArrayList
                         contactList.add(map);
                    }
                	
                	System.out.println(db.getLastUpdatedTime());
                	//System.out.println(contactList);
                	
                    
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
          //  ListView lv = (ListView) findViewById(R.id.lvSearchResult);

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
    
    class UpdateContacts extends AsyncTask<String, String, String>  {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog3 = new ProgressDialog(SearchActivity .this);
            pDialog3.setMessage("Updating contacts. Please wait...");
            pDialog3.setIndeterminate(false);
            pDialog3.setCancelable(false);
            pDialog3.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	db=new SqliteDatabaseHandler(SearchActivity.this);
        	Date date;
        	//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // Building Parameters
            String last_updated_time=db.getLastUpdatedTime();
            String[] x=last_updated_time.split("\\s");
            System.out.println(last_updated_time);
            
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("created_at_sqlite",last_updated_time));
            params.add(new BasicNameValuePair("created_at_date",x[0]));
            params.add(new BasicNameValuePair("created_at_time",x[1]));
            //params.add
           // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_update_contacts ,"GET", params);
            
            // Check your log cat for JSON reponse
           // Log.d("All contacts: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                //int success = json.getInt(TAG_SUCCESS);
                 
                if (true) {
                    // contacts found
                    // Getting Array of contacts
                    contact_array = json.getJSONArray(TAG_CONTACTS);
                    //System.out.println(contacts);
                    // looping through All Products
                    for (int i = 0; i < contact_array.length(); i++) {
                        JSONObject c = contact_array.getJSONObject(i);
 
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
                        //db.addContact(new Contact(name,mobile,home,email,created,updated,bit));
                        
                        
                    }
                    System.out.println(db.getAllContacts());
                } /*else {
                    // no updated contacts found
                    Log.d("database is upto date","now");
                	
                    
                }*/
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
            /*pDialog3.dismiss();*/
        }
 
    }
}