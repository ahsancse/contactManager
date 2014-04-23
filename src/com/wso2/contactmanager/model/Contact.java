package com.wso2.contactmanager.model;

import java.sql.Blob;

import android.graphics.Bitmap;

public class Contact {
    
    //private variables
    private String employee_no;
    private String employee_name;
    private String mobile_number;
    private String office_number;
    private String email_id;
    //private Bitmap photo;
    private String product_team;;
    private String designation;
   
     
    // Empty constructor
    public Contact(){
         
    }
    // constructor
    public Contact(String employee_no,String name, String mobile_number,String office_number,String emailId,String product_team,
    		String designation){
        this.employee_no=employee_no;
        this.employee_name = name;
        this.mobile_number =mobile_number;
        this.office_number=office_number;
        this.email_id=emailId;
        this.product_team=product_team;
        this.designation=designation;
        //this.photo=photo;
     
    }
     
    // constructor
    
    // getting EMPLOYEE NO
    public String getEMPLOYEENO(){
        return this.employee_no;
    }
     
    // setting id
    public void setEMPLOYEENO(String employee_no){
        this.employee_no = employee_no;
    }
     
    // getting name
    public String getName(){
        return this.employee_name;
    }
     
    // setting name
    public void setName(String employee_name){
        this.employee_name = employee_name;
    }
     
    // getting Mobile number
    public String getMobileNumber(){
        return this.mobile_number;
    }
     
    // setting Mobile number
    public void setMobileNumber(String mobile_number){
        this.mobile_number = mobile_number;
    }
    
    // getting Home number
    public String getOfficeNumber(){
        return this.office_number;
    }
     
    // setting Home number
    public void setHomeNumber(String office_number){
        this.office_number = office_number;
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
    /*public Bitmap getPhoto() {
        return photo;
    }

    //set photo
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }*/
    
    public String getProductTeam() {
        return this.product_team;
    }

    //set photo
    public void setProductTeam(String product_team) {
        this.product_team=product_team;
    }
    
    public String getDesignation() {
        return this.designation;
    }

    //set photo
    public void setDesignation(String designation) {
        this.designation= designation;
    }
}
