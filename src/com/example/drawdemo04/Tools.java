package com.example.drawdemo04;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.example.drawdemo04.PenGraphycal.DrawBS;
import com.example.drawdemo04.SocietyModel.GetDisplayPicTask;
import com.example.drawdemo04.autoset_widget.SelectDialog;
import com.example.drawdemo04.initModel.Init_choose;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;

import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;

public class Tools {

	public static String Colorchange_flag = "color_change";
	public static String localadd_flag = "local_add"; 
	public static String concretadd_flag = "concret_add";
	public static int SOCIETY = 1;
	public static int LOCAL = 0;
	public static int SetName = 1;
	public static int WriteText = 0;
	public static  Config config = Bitmap.Config.ARGB_4444;
//	public static  Config config1 = Bitmap.Config.RGB_565;
	public static int screenWidth =  0;
	public static int screenHeight = 0;
	public static int viewHeight;
	public static int mheight = 0;
	public static ArrayList<DrawbsObject> traceRecord = new ArrayList<DrawbsObject>();
	public static String DEVICE_ID;
	public static String TUYA_MODLE;
	public static int PAGE_SIZE = 36;
	public static String filename = "/mnt/sdcard/test/1";
	public static String surfacepic_name = "surface";
	public static String backpic_name = "back";
	public static String little_name = "little";
	public static String configInfo_name = "config";
	public static  String thumbTag = "little";
	public static int itemWidth,itemHeight;
	public static int saveTag = 0;
	public static int scaleX,scaleY,scaleWidth,scaleHeight,initWidth,initHeight;
	public static float scale = 0;
    public static String charset = "UTF-8";
    public static Bitmap commentDisplay_bit = null;
    public static int submitStandard_width = 320;
    public static int submitStandard_height = 480;
    public static String title_name = "Title";
    public static Bitmap init_titlebit = null;
	public static String localSave_path = "mnt/sdcard/HBImg/";
	public static String cacheDir_path = "/mnt/sdcard/tuya/";
	public static String titleSaved_path = "mnt/sdcard/Title/";
	public static String currentSaved_path = "/mnt/sdcard/cache/";
	public static String themeSaved_path = "/mnt/sdcard/theme/";
	public static String themePic_path = "theme";
    public static Bitmap title_bit;
    public static ArrayList<Integer> refresh_arr = new ArrayList<Integer>();
    public static int layer_width ;
    public static int society_width;
    public static int scroll_state = OnScrollListener.SCROLL_STATE_IDLE;
    public static String back_cache = "back_drawable";
    public static String floor_cache = "floor_drawable";
    public static String floor_bit_cache = "floor_bit_drawable";
    public static int cacheSize;
    public static SelectDialog dialog;
    public static String identity = "cookie_1";
    public static String AccountName = "account";
    public static String theme_cache_key = "ThemePic";
    public static String default_pic_key = "ic_tuya";
    public static String init_add_key = "add_drawable";
    public static String init_little_key = "little_drawable";
    public static String add_key = "add_bit";
    
