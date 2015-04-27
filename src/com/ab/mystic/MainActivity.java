package com.ab.mystic;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ab.mystic.james.Jpeg;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	public final static int RESULT_LOAD_IMAGE=100;
	public final int REQUEST_TAKE_PHOTO= 70;
	public String imageCapturePath=null;
	private static String picturePath=null;
	String imageFileName=null;
	final android.app.Activity main_ac = this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   
        
        StegTaskArray.initialise();
        
        

    ImageButton buttonForLoadImage = (ImageButton) findViewById(R.id.browse_button);
    
    
    buttonForLoadImage.setOnClickListener( new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		    
			startActivityForResult(i, RESULT_LOAD_IMAGE);

		}
	});
    
    
    
    
    
    
    
    ImageButton toEncode= (ImageButton) findViewById(R.id.toEncode);
    
    toEncode.setOnClickListener( new View.OnClickListener(){
    	
    	@Override
    	
    	public void onClick(View v){
    	
    		
    		
    	Intent newActivity = new Intent(main_ac, EncodeActivity.class);
    	newActivity.putExtra("pathPic",picturePath );
    	startActivity(newActivity);	
    	}
    	
    	
    } );
    
      
    
    ImageButton toDecode = (ImageButton) findViewById(R.id.toDecode);
    toDecode.setOnClickListener( new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
						
			//com.ab.mystic.info.guardianproject.f5android.Extract e = new com.ab.mystic.info.guardianproject.f5android.Extract(main_ac,picturePath);		
			Toast.makeText(main_ac, "Decoding task added!", Toast.LENGTH_SHORT).show();
			new DecodeTasker().execute(new Object[]{main_ac,picturePath});
		}
	});
    
    ImageButton camButton=(ImageButton) findViewById(R.id.cameraButton);
    camButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			captureImage();
			
		}
	});
   
    }
    
    
    
    
    
    
    
    
   
    protected void captureImage(){
    	
    	
    	Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	
    	  File photoFile = null;
          try {
              photoFile = createImageFile();
          } catch (Exception ex) {
              Log.d(Jpeg.LOG, "Image file not created");
        	  
          }
          
          if (photoFile != null) {
              camIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                      Uri.fromFile(photoFile));
             
              startActivityForResult(camIntent, REQUEST_TAKE_PHOTO);
          }
    	
    }
    
    
    
    private File createImageFile() throws Exception {
        
       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         	 imageFileName = "JPEG_" + timeStamp;
      //  File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File storageDir= main_ac.getCacheDir();
        
        String dump_dir = "F5Android";
        File root_dir = new File(Environment.getExternalStorageDirectory(), dump_dir);
    	if(!root_dir.exists())
    		root_dir.mkdir();
    	
    	
        File image = new File(root_dir,imageFileName+".jpg");
        
        
        
        imageCapturePath= image.getAbsolutePath();
        
        return image;
    }
    
    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(picturePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
       this.sendBroadcast(mediaScanIntent);
    }
    
    @Override 
    
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            Log.d(Jpeg.LOG, "Image file loading from:"+picturePath);
            
            
            ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            
            imageView.setImageBitmap(
            	    decodeSampledBitmapFromResource(getResources(), R.id.imageView1, 100, 100));
            imageView.setVisibility(1);
        }
        
        if(requestCode==REQUEST_TAKE_PHOTO&&resultCode==RESULT_OK){
        	
        	picturePath=imageCapturePath;
        	
        	MediaScannerConnection.scanFile(main_ac.getBaseContext(), new String[] {picturePath}, null, null);
			
        	
        	try { galleryAddPic(); }
        	catch(Exception e){ Log.d(Jpeg.LOG, "galleryAddPic() didn't work!"); }
        	
        	try
        	{
        		MediaStore.Images.Media.insertImage(getContentResolver(), picturePath, imageFileName , null);
        	}
        	catch(Exception e){ Log.d(Jpeg.LOG, "Second failsafe didn't work!"); }
        	
        	 ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            // imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
             
             imageView.setImageBitmap(
            	    decodeSampledBitmapFromResource(getResources(), R.id.imageView1, 100, 100));
             
             imageView.setVisibility(1);
        	
        }
        
        Bitmap bmp = BitmapFactory.decodeFile(picturePath);
        
        if(bmp.getHeight()*bmp.getWidth()>4000000){
        	bmp=null;
        	compressThis(picturePath);
        }
     
    }
    
    
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
    
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
            int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        
        BitmapFactory.decodeFile(picturePath);
        
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath);
        		//BitmapFactory.decodeResource(res, resId, options);
    } 
    
    
    
    public void compressThis(String imgPath){
		
    	//Bitmap bmp = BitmapFactory.decodeFile(imgPath);
    	//Bitmap bmp = decodeSampledBitmapFromResource(getResources(), R.id.imageView1, 768, 1024);
    	Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgPath), 1024, 768, false);
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 30, bos);
		
		try{
		
			File imgFile = new File(imgPath);
		 
			FileOutputStream fos;
			
			String parentFolder=imgFile.getParentFile().getName();
		
			if(parentFolder!=null&&parentFolder.equals("F5Android"))	
				fos = new FileOutputStream(new File(imgPath+"_compressed.jpg")); 
			else {
				File newImageFile = createImageFile();
				fos = new FileOutputStream(newImageFile);
				picturePath=newImageFile.getAbsolutePath();
			}
		
		 bos.writeTo(fos);
		 fos.close();
		}
		 catch(Exception e){}
		
		
		
	}
    
    
    
    }
    

	

