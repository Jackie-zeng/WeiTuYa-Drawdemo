package com.example.drawdemo04.SocietyModel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.MyGalleryAdapter.MyHandler;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter{

	private final int BACK = 0;
	private final int SURFACE = 1;
	private ArrayList<Comment> commentcontent = new ArrayList<Comment>();
	private Context context;
	private Bitmap bit;
	private boolean ishigher;

	private int width,height;
	private String urlOrigin,imageUrlOrigin;
	private ProgressBar progress;
	private int progressUpdate ;
	
	public CommentListAdapter(Context context,ArrayList<Comment> commentcontent,int width,int height,String url_origin,String image_url_origin){
		this.context = context;
		this.commentcontent = commentcontent;
		this.width = width;
		this.height = height;
		this.urlOrigin = url_origin;
		this.imageUrlOrigin = image_url_origin;
		
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		Log.i("commentcontentsize",String.valueOf(commentcontent.size()));
		if(urlOrigin == null)
		    return commentcontent.size();
		else
			return (1 + commentcontent.size()); 
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		if(urlOrigin == null)
			return commentcontent.get(arg0);
		else
			return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}
	
	@Override
	public int getViewTypeCount(){
		if(urlOrigin == null)
			return 1;
		else
			return 2;
	}
    
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		// TODO 自动生成的方法存根
		 
		ViewHolder viewholder;
		if(urlOrigin != null)
		{
			if(position == 0)
			{
				View v = LayoutInflater.from(context).inflate(R.layout.image_item, null);
				viewholder = new ViewHolder();
				viewholder.image = (RecyclingImageView)v.findViewById(R.id.contentImage);
				viewholder.image.setId(position);
				viewholder.isTitle = true;
			
				progress = (ProgressBar)v.findViewById(R.id.progress);
				
				MyHandler handler = new MyHandler(viewholder.image);
				RecyclingBitmapDrawable value = getBitmapDrawable(imageUrlOrigin,
						urlOrigin, viewholder.image, position, handler);
	//			RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(viewholder.image,displaypicUrl,
	//					Init_choose.fixedthreadpool,width,(int)(height*0.45),false,null);
	//		    RecyclingBitmapDrawable value = null;	
				if(value == null)
				{
					/*
					Bitmap bit = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_tuya);
					RecyclingBitmapDrawable value_drawable = new RecyclingBitmapDrawable(context.getResources(), bit);
					viewholder.image.setImageDrawable(value_drawable);
					Log.i("CommentListAdapter","getView0");
					*/
					progress.setVisibility(View.VISIBLE);
				}
				else
				{
					viewholder.image.setImageDrawable(value);
				}		
				convertview = v;
				convertview.setTag(viewholder);
			}
			else
			{
				
				if(convertview == null)
				{
					View v = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
					viewholder = new ViewHolder();
					viewholder.titleImage = (RecyclingImageView)v.findViewById(R.id.image);
					viewholder.name = (TextView)v.findViewById(R.id.nameText);
					viewholder.time = (TextView)v.findViewById(R.id.time);
					viewholder.content = (TextView)v.findViewById(R.id.contentText);
					viewholder.isTitle = false;
					
					convertview = v;
					convertview.setTag(viewholder);
				}
				else
				{
					viewholder = (ViewHolder)convertview.getTag();
					if(viewholder != null && viewholder.isTitle == true)
					{
						convertview = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
						
						viewholder = new ViewHolder();
						viewholder.titleImage = (RecyclingImageView)convertview.findViewById(R.id.image);
						viewholder.name = (TextView)convertview.findViewById(R.id.nameText);
						viewholder.time = (TextView)convertview.findViewById(R.id.time);
						viewholder.content = (TextView)convertview.findViewById(R.id.contentText);
						viewholder.isTitle = false;
						
						convertview.setTag(viewholder);
						
					}
				}
				
	//		viewholder.titleImage.setImageBitmap(null);
		
			viewholder.name.setText(commentcontent.get(position-1).getName());
			viewholder.time.setText(commentcontent.get(position-1).getTime());
			viewholder.content.setText(commentcontent.get(position-1).getContent());
			
			Log.i("commentListAdapter",String.valueOf(position));
			RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)Init_choose.asynloader.loadBitmap(viewholder.titleImage, commentcontent.get(position-1).getImageURL(), 
					Init_choose.fixedthreadpool,(int)(width*0.35),(int)(width*0.35),false,null);
			if(value == null)
			{
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.title);
				viewholder.titleImage.setImageDrawable(new RecyclingBitmapDrawable(context.getResources(), bitmap));
			}
			else
				viewholder.titleImage.setImageDrawable(value);
		  }
		}
		else
		{
			if(convertview == null)
			{
				View v = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
				viewholder = new ViewHolder();
				viewholder.titleImage = (RecyclingImageView)v.findViewById(R.id.image);
				viewholder.name = (TextView)v.findViewById(R.id.nameText);
				viewholder.time = (TextView)v.findViewById(R.id.time);
				viewholder.content = (TextView)v.findViewById(R.id.contentText);
				
				convertview = v;
				convertview.setTag(viewholder);
			}
			else
			{
				viewholder = (ViewHolder)convertview.getTag();
			}
			viewholder.name.setText(commentcontent.get(position).getName());
			viewholder.time.setText(commentcontent.get(position).getDate() + " " + commentcontent.get(position).getTime());
			viewholder.content.setText(commentcontent.get(position).getContent());
			
			Log.i("CommentListAdapter","beforeSetValue");
			RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)Init_choose.asynloader.loadBitmap(viewholder.titleImage, commentcontent.get(position).getImageURL(), 
					Init_choose.fixedthreadpool,(int)(width*0.35),(int)(width*0.35),false,null);
			Log.i("CommentListAdapter","afterSetValue");
			if(value == null)
			{
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.title);
				viewholder.titleImage.setImageDrawable(new RecyclingBitmapDrawable(context.getResources(), bitmap));
			}
			else
				viewholder.titleImage.setImageDrawable(value);
		}
		
		return convertview;
	}
    
	class ViewHolder{

		RecyclingImageView image;
		RecyclingImageView titleImage;
		TextView name;
		TextView time;
		TextView content;
		Boolean isTitle;
	}
	
	private void judgeWhichVerson(){
	     if(Integer.parseInt(VERSION.SDK)>10)
	    	 ishigher = true;
	     else
	    	 ishigher = false;   
	}
	
	private void getBitmap(int width,int height){
		
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    bit = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg01, options);
	    
	    options.inSampleSize = options.outHeight/(int)(height*0.45);
	    options.outHeight = (int)(height*0.45);
	    options.outWidth = width;
	    
	    options.inDither = false;
	    options.inPreferredConfig = Config.RGB_565;
	    options.inPurgeable = true;
	    options.inInputShareable = true;
	    options.inJustDecodeBounds = false;
	    
        bit = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg01,options);
	    	
	}
	
	private Bitmap ZoomImage(Bitmap init,int newWidth,int newHeight){
		float width = init.getWidth();
		float height = init.getHeight();
		
		Matrix matrix = new Matrix();
    	float scaleWidth = ((float)newWidth)/width;
    	float scaleHeight = ((float)newHeight)/height;
    	
    	matrix.postScale(scaleWidth, scaleHeight);
    	return Bitmap.createBitmap(init, 0, 0, (int)width, (int)height,matrix,true); 	
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
				if(progress != null)
					progress.setVisibility(View.GONE);
				imageView.setImageDrawable((RecyclingBitmapDrawable)(msg.obj));
		//		imageView.setId(1);
				notifyDataSetChanged();
				break;
			case 1:
				
				StreamObj lock = new StreamObj();
				GetDisplayPicTask getBackPicTask = new GetDisplayPicTask(context, lock, imageView, BACK, width, height,imageUrlOrigin + urlOrigin);
				getBackPicTask.setAdapter(CommentListAdapter.this);
				getBackPicTask.execute(imageUrlOrigin);
				
				GetDisplayPicTask getSurfacePicTask = new GetDisplayPicTask(context, lock, imageView, SURFACE, width, height,imageUrlOrigin + urlOrigin);
				getSurfacePicTask.setAdapter(CommentListAdapter.this);
				getSurfacePicTask.execute(urlOrigin);
				
			}
		}
	}
	
	 public void setProgressGone()
	 {
		 if(progress != null && progress.getVisibility() == View.VISIBLE)
			 progress.setVisibility(View.VISIBLE);
	 }
	 
	 private void showProgressUpdate(int progress_value,StreamObj lock)
	 {
		 synchronized (lock) {
			 Log.i("CommentListAdapter","showProgressUpdate");
			 progressUpdate += progress_value;
			 this.progress.setProgress(progressUpdate/2);
			 if(progressUpdate >= 200)
			 {
				 progressUpdate = 0;
				 progress.setVisibility(View.GONE);
			 } 
		} 
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
