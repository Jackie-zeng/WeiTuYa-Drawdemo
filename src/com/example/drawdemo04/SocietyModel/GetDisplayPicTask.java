package com.example.drawdemo04.SocietyModel;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;

import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

public class GetDisplayPicTask extends AsyncTask<String, Integer, Bitmap>
{
	private final int BACK = 0;
	private final int SURFACE = 1;
    private StreamObj lock;
    private RecyclingImageView view;    
    private int isBackOrSurface;
    private int width,height;
    private String tag;
    private Context context;
    private CommentListAdapter adapter;
    
	public GetDisplayPicTask(Context context ,StreamObj lock,RecyclingImageView view,int isBackOrSurface,int width,int height,String tag)
	{
		this.context = context;
		this.lock = lock;
		this.view = view;
		this.isBackOrSurface = isBackOrSurface;
		this.width = width;
		this.height = height;
		this.tag = tag;

	}
	@Override
	protected Bitmap doInBackground(String... params) 
	{
		// TODO 自动生成的方法存根	
		InputStream in = HttpUtil.getStreamFromURL(params[0]);
		
		Bitmap bit = Tools.getBitmapFromStream(width, (int)(height*0.45), in,view,0);

		return bit;
	}
	@Override
	protected void onPostExecute(Bitmap bit)
	{
		
		if(bit != null && !bit.isRecycled())
		{
			Bitmap bitmap = Tools.zoomBitmap(bit, width, (int)(height*0.45));		
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
		if((value_drawable = ComposeBitmap(lock, view)) != null)
		{
			view.setImageDrawable(value_drawable);
			if(adapter != null)
			{
				adapter.setProgressGone();
				adapter.notifyDataSetChanged();
			}   
		}	
	}	
	@Override
	protected void onProgressUpdate(Integer... values) 
	{  
	    int value = values[0]; 
	    /*
	    progressUpdate += value;
		progress.setProgress(value);
		if(progressUpdate >= 200)
		{
			progressUpdate = 0;
		    progress.setVisibility(View.GONE);
		} 
		*/
//	    showProgressUpdate(value,lock); 
	}  
    public void setAdapter(CommentListAdapter adapter)
    {
    	this.adapter = adapter;
    }
    
	public void publishProgress(int value)
	{
		publishProgress(value);
	}
	
	
	private RecyclingBitmapDrawable ComposeBitmap(StreamObj lock,RecyclingImageView view)
	{
		synchronized (lock) {
			
			if(lock.backBit != null && lock.surfaceBit != null)
			{		
				if(Init_choose.asynloader.getBitmapFromMemCache(tag) == null)
				{ 
			        Bitmap compoesedBit = Tools.composeBitmap(lock.backBit, lock.surfaceBit, width, (int)(height*0.45), false);
			        
			        Log.i("MyGalleryAdapter","composeBitmap");
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
			return null;
		}
	}
}