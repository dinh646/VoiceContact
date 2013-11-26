package com.example.voicecontact;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voicecontact.util.Util;

public class MainActivity extends Activity {

	public static String TAG = "MainActivity";
	
	private static final int RESULT_SPEECH = 1;
	
	private LinearLayout main_sample_search;
	private ImageView button_speak;
    private EditText edittext_search; 
    
    private ListView listview_contact;
    private TextView listview_empty;
    
    private AdapterContact adapter;
    private ArrayList<ContactMO> contacts = new ArrayList<ContactMO>();
	
    //	More
    ProgressDialog progress;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		//
		//	Get controls
		//
		main_sample_search = (LinearLayout) findViewById(R.id.main_sample_search);
		button_speak = (ImageView) findViewById(R.id.button_speak);
		edittext_search = (EditText) findViewById(R.id.edittext_search);
		progress = CommonFunction.GetProgressDialog(MainActivity.this, "", getString(R.string.loading));
		listview_contact = (ListView) findViewById(R.id.listview_contact);
		listview_empty = (TextView) findViewById(android.R.id.empty);
		listview_contact.setEmptyView(listview_empty);
		
		adapter = new AdapterContact(MainActivity.this, R.layout.template_contact, contacts);
		
		//
		//	Set value list view contact
		//
		listview_contact.setAdapter(adapter);
		
		getLocalPhoneContactList();
		
		//
		//	Set event button speak
		//
		button_speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	Log.i(TAG, Charset.defaultCharset().name());

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }); 
		
		//
		//	Set event edittext search
		//
		edittext_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@SuppressLint("NewApi")
			@Override
			public void afterTextChanged(Editable s) {
				adapter.clear();
				String key = edittext_search.getText().toString();
				
				if( (key != null) && (!key.isEmpty()) ){
					adapter.addAll(CommonFunction.searchContact(contacts, Constant.SEARCH_NAME, key));
				}
				else{
					adapter.addAll(contacts);
				}
				
				adapter.notifyDataSetChanged();
				
			}
		});
		
	}

	/**
	 * ASYNC TASK
	 * 	
	 * getLocalPhoneContactList
	 *
	 **/
	private void getLocalPhoneContactList(){
		progress.show();
		new RetreiveFeedTask_GetLocalPhoneContactList().execute();
	}
	
	private class RetreiveFeedTask_GetLocalPhoneContactList extends AsyncTask<String, Void, ArrayList<ContactMO>> {
		//
		// Varaiables
		//
		private String TAG = "RetreiveFeedTask_GetLocalPhoneContactList";	
		private boolean error = false;		
			
		protected void onPreExecute() {
			Log.println(Log.DEBUG, TAG, "onPreExecute()");
		}
		
		protected void onPostExecute(ArrayList<ContactMO> feed) {
	    	if (error) {	    		
	    		Log.println(Log.DEBUG, TAG,  "onPostExecute()");
	    	} else {
	    		SetGetLocalPhoneContactListResult(feed);
	    	}
		}
		
		protected void onCancelled() {   	   
	    	Log.println(Log.DEBUG, TAG, "onCancelled()");    	   
		}
		
		protected ArrayList<ContactMO> doInBackground(String... urls) {
			ArrayList<ContactMO> response = null;
			
			try {				
				response = CommonFunction.getContactList(getApplicationContext());		
				contacts = response;
			}
			catch (Exception ex) {
				Log.println(Log.DEBUG, ex.toString(), ex.getMessage());
			}
			
			return response;
		}
		
		private void SetGetLocalPhoneContactListResult(ArrayList<ContactMO> response){
			try {
				if(response == null){											              
					CommonFunction.dialogMessage(MainActivity.this, Constant.APP_TITLE, CommonMessage.NO_CONTACTS_FOUNDED);			    						
	    	   }
	    	   else {
	    		   
	    		   adapter.addAll(response);
	    		   adapter.notifyDataSetChanged();
//	    		   
//	    		   getContactPhone();
//	    		   getContactEmail();
	    		   
	    	   	}				     
			} catch(Exception ex) {
				Log.println(Log.DEBUG, ex.toString(), ex.getMessage());				
			}
			
			progress.dismiss();
	    }	
	}
	
	
	/**
	 * ASYNC TASK
	 * 	
	 * getContactPhoto
	 *
	 **/
//	private void getContactPhoto(){
//		progress.show();
//		new RetreiveFeedTask_GetContactPhoto().execute();
//	}
	/**
	 * RetreiveFeedTask_GetContactPhoto
	 * 
	 * @author Administrator
	 *
	 */
