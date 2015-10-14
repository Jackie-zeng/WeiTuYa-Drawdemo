package com.example.drawdemo04.initModel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.AsyncBitmapLoader;
import com.example.drawdemo04.SocietyModel.HttpUtil;
import com.example.drawdemo04.SocietyModel.Society_gallery;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.SelectDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode.VmPolicy;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class Init_choose extends Activity{

	private RelativeLayout initLayout;
	private RelativeLayout.LayoutParams buttParams;
	private int screenHeight;
	private int screenWidth;
	private Button newCanvas;
	private Button localGallery;
	private Button societyGallery;
	private Button MyGallery; 
	private Button upload;
	public static AsyncBitmapLoader asynloader;
	public static ExecutorService fixedthreadpool;
	private int NUMOFTHREADS = 10;
	private final static int CWJ_HEAP_SIZE = 6* 1024* 1024;
	private CharSequence[] mEntries = {"zhangzekai","dongruyue","chenzejia"};
	private int mClickedDialogEntryIndex = 0;
	private final int LayoutId = 0;
	
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private int progressUpdate = 0;
	private boolean isReScan = false;
	private SelectDialog progressDialog;
	private ProgressBar progressBar;
	
	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		    setContentView(R.layout.init);
		    
		    SharedPreferences mySharePreference = getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		    if(mySharePreference.getInt("num", 0) == 0)
		    {
		    	SharedPreferences.Editor editor = mySharePreference.edit();
		    	editor.putInt("num", 1);
		    	editor.putString("Identity", "cookie_1");
		    	editor.putString("cookie_1","TUYA_ID=" + Tools.DEVICE_ID);
		    	editor.putString(Tools.AccountName + "1", "localUser");
		    }
		    
		    Tools.identity = mySharePreference.getString("Identity", "cookie_1");
		    mClickedDialogEntryIndex = Integer.parseInt(Tools.identity.substring(Tools.identity.indexOf("_") + 1)) - 1;
		    
		    Log.i("directory",Environment.getExternalStorageDirectory().getName());
		    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		    Tools.DEVICE_ID = tm.getDeviceId();
            
		    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.renma);
		    
		    final float scale = getResources().getDisplayMetrics().density;
		    Tools.scale = scale;
		    MainActivity.eraser_thickness = (int)(scale*10);
		    MainActivity.thickness = (int)(scale*5);
		    MainActivity.textSize = (int)(scale*37);
		    
		    int memory = (int)(Runtime.getRuntime().maxMemory()/1024);
		    Log.i("InitChoose",String.valueOf(memory));
		    bitmap = Tools.zoomBitmap(bitmap, Tools.Dp2Px(70, scale), Tools.Dp2Px(70, scale));
		    Tools.init_titlebit = bitmap;
		    
			asynloader = new AsyncBitmapLoader(getApplicationContext());
			fixedthreadpool = Executors.newFixedThreadPool(NUMOFTHREADS);
			
		    DisplayMetrics dm=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenWidth=dm.widthPixels;
			screenHeight=dm.heightPixels;
			
			Log.i("screenwidth",String.valueOf(screenWidth));
			Tools.viewHeight = screenHeight-(int)(screenHeight*0.09)-(int)(screenHeight*0.08);
			Tools.screenWidth = screenWidth;
			Tools.screenHeight = screenHeight;
			
			newCanvas = (Button)findViewById(R.id.newcanvas);
			localGallery = (Button)findViewById(R.id.localgallery);
			societyGallery = (Button)findViewById(R.id.societygallery);
			MyGallery = (Button)findViewById(R.id.mygallery);
			upload = (Button)findViewById(R.id.upload);
			
			initLayout = (RelativeLayout)findViewById(R.id.initLayout);
			initLayout.setId(LayoutId);
			
			initView();
		    
			newCanvas.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(Init_choose.this,MainActivity.class);
					intent.putExtra("from", 0);
					startActivity(intent);
					overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft1);
				}
			});
			societyGallery.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(Init_choose.this,Society_gallery.class);
					Bundle bundle = new Bundle();
					bundle.putInt("from", 0);
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft1);
				}
			});
			localGallery.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(Init_choose.this,Society_gallery.class);
					Bundle bundle = new Bundle();
					bundle.putInt("from", 2);
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft1);
				}
			});
			MyGallery.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(Init_choose.this,com.example.drawdemo04.SocietyModel.MyGallery.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft1);
				}
			});
			upload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					/*
					if(Tools.judgeIfExistSD())
					{
						 File file = new File(Tools.currentSaved_path);
				            Tools.DeleteFile(file);
					}
					else
					{
						File file = new File(getFilesDir().getName() + Tools.back_cache);
						Tools.DeleteFile(file);
						file = new File(getFilesDir().getName() + Tools.floor_cache);
						Tools.DeleteFile(file);
					}
					*/
					/*
					Intent intent = new Intent(Init_choose.this, ScanUpload.class);
					startActivity(intent);
					*/
					SingleSelectionDialog dialog = new SingleSelectionDialog.Builder(Init_choose.this).setTitle(getString(R.string.Account)).setSingleChoiceItems(getData(),
							mClickedDialogEntryIndex, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									mClickedDialogEntryIndex = which;
									
									Tools.identity = "cookie_" + String.valueOf(which + 1);
									SharedPreferences.Editor editor = getSharedPreferences("Cookie", Activity.MODE_PRIVATE).edit();
									editor.putString("Identity", Tools.identity);
									editor.commit();
									
									dialog.dismiss();
									getThemePicUrlTask startTask = new getThemePicUrlTask();
									startTask.execute(HttpUtil.getThemePicURL);
									
									getUserNameTask getNameTask = new getUserNameTask();
									getNameTask.execute(HttpUtil.getUserNameURL);
									
									getUserTitleUrl getTitleUrlTask = new getUserTitleUrl();
									getTitleUrlTask.execute(HttpUtil.getTitleImg);
								}
							},new DialogInterface.OnClickListener() {
					
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO 自动生成的方法存根
									mClickedDialogEntryIndex = -1;
									
									Intent intent = new Intent(Init_choose.this, MipcaActivityCapture.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									dialog.dismiss();
									startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
								}
							}).create();

					dialog.show();
				}
			});
	 }
	 
	@Override 
	public void onResume()
	{
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				case 0:
		//			Tools.closeMyProgress();
					RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)msg.obj;
					if(value != null && value.getBitmap() != null)
						initLayout.setBackgroundDrawable(value);
					break;
				}
			}
		};
		String imgURL = Tools.themeSaved_path + Tools.themePic_path + ".png";
		RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.getAdustedBitmap(4, imgURL, Init_choose.fixedthreadpool, handler, 
		    		screenWidth, screenHeight); 
		if(value == null)
		{
			
			RecyclingBitmapDrawable init_value = null;
			if((init_value = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMemCache(Tools.theme_cache_key)) != null)
			{
				initLayout.setBackgroundDrawable(init_value);
			}
			else
			{
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg02);
				init_value = new RecyclingBitmapDrawable(getResources(), bitmap);
				Init_choose.asynloader.addBitmapToMemoryCache(Tools.theme_cache_key, init_value);
				initLayout.setBackgroundDrawable(init_value);
			}
			
			/*
			Bitmap backBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg02);
			RecyclingBitmapDrawable backValue = new RecyclingBitmapDrawable(getResources(), backBitmap);
			
			initLayout.setBackgroundDrawable(background)
			*/
		}
		else
		{
			if(value.getBitmap() == null)
			{
	//			Tools.showMyProgress(this);
			}
			else
			    initLayout.setBackgroundDrawable(value);
		}
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override 
	protected void onStop()
	{
		super.onStop();
		if(initLayout != null)
			initLayout.setBackgroundDrawable(null);
	}
	 public void initView(){
		 initwidget(newCanvas,R.string.newCanvas,(int)(screenHeight*0.1));
		 initwidget(localGallery,R.string.localGallery,(int)(screenHeight*0.25));
		 initwidget(societyGallery,R.string.societyGallery,(int)(screenHeight*0.4));
		 initwidget(MyGallery, R.string.MyGallery, (int)(screenHeight*0.55));
		 initwidget(upload,R.string.upload,(int)(screenHeight*0.7));
	 }
	 
	 private void initwidget(Button button,int stringId,int topmargin)
	 {
		 /*
		 buttParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 buttParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 buttParams.topMargin = topmargin;
		 */
		 buttParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
		 buttParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 buttParams.topMargin = topmargin;
		 
	//	 button.setBackgroundResource(R.drawable.search_bar_edit_normal);
		 button.setText(stringId);
		 button.setWidth((int)(screenWidth*0.75));
		 button.setHeight((int)(screenHeight*0.08));
	//	 button.setAlpha(0.5f);
	//	 initLayout.addView(button, buttParams);
		 button.setLayoutParams(buttParams);
	 }
	 
	 private ArrayList<HashMap<String, Object>> getData() 
	 {
		 SharedPreferences mySharedPreferences = getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		 int num = mySharedPreferences.getInt("num", 1);
		 ArrayList<HashMap<String, Object>> mHashMaps = new ArrayList<HashMap<String,Object>>();
		 
		 for(int i=0;i<num;i++)
		 {
			 HashMap<String, Object> map = new HashMap<String, Object>();
			 map.put("image",String.valueOf(i+1));
			 map.put("title", mySharedPreferences.getString(Tools.AccountName + String.valueOf(i+1), "匿名"));
			 if(i == 0)
				 map.put("info", getString(R.string.local_user));
			 else
				 map.put("info", getString(R.string.remote_user));
			 
			 mHashMaps.add(map);
		 }
		 return mHashMaps;
    }
	 
	 private class getThemePicUrlTask extends AsyncTask<String, Integer, String>
	 {
		@Override
		protected void onPreExecute()
		{
			if(progressDialog == null)
			{
				progressDialog = Tools.showProgress(Init_choose.this);
			    progressBar = (ProgressBar)progressDialog.findViewById(R.id.progress);
			}
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String imgUrl = params[0];
			return HttpUtil.getThemePic(imgUrl);
		}
		@Override
		protected void onPostExecute(String result)
		{
			if(result != null && !result.equals(""))
			{
				getThemePic_Task mtask = new getThemePic_Task();
				mtask.execute(result);
			}
			else
			{
				clearProgress();
				showAlertDialog();
			}
		} 
		@Override  
		protected void onProgressUpdate(Integer... values) 
		{  
		    int value = values[0];  
		    showProgressUpdate(value); 
		}  
	 }
	 
	 private class getThemePic_Task extends AsyncTask<String, Integer, RecyclingBitmapDrawable>
	 {
		 
		@Override
		protected RecyclingBitmapDrawable doInBackground(String... params) {

			String imgUrl = params[0];
			InputStream in = HttpUtil.getStreamFromURL(imgUrl);
			if(in != null)
			{
				Bitmap backBit = Tools.getBitmap(Tools.screenWidth, Tools.screenHeight, in, false, null, initLayout, LayoutId);
				if(backBit != null)
				{
					Tools.saveImg(backBit, Tools.themeSaved_path, Tools.themePic_path, null);
					RecyclingBitmapDrawable backDrawable = new RecyclingBitmapDrawable(getResources(), backBit);
					String imgURL = Tools.themeSaved_path + Tools.themePic_path + ".png";
					Init_choose.asynloader.addBitmapToMemoryCache(imgURL, backDrawable);
				    return backDrawable;
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(RecyclingBitmapDrawable value)
		{
			if(value != null)
			{
				if(initLayout != null)
					initLayout.setBackgroundDrawable(value);
			}
	//		Tools.closeProgress();
		} 
		@Override  
		protected void onProgressUpdate(Integer... values) 
		{  
		    int value = values[0];  
		    showProgressUpdate(value); 
		}  
	 }
	
	 private class getNewCookieTask extends AsyncTask<String, Integer, Integer>
	 {
		 @Override
		 protected void onPreExecute()
		 {
			if(progressDialog == null)
			{
				progressDialog = Tools.showProgress(Init_choose.this);
				progressBar = (ProgressBar)progressDialog.findViewById(R.id.progress);
			}
			isReScan = true;
		 }
		 
		@Override
		protected Integer doInBackground(String... params) {
			// TODO 自动生成的方法存根
			return HttpUtil.getNewTUYA_ID(params[0], Init_choose.this, mClickedDialogEntryIndex + 1);
		}
		
		@Override
		protected void onPostExecute(Integer result)
		{
			if(result == 1)
			{
		//		Tools.closeProgress();
				if(mClickedDialogEntryIndex == -1)
				{
					SharedPreferences mSharePreference = getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
					mClickedDialogEntryIndex = mSharePreference.getInt("num", 1) - 1;
				}
				getThemePicUrlTask startTask = new getThemePicUrlTask();
				startTask.execute(HttpUtil.getThemePicURL);
				
				getUserNameTask getNameTask = new getUserNameTask();
				getNameTask.execute(HttpUtil.getUserNameURL);
				
				getUserTitleUrl getTitleUrlTask = new getUserTitleUrl();
				getTitleUrlTask.execute(HttpUtil.getTitleImg);
				
			}
			else
			{
				clearProgress();
				Toast.makeText(Init_choose.this, getString(R.string.FailToNotify), 3000).show();
	//			Tools.closeProgress();
			}
		}
		@Override  
		protected void onProgressUpdate(Integer... values) 
		{  
		    int value = values[0];  
		    showProgressUpdate(value); 
		}  
	 }
	 
	 private class getUserNameTask extends AsyncTask<String, Integer, String>
	 {
		 @Override
		 protected void onPreExecute()
		 {
			if(progressDialog == null)
			{
				progressDialog = Tools.showProgress(Init_choose.this);
				progressBar = (ProgressBar)progressDialog.findViewById(R.id.progress);
			}
		 }
		 
		@Override
		protected String doInBackground(String... arg0) {
			// TODO 自动生成的方法存根
			return HttpUtil.getUserName(HttpUtil.getUserNameURL);
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			if(result != null && !result.equals(""))
			{
				String accountId = Tools.identity.substring(Tools.identity.indexOf("_") + 1);
				SharedPreferences.Editor editor = getSharedPreferences("Cookie", Activity.MODE_PRIVATE).edit();
				editor.putString(Tools.AccountName + accountId, result);
				editor.commit();
			}
		}
		@Override  
		protected void onProgressUpdate(Integer... values) {  
		        int value = values[0];  
		        showProgressUpdate(value);
		    }  
	 }
	 
	 private class getUserTitleUrl extends AsyncTask<String, Integer, String>
	 {
		 @Override
		 protected void onPreExecute()
		 {
			if(progressDialog == null)
			{
				progressDialog = Tools.showProgress(Init_choose.this);
				progressBar = (ProgressBar)progressDialog.findViewById(R.id.progress);
			}
		 }
		 
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			return HttpUtil.getTitleURL(Init_choose.this,HttpUtil.getTitleImg,null,0);
			
		}
		@Override
		protected void onPostExecute(String result)
		{
			if(result != null && !result.equals(""))
			{
				getUserTitleImg mtask = new getUserTitleImg();
				mtask.execute(result);
			}
			else
				Toast.makeText(Init_choose.this, getString(R.string.FailToGetTitle), 3000).show();
		}
		@Override  
		protected void onProgressUpdate(Integer... values) 
		{  
		    int value = values[0];  
		    showProgressUpdate(value); 
		}  
	 }
	 
	 private class getUserTitleImg extends AsyncTask<String, Integer, RecyclingBitmapDrawable>
	 {
       
		@Override
		protected RecyclingBitmapDrawable doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String imgUrl = params[0];
			InputStream in = HttpUtil.getStreamFromURL(imgUrl);
			
			if(in != null)
			{
				final float scale = getResources().getDisplayMetrics().density;
				int length = Tools.Dp2Px(70, scale);
				Bitmap bit = Tools.getBitmap(length, length,in, false, null,null,0);
				
				if(bit != null)
				{
					RecyclingBitmapDrawable bit_value = new RecyclingBitmapDrawable(getResources(), bit);
					Init_choose.asynloader.addBitmapToMemoryCache(Tools.titleSaved_path + Tools.title_name + 
							Tools.identity.substring(Tools.identity.indexOf("_") + 1) + ".png", bit_value);
					
					Tools.saveTitleImg(bit,null);          //通过网络获取头像后加入缓存和本地文件夹
				    return bit_value;
				}
			}
			return null;
		}
		@Override  
		protected void onProgressUpdate(Integer... values) 
		{  
		    int value = values[0];  
		    showProgressUpdate(value); 
		}  
		 
	 }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	        super.onActivityResult(requestCode, resultCode, data);
	        switch (requestCode)
	        {
			case SCANNIN_GREQUEST_CODE:
				if(resultCode == RESULT_OK)
				{
					Bundle bundle = data.getExtras();
					final String urlFromQRCode = bundle.getString("result");
		
					getNewCookieTask getCookieTask = new getNewCookieTask();
					getCookieTask.execute(urlFromQRCode);
					
				}
				break;
			}
	    }	
	 
	 private synchronized void showProgressUpdate(int progress)
	 {
		 progressUpdate += progress;
		 if(progressBar == null)
		 {
			 if(progressDialog != null)
				 progressBar = (ProgressBar)progressDialog.findViewById(R.id.progress);
		 }
		 if(!isReScan)
			 progressBar.setProgress(progressUpdate/5);
		 else
			 progressBar.setProgress(progressUpdate/6);
		
		 if(!isReScan && progressUpdate >= 500)
		 {
			 clearProgress();
		 }
		 if(isReScan && progressUpdate >= 600)
		 {
			 clearProgress();
		 }
			 
	 }
	 
	 private void showAlertDialog()
	 {
		 new AlertDialog.Builder(Init_choose.this).setTitle(getString(R.string.application_title))
			.setMessage(getString(R.string.NeedToReUpload)).setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO 自动生成的方法存根
					Intent intent = new Intent(Init_choose.this, MipcaActivityCapture.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					arg0.dismiss();
					startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
				}
			})
			.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO 自动生成的方法存根
					arg0.dismiss();
				}
			}).show();
	 }
	 
	 private void clearProgress()
	 {
		 isReScan = false;
		 Tools.closeProgress(progressDialog);
		 progressDialog = null;
		 progressBar = null;
		 progressUpdate = 0;
	 }
	 
	 
	  class TestThread extends Thread
	  {
		  @Override
		  public void run()
		  {
		   try {
		    Thread.currentThread().sleep(3000);
		   } catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		   }
		   Log.i("tag", "onPause");
		   
		   finish();
		  }
	  }
}
