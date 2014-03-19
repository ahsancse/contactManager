package com.example.contactmanager.helpers;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.contactmanager.model.Contact;
import com.example.contactmanager.R;
//import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
//import android.widget.TextView;
 
public class UsageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contact_details);
         
        SqliteDatabaseHandler db = new SqliteDatabaseHandler(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1= dateFormat.format(new GregorianCalendar().getTime());
        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact(001,"Ravi", "0715444454","0342270819","ahsan@wso2.com",date1,date1));       
        //db.addContact(new Contact("Srinivas", "9199999999"));
        //db.addContact(new Contact("Tommy", "9522222222"));
        //db.addContact(new Contact("Karthik", "9533333333"));
         
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();      
         
        for (Contact cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getMobileNumber()+",Date:"+cn.getCreatedTime();
            System.out.println(log);
                // Writing Contacts to log
        Log.d("Name: ", log);
        }
    }
    
    
    	
    }

