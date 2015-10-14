package com.example.drawdemo04.PenGraphycal;

import java.util.Timer;
import java.util.TimerTask;

import com.example.drawdemo04.BottomSlideView;
import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class WriteTextPopupwindow extends PopupWindow{
    
	public final int back_return = 0;
	public final int unsure_return = 1;
	public final int sure_return = 2;
	public int returnType;
	private View writeTextview;
	private Button sure;
	private Button delete;
	private TextView head_text;
	private EditText text_input;
	private RelativeLayout textLayout;
	private MainActivity activity;
	
	public WriteTextPopupwindow(Activity context,OnClickListener onclick,int screenWidth,int screenHeight){
		
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		writeTextview = inflater.inflate(R.layout.write_comment, null);
		activity = (MainActivity) context;
		
		this.setContentView(writeTextview);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT); 
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationFadefromleft);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		
		sure = (Button)writeTextview.findViewById(R.id.head_offer);
		delete = (Button)writeTextview.findViewById(R.id.head_cancel);
		head_text = (TextView)writeTextview.findViewById(R.id.head_text);
		text_input = (EditText)writeTextview.findViewById(R.id.commentEdit);
		/*
		textLayout = (RelativeLayout)writeTextview.findViewById(R.id.commentHeadBar);
		RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams)textLayout.getLayoutParams();
	    params.height = (int)(screenHeight*0.13);
	    textLayout.setLayoutParams(params);
	    
		params =(RelativeLayout.LayoutParams)sure.getLayoutParams();
		
		params.topMargin = (int)(screenHeight*0.13*0.5 - sure.getHeight()*0.5);
		sure.setLayoutParams(params);
		*/
		sure.setText(context.getString(R.string.sure));
		delete.setText(context.getString(R.string.delete1));
		head_text.setText(context.getString(R.string.text));
		
		sure.setOnClickListener(onclick);
		delete.setOnClickListener(onclick);
		
	    text_input.setFocusable(true);
	    text_input.setFocusableInTouchMode(true);
	    text_input.requestFocus();
	    	
	    Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
			@Override
			public void run() {
					// TODO 自动生成的方法存根
				InputMethodManager inputManager = (InputMethodManager)text_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			    inputManager.showSoftInput(text_input, 0);
			}
		}, 600);
	}
	
	public String getText(){
		return text_input.getText().toString().trim();
	}
	
	public void setReturnType(int type)
	{
		this.returnType = type;
	}
	
	@Override
	public void dismiss()
	{
		if(returnType != sure_return)
		{
			activity.choosed_graphic = activity.last_graphic;
			BottomSlideView bottomSlider = activity.getBottomSliderView();
			if(bottomSlider != null)
			{
				if(activity.choosed_graphic == 6)
					bottomSlider.setGraphyicalButtonResource(1, 0);
				else
					bottomSlider.setGraphyicalButtonResource(1, activity.choosed_graphic + 2);
			}
			    
		}
		if(returnType != back_return)
		{
			InputMethodManager imm = (InputMethodManager)text_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
		}
		
		super.dismiss();
		
	}
}