	 public static Bitmap zoomImage(Bitmap init,double newWidth,double newHeight){  //等比缩放，使得缩放后图片的宽和高到不小于newWidth,newHeight
	    	float width = init.getWidth();
	    	float height = init.getHeight();
	    	
	    	Matrix matrix = new Matrix();
	    	float scaleWidth = ((float)newWidth)/width;
	    	float scaleHeight = ((float)newHeight)/height;
	    	
	    	float scale;
	    	if(scaleWidth >= scaleHeight)
	    		scale = scaleWidth;		
	    	else
	    		scale = scaleHeight;
	    	
	        matrix.reset();
	    	matrix.postScale(scale, scale);
	    	return Bitmap.createBitmap(init, 0, 0, (int)width, (int)height,matrix,true); 
	    	
	    }
	 public static Bitmap zoomBitmap(Bitmap init,double newWidth,double newHeight)  //按照newWidth,newHeight,不等比缩放
	 {
		 float width = init.getWidth();
		 float height = init.getHeight();
		 
		 Matrix matrix = new Matrix();
		 float scaleWidth = ((float)newWidth)/width;
	     float scaleHeight = ((float)newHeight)/height;
	     	     
	     matrix.postScale(scaleWidth, scaleHeight);
	     return Bitmap.createBitmap(init, 0, 0, (int)width, (int)height,matrix,true); 
	 }
	 public static Bitmap zoomLargeImage(Bitmap init,double newWidth,double newHeight)  //缩放图片 效果相当于ImageView 的fitCenter,等比缩放
	 {
		 float width = init.getWidth();
		 float height = init.getHeight();
		 
		 Matrix matrix = new Matrix();
		 float scaleWidth = ((float)newWidth)/width;
	     float scaleHeight = ((float)newHeight)/height;
	     
	     float scale;
	     if(scaleWidth <= scaleHeight)
	    	scale = scaleWidth;		
	     else
	    	scale = scaleHeight;
	     Log.i("ZOOMLargeImage",String.valueOf(scale));
	     if(scale > 1)
	    	 scale = 1;
//	     Tools.scale = scale;
	     Log.i("scale",String.valueOf(scale));
	     
	     matrix.postScale(scale, scale);
	     Bitmap bit = Bitmap.createBitmap(init, 0, 0, (int)width, (int)height,matrix,true).copy(Tools.config, true);
	    
	     return bit;
	 }
	 public static Bitmap zoomLargeImageInBound(Bitmap init,double newWidth,double newHeight)
	 {

		 float width = init.getWidth();
		 float height = init.getHeight();
		 
		 Matrix matrix = new Matrix();
		 float scaleWidth = ((float)newWidth)/width;
	     float scaleHeight = ((float)newHeight)/height;
	     
	     float scale;
	     if(scaleWidth <= scaleHeight)
	    	scale = scaleWidth;		
	     else
	    	scale = scaleHeight;
	     
	     if(scale > 1)
	    	 scale = 1;
	     
	     if(width*scale*height*scale*2 > cacheSize*0.05*1024)
	     {
	    	 while(width*scale*height*scale*2 > cacheSize*0.05*1024)
	    	 {
	    		 scale = scale/2;
	    	 }
	     }
//	     Tools.scale = scale;
	     matrix.postScale(scale, scale);
	     return Bitmap.createBitmap(init, 0, 0, (int)width, (int)height,matrix,true); 
	 }
	 
	 public static Bitmap zoomSpecificImage(Bitmap init,float newWidth,float newHeight,int startX,int startY,int diswidth,int disheight){
		 float width = init.getWidth();                                          // 将 init 从StartX,startY开始，宽diswidth,高disheight的区域缩放到newWidth,newHeight
		 float height = init.getHeight();
		 
		 Matrix matrix = new Matrix();
	     float scaleWidth = (newWidth)/width;
	     float scaleHeight = (newHeight)/height;
	     /*
	     if(width*scaleWidth*height*scaleHeight*2 > cacheSize*0.1*1024)
	     {
	    	 while(width*scaleWidth*height*scaleHeight*2 > cacheSize*0.1*1024)
	    	 {
	    		 scaleWidth = scaleWidth/2;
	    		 scaleHeight = scaleHeight/2;
	    	 }
	     }
	     */
	     matrix.postScale(scaleWidth, scaleHeight);
	     return Bitmap.createBitmap(init, startX, startY, diswidth, disheight, matrix, true);
	 }
	 
	 public static int Dp2Px(float dp,final float scale){
			Log.i("Tools","density");
			return (int)(dp*scale+0.5f);
		}
	 
	 public static int getFontHeight(float fontSize){   //获取字体高度
		 Paint paint = new Paint();
		 paint.setTextSize(fontSize);
		 FontMetrics fm = paint.getFontMetrics();
		 return (int) Math.ceil(fm.descent - fm.top) + 2;  
	//	 return Math.ceil(fm.descent - fm.ascent);
	 }
	 
	 public static boolean isNetConnected(Context context)   //判断是否联网
	 {
		 ConnectivityManager cm = (ConnectivityManager)context.
				 getSystemService(Context.CONNECTIVITY_SERVICE);
		 if(cm != null)
		 {
			 NetworkInfo[] infos = cm.getAllNetworkInfo();
			 if(infos != null)
			 {
				 for(NetworkInfo ni : infos)
				 {
					 if(ni.isConnected()){
						 return true;
					 }
				 }
			 }
		 }
		 return false;
	 }
	 
