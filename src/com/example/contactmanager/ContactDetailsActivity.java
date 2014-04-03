package com.example.contactmanager;


import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

 public class ContactDetailsActivity extends Activity {
	
	
 
    // Progress Dialog
    private ProgressDialog pDialog;
    private List<Contact> contacts;
    // Creating JSON Parser object
    //JSONParser jParser = new JSONParser();
 
    //ArrayList<HashMap<String, String>> productsList;
    private TextView contact_name;
    private TextView mobile_number;
    private TextView home_number;
    private TextView email;
    private ImageButton ctArrowRightHeaderBtn;
    private ImageView contact_image;
    //private TextView created_at;
    //private TextView updated_at;
    
    private SqliteDatabaseHandler db;
    private Contact contact;
    private String contact_id;
    private String mobile_no;
    private String home_no;
    private String email_id;
    private String contact_nam;
    private byte[] image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        
        Intent i = getIntent();
        // getting contact id from intent
        contact_id = i.getStringExtra("contact_id");
        image=i.getByteArrayExtra("contact_photo");
        contact_name=(TextView)findViewById(R.id.ctName);
        mobile_number=(TextView)findViewById(R.id.mobile_number);
        home_number=(TextView)findViewById(R.id.home_number);
        email=(TextView)findViewById(R.id.email);
        contact_image=(ImageView)findViewById(R.id.ctPhoto);
        ctArrowRightHeaderBtn = (ImageButton) findViewById(R.id.ctArrowRightHeaderBtn);
        //created_at=(TextView)findViewById(R.id.createdat);
        //updated_at=(TextView)findViewById(R.id.updatedat);
        // Loading products in Background Thread
        new GetContactDetails().execute();
        
        ctArrowRightHeaderBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                // Returns to previous activity.
                onBackPressed();
               
            }
        });
        
        mobile_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	launchSMSAction(mobile_no);
            }
        });
        
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	launchMailAction(email_id);
            }
        });
        
        home_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	launchCall(home_no);
            }
        });
 
        
 
    }
    
    private void launchSMSAction(final String number) {
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("sms:" + number));
        startActivity(intent);
    }
    
    private void launchMailAction(final String email) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        startActivity(Intent.createChooser(intent, "Send an email to " + email));
    }
    
    private void launchCall(final String phoneNumber) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format(getResources().getString(R.string.warning_call_number), phoneNumber))
                .setIcon(android.R.drawable.ic_dialog_dialer)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {

                        launchCallPhoneAction(phoneNumber);
                    }
                }).setNegativeButton("No", null).show();
    }
    
    private void launchCallPhoneAction(final String number) {
        final Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }
 
    
    /**
     * Background Async Task to Load contact details by making sqlite db Request
     * */
    class GetContactDetails extends AsyncTask<String, String, String> {
    	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContactDetailsActivity.this);
            pDialog.setMessage("Loading contact details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting contact details in background thread
         * */
        protected String doInBackground(String... params) {
        	db=new SqliteDatabaseHandler(ContactDetailsActivity.this);
        	contact=db.getContact(Integer.parseInt(contact_id));
        	contact_nam=contact.getName();
        	mobile_no=contact.getMobileNumber();
        	home_no=contact.getHomeNumber();
        	email_id=contact.getEmail();
 
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
						contact_name.setText(contact_nam);
						mobile_number.setText(mobile_no);
				        home_number.setText(home_no);
				        email.setText(email_id);
				        contact_image.setImageBitmap(Utility.getPhoto(image));
						//created_at.setText(contact.getCreatedTime());
						//updated_at.setText(contact.getUpdatedTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
}