//	private class RetreiveFeedTask_GetContactPhoto extends AsyncTask<String, Void, ArrayList<ContactMO>> {
//		//
//		// Varaiables
//		//
//		private String TAG = "RetreiveFeedTask_GetContactPhoto";	
//		private boolean error = false;		
//			
//		protected void onPreExecute() {
//			Log.println(Log.DEBUG, TAG, "onPreExecute()");
//		}
//			
//		protected void onPostExecute(ArrayList<AgentReferralMO> feed) {
//	    	if (error) {	    		
//	    		Log.println(Log.DEBUG, TAG,  "onPostExecute()");
//	    	} else {
//	    		SetGetContactPhotoResult(feed);
//	    	}
//		}
//		
//		protected void onCancelled() {   	   
//	    	Log.println(Log.DEBUG, TAG, "onCancelled()");    	   
//		}
//		
//		protected ArrayList<AgentReferralMO> doInBackground(String... urls) {
//			ArrayList<AgentReferralMO> response = new ArrayList<AgentReferralMO>();
//			
//			try {				
//				for (int i=0; i< adapter.getCount(); i++)
//				{
//					AgentReferralMO phoneContact = adapter.getItem(i);
//					if (phoneContact!=null && phoneContact.getPhotoBitmap()==null)
//					{
//						Bitmap photo = CommonFunction.GetContactPhoto(getApplicationContext(), phoneContact.getMobile());						
//						phoneContact.setPhotoBitmap(photo);											
//						response.add(phoneContact);						
//					}
//				}
//			}
//			catch (Exception ex) {
//				Log.println(Log.DEBUG, ex.toString(), ex.getMessage());
//			}
//			
//			return response;
//		}
//	
//		//
//		// * Set  Result of get contact photo
//		// * @param response
//	    //
//		private void SetGetContactPhotoResult(ArrayList<AgentReferralMO> response){
//			try {
//				if(response == null){											              
//	    	   }
//	    	   else {
//	    		   for (int i=0; i< adapter.getCount(); i++)
//					{
//		    		   adapter.notifyItem(adapter.getItem(i));
//					}
//	    	   	}				     
//			} catch(Exception ex) {
//				Log.println(Log.DEBUG, ex.toString(), ex.getMessage());				
//			}
//			
//			progress.dismiss();
//	    }	
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
	        case RESULT_SPEECH: {
	            if (resultCode == RESULT_OK && null != data) {
	
	                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	                
	                String key="";
	                
	                
	                main_sample_search.removeAllViews();
	                int i = 0;
	                while(i<text.size()){
	                	
	                	if( (text.get(i) != null) && (text.get(i).length() > 0) ){
	                	
//	                		try {
	                			
								key = text.get(i);
//								key.
								
//							} catch (UnsupportedEncodingException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
	                		
	                		if(i==0){
	                			edittext_search.setText(key);
	                		}
	                		
		                	final Button text_search = new Button(this);
		                	
		                	LinearLayout.LayoutParams params_1 	= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		                	
		                	params_1.weight 	= 1;
		                	params_1.width 	= 0;
		                	params_1.height 	= LayoutParams.WRAP_CONTENT;
							text_search.setPadding((int)this.getResources().getDimension(R.dimen.margin_left_normal), 0, (int)this.getResources().getDimension(R.dimen.margin_right_normal), 0);
							text_search.setLayoutParams(params_1);
							
							text_search.setTextSize(this.getResources().getDimension(R.dimen.text_size_medium));
							text_search.setText(key);
							
							text_search.setBackgroundResource(R.drawable.style_button_sample_search);
							if(i==0){
								text_search.setFocusable(true);
		                	}
							text_search.setOnClickListener( new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									
									edittext_search.setText(text_search.getText().toString());
									
								}
							});
							main_sample_search.addView(text_search);
							
							ImageView h_divider = new ImageView(this);
							LinearLayout.LayoutParams params_2 	= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		                	params_2.weight 	= 1;
		                	params_2.width 	= 0;
		                	params_2.height 	= LayoutParams.WRAP_CONTENT;
		                	h_divider.setImageResource(R.drawable.h_devider);
		                	main_sample_search.addView(h_divider);
		                	
	                	}
	                	
	                	i++;
	                	
	                }
	                
	                
	
	            }
	            break;
	        }

        }
	}
	
}
