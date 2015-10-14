package com.example.drawdemo04;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.drawdemo04.PenSize.PenSizeBar;
import com.example.drawdemo04.PenSize.PenSizeBar.OnThicknessChangedListener;
import com.example.drawdemo04.SelectPicPopupWindow.showMenuAsyncTask;
import com.example.drawdemo04.autoset_widget.LocalPhoto_Button;
import com.example.drawdemo04.autoset_widget.Undo_Button;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class BottomSlideView extends LinearLayout implements Serializable{

	private LinearLayout alllayout;
	private LinearLayout scrollLayout1;
	private LinearLayout scrollLayout2;
	private int adapterHeight ;
	private int mwhich;
	private int displayWidth;
	private int leftMax,rightMax,startX,endX;
	private Context context;
	private MyView myView;;
	private PenSizeBar eraserBar;
	private LocalPhoto_Button localChoose;
	private LocalPhoto_Button societyChoose;
	private Undo_Button undoChoose;
	private Undo_Button redoChoose;
	private Undo_Button deleteChoose;
	private ImageView cube_butt;
	private HorizontalScrollView graphyicalScroll;
	private HorizontalScrollView paintTypeScroll;
	private LinearLayout eraser,pen,backgroundLinear,deleteLinear;
	private LayoutParams eraserParams,penParams,backParams,deleteParams;
	private OnClickListener local_onClick,society_onClick;
	private OnThicknessChangedListener eraserthickness_onChanged;
	private Handler handler;
	private Activity activity;
	private ImageView addOrSub;
	
	private int[] resIds = new int[]{R.drawable.curve25,R.drawable.edit4,R.drawable.line,R.drawable.rectangular,R.drawable.circle46,R.drawable.yield,
			R.drawable.cube,R.drawable.cylindrical7,R.drawable.recycle};
	
	private int[] littleResIds = new int[]{R.drawable.curve25_little,R.drawable.edit4_little,R.drawable.line_little,R.drawable.rectangular_little,R.drawable.circle46_little,R.drawable.yield_little,
			R.drawable.cube_little,R.drawable.cylindrical7_little,R.drawable.recycle};
	
	private int[] paintTypeIds = new int[]{R.drawable.pencil5,R.drawable.fountain,R.drawable.brush17};
	
	private int[] littlePaintTypeIds = new int[]{R.drawable.pencil5_little,R.drawable.fountain_little,R.drawable.brush17_little};
	
	private ArrayList<ImageView> graphyicalButtons = new ArrayList<ImageView>();
	private ArrayList<ImageView> paintTypeButtons = new ArrayList<ImageView>();
	private int graphyic_lastChoosed = 0;
	private int painttype_lastChoosed = 0;
	private final int ADD = 1;
	private final int SUB = 0;
	
	public BottomSlideView(Context context,OnClickListener local_onClick,OnClickListener society_onClick,
    		OnThicknessChangedListener eraserthickness_onChanged,int displayWidth,final int which,int adapterHeight)
	{
		this(context,null);
		this.mwhich = which;
		this.context = context;
		this.adapterHeight = adapterHeight;
		this.displayWidth = displayWidth;
//		this.myView = myView;
		this.local_onClick = local_onClick;
		this.society_onClick = society_onClick;
		this.eraserthickness_onChanged = eraserthickness_onChanged;
		Log.i("displayWidht",String.valueOf(displayWidth));
		Log.i("adapterHeight",String.valueOf(adapterHeight));
		 
		initWidget();
		Log.i("BottomSlider","initwidget");
		adjustWidget();
		Log.i("BottomSlider","adjustWidget");
	//	showMenu(mwhich);
		Log.i("BottomSlider","showMenu");
		setWidgetListener();
		Log.i("BottomSlider","setWidgetListener");
	}
	public BottomSlideView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.alert_dialog, this,true);
	}
	
	private void initWidget()
	{
		alllayout = (LinearLayout)findViewById(R.id.penLayout);
	    eraserBar = (PenSizeBar)findViewById(R.id.eraserSizebar);
	    localChoose = (LocalPhoto_Button)findViewById(R.id.localChoose);
	    societyChoose = (LocalPhoto_Button)findViewById(R.id.societyChoose);
	    undoChoose = (Undo_Button)findViewById(R.id.undoChoose);
	    redoChoose = (Undo_Button)findViewById(R.id.redoChoose);
	    deleteChoose = (Undo_Button)findViewById(R.id.deleteChoose);
	    eraser = (LinearLayout)findViewById(R.id.eraserSet);
		pen = (LinearLayout)findViewById(R.id.penSet);
		backgroundLinear = (LinearLayout)findViewById(R.id.backgroundSet);
		deleteLinear = (LinearLayout)findViewById(R.id.deleteSet);
		graphyicalScroll = (HorizontalScrollView)findViewById(R.id.graphyicScroll);
		paintTypeScroll = (HorizontalScrollView)findViewById(R.id.PaintTypeScroll);
		scrollLayout1 = (LinearLayout)findViewById(R.id.graphyic_linear);
		scrollLayout2 = (LinearLayout)findViewById(R.id.PaintType_linear);
		
		handler = new myHandler();
		
	}
	
    private void adjustWidget()
    {
    	float scale = getResources().getDisplayMetrics().density;
    	eraserBar.setMaximum((int)(scale*10)*2);
 	    
 	    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) localChoose.getLayoutParams();
 	    params.width = (int)(displayWidth*0.25);
 	    params.height = (int)(adapterHeight*0.7);
 	    localChoose.setLayoutParams(params);
 	    localChoose.setImageSrc(R.drawable.ic_camera);
 	    localChoose.setText(context.getString(R.string.localGallery));
 	    
 	    params = (RelativeLayout.LayoutParams) societyChoose.getLayoutParams();
 	    params.width = (int)(displayWidth*0.25);
 	    params.height = (int)(adapterHeight*0.7);
 	    societyChoose.setLayoutParams(params);
 	    societyChoose.setImageSrc(R.drawable.ic_gallery);
 	    societyChoose.setText(context.getString(R.string.societyGallery));
 	    
 	    params = (RelativeLayout.LayoutParams) undoChoose.getLayoutParams();
 	    params.width = (int)(displayWidth*0.2);
 	    params.height = params.width;
 	    undoChoose.setLayoutParams(params);
 	    undoChoose.setImageSrc(R.drawable.ic_undo_back);
 	    undoChoose.setText(context.getString(R.string.undo));
 	    
 	    params = (RelativeLayout.LayoutParams)redoChoose.getLayoutParams();
 	    params.width = (int)(displayWidth*0.2);
 	    params.height = params.width;
 	    redoChoose.setLayoutParams(params); 
 	    redoChoose.setImageSrc(R.drawable.ic_undo_ahead);
 	    redoChoose.setText(context.getString(R.string.redo));
 	    
 	    params = (RelativeLayout.LayoutParams)deleteChoose.getLayoutParams();
 	    params.width = (int)(displayWidth*0.2);
 	    params.height = params.width;
 	    deleteChoose.setLayoutParams(params);
 	    deleteChoose.setImageSrc(R.drawable.ic_delete);
 	    deleteChoose.setText(context.getString(R.string.delete));
 	    
 	    eraserParams = (LayoutParams)eraser.getLayoutParams();
 		penParams = (LayoutParams)pen.getLayoutParams();
 		backParams = (LayoutParams)backgroundLinear.getLayoutParams();
 		deleteParams = (LayoutParams)deleteLinear.getLayoutParams();
 		
 		eraserParams.width = displayWidth;
 		penParams.width = displayWidth;
 		backParams.width = displayWidth;
 		deleteParams.width = displayWidth;
 		
 		RelativeLayout backgroundRelative = (RelativeLayout)findViewById(R.id.backgroundChoose);
 		LinearLayout.LayoutParams backparams= (LinearLayout.LayoutParams)backgroundRelative.getLayoutParams();
 		backparams.topMargin = (int)(adapterHeight*0.5 - adapterHeight*0.7*0.5);
 		backgroundRelative.setLayoutParams(backparams);
 		
 		backgroundRelative = (RelativeLayout)findViewById(R.id.undoback);
 		backparams = (LinearLayout.LayoutParams) backgroundRelative.getLayoutParams();
 		backparams.topMargin = (int)(adapterHeight*0.3);
 		backgroundRelative.setLayoutParams(backparams);
 		
 		LinearLayout.LayoutParams scrollParams = (LayoutParams) graphyicalScroll.getLayoutParams();
 	//	scrollParams.width = (int)(displayWidth*0.85);
 		scrollParams.width = LayoutParams.WRAP_CONTENT;
 		scrollParams.height = (int)(adapterHeight*0.3);
 		scrollParams.topMargin = (int)(adapterHeight*0.145);
 		scrollParams.leftMargin = (int)(displayWidth*0.08);
 		graphyicalScroll.setLayoutParams(scrollParams);
 		addGraphyicalButtons();
 		addView1();
 		
 		scrollParams = (LayoutParams)paintTypeScroll.getLayoutParams();
 		scrollParams.height = (int)(adapterHeight*0.3);
 		scrollParams.topMargin = (int)(adapterHeight*0.08);
 		scrollParams.leftMargin = (int)(displayWidth*0.08);
 		paintTypeScroll.setLayoutParams(scrollParams);
 		addPaintTypeButtons();
 		addView2();
    }
    private void addGraphyicalButtons()
    {
    	for(int i=0;i<littleResIds.length;i++)
    	{
    		ImageView image = new ImageView(context);
    		image.setTag(i);
    		graphyicalButtons.add(image);
    	}
    			
    }
    private void addPaintTypeButtons()
    {
    	for(int i=0;i<3;i++)
    	{
    		ImageView image = new ImageView(context);
    		image.setTag(i);
    		paintTypeButtons.add(image);
    	} 		
    }
    private void addView1()
    {
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(adapterHeight*0.35*0.8), (int)(adapterHeight*0.35*0.8));
    	for(int i=0;i<2;i++)
    	{
    		ImageView graphyicalImg = graphyicalButtons.get(i);
        	
        	graphyicalImg.setScaleType(ImageView.ScaleType.CENTER);
        	if(i == 0)
        		graphyicalImg.setImageResource(resIds[i]);
        	else
        		graphyicalImg.setImageResource(littleResIds[i]);
        	
        	scrollLayout1.addView(graphyicalImg, params);
    	}
    	addOrSub = new ImageView(context);
    	addOrSub.setScaleType(ImageView.ScaleType.CENTER);
    	addOrSub.setImageResource(R.drawable.layer_add);
    	addOrSub.setTag(ADD);
    	scrollLayout1.addView(addOrSub, params);
    	
    	for(int i = 2;i<graphyicalButtons.size();i++)
    	{
    		ImageView graphyicalImg = graphyicalButtons.get(i);
    		graphyicalImg.setScaleType(ImageView.ScaleType.CENTER);
    		graphyicalImg.setImageResource(littleResIds[i]);
    	}
    }
    private void addView2()
    {
    	for(int i=0;i<paintTypeIds.length;i++)
    	{
    		ImageView paintTypeImg = paintTypeButtons.get(i);
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(adapterHeight*0.35*0.8), (int)(adapterHeight*0.35*0.8));
    		paintTypeImg.setScaleType(ImageView.ScaleType.CENTER);
    		if(i == 0)
    			paintTypeImg.setImageResource(paintTypeIds[i]);
    		else
    			paintTypeImg.setImageResource(littlePaintTypeIds[i]);
    		   
    		scrollLayout2.addView(paintTypeImg, params);
    	}
    }
    public void setPage(int page){
    	mwhich = page; 
    	showMenu(mwhich);
    }
    
    private void setWidgetListener()
    {
    	this.setOnTouchListener(moveOnTouch);

    	eraserBar.setOnThicknessChangedListener(eraserthickness_onChanged);
    	
    	localChoose.setOnClickListener(local_onClick);
    	societyChoose.setOnClickListener(society_onClick);
    	undoChoose.setOnClickListener(undoOnclick);
    	redoChoose.setOnClickListener(redoOnclick);
    	deleteChoose.setOnClickListener(deleteOnclick);
    	
    	addOrSub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(((Integer)arg0.getTag()) == ADD)
				{
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(adapterHeight*0.35*0.8), (int)(adapterHeight*0.35*0.8));
					for(int i = graphyicalButtons.size()-1;i >= 2;i--)
					{
						scrollLayout1.addView(graphyicalButtons.get(i), 2, params);
					}
					
					LinearLayout.LayoutParams scrollParams = (LayoutParams) graphyicalScroll.getLayoutParams();
				 	scrollParams.width = (int)(displayWidth*0.85);
				 	graphyicalScroll.setLayoutParams(scrollParams);
				 	arg0.setTag(SUB);
				 	addOrSub.setImageResource(R.drawable.sub);
				}
				else
				{
					scrollLayout1.removeViewsInLayout(2, graphyicalButtons.size()-2);
					LinearLayout.LayoutParams scrollParams = (LayoutParams) graphyicalScroll.getLayoutParams();
				 	scrollParams.width = LayoutParams.WRAP_CONTENT;
				 	graphyicalScroll.setLayoutParams(scrollParams);
				 	arg0.setTag(ADD);
				 	addOrSub.setImageResource(R.drawable.layer_add);
				}
			}
		});
    	for(int i=0;i<graphyicalButtons.size();i++)
    		graphyicalButtons.get(i).setOnClickListener(graphyical_onclicklistener);    
    	
    	for(int i=0;i<paintTypeButtons.size();i++)
    		paintTypeButtons.get(i).setOnClickListener(paintType_onclicklistener);
    }
    
    private OnTouchListener moveOnTouch = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.i("ontouch","touch");
			// TODO 自动生成的方法存根
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
	}; 

	private OnClickListener graphyical_onclicklistener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			int tag = (Integer)arg0.getTag();
			if(graphyic_lastChoosed != tag)
			{
				((ImageView)arg0).setImageResource(resIds[tag]);
				graphyicalButtons.get(graphyic_lastChoosed).setImageResource(littleResIds[graphyic_lastChoosed]);
				graphyic_lastChoosed = tag;
			}
			if(tag == 0)
			{
				if(myView != null)
					myView.setDrawTool(6);
				if(activity != null)
				{
					((MainActivity)activity).choosed_graphic = 6;
				    ((MainActivity)activity).isEraser = false;
				}
			}
			if(tag == 1)
			{
				if(activity != null)
					   ((MainActivity)activity).showWriteTextWindow();
			}
			if(tag >=2 && tag <= 7)
			{
				if(myView != null)
					myView.setDrawTool(tag-2);
				if(activity != null)
				{
					((MainActivity)activity).choosed_graphic = tag-2;
				    ((MainActivity)activity).isEraser = false;
				}
			}
			if(tag == 8)
			{
				if(activity != null)
					((MainActivity)activity).showRecycleTag();
			}
		}
	};
	
	public void setGraphyicalButtonResource (int current,int next)
	{
		if(graphyicalButtons != null && graphyicalButtons.size() > current && graphyicalButtons.size() > next)
		{
			graphyicalButtons.get(current).setImageResource(littleResIds[current]);
			graphyicalButtons.get(next).setImageResource(resIds[next]);
			graphyic_lastChoosed = next;
		}
	}
	
	private OnClickListener paintType_onclicklistener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			int tag = (Integer)arg0.getTag();
			if(painttype_lastChoosed != tag)
			{
				((ImageView)arg0).setImageResource(paintTypeIds[tag]);
				paintTypeButtons.get(painttype_lastChoosed).setImageResource(littlePaintTypeIds[painttype_lastChoosed]);
			    painttype_lastChoosed = tag;
			}
			if(activity != null)
				((MainActivity)activity).choosePaintType(tag);
		}
	};
    private OnClickListener undoOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(myView != null)
				myView.setDrawTool(7);
		}
	};
    private OnClickListener redoOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(myView != null)
				myView.setDrawTool(8);
		}
	};
    private OnClickListener deleteOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(activity != null)
				((MainActivity)activity).deleteView();	
		}
	};
	public void setView(MyView myview){
		this.myView = myview;
	}
	public void setActivity(Activity activity){
		this.activity = activity;
	}
	private void showMenu(int which)
	{
	    switch(which)
	    {
	    case 0:
	    	eraserParams.leftMargin= 0;  	
	    	if(myView != null)
	    	{
	    		Log.i("BottomSliderView_showMenu","chooseEraser");
	    		myView.setDrawTool(10);
	    	} 		
	    	break;
	    case 1:
	    	eraserParams.leftMargin = 0 - eraserParams.width; 
	    	if(myView != null)
	    	{
	    		Log.i("actualrecord",String.valueOf(myView.actualRecord));
	    		myView.setDrawTool(myView.actualRecord);
	    	}
	    	break;
	    case 2:
	    	eraserParams.leftMargin = 0 - eraserParams.width-penParams.width;
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
	}
	
	private void movepage0(MotionEvent event)
	{
		int topheight = findViewById(R.id.eraserSizebar).getTop();
		int bottomheight = findViewById(R.id.eraserSizebar).getBottom();	
		int y=(int) event.getY();

		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
			if(y<topheight-10 || y>bottomheight+10)
				startX=(int)event.getX();				
			else
				startX=-1000;
		    
			startX=(int)event.getX();
		}
	    if(event.getAction() == MotionEvent.ACTION_UP)
	    {
	    	if(y<topheight-10 || y>bottomheight+10)
	    	{
	    		endX=(int)event.getX();
	    		if(startX - endX>5)
	    		{
	    			new showMenuAsyncTask().execute(-10);
	    			if(myView !=null && activity != null)
	    				myView.setDrawTool(((MainActivity)activity).choosed_graphic);
	    			mwhich=1;	
	    			((MainActivity)activity).setTag(2, 1);
	    			((MainActivity)activity).setTag(1, 0);
	    		}
	    	}
	    }
	}
	private void movepage1(MotionEvent event)
	{
//		int topheight1 = findViewById(R.id.penSizebar).getTop();
//		int bottomheight1 = findViewById(R.id.penSizebar).getBottom();
//		int topheight2 = findViewById(R.id.penTransparencybar).getTop();
//		int bottomheight2 = findViewById(R.id.penTransparencybar).getBottom();
		
		int y=(int) event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
	//		if(y<topheight1-10 || (y>bottomheight1+10 && y<topheight2-10))
				startX=(int)event.getX();
		}
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
	//		if(y<topheight1-10 || (y>bottomheight1+10 && y<topheight2-10))
	//		{
				endX = (int)event.getX();
				if(endX-startX > 5)
				{
					new showMenuAsyncTask().execute(10);   //往左
				    if(myView != null)
				    	myView.setDrawTool(10);
					mwhich=0;
					((MainActivity)activity).setTag(1, 1);
	    			((MainActivity)activity).setTag(2, 0);
				}
	            if(startX-endX>5)
	            {
	            	new showMenuAsyncTask().execute(-10);
	            	mwhich=2;
	            	((MainActivity)activity).setTag(3, 1);
	    			((MainActivity)activity).setTag(2, 0);
	            	
	            }
