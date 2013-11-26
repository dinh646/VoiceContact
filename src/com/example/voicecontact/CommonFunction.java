package com.example.voicecontact;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

public class CommonFunction {

	static String TAG = "CommonFunction";
	
	static ContentResolver contentResolver;
	
	public static ProgressDialog GetProgressDialog(Context context, String title, String message)
	{		
		ProgressDialog progress = new ProgressDialog(context); 
		progress.setTitle(title);
		progress.setMessage(message);
		//progress.setCancelable(false);
		
      	return progress;
	}

	
	public static void dialogMessage(Context context, String title, String message) {
		try
		{			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //Do Nothing, just show message with OK button			        	   
			           }
			       });
			// Create the AlertDialog
			AlertDialog dialog = builder.create();			
			dialog.show();
			
		}catch(Exception ex)
		{			
			Log.v(TAG, ex.getMessage());
		}
		
	}
	
	public static void dialogCall(final Context context, String title, String mesage, final String phone) {
		try
		{			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder
				.setTitle(title)
				.setMessage(mesage)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
			        	   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.trim()));
			        	   context.startActivity(intent);
			        	   
			        	   dialog.dismiss();
			        	   
			           }
			       })
			       
			      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				
			// Create the AlertDialog
			AlertDialog dialog = builder.create();			
			dialog.show();
			
		}catch(Exception ex)
		{			
			Log.v(TAG, ex.getMessage());
		}
		
	}

	/**
	 * 
	 * @param context
	 * 
	 * @return array contacts
	 * 
	 */
	public static ArrayList<ContactMO> getContactList(Context context) {
		
		ArrayList<ContactMO> contacts = new ArrayList<ContactMO>();
		
		ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                  String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                  try {
					name = new String(name.getBytes("UNICODE"), "UNICODE");
				  } catch (UnsupportedEncodingException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
                  String phone=null;
                  if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = cr.query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                               null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                               new String[]{id}, null);
                     
                     while (pCur.moveToNext()) {
                         phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                         
                     }
                     
                     contacts.add(new ContactMO(id, name, phone, null, false));
                     
                    pCur.close();
                    
                }
            }
        }
		
		return contacts;
	}
	
	/**
	 * Get Contact Photo
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap getContactPhoto(Context context, String mobilenumber){
		if (mobilenumber == null || mobilenumber.isEmpty() || mobilenumber.equals(Constant.DEFAULT_NULL_VALUE_PHONE_CONTACT))
		{
			return null;
		}
		
		Bitmap photo = null;
		contentResolver = context.getContentResolver();
		try
		{		
			//
        	// Photo
        	//	      
        	final Integer thumbnailId = fetchThumbnailId(mobilenumber);
            if (thumbnailId != null) 
            {
                photo = fetchThumbnail(thumbnailId);        		
            }

		} catch(Exception ex) {
			Log.println(Log.DEBUG, ex.toString(), ex.getMessage());	
			
		}
		
		return photo;
	}
	
	final static Bitmap fetchThumbnail(final int thumbnailId){

	    final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, thumbnailId);
	    final Cursor cursor = contentResolver.query(uri, PHOTO_BITMAP_PROJECTION, null, null, null);

	    try {
	        Bitmap thumbnail = null;
	        if (cursor.moveToFirst()) {
	            final byte[] thumbnailBytes = cursor.getBlob(0);
	            if (thumbnailBytes != null) {
	                thumbnail = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
	            }
	        }
	        return thumbnail;
	    }
	    finally {
	        cursor.close();
	    }

	}
	
	private static final String[] PHOTO_BITMAP_PROJECTION = new String[] {
	    ContactsContract.CommonDataKinds.Photo.PHOTO
	};
	
	
	private static Integer fetchThumbnailId(String phoneNumber) {

	    final Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	    final Cursor cursor = contentResolver.query(uri, PHOTO_ID_PROJECTION, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

	    try {
	        Integer thumbnailId = null;
	        if (cursor.moveToFirst()) {
	            thumbnailId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
	        }
	        return thumbnailId;
	    }
	    finally {
	        cursor.close();
	    }

	}
	
	private static final String[] PHOTO_ID_PROJECTION = new String[] {
	    ContactsContract.Contacts.PHOTO_ID
	};
	
	public static ArrayList<ContactMO> searchContact(ArrayList<ContactMO> contats, int mode, String key){
		
		ArrayList<ContactMO> contats_serach = new ArrayList<ContactMO>();
		
		for(int i=0; i<contats.size(); i++){
			
			ContactMO contact = contats.get(i);
			
			if(mode == Constant.SEARCH_NAME){
				if(searchIndexOf(contact.getName(), key) != -1 ){
					contact.setFound(true);
					contats_serach.add(contact);
				}
			}
			else if(mode == Constant.SEARCH_PHONE){
				if(searchIndexOf(contact.getPhone(), key) != -1 ){
					contact.setFound(true);
					contats_serach.add(contact);
				}
			}
			
		}
		
		return contats_serach;
		
	}
	
	/**
	 * 
	 * Highlight TextView
	 * 
	 * @param tv
	 * @param start
	 * @param end
	 * 
	 */
	public static void highlightTextView(TextView tv, int start, int end, int color){
		
		String str = tv.getText().toString();
		
		Spannable WordtoSpan = new SpannableString(str);        
		WordtoSpan.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(WordtoSpan);
		tv.invalidate();
		
	}
	
	/**
	 * 
	 * Search Index Of
	 * 
	 * @param str
	 * @param str_search
	 * @return index of
	 * 
	 */
	public static int searchIndexOf(String str, String str_search){
		int indexof = -1;
		str = unAccent(str.toUpperCase());
		str_search = str_search.toUpperCase();
		
		for(int i=0; i<str.length(); i++){
			indexof = str.indexOf(str_search, i);
			if(indexof != -1){
				return indexof;
			}
		}
		return indexof;
	}

	@SuppressLint("NewApi")
	public static String unAccent(String s) {
	      //
	      // JDK1.5
	      //   use sun.text.Normalizer.normalize(s, Normalizer.DECOMP, 0);
	      //
	      String temp = Normalizer.normalize(s, Normalizer.Form.NFC);
	      
	      Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	      System.out.println(pattern);
	      return pattern.matcher(temp).replaceAll("");
	  }
	
}
