package com.example.voicecontact;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterContact extends ArrayAdapter<ContactMO>{

	/**
	 * ---------------------------------------------------------
	 * Variables 
	 * ---------------------------------------------------------
	 */	
	final String 					TAG = "AdapterContact";
	private HashMap<String, View> 	mHashedItems = new HashMap<String, View>();
	private ArrayList<ContactMO> 	contacts;
	private LayoutInflater 			layout_inflater;		
	Context 						context;
	
	
	/**
	 * ---------------------------------------------------------
	 * Constructors
	 * ---------------------------------------------------------
	 */
	public AdapterContact(Context context, int text_view_resource_id, ArrayList<ContactMO> results){		
		super(context, text_view_resource_id, results);
		
		this.context 				= context;
		this.contacts 				= new ArrayList<ContactMO>(results);				
		this.layout_inflater 		= LayoutInflater.from(context);						
		
	}
	
	/**
	 * ---------------------------------------------------------
	 * Override METHODS
	 * ---------------------------------------------------------
	 * */
	
	@Override
	public void add(ContactMO contact) {
		contacts.add(contact);
	}
	
	@Override
	public void clear() {
		
		contacts.clear();
		
	}
	
	@Override
	public int getCount() {	
		return contacts.size();
	}

	@Override
	public ContactMO getItem(int position) {		
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return position; //id of view is position
	}
	
	public void addAll(ArrayList<ContactMO> c){
		for(int i=0; i<c.size(); i++){
			this.add(c.get(i));
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try{
			final ContactMO contact = contacts.get(position);
			if (contact == null) return null;
			
	        String k = String.valueOf(contact.getName()+contact.getPhone());
	        View view = mHashedItems.get(k);
	        
//	        if(view == null){       
	        	final ViewHolder holder = new ViewHolder();								
				//
				// Get Controls
				//
//	        	LayoutInflater mInflater = (LayoutInflater) context
//	                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				view = layout_inflater.inflate(R.layout.template_contact, parent, false);
				holder.main_item = (RelativeLayout)view.findViewById(R.id.main_item);
				holder.button_call = (Button) view.findViewById(R.id.button_call);
				holder.textview_name = (TextView) view.findViewById(R.id.textview_name);
				holder.textview_no_phone = (TextView) view.findViewById(R.id.textview_no_phone);
				
				//
				//	Set value
				//
				holder.textview_name.setText(contact.getName());
				holder.textview_no_phone.setText(contact.getPhone());
				
				holder.button_call.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						CommonFunction.dialogCall(context, Constant.DIAL, Constant.Call+" "+contact.getName() + " ?", contact.getPhone());
						
					}
				});
				
				holder.main_item.setOnClickListener( new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {

						CommonFunction.dialogCall(context, Constant.DIAL, Constant.Call+" "+contact.getName() + " ?", contact.getPhone());
						
					}
				});
				
				//
				// Set Tag & HashedItems
				//
				view.setTag(holder);						
	        	mHashedItems.put(k, view);
	        	
//	        }
	        	
        	return mHashedItems.get(k);
		} catch(Exception ex) {
			if (ex!=null)
			{
				Log.println(Log.DEBUG, ex.toString(), ex.getMessage());
			}
		}
        return null;
	}
	
	public void showContactFound(ArrayList<ContactMO> contacts, int mode){
		
	}
	
	
	public class ViewHolder{
		
		RelativeLayout main_item;
		Button button_call;
		TextView textview_name;
		TextView textview_no_phone;
		
	}

	
}
