package com.example.drawdemo04.SocietyModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import org.apache.http.params.HttpAbstractParamBean;

import com.example.drawdemo04.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AsyncHeadBitmapLoader {

	private LruCache<String, Bitmap> mMemoryCache;
	private HashMap<String, SoftReference<Bitmap>>  secondCache;
	private boolean isHigher = false;
	private String dirpath = "/mnt/sdcard/test/";
    private Context context;
    private int maxMemory;
 //   private Bitmap newThumbnail;
 //   private String thumbTag = "little";
//    private String judgeIsUrl = "http://";
    
	public AsyncHeadBitmapLoader(Context context)
	{
		this.context = context;
		initLru();
		
	}
	
	public Bitmap loadBitmap(final ImageView imageView,final String imageURL,ExecutorService fixedthreadpool,
			final int width,final int height)
	{
	
		if(getBitmapFromMemCache(imageURL) != null)
		{
			return getBitmapFromMemCache(imageURL);
		}
		else
	  {
			final String bitmapName = imageURL.substring(imageURL.lastIndexOf("&")+1);
			
			final Handler handler = new Handler(){
				@Override			
				public void handleMessage(Message msg){
					
					switch(msg.what)
					{
					case 0:
						imageView.setImageBitmap((Bitmap)msg.obj);
					}
					
				}
			};

		 fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run()
			{
				// TODO 自动生成的方法存根
		//		InputStream bitmapIs = HttpUtils.getStreamFromURL(imageURL);
				
				Boolean judge = false;
				File cacheDir = new File(dirpath);
				
				if(cacheDir.exists())
			  {
					
				File[] cacheFiles = cacheDir.listFiles();
			
				int i = 0;
				for(;i<cacheFiles.length;i++)
				{
					if(bitmapName.equals(cacheFiles[i].getName()))
					{
						Bitmap bit = BitmapFactory.decodeFile(dirpath + bitmapName);
						if(imageView.getTag() == null || (Integer)(imageView.getTag()) != 1)
						addBitmapToMemoryCache(imageURL, bit);
						judge = true;
						Message msg = handler.obtainMessage(0, bit);
						handler.sendMessage(msg);
					}
				}
				
			  }
				
				
			if(judge == false)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				Bitmap bitmap = getBitmap(width, height, R.drawable.bg01);
			    
				if(imageView.getTag() == null || (Integer)(imageView.getTag()) != 1)
				addBitmapToMemoryCache(imageURL, bitmap);
				Message msg = handler.obtainMessage(0, bitmap);
				handler.sendMessage(msg);
				
				File dir = new File(dirpath);
				if(!dir.exists())
					dir.mkdirs();
				
				File bitmapFile = new File(dirpath + imageURL.substring(imageURL.lastIndexOf("&")+1));
				if(!bitmapFile.exists())
				{
					try {
						bitmapFile.createNewFile();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(bitmapFile);
					
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);   //100 表示不压缩  30 表示压缩了70%
					fos.close();
					
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
		   }
				
		  }
		});
		return null;
	}
}
	
	private void initLru()
	{
		isHigherthan10();
		
		secondCache = new HashMap<String, SoftReference<Bitmap>>();
		maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
		int cacheSize = maxMemory/8;
		Log.i("cacheSize",String.valueOf(cacheSize));
		
		   mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
			protected int sizeOf(String key,Bitmap bitmap){
            	
            	return bitmap.getRowBytes()*bitmap.getHeight()/1024;
			}
			@Override
			protected synchronized void entryRemoved(boolean evicted, String key, Bitmap oldValue,

					Bitmap newValue)
			{
				if(isHigher == false)
					oldValue.recycle();
				else
				{
					if(secondCache.get(key) == null)
						secondCache.put(key, new SoftReference<Bitmap>(oldValue));
				}				
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
	}
	
	private void addBitmapToMemoryCache(String key,Bitmap bitmap)
    {
    	if(getBitmapFromMemCache(key) == null)
    		mMemoryCache.put(key, bitmap);
    }
	
    private Bitmap getBitmapFromMemCache(String key)
    {
    	
    	if(mMemoryCache.get(key) == null)
    	{
    		if(!secondCache.containsKey(key))
    			return null;
    		else
    		{
    			SoftReference<Bitmap> cache = secondCache.get(key);
    			if(cache.get() == null)
    				return null;
    			else
    			{
    				mMemoryCache.put(key, cache.get());
    				return cache.get();
    			}
    		}
    	}
    	return mMemoryCache.get(key);
    }
    
    private void isHigherthan10()
    {
    	int sysVersion = Integer.parseInt(VERSION.SDK);
    	if(sysVersion > 10)
    		isHigher = true;
    	else
    		isHigher = false;
    }
    
    private Bitmap getBitmap(int width,int height,int resId)
    {
			
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    Bitmap bit = BitmapFactory.decodeResource(context.getResources(), resId, options);
      
	    Log.i("width",String.valueOf(width));
	    Log.i("height",String.valueOf(height));
	    
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
	    
        bit = BitmapFactory.decodeResource(context.getResources(), resId,options);
        return bit;
	}

}
