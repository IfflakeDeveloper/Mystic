package com.ab.mystic;

public class StegTask {

	long id;
	String picturePath;
	String imageName;
	boolean isPending=true;
	boolean isEncoding = false;
	boolean isDecoding = false;
	
	
	public StegTask(String picPath, String imgName, boolean aIsPending, boolean aIsEncoding, boolean aIsDecoding){
	
		this.picturePath=picPath;
		this.imageName=imgName;
		this.isPending=aIsPending;
		this.isEncoding=aIsEncoding;
		this.isDecoding=aIsDecoding;
	
	
}

}