	 public static boolean hasSdCard()
	 {
		 if (android.os.Environment.getExternalStorageState().equals(
	               android.os.Environment.MEDIA_MOUNTED))
		 {
			 return true;
		 }
		 return false;
	 }
	 public static byte[] readStream(InputStream inStream,View view,int id,AsyncTask<String, Integer, Bitmap> task,int total) throws IOException
	 {
		 if(inStream == null)
			 return null;
		 
		 int count = 0;
		 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		 byte[] buffer = new byte[1024];
		 int len = 0;
		 if(view != null)
		 {
			 while((view.getId() == id && (len=inStream.read(buffer)) != -1))
			 {
				 outStream.write(buffer, 0, len);
				 if(task != null)
				 {
					 count += len;
					 ((GetDisplayPicTask)task).publishProgress((int) ((count / (float) total) * 100));
				 }
			 }	 
		 }
		 else
		 {
			 while((len=inStream.read(buffer)) != -1)
			 {
				 outStream.write(buffer, 0, len);
				 if(task != null)
				 {
					 count += len;
					 ((GetDisplayPicTask)task).publishProgress((int) ((count / (float) total) * 100));
				 }
			 }
		 }
		 outStream.close();
		 inStream.close();
		 inStream = null;
		 
		 if(view != null && id != view.getId())
		 {
			 outStream = null;
			 return null;
		 }
		 return outStream.toByteArray();
	 }
	 public static void saveImage(final Bitmap surface_bit,final Bitmap back_bit,final String configInfo,final int viewWidth,
			 final int viewHeight,final int screenHeight,final int dis,final Handler handler)
	 {
			
		Init_choose.fixedthreadpool.execute(new Runnable() {
			
			boolean judge1 = false,judge2 = false,judge3 = false,judge4 = false;
			String pathname = composeFilename();
			FileOutputStream backfos = null;
			FileOutputStream surfacefos = null;
			FileOutputStream littlefos = null;
			FileOutputStream configInfofos = null;
			String type = ".png";
	        String text_type = ".txt";
	        Canvas canvas ;
	        Bitmap bitmap = null;
	        int itemWidth = (int)((screenWidth - 6*dis)/3.0);
			int itemHeight = (int)((screenHeight*0.9 - 6*dis)/3.0);
			String strPath = null;
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				try {
					strPath = new String(Tools.localSave_path);
						
					File fPath = new File(strPath);
					if (!fPath.exists()) 
					{
						fPath.mkdir();
					}
					
					strPath = new String(Tools.localSave_path + pathname);
					fPath = new File(strPath);
					if (!fPath.exists()) 
					{
						fPath.mkdir();
					}
					File back_file = new File(strPath + Tools.backpic_name + type);
					File surface_file = new File(strPath + Tools.surfacepic_name + type);
					File little_file = new File(strPath + Tools.little_name + type);
					File info_file = null;
					if(configInfo != null)
					     info_file = new File(strPath + Tools.configInfo_name + text_type);
				
					try {
						back_file.createNewFile();
						surface_file.createNewFile();
						if(configInfo != null)
						    info_file.createNewFile();
						little_file.createNewFile();
						
						} catch (IOException e) 
						{
							e.printStackTrace();
						}

						if(back_bit != null)
						{
							backfos = new FileOutputStream(back_file);
							back_bit.compress(Bitmap.CompressFormat.PNG, 100, backfos);	
					//		bitmap = Bitmap.createBitmap(back_bit).copy(Tools.config, true);
							bitmap = back_bit;
							canvas = new Canvas(bitmap);
						}
						else
						{
							bitmap = Bitmap.createBitmap(viewWidth, viewHeight, null);
							canvas = new Canvas(bitmap);
						}
						
						if (surface_bit != null)
						{
							surfacefos = new FileOutputStream(surface_file);
							surface_bit.compress(Bitmap.CompressFormat.PNG, 100, surfacefos);
							canvas.drawBitmap(surface_bit, 0, 0, null);
							surface_bit.recycle();
						}
					    if(bitmap != null)
					    {
					    	bitmap = Tools.zoomImage(bitmap, itemWidth, itemHeight);
					    	littlefos = new FileOutputStream(little_file);
					    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, littlefos);
					    	bitmap.recycle();
					    }
						if(configInfo != null && !configInfo.equals(""))
						{
							configInfofos = new FileOutputStream(info_file);
							byte[] content = configInfo.getBytes();
							
							try {
								configInfofos.write(content);
								
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}			
						}
						else
							judge3 = true;
						
						System.gc();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally 
					{
						if (backfos != null) {
							try {
								backfos.flush();
								backfos.close();
								judge1 = true;
							} catch (IOException e) {
								e.printStackTrace();
								Log.e("send picture to dbserver", "关闭上传图片的数据流失败！");
								judge1 = false;
							}
						}
						if(surfacefos != null){
							try {
								surfacefos.flush();
								surfacefos.close();
								judge2 = true;
							} catch (IOException e) {
								e.printStackTrace();
								Log.e("send picture to dbserver", "关闭上传图片的数据流失败！");
								judge2 = false;
							}
						}
						if(littlefos != null){
							try {
								littlefos.flush();
								littlefos.close();
								judge4 = true;
							} catch (IOException e) {
								e.printStackTrace();
								Log.e("send picture to dbserver", "关闭上传图片的数据流失败！");
								judge4 = false;
							}
						}
						if(configInfofos != null){
							try {
								configInfofos.flush();
								configInfofos.close();
								judge3 = true;
							} catch (IOException e) {
								e.printStackTrace();
								Log.e("send picture to dbserver", "关闭上传图片的数据流失败！");
								judge3 = false;
							}
						}
					}
				 if(judge1 && judge2 && judge3 && judge4)
				 {
					 Message msg = handler.obtainMessage(4,strPath);
					 handler.sendMessage(msg);
				 }
				 else
				 {
					 Message msg = handler.obtainMessage(5);
					 handler.sendMessage(msg);
				 }
			}
		});
		
	 }
	 private static String composeFilename()
	 {
		  Time t = new Time();
		  t.setToNow();
		    
		  String date = String.valueOf(t.year) + String.valueOf(t.month+1) + String.valueOf(t.monthDay);
		  
		  String time = String.valueOf(t.hour) + String.valueOf(t.minute) + String.valueOf(t.second);
		  return (date + time + "/");
	 }
	 
	 public static void saveTitleImg(Bitmap title_bit,Handler handler)
	 {
		 if(title_bit == null)
		 {
			 if(handler != null)
			 {
				 Message msg = handler.obtainMessage(2);
				 handler.sendMessage(msg);
			 }
		 }
		 String accountId = Tools.identity.substring(Tools.identity.indexOf("_")+1);
		 saveImg(title_bit, Tools.titleSaved_path, Tools.title_name + accountId, handler);
	 }
	 public static String saveImg(Bitmap bit,String dir_path,String file_path,Handler handler)
	 {
		 String type = ".png";
		 FileOutputStream titlefos = null;
		 try {
				String strPath = new String(dir_path);
					
				File fPath = new File(strPath);
				if (!fPath.exists()) 
				{
					fPath.mkdir();
				}
				
				strPath = new String(dir_path + file_path + type);
				File title_file = new File(strPath);
				if (!title_file.exists()) 
				{
					title_file.createNewFile();
				}
				else
				{
					title_file.delete();
					title_file.createNewFile();
				}

				if(bit != null)
				{
					titlefos = new FileOutputStream(title_file);
					bit.compress(Bitmap.CompressFormat.PNG, 100, titlefos);	
					return new String("保存成功");
				}

					
			} catch (FileNotFoundException e) {
					e.printStackTrace();
					if(handler != null)
					{
						Message msg = handler.obtainMessage(2);
						handler.sendMessage(msg);
					}
					else
						return new String("保存失败");
		
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				if(handler != null)
				{
					Message msg = handler.obtainMessage(2);
					handler.sendMessage(msg);
				}
				else
					return new String("保存失败");
							
			} finally 
			{		
				if (titlefos != null)
				{
					try {
						titlefos.flush();
						titlefos.close();

						} catch (IOException e) 
						{
							e.printStackTrace();
							Log.e("send picture to dbserver", "关闭上传图片的数据流失败！");
							if(handler != null)
							{
								Message msg = handler.obtainMessage(2);
								handler.sendMessage(msg);
							}
							else
								return new String("保存失败");
						}
				}
					
			}
		 return new String("保存失败");
	 }
	 public static Bitmap getBitmapFromStream(int width,int height,InputStream in,View view,int id)
	 {
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    
		    options.inJustDecodeBounds = true; 
		    byte[] buffer = null;
		    Bitmap bit = null;
		    
			try {
				buffer = Tools.readStream(in,view,id,null,0);
				
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		    if(buffer == null)
		    	return null;
		    else
		    {
		    	bit = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);  //先读取图片的宽和高
		    	
			    int inSampleSize = 1;
			    if (options.outHeight > height  || options.outWidth > width) {

			        final int halfHeight = options.outHeight / 2;
			        final int halfWidth = options.outWidth / 2;

			        while ((halfHeight / inSampleSize) > options.outHeight
			                && (halfWidth / inSampleSize) > options.outWidth) {
			            inSampleSize *= 2;
			        }
			    }
			    options.inSampleSize = inSampleSize;
			    options.inDither = false;
	    	//    options.inPreferredConfig = Tools.config1;
			    options.inPreferredConfig = Tools.config;
			    options.inPurgeable = true;
			    options.inInputShareable = true;
			    
			    options.inJustDecodeBounds = false;
			   
			    bit = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
			    return bit;
		    }
	 }
	 public static Bitmap getBitmap(int width,int height,InputStream in,boolean islarge,Bitmap floorBitmap,View view,int id){
			
		    Bitmap bit = getBitmapFromStream(width, height, in, view, id);
		    if(bit != null)
		    {
		    	if(floorBitmap != null && !floorBitmap.isRecycled())
		        {
		    		
		        	Bitmap bt = composeBitmap(floorBitmap, bit,width,height,islarge);   //把背景图和涂鸦层合成一张图片

		    		bit = bt;
		        }
		        else
		        {
		        	if(islarge == false)
		        	{
		        		Bitmap bt = Tools.zoomImage(bit, width, height);
		      // 		if(!Tools.isHigherthan10() && !bit.isRecycled())      ///////////////////////////////////////////////
		      //  		    bit.recycle();
		        		bit = bt;
		        	}
		        	else
		        	{
		    //    		Bitmap bt = Tools.zoomLargeImageInBound(bit, width, height);
		        		Bitmap bt = Tools.zoomLargeImage(bit, width, height);
		 //       		if(!Tools.isHigherthan10() && !bit.isRecycled())     //////////////////////////////////////////////
		 //       		    bit.recycle();
		        		bit = bt;
		        	}
		        }
		    }     
	        return bit;
		}
	 
	  public static Bitmap composeBitmap(Bitmap bitmap,Bitmap surfaceBitmap,double width,double height,boolean islarge){
	    	
		    if(bitmap == null || bitmap.isRecycled() || surfaceBitmap == null || surfaceBitmap.isRecycled())
		    	return null;
		    
	    	Bitmap floorBitmap = Bitmap.createBitmap(bitmap);
	    	Canvas floorCanvas = new Canvas(floorBitmap);
	    
	    	if(islarge == false)
	    	{
	    		Log.i("Tools_composeBitmap","large=false");
	    	    Bitmap mbit = Tools.zoomImage(surfaceBitmap, floorBitmap.getWidth(), floorBitmap.getHeight());
	    	    if(!Tools.isHigherthan10() && !surfaceBitmap.isRecycled())
	    	    	surfaceBitmap.recycle();
	     	    surfaceBitmap = mbit;
	    	}
	    	else
	    	{
	    		Bitmap mbit = Tools.zoomBitmap(surfaceBitmap, floorBitmap.getWidth(), floorBitmap.getHeight());
	    		 if(!Tools.isHigherthan10() && !surfaceBitmap.isRecycled())
		    	    	surfaceBitmap.recycle();
	      	    surfaceBitmap = mbit;
	    	}
	    	
	    	floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
	    	if(!Tools.isHigherthan10() && !surfaceBitmap.isRecycled())
	    		surfaceBitmap.recycle();
	    	
	    	return floorBitmap;
	    }
	  
	   public static Bitmap decodeAdjustedBitmap(String imageURL,int width,int height)
	   {
			File image_file = new File(imageURL);
			if(!image_file.exists())
				return null ;
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
		    return bit;
	   }
	   public static Bitmap decodeAdjustedBitmapFromSrc(Context context,int resId,int width,int height)
	   {
			BitmapFactory.Options options = new BitmapFactory.Options();							   
			options.inJustDecodeBounds = true;								    
			Bitmap bit = BitmapFactory.decodeResource(context.getResources(), resId,options);
		       
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
		
		    bit = BitmapFactory.decodeResource(context.getResources(), resId,options);
		    return bit;
	   }
	   public static void DeleteFile(File file)
		{
			if(file.exists())
			{
				if(file.isFile())
				{
					file.delete();
					return;
				}
				if(file.isDirectory())
				{
					File[] childFiles = file.listFiles();
					if(childFiles == null || childFiles.length == 0){
						file.delete();
						return;
					}
					for(File f:childFiles)
						DeleteFile(f);
					
					file.delete();
				}
			}
		}
	   
	   public static Boolean isHigherthan10(){
	    	int sysVersion = Integer.parseInt(VERSION.SDK);
	    	if(sysVersion > 10){
	    		return true;
	    	}
	    	else{
	    		return true;
	    	} 		
	    }
	    public static Boolean isLargerthan10()
	    {
	    	int sysVersion = Integer.parseInt(VERSION.SDK);
	    	if(sysVersion > 10){
	    		return true;
	    	}
	    	else{
	    		return false;
	    	} 		
	    }
	 public static boolean judgeIfExistSD()
	 {
		 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		 {
			 return true;
		 }
		 return false;
	 }
	 
	 public static void clearMemoryCache()            //清除图片缓存操作
	 {
		for(int i=0;i<Tools.refresh_arr.size();i++)
		{
			int index = Tools.refresh_arr.get(i);
			Init_choose.asynloader.removeBitmapFromCache(String.valueOf(index));
		}
	 }
	 
	 public static Bitmap createQRImage(String url,int width,int height)
	 {
		 int QR_WIDTH = width;
		 int QR_HEIGHT = height;
		 try
		 {
			 if(url == null || "".equals(url) || url.length() < 1)
			 {
				 return null;
			 }
			 Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			 hints.put(EncodeHintType.CHARACTER_SET, charset);
			 BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
	         int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
	       
	         for (int y = 0; y < QR_HEIGHT; y++)
	         {
	            for (int x = 0; x < QR_WIDTH; x++)
	           {
	              if(bitMatrix.get(x, y))
	              {
	                 pixels[y * QR_WIDTH + x] = 0xff000000;
	              }
	              else
	              {
	                 pixels[y * QR_WIDTH + x] = 0xffffffff;
	              }
	           }
	         }
	            //生成二维码图片的格式，使用ARGB_8888
	         Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,Tools.config);
	         bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
	            //显示到一个ImageView上面
	         return bitmap;
		 }
		 catch (WriterException e)
	     {
	        e.printStackTrace();
	     }
		 return null;
	 }
	    
	    public static SelectDialog showProgress(Activity activity)
		{
	    	SelectDialog dialog = new SelectDialog(activity,R.style.progressdialog,R.layout.progress_dialog);
			dialog.setCancelable(false);
			dialog.show();
			return dialog;
		}
		
		public static void closeProgress(SelectDialog dialog)
		{
			if(dialog != null)
				dialog.dismiss();
		}
	    public static void showMyProgress(Activity activity)
	   {
		    dialog = new SelectDialog(activity,R.style.progressdialog,R.layout.progress_dialog);
			dialog.setCancelable(false);
			dialog.show();
		}
		public static void closeMyProgress()
		{
			if(dialog != null)
				dialog.dismiss();
		}
		
}
