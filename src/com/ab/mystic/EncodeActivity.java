package com.ab.mystic;

import java.io.File;

import com.ab.mystic.R.layout;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;




import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
public class EncodeActivity extends Activity {


	final android.app.Activity ac = this;
	private EditText user_msg;
	private StringBuffer ExifData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_encoding);
		

        user_msg =  (EditText) findViewById(R.id.user_message);
		
		ImageView encodedImage = (ImageView) findViewById(R.id.encodedImage);
		
		 final String imagePath=getIntent().getExtras().getString("pathPic");
		
		
		if(imagePath!=null) {
			//encodedImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
			encodedImage.setImageBitmap(
            	    MainActivity.decodeSampledBitmapFromResource(getResources(), R.id.encodedImage, 100, 100));
            
			
			encodedImage.setVisibility(android.view.View.VISIBLE);
		}
		
		Button encodeButton = (Button) findViewById(R.id.encode);
		encodeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				final String msg=user_msg.getText().toString();
				
				
				
				new EncodeTasker().execute(new Object[]{ac,imagePath,msg});
				Toast.makeText(ac, "New encoding task added!", Toast.LENGTH_SHORT).show();				
			}
		});
		
		
		Button taskHistoryButton = (Button) findViewById(R.id.taskHistoryButton);
		
		taskHistoryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {


				Intent i = new Intent(ac,TaskHistoryActivity.class);
				startActivity(i);
	
			
				
			
			
			}
		});
		
	
		
		 try {
			 	 ExifData = new StringBuffer();
	            Metadata metadata = ImageMetadataReader.readMetadata(new File(imagePath));

	            

	   		 for (Directory directory : metadata.getDirectories()) {

	   	            
	   	            for (Tag tag : directory.getTags()) {
	   	                
	   	            	ExifData.append(tag).append("\n");
	   	            }
	            
	        }
		 }
	   		 
		 catch(Exception e){}
		
		
		
	}
	
	
	
	public void onCheckboxClick(View v){
    	if(v==null) return;
    	if(v.getId()==R.id.EXIFcheckbox){

        	final CheckBox checkBox = (CheckBox) v;
    		if(checkBox.isChecked()) user_msg.setText(ExifData.toString());
    		else user_msg.setText("");
    	}
    }
	
}
