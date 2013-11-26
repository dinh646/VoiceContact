package com.example.voicecontact;

import android.graphics.Bitmap;

public class ContactMO {

	private String id;
	private String name;
	private String phone;
	private Bitmap image;
	private boolean found;
	
	ContactMO (String id, String n, String p, Bitmap i, boolean s){
		this.id 	= id;
		this.name 	= n;
		this.phone 	= p;
		this.image 	= i;
		this.setFound(s);
	}
	
	//	Get name
	public String getName() {
		return name;
	}
	
	//	Set name
	public void setName(String name) {
		this.name = name;
	}
	
	//	Get phone
	public String getPhone() {
		return phone;
	}
	
	//	Set phone
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	//	Get image
	public Bitmap getImage() {
		return image;
	}
	
	//	Set image
	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	
}
