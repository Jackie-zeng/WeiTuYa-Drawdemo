package com.example.drawdemo04.SocietyModel;

import android.app.Activity;
import android.os.AsyncTask;

public class ProgressAsyncTask extends AsyncTask<String, Integer, String>{

	Activity activity;
	public ProgressAsyncTask(Activity activity)
	{
		this.activity = activity;
	}
	

	@Override
	protected String doInBackground(String... params) {
		// TODO �Զ����ɵķ������
		
		return null;
	}
	
	

}
