package com.example.drawdemo04.SocietyModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.Society_gallery.ReceiveBroadCast;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class ConcretPicDisplay extends Activity{

//	private ImageView currentimg;
	private final int onResume = 1;
	private final int onPause = 0;
	private RecyclingImageView currentimg;
	private Gallery displayGallery;
//	private ArrayList<String> imgUrls;
	private Tuya tuya;
	private int screenWidth;
	private int screenHeight;
	private int currentChoose = 0;
    private int lastX;
	private int NUMOFTHREADS = 8;
	private Bitmap initbit = null;
//	private String uniqueurl;
	private String imageurl,largepicurl,largepicid,init_uniqueurl;
	private MyHandler mhandler;
	private ArrayList<TuyaLayer> tuyaLayer_arrs ;
	private GalleryAdapter adapter;
	private AdjustSizeofInitpicWindow adjustWindow;
	private BroadcastReceiver receiveBroadCast;
	private Bitmap bit;
	private Bitmap initbitmap;
	private boolean door = false;
	private boolean closed = false;
	private int state ;
	
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.concret_pic);
		   
		    receiveBroadCast = new ReceiveBroadCast();
		 	IntentFilter filter = new IntentFilter();
		 	filter.addAction(Tools.concretadd_flag);
		 	registerReceiver(receiveBroadCast, filter);
		 	
		    imageurl = getIntent().getExtras().getString("imageurl");
		    largepicurl = getIntent().getExtras().getString("largepicurl");
		    largepicid = getIntent().getExtras().getString("largepicid");
		    init_uniqueurl = getIntent().getExtras().getString("uniqueurl");
		 	
		    Log.i("ConcretPicDiaplay",largepicurl);
		    DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenHeight = dm.heightPixels;
			screenWidth = dm.widthPixels;
			 
			displayGallery = (Gallery)findViewById(R.id.gallery);
		    currentimg = (RecyclingImageView)findViewById(R.id.initalImage);
		   
		    mhandler = new MyHandler();
		    tuya = new Tuya();
		    
		    currentimg.setOnTouchListener(moveOnTouch);
		    displayGallery.setOnItemClickListener(onItemClick);
		    Log.i("ConcretPicDisplay","onCreate");
	 }
	 
	 private void displayCurrentImg(int position)
	 {
		 
		 RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(currentimg, tuya.getTotalurlList().get(position-1), Init_choose.fixedthreadpool, 
		    		screenWidth, screenHeight, true, initbitmap);
		    if(value == null)
		    {
	//	    	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tuya);
	//	    	currentimg.setImageDrawable(new RecyclingBitmapDrawable(getResources(), bitmap));
		    }
		    else
		    	currentimg.setImageDrawable(value);
	 }
	 
	 private void initAdatper(RecyclingBitmapDrawable value)
	 {
		 if(value != null && value.getBitmap() != null && !value.getBitmap().isRecycled()) 
		 {
			 bit = Bitmap.createBitmap(value.getBitmap()).copy(Tools.config, true);
			 if(adapter == null)
			 {
			    adapter = new GalleryAdapter(this, tuya.getThumburlList(),screenWidth,screenHeight,Init_choose.fixedthreadpool,Init_choose.asynloader,bit);
				Init_choose.asynloader.setAdapter(adapter);
				initData();
				displayGallery.setAdapter(adapter);
			    displayGallery.setSelection(1);
			 }
			 else
			 {
			    adapter.setInitBit(bit);
			 }			    			    			 	
		 }
			    
	 }
	 private void initCurrentImg()
	 {
		 currentimg.setTag(0);
		 currentimg.setId(0);
		    
		 RecyclingBitmapDrawable mvalue = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(currentimg, tuya.getTotalurlList().get(0), Init_choose.fixedthreadpool, 
		    		screenWidth, (int)(screenHeight),true,null);	    
		   
		 if(mvalue == null || (mvalue != null && (mvalue.getBitmap() == null || mvalue.getBitmap().isRecycled())))
		 {
			 setDefaultImage();
		 }
		 else
		    currentimg.setImageDrawable(mvalue);
	 }
	 @Override
	 public void finish()
	 {
		 if(closed == false)
		 {
			 closed = true;
			 super.finish();
		 }		
	 }
	 private void initData(){

		 tuya.addThumburl(imageurl);
		 tuya.addTotalurl(largepicurl);
		 
		 Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				
				tuyaLayer_arrs = HttpUtil.getTuyaLayerFromURL(HttpUtil.getTuyaLayerURL, largepicid, mhandler);

	            for(int i=0;i<tuyaLayer_arrs.size();i++)
	            {
	            	tuya.addTotalurl(tuyaLayer_arrs.get(i).getTuyaUrl());
	     //       	String thumbUrl = tuyaLayer_arrs.get(i).getTuyaUrl() + Tools.thumbTag;
	            	tuya.addThumburl(tuyaLayer_arrs.get(i).getSmallUrl());
	            	
	            }
	            if(tuyaLayer_arrs.size() != 0)
	            {
	            	Message msg = mhandler.obtainMessage(3);
	            	mhandler.sendMessage(msg);
	            }
			}
		});
	 }
	 
	 private Bitmap getBitmap(int width,int height,int resid){
			
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    Bitmap bit = BitmapFactory.decodeResource(getResources(), resid, options);
		    Log.i("before","before");
		   
		    if(options.outHeight/height>=options.outWidth/width)
		    {
		    	options.inSampleSize = options.outHeight/height;    
		    }
		    else 
		    {
		    	options.inSampleSize = options.outWidth/width;
		//    	options.inSampleSize = 2;
		    }
		    
		    options.inDither = false;
		    options.inPreferredConfig = Config.RGB_565;
		    options.inPurgeable = true;
		    options.inInputShareable = true;
		    options.inJustDecodeBounds = false;
		    
	        bit = BitmapFactory.decodeResource(getResources(), resid,options);
	        
	        Log.i("width2",String.valueOf(bit.getWidth()));
	        Log.i("height2",String.valueOf(bit.getHeight()));
	        
		    return bit;
		}
	 private class MyHandler extends Handler
	 {
		 @Override
		 public void handleMessage(Message msg)
		 {
			 switch(msg.what)
			 {
			 case 0:
				 Toast.makeText(ConcretPicDisplay.this, getString(R.string.failTosuccess), 3000).show();
				 break;
			 case 1:
				 Toast.makeText(ConcretPicDisplay.this, getString(R.string.failToget), 3000).show();
				 break;
			 case 2:
				 Toast.makeText(ConcretPicDisplay.this, getString(R.string.noTuyaLayer), 3000).show();
				 break;
			 case 3:
				 if(adapter != null && state == onResume)
				      adapter.notifyDataSetChanged();
				 break;
			 case 4:
				 RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)(msg.obj);
				 if(value != null && value.getBitmap() != null && !value.getBitmap().isRecycled())
					 initAdatper(value);
				 break;
			 }
		 }
	 }
	 private OnClickListener sure_onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				String pic_url = null;
				if(adjustWindow != null)
				{
					pic_url = adjustWindow.finishAdjustImg();
					adjustWindow.dismiss();
				}
				
				Intent intent = new Intent(ConcretPicDisplay.this,MainActivity.class);
				intent.putExtra("from", 4);
				intent.putExtra("initpicurl", pic_url);
				intent.putExtra("initpic", largepicid);
				Log.i("initpic",init_uniqueurl);
				startActivity(intent);
				/*
				Bitmap bit = Init_choose.asynloader.loadBitmap(myView, initpicurl, Init_choose.fixedthreadpool, screenWidth, viewHeight, true, null);
				if(bit != null && myView != null)
				{
				    myView.setImageBitmap(bit);
				    backBitmap = Bitmap.createBitmap(bit);
				}
				*/
			}
	};
    private OnClickListener cancel_onClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(adjustWindow != null)
				adjustWindow.dismiss();
		}
	};
	 private void showAdjustedPopupWindow()
	 {
		 Handler mhandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					switch(msg.what)
					{
					case 3:
						 RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)(msg.obj);
						 if(value != null)
						 {
							 adjustWindow = new AdjustSizeofInitpicWindow(0,ConcretPicDisplay.this, value,sure_onClickListener,cancel_onClick,screenWidth,Tools.viewHeight,screenHeight);
							 adjustWindow.showAtLocation(ConcretPicDisplay.this.findViewById(R.id.frame), Gravity.CENTER, 0, 0);
						 }		
						 break;
					}
				}
			};
			RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.getAdustedBitmap(0,largepicurl,
					Init_choose.fixedthreadpool, mhandler, screenWidth, screenHeight);
			if(value != null)
			{
				adjustWindow = new AdjustSizeofInitpicWindow(0, ConcretPicDisplay.this, value, sure_onClickListener, cancel_onClick, screenWidth,Tools.viewHeight, screenHeight);
				adjustWindow.showAtLocation(ConcretPicDisplay.this.findViewById(R.id.frame),Gravity.CENTER, 0, 0);
			}
	 }
	 
	 @Override
	 public void onResume() 
	 {
	 	door = false;
	 	
	 	RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.getInitBit(imageurl,mhandler);
	 	initAdatper(value);
	 	initCurrentImg();
	 	
	 	if(adapter != null)
	 		adapter.notifyDataSetChanged();
	 	
	 	super.onResume();
	 	state = onResume;
	 	
	 	Log.i("ConcretPicDisplay","onResume");
	 }
     @Override
     public void onDestroy()
     {
    	if(receiveBroadCast != null)
    	    unregisterReceiver(receiveBroadCast);	
    	 super.onDestroy();
     }
	 @Override
	 public void onPause()
	 {
		super.onPause();
	 
	 	if(bit != null && !bit.isRecycled())
	 	{
	 		bit.recycle();
	 		bit = null;
	 	}
	 	if(initbitmap != null && !initbitmap.isRecycled())
	 	{
	 		initbitmap.recycle();
	 		initbitmap = null;
	 	}
	 	if(currentimg != null)
	 		currentimg.setBackgroundDrawable(null);
	 	
	 	state = onPause;
	 	Log.i("ConcretPicDisplay","onPause");
//	 	door = false;
//	 	initDis = -100;
	 }
	 public class ReceiveBroadCast extends BroadcastReceiver
	 {

	 	@Override
	 	public void onReceive(Context arg0, Intent intent) {
	 		// TODO 自动生成的方法存根
	 		if(intent.getStringExtra("destination").equals("gallery_display"))
	 		{
	 			Log.i("receive","gallery");
	 		    tuya.clear();
	 			initData();
	 	//		adapter.notifyDataSetChanged();
	 		}		
	 	}
	 	
	 }
	 private OnTouchListener moveOnTouch = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO 自动生成的方法存根
			
			switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				lastX = (int)event.getRawX();
				Log.i("ConcretPicdisplay_down",String.valueOf(lastX));
				break;
				
			case MotionEvent.ACTION_MOVE:
				Log.i("ConcretPicdisplay_move",String.valueOf(event.getRawX()));
				int dx=(int)event.getRawX()-lastX;
				
				if(dx < -100)
				{
					startActivity();
				}
				else
				{
					if(dx > 100)
						finish();
				}
				break;
			case MotionEvent.ACTION_UP:
		
				break;
			}
			return true;
		}
	};
	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
				long arg3) {
			// TODO 自动生成的方法存根
			
			if(position == 0)
			{
				showAdjustedPopupWindow();
			}
			else
			{
				if((Integer)(currentimg.getTag()) != position-1)
				{
					currentimg.setId(0);

					if(position == 1)
					{
					    RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(currentimg, tuya.getTotalurlList().get(position-1),
					    		Init_choose.fixedthreadpool, screenWidth,screenHeight, true,null);
					
					    if(value == null)
					    {
					//    	setDefaultImage();
					    }
					    else
					    	currentimg.setImageDrawable(value);
					}
					else
					{
						if(initbitmap == null || initbitmap.isRecycled())
						{
							if(initbit == null || initbit.isRecycled())
							{
								Handler mhandler = new Handler()
								{
									@Override
									public void handleMessage(Message msg)
									{
										switch(msg.what)
										{
										case 4:
											RecyclingBitmapDrawable mvalue = (RecyclingBitmapDrawable)(msg.obj);
											if(mvalue != null && mvalue.getBitmap() != null && !mvalue.getBitmap().isRecycled())
											{
												initbit = mvalue.getBitmap();
												initbitmap = Bitmap.createBitmap(initbit).copy(Tools.config, true);
												displayCurrentImg(position);
											}
											break;
										}
									}
								};
								
								RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.getInitBit(tuya.getTotalurlList().get(0),mhandler);
								if(value != null && value.getBitmap() != null && !value.getBitmap().isRecycled())
								{
									initbit = value.getBitmap();
									initbitmap = Bitmap.createBitmap(initbit).copy(Tools.config, true);
								}   
							}
							else
							    initbitmap = Bitmap.createBitmap(initbit).copy(Tools.config, true);
						}
	
						if(initbitmap != null && !initbitmap.isRecycled())
						{
							Log.i("ConcrePicdisplay","initbitmapnotnull");
						    displayCurrentImg(position);
						}
						else
						{
				//			setDefaultImage();
						}
					}
					currentimg.setTag(position-1);
				}			
			}
		}
	};
	
	private void setDefaultImage()
	{
		RecyclingBitmapDrawable mdrawable = (RecyclingBitmapDrawable)Init_choose.asynloader.getBitmapFromMemCache(Tools.default_pic_key);
    	
    	if(mdrawable != null && mdrawable.getBitmap() != null)
    	{
    		currentimg.setImageDrawable(mdrawable);
    	}
    	else
    	{
    		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tuya);
    		mdrawable = new RecyclingBitmapDrawable(getResources(), bitmap);
    		currentimg.setImageDrawable(mdrawable);
    	}
	}
	private void startActivity()
	{
		if(door == false)
		{
			door = true;
			Log.i("startActivity","ConcretPicDisplay");
			int which = (Integer)currentimg.getTag();
			Intent intent = new Intent(ConcretPicDisplay.this,Comment_view.class);
			Bundle bundle = new Bundle();     
			if(which != 0)
			{			   
			   bundle.putString("uniqueurl",tuyaLayer_arrs.get(which-1).getUniqueUrl());        //uniqueurl，评论系统用来唯一标识评论界面的，由项目后台系统提供
			   bundle.putString("url", tuya.getTotalurlList().get(which));
			   
			}
			else
			{
			   bundle.putString("uniqueurl", init_uniqueurl);
			   bundle.putString("url", tuya.getTotalurlList().get(which));		   
		    }
			bundle.putInt("from", 0);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}

}
