package com.example.drawdemo04;

import com.example.drawdemo04.PenGraphycal.UnDoDraw;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyHandler extends Handler{

	private MainActivity activity;
	private int which,from;
    private Bitmap backBitmap,floorBitmap;
    
	public MyHandler(MainActivity activity,int which,int from){
		this.activity = activity;
		this.which = which;
		this.from = from;
	}
	
	@Override
	public void handleMessage(Message msg){
		
		switch(msg.what)
		{
		case 0:
			
			if(which ==activity.BACKBITMAP)                      //从文件夹中获取选定的作品的背景图
			{
				RecyclingBitmapDrawable backDrawable = (RecyclingBitmapDrawable)(msg.obj);
				if(backDrawable != null)
				{
					Bitmap bit = backDrawable.getBitmap();
					if(bit != null)
						backBitmap = Bitmap.createBitmap(bit);
				}
			}
			else  
			{
				RecyclingBitmapDrawable floorDrawable = (RecyclingBitmapDrawable)(msg.obj);   //从文件夹中获取选定的作品的涂鸦层
				if(floorDrawable != null)
				{
					Bitmap bit = floorDrawable.getBitmap();
					if(bit != null)
						floorBitmap = Bitmap.createBitmap(bit);
				}
			}	
			activity.showMyView(-2, backBitmap, floorBitmap);
			
			break;
		case 1:
			String configInfo = msg.getData().getString("configInfo");   //从文件中获取配置文件
			
			if(configInfo != null)
			{
				Log.i("configInfo",configInfo);
				
				String[] infos = configInfo.split("/");
				Tools.scaleWidth = Integer.parseInt(infos[0]);                        //设置好配置参数，用于上传操作
				Tools.scaleHeight = Integer.parseInt(infos[1]);
				Log.i(String.valueOf(Tools.screenWidth),String.valueOf(Tools.screenHeight));
				
				Tools.initWidth = Integer.parseInt(infos[2]);
				Tools.initHeight = Integer.parseInt(infos[3]);
				Log.i(String.valueOf(Tools.initWidth),String.valueOf(Tools.initHeight));
				
				Tools.scaleX = Integer.parseInt(infos[4]);
				Tools.scaleY = Integer.parseInt(infos[5]);
				Log.i(String.valueOf(Tools.scaleX),String.valueOf(Tools.scaleY));
				
				activity.setInitPic(infos[6]);
				activity.setImageFrom(Tools.SOCIETY);
			}
			break;
		case 2:
			Toast.makeText(activity, activity.getString(R.string.failTosave), 3000).show();
			break;
		case 3:
            activity.ProgressDismiss();
			Toast.makeText(activity, activity.getString(R.string.haveCleared),3000).show();
			break;
		case 4:
			Toast.makeText(activity,activity.getString(R.string.successTosave) ,3000).show();
			if(from == 3 || from == 1)
			{
				String strPath = (String) msg.obj;
				activity.sendBroadcast(Tools.localadd_flag,"local_gallery",strPath);
			}
			activity.saveRecord = UnDoDraw.currentsaved.size();
		    MainActivity.hasDrawn_save = false;
		    Tools.closeMyProgress();
			break;
		case 5:
			Toast.makeText(activity, activity.getString(R.string.failTosave), 3000).show();
			break;
		}
	}

}
