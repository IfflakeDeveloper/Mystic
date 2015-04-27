package com.ab.mystic;

import java.io.File;

import com.ab.mystic.james.Jpeg;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class EncodeTasker extends AsyncTask<Object,Integer,Boolean> {
	
	private Activity thisActivity = null;
	private String picPath = null;
	private File output=null;
	private File input = null;
	private int Id = 0;
	
	@Override
	protected void onPreExecute(){
		
		Log.d(Jpeg.LOG, "encCount= "+StegTaskArray.encCount);
		
		Id = StegTaskArray.encCount;
		StegTaskArray.encCount++;
		
		
		
		
	}
	
	
	/*
	 * Encodes the given message into the image, and returns the following result to PostExecute():
	 * true if encoding is successful, else false. 
	 * Following arguments are used:
	 * params[0] - Activity
	 * params[1] - Path of the image to be encoded (String)
	 * params[2] - Message to be encoded (String)
	 */
	@Override
	protected Boolean doInBackground(Object... params) {
		
		 thisActivity = (Activity) params[0];
		 picPath = (String) params[1];

		StegTaskArray.encodeList.add( new StegTask(picPath,"New Image- "+StegTaskArray.encCount,true,true,false));
		
		
		
		 
		 Log.d(Jpeg.LOG, "Background task now running: ");
		
		try{
			
			com.ab.mystic.info.guardianproject.f5android.Embed e = new com.ab.mystic.info.guardianproject.f5android.Embed((Activity)params[0],(String)params[1],(String) params[2]);
			output=e.outFile;
			input = e.file;
			
			return true;
		}
		catch(Exception e){
			
			e.printStackTrace();
			return false;
		}		
		
	}
	
	
	
	
	/*
	 * Launches an AlertDialog if the encoding is successful	
	 */
	
	protected void onPostExecute(Boolean result){
			
		if(result){
		Log.d(Jpeg.LOG,"Encoding Successful....");
		
		Toast.makeText(thisActivity,"Message encoded successfully!",  Toast.LENGTH_LONG).show();
		
		/*
		String parentFolder=input.getParentFile().getName();
		if(parentFolder!=null&&parentFolder.equals("F5Android"))  input.delete();
		*/	
		
		
		
		StegTaskArray.encodeList.get(Id).isPending=false;

		
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("application/image");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
		intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
		intent.putExtra(Intent.EXTRA_TEXT, "body text");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+output.getAbsolutePath()));

		//thisActivity.startActivity(Intent.createChooser(intent, "Send email..."));
		
		
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(thisActivity)
		        .setSmallIcon(R.drawable.notification_icon)
		        .setContentTitle("Encoding successful!")
		        .setContentText("Click here to send the image.");
		
		
		
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(thisActivity);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		
		mBuilder.setContentIntent(resultPendingIntent);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) thisActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		mNotificationManager.notify(StegTaskArray.encCount, mBuilder.build());
		}
		
		else{
			Toast.makeText(thisActivity,"Encoding failed: Couldn't encode item.", Toast.LENGTH_LONG).show();
			
		}
	}
}
