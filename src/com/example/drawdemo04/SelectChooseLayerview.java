package com.example.drawdemo04;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.example.drawdemo04.PenGraphycal.DisplayView;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;
import com.example.drawdemo04.initModel.Init_choose;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

public class SelectChooseLayerview extends PopupWindow
{
    private Button recover;
    private Button save;
    private DynamicListView mDynamicListView;
    private LayerAdapter layeradatper;

	private View LayerView;
	private LayerView display;
//	private ScrollView sView;
///	private Activity context;
	private Activity activity;
	private RelativeLayout relativelayout;
	private LinearLayout layout;
	private LinearLayout layerlinear;
	private LinearLayout.LayoutParams params;
	private Bitmap backbitmap = null;
	private Bitmap floorbitmap = null;
	private Handler handler;
	private OnClickListener onclick;
	private OnTouchListener onTouch;
	private int initleft;
    public ArrayList<LayerView> layerViews;

	public SelectChooseLayerview(Activity context,
			int width,int height,Bitmap bitmap,Bitmap floorbitmap)
	{
		super(context);
		Log.i("selectChooselayerview","start");
		this.activity = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LayerView = inflater.inflate(R.layout.layer_view, null);
        this.onclick = onclick;
        this.onTouch = onTouch;
        
		this.setContentView(LayerView);
		
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
	   
	
        relativelayout = (RelativeLayout)LayerView.findViewById(R.id.title);
   //	LinearLayout.LayoutParams params = (LayoutParams) relativelayout.getLayoutParams();
	//	params.height = (int)(height*0.1);
	//	relativelayout.setLayoutParams(params);
		
        Tools.mheight = relativelayout.getHeight();
	
		save = (Button)LayerView.findViewById(R.id.save);
		recover = (Button)LayerView.findViewById(R.id.recover);
	    
		judgeSaveVisibility();
		judgeRecoverVisibility();
		
	    layerlinear = (LinearLayout)LayerView.findViewById(R.id.layerlinear);
	    
	    Log.i("SelectChooseLayerview","beforeCompose");
		composeBackbitmap(bitmap,floorbitmap,(int)(width*0.8), (int)(height*0.25));
	 
		Log.i("SelectChooseLayerview","beforeCompose");
		layerViews = getLayerViews(width, height);

		mDynamicListView = (DynamicListView)LayerView.findViewById(R.id.dynamiclistview);
		
		layeradatper = new LayerAdapter(layerViews, context, width, height);
		mDynamicListView.setAdapter(layeradatper);
		mDynamicListView.enableSwipeToDismiss(new OnDismissCallback() {
	        @Override
	        public void onDismiss(final ViewGroup listView, final int[] reverseSortedPositions) {
	            for (int position : reverseSortedPositions) {

	            	 if(activity != null)
	            		 ((MainActivity)activity).removeLayerView(position-2);
	            	 removeView(position);
	            	 layeradatper.remove(position);
                     judgeRecoverVisibility();
	            }
	        }
	    });
		
	}
	public void setSaveButVisibility()
	{
		judgeSaveVisibility();
	}
	public void setRecoverVisibility()
	{
		judgeRecoverVisibility();
	}
	private void judgeSaveVisibility()
	{
		if(UnDoDraw.saved1.size() > 0 && ((MainActivity)activity).getTag() != -2)
		{
			if(save.getVisibility() == View.GONE)
			{
				Animation animation = AnimationUtils.loadAnimation(activity, R.anim.in_righttoleft);
				save.startAnimation(animation);
				save.setVisibility(View.VISIBLE);
			}
		}
		else
		{
			if(save.getVisibility() == View.VISIBLE)
			{
				Animation animation = AnimationUtils.loadAnimation(activity, R.anim.out_lefttoright1);
				save.startAnimation(animation);
				save.setVisibility(View.GONE);
			}	
		}
	}
	private void judgeRecoverVisibility()
	{
		if(Tools.traceRecord.size() > 0)
		{
			if(recover.getVisibility() == View.GONE)
			{
				Animation animation = AnimationUtils.loadAnimation(activity, R.anim.in_lefttoright);
				recover.startAnimation(animation);
				recover.setVisibility(View.VISIBLE);
			}			
		}
		else
		{
			if(recover.getVisibility() == View.VISIBLE)
			{
				Animation animation = AnimationUtils.loadAnimation(activity, R.anim.out_righttoleft1);
				recover.startAnimation(animation);
				recover.setVisibility(View.GONE);
			}	
		}
	}
	
