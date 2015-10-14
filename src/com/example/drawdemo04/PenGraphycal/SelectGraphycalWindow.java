package com.example.drawdemo04.PenGraphycal;

import java.util.ArrayList;
import java.util.List;

import com.example.drawdemo04.holocolorpicker.ColorPicker;
import com.example.drawdemo04.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.example.drawdemo04.holocolorpicker.OpacityBar;
import com.example.drawdemo04.holocolorpicker.SVBar;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.PenSize.PenSizeBar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
 
public class SelectGraphycalWindow extends PopupWindow{
	
	private View graphycalView;
	private DisplayView display;
	private ExpandableListView graphycallist;
	private ArrayList<String> graphygroupdata=new ArrayList<String>();
	private ArrayList<ArrayList<String>> graphychilddata = new ArrayList<ArrayList<String>>();
	private Context context;
	private LinearLayout linearlayout;
	private BaseExpandableListAdapter adapter;

	public SelectGraphycalWindow(Context context,OnChildClickListener onChildClickListener,int width,int height)
	{
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		graphycalView = inflater.inflate(R.layout.graphycal_dialog, null);
		Log.i("graphycal2","window");
		this.setContentView(graphycalView);
		
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT); 
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationFade1);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		 
		display = new DisplayView(context,width,(int)(height*1/3));
		display.setBackgroundColor(Color.TRANSPARENT);
		
		linearlayout = (LinearLayout)graphycalView.findViewById(R.id.graphycalLinear);
		linearlayout.removeAllViews();
		linearlayout.addView(display);
	
		getData();
		graphycallist = (ExpandableListView)graphycalView.findViewById(R.id.expandableListview);
	//	ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,
	//			getData()); 
	    adapter = new ExpandableAdapter(height);
		graphycallist.setAdapter(adapter);
	
	    graphycallist.setOnChildClickListener(onChildClickListener);
		
		
	}
	
	private void getData(){
		graphygroupdata.add(context.getString(R.string.paintTools));
		graphygroupdata.add(context.getString(R.string.paintStyles));
		
		ArrayList<String> painttools = new ArrayList<String>();
		painttools.add(context.getString(R.string.line));
		painttools.add(context.getString(R.string.rectangle));
		painttools.add(context.getString(R.string.circle));
		painttools.add(context.getString(R.string.triangle));
		painttools.add(context.getString(R.string.cube));
		painttools.add(context.getString(R.string.column));
		painttools.add(context.getString(R.string.draw));
		painttools.add(context.getString(R.string.writetext));
	    graphychilddata.add(painttools);
	    
	    ArrayList<String> paintstyles = new ArrayList<String>();
	    paintstyles.add(context.getString(R.string.pencil));
	    paintstyles.add(context.getString(R.string.qianbimohu));
	    paintstyles.add(context.getString(R.string.maobifudiao));
	    graphychilddata.add(paintstyles);
	    
	}
	public void chooseGraphy(int value){	
		display.setDrawTool(value,null);
	}
	
	private class ExpandableAdapter extends BaseExpandableListAdapter
	{
        int height;
        
		public ExpandableAdapter(int height){
			this.height = height;
		}
		@Override
		public Object getChild(int groupposition, int childposition) {
			// TODO 自动生成的方法存根
			return graphychilddata.get(groupposition).get(childposition);
		}

		@Override
		public long getChildId(int groupposition, int childposition) {
			// TODO 自动生成的方法存根
			return childposition;
		}

		@Override
		public View getChildView(int groupposition, int childposition, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO 自动生成的方法存根
			String string = graphychilddata.get(groupposition).get(childposition);
			return getGeneralView(string);
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO 自动生成的方法存根
			return graphychilddata.get(arg0).size();
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO 自动生成的方法存根
			return graphygroupdata.get(arg0);
		}

		@Override
		public int getGroupCount() {
			// TODO 自动生成的方法存根
			return graphygroupdata.size();
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO 自动生成的方法存根
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO 自动生成的方法存根
			String string = graphygroupdata.get(arg0);
			return getGeneralView(string);
		}

		@Override
		public boolean hasStableIds() {
			// TODO 自动生成的方法存根
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO 自动生成的方法存根
			return true;
		}
		
		public TextView getGeneralView(String string){
			AbsListView.LayoutParams layoutparams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,(int)(height*0.1) );
			TextView text = new TextView(context);
			text.setLayoutParams(layoutparams);
			text.setTextSize(20);
			text.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
		//	text.setPadding(, 0, 0, 0);
			text.setText(string);
			return text;
		}
	}
}

