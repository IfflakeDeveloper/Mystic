package com.ab.mystic;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<StegTask> {
	
	Context context;
	ArrayList<StegTask>values;
	
	public TaskListAdapter(Context c, int textViewResourceId, ArrayList<StegTask> objects) {
		super(c, textViewResourceId, objects);

		context= c;
		values = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Holder holder = null;
		View rowView=null;
		if(convertView==null){
		LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.task_list_item, parent, false);
		


		 holder = new Holder();
		
		holder.name = (TextView) rowView.findViewById(R.id.taskName);
		holder.status = (TextView) rowView.findViewById(R.id.taskStatus);
		holder.taskImage = (ImageView) rowView.findViewById(R.id.taskImage);
		
		rowView.setTag(holder);
		}
		
		else{
			holder = (Holder) convertView.getTag();
		}
		
		final StegTask mView = values.get(position);
		
		holder.name.setText(mView.imageName);
	
		
		new AsyncTask<Holder,Integer, Bitmap>(){
			
			Holder h = null; 
			
			protected Bitmap doInBackground(Holder...params){
				
				h=params[0];
				return BitmapFactory.decodeFile(mView.picturePath);
				
			}
			
			protected void onPostExecute(Bitmap result){
				
				h.taskImage.setImageBitmap(result);				
			}
			
		}.execute(holder);
		
		
		//holder.taskImage.setImageBitmap(
	    //         	    com.ab.mystic.MainActivity.decodeSampledBitmapFromResource(context.getResources(), R.id.imageView1, 20, 20));
	             
		
		if(mView.isPending){
			if(mView.isEncoding)
				holder.status.setText("Encoding ongoing");
			else holder.status.setText("Decoding ongoing");
		}
		else{
			
			if(mView.isEncoding)
				 holder.status.setText("Encoding completed!");
			else holder.status.setText("Decoding completed.");
			
		}
		
		holder.name.setVisibility(View.VISIBLE);
		holder.status.setVisibility(View.VISIBLE);
		
		return rowView;
	}
	
	
	
}



 class Holder{
	TextView name;
	TextView status;
	ImageView taskImage;
	
}