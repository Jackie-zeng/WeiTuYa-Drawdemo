package com.example.drawdemo04.SocietyModel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.xml.sax.InputSource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;

import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.PicutureAdapter.MyHandler;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

public class MyGalleryAdapter extends BaseAdapter{

	private final int BACK = 0;
	private final int SURFACE = 1;
	private LayoutInflater inflater;
	private int width,height;
    private ArrayList<MyPicInfo> picinfo_arrs;
    private Context context;
    
	public MyGalleryAdapter(ArrayList<MyPicInfo> picinfo_arrs,Context context)
	{
		super();
		this.picinfo_arrs = picinfo_arrs;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.width = Tools.itemWidth;
		this.height = Tools.itemHeight;
			
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return picinfo_arrs.size();
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
		if(convertview == null)
		{
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
		
		viewholder.littleimg.setId(position);
		
		MyHandler handler = new MyHandler(viewholder.littleimg,position);

		RecyclingBitmapDrawable value = getBitmapDrawable(picinfo_arrs.get(position).getImageUrlSmall(),
				picinfo_arrs.get(position).getUrlSmall(), viewholder.littleimg, position, handler);
		                                 //which = 0 社区画廊有加号  ; which = 1 社区画廊无加号; which = 2 本地画廊
	    if(value == null)
	    {
	    	Bitmap bit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tuya);
	    	RecyclingBitmapDrawable displayValue = new RecyclingBitmapDrawable(context.getResources(), bit);
	    	viewholder.littleimg.setImageDrawable(displayValue);
	    }
	    else
	    	viewholder.littleimg.setImageDrawable(value);
	    
		return convertview;
	}

	class ViewHolder{
		private RecyclingImageView littleimg;
	}
	
	class MyHandler extends Handler
	{
		private RecyclingImageView imageView;
		private int index;
		public MyHandler(RecyclingImageView imageView,int index)
		{
			super();
			this.index = index;
			this.imageView = imageView;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				imageView.setImageDrawable((RecyclingBitmapDrawable)(msg.obj));
		//		imageView.setId(1);
				notifyDataSetChanged();
				break;
			case 1:
				Log.i("MyGalleryAdapter","handleMessage_1");
				StreamObj lock = new StreamObj();
				GetPicTask getBackPicTask = new GetPicTask(lock, imageView, index, BACK);
				getBackPicTask.execute(picinfo_arrs.get(index).getImageUrlSmall());
				
				GetPicTask getSurfacePicTask = new GetPicTask(lock, imageView, index, SURFACE);
				getSurfacePicTask.execute(picinfo_arrs.get(index).getUrlSmall());
				Log.i("MyGalleryAdapter","handleMessage_1_1");
			}
		}
	}
	
	private class GetPicTask extends AsyncTask<String, Integer, Bitmap>
	{
        private StreamObj lock;
        private RecyclingImageView view;    
        private int index;
        private int isBackOrSurface;
        
		public GetPicTask(StreamObj lock,RecyclingImageView view,int index,int isBackOrSurface)
		{
			this.lock = lock;
			this.view = view;
			this.index = index;
			this.isBackOrSurface = isBackOrSurface;
		}
		@Override
		protected Bitmap doInBackground(String... params) 
		{
			// TODO 自动生成的方法存根	
			InputStream in = HttpUtil.getStreamFromURL(params[0]);
			Bitmap bit = Tools.getBitmapFromStream(width, height, in,view, index);
			/*
			Bitmap bitmap = Tools.zoomImage(bit, width, height);		
			if(bit != null && !bit.isRecycled())
			{
				bit.recycle();
				bit = null;
			}
			*/
			return bit;
			
//			return bit;
		}
		@Override
		protected void onPostExecute(Bitmap bit)
		{
			if(bit != null && !bit.isRecycled())
			{
				Bitmap bitmap = Tools.zoomImage(bit, width, height);		
				if(bit != null && !bit.isRecycled())
				{
					bit.recycle();
					bit = null;
				}
				bit = bitmap;
			}
		    if(lock != null && isBackOrSurface == BACK)
		    	lock.backBit = bit;
		    if(lock != null && isBackOrSurface == SURFACE)
		    	lock.surfaceBit = bit;
		    
		    RecyclingBitmapDrawable value_drawable = null;
			if((value_drawable = ComposeBitmap(lock, view, index)) != null)
			{
				view.setImageDrawable(value_drawable);
				notifyDataSetChanged();
			}
		}	
	}
	
	private RecyclingBitmapDrawable ComposeBitmap(StreamObj lock,RecyclingImageView view,int index)
	{
		synchronized (lock) {
			
			if(lock.backBit != null && lock.surfaceBit != null)
			{
				if(picinfo_arrs.size() > index)
				{
					String tag = picinfo_arrs.get(index).getImageUrlSmall() + picinfo_arrs.get(index).getUrlSmall();
					
					if(Init_choose.asynloader.getBitmapFromMemCache(tag) == null)
					{ 
						Bitmap compoesedBit = Tools.composeBitmap(lock.backBit, lock.surfaceBit, width, height, false);
						if(compoesedBit != null)
						{
							 RecyclingBitmapDrawable composed_drawable =  new RecyclingBitmapDrawable(context.getResources(), compoesedBit);
						      
						        Init_choose.asynloader.addBitmapToMemoryCache(tag,composed_drawable);
						        
						        if(lock.backBit != null && !lock.backBit.isRecycled())
						        {
						        	lock.backBit.recycle();
						        	lock.backBit = null;
						        }
						        return composed_drawable;
						} 
					}				
				}
			}
			return null;
		}
	}
	private class StreamObj
	{
//		public InputStream backIn = null;
//		public InputStream surfaceIn = null;
		public Bitmap backBit = null;
		public Bitmap surfaceBit = null;
	}
	
	private RecyclingBitmapDrawable getBitmapDrawable(final String back_imgurl,final String surface_imgurl,
			final RecyclingImageView imageView,final int id,final Handler handler)
	{
		RecyclingBitmapDrawable bitmapDrawable = null;
		final String imgurl = back_imgurl + surface_imgurl;
		
		if((bitmapDrawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMemCache(imgurl)) != null
				&& !bitmapDrawable.getBitmap().isRecycled())
			return bitmapDrawable;
		
		Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() 
			{
				// TODO 自动生成的方法存根
				final String bitmapName = back_imgurl.substring(back_imgurl.lastIndexOf("/")+1) + surface_imgurl.substring(surface_imgurl.lastIndexOf("/")+1); 
				File cacheDir = new File(Tools.cacheDir_path);
				
				if(cacheDir.exists())
				{
					File[] cacheFiles = cacheDir.listFiles();
					if(cacheFiles != null)
					{
						int i = 0;
						for(;i<cacheFiles.length;i++)
						{
							if(bitmapName.equals(cacheFiles[i].getName()))
							{
								if(id != imageView.getId())
									return ;
								Bitmap bit = BitmapFactory.decodeFile(Tools.cacheDir_path + bitmapName);    //从文件夹中获取图片
								if(bit != null)
								{
									RecyclingBitmapDrawable bitDrawable = new RecyclingBitmapDrawable(context.getResources(), bit);
									
									Init_choose.asynloader.addBitmapToMemoryCache(imgurl, bitDrawable);                      //将图片加入到缓存中
									Message msg = handler.obtainMessage(0, bitDrawable);
									handler.sendMessage(msg);
									return ;
								}	
							}
						}
					}
				}
				Message msg = handler.obtainMessage(1);
				handler.sendMessage(msg);
			}
			
		});
		return null;
	}
    
}
