package com.example.drawdemo04.SocietyModel;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.SelectPicPopupWindow;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.SelectDialog;
import com.example.drawdemo04.initModel.Init_choose;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

///from == 0 来自 init-SocietyGallery from == 1 来自Mainactivity from == 2 来自init-localGallery
public class Society_gallery extends Activity implements OnScrollListener{
	
	private final int OnResume = 1;
	private final int onPause = 0;
	private GridView gridview;
	private TextView title_text;
	private ProgressBar progressDisplay;
	private RelativeLayout gridrelative;
	private RelativeLayout gridtitle_layout;
//	private ArrayList<String> agreeinfo;
//	private ArrayList<String> commentinfo;
	private ArrayList<String> imgUrls;
	private ArrayList<PicInfo> picinfo_arrs = new ArrayList<PicInfo>();
	private ArrayList<PicInfo> recoversaved_arrs = new ArrayList<PicInfo>();
	private PicutureAdapter adapter;
    private int screenHeight,screenWidth,viewHeight,viewWidth;
    private MyHandler mhandler;
    private int lastVisibleIndex;
//    private int Totaldata = 9999999;
    private int currentPosition = 0;
    private int lastPosition = 0;
    private int currentPage = 1;
    private boolean isCompleted = false;
    private int loadingpage = 0;
    private AdjustSizeofInitpicWindow adjustWindow;
    private DeleteLocalpicWindow deleteWindow;
    private int choosed = 0;
    private int from = 0;
    public static int longChoosed = 0;
    private BroadcastReceiver receiveBroadCast ;
    private int state;
    private SelectDialog progressDialog;
    private boolean door = true;
    
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.society_gallery);
  
	    receiveBroadCast = new ReceiveBroadCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Tools.localadd_flag);
		registerReceiver(receiveBroadCast, filter);
		
	    from = getIntent().getExtras().getInt("from");	    
	    if(from == 1)
	    {
	    	viewHeight = getIntent().getExtras().getInt("viewHeight");
	    	viewWidth = getIntent().getExtras().getInt("viewWidth");
	    }

	    imgUrls = new ArrayList<String>();
	    
        DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight=dm.heightPixels;
		screenWidth = dm.widthPixels;
	
		int px = Tools.Dp2Px(50, Tools.scale);
		int dis = 2;
	    Tools.itemWidth = (int)((screenWidth - 4*dis)/3.0);
		Tools.itemHeight = (int)((screenHeight - px - 4*dis)/3.0);

		gridrelative = (RelativeLayout)findViewById(R.id.backgroundLayout);
		
		title_text = (TextView)findViewById(R.id.Gallery);
		
		if(from == 2)
			title_text.setText(getString(R.string.localGallery));
		if(from == 0 || from == 1)
			title_text.setText(getString(R.string.societyGallery));
		
		gridview = (GridView)findViewById(R.id.gridview);
		gridview.setColumnWidth((int)((screenWidth - 4*dis)/3.0));
	    
		progressDisplay = (ProgressBar)findViewById(R.id.progressDisplay);
				
		mhandler = new MyHandler();
		
		initDatas(from,null);
		
		if(from == 2)
		{
			adapter = new PicutureAdapter(from,picinfo_arrs,this);
			gridview.setAdapter(adapter);
		}
		else
		{
			adapter = new PicutureAdapter(from,picinfo_arrs,this);
			gridview.setAdapter(adapter);
			
			gridview.setOnScrollListener(this);
	//		Log.i("error","bug");
		}
		if(from == 2)
		{
			gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO 自动生成的方法存根
					if(arg2 > 0)
					{
						if(deleteWindow == null)
						{
							
							Vibrator mVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE); 
					//		mVibrator.vibrate( new long[]{100,10,100,1000},-1);
							mVibrator.vibrate(100);
							
							displayDeleteView(arg2-1);
							
							showDeleteWindow();
						}
						longChoosed = arg2 - 1;
					}		
					return true;
				}
				
			});
		}

		RelativeLayout layout = (RelativeLayout)findViewById(R.id.backgroundLayout);
		layout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO 自动生成的方法存根
	//			Log.i("layout","longclick");
				if(deleteWindow == null)
				{
					showDeleteWindow();
				}
				return false;
			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
					if(from == 0)   //跳转到评论界面
					{
						if(arg2 > 0)
						{
							Intent intent = new Intent(Society_gallery.this, ConcretPicDisplay.class);
							Bundle bundle = new Bundle();
						//	bundle.putString("initpic", String.valueOf(arg2));
							arg2 = arg2 - 1;
							if(picinfo_arrs != null && picinfo_arrs.size() > arg2)
							{
								bundle.putString("imageurl", picinfo_arrs.get(arg2).getImageUrl());
								bundle.putString("largepicurl", picinfo_arrs.get(arg2).getLargepicUrl());
								bundle.putString("largepicid", picinfo_arrs.get(arg2).getLargepicId());
								bundle.putString("uniqueurl", picinfo_arrs.get(arg2).getUniqueUrl());
							}
							else
							{
								bundle.putString("imageurl", "");
								bundle.putString("largepicurl", "");
								bundle.putString("largepicid", "");
								bundle.putString("uniqueurl", "");
							}
							intent.putExtras(bundle);
							startActivity(intent);
							System.gc();
						}
						else
						{
							Intent intent = new Intent(Society_gallery.this,MainActivity.class);
							intent.putExtra("from", 2);
							startActivity(intent);
							System.gc();
						}
					}
					else
					{
						/*
						Intent intent = new Intent();				
						intent.putExtra("initpicurl", picinfo_arrs.get(arg2).getLargepicUrl());
				     	intent.putExtra("initpic",picinfo_arrs.get(arg2).getLargepicId());
						setResult(20, intent);			
						finish();
						*/
						if(from == 1)   //跳转到调制大小界面
						{
							if(door == true)
							{
								choosed = arg2;
								arg2 = arg2;
								
								Log.i("Society_gallery_largepic",picinfo_arrs.get(arg2).getLargepicUrl());
								Log.i("Society_gallery_image",picinfo_arrs.get(arg2).getImageUrl());
								
								RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.getAdustedBitmap(0,picinfo_arrs.get(arg2).getLargepicUrl(),
										Init_choose.fixedthreadpool, mhandler, screenWidth, screenHeight-Tools.Dp2Px(50, Tools.scale));
								
								 adjustWindow = new AdjustSizeofInitpicWindow(0,Society_gallery.this, value,sure_onClick,cancel_onClick,viewWidth, viewHeight,screenHeight);
								 adjustWindow.showAtLocation(gridrelative, Gravity.CENTER, 0, 0);
								 door = false;
							}
					//		 System.gc();
						}
						else      //从本地画廊跳转到涂鸦界面
						{
							if(arg2 > 0)
							{
								arg2 = arg2 - 1;
								Intent intent = new Intent(Society_gallery.this,MainActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("surface_path",picinfo_arrs.get(arg2).getSurfacePath());
								bundle.putString("back_path", picinfo_arrs.get(arg2).getBackPath());
								bundle.putString("config_path", picinfo_arrs.get(arg2).getConfigInfo());
								intent.putExtras(bundle);
								intent.putExtra("from", 1);
								Log.i("local","yes");
								startActivity(intent);
							}
							else
							{				
								Intent intent = new Intent(Society_gallery.this,MainActivity.class);
								intent.putExtra("from", 3);
								startActivity(intent);
							}
						}
					}
				}
			
		});   
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	if(from == 2 && deleteWindow == null)
    	{
    		if(picinfo_arrs.size() > 0)
    		{
    			displayDeleteView(picinfo_arrs.size()-1);
    			longChoosed = picinfo_arrs.size() - 1;
    		}
    		showDeleteWindow();
    	}	
		return false;
    }
    @Override
    public void onBackPressed()
    {
    	if(deleteWindow != null)
    	{
    		deleteWindow.dismiss();
    		deleteWindow = null;
    		if(adapter != null)
    			adapter.notifyDataSetChanged();
    	}
    	else
    		super.onBackPressed();
    }
	private void initDatas(int from,String path){

		if(from == 2)
	  {
			showProgressBar();
			
			File cacheDir = new File(Tools.localSave_path);			
			if(cacheDir.exists())
		 {
			File[] cacheFiles = cacheDir.listFiles();	
			if(cacheFiles != null)
			{
				int i = 0;
				for(;i<cacheFiles.length;i++)
				{
					String centerPath = cacheFiles[i].getName() + "/";
					
					if(null != path)
					{
						if(path.equals(Tools.localSave_path + centerPath))
						{
							File[] saved_files = cacheFiles[i].listFiles();
							if(saved_files != null)
							{
								PicInfo picOb = new PicInfo();
								for(int j=0;j<saved_files.length;j++)
								{
									if((Tools.little_name + ".png").equals(saved_files[j].getName()))
										picOb.setLittlePath(Tools.localSave_path + centerPath + saved_files[j].getName());
									
									if((Tools.surfacepic_name + ".png").equals(saved_files[j].getName()))
										picOb.setSurfacePath(Tools.localSave_path + centerPath + saved_files[j].getName());
									
									if((Tools.backpic_name + ".png").equals(saved_files[j].getName()))
										picOb.setBackPath(Tools.localSave_path + centerPath + saved_files[j].getName());
									
									if((Tools.configInfo_name + ".txt").equals(saved_files[j].getName()))
										picOb.setConfigInfo(Tools.localSave_path + centerPath + saved_files[j].getName());
								}
								picinfo_arrs.add(picOb);
								break;
							}
						}
					}
					else
					{
						File[] saved_files = cacheFiles[i].listFiles();
						if(saved_files != null)
						{
							PicInfo picOb = new PicInfo();
							for(int j=0;j<saved_files.length;j++)
							{
								if((Tools.little_name + ".png").equals(saved_files[j].getName()))
									picOb.setLittlePath(Tools.localSave_path + centerPath + saved_files[j].getName());
								
								if((Tools.surfacepic_name + ".png").equals(saved_files[j].getName()))
									picOb.setSurfacePath(Tools.localSave_path + centerPath + saved_files[j].getName());
								
								if((Tools.backpic_name + ".png").equals(saved_files[j].getName()))
									picOb.setBackPath(Tools.localSave_path + centerPath + saved_files[j].getName());
								
								if((Tools.configInfo_name + ".txt").equals(saved_files[j].getName()))
									picOb.setConfigInfo(Tools.localSave_path + centerPath + saved_files[j].getName());
							}
							picinfo_arrs.add(picOb);
						}
					}
					
				}		
			}
		}
			closeProgressBar();
	 }
		else
		{
			if(Tools.isNetConnected(Society_gallery.this))
			{
				Log.i("Society_gallery","Tools.connected");
				if(currentPage > loadingpage)
				{
					showProgressBar();
					
					loadingpage ++;
					Init_choose.fixedthreadpool.execute(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							ArrayList<PicInfo> lists = HttpUtil.getPicInfoFromURL(HttpUtil.getPicURL, Tools.PAGE_SIZE, currentPage,mhandler);
							if(lists != null && lists.size() > 0)
							{
								Message msg = mhandler.obtainMessage(2, lists);
								mhandler.sendMessage(msg);
							}
				            if(lists != null && lists.size() == 0)
				            {
				            	Message msg = mhandler.obtainMessage(6);
				            	mhandler.sendMessage(msg);
				            }
						}
					});
				}
				
			}
			else
			{
				Toast.makeText(Society_gallery.this, getString(R.string.NotInternet),1000).show();
				//从数据库中获取
			}
		}		
	}
	
    private void showDeleteWindow()
    {
    	deleteWindow = new DeleteLocalpicWindow(Society_gallery.this,(int)(screenHeight*0.2),deletepic_onClick, giveup_onClick);
		deleteWindow.setWidth(LayoutParams.FILL_PARENT);
		deleteWindow.setHeight((int)(screenHeight*0.2));
		
		deleteWindow.showAtLocation(Society_gallery.this.findViewById(R.id.backgroundLayout),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		gridview.setEnabled(false);
    }
    private Bitmap getBitmap(int width,int height,int resId){
		
		
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    Bitmap bit = BitmapFactory.decodeResource(getResources(), resId, options);
	    

		
	    if(options.outHeight/height>=options.outWidth/width)
	    {
	    	options.inSampleSize = (int)(options.outHeight/(float)height);
	    }
	    else 
	    {
	    	options.inSampleSize = (int)(options.outWidth/(float)width);
	    }
	    
	    options.inDither = false;
	    options.inPreferredConfig = Config.ARGB_4444;
	    options.inPurgeable = true;
	    options.inInputShareable = true;
	    options.inJustDecodeBounds = false;
	    
        bit = BitmapFactory.decodeResource(getResources(), resId,options);

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
			   Toast.makeText(Society_gallery.this, getString(R.string.failTosuccess), 3000).show();
                break;
		   case 1:
			   Toast.makeText(Society_gallery.this, getString(R.string.failToget), 3000).show();
			   break;
		   case 2:
			   lastPosition = currentPosition;
			   currentPosition += ((ArrayList<PicInfo>)(msg.obj)).size();

			   currentPage++;
		   
			   for(int i=0;i<((ArrayList<PicInfo>)(msg.obj)).size();i++)
			   {
				   picinfo_arrs.add(((ArrayList<PicInfo>)(msg.obj)).get(i));	   
			   }
			   adapter.notifyDataSetChanged();
			  
			   break;
		   case 3:
	 //		   Bitmap bit = (Bitmap)(msg.obj);
			   RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)(msg.obj);
			   if(value != null  && value.getBitmap() != null)
			   {
				   if(adjustWindow == null)
				   {
					   adjustWindow = new AdjustSizeofInitpicWindow(0,Society_gallery.this, value,sure_onClick,cancel_onClick,viewWidth, viewHeight,screenHeight);
					   adjustWindow.showAtLocation(gridrelative, Gravity.CENTER, 0, 0); 
				   }
				   else
				   {
					   adjustWindow.displayAjustView(value);
				   }
			   }		
			   break;
		   case 4:
			   Toast.makeText(Society_gallery.this, getString(R.string.ReadTimeout), 3000).show();
			   break;
		   case 5:
			   Toast.makeText(Society_gallery.this, getString(R.string.ConnectTimeout), 3000).show();
			   break;
		   case 6:
		//	   Toast.makeText(Society_gallery.this, getString(R.string.NoPic), 3000).show();
			   isCompleted = true;
			   break;
		   }
		   closeProgressBar();
		   
	   }
   }
   
	private OnClickListener deletepic_onClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(picinfo_arrs.size() > longChoosed)
			{
	            PicInfo picinfo = picinfo_arrs.get(longChoosed);
	            picinfo.setPosition(longChoosed);
				recoversaved_arrs.add(picinfo);
				
				if(deleteWindow != null)
				{
					deleteWindow.dismiss();
					deleteWindow = null;
				}
				picinfo_arrs.remove(longChoosed);
				
				adapter.notifyDataSetChanged();
			}
		}
	};
	
   private OnClickListener giveup_onClick = new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		if(recoversaved_arrs.size() > 0)
		{
			int index = recoversaved_arrs.size() - 1;
			PicInfo picinfo = recoversaved_arrs.get(index);
			recoversaved_arrs.remove(index);
			picinfo_arrs.add(picinfo.getPosition(),picinfo);
			adapter.notifyDataSetChanged();
		}
		else
		{
			if(deleteWindow != null)
			{
				deleteWindow.dismiss();
				deleteWindow = null;
				adapter.notifyDataSetChanged();
			}
		}	
	}
};
   private OnClickListener cancel_onClick = new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		if(adjustWindow != null)
		{
			adjustWindow.dismiss();
			door = true;
		}
		
	}
};
   private OnClickListener sure_onClick = new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		String initpicUrl = null;
		if(adjustWindow != null)
		{
			initpicUrl = adjustWindow.finishAdjustImg();
			adjustWindow.dismiss();
			door = true;
		}
		if(initpicUrl != null)
		{
			final String initpic_url = initpicUrl;
			
			Tools.showMyProgress(Society_gallery.this);
			Timer timer = new Timer();
	    	timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					startActivity(initpic_url);
				}
			}, 600);
		}
	}
};
@Override
public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	// TODO 自动生成的方法存根
	lastVisibleIndex = firstVisibleItem + visibleItemCount -1;
	
	 int edge =  (int)((currentPosition-lastPosition)*0.75) + lastPosition;

	if(isCompleted == false && lastVisibleIndex >= edge && from != 2)
	{
		initDatas(from,null);
	}
}

