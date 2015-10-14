package com.example.drawdemo04.SocietyModel;

import java.util.ArrayList;
import java.util.List;

import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.holocolorpicker.ColorPicker;
import com.example.drawdemo04.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.example.drawdemo04.holocolorpicker.OpacityBar;
import com.example.drawdemo04.holocolorpicker.SVBar;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenSize.PenSizeBar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class Society_set extends PopupWindow{
	
	private View societysetView;
	private Context context;
	private RelativeLayout buttonRelative;
	private RelativeLayout titleRelative;
	private RelativeLayout.LayoutParams buttParams;
//	private LinearLayout.LayoutParams buttParams;
	private int screenHeight;
	private int adpterWidth;
	public Button submit;
	private Button ThemeSet;
	public Button localSave;
	private Button localClear;
	private Button remoteUpload;
//	private ImageView iconimage;
	private RecyclingImageView iconimage;
	private TextView  nickname;
	private String name;
	private ProgressBar progress;
	
	public Society_set(Context context,OnClickListener submit_onclickListener,OnClickListener themeSet_onclickListener,OnClickListener localdelete_onclickListener,OnClickListener localsave_onClick,OnClickListener remoteUpload_onclick,OnLongClickListener titleset_onLongClick,OnLongClickListener text_onLongClick,int width,int height) 
	{
		super(context);
		this.context = context;
		screenHeight = height;
		adpterWidth = width;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 

		societysetView = inflater.inflate(R.layout.my_society, null);
		 
		this.setContentView(societysetView);
		
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
		
		iconimage = (RecyclingImageView)societysetView.findViewById(R.id.iconImage);
		iconimage.setImageResource(R.drawable.renma);
		nickname = (TextView)societysetView.findViewById(R.id.nickname);
		
		SharedPreferences mySharePreference = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		String key = Tools.AccountName + Tools.identity.substring(Tools.identity.indexOf("_") + 1); 
		String name = mySharePreference.getString(key, "localUser");
		nickname.setText(name);
//      progress = (ProgressBar)societysetView.findViewById(R.id.progressDisplay);
	//  	nickname.setText(name);
		
		
		buttonRelative = (RelativeLayout)societysetView.findViewById(R.id.setLayout);
		titleRelative = (RelativeLayout)societysetView.findViewById(R.id.title);
		
		/*
		RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) titleRelative.getLayoutParams();
		Log.i("screenHeight2",String.valueOf(screenHeight));
		params.height = (int)(screenHeight*0.13);
		titleRelative.setLayoutParams(params);
	    */
		
		progress = (ProgressBar)societysetView.findViewById(R.id.progressDisplay);
		
	    submit = new Button(context);
	    localSave = new Button(context);
	    localClear = new Button(context);
	    ThemeSet = new Button(context);
	    remoteUpload = new Button(context);
	    
	    initView(); 
	    
	    localClear.setOnClickListener(localdelete_onclickListener);
	    localSave.setOnClickListener(localsave_onClick);
	    submit.setOnClickListener(submit_onclickListener);
	    ThemeSet.setOnClickListener(themeSet_onclickListener);
	    remoteUpload.setOnClickListener(remoteUpload_onclick);
	    nickname.setOnLongClickListener(text_onLongClick);
	/*	
		buttParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		buttParams.topMargin = (int)(screenHeight*0.45);
		comment.setBackgroundResource(R.drawable.search_bar_edit_normal);
		comment.setText(R.string.comment);
		buttonRelative.addView(comment, buttParams);
		
		buttParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		buttParams.topMargin = (int)(screenHeight*0.6);
		localSave.setBackgroundResource(R.drawable.search_bar_edit_normal);
		localSave.setText(R.string.localsave);
		buttonRelative.addView(localSave, buttParams);
		
		buttParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		buttParams.topMargin = (int)(screenHeight*0.75);
		localDelete.setBackgroundResource(R.drawable.search_bar_edit_normal);
		localDelete.setText(R.string.localdelete);
		buttonRelative.addView(localDelete, buttParams);
  */
	    iconimage.setOnLongClickListener(titleset_onLongClick);
	}
	
	 private void initView()
	 {
		 initwidget(submit,R.string.submit,(int)(screenHeight*0.3));
		 initwidget(localSave, R.string.localsave, (int)(screenHeight*0.45));
		 initwidget(localClear, R.string.localdelete, (int)(screenHeight*0.6));
		 initwidget(ThemeSet, R.string.themeSet, (int)(screenHeight*0.75));
		 initwidget(remoteUpload, R.string.remote_upload, (int)(screenHeight*0.9)); 
	 }
	 private void initwidget(Button button,int stringId,int topmargin)
	 {
		 buttParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 buttParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		 buttParams.leftMargin = (int)(adpterWidth*0.5-adpterWidth*0.75*0.5);
		 buttParams.topMargin = topmargin;
		 
		 button.setBackgroundResource(R.drawable.search_bar_edit_normal);
		 button.setText(stringId);
		 button.setWidth((int)(adpterWidth*0.75));
		 button.setHeight((int)(screenHeight*0.08));
		 button.setBackgroundResource(R.drawable.society_button);
		 buttonRelative.addView(button, buttParams);
	 }
     public void setName(String name){
    	 
    	 nickname.setText(name);
    	 SharedPreferences.Editor editor = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE).edit();
    	 String key = Tools.AccountName + Tools.identity.substring(Tools.identity.indexOf("_") + 1); 
    	 editor.putString(key, name);
    	 editor.commit();
     }
     public void setTitleImg(RecyclingBitmapDrawable value){
    	 if(iconimage != null)
    		 iconimage.setImageDrawable(value);
     }
     public int getTitleImgLength()
     {
     	final float scale = context.getResources().getDisplayMetrics().density;
     	return Tools.Dp2Px(70, scale);
     }
     public void progressDisplay()
     {
    	 if(progress.getVisibility() == View.GONE)
    		 progress.setVisibility(View.VISIBLE);
     }
     public void progressDismiss()
     {
    	 if(progress.getVisibility() == View.VISIBLE)
    		 progress.setVisibility(View.GONE);
     }
}

