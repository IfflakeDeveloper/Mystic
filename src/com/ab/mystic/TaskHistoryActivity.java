package com.ab.mystic;

import com.ab.mystic.james.Jpeg;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;



public class TaskHistoryActivity extends FragmentActivity {

	 static FragmentTabHost taskHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_task_history);
		
		
		taskHistory = (FragmentTabHost) findViewById(android.R.id.tabhost);
		
		taskHistory.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		
		taskHistory.addTab(taskHistory.newTabSpec("encodeTab").setIndicator("Encode Tasks"),TaskFragment.class,null);
		taskHistory.addTab(taskHistory.newTabSpec("decodeTab").setIndicator("Decode Tasks"),TaskFragment.class,null);
		
		
		Log.d(Jpeg.LOG,"xxxx");
	}
	

}
