package com.example.drawdemo04.autoset_widget;

import com.example.drawdemo04.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocalPhoto_Button extends RelativeLayout{

	private ImageView image;
	private TextView textview;
	
	public LocalPhoto_Button(Context context) {

		this(context,null);
		// TODO 自动生成的构造函数存根
	}

	public LocalPhoto_Button(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.local_choose, this, true);
		image = (ImageView)findViewById(R.id.image);
		textview = (TextView)findViewById(R.id.text);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image.getLayoutParams();
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		params.leftMargin = (int)(widgtWidth*0.5);
		image.setLayoutParams(params);
		
		params = (RelativeLayout.LayoutParams) textview.getLayoutParams();
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		params.leftMargin = (int)(widgtWidth*0.5);
		textview.setLayoutParams(params);
	}
	public void setImageSrc(int resId)
	{
		image.setImageResource(resId);
	}
	public void setText(String text)
	{
		textview.setText(text);
	}
}
