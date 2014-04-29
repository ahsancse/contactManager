package com.wso2.contactmanager.helper;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import com.wso2.contactmanager.model.Contact;
import com.wso2.contactmanager.util.Constants;

public class SqliteUpdateHandler {
	private static final String TAG_EMPLOYEE_NAME = "EmplyeeName";
	private static final String TAG_EMPLOYEE_NUMBER = "EmployeeNumber";
	private static final String TAG_PRODUCT_TEAM = "ProductTeam";
	private static final String TAG_DESIGNATION = "CurrentDesignation";
	private static final String TAG_MOBILE = "OfficeMobile";
	private static final String TAG_PHONE = "OfficialPhone";
	private static final String TAG_EMAIL = "Email";
	private static final String TAG_TEAM ="Team";
	
	private String employee_number;
	private String employee_name;
	private String product_team;
	private String designation;
	private String mobile;
	private String office_no;
	private String email;
	private String team;
	
	private Contact contact;
	
	
	public void updateDatabase(JSONArray array){
		//db=new SqliteDatabaseHandler(context);
		
		for(int i=0;i<array.length();i++){
			try {
				JSONObject job=array.getJSONObject(i);
				//System.out.println(job);
				employee_name=Constants.enc.encrypt(job.getString(TAG_EMPLOYEE_NAME));
				//System.out.println(employee_name);
				employee_number=Constants.enc.encrypt(job.getString(TAG_EMPLOYEE_NUMBER));
				product_team=Constants.enc.encrypt(job.getString(TAG_PRODUCT_TEAM));
				designation=Constants.enc.encrypt(job.getString(TAG_DESIGNATION));
				mobile=Constants.enc.encrypt(job.getString(TAG_MOBILE));
				office_no=Constants.enc.encrypt(job.getString(TAG_PHONE));
				email=Constants.enc.encrypt(job.getString(TAG_EMAIL));
				team=Constants.enc.encrypt(job.getString(TAG_TEAM));
				if(job.getString(TAG_PRODUCT_TEAM).equalsIgnoreCase("")){
					product_team=team;
				}
				//db.addContact(new Contact(employee_number,employee_name,mobile,office_no,email,product_team,designation));
				if(Constants.db.getContact(employee_number)==null){
					System.out.println("adding new contacts");
					Constants.db.addContact(new Contact(employee_number,employee_name,mobile,office_no,email,product_team,designation));
				}
				
				/*else if(isContactUpdated()){
					System.out.println("updating contacts");
					SearchActivity.db.addContact(new Contact(employee_number,employee_name,mobile,office_no,email,product_team,designation));
				}*/
				else{
					System.out.println("contacts already added");
				}
		     } 
			 catch (Exception e) {
				Log.e("error",e.toString());
			 }
        	
        	
        }
		
	}
	
	private boolean isContactUpdated(){
		contact=Constants.db.getContact(employee_number);
		if(contact==null){
			System.out.println("contact is null");
		}
		if(contact.getName()!=employee_name){
			return true;
		}else if(contact.getMobileNumber()!=mobile){
			return true;
		}else if(contact.getOfficeNumber()!=office_no){
			return true;
		}else if(contact.getEmail()!=email){
			return true;
		}else if(contact.getProductTeam()!=product_team){
			return true;
		}else if(contact.getDesignation()!=designation){
			return true;
		}else{
			return false;
		}
	}
	

}
