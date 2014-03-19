package com.example.contactmanager.model;

import android.graphics.Bitmap;

public class Contact {
    
    //private variables
    private int contact_id;
    private String contact_name;
    private String contact_mobile_number;
    private String contact_home_number;
    private String email_id;
    private Bitmap photo;
    private String created_at;
    private String updated_at;
    
     
    // Empty constructor
    public Contact(){
         
    }
    // constructor
    public Contact(int id, String name, String mobile_number,String home_number,String emailId,String created_at,
    		String updated_at){
        this.contact_id = id;
        this.contact_name = name;
        this.contact_mobile_number =mobile_number;
        this.contact_home_number=home_number;
        this.email_id=emailId;
        this.created_at=created_at;
        this.updated_at=updated_at;
        //this.photo=photo;
    }
     
    // constructor
    public Contact(String name, String mobile_number){
        this.contact_name = name;
        this.contact_mobile_number = mobile_number;
    }
    // getting ID
    public int getID(){
        return this.contact_id;
    }
     
    // setting id
    public void setID(int id){
        this.contact_id = id;
    }
     
    // getting name
    public String getName(){
        return this.contact_name;
    }
     
    // setting name
    public void setName(String name){
        this.contact_name = name;
    }
     
    // getting Mobile number
    public String getMobileNumber(){
        return this.contact_mobile_number;
    }
     
    // setting Mobile number
    public void setMobileNumber(String mobile_number){
        this.contact_mobile_number = mobile_number;
    }
    
    // getting Home number
    public String getHomeNumber(){
        return this.contact_home_number;
    }
     
    // setting Home number
    public void setHomeNumber(String home_number){
        this.contact_home_number = home_number;
    }
    
    // getting email id
    public String getEmail(){
        return this.email_id;
    }
     
    // setting Mobile number
    public void setEmail(String emailId){
        this.email_id = emailId;
    }
    
    //get photo
    public Bitmap getPhoto() {
        return photo;
    }

    //set photo
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
    
    public String getCreatedTime() {
        return created_at;
    }

    //set photo
    public void setCreatedTime(String created_at) {
        this.created_at= created_at;
    }
    
    public String getUpdatedTime() {
        return updated_at;
    }

    //set photo
    public void setUpdatedTime(String updated_at) {
        this.updated_at= updated_at;
    }
}
