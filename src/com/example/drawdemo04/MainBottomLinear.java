package com.example.drawdemo04;

import java.io.Serializable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainBottomLinear extends LinearLayout{

	private ImageView drawtoolButton;
	private LinearLayout drawtoolLinear;
	private ImageView ColorButton;
	private LinearLayout ColorLinear;
	private ImageView backgroundButton;
	private LinearLayout backgroundLinear;
	private ImageView deleteButton;
	private LinearLayout deleteLinear;
	private ImageView penButton;
	private LinearLayout penLinear;
	
	public MainBottomLinear(Context context,int screenWidth)
	{
		this(context,null);
		initWidget();
		adjustWidget(screenWidth);
	}
	public MainBottomLinear(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.main_bottom, this,true);
	
	}
	private void initWidget()
	{
		drawtoolButton = (ImageView)findViewById(R.id.penOreraser);
		drawtoolButton.setTag(0);
		drawtoolLinear = (LinearLayout)findViewById(R.id.penOreraser_linear);
		drawtoolLinear.setTag(0);
		
		ColorButton = (ImageView) findViewById(R.id.chooseColor);
		ColorButton.setTag(0);
		ColorLinear = (LinearLayout)findViewById(R.id.chooseColor_linear);
		ColorLinear.setTag(0);
		
		backgroundButton = (ImageView)findViewById(R.id.backgroundColor);
		backgroundButton.setTag(0);
		backgroundLinear = (LinearLayout)findViewById(R.id.background_linear);
		backgroundLinear.setTag(0);
		
		deleteButton = (ImageView) findViewById(R.id.unDoOrreDo);
		deleteButton.setTag(0);
		deleteLinear = (LinearLayout)findViewById(R.id.delete_linear);
		deleteLinear.setTag(0);
		
		penButton = (ImageView)findViewById(R.id.pen);
		penButton.setTag(0);
		penLinear = (LinearLayout)findViewById(R.id.pen_linear);
		penLinear.setTag(0);
	}
	
	private void adjustWidget(int screenWidth)
	{
		/*
		LinearLayout.LayoutParams params = (LayoutParams) drawtoolButton.getLayoutParams();
    	int itemWidth = (int)(screenWidth/12.0);
    	params.height = params.width = itemWidth;
    	drawtoolButton.setLayoutParams(params);
    	
       	params = (LayoutParams)penButton.getLayoutParams();
     	itemWidth = (int)(screenWidth/12.0);
    	params.height = params.width = itemWidth;
    	penButton.setLayoutParams(params);
    	
    	params = (LayoutParams) ColorButton.getLayoutParams();
     	itemWidth = (int)(screenWidth/12.0);
    	params.height = params.width = itemWidth;
    	ColorButton.setLayoutParams(params);
    	
    	params = (LayoutParams) backgroundButton.getLayoutParams();
     	itemWidth = (int)(screenWidth/12.0);
    	params.height = params.width = itemWidth;
    	backgroundButton.setLayoutParams(params);
    	
    	params = (LayoutParams) deleteButton.getLayoutParams();
     	itemWidth = (int)(screenWidth/12.0);
    	params.height = params.width = itemWidth;
    	deleteButton.setLayoutParams(params);
    	*/
	}
	
	public void setDrawtoolOnclickListener(OnClickListener onclicklistener)
	{
		drawtoolButton.setOnClickListener(onclicklistener);
		drawtoolLinear.setOnClickListener(onclicklistener);
		
	}
	public void setColorOnclickListener(OnClickListener onclicklistener)
	{
		ColorButton.setOnClickListener(onclicklistener);
		ColorLinear.setOnClickListener(onclicklistener);
	}
	public void setBackgroundOnclickListener(OnClickListener onclicklistener)
	{
		backgroundButton.setOnClickListener(onclicklistener);
		backgroundLinear.setOnClickListener(onclicklistener);
	}
	public void setDeleteOnclickListener(OnClickListener onclicklistener)
	{
		deleteButton.setOnClickListener(onclicklistener);
		deleteLinear.setOnClickListener(onclicklistener);
	}
	public void setPenOnclickListener(OnClickListener onclicklistener)
	{
		penButton.setOnClickListener(onclicklistener);
		penLinear.setOnClickListener(onclicklistener);
	}
	
	public void setDrawtoolTag(int tag)
	{
		drawtoolButton.setTag(tag);
		drawtoolLinear.setTag(tag);
		if((Integer)drawtoolButton.getTag() == 1)
		{
			drawtoolButton.setSelected(true);
			ColorButton.setSelected(false);
			backgroundButton.setSelected(false);
			deleteButton.setSelected(false);
			penButton.setSelected(false);
		}
		else
			drawtoolButton.setSelected(false);
	}
	public void setColorTag(int tag)
	{
		ColorButton.setTag(tag);
		ColorLinear.setTag(tag);
		if((Integer)ColorButton.getTag() == 1)
		{
			drawtoolButton.setSelected(false);
			ColorButton.setSelected(true);
			backgroundButton.setSelected(false);
			deleteButton.setSelected(false);
			penButton.setSelected(false);
		}
		else
			ColorButton.setSelected(false);
	}
	public void setBackgroundTag(int tag)
	{
		backgroundButton.setTag(tag);
		backgroundLinear.setTag(tag);
		if((Integer)backgroundButton.getTag() == 1)
		{
			drawtoolButton.setSelected(false);
			ColorButton.setSelected(false);
			backgroundButton.setSelected(true);
			deleteButton.setSelected(false);
			penButton.setSelected(false);
		}
		else
			backgroundButton.setSelected(false);
	}
	public void setDeleteTag(int tag)
	{
		deleteButton.setTag(tag);
		deleteLinear.setTag(tag);
		if((Integer)deleteButton.getTag() == 1)
		{
			drawtoolButton.setSelected(false);
			ColorButton.setSelected(true);
			backgroundButton.setSelected(false);
			deleteButton.setSelected(true);
			penButton.setSelected(false);
		}
		else
			deleteButton.setSelected(false);
	}
	public void setPenTag(int tag)
	{
		penButton.setTag(tag);
		penLinear.setTag(tag);
		if((Integer)penButton.getTag() == 1)
		{
			drawtoolButton.setSelected(false);
			ColorButton.setSelected(false);
			backgroundButton.setSelected(false);
			deleteButton.setSelected(false);
			penButton.setSelected(true);
		}
		else
			penButton.setSelected(false);
	}
    public boolean isColorSelected()
    {
    	if((Integer)(ColorButton.getTag()) == 1 || (Integer)(ColorLinear.getTag()) == 1)
    		return true;
    	else
    		return false;
    }
}
