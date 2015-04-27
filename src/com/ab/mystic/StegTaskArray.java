package com.ab.mystic;

import java.util.ArrayList;

public class StegTaskArray {
	
	static int encCount=0;
	static int decCount=0;
	
	static ArrayList<StegTask> encodeList;
	static ArrayList<StegTask> decodeList;
	
	static void initialise(){
		
		encodeList = new ArrayList<StegTask>();
		decodeList = new ArrayList<StegTask>();
	}
}
