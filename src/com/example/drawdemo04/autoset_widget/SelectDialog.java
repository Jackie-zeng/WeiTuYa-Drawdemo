package com.example.drawdemo04.autoset_widget;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public class SelectDialog extends Dialog{

	public int layoutId;
	private AsyncTask<String, Integer, String> task;
	
	public SelectDialog(Context context, int theme, int layoutId) {
	    super(context, theme);
	    this.layoutId = layoutId;
	}
	public SelectDialog(Context context, int layoutId) {
	    super(context);
	    this.layoutId = layoutId;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(layoutId);
	}
	public void setTask(AsyncTask<String, Integer,String> mtask)
	{
		task = mtask;
	}
	public AsyncTask<String, Integer,String> getTask()
	{
		return task;
	}
}
