package com.example.drawdemo04.autoset_widget;

import com.example.drawdemo04.Tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class RecyclingBitmapDrawable extends BitmapDrawable{

	private int mCacheRefCount = 0;
	private int mDisplayRefCount = 0;
	private boolean mHasBeenDisplayed = false;
	
	public RecyclingBitmapDrawable(Resources res,Bitmap bitmap){
		super(res,bitmap);
	}
	
	public void setIsDisplayed(boolean isDisplayed){
		synchronized (this) {
			if(isDisplayed){
				mDisplayRefCount ++ ;
				mHasBeenDisplayed = true;
			}
			else{
				mDisplayRefCount -- ;
			}
		}
		checkState();
	}
	
	public void setIsCached(boolean isCached){
		synchronized (this) {
			if(isCached){
				mCacheRefCount ++;
			}
			else{
				mCacheRefCount -- ;
			}
		}
		checkState();
	}
	
	private synchronized void checkState(){
//		if(mCacheRefCount <= 0 && mDisplayRefCount <=0 && hasValidBitmap())
//		if(mCacheRefCount <= 0 && mDisplayRefCount <=0 && mHasBeenDisplayed && hasValidBitmap() && !Tools.isHigherthan10())
		if(mCacheRefCount <= 0 && mDisplayRefCount <=0 && mHasBeenDisplayed && hasValidBitmap())
		{
			getBitmap().recycle();
			Log.i("recycle","bitmaprecycle");
		}
	}
	
	private synchronized boolean hasValidBitmap()
	{
		Bitmap bitmap = getBitmap();
		return bitmap != null && !bitmap.isRecycled();
	}
}
