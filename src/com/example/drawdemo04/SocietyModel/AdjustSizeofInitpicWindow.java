package com.example.drawdemo04.SocietyModel;

import com.example.drawdemo04.AdjustView;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.initModel.Init_choose;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class AdjustSizeofInitpicWindow extends PopupWindow{

	private View adjustSizeview;
	private RelativeLayout relayout;
	private AdjustView adjustView;
	public Context context;
	private int screenWidth,screenHeight,viewHeight,viewWidth;
	private Button sure,cancel;
	private RecyclingBitmapDrawable value;
	private Bitmap backBit;
	private ProgressBar mprogress;
	
	public AdjustSizeofInitpicWindow(int which,Context context,RecyclingBitmapDrawable value,OnClickListener sure_onClick,OnClickListener cancel_onClick,final int viewWidth,final int viewHeight,int screenHeight)
	{
		super(context);
		this.context = context;
		
		DisplayMetrics dm=new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.screenWidth=dm.widthPixels;                  // фад╩©М
		this.screenHeight = screenHeight;
		
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
		this.value = value;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		adjustSizeview = inflater.inflate(R.layout.adjust_size, null);
		
		relayout = (RelativeLayout)adjustSizeview.findViewById(R.id.adjustSizeLayout);
		this.setContentView(adjustSizeview);
		this.setWidth(LayoutParams.FILL_PARENT); 
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setFocusable(true);
		
		if(which == 0)
			this.setAnimationStyle(R.style.AnimationFade1);
		else
			this.setAnimationStyle(R.style.AnimationFadefromleft);
		
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);	
		
		adjustBarSize();
		
		if(value != null)
		{
			displayAjustView(value);
		}
		else
		{
			mprogress = new ProgressBar(context);
			relayout.removeAllViews();
			float scale = context.getResources().getDisplayMetrics().density;

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Tools.Dp2Px(60, scale), 
					Tools.Dp2Px(60, scale));
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			relayout.addView(mprogress, params);
			mprogress.setVisibility(View.VISIBLE);
		}
		Log.i("AdjustSizeofWindow","onCreate");
		sure.setOnClickListener(sure_onClick);
		cancel.setOnClickListener(cancel_onClick);
	
	}
	private void adjustBarSize()
	{
		/*
		RelativeLayout backlayout = (RelativeLayout)adjustSizeview.findViewById(R.id.undergroundBar);
		RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) backlayout.getLayoutParams();
    	params.height = (int)(screenHeight*0.1);
    	backlayout.setLayoutParams(params);
    	*/
    	
    	sure = (Button)adjustSizeview.findViewById(R.id.sure);
    	cancel = (Button)adjustSizeview.findViewById(R.id.cancel);
    	
		sure.setWidth((int)(screenWidth*0.5));
//		sure.setHeight((int)(screenHeight*0.1));
		cancel.setWidth((int)(screenWidth*0.5));
//		cancel.setHeight((int)(screenHeight*0.1));
	}
	
	public void displayAjustView(RecyclingBitmapDrawable value)
	{
		if(value != null && value.getBitmap() != null)
		{
			this.value = value;
			adjustView = new AdjustView(context, value.getBitmap().getWidth(), value.getBitmap().getHeight(),value);
			adjustView.setBackgroundColor(Color.TRANSPARENT);
//			adjustView.setImageBitmap(bitmap);
			
			relayout.removeAllViews();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, 
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			
	        Tools.initWidth = value.getBitmap().getWidth();
	        Tools.initHeight = value.getBitmap().getHeight();
	        
			params.topMargin = (int)(screenHeight*0.5 - value.getBitmap().getHeight()*0.5);
			params.leftMargin = (int)(screenWidth*0.5 - value.getBitmap().getWidth()*0.5);
			relayout.addView(adjustView,params);
			
			float rate = (float)viewWidth/viewHeight;
			int mheight = (int)(value.getBitmap().getHeight()*0.5);
			int mwidth = (int)(mheight*rate);
			
			adjustView.auto_setAdjustImg(0, 0, mwidth, mheight);
		}
		else
			relayout.removeAllViews();
	}
	public String finishAdjustImg()
	{
		return adjustView.auto_finishAdjustImg(viewWidth,viewHeight);
	}
	
	@Override 
	public void dismiss()
	{
		if(adjustView != null)
			adjustView.recycle();
	//	value.setIsDisplayed(false);
		super.dismiss();
//		Society_gallery.isOver = true;
	}
	
}
