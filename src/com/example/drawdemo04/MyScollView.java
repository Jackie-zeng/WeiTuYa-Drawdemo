package com.example.drawdemo04;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.ScrollView;

public class MyScollView extends ScrollView{

	private int width,height;
	
	public MyScollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}
	public MyScollView(Context context,AttributeSet attrs)
	{
		super(context, attrs);
	}
	public MyScollView(Context context)
	{
		super(context);
	
	}
	/*
	public void setWidth(int width){
		this.width = width;
	}
	public void setHeight(int height){
		this.height = height;
	}
	*/
	/*
	 @Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        final int instriwidth = width;
	        final int instriheight = height;
	        Log.i("width",String.valueOf(width));
	        Log.i("height",String.valueOf(height));
			int widthMode = MeasureSpec.getMode(widthMeasureSpec);
			int widthSize = MeasureSpec.getSize(widthMeasureSpec);
			int heightMode = MeasureSpec.getMode(heightMeasureSpec);
			int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            Log.i("heightsize",String.valueOf(heightSize));
            
			int width;
			int height;

			if (widthMode == MeasureSpec.EXACTLY) {
				width = widthSize;
			} else if (widthMode == MeasureSpec.AT_MOST) {
				width = Math.min(instriwidth, widthSize);
			} else {
				width = instriwidth;
			}

			if (heightMode == MeasureSpec.EXACTLY) {
				height = heightSize;
			} else if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(instriheight, heightSize);
			} else {
				height = instriheight;
			}

		//	int min = Math.min(width, height);
			Log.i("height",String.valueOf(height));
			setMeasuredDimension(width,height);
			
		}
        */
}
