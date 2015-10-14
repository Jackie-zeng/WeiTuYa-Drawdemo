package com.example.drawdemo04.SocietyModel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.PenSize.PenSizeBar;
import com.example.drawdemo04.PenSize.PenSizeBar.OnThicknessChangedListener;
import com.example.drawdemo04.autoset_widget.LocalPhoto_Button;
import com.example.drawdemo04.autoset_widget.Undo_Button;
import com.example.drawdemo04.initModel.Init_choose;

public class DeleteLocalpicWindow extends PopupWindow{

	 private Activity context;
	 private View deleteView;
	 private Button delete_butt,giveup_butt;

	 public DeleteLocalpicWindow(Activity context,int windowHeight,OnClickListener deletepic_onClick,OnClickListener giveup_onClick) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		deleteView = inflater.inflate(R.layout.delete_localimg, null);   
		
		this.setContentView(deleteView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT); 
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
	//	this.setFocusable(true);   //点击屏幕的其他地方popwindow 都会消失
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw); 
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		
		delete_butt = (Button)deleteView.findViewById(R.id.deletePic);
		giveup_butt = (Button)deleteView.findViewById(R.id.giveup);
		
		delete_butt.setHeight((int)(windowHeight*0.3));
		giveup_butt.setHeight((int)(windowHeight*0.3));
		
		delete_butt.setOnClickListener(deletepic_onClick);
		giveup_butt.setOnClickListener(giveup_onClick);
	 }
	 
	 @Override 
	 public void dismiss()
	 {
		 super.dismiss();
		 ArrayList<PicInfo> picInfos = ((Society_gallery)context).getPicInfoList();
		 int longChoosed = ((Society_gallery)context).longChoosed;
		 
		 if(picInfos != null && picInfos.size() > longChoosed)
			 Init_choose.asynloader.removeBitmapFromCache(picInfos.get(longChoosed).getLittlePath());
		 
		 ((Society_gallery)context).getGridView().setEnabled(true);
	 }
}
