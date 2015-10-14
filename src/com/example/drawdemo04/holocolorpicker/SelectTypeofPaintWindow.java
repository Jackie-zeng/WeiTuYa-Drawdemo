package com.example.drawdemo04.holocolorpicker;

import com.example.drawdemo04.holocolorpicker.ColorPicker;
import com.example.drawdemo04.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.example.drawdemo04.holocolorpicker.OpacityBar;
import com.example.drawdemo04.holocolorpicker.SVBar;
import com.example.drawdemo04.R;
import com.example.drawdemo04.PenSize.PenSizeBar;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
 
public class SelectTypeofPaintWindow extends PopupWindow{
	
	private View graphycalView;
	private ColorPicker picker;
	private SVBar svBar;
	private OpacityBar opacityBar;
	
	public SelectTypeofPaintWindow(Context context,OnColorChangedListener changedlistener)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		graphycalView = inflater.inflate(R.layout.color_dialog, null);
		
		this.setContentView(graphycalView);
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
		
		picker = (ColorPicker)graphycalView.findViewById(R.id.picker);
		svBar = (SVBar)graphycalView.findViewById(R.id.svbar);
		opacityBar = (OpacityBar)graphycalView.findViewById(R.id.opacitybar);
		picker.setOnColorChangedListener(changedlistener);
	}
	
}

