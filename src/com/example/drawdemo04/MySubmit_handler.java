package com.example.drawdemo04;

import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class MySubmit_handler extends Handler{

	 MainActivity activity;
	 int from;
	 public MySubmit_handler(MainActivity activity,int from)
	 {
		 this.activity = activity;
		 this.from = from;
	 }
	 @Override
	 public void handleMessage(Message msg)
	 {
		 switch(msg.what)
		 {
		 case 0:
			   Toast.makeText(activity,activity.getString(R.string.failTosubmit), 3000).show();
			   if(activity.progressDialog != null)
			   {
				   Tools.closeProgress(activity.progressDialog);
				   activity.progressDialog = null;
			   }
			   break;
		 case 1:
			   Toast.makeText(activity, activity.getString(R.string.successTosubmit), 3000).show();
			   if(from == 2)
			   {
				   activity.sendBroadcast(Tools.localadd_flag, "society_gallery",null);
			   }
			   if(from == 4)
			   {
				   activity.sendBroadcast(Tools.concretadd_flag,"gallery_display",null);
			   }
			   
			   activity.submitRecord = UnDoDraw.currentsaved.size();
			   MainActivity.hasDrawn_submit = false;
			   
			   if(activity.progressDialog != null)
			   {
				   Tools.closeProgress(activity.progressDialog);
				   activity.progressDialog = null;
			   }
			   break;
		 case 2:
			   Toast.makeText(activity,activity.getString(R.string.failTosuccess), 3000).show();
			   if(activity.progressDialog != null)
			   {
				   Tools.closeProgress(activity.progressDialog);
				   activity.progressDialog = null;
			   }
			   break;
		 }
	 }
}
