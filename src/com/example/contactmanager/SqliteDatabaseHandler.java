package com.example.contactmanager;

import java.util.ArrayList;
import java.util.List;
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
    private static final int DATABASE_VERSION = 21;
    public static  SQLiteDatabase db;
    //Utility util=new Utility();
    // Database Name
    private static final String DATABASE_NAME = "contactsManager";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    
    //logcat variable
    private static final String LOGCAT = null;
 
    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "_name";
    private static final String KEY_MOBILE_NO = "_mobile_number";
    private static final String KEY_HOME_NO = "_home_number";
    private static final String KEY_EMAIL_ID = "_email_id";
    private static final String KEY_CREATED_TIME="created_at";
    private static final String KEY_UPDATED_TIME="updated_at";
    private static final String KEY_PHOTO= "photo";
 
    public SqliteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOGCAT,"Created");
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d(LOGCAT,"contacts table Created before");
    	String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
    			KEY_NAME + " TEXT NOT NULL,"+ 
    			KEY_MOBILE_NO + " TEXT NOT NULL,"+
    			KEY_HOME_NO  + " TEXT,"+
    			KEY_EMAIL_ID  + " TEXT NOT NULL,"+
    			KEY_CREATED_TIME  + " TEXT,"+
    			KEY_UPDATED_TIME  + " TEXT,"+
    			KEY_PHOTO + " BLOB" + ")";
    	
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
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
      //  values.put(KEY_ID, contact.getID());    
        values.put(KEY_NAME, contact.getName());                           // Contact Name
        values.put(KEY_MOBILE_NO, contact.getMobileNumber());              // Contact Mobile
        values.put(KEY_HOME_NO, contact.getHomeNumber());             // Contact Mobile
        values.put(KEY_EMAIL_ID, contact.getEmail());  
        values.put(KEY_CREATED_TIME, contact.getCreatedTime());
        values.put(KEY_UPDATED_TIME, contact.getUpdatedTime()); 
        values.put(KEY_PHOTO, Utility.getBytes(contact.getPhoto()));
        // Inserting Row
        db.insert(TABLE_CONTACTS, null,values);
       // db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    } 
 
    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                KEY_NAME, KEY_MOBILE_NO,KEY_HOME_NO,KEY_EMAIL_ID,KEY_CREATED_TIME,KEY_UPDATED_TIME,KEY_PHOTO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        	byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_PHOTO));
        	Contact contact = new Contact(
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),Utility.getPhoto(blob));
        // return contact
        return contact;
    }
     
    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  _id,_name,photo FROM " + TABLE_CONTACTS;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_PHOTO));
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoto(Utility.getPhoto(blob));
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
        return contactList;
    }
 
    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, contact.getID());    
        values.put(KEY_NAME, contact.getName());                           // Contact Name
        values.put(KEY_MOBILE_NO, contact.getMobileNumber());              // Contact Mobile
        values.put(KEY_HOME_NO, contact.getHomeNumber() );             // Contact Mobile
        values.put(KEY_EMAIL_ID, contact.getEmail() );  
        values.put(KEY_CREATED_TIME, contact.getCreatedTime());
        values.put(KEY_UPDATED_TIME, contact.getUpdatedTime());
        values.put(KEY_PHOTO, Utility.getBytes(contact.getPhoto()));
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
 
    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
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
    
    public String getLastUpdatedTime(){
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
       
        
    }
 
}
