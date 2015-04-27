package com.ab.mystic;

import com.ab.mystic.james.Jpeg;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskFragment extends ListFragment {
	
	
	private TaskListAdapter mAdapter;
	private String currentTab = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.task_view, container, false);
	
		return v;
		
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		
		currentTab = TaskHistoryActivity.taskHistory.getCurrentTabTag();
		
		Log.d(Jpeg.LOG, "ListFragment's onViewCreated called !");
		if(currentTab.equals("encodeTab"))
		mAdapter = new TaskListAdapter(this.getActivity().getApplicationContext(),R.layout.task_list_item,StegTaskArray.encodeList);
		else
		mAdapter = new TaskListAdapter(this.getActivity().getApplicationContext(),R.layout.task_list_item,StegTaskArray.decodeList);
		
		setListAdapter(mAdapter);
	}
	
	
}
