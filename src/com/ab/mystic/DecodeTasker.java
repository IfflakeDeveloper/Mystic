package com.ab.mystic;

import com.ab.mystic.james.Jpeg;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DecodeTasker extends AsyncTask<Object,Integer,Boolean>{
	
	Activity thisActivity = null;
	String picPath = null;
	int Id = 0;

	com.ab.mystic.info.guardianproject.f5android.Extract e= null;
	
	
	
	@Override
	protected void onPreExecute(){
		
		Id = StegTaskArray.decCount;
		StegTaskArray.decCount++;
		
	}
	
	
	@Override
	protected Boolean doInBackground(Object... params){
		
		thisActivity = (Activity) params[0];
		picPath = (String) params[1];
		
		

		StegTaskArray.decodeList.add( new StegTask(picPath,"New Image- "+StegTaskArray.decCount,true,false,true));
		
		
		try{
			e = new com.ab.mystic.info.guardianproject.f5android.Extract((Activity) params[0],(String) params[1]);		
			return true;
		}
		
		catch(Exception e){
			e.printStackTrace();
			return false;
			
		}
	}
	
	
	
	protected void onPostExecute(Boolean result){
			
		if(result){
			
			Log.d(Jpeg.LOG,"Decoding Successful....");
			

			StegTaskArray.decodeList.get(Id).isPending=false;
			//TaskFragment.mAdapter.notifyDataSetChanged();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);

			builder.setMessage(e.fos.toString())
			       .setTitle(R.string.decoded_title)
			       .setPositiveButton("Ok", null);

			AlertDialog dialog = builder.create();
			builder.show();

			
		}
		
		else{
			Toast.makeText(thisActivity,"Decoding failed: Couldn't decode item.", Toast.LENGTH_LONG).show();
			
		}
		
	}
}
