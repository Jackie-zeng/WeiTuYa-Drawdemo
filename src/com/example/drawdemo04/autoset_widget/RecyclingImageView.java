package com.example.drawdemo04.autoset_widget;

import com.example.drawdemo04.Tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class RecyclingImageView extends ImageView{

	public RecyclingImageView(Context context){
		super(context);
	}
	public RecyclingImageView(Context context, AttributeSet attrs) {
		
		super(context, attrs);
	}
	
	@Override
    protected void onDetachedFromWindow() {
        // This has been detached from Window, so clear the drawable
        setImageDrawable(null);
        super.onDetachedFromWindow();
    }
	
	@Override
	public void setImageDrawable(Drawable drawable){
		final Drawable previousDrawable = getDrawable();
		super.setImageDrawable(drawable);
		
		notifyDrawable(drawable,true);
		notifyDrawable(previousDrawable,false);
	}
	
	public Drawable getImageDrawable()
	{
		return getDrawable();
	}
	private static void notifyDrawable(Drawable drawable,final boolean isDisplayed)
	{
		if(drawable instanceof RecyclingBitmapDrawable){
			
	//		if(!Tools.isHigherthan10())
			if(!Tools.isLargerthan10())
			     ((RecyclingBitmapDrawable)drawable).setIsDisplayed(isDisplayed);
		}
		else
		{
			if(drawable instanceof LayerDrawable){
				
				LayerDrawable layerDrawable = (LayerDrawable) drawable;
	            for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
	                notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
	            }
			}
		}
	}
	
	
}
