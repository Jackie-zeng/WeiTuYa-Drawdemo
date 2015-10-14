package com.example.drawdemo04;

import java.util.Timer;
import java.util.TimerTask;

import com.example.drawdemo04.PenSize.PenSizeBar;
import com.example.drawdemo04.PenSize.PenSizeBar.OnThicknessChangedListener;
import com.example.drawdemo04.autoset_widget.LocalPhoto_Button;
import com.example.drawdemo04.autoset_widget.Undo_Button;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow; 
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectPicPopupWindow extends PopupWindow {
	private View mMenuView;
    private LayoutParams eraserParams;
    private LayoutParams penParams;
    private LayoutParams backParams;
    private LayoutParams deleteParams;
    private RelativeLayout.LayoutParams params;
    private android.widget.RelativeLayout.LayoutParams testParams;
    private RelativeLayout testrelative;
    private Button testbut;
    private LinearLayout eraser;
    private LinearLayout pen;
    private LinearLayout alllayout;
    private LinearLayout backgroundLinear;
    private LinearLayout deleteLinear;
    private int menuPadding=0;
    private int leftMax;
    private int rightMax;
	private static final int speed = 120;
	private int displayWidth;
	private int adapterHeight;
	private ImageButton morebut;
	private PenSizeBar pensizeBar;
	private PenSizeBar eraserBar;
	private PenSizeBar pentransparencyBar;
	private TextView pensize;
	private int endX,startX;
	private int mwhich;
	
	private LocalPhoto_Button localChoose;
	private LocalPhoto_Button societyChoose;
	private Undo_Button undoChoose;
	private Undo_Button redoChoose;
	private Undo_Button deleteChoose;
	private Handler handler;
	private Context context;
	private MyView myview;
    private Activity activity;
  
        public SelectPicPopupWindow(Context context,OnClickListener itemsOnClick,OnClickListener local_onClick,OnClickListener society_onClick, OnThicknessChangedListener thickness_onChanged,
        		OnThicknessChangedListener transparency_onChanged,OnThicknessChangedListener eraserthickness_onChanged,int displayWidth,final int which,int adapterHeight,MyView myView) {
		super(context);
		this.mwhich = which;
		this.context = context;
		this.adapterHeight = adapterHeight;
		myview = myView;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_dialog, null);
        alllayout = (LinearLayout)mMenuView.findViewById(R.id.penLayout);
              
        this.displayWidth=displayWidth;
      
   //   morebut = (ImageButton)mMenuView.findViewById(R.id.morePic);
        pensize = (TextView)mMenuView.findViewById(R.id.penSize);
        pensizeBar = (PenSizeBar)mMenuView.findViewById(R.id.penSizebar);

        if(MainActivity.graphicOrtext == MainActivity.GRAPHY)
        {
        	pensizeBar.setMaximum(50);
        	pensize.setText(context.getString(R.string.paintsize));
        }
        else
        {
        	pensizeBar.setMaximum(200);
        	pensize.setText(context.getString(R.string.textsize));
        }
        
        
        pentransparencyBar = (PenSizeBar)mMenuView.findViewById(R.id.penTransparencybar);
        pentransparencyBar.setMaximum(200);
        
        eraserBar = (PenSizeBar)mMenuView.findViewById(R.id.eraserSizebar);
        eraserBar.setMaximum(60);
        
        localChoose = (LocalPhoto_Button)mMenuView.findViewById(R.id.localChoose);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) localChoose.getLayoutParams();
        params.width = (int)(displayWidth*0.25);
        params.height = (int)(adapterHeight*0.7);
        localChoose.setLayoutParams(params);
        
        localChoose.setImageSrc(R.drawable.ic_camera);
        localChoose.setText(context.getString(R.string.localGallery));
        
        societyChoose = (LocalPhoto_Button)mMenuView.findViewById(R.id.societyChoose);
        params = (RelativeLayout.LayoutParams) societyChoose.getLayoutParams();
        params.width = (int)(displayWidth*0.25);
        params.height = (int)(adapterHeight*0.7);
        societyChoose.setLayoutParams(params);
        
        societyChoose.setImageSrc(R.drawable.ic_gallery);
        societyChoose.setText(context.getString(R.string.societyGallery));
        
        undoChoose = (Undo_Button)mMenuView.findViewById(R.id.undoChoose);
        params = (RelativeLayout.LayoutParams) undoChoose.getLayoutParams();
        params.width = (int)(displayWidth*0.2);
        params.height = params.width;
        undoChoose.setLayoutParams(params);
        
        undoChoose.setImageSrc(R.drawable.ic_undo_back);
        undoChoose.setText(context.getString(R.string.undo));
        
        redoChoose = (Undo_Button)mMenuView.findViewById(R.id.redoChoose);
        params = (RelativeLayout.LayoutParams)redoChoose.getLayoutParams();
        params.width = (int)(displayWidth*0.2);
        params.height = params.width;
        redoChoose.setLayoutParams(params);
        
        redoChoose.setImageSrc(R.drawable.ic_undo_ahead);
        redoChoose.setText(context.getString(R.string.redo));
        
        deleteChoose = (Undo_Button)mMenuView.findViewById(R.id.deleteChoose);
        params = (RelativeLayout.LayoutParams)deleteChoose.getLayoutParams();
        params.width = (int)(displayWidth*0.2);
        params.height = params.width;
        deleteChoose.setLayoutParams(params);
        
        deleteChoose.setImageSrc(R.drawable.ic_delete);
        deleteChoose.setText(context.getString(R.string.delete));
       
        
		//取消按钮 
		//设置按钮监听
	//	btn_pick_photo.setOnClickListener(itemsOnClick);
	//	btn_take_photo.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View
	   
		init();
	    
	//	initView();
		
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT); 
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);   //点击屏幕的其他地方popwindow 都会消失
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw); 
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		handler = new myHandler();
		
		mMenuView.setOnTouchListener(new OnTouchListener() 
		{
			
			public boolean onTouch(View v, MotionEvent event) 
			{
			
				switch(mwhich)
			   {
				case 0:
				{
					leftMax = -eraserParams.width;
					rightMax = 0;
					movepage0(event);			
					break;
				}
				case 1:
				{
					
					leftMax = -eraserParams.width-penParams.width;
					rightMax = 0;
					movepage1(event);		
					break;
				}
				case 2:
				{
					
					leftMax = -eraserParams.width-penParams.width-backParams.width;
					rightMax = -eraserParams.width;
					movepage2(event);
					break;
				}
				case 3:
				{
					leftMax = -eraserParams.width-penParams.width-backParams.width;
					rightMax = -eraserParams.width-penParams.width;
					movepage3(event);
					break;
				}				
				case 4:
					break;
	
				default:
				    break;
			  }
				return true;
			}
			
				
	});
	
	    
        
		morebut.setOnClickListener(itemsOnClick); 
		pensizeBar.setOnThicknessChangedListener(thickness_onChanged);
		pentransparencyBar.setOnThicknessChangedListener(transparency_onChanged);
		eraserBar.setOnThicknessChangedListener(eraserthickness_onChanged);
		
		localChoose.setOnClickListener(local_onClick);
		societyChoose.setOnClickListener(society_onClick);
		
		undoChoose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(myview != null)
					myview.setDrawTool(7);
			}
		});
		redoChoose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(myview != null)
					myview.setDrawTool(8);
			}
		});
		deleteChoose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(activity != null)
					((MainActivity)activity).deleteView();			
			}
		});
	}
       
	/*
	private void initView(){
		int height = Dp2Px(50);
		Log.i("height",String.valueOf(height));
		Log.i("height1",String.valueOf((int)(height*0.1)));
		Log.i("height2",String.valueOf((int)(height*0.3)));
		Log.i("height3",String.valueOf((int)(height*0.5)));
		Log.i("height4",String.valueOf((int)(height*0.8)));
		
		RelativeLayout layout = (RelativeLayout)mMenuView.findViewById(R.id.penup);
		initwidget(pensizeBar, layout, (int)(height*0.1), false);
		initwidget(pensizeBar, layout, (int)(height*0.3), true);
		layout = (RelativeLayout)mMenuView.findViewById(R.id.penbelow);
		initwidget(pentransparencyBar, layout, (int)(height*0.5), false);
		initwidget(pentransparencyBar, layout, (int)(height*0.8), true);
	}
	*/
        /*
    public void setText(String text){
    	pensize.setText(text);
    }
    public void setSizeMaximum(int size){
    	pensizeBar.setMaximum(200);
    }
    */
    public void setView(MyView myview){
    	this.myview = myview;
    }
    public void setActivity(Activity activity){
    	this.activity = activity;
    }
	private void initwidget(View widget,RelativeLayout layout,int topmargin,boolean which){
		
		if(which == true)
		{
			params = (RelativeLayout.LayoutParams)widget.getLayoutParams();
			params.topMargin = topmargin;
			widget.setLayoutParams(params);
		}
		else
		{
			params = (RelativeLayout.LayoutParams)layout.getLayoutParams();
			params.topMargin = topmargin;
			layout.setLayoutParams(params);
		}		 
	}
	public void setPage(int page){
		mwhich = page; 
		showMenu(mwhich);
	}
	
    private void init()
    {
    	eraser = (LinearLayout)mMenuView.findViewById(R.id.eraserSet);
    	pen = (LinearLayout)mMenuView.findViewById(R.id.penSet);
    	backgroundLinear = (LinearLayout)mMenuView.findViewById(R.id.backgroundSet);
    	deleteLinear = (LinearLayout)mMenuView.findViewById(R.id.deleteSet);
  //  	eraser.setTag(1);
    	
    	eraserParams = (LayoutParams)eraser.getLayoutParams();
    
    	penParams = (LayoutParams)pen.getLayoutParams();
    	backParams = (LayoutParams)backgroundLinear.getLayoutParams();
    	deleteParams = (LayoutParams)deleteLinear.getLayoutParams();
    	
    	eraserParams.width = displayWidth;
    	penParams.width = displayWidth;
    	backParams.width = displayWidth;
    	deleteParams.width = displayWidth;
    	
    	RelativeLayout backgroundRelative = (RelativeLayout)mMenuView.findViewById(R.id.backgroundChoose);
    	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) backgroundRelative.getLayoutParams();
    	params.topMargin = (int)(adapterHeight*0.5 - adapterHeight*0.7*0.5);
    	backgroundRelative.setLayoutParams(params);
    	
    	backgroundRelative = (RelativeLayout)mMenuView.findViewById(R.id.undoback);
    	params = (LinearLayout.LayoutParams) backgroundRelative.getLayoutParams();
    	params.topMargin = (int)(adapterHeight*0.3);
    	backgroundRelative.setLayoutParams(params);
    	showMenu(mwhich);
  	
    }
    private void showMenu(int which)
    {
        switch(which)
        {
        case 0:
        	eraserParams.leftMargin= 0;
   //     	penParams.leftMargin = eraserParams.width;
 //       	backParams.leftMargin = eraserParams.width +penParams.width;    	
        	if(myview != null)
        		myview.setDrawTool(10);
        	break;
        case 1:
        	eraserParams.leftMargin = 0 - eraserParams.width;
  //      	penParams.leftMargin = 0;
  //      	backParams.leftMargin = penParams.width;    
        	if(myview != null)
        	{
        		Log.i("actualrecord",String.valueOf(myview.actualRecord));
        		myview.setDrawTool(myview.actualRecord);
        	}
        	break;
        case 2:
        	eraserParams.leftMargin = 0 - eraserParams.width-penParams.width;
 //       	penParams.leftMargin = 0-penParams.width;
  //      	backParams.leftMargin = 0;
        	break;
        case 3:
        	eraserParams.leftMargin = 0 - eraserParams.width-penParams.width-backParams.width;
        	break;
        case 4:
        	break;
        default :
            break;		
        }
        eraser.setLayoutParams(eraserParams);
   //     pen.setLayoutParams(penParams);
  //      backgroundLinear.setLayoutParams(backParams);    
        
    }
    
    private void movepage0(MotionEvent event)
    {
    	int topheight = mMenuView.findViewById(R.id.eraserSizebar).getTop();
		int bottomheight = mMenuView.findViewById(R.id.eraserSizebar).getBottom();	
		int y=(int) event.getY();
	
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(y<topheight-10 || y>bottomheight+10)
				startX=(int)event.getX();				
			else
				startX=-1000;					
		}
	    if(event.getAction() == MotionEvent.ACTION_UP)
	    {
	    	if(y<topheight-10 || y>bottomheight+10)
	    	{
	    		endX=(int)event.getX();
	    		if(startX-endX>5)
	    		{
	    			new showMenuAsyncTask().execute(-10);
	    			if(myview !=null && activity != null)
	    				myview.setDrawTool(((MainActivity)activity).choosed_graphic);
	    			mwhich=1;	
	    		}
	    	}
	    }
    }
    
    private void movepage1(MotionEvent event)
    {
    	int topheight1 = mMenuView.findViewById(R.id.penSizebar).getTop();
		int bottomheight1 = mMenuView.findViewById(R.id.penSizebar).getBottom();
		int topheight2 = mMenuView.findViewById(R.id.penTransparencybar).getTop();
		int bottomheight2 = mMenuView.findViewById(R.id.penTransparencybar).getBottom();
		
		int y=(int) event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(y<topheight1-10 || (y>bottomheight1+10 && y<topheight2-10))
				startX=(int)event.getX();
		}
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			if(y<topheight1-10 || (y>bottomheight1+10 && y<topheight2-10))
			{
				endX = (int)event.getX();
				if(endX-startX > 5)
				{
					new showMenuAsyncTask().execute(10);   //往左
				    if(myview != null)
				    	myview.setDrawTool(10);
					mwhich=0;
				}
                if(startX-endX>5)
                {
                	new showMenuAsyncTask().execute(-10);
                	mwhich=2;
                	
                }
			}
		} 
    }
    private void movepage2(MotionEvent event)
    {

    	if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		startX=(int)event.getX();
    	}
    	if(event.getAction() == MotionEvent.ACTION_UP)
		{
    		endX=(int)event.getX();
	    	if(startX-endX>5)
	    	{
	    		new showMenuAsyncTask().execute(-10);	
	    		mwhich=3;
	    	}
	    	if(endX-startX>5)
	    	{
	    		new showMenuAsyncTask().execute(10);
                if(myview != null)
                	myview.setDrawTool(((MainActivity)activity).choosed_graphic);
	    		mwhich=1;
	    
	    	}
		}
    }
    private void movepage3(MotionEvent event)
    {
    	if(event.getAction() == MotionEvent.ACTION_DOWN)
    	{
    		startX=(int)event.getX();
    	}
    	if(event.getAction() == MotionEvent.ACTION_UP)
		{
    		endX=(int)event.getX();

	    	if(endX-startX>5)
	    	{
	    		new showMenuAsyncTask().execute(10);
	    		mwhich=2;
	    	
	    	}
		}
    }
    class showMenuAsyncTask extends AsyncTask<Integer, Integer, Integer>
    {
        @Override
        protected Integer doInBackground(Integer... params)
        {
            int leftMargin = eraserParams.leftMargin;

            while (true)
            {
                leftMargin += params[0];
                if (params[0] > 0 && leftMargin > rightMax)
                {
        
                        break;
                } else if (params[0] < 0 && leftMargin < leftMax)
                {
                	
                        break;
                }
                publishProgress(leftMargin);
                try
                {
                    Thread.sleep(2);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } 
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            eraserParams.leftMargin = values[0];
            eraser.setLayoutParams(eraserParams);
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            super.onPostExecute(result);
            eraserParams.leftMargin = result;
            eraser.setLayoutParams(eraserParams);
        }
    }
    public int getWhich()
    {
    	return mwhich;
    }
    @Override
    public void dismiss()
    {
    
    	alllayout.removeAllViews();
    	setView(null);
    	
   // 	Timer timer = new Timer();
    	/*
    	timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
			//	dismiss();
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}, 1); 	
		*/
    	super.dismiss();
   // 	((MainActivity)activity).selectwindowDismiss();
    }
    private class myHandler extends Handler
    {
    	@Override
		public void handleMessage(Message msg) 
    	{ 
			 switch (msg.what) 
			 {  
	            case 1:  
	            	if(msg!=null)
	            		dismiss();
	            	break;	           	            	
	        }  
	    }
    }
    
	private int Dp2Px(float dp){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dp*scale+0.5f);
	}
}

