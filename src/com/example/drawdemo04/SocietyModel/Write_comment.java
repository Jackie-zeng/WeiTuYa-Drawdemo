package com.example.drawdemo04.SocietyModel;

import java.util.Timer;
import java.util.TimerTask;

import com.example.drawdemo04.R;
import com.example.drawdemo04.initModel.Init_choose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Write_comment extends Activity{

	 private int screenWidth;
	 private int screenHeight;
	 private Button cancelbutton;
	 private Button sendbutton;
	 private EditText edit;
	 private String uniqueUrl;
	 private RelativeLayout commentLayout;
	 private String synctower = "";
	 private MyHandler handler;
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.write_piccomment);
		    
		    uniqueUrl = getIntent().getExtras().getString("uniqueurl");
		    
		    cancelbutton = (Button)findViewById(R.id.head_cancel);
		    
		    sendbutton = (Button)findViewById(R.id.head_offer);
		    
		    edit = (EditText)findViewById(R.id.commentEdit);
		    
		    DisplayMetrics dm=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
			
			commentLayout = (RelativeLayout)findViewById(R.id.commentHeadBar);
	//		RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) commentLayout.getLayoutParams();
	//    	params.height = (int)(screenHeight*0.09);
	//    	commentLayout.setLayoutParams(params);
	    	
	    	

	    	handler = new MyHandler();
	    	
	    	edit.setFocusable(true);
	    	edit.setFocusableInTouchMode(true);
	    	edit.requestFocus();
	    	
	    	Timer timer = new Timer();
	    	timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			    	inputManager.showSoftInput(edit, 0);
			    	
				}
			}, 600);
	    	
	    	sendbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
					
					String content = edit.getText().toString().trim();
					final OfferCommentObject commentObj = new OfferCommentObject(content, uniqueUrl, synctower);
					Init_choose.fixedthreadpool.execute(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							HttpUtil.offerComment(HttpUtil.offerCommentURL, commentObj, handler);
		                    
						}
					});
				}
			});
	 }
	 private class MyHandler extends Handler
	 {
		 @Override
		 public void handleMessage(Message msg)
		 {
			 switch(msg.what)
			 {
			 case 0:
				 Toast.makeText(Write_comment.this, getString(R.string.failTosubmit), 3000).show();
				 break;
			 case 1:
				 Toast.makeText(Write_comment.this, getString(R.string.successTosubmit), 3000).show();
				 
				 Timer timer = new Timer();
			    	timer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							 Intent intent = new Intent();
						     intent.putExtra("content", edit.getText().toString().trim());
						     
						     setResult(20, intent);
						     finish();
						}
					}, 1200);
				 break;
			 }
		 }
	 }
}