	public void setSaveOnclickListener(OnClickListener save_onClick)
	{
		save.setOnClickListener(save_onClick);
	}
	public void setRecoverOnClickListener(OnClickListener recover_onClick)
	{
		 recover.setOnClickListener(recover_onClick);
	}
	public void setOnItemClickListener(OnItemClickListener onItemclick)
	{
		mDynamicListView.setOnItemClickListener(onItemclick);
	}
    public void setActivity(Activity activity)
    {
    	this.activity = activity;
    }
    private ArrayList<LayerView> getLayerViews(int width,int height)
    {
    	/*
    	initleft = (int)(width*0.1);
    	LinearLayout layout = new LinearLayout(context);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    params.gravity = Gravity.CENTER_HORIZONTAL;
	    layout.setLayoutParams(params);
	    
	    LayerView layer = new LayerView(context, (int)(width*0.8), (int)(height*0.25),-2,backbitmap,floorbitmap,begin);
		layer.setTag(-2);
		params = new LinearLayout.LayoutParams( (int)(width*0.8),(int)(height*0.25));
		params.rightMargin = (int)(width*0.1);
		params.leftMargin = (int)(width*0.1);		
		layout.addView(layer, params);
	    layer.setOnClickListener(onclick);
	    layer.setOnTouchListener(onTouch);
	    
	    
	    layer = new LayerView(context, (int)(width*0.8), (int)(height*0.25),-1,backbitmap,floorbitmap,begin);
		layer.setTag(-1);
		params = new LinearLayout.LayoutParams( (int)(width*0.8),(int)(height*0.25));
		params.rightMargin = (int)(width*0.1);
		params.leftMargin = (int)(width*0.1);
		layout.addView(layer, params);
	    layer.setOnClickListener(onclick);
	    layer.setOnTouchListener(onTouch);
	    */
    	ArrayList<LayerView> layerViews = new ArrayList<LayerView>();
    	
    	LayerView layer = new LayerView(activity, (int)(width*0.8), (int)(height*0.25),-2,backbitmap,floorbitmap);
    	layer.setTag(-2);
    	layerViews.add(layer);
    	
    	layer = new LayerView(activity, (int)(width*0.8), (int)(height*0.25),-1,backbitmap,floorbitmap);
    	layer.setTag(-1);
    	layerViews.add(layer);
    	
    	for(int i=0;i<UnDoDraw.saved.size();i++)
		{
			layer = new LayerView(activity, (int)(width*0.8), (int)(height*0.25),i,backbitmap,floorbitmap);
			layer.setTag(i);
			layerViews.add(layer);				
		}    
    	Log.i("SelectChooseLayerview",String.valueOf(layerViews.size()));
	    return layerViews;
    }
	private void composeBackbitmap(Bitmap back,Bitmap floor,int width,int height)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    if(back == null)
	    {
	    	backbitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.bsc, options);
	    	if(options.outHeight/height>=options.outWidth/width)
		    {
		    	options.inSampleSize = (int)(options.outHeight/(float)height);
		    	
		    }
		    else 
		    {
		    	options.inSampleSize = (int)(options.outWidth/(float)width);
		    }

		    options.inDither = false;
		    options.inPreferredConfig = Tools.config;
		    options.inPurgeable = true;
		    options.inInputShareable = true;
		    options.inJustDecodeBounds = false;
		    
	        backbitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.bsc,options);
	        Bitmap bit = Tools.zoomImage(backbitmap, width, height);
	        backbitmap.recycle();
	        backbitmap = bit;
	    }
	    else
	    {
	    	backbitmap = Tools.zoomBitmap(back, width*0.96, height*0.96);
	    }
	    if(floor != null)
	    	floorbitmap = Tools.zoomBitmap(floor, width*0.96, height*0.96);
	}
	
    public void removeView(int position)
    {
    	
    	if(position == 0)
    	{
    		for(int i=0;i<layerViews.size();i++)
    		{
    			Init_choose.asynloader.removeBitmapFromCache(String.valueOf(i));
    		}
    		layerViews.clear();
    	}
    	
    	if(position >= 2)
    	{
    		for(int i=position+1;i<layerViews.size();i++)
    		{
    			int tag = (Integer)(layerViews.get(i).getTag());
    			layerViews.get(i).setTag(tag - 1);
    		}
    		if(layerViews.get(0) != null)
    			layerViews.get(0).drawLayerBitmap();
    	}
    }
    public int getChildCount()
    {
    	if(layout != null)
    		return layout.getChildCount();
    	return 0;
    }
    
    public void addView(int i,int width,int height,int begin)
    {
    	layerViews.get(0).drawLayerBitmap();
    	
    	LayerView layer = new LayerView(activity, (int)(width*0.8), (int)(height*0.25),i-2,backbitmap,floorbitmap);
		layer.setTag(i-2);
		
		layerViews.add(i,layer);
		for(int j=i+1;j<layerViews.size();j++)
		{
			layerViews.get(j).setTag(j-2);
		}
		Log.i("SelectChooseLayerview","addView");
		Log.i("addView",String.valueOf(i));
		mDynamicListView.insert(i, layer);
		
    }
    
    public void recoverPosition(int start,int num)
    {
    	for(int i=0;i<num;i++)
    	{
    		int inittop = layout.getChildAt(i+start+2).getTop();
    		layout.getChildAt(i+start+2).layout(initleft, inittop, 
    				initleft + layout.getChildAt(i+start+2).getWidth(), inittop + layout.getChildAt(i+start+2).getHeight());
    				
    	}
    }
    public View getView(int index)
    {
    	if(layout.getChildCount() > index)
    		return layout.getChildAt(index);
    	return null;
    }
}