//			}
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
	    		((MainActivity)activity).setTag(4, 1);
    			((MainActivity)activity).setTag(3, 0);
	    	}
	    	if(endX-startX>5)
	    	{
	    		new showMenuAsyncTask().execute(10);
	            if(myView != null)
	            	myView.setDrawTool(((MainActivity)activity).choosed_graphic);
	    		mwhich=1;
	    		((MainActivity)activity).setTag(2, 1);
    			((MainActivity)activity).setTag(3, 0);
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
	     		((MainActivity)activity).setTag(3, 1);
    			((MainActivity)activity).setTag(4, 0);
	    	}
	    	else
	    	{
	    		if(startX - endX > 5)
	    		{
	    			((MainActivity)activity).setTag(5, 1);
	    			((MainActivity)activity).setTag(4, 0);
	    			if(activity != null)
	    				((MainActivity)activity).startActivityToColorPicker();
	    		}
	    	}
		}
	}
	public int getWhich()
	{
		return mwhich;
	}
	class showMenuAsyncTask extends AsyncTask<Integer, Integer, Integer>
	{
	    @Override
	    protected Integer doInBackground(Integer... params)
	    {
	        int leftMargin = eraserParams.leftMargin;
            Log.i("move","asynic");
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

	private class myHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg) 
		{ 
			 switch (msg.what) 
			 {  
	            case 1:  
	            	if(msg!=null)
	         //   		dismiss();
	            	break;	           	            	
	        }  
	    }
	}
	/*
	@Override
	public void dismiss()
	{

		alllayout.removeAllViews();
		setView(null);
		super.dismiss();
		((MainActivity)activity).selectwindowDismiss();
	}
    */

}
