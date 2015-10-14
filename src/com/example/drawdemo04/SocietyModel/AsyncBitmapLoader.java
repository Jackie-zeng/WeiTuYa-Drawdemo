package com.example.drawdemo04.SocietyModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.util.EncodingUtils;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class AsyncBitmapLoader {

//	private LruCache<String, Bitmap> mMemoryCache;
	private LruCache<String,BitmapDrawable> mMemoryCache;
//	private HashMap<String, SoftReference<Bitmap>>  secondCache;
	private HashMap<String, SoftReference<BitmapDrawable>> secondCache;
	private boolean isHigher = false;
	public static String dirpath ;
    private Context context;
    private int maxMemory;
    private AdjustSizeofInitpicWindow adjustView_window;
    private BaseAdapter adapter;
    
	public AsyncBitmapLoader(Context context)
	{
		this.context = context;
		initLru();
		dirpath = Tools.cacheDir_path;
	}

	public void setAdapter(BaseAdapter adapter){
		this.adapter = adapter;
	}
	public void setWindow(AdjustSizeofInitpicWindow mWindow)
	{
		this.adjustView_window = mWindow;
	}
	
	public BitmapDrawable loadBitmap(final View imageView,final String imageURL,ExecutorService fixedthreadpool,
			final int width,final int height,final boolean islarge,final Bitmap floorBitmap)
	{
		final int id;
		if(imageView instanceof RecyclingImageView)
			id = imageView.getId();
		else
		{
			imageView.setId(0);
			id = 0;
		}
		
		if(imageURL == null || imageURL.equals(""))
			return null;
		
		BitmapDrawable mvalue;
		if((mvalue = getBitmapFromMemCache(imageURL)) != null)        //根据imageURL 先从缓存中招
		{
			return mvalue;
		}
		else
	  {	
			final String bitmapName = imageURL.substring(imageURL.lastIndexOf("/")+1);  //根据imageURL获取文件名bitmapName
			
			final Handler handler = new Handler(){
				@Override			
				public void handleMessage(Message msg){
					
					switch(msg.what)
					{
					case 0:
						Log.i("AsyncBitmaploader_handler","0");
						if(imageView instanceof RecyclingImageView && Tools.scroll_state == OnScrollListener.SCROLL_STATE_IDLE)
						{
							if(imageView.getId() == id)
							{
								((RecyclingImageView) imageView).setImageDrawable((RecyclingBitmapDrawable)(msg.obj));
								
								if(adapter!=null)
									adapter.notifyDataSetChanged();
							}			   
						}
						if(imageView instanceof MyView)
						{
							Bitmap bit = ((RecyclingBitmapDrawable)msg.obj).getBitmap();
							((MyView)imageView).setImageBitmap(bit);
							
						}
						break;	
						
					case 1:
						Log.i("AsyncBitmaploader_handler","1");
						if(adapter != null)
						   adapter.notifyDataSetChanged();
						break;
					case 2:
						Log.i("AsyncBitmaploader_handler","2");
						Bitmap bit = ((Bitmap)msg.obj);
						if(imageView instanceof RecyclingImageView)
						{		
							if(bit != null && imageView.getTag() != null && (Integer)(imageView.getTag()) == 1)
							{
								Bitmap mbit = Tools.zoomLargeImage(bit, width, height);
								RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), mbit);
								addBitmapToMemoryCache(imageURL, value);
								
								((RecyclingImageView) imageView).setImageDrawable(value);
/////////////////////////		Tools.commentDisplay_bit = bit;	
						//		bit.recycle();
							}
							else
							{
								RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
								addBitmapToMemoryCache(imageURL, value);
								
								if(Tools.scroll_state == OnScrollListener.SCROLL_STATE_IDLE)
									 ((RecyclingImageView) imageView).setImageDrawable(value);     
							}
								
							imageView.setId(1);
							
							if(adapter!=null)
								   adapter.notifyDataSetChanged();
								   
						}
						if(imageView instanceof MyView)
						{
							RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
							addBitmapToMemoryCache(imageURL, value);
							
							Bitmap bitmap = value.getBitmap();
							((MyView)imageView).setImageBitmap(bitmap);
						}
						break;
						
					}
					
				}
			};
       
	  if(Tools.scroll_state == OnScrollListener.SCROLL_STATE_IDLE)
	  {
		 fixedthreadpool.execute(new Runnable() {    //用线程池执行线程从文件夹 或网上获取图片
                                                     //线程池 的并发线程数量设置为10，避免 过多线程并发进行 
			@Override
			public void run()
			{
				Log.i("AsyncBitmapLoader_runnable","run");
		
				Boolean judge = false;
				File cacheDir = new File(dirpath);
			
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
						Bitmap bit = BitmapFactory.decodeFile(dirpath + bitmapName);    //从文件夹中获取图片
						
	//					addBitmapToMemoryCache(imageURL, bit);                      //将图片加入到缓存中
						judge = true;
						Message msg = handler.obtainMessage(2, bit);
						handler.sendMessage(msg);
					}
				}
			}
			  }
				
			if(judge == false && imageURL.startsWith(HttpUtil.judgeIsUrl))     //以http 开头，能偶从网上获取，涂鸦图的缩略图服务器不提供
			{
				
				InputStream in = HttpUtil.getStreamFromURL(imageURL);   //根据图片的url 从网上获取图片
				Bitmap newThumbnail = null;

				Bitmap bitmap = Tools.getBitmap(width, height, in,islarge,floorBitmap,imageView,id);
	            Log.i("AsyncBitmaploader","loadbitmap1");
	            if(bitmap == null)
	            	return ;
				RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bitmap);
				addBitmapToMemoryCache(imageURL, value);  // 将从网络获取的图片加入缓存
				 Log.i("AsyncBitmaploader","loadbitmap2");
				 
				Message msg = handler.obtainMessage(0, value);
				handler.sendMessage(msg);
				
				File dir = new File(dirpath);
				if(!dir.exists())
					dir.mkdirs();
				
				File bitmapFile = new File(dirpath + imageURL.substring(imageURL.lastIndexOf("/")+1));
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
					fos.close();                                            // 把图片存进本地文件
					
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
	  }
		return null;
	}
}
	
	private void initLru()
	{
		isHigherthan10();          //判断系统是否高于2.3, 2.3系统对于bitmap 的释放略有不同，要调用recycle()
		
		secondCache = new HashMap<String, SoftReference<BitmapDrawable>>();
		maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
		int cacheSize = maxMemory/8;             //LRUCache 的内存限制设为 应用所获得的最大内存的 1/8，避免内存溢出
		Tools.cacheSize = cacheSize;
		
		   mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize){
            @Override
			protected int sizeOf(String key,BitmapDrawable bitmapdrawable){

            	return getBitmapSize(bitmapdrawable)/1024;  
			}
			@Override
			protected synchronized void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue,

					BitmapDrawable newValue)
			{	
				if(isHigher == false)        // 如果系统低于或等于2.3，则bitmap 从LRUCache 删除后直接recyle（）回收
				{
					if(RecyclingBitmapDrawable.class.isInstance(oldValue)){
						((RecyclingBitmapDrawable) oldValue).setIsCached(false);
					}
				}
				else
				{
					if(secondCache.get(key) == null)              // 系统高于2.3，将bitmap 加入softReference 二级缓存
						secondCache.put(key, new SoftReference<BitmapDrawable>(oldValue));
				}				
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
	}
	public void addBitmapToDirCache(final Bitmap backBitmap,final Bitmap floorBit,final Handler handler)
	{
		Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				FileOutputStream backfos = null,floorfos = null,floorbitfos = null;
				
				if(Tools.judgeIfExistSD())
				{
					File dir = new File(Tools.currentSaved_path);
					if(!dir.exists())
						dir.mkdirs();
					
					File back_bitmapFile = new File(Tools.currentSaved_path + Tools.back_cache);
					File floor_bitFile = null;
					
					if(floorBit != null)
						floor_bitFile = new File(Tools.currentSaved_path + Tools.floor_bit_cache);
					try 
					{
						
						if(!back_bitmapFile.exists())
							back_bitmapFile.createNewFile();
						
						if(floor_bitFile != null)
						{
							if(!floor_bitFile.exists())
								floor_bitFile.createNewFile();
						}
						
					} catch (IOException e) 
					{
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					try {
						backfos = new FileOutputStream(back_bitmapFile);
						if(floorBit != null)
							floorbitfos = new FileOutputStream(floor_bitFile);
						
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				else
				{
					try {
						backfos = context.openFileOutput(Tools.back_cache, Context.MODE_PRIVATE);
						if(floorBit != null)
							floorbitfos = context.openFileOutput(Tools.floor_bit_cache, Context.MODE_PRIVATE);
						Log.i("AsyncBitmaploader","noSdcardSave");
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
				}
				Log.i("Async_addBitmaptodircache","APPcache");
				
				if(backfos != null && backBitmap != null && !backBitmap.isRecycled())
					backBitmap.compress(Bitmap.CompressFormat.PNG, 100, backfos);   //100 表示不压缩  30 表示压缩了70%  
				Log.i("Async_addBitmaptodircache","backbitmap");
	
				if(floorbitfos != null && floorBit != null && !floorBit.isRecycled())
					floorBit.compress(Bitmap.CompressFormat.PNG, 100, floorbitfos);
				
				try {
					if(backfos != null)
					   backfos.close();
					
					if(floorbitfos != null)
						floorbitfos.close();
					
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					
				} finally
				{
					Message msg = handler.obtainMessage(0);
					handler.sendMessage(msg);		// 把图片存进本地文件
				}
			}
		});
	}
	public void clearCache()
	{
		if(mMemoryCache != null)
			mMemoryCache.evictAll();
	}
	public Bitmap getBitmapFromDirCache(String fileName)
	{
	    if(Tools.judgeIfExistSD())
	    {
	    	File bitmapFile = new File(Tools.currentSaved_path + fileName);

			if(bitmapFile.exists())
			{
				Bitmap bit = BitmapFactory.decodeFile(Tools.currentSaved_path + fileName);
				Tools.DeleteFile(bitmapFile);
				
				if(bit != null)
					return bit;
				else
					return Bitmap.createBitmap(Tools.screenWidth, Tools.viewHeight, Tools.config);
			}
			return null;
	    }
	    else
	    {
	    	Log.i("AsyncBitmaploader","noSdcardSave");
	    	FileInputStream in = null;
			try {
				in = context.openFileInput(fileName);
				if(in != null)
				{
				   byte[] buffer = Tools.readStream(in,null,0,null,0);
				   if(buffer != null && buffer.length != 0)
			       {
			    	   Bitmap bit = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			    	   if(bit != null)
			    	   {
			    		   Log.i("Async_getBitmapFromdircache","bit notnull");
			    		   return bit;
			    	   }
			    	   else
			    		   return Bitmap.createBitmap(Tools.screenWidth, Tools.viewHeight, Tools.config);
			       }
				   else
					   return null;
				}
				else
					return null;
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 	 
	    }
		return null;
	}
	public void addBitmapToMemoryCache(String key,BitmapDrawable value)
    {
		if(value== null || key == null)
			return;
        if(mMemoryCache != null)
        {
        //	RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bitmap);
       // 	value.setIsCached(true);
        	if(isHigher == false && RecyclingBitmapDrawable.class.isInstance(value))
            	((RecyclingBitmapDrawable) value).setIsCached(true);
        	mMemoryCache.put(key, value);
        }
    }
	
	public void removeBitmapFromCache(String key)            //显示地从LRUCache 中移除bitmap
	{
		BitmapDrawable value = mMemoryCache.get(key);
		
		if(isHigher == false && RecyclingBitmapDrawable.class.isInstance(value))
			((RecyclingBitmapDrawable) value).setIsCached(false);
		
		mMemoryCache.remove(key);
	}
	
	public BitmapDrawable getBitmapFromMem(String key)    
	{
		return mMemoryCache.get(key);
	}
    public BitmapDrawable getBitmapFromMemCache(String key){
    	
    	if(mMemoryCache.get(key) == null)
    	{
    		if(isHigher == false)
    			return null;
    		
    		if(!secondCache.containsKey(key))
    		{
    			return null;
    		}		
    		else
    		{
    			SoftReference<BitmapDrawable> cache = secondCache.get(key);
    			
    			if(cache != null && cache.get() == null)
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
    
    private void SaveifComposeThumbnail(String URL,BitmapDrawable bitmap)  //保存图片
    {
    	String thumbUrl = URL + Tools.thumbTag;
    	
    	addBitmapToMemoryCache(thumbUrl.substring(thumbUrl.lastIndexOf("/")+1), bitmap);
    	
    	File dir = new File(dirpath);
		if(!dir.exists())
			dir.mkdirs();
		
		File bitmapFile = new File(dirpath + thumbUrl.substring(thumbUrl.lastIndexOf("/")+1));
		
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
			
			bitmap.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);   //100 表示不压缩  30 表示压缩了70%
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	
    }
    
    private Bitmap getThumbnail(Bitmap bitmap)
    {
    	if(bitmap != null)
    	{
    		float width = bitmap.getWidth();
    		float height = bitmap.getHeight();
    	    float newWidth = GalleryAdapter.itemwidth;
    	    float newHeight = GalleryAdapter.itemheight;
    	    
    		Matrix matrix = new Matrix();
    		float scaleWidth = newWidth/width;
    		float scaleHeight = newHeight/height;
    		
    		matrix.postScale(scaleWidth, scaleHeight);
    		return Bitmap.createBitmap(bitmap, 0, 0, (int)width, (int)height,matrix,true);
    	}
    	return null;
    }
    public BitmapDrawable getInitBit(final String imageURL,final Handler handler)    //获取图片
    {	
    	RecyclingBitmapDrawable mvalue;
    	if((mvalue = (RecyclingBitmapDrawable) getBitmapFromMemCache(imageURL)) != null)
		{
    		Log.i("Async_getInitBit","fromCache");
			return mvalue;
		}
    	Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				final String bitmapName = imageURL.substring(imageURL.lastIndexOf("/")+1);
				
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
						RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
						addBitmapToMemoryCache(imageURL, value);
						Message msg = handler.obtainMessage(4, value);
						handler.sendMessage(msg);
					}
				}	
			  }			
			}
		});
    	return null;
    }
    public void getText_Content(final String path,ExecutorService fixedthreadpool,final Handler handler)
    {
    	if(path == null || path.equals(""))
    		return ;
    	
    	fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				File contentFile = new File(path);
				String content = null;
				if(contentFile.exists())
				{
					try {
						
						FileInputStream fin = new FileInputStream(path);
						int length = fin.available();
						byte[] buffer = new byte[length];
						fin.read(buffer);
						content = EncodingUtils.getString(buffer, Tools.charset);
						
						fin.close();
						
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						
					}finally{
						Message msg = handler.obtainMessage(1);
						Bundle bundle = new Bundle();
						bundle.putString("configInfo", content);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
					
				}
			}
		});
    }
    
    /* 获取图片，基本按照内存，本地文件，网络三部曲，只不过根据参数 which，标识不同的使用，以上三种方法略有不同
     * 
     */
    public BitmapDrawable getAdustedBitmap(final int which,final String imageURL,ExecutorService fixedthreadpool,final Handler handler,final int width,final int height)
    {
    	if(imageURL == null || imageURL.equals(""))
    	{
    		Log.i("AsyncBitmaploader","GetAdjustedBitmap");	
    		return null;
    	}
			
    	if(getBitmapFromMemCache(imageURL) != null)
    	{
    		Log.i("fromcache","yes");
    		return getBitmapFromMemCache(imageURL);
    	}
    	else
    	{
    		if(which == 4)
    		{
    			File image_file = new File(imageURL);
				if(!image_file.exists())
					return null;
    		}
    		
    		if(Tools.scroll_state == OnScrollListener.SCROLL_STATE_IDLE)
    		{
    		    
    			fixedthreadpool.execute(new Runnable() {
    				
    				@Override
    				public void run() {
    					boolean hasFound = false;
    					// TODO 自动生成的方法存根
    					if(which != 0)
    					{
    						File image_file = new File(imageURL);
    						if(!image_file.exists())
    							return ;
    						BitmapFactory.Options options = new BitmapFactory.Options();							   
    						options.inJustDecodeBounds = true;								    
    						Bitmap bit = BitmapFactory.decodeFile(imageURL, options);
    					       
    						if(options.outHeight/height>=options.outWidth/width)
    						    options.inSampleSize = (int)(options.outHeight/(float)height);    	
    						else 
    						    options.inSampleSize = (int)(options.outWidth/(float)width);

    						Log.i("inSamplesize",String.valueOf(options.inSampleSize));
    						    
    						if(options.inSampleSize == 0)
    						    options.inSampleSize = 1;
    						options.inDither = false;
    						options.inPreferredConfig = Tools.config;
    						options.inPurgeable = true;
    						options.inInputShareable = true;					    
    						options.inJustDecodeBounds = false;
    					
    					    bit = BitmapFactory.decodeFile(imageURL, options);
    					    if(bit != null && !bit.isRecycled())
    					    {
    					    	 if(which == 1 || which == 3)
    							    {
    							    	bit = Tools.zoomLargeImage(bit, width, height);	
    							    	RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
    							    	Message msg = handler.obtainMessage(0,value);
    								    handler.sendMessage(msg);
    							    }
    							    else
    							    {
    							    	bit = Tools.zoomImage(bit, width, height);      ///////
    							    	RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
    							    	addBitmapToMemoryCache(imageURL, value);
    							    	Message msg = handler.obtainMessage(0,value);
    								    handler.sendMessage(msg);
    							    }
    					    }			   					    						 			 
    					}
    					else
    					{
    						final String bitmapName = imageURL.substring(imageURL.lastIndexOf("/")+1);
    						
    						File cacheDir = new File(dirpath);		
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
    										Log.i("AsyncBitmaploader","getAdjustbitmap_before");
    										Bitmap bit;
    										Log.i("AsyncBitmaploader",cacheFiles[i].getName());
    										
    										bit = BitmapFactory.decodeFile(dirpath + bitmapName);
    										
    										RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
    										addBitmapToMemoryCache(imageURL, value);
    										hasFound = true;
    										Message msg = handler.obtainMessage(3,value);
    										handler.sendMessage(msg);
    									}
    								}	
    							}
    						}
    						if(hasFound == false && imageURL.startsWith(HttpUtil.judgeIsUrl))
    						{
    							Log.i("AsyncBitmaploader","getAdjustbitmap_after");
    							InputStream in = HttpUtil.getStreamFromURL(imageURL);   //根据图片的url 从网上获取图片
    							
    							
    							Bitmap bitmap = Tools.getBitmap(width, height, in,true,null,null,0);
    								
    							File dir = new File(dirpath);
    							if(!dir.exists())
    								dir.mkdirs();
    							
    							File bitmapFile = new File(dirpath + imageURL.substring(imageURL.lastIndexOf("/")+1));
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
    								if(bitmap != null && !bitmap.isRecycled())
    								    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);   //100 表示不压缩  30 表示压缩了70%
    								fos.close();
    			//					value.setIsDisplayed(false);
    							} catch (FileNotFoundException e) {
    								// TODO 自动生成的 catch 块
    								e.printStackTrace();
    							} catch (IOException e) {
    								// TODO 自动生成的 catch 块
    								e.printStackTrace();
    							}
    							
    							RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bitmap);
    							if(value.getBitmap() == null)
    								Log.i("AsynicBitmaploader","value.getBitmap null");
    							
    							if(value.getBitmap() != null)
    							    addBitmapToMemoryCache(imageURL, value);

    			//				value.setIsDisplayed(true);
    							Message msg = handler.obtainMessage(3, value);
    							handler.sendMessage(msg);
    						}
    					}			
    				}
    			});
    		}
    		
    	}
    	if(which == 4)
    	{
    		return new RecyclingBitmapDrawable(context.getResources(), null);
    	}
    	return null;
    }
    
    private void isHigherthan10(){
    	int sysVersion = Integer.parseInt(VERSION.SDK);
    	Log.i("version",String.valueOf(sysVersion));
    	if(sysVersion > 10){
    		Log.i("ishigher","true");
    		isHigher = true;
    	}
    	else{
    		Log.i("ishigher","false");
    		isHigher = false;
    	}
    		
    }
    private int getBitmapSize(BitmapDrawable value){
    	Bitmap bit = value.getBitmap();
  //  	Log.i("getBitmapsize","error");
    	if(bit != null && !bit.isRecycled())
    	    return bit.getRowBytes()*bit.getHeight();
    	else return 0;
    }
}
