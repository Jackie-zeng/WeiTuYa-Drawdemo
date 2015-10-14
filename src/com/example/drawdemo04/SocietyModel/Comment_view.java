package com.example.drawdemo04.SocietyModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.DataBase.DBadapter;
import com.example.drawdemo04.autoset_widget.SelectDialog;
import com.example.drawdemo04.initModel.Init_choose;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Comment_view extends Activity{
	
	private int REQUSTCODE = 0;
	private ListView commentList;
	private ArrayList<Comment> commentContent = new ArrayList<Comment>();
	private CommentListAdapter adapter;
	private int screenWidth,screenHeight;
	private Button offerComment;
	private Button backTo;
	private EditText edit;
	private RelativeLayout commentLayout;
	private DBadapter madapter;
	private Handler mHandler;
	private String uniqueUrl;
	private String synctower = "";
	private int lastX;
	private boolean door = false;
	private int from = 0;
	private String url_origin,image_url_origin;
//	private String initpic,tuyapic;
	private SelectDialog progress;
	
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.comment_page);
		    
		    Intent intent = getIntent();
		    Bundle bundle = intent.getExtras();
		    from = bundle.getInt("from");
		    
		    uniqueUrl = bundle.getString("uniqueurl");
		    
		    if(from == 1)
		    {
		    	url_origin= bundle.getString("url_origin");
		        image_url_origin =  bundle.getString("image_url_origin");
		    }
		        
		    offerComment = (Button)findViewById(R.id.CommentOffer);
		    backTo = (Button)findViewById(R.id.backTo);
		    edit = (EditText)findViewById(R.id.CommentEdit);
		    
		    DisplayMetrics dm=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
			
			mHandler = new MyHandler();
			
			addData();
		    commentList = (ListView)findViewById(R.id.commentList);
	    	
		    if(from == 1)
		        adapter = new CommentListAdapter(this, commentContent,screenWidth,screenHeight,url_origin,image_url_origin);
		    else
		    	adapter = new CommentListAdapter(this, commentContent,screenWidth,screenHeight,null,null);
		    
		    Init_choose.asynloader.setAdapter(adapter);
		    
		    commentList.setAdapter(adapter);
		    
		    offerComment.setOnClickListener(offerComment_onclick);
			
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
		    Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		    String str = formatter.format(curDate);
		    Log.i("when",str);
		    
		    commentList.setOnTouchListener(moveOntouch);
		    backTo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					finish();
				}
			});
	 }
/*
	 public int getCommentCount(){
		 return commentContent.size();
	 }
	*/ 
	 
     private void addData(){
  //  	 commentContent.add(new Comment(displayurl));
    	 getFromInternet();
    
     }
     private class MyHandler extends Handler
     {
    	 @Override
    	 public void handleMessage(Message msg){
    		 
    		 switch(msg.what)
    		 {
    		 case 0:
    			 Toast.makeText(Comment_view.this, getString(R.string.failTosuccess), 3000).show();
    			 Tools.closeProgress(progress);
    			 break;
    		 case 1:
    			 Toast.makeText(Comment_view.this, getString(R.string.noComments), 3000).show();
    			 Tools.closeProgress(progress);
    			 break;
    		 case 2:
    			 Toast.makeText(Comment_view.this, getString(R.string.failToget), 3000).show();
    			 Tools.closeProgress(progress);
    			 break;
    		 case 3:
    			 for(int i=0;i<((ArrayList<Comment>)msg.obj).size();i++)
				 {
					commentContent.add(((ArrayList<Comment>)msg.obj).get(i));
				 }
    			 Log.i("commentcontentsize1",String.valueOf(commentContent.size()));
    			 Tools.closeProgress(progress);
    			 
    			 adapter.notifyDataSetChanged();
    			 break;
    		 case 4:
    			 adapter.notifyDataSetChanged();
    			 
    			 getFromInternet();
    			 break;
    		 case 5:
    			 adapter.notifyDataSetChanged();
    			 break;
    		 case 6:
    			 Toast.makeText(Comment_view.this, getString(R.string.ConnectTimeout), 3000).show();
    			 Tools.closeProgress(progress);
    			 break;
    		 case 7:
    			 Toast.makeText(Comment_view.this, getString(R.string.noComments), 3000).show();
    			 Tools.closeProgress(progress);
    			 break;
    		 }
    	 }
     }
     
     private void getFromInternet()
     {
    	   final int startId = commentContent.size();       //从startId 开始加载
    	   progress = Tools.showProgress(this);
  		   Init_choose.fixedthreadpool.execute(new Runnable() {
	
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					
					ArrayList<Comment> comments = HttpUtil.getCommentsFromURL(HttpUtil.getCommentsURL, uniqueUrl, mHandler);
					if(comments != null && comments.size() > 0)
					{
						Message msg = mHandler.obtainMessage(3,comments);
						mHandler.sendMessage(msg);
					}
				}
			});
     }
     
     
     @Override
     public void finish()
     {
    	 Log.i("finish","finish");
    	 if(door == false)
    	 {
    		 door = true;
    		 Tools.commentDisplay_bit = null;
        	 super.finish();
    	 }
     }
     
     private OnClickListener offerComment_onclick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			
			final String content = edit.getText().toString().trim();
			if(content.equals(""))
			{
				Toast.makeText(Comment_view.this, getString(R.string.PleaseInput), 3000).show();
				return ;
			}
			
			InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
			
			final OfferCommentObject commentObj = new OfferCommentObject(content, uniqueUrl, synctower);
			
			edit.setText("");
			progress = Tools.showProgress(Comment_view.this);
			
			final Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					 {
					 case 0:
						 Toast.makeText(Comment_view.this, getString(R.string.failTosubmit), 3000).show();
						 break;
					 case 1:
						 Toast.makeText(Comment_view.this, getString(R.string.successTosubmit), 3000).show();
						 Time t = new Time();
						 t.setToNow();
						    
						 String date = String.valueOf(t.month+1) + "-" + String.valueOf(t.monthDay);
						 String time = String.valueOf(t.hour) + ":" + String.valueOf(t.minute);
						    
		  				 Comment comment = new Comment("null", date, time, content, null, uniqueUrl);
		  				 commentContent.add(0, comment);
		  				 adapter.notifyDataSetChanged();
						 break;
					 }
					Tools.closeProgress(progress);
				}
			};
			
			Init_choose.fixedthreadpool.execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					HttpUtil.offerComment(HttpUtil.offerCommentURL, commentObj, handler);
   
				}
			});
		}
	};
	private OnTouchListener moveOntouch = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO 自动生成的方法存根
			
			switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				lastX = (int)event.getRawX();

				break;
				
			case MotionEvent.ACTION_MOVE:
				Log.i("ConcretPicdisplay_move",String.valueOf(event.getRawX()));
				int dx=(int)event.getRawX()-lastX;
				
				if(dx > 300)
				{
					finish();
				}
				break;
			case MotionEvent.ACTION_UP:
		
				break;
			}
			
			return false;
		}
	};
	


}
