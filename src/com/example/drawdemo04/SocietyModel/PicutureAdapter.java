package com.example.drawdemo04.SocietyModel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class PicutureAdapter extends BaseAdapter{
    

	private LayoutInflater inflater;
    private int which;
	private int width,height;
    private ArrayList<PicInfo> picinfo_arrs;
    private MyHandler handler;
    private Bitmap add_bit;
    private RecyclingBitmapDrawable little_drawable;
    private Context context;
    
	public PicutureAdapter(int which, ArrayList<PicInfo> picinfo_arrs,Context context)
	{
		super();
		this.picinfo_arrs = picinfo_arrs;
		this.which = which;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.width = Tools.itemWidth;
		this.height = Tools.itemHeight;
		
		if(which == 0 || which == 2)
		{
			composeAddDrawable();
		}	
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if(which == 0 || which == 2)
		{
			if(null != picinfo_arrs)
				return picinfo_arrs.size() + 1;
			else
				return 1;
		}
		else
		{
			if(null != picinfo_arrs)
				return picinfo_arrs.size();
			else
				return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
//		return picinfo_arrs.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}
   
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		// TODO 自动生成的方法存根

		ViewHolder viewholder;
		if(convertview == null){
			convertview = inflater.inflate(R.layout.picture_item, null);
			viewholder = new ViewHolder();

			viewholder.littleimg = (RecyclingImageView)convertview.findViewById(R.id.littleimg);
			
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewholder.littleimg.getLayoutParams();
	    	params.width = width;
			params.height = height;
			viewholder.littleimg.setLayoutParams(params);
			
			convertview.setTag(viewholder);
		}
		else
			viewholder = (ViewHolder)convertview.getTag();
		
		MyHandler handler = new MyHandler(viewholder.littleimg);
//		viewholder.littleimg.setImageDrawable(null);
//	    viewholder.littleimg.setImageBitmap(null);
	    
		RecyclingBitmapDrawable value = null;
		                                 //which = 0 社区画廊有加号  ; which = 1 社区画廊无加号; which = 2 本地画廊
		if(position == 0)
		{
			if(which == 0 || which == 2)
			{
				if(little_drawable != null && little_drawable.getBitmap() != null && !little_drawable.getBitmap().isRecycled())
				     viewholder.littleimg.setImageDrawable(little_drawable);
				else
				{
					composeAddDrawable();
					viewholder.littleimg.setImageDrawable(little_drawable);
				}
	//			Log.i("position","0");
			}
			else
			{
				value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(viewholder.littleimg, picinfo_arrs.get(0).getImageUrl(), 
						Init_choose.fixedthreadpool,width,height,false,null);
				if(value == null)
				{
					Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
					viewholder.littleimg.setImageDrawable(new RecyclingBitmapDrawable(context.getResources(),bitmap));
				}
				else
					viewholder.littleimg.setImageDrawable(value); 
			}
			
		}
		else
		{
			convertview.setId(position);
			if(which == 0 || which == 1)
			 {
				 if(which == 0)
					 value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(viewholder.littleimg, picinfo_arrs.get(position-1).getImageUrl(), 
							 Init_choose.fixedthreadpool,width,height,false,null);
				 else
					 value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(viewholder.littleimg, picinfo_arrs.get(position).getImageUrl(),
							 Init_choose.fixedthreadpool,width,height,false,null);
			 } 	
		     else
		        value = (RecyclingBitmapDrawable) Init_choose.asynloader.getAdustedBitmap(2, picinfo_arrs.get(position-1).getLittlePath(),
		        		Init_choose.fixedthreadpool, handler, width, height);
			if(value == null)
			{
				setDefaultImage(viewholder.littleimg);
			}
			else
				viewholder.littleimg.setImageDrawable(value);
		}
				
		return convertview;
	}
	private void composeAddDrawable()
	{
		add_bit = Tools.decodeAdjustedBitmapFromSrc(context, R.drawable.passagelistbg2, width, height);
		add_bit = Tools.zoomBitmap(add_bit, width, height);
		Canvas canvas = new Canvas(add_bit);

		Bitmap little = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_normal);
		Bitmap little_add = Tools.zoomBitmap(little, (int)(width*0.3), (int)(width*0.3));
//		little.recycle();
		canvas.drawBitmap(little_add, width*0.3f, height*0.35f, null);
		little_drawable = new RecyclingBitmapDrawable(context.getResources(), add_bit);
		little_add.recycle();
	}
	private void setDefaultImage(RecyclingImageView view)
	{
		
		RecyclingBitmapDrawable drawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMemCache(Tools.default_pic_key);
		if(drawable != null && drawable.getBitmap() != null && !drawable.getBitmap().isRecycled())
			view.setImageDrawable(drawable);
		else
		{
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tuya);
			drawable = new RecyclingBitmapDrawable(context.getResources(), bitmap);
			Init_choose.asynloader.addBitmapToMemoryCache(Tools.default_pic_key, drawable);
			view.setImageDrawable(drawable);
		}
		/*
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tuya);
		RecyclingBitmapDrawable drawable = new RecyclingBitmapDrawable(context.getResources(), bitmap);
		Init_choose.asynloader.addBitmapToMemoryCache(Tools.default_pic_key, drawable);
		view.setImageDrawable(drawable);
		*/
	}
	
	class MyHandler extends Handler
	{
		private RecyclingImageView imageView;
		public MyHandler(RecyclingImageView imageView)
		{
			super();
			this.imageView = imageView;
		}
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				imageView.setImageDrawable((RecyclingBitmapDrawable)(msg.obj));
				imageView.setId(1);
				notifyDataSetChanged();
			}
		}
	}

}