@Override
public void onScrollStateChanged(AbsListView arg0, int scrollState) {
	// TODO 自动生成的方法存根
	Tools.scroll_state = scrollState;
	if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && adapter != null)
		adapter.notifyDataSetChanged();
	
	if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == adapter.getCount()-1 && isCompleted == false && from != 2)
	{
		if(progressDisplay != null && progressDisplay.getVisibility() == View.GONE)
			initDatas(from, null);
	}
	if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == adapter.getCount()-1 && isCompleted == true && from != 2)
		Toast.makeText(Society_gallery.this, getString(R.string.loadAll), 800).show();
}

private void showProgressBar()
{
	if(progressDisplay.getVisibility() == View.GONE)
		progressDisplay.setVisibility(View.VISIBLE);
}
private void closeProgressBar()
{
	if(progressDisplay.getVisibility() == View.VISIBLE)
		progressDisplay.setVisibility(View.GONE);
}

@Override
public void finish()
{
	super.finish();
	if(recoversaved_arrs.size() != 0)
	{
		Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				for(int i=0;i<recoversaved_arrs.size();i++)
				{	
					String filepath = recoversaved_arrs.get(i).getBackPath();		
					int pos = filepath.lastIndexOf("/");
					filepath = filepath.substring(0, pos + 1);
					
					File file = new File(filepath);
					if(file.exists())
						Tools.DeleteFile(file);
				}
				recoversaved_arrs.clear();
				Log.i("finsh","delete");
			}
		});
	}
}
@Override
public void onResume() 
{
	super.onResume();
	state = OnResume;
	if(adapter != null)
		adapter.notifyDataSetChanged();
}

