package com.wso2.contactmanager.helper;

import java.util.ArrayList;
import java.util.List;

import com.wso2.contactmanager.model.Contact;
import com.wso2.contactmanager.util.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.graphics.Bitmap;
 
public  class SqliteDatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 25;
    public static  SQLiteDatabase db;
    //Utility util=new Utility();
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    
    //logcat variable
    private static final String LOGCAT = null;
 
    // Contacts Table Columns names
    private static final String KEY_EMPLOYEE_NO="_id";
    private static final String KEY_NAME = "_name";
    private static final String KEY_MOBILE_NO = "_mobile_number";
    private static final String KEY_OFFICE_NO = "_office_number";
    private static final String KEY_EMAIL_ID = "_email_id";
    private static final String KEY_TEAM = "_product_team";
	private static final String KEY_DESIGNATION = "_designation";
    //private static final String KEY_CREATED_TIME="created_at";
    //private static final String KEY_UPDATED_TIME="updated_at";
    //private static final String KEY_PHOTO= "photo";
 
    public SqliteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOGCAT,"Created");
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d(LOGCAT,"contacts table Created before");
    	String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_EMPLOYEE_NO + " TEXT PRIMARY KEY ," + 
    			KEY_NAME + " TEXT NOT NULL,"+ 
    			KEY_MOBILE_NO + " TEXT ,"+
    			KEY_OFFICE_NO  + " TEXT,"+
    			KEY_EMAIL_ID  + " TEXT NOT NULL,"+
    			KEY_TEAM  + " TEXT,"+
    			KEY_DESIGNATION + " TEXT" + ")";
    	
    	db.execSQL(CREATE_CONTACTS_TABLE); 
        Log.d(LOGCAT,"contacts table Created");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        Log.d(LOGCAT,"onUpgrade method called");
        // Create tables again
        onCreate(db);
        
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_EMPLOYEE_NO, contact.getEMPLOYEENO());    
        values.put(KEY_NAME, contact.getName());                           // Contact Name
        values.put(KEY_MOBILE_NO, contact.getMobileNumber());              // Contact Mobile
        values.put(KEY_OFFICE_NO, contact.getOfficeNumber());             // Contact Mobile
        values.put(KEY_EMAIL_ID, contact.getEmail());  
        values.put(KEY_TEAM, contact.getProductTeam());
        values.put(KEY_DESIGNATION, contact.getDesignation()); 
        //values.put(KEY_PHOTO, Utility.getBytes(contact.getPhoto()));
        // Inserting Row
        db.insert(TABLE_CONTACTS, null,values);
       // db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    } 
 
    // Getting single contact
    public Contact getContact(String employee_no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact contact=null;
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_EMPLOYEE_NO,
                KEY_NAME, KEY_MOBILE_NO,KEY_OFFICE_NO,KEY_EMAIL_ID,KEY_TEAM,KEY_DESIGNATION}, KEY_EMPLOYEE_NO + "=?",
                new String[] { employee_no }, null, null, null, null);
        if (!(cursor.getCount()>0)){
        	return contact;
        }
        else{
        	cursor.moveToFirst();
        	contact = new Contact(
                    cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
            // return contact
            return contact;
        }
            
        	//byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_PHOTO));
        	
    }
     
    // Getting All Contacts
    public List<Contact> getAllContacts() {
    	System.out.println("ordering contacts");
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  _id,_name FROM " +TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	//byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_PHOTO));
                Contact contact = new Contact();
                contact.setEMPLOYEENO(cursor.getString(0));
                contact.setName(cursor.getString(1));
                //contact.setPhoto(Utility.getPhoto(blob));
                /*contact.setMobileNumber(cursor.getString(2));
                contact.setHomeNumber(cursor.getString(3));
                contact.setEmail(cursor.getString(4));
                contact.setCreatedTime(cursor.getString(5));
                contact.setUpdatedTime(cursor.getString(6)); */
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        //System.out.println(contactList);
        return contactList;
    }
 
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, contact.getID());    
        values.put(KEY_EMPLOYEE_NO, contact.getEMPLOYEENO());    
        values.put(KEY_NAME, contact.getName());                           // Contact Name
        values.put(KEY_MOBILE_NO, contact.getMobileNumber());              // Contact Mobile
        values.put(KEY_OFFICE_NO, contact.getOfficeNumber());             // Contact Mobile
        values.put(KEY_EMAIL_ID, contact.getEmail());  
        values.put(KEY_TEAM, contact.getProductTeam());
        values.put(KEY_DESIGNATION, contact.getDesignation()); 
        //values.put(KEY_PHOTO, Utility.getBytes(contact.getPhoto()));
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_EMPLOYEE_NO + " = ?",
                new String[] { String.valueOf(contact.getEMPLOYEENO()) });
       
    }
 
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_EMPLOYEE_NO + " = ?",
                new String[] { String.valueOf(contact.getEMPLOYEENO()) });
        db.close();
    }
 
 
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
    /*public String getLastUpdatedTime(){
    	String countQuery = "SELECT  MAX(updated_at) FROM " + TABLE_CONTACTS;
    	SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        if(cursor!=null){
        	cursor.moveToFirst();
        	String time=cursor.getString(0);
        	cursor.close();
        	return time;
        }
        return "error";
       
        
    }*/
 
}
