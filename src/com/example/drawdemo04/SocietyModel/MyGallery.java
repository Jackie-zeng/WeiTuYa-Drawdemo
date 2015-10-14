package com.example.drawdemo04.SocietyModel;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.initModel.GenerateCode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class MyGallery extends Activity implements OnScrollListener{

	 private int pageSize = 9;
	 private int currentPage = 1,loadingPage = 0;
	 private int lastVisibleIndex = 0;
	 private int lastPosition = 0,currentPosition = 0;
	 private int screenWidth,screenHeight;
	 private ArrayList<MyPicInfo> myPicInfos = new ArrayList<MyPicInfo>();
	 private TextView titleText;
	 private GridView gridview;
	 private ProgressBar progressDisplay;
	 private MyGalleryAdapter myGallery_adapter;
	 private boolean isCompleted = false;
	 
	 @Override
		public void onCreate(Bundle savedInstanceState) {
		 
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.society_gallery);
		 
		 titleText = (TextView)findViewById(R.id.Gallery);
		 titleText.setText(getString(R.string.MyGallery));
		 progressDisplay = (ProgressBar)findViewById(R.id.progressDisplay);
		 
		 screenWidth = Tools.screenWidth;
		 screenHeight = Tools.screenHeight;
		
		 int px = Tools.Dp2Px(53, Tools.scale);
		 int dis = 2;
		 Tools.itemWidth = (int)((screenWidth - 4*dis)/3.0);
	     Tools.itemHeight = (int)((screenHeight - px - 4*dis)/3.0);
	     
	     gridview = (GridView)findViewById(R.id.gridview);
		 gridview.setColumnWidth((int)((screenWidth - 4*dis)/3.0));
		 
		 initData();
		 
		 myGallery_adapter = new MyGalleryAdapter(myPicInfos, this);
		 gridview.setAdapter(myGallery_adapter);
		 gridview.setOnScrollListener(this);
		 gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MyGallery.this, Comment_view.class);
				Bundle bundle = new Bundle();
				bundle.putString("url_origin", myPicInfos.get(arg2).getUrlOrigin());
				bundle.putString("image_url_origin", myPicInfos.get(arg2).getImageUrlOrigin());
				bundle.putString("uniqueurl", myPicInfos.get(arg2).getUniqueUrl());
				bundle.putInt("from", 1);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	 }
	
    
	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根
		lastVisibleIndex = firstVisibleItem + visibleItemCount -1;
		
		 int edge =  (int)((currentPosition-lastPosition)*0.75) + lastPosition;

		if(lastVisibleIndex >= edge)
		{
			initData();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO 自动生成的方法存根
		
		Tools.scroll_state = scrollState;
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && myGallery_adapter != null)
			myGallery_adapter.notifyDataSetChanged();
		
		Log.i("Mygallery_lastvisibleIndex",String.valueOf(lastVisibleIndex));
		Log.i("Mygallery_getCount",String.valueOf(myGallery_adapter.getCount()-1));
		
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == myGallery_adapter.getCount()-1 && isCompleted == false)
		{
//			showPogress();  判断菊花是否还在转？不在转则尝试最后加载一次  list.size() == 0 iscompleted = true
			if(progressDisplay != null && progressDisplay.getVisibility() == View.GONE)
				initData();
		}
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == myGallery_adapter.getCount()-1 && isCompleted == true)
			Toast.makeText(MyGallery.this, getString(R.string.loadAll), 800).show();
	}
	private void initData()
	{
		if(Tools.isNetConnected(MyGallery.this))
		{
			Log.i("MyGallery_currentPage",String.valueOf(currentPage));
			Log.i("MyGallery_loadingpage",String.valueOf(loadingPage));
			
			if(currentPage > loadingPage)
			{
				Log.i("MyGallery_initData","loading");
				loadingPage ++;
				GetMyPicInfoTask getMyPicTask = new GetMyPicInfoTask(pageSize, currentPage);
				getMyPicTask.execute(HttpUtil.getMyPicInfoURL);
			}
		}
		else
			Toast.makeText(MyGallery.this, getString(R.string.NotInternet), 1000).show();
	}
	
	private class GetMyPicInfoTask extends AsyncTask<String, Integer, GetfromNetWorkOb>
	{
		private int pageSize,current_Page;
		
		public GetMyPicInfoTask(int pageSize,int currentPage)
		{
			this.pageSize = pageSize;
			this.current_Page = currentPage;
		}
		
		@Override
		protected GetfromNetWorkOb doInBackground(String... params) {
			// TODO 自动生成的方法存根
			int times = 0;
			return HttpUtil.getMyPicInfo(MyGallery.this, params[0], pageSize, current_Page, times);
		}
		
		@Override
		protected void onPostExecute(GetfromNetWorkOb gob)
		{
			dismissProgress();
			Log.i("MyGallery","onPostExecute");
			if(gob != null)
			{
				String toastText = gob.getToastText();
				if(toastText != null && !toastText.equals(""))
					Toast.makeText(MyGallery.this, toastText, 3000).show();
				
				ArrayList<MyPicInfo> lists = (ArrayList<MyPicInfo>) gob.getObj();
				if(lists != null && lists.size() != 0)
				{
					lastPosition = currentPosition;
					currentPosition += lists.size();
					currentPage ++;
					
					for(int i=0;i<lists.size();i++)
					{
						myPicInfos.add(lists.get(i));
					}
					myGallery_adapter.notifyDataSetChanged();
				}
				else
				{
					if(lists != null && lists.size() == 0)
		            {
						Log.i("MyGallery","listSize==0");
		            	isCompleted = true;
		            }
				}	
			}
		}
		@Override
		protected void onPreExecute()
		{
			showPogress();
		}
	}
    private void showPogress()
    {
    	Log.i("MyGallery_showProgress","showProgress");
    	if(progressDisplay.getVisibility() == View.GONE)
			progressDisplay.setVisibility(View.VISIBLE);
    }
    private void dismissProgress()
    {
 	   if(progressDisplay.getVisibility() == View.VISIBLE)
		   progressDisplay.setVisibility(View.GONE);
    }
    
    @Override
	 public void onBackPressed()           //退出涂鸦界面 MainActivity
	{	
    	super.onBackPressed();
	}
    
    public class MyProgressBar extends ProgressBar
    {
        public  AsyncTask<String, Integer, GetfromNetWorkOb> task;
        
		public MyProgressBar(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO 自动生成的构造函数存根
		}
    }
}