@Override
public void onPause()
{
	super.onPause();
    state = onPause;
}
@Override
public void onDestroy()
{
	super.onDestroy();
	if(receiveBroadCast != null)
	    unregisterReceiver(receiveBroadCast);
}

public ArrayList<PicInfo> getPicInfoList()
{
	return picinfo_arrs;
}

public GridView getGridView()
{
	return gridview;
}

public void displayDeleteView(int index)
{
	RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMemCache(picinfo_arrs.get(index).getLittlePath());
	if(value != null && !value.getBitmap().isRecycled())
	{
		 Bitmap bit = Bitmap.createBitmap(value.getBitmap());
		 Canvas canvas = new Canvas(bit);
		 Bitmap delete_bit = BitmapFactory.decodeResource(getResources(), R.drawable.delete_pressed);
		 canvas.drawBitmap(delete_bit, 20, 5, null);
		 
		 RecyclingBitmapDrawable delete_value = new RecyclingBitmapDrawable(getResources(), bit);
		 Init_choose.asynloader.removeBitmapFromCache(picinfo_arrs.get(index).getLittlePath());
		 Init_choose.asynloader.addBitmapToMemoryCache(picinfo_arrs.get(index).getLittlePath(), delete_value);
		 adapter.notifyDataSetChanged();
	}
}
public class ReceiveBroadCast extends BroadcastReceiver
{

	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO 自动生成的方法存根
		Log.i("broadcast","from");
		if(intent.getStringExtra("destination").equals("local_gallery"))
		{
			from = 2;
			String path = intent.getStringExtra("path");
			initDatas(from,path);
			
			if(state == OnResume)
			   adapter.notifyDataSetChanged();
		}
		if(intent.getStringExtra("destination").equals("society_gallery"))
		{
			from = 0;
			initDatas(from,null);
            adapter.notifyDataSetChanged();
		}
		
	}
	
}
private void startActivity(String initpicUrl)
{
	Tools.closeMyProgress();
	
	Intent intent = new Intent();
	
	Bundle bundle = new Bundle();
	bundle.putString("initpicUrl",initpicUrl);
	bundle.putString("initpic", picinfo_arrs.get(choosed).getLargepicId());
    intent.putExtras(bundle);
    
	setResult(20, intent);	
	System.gc();
	finish();
}
    
}
