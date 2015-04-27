package com.ab.mystic.info.guardianproject.f5android;


import com.ab.mystic.james.Jpeg;
import com.ab.mystic.james.JpegEncoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

public class Embed {
	Activity a;
	Bitmap image = null;
	FileOutputStream dataOut = null;
    public File file;
	public File outFile;
	File root_dir;
    JpegEncoder jpg;
    int i, Quality = 90;
    // Check to see if the input file name has one of the extensions:
    // .tif, .gif, .jpg
    // If not, print the standard use info.
    boolean haveInputImage = false;
    String comment = "JPEG Encoder Copyright 1998, James R. Weeks and BioElectroMech.  ";
    String inFileName = null;
    String outFileName = null;
    String secret_message = null;
    
    public interface EmbedListener {
    	public void onEmbedded(File outFile);
    }
    
    public Embed(Activity a, String inFileName, String secret_message) {
    	this(a, "F5Android", inFileName, null, secret_message);
    }
    
    public Embed(Activity a, String dump_dir, String inFileName, String secret_message) {
    	this(a, dump_dir, inFileName, null, secret_message);
    	
    	this.secret_message = secret_message;
    }
    
    public Embed(Activity a, String dump_dir, String inFileName, String outFileName, String secret_message) {
    	this.a = a;
    	this.inFileName = inFileName;
    	this.file = new File(this.inFileName);
    	
    	root_dir = new File(Environment.getExternalStorageDirectory(), dump_dir);
    	if(!root_dir.exists())
    		root_dir.mkdir();
    	
    	if(outFileName == null) {
    		String extension = this.file.getName().substring(this.file.getName().lastIndexOf(".") - 1);
    		this.outFile = new File(root_dir, this.file.getName().replace(extension, "_embed.jpg"));
    		this.outFileName = this.outFile.getAbsolutePath();
    	} else {
    		this.outFileName = outFileName;
    		this.outFile = new File(outFileName);
    	}
    	    	
    	i = 1;
    	while(outFile.exists()) {
    		this.outFile = new File(root_dir, outFile.getName().substring(0, outFile.getName().lastIndexOf(".")) + "_" + i++ + ".jpg");
    		if(i > 100)
    			return;
    	}
    	
    	if(this.file.exists()) {
    		try {
    			dataOut = new FileOutputStream(outFile);
    		} catch(final IOException e) {}
    		
    		image = BitmapFactory.decodeFile(this.inFileName);
    		jpg = new JpegEncoder(image, Quality, dataOut, comment);
    		
    		try {
    			if(jpg.Compress(new ByteArrayInputStream(secret_message.getBytes()))) {
    				//((EmbedListener) a).onEmbedded(outFile);
    				
    				/*
    				Log.d(Jpeg.LOG,"Launching AlertDialog....");
    				AlertDialog.Builder builder = new AlertDialog.Builder(a);

    				builder.setMessage(R.string.dialog_message)
    				       .setTitle(R.string.dialog_title)
    				       .setPositiveButton("Ok", null);

    				AlertDialog dialog = builder.create();
    				builder.show();
    			*/
    				MediaScannerConnection.scanFile(a.getBaseContext(), new String[] {outFile.getPath()}, null, null);
    				//MainActivity.galleryAddPic();
    			
    			}
    		} catch(final Exception e) {
    			Log.e(Jpeg.LOG, e.toString());
    			e.printStackTrace();
    		}
    		
    		try {
    			dataOut.close();
    		} catch(final IOException e) {
    			Log.e(Jpeg.LOG, e.toString());
    			e.printStackTrace();
    		}
    	}
    	
    	
    }
	
}
