package com.example.drawdemo04;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import myListener.BottomSliderOnClickListener;
import myListener.LayViewRemove;
import myListener.LayerOnItemClickListener;
import myListener.LocalOnClickListener;
import myListener.LocaldeleteOnClickListener;
import myListener.RecoverOnClickListener;
import myListener.SaveOnClickListener;
import myListener.SocietyOnClickListener;
import myListener.SubmitOnClickListener;
import myListener.SureOnClickListener;
import myListener.TextOnClickListener;
import myListener.ThemeSetOnClickListener;
import myListener.TitleBtnOnClickListener;

import org.apache.http.impl.conn.Wire;

import com.example.drawdemo04.PenGraphycal.DrawBS;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.SelectGraphycalWindow;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;
import com.example.drawdemo04.PenGraphycal.WriteTextPopupwindow;
import com.example.drawdemo04.PenSize.PenSizeBar.OnThicknessChangedListener;
import com.example.drawdemo04.SocietyModel.AdjustSizeofInitpicWindow;
import com.example.drawdemo04.SocietyModel.AsyncBitmapLoader;
import com.example.drawdemo04.SocietyModel.Comment_view;
import com.example.drawdemo04.SocietyModel.HttpUtil;
import com.example.drawdemo04.SocietyModel.Society_gallery;
import com.example.drawdemo04.SocietyModel.Society_set;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.SelectDialog;
import com.example.drawdemo04.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.example.drawdemo04.holocolorpicker.SelectTypeofPaintWindow;
import com.example.drawdemo04.holocolorpicker.SelectWindow;
import com.example.drawdemo04.initModel.GenerateCode;
import com.example.drawdemo04.initModel.Init_choose;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

///////from == 0 来自init 新画布  ;from == 1来自init 本地画廊; from == 2 来自init-社区画廊-加号;from == 3 来自 init-本地画廊-加号  from == 4 来自gallery-加号
public class MainActivity extends Activity{

	public final int FLOORBITMAP = 1,BACKBITMAP = 0;
    public final int REQUSTCODE = 100;
    public final int  IMAGE_CODE = 200;
    public final String IMAGE_TYPE = "image/*";
	LinearLayout linearlayout;
	LinearLayout.LayoutParams params;

	MyView myView = null;
	public boolean isEraser = false; 
	    
	private Button societysetButton; 
	private Button titlebtn;
	private Button recyclebtn;
	
	public static final int TEXT = 1;
	public static final int GRAPHY = 0;
	public static int graphicOrtext;
	public boolean writingText = false;
	private int screenWidth,screenHeight;
	private ArrayList<DrawBS> saves=new ArrayList<DrawBS>();
	 
    private RelativeLayout recycle_relative;
    private SelectTypeofPaintWindow colorChooseWindow;       //选择颜色窗口
    private SelectGraphycalWindow graphycalWindow;           //
    private Society_set societysetWindow;                    //社区分享窗口
    private SelectChooseLayerview chooseLayerWindow;         //显示涂鸦层窗口
    private WriteTextPopupwindow writeTextWindow;            //文字窗口
    private AdjustSizeofInitpicWindow adjustWindow;          //裁剪背景图窗口
    
    private RelativeLayout titleLayout;
    private Boolean isCurrent = false;
    
    private int[] idArray = new int[]{R.id.penOreraser,R.id.pen,
    		R.id.backgroundColor,R.id.unDoOrreDo};
    private int[] linearidArray = new int[]{R.id.penOreraser_linear,
    		R.id.pen_linear,R.id.background_linear,R.id.delete_linear};

    private int viewHeight,viewWidth;
    private String initpic;     //当前背景图的上传id，如果是本地图片，则initpic=""
    private String initpicurl;  //initpicurl 为背景图的url
    public int choosed_graphic = 6;
    public int last_graphic;
    private ReceiveBroadCast receiveBroadcase;
    
    
    public static int thickness;       
    public static int currentColor = Color.RED; 
    public static int transparency = 100;
    public static int eraser_thickness;
    public static int textSize;
    public static MaskFilter filter = null;
    private static int tag = -2;
   
	private int initleft = 0;
	private int inittop = 0;
	private int lastX = 0;
	private float thisX = 0.0f;
    private int ins = 0;
	private int from;
	private MyHandler back_handler,floor_handler;
    private String configInfo;
    private int imageFrom ;            //判断当前背景图是本地图片还是社区在线图片
    public int saveRecord = 0;
    public int submitRecord = 0;
    private int unsavedlayer_remove_success = 0;

    public static boolean hasDrawn_submit = false;
    public static boolean hasDrawn_save = false;
    private BottomSlideView bottomslider;
    private MainBottomLinear mainbottom;
    private RelativeLayout content_layout;
    private String text;
    private int pic_id ;
    private float scale ;
    private boolean isActivityRecover = false;
    private boolean isToGallery = false;
    private boolean isToLocal = false;
    public SelectDialog progressDialog;
    
    public static int PenSizeBar_position = 0;
    public static int Transparency_position = 0;
    public static int Opacity_position = 0;
    public static int SVB_position = 0;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("mainactivity","oncreate");
		
		from = getIntent().getIntExtra("from", 0);
	    registerBroadCast();
	    
	    initWidthFromId();
        DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth=dm.widthPixels;                  // 屏幕宽
		screenHeight=dm.heightPixels;                //屏幕高 
		
		viewHeight = screenHeight-(int)(screenHeight*0.09)-(int)(screenHeight*0.08);               //涂鸦的view的高度
		viewWidth = screenWidth;
	
    	scale = getResources().getDisplayMetrics().density;
		Log.i("MainActivity_scale",String.valueOf(scale));
		Tools.layer_width = Tools.Dp2Px(220, scale);
		Tools.society_width = Tools.Dp2Px(220, scale);
		graphicOrtext = GRAPHY;              // 表明当前是画图还是写文字
		tag = -2;
	    
		if(savedInstanceState == null)
		{
			
			judgeFromWhich(viewWidth,viewHeight);        //判断Mainactivity从哪个activity进入
			adjustBottomSlideView();
			Log.i("MainActivity_oncreate","ajustBottomSliderview");
			adjustBottomBar();
			Log.i("MainActivity_oncreate","adjustBottombar");
			initBottomButton();
			Log.i("MainActivity_oncreate","initBottomButton");
			
		}
		else
		{
			isActivityRecover = true;
			imageFrom = savedInstanceState.getInt("imageFrom");
			initpic = savedInstanceState.getString("initpic");
			initpicurl = savedInstanceState.getString("initpicurl");
			tag = savedInstanceState.getInt("tag");
			
			myView = new MyView(MainActivity.this, viewWidth,viewHeight, tag, null, null,false);
			myView.setBackgroundColor(Color.TRANSPARENT);

			adjustBottomSlideView();
			adjustBottomBar();
			initBottomButton();
		}
		 /*
		 myView = new MyView(MainActivity.this, viewWidth, viewHeight,tag);
		 adjustBottomSlideView();
		 adjustBottomBar();
		 initBottomButton();
		 */
	}
    private void initWidthFromId()
    {
        recycle_relative = (RelativeLayout)findViewById(R.id.recycle_tag);
        recycle_relative.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO 自动生成的方法存根
				HideRecycleTag();
			}
		});
        
        recyclebtn = (Button)findViewById(R.id.recycle_btn);
    	content_layout = (RelativeLayout)findViewById(R.id.content);
    	linearlayout = (LinearLayout) findViewById(R.id.linearlayout01);
    	titlebtn = (Button)findViewById(R.id.head_TitleBackBtn);
		societysetButton = (Button)findViewById(R.id.Society);
	
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)  
    {  
    	Log.i("MainActivity","onSaveInstanceState");
    	outState.putInt("imageFrom", imageFrom);
    	outState.putString("initpic", initpic);
    	outState.putString("initpicurl", initpicurl);
        outState.putInt("tag", tag);
        super.onSaveInstanceState(outState);  

    }  
    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
    	Log.i("MainActivity","onRestoreInstance");
		super.onRestoreInstanceState(savedInstanceState);
	}
    
    @Override 
	public void onResume()
	{
		if(mainbottom != null && mainbottom.isColorSelected())
		{
			setTag(5, 0);
			setTag(4, 1);
		}
		if(isActivityRecover == true)
		{
			super.onResume();
			return ;
		}
		Log.i("MainActivity_onResume","afterRestore");
		
		if(isToGallery || isToLocal)
		{
			Log.i("MainActivity_isToGallery","true");
			if(myView != null)
			{
				myView.restore();
		//		myView.preWork(tag);
				myView.drawWork(tag);
				if(linearlayout != null && linearlayout.getChildCount() == 0)
				{
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewWidth, viewHeight);
					linearlayout.addView(myView,params);
				}
			} 
			if(isToGallery)
			    isToGallery = false;
			if(isToLocal)
				isToLocal = false;
		}
		super.onResume();
	}
    
    private void judgeFromWhich(int viewWidth,int viewHeight)          //判断Mainactivity从哪个activity进入
    {
    	if(from == 1)                     // from == 1来自init 本地画廊 society_gallery;
		{
    		Log.i("MainActivity_from","1");
			String back_path = getIntent().getExtras().getString("back_path");        // 获取存在文件中的背景图片，涂鸦层以及  背景图和涂鸦层配置文件 的路径
			String floor_path = getIntent().getExtras().getString("surface_path");     //分别为back_path,floor_path,configInfo_path
            String configInfo_path = getIntent().getExtras().getString("config_path");
            
			back_handler = new MyHandler(this,BACKBITMAP,from);
			floor_handler = new MyHandler(this,FLOORBITMAP,from);
			Init_choose.asynloader.getAdustedBitmap(3, back_path, Init_choose.fixedthreadpool, back_handler, viewWidth, viewHeight);
			Init_choose.asynloader.getAdustedBitmap(3, floor_path, Init_choose.fixedthreadpool, floor_handler, viewWidth, viewHeight);
			
			if(configInfo_path != null)
			    Init_choose.asynloader.getText_Content(configInfo_path, Init_choose.fixedthreadpool, back_handler);
			else
			{
				setImageFrom(Tools.LOCAL);
				setInitPic("");                           
			}
		}
		else
		{
			showMyView(tag, null, null);
			
			if(from == 0 || from == 2 || from == 3)  //from == 0 来自init 新画布  ;from == 2 来自init-社区画廊-加号;from == 3 来自 init-本地画廊-加号  
			{
				setImageFrom(Tools.LOCAL);
				setInitPic("");
			}
			if(from == 4)                           //from == 4 来自gallery-加号
			{
				setImageFrom(Tools.SOCIETY);
				setInitPic(getIntent().getStringExtra("initpic"));
				setInitPicUrl(getIntent().getStringExtra("initpicurl"));
				
			    RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(myView, initpicurl, Init_choose.fixedthreadpool, viewWidth, viewHeight, true, null);
				if(myView != null)
				{
				    myView.setImageBitmap(value.getBitmap());
				}
			}
		}
    }
    private void adjustBottomSlideView()       //调节BottomSliderView的大小
    {
    	bottomslider = new BottomSlideView(MainActivity.this,local_onClick,society_onClick,
    						eraserthickness_onChanged,getWindowManager().getDefaultDisplay().getWidth(),0,Tools.Dp2Px(140, scale));
    	Log.i("MainActivity","adjustBottomSliderView");
 		RelativeLayout.LayoutParams bottom_slider_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,

 				RelativeLayout.LayoutParams.WRAP_CONTENT);

 		bottom_slider_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
 		bottom_slider_params.bottomMargin = Tools.Dp2Px(45, scale);
 		content_layout.addView(bottomslider, bottom_slider_params);
 		bottomslider.setVisibility(View.GONE);
 		bottomslider.setView(myView);
 		bottomslider.setActivity(this);
 		Log.i("MainActivity","adjustBottomSliderView");
    }

    private void adjustBottomBar()     //调节底部条的大小
    {
    	mainbottom = new MainBottomLinear(MainActivity.this,screenWidth);
    	float scale = getResources().getDisplayMetrics().density;

    	RelativeLayout.LayoutParams bottom_params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, Tools.Dp2Px(45, scale));
    	bottom_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	content_layout.addView(mainbottom, bottom_params);	
    }
	// 绑定按钮，并设置监听事件
	private void initBottomButton() {          //初始化按钮 ，设置监听函数
		
		mainbottom.setDrawtoolOnclickListener(bottomslider_onClick);
		mainbottom.setColorOnclickListener(bottomslider_onClick);
		mainbottom.setBackgroundOnclickListener(bottomslider_onClick);
		mainbottom.setDeleteOnclickListener(bottomslider_onClick);
		mainbottom.setPenOnclickListener(bottomslider_onClick);
		
		titlebtn.setOnClickListener(titlebtn_onClick);
		
		societysetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				showSocietySetDialog(null);
			}
		});
		
		recyclebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				
				if(choosed_graphic < 6)
				{
					if(myView != null)
						myView.setDrawTool(choosed_graphic);
			        isEraser = false;
				}
			}
		});
	}

	public void bottomslider_gone()   //底部滑动窗口BottomSliderView 消失
	{
		if(bottomslider.getVisibility() == View.VISIBLE)
		{
			Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_bottom_out);
			bottomslider.startAnimation(animation);
			
			bottomslider.setVisibility(View.GONE);
			mainbottom.setDrawtoolTag(0);
			mainbottom.setBackgroundTag(0);
			mainbottom.setDeleteTag(0);
			mainbottom.setPenTag(0);
			isCurrent = false;
		}	
	}
	public void bottomslider_visible()   //底部滑动窗口BottomSliderView 出现
	{
		if(bottomslider.getVisibility() == View.GONE)
		{
			Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.push_bottom_in);
			bottomslider.startAnimation(animation);
			bottomslider.setVisibility(View.VISIBLE);;
			isCurrent = true;
		}
	}
	public void showSocietySetDialog(RecyclingBitmapDrawable value)           //显示社区分享窗口
	{          			
		societysetWindow = new Society_set(MainActivity.this, submit_OnclickListener,themeSet_OnclickListener,localdelete_OnclickListener,
				localsave_onClickListener,remoteUpload_onClick,titleset_onLongClick,name_onLongClick,Tools.society_width,screenHeight);
		
		societysetWindow.setWidth(Tools.society_width);
		societysetWindow.showAtLocation(MainActivity.this.findViewById(R.id.theAll),Gravity.LEFT, 0, 0);
		setTitleImg(societysetWindow, value);
	}  
    
	public void showMyView(int mtag,Bitmap back,Bitmap floor)
	{
		tag = mtag;
		if(linearlayout != null)
			linearlayout.removeAllViews();
		Bitmap backbitmap = back;
		Bitmap floorBitmap = floor;
	    
		if(myView != null)
		{
			choosed_graphic = myView.actualRecord;
			if(backbitmap == null)
			   backbitmap = myView.getBackBitmap();
			if(floorBitmap == null)
				floorBitmap = myView.getFloorBit();
			myView.dismiss();
		}

		myView = new MyView(MainActivity.this, viewWidth, viewHeight, mtag,backbitmap,floorBitmap,true);   //根据恢复操作后刷新myView画板
		myView.setBackgroundColor(Color.TRANSPARENT);
		myView.setDrawTool(choosed_graphic);
		if(!Tools.isHigherthan10())
		{
			if(backbitmap != null && !backbitmap.isRecycled())
			{
				backbitmap.recycle();
				backbitmap = null;
			}
			if(floorBitmap != null && !floorBitmap.isRecycled())
			{
				floorBitmap.recycle();
				floorBitmap = null;
			}
		}
		Log.i("MainActivity_showMyView","finish");
		params = new LinearLayout.LayoutParams(viewWidth, viewHeight);
	    linearlayout.addView(myView,params);
	    if(bottomslider != null)
	        bottomslider.setView(myView);
	   
	}
	
    //刷新涂鸦分层窗口
    public void showLayerViewDialog()
	{      //显示涂鸦分层窗口
		if(myView != null)
		{
			if(chooseLayerWindow != null)
				chooseLayerWindow.dismiss();		
			chooseLayerWindow = new SelectChooseLayerview(MainActivity.this,
							Tools.layer_width,screenHeight,myView.getBackBitmap(),myView.getFloorBit());
			chooseLayerWindow.setWidth(Tools.layer_width);
			Log.i("undosaved.size",String.valueOf(UnDoDraw.saved.size()));
			chooseLayerWindow.setActivity(this);
			
			chooseLayerWindow.setRecoverOnClickListener(recoverOnclick);
			chooseLayerWindow.setSaveOnclickListener(saveOnclick);
			chooseLayerWindow.setOnItemClickListener(layerOnclick);
			
			chooseLayerWindow.showAtLocation(MainActivity.this.findViewById(R.id.theAll),Gravity.RIGHT, 0, 0);
		}	
	}
	public void showWriteTextWindow()    //写文字窗口
	{
		writeTextWindow = new WriteTextPopupwindow(MainActivity.this, text_onClick, screenWidth, screenHeight);
		writeTextWindow.showAtLocation(MainActivity.this.findViewById(R.id.theAll),Gravity.LEFT, 0, 0);
		last_graphic = choosed_graphic;
		choosed_graphic = 7;
	//	bottomslider_gone();
	}
	private void HideRecycleTag()
	{
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.out_righttoleft1);
		if(recycle_relative != null)
		{
			recycle_relative.startAnimation(animation);
			recycle_relative.setVisibility(View.GONE);
		}
	}
	public void showRecycleTag()
	{
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.in_lefttoright);
		if(recycle_relative != null && recycle_relative.getVisibility() == View.GONE)
		{
			recycle_relative.startAnimation(animation);
			recycle_relative.setVisibility(View.VISIBLE);
		}
	}
	public void startActivityToColorPicker()
	{
		setIsToGallery(false);
		setIsToLocal(false);
		Intent intent = new Intent(MainActivity.this,SelectWindow.class);
		Log.i("BottomSliderOnclick",String.valueOf(getchoosed_graphic()));
		intent.putExtra("tool",getchoosed_graphic());
		intent.putExtra("text", getText());
		startActivity(intent);
		overridePendingTransition(R.anim.in_righttoleft, R.anim.out_righttoleft1);
	//	overridePendingTransition(R.anim.in_lefttoright, R.anim.out_righttoleft1);
	}
	/* some OnclickListener for 
	 * SelectTypeofPaintWindow,
	 * Society_set,
	 * SelectChooseLayerview ,
	 * AdjustSizeofInitpicWindow,
	 * adjustWindow,
	 * writeTextWindow
   */ 
	
    public boolean removeLayerView(int mtag){      //移除涂鸦层图片操作
    	LayViewRemove lay_remove = new LayViewRemove(this,viewWidth, viewHeight);
    	return lay_remove.removeLayerView(mtag, tag);
    }


	private OnThicknessChangedListener eraserthickness_onChanged = new OnThicknessChangedListener() {   //监听橡皮大小改变
		
		@Override
		public void onThicknessChanged(int opacity,int position) {
			// TODO 自动生成的方法存根
			eraser_thickness = opacity;
			myView.setEraseThickness();
		}
	};
	
	public void choosePaintType(int tag)       //选择笔触
	{
		switch(tag)
		{
		case 0:                        //签字笔
			filter = null;
			myView.setMaskFilter();
			myView.setDrawTool(myView.actualRecord);
			break;
		case 1:                        //铅笔模糊
			filter = new BlurMaskFilter(8.0f,BlurMaskFilter.Blur.SOLID);
			myView.setMaskFilter();
			myView.setDrawTool(myView.actualRecord);
			break;
		case 2:                        //毛笔浮雕
			filter = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
			myView.setMaskFilter();
			break;
		default:
			filter = null;
			myView.setMaskFilter();
			break;
		}
	}
		
	private OnClickListener cancel_onClick = new OnClickListener() {    //监听背景图片裁剪窗口 的 取消Button
		
		@Override
		public void onClick(View arg0) {
			// TODO 自动生成的方法存根
			if(adjustWindow != null)
				adjustWindow.dismiss();
		}
	};
	
	
	 /* Override some system function 
	    Some inner class 
	*/
	public BottomSlideView getBottomSliderView()
	{
		return bottomslider;
	}
	public SelectChooseLayerview getChooseLayerWindow()
	{
		return chooseLayerWindow;
	}
	public AdjustSizeofInitpicWindow getAdjustWindow()
	{
		return adjustWindow;
	}
	public WriteTextPopupwindow getWriteTextWindow()
	{
		return writeTextWindow;
	}
	public Society_set getSocietySet()
	{
		return societysetWindow;
	}
	public Button getTitleBtn()
	{
		return titlebtn;
	}
	public MyView getMyView()
	{
		return myView;
	}
	public int getViewWidth()
	{
		return viewWidth;
	}
	public int getViewHeight()
	{
		return viewHeight;
	}
	public int getScreenHeight()
	{
		return screenHeight;
	}
    public int getTag()
    {
    	return tag;
    }
    public int getPicId()
    {
    	return pic_id;
    }
    public int getFrom()
    {
    	return from;
    }
    public int getImageFrom()
    {
    	return imageFrom;
    }
    public String getInitPic()
    {
    	return initpic;
    }
    
    public void setIsToGallery(boolean is)
    {
    	isToGallery = is;
    }
   
    public void setIsToLocal(boolean is)
    {
    	isToLocal = is;
    }
	public void setInitPic(String initpic)
	{
		this.initpic = initpic;
	}
	public void setImageFrom(int imageFrom)
	{
		this.imageFrom = imageFrom;
	}
	public void setInitPicUrl(String initpicUrl)
	{
		this.initpicurl = initpicUrl;
	}
	public void ProgressDismiss()
	{
		if(societysetWindow != null)
			societysetWindow.progressDismiss();
	}
	public void ProgressDisplay()
	{
		if(societysetWindow != null)
			societysetWindow.progressDisplay();
	}
	public void setViewTag(int i)
	{
		tag = i;
	}
	public void setPicid(int i)
	{
		pic_id = i;
	}
	public void setRecoverOnClickNum(int num)
	{
		if(recoverOnclick != null)
			recoverOnclick.setNum(num);
	}
	public void setTag(int id,int i)
	{
		if(mainbottom != null)
		{
			switch(id)
			{
			case 1:
				mainbottom.setDrawtoolTag(i);
				break;
			case 2:
			    mainbottom.setPenTag(i);
			    break;
			case 3:
				 mainbottom.setBackgroundTag(i);
				 break;
			case 4:
				mainbottom.setDeleteTag(i);
				break;
			case 5:
				mainbottom.setColorTag(i);
				break;
			}
		}
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getText()
	{
		return text;
	}
	public int getchoosed_graphic()
	{
		return choosed_graphic;
	}
	
	public void bottomSlider_select(int tag,int id,int buttid,int layoutid)
	{
		if(tag == 0)
		{
			if(bottomslider.getVisibility() == View.GONE)
			{
		        bottomslider.setPage(id-1);
		        bottomslider_visible();
			}
			else
				bottomslider.setPage(id-1);
			setTag(id, 1);
			
	//		if(id == 1)
	//	       eraser();    
		}
		else
		{
			if(bottomslider.getVisibility() == View.VISIBLE && (idArray[bottomslider.getWhich()] == buttid 
					|| linearidArray[bottomslider.getWhich()] == layoutid)){
				bottomslider_gone();
				setTag(id, 0);
			}
			else
			{
				if(bottomslider.getVisibility() == View.VISIBLE)
				{
					bottomslider.setPage(id-1);
					setTag(id, 1);
				}
			}
		}
	}
	
	public void eraser() {
		
		myView.setDrawTool(10);
	}  
	public void setTitleImg(final Society_set societysetWindow,RecyclingBitmapDrawable mvalue)
	{
		if(mvalue != null && societysetWindow != null)
		{
			societysetWindow.setTitleImg(mvalue);
			return ;
		}
			
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				societysetWindow.setTitleImg((RecyclingBitmapDrawable)(msg.obj));
			}
		};
		 
	    RecyclingBitmapDrawable title = (RecyclingBitmapDrawable) Init_choose.asynloader.getAdustedBitmap(4, Tools.titleSaved_path + Tools.title_name + Tools.identity.substring(Tools.identity.indexOf("_") + 1) + ".png", Init_choose.fixedthreadpool, handler, 
	    		societysetWindow.getTitleImgLength(), societysetWindow.getTitleImgLength());                 //获取头像图片
	    
	    if(title != null)
	    {
	    	Log.i("immediately","dd");
	    	if(title.getBitmap() != null)
	    	   societysetWindow.setTitleImg(title);
	    } 	
	    else                         //如果头像图片从本地文件夹无法获取（被用户删除），则从网络获取
	    {  
			RecyclingBitmapDrawable inittitle_value = new RecyclingBitmapDrawable(getResources(), Tools.init_titlebit);
	    	societysetWindow.setTitleImg(inittitle_value);         //设置默认的头像图片
	    	
	    	final MyHandler handler0 = new MyHandler(this,0,from);
	    	
	    	final Handler handler1 = new Handler(){
	    		@Override
	    		public void handleMessage(Message msg){
	    			switch(msg.what)
	    			{
	    			case 0:
	    				  // 进度条消失
	    				final RecyclingBitmapDrawable bit_value = new RecyclingBitmapDrawable(getResources(), (Bitmap)msg.obj);
	    				Init_choose.fixedthreadpool.execute(new Runnable() {				
	    					@Override
	    					public void run() {
	    						// TODO 自动生成的方法存根
	    						
	    						Init_choose.asynloader.addBitmapToMemoryCache(Tools.titleSaved_path + Tools.title_name + 
	    								Tools.identity.substring(Tools.identity.indexOf("_") + 1) + ".png", bit_value);
	    						Tools.saveTitleImg(bit_value.getBitmap(),handler0);          //通过网络获取头像后加入缓存和本地文件夹
	    					}
	    				});
	    				if(societysetWindow != null)
	    				{
	    					societysetWindow.setTitleImg(bit_value);	
	    				}
	    				break;
	    			case 1:
	    				//精度条消失
	    				break;
	    			}
	    		}
	    	};
	    			
	    	final Handler mhandler = new Handler()
	    	{
	    		@Override
	    		public void handleMessage(Message msg)
	    		{
	    			switch(msg.what)
	    			{
	    			case 0:
	    				//进度条消失

	    				break;
	    			case 1:
	    				final String title_url = (String)msg.obj;            
	    				Log.i("title_url",title_url);
	    				
	    				Init_choose.fixedthreadpool.execute(new Runnable() {     //通过url地址获取头像
							
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								InputStream in = HttpUtil.getStreamFromURL(title_url);
								int length = Tools.Dp2Px(70, scale);
								
			    				Bitmap bit = Tools.getBitmap(length, length,in, false, null,null,0);
								if(bit != null)
								{
									Log.i("bitmap","not null");
									Message msg = handler1.obtainMessage(0,bit);
									handler1.sendMessage(msg);
								}
								else
								{
									Message msg = handler1.obtainMessage(1);
									handler1.sendMessage(msg);
								}
							}
						});
	    				
	    			}
	    		}
	    	};
	    	Init_choose.fixedthreadpool.execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					String titleUrl = HttpUtil.getTitleURL(MainActivity.this,HttpUtil.getTitleImg,mhandler,0); //访问获取头像的url地址
					if(titleUrl != null)
					{
						Message msg = mhandler.obtainMessage(1,titleUrl);
						mhandler.sendMessage(msg);
					}
				}
			});
	    }
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){   //监听返回键
		
		if(keyCode == KeyEvent.KEYCODE_BACK && isCurrent == true && bottomslider.getVisibility() == View.VISIBLE){
			bottomslider_gone();
			isCurrent = false;
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == REQUSTCODE)                        //从在线社区获取背景图片
		{
			if(20 == resultCode){
				
				Bundle bundle = data.getExtras();
				
				setInitPicUrl(bundle.getString("initpicUrl"));
				setInitPic(bundle.getString("initpic"));
				setImageFrom(Tools.SOCIETY);
				/*
				if(myView == null)
				{
					myView = new MyView(MainActivity.this, viewWidth,viewHeight, tag, null, null,false);
					myView.setBackgroundColor(Color.TRANSPARENT);
				}
			    */
				RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(myView, initpicurl, Init_choose.fixedthreadpool, 
						viewWidth, viewHeight, true, null);
				if(value != null)
				{
					Bitmap bit = value.getBitmap();
					if(bit != null)
					{
						Log.i("MainActivity_onActivityResult","bit!=null");
						if(myView != null)
						{
							Log.i("MainActivity_onActivityResult","value!=null");
						    myView.setImageBitmap(bit);
						    isActivityRecover = false;
						}
					}
				}
			
				 Tools.refresh_arr.clear();
				 Tools.refresh_arr.add(0);
				 Tools.refresh_arr.add(1);
				 for(int i=0;i<UnDoDraw.saved.size();i++)
				    Tools.refresh_arr.add(i+2);
				 Tools.clearMemoryCache();
			}
		}
		if(requestCode == IMAGE_CODE)                   //从手机相册获取背景图片
		{
			if(resultCode == RESULT_OK)
			{
				
				ContentResolver resolver = getContentResolver();
				Cursor cursor;
				String path = null;
				Uri imageUri = data.getData();
				
				String[] proj = {MediaStore.Images.Media.DATA};
				cursor = resolver.query(imageUri, proj,null, null, null);
		//		cursor = managedQuery(imageUri, proj, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				path = cursor.getString(column_index);
				if(cursor != null)
					cursor.close();
				
				Handler mhandler = new Handler(){
					@Override
					public void handleMessage(Message msg)
					{
						switch(msg.what)
						{
						case 0:
							RecyclingBitmapDrawable mvalue = (RecyclingBitmapDrawable)msg.obj;
							if(mvalue != null)                 //背景图片裁剪窗口，对从手机相册获取的图片进行裁剪到适合屏幕大小
							{
								adjustWindow = new AdjustSizeofInitpicWindow(1, MainActivity.this, mvalue, sure_onClick, cancel_onClick, viewWidth, viewHeight, screenHeight);
								adjustWindow.showAtLocation(MainActivity.this.findViewById(R.id.theAll),Gravity.LEFT, 0, 0);  
							}
							break;
						}
					}
				};
				if(path == null)
				    Log.i("MainActivity_OnActivityResult","IMAGE_CODE");
				if(path != null)
					Init_choose.asynloader.getAdustedBitmap(1, path,Init_choose.fixedthreadpool, mhandler, viewWidth, viewHeight); //根据手机相册中图片的uri路径获取图片	
			}
		}
	}

	public void deleteView()            //删除当前涂鸦作品
	{
		
		if(UnDoDraw.saved1.size() > 0)
			UnDoDraw.saved1.clear();
		if(ReDoDraw.redoList1.size() > 0)
			ReDoDraw.redoList1.clear();
		
		Tools.refresh_arr.clear();
		Tools.refresh_arr.add(0);
		Tools.refresh_arr.add(1);
		for(int i=0;i<UnDoDraw.saved.size();i++)
		{
			Tools.refresh_arr.add(i + 2);
		}
		Tools.clearMemoryCache();
		
		UnDoDraw.saved.clear();
		ReDoDraw.redoList.clear();
		
		linearlayout.removeAllViews();
		myView.alldismiss();
		myView = null;
		showMyView(tag, null,null);
	}
	
	private void registerBroadCast()  
	{
		receiveBroadcase = new ReceiveBroadCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Tools.Colorchange_flag);
		registerReceiver(receiveBroadcase, filter);
	}
  
	@Override
	public void onDestroy()
	{
		Log.i("MainActivity","ondestroy");
		if(receiveBroadcase != null)
			unregisterReceiver(receiveBroadcase);
		
		if(linearlayout != null)
		{
			linearlayout.removeAllViews();
			linearlayout = null;
		}
		if(myView != null)
		{
			myView.alldismiss();
			myView = null;
		}
		ReDoDraw.redoList.clear();
		UnDoDraw.saved.clear();
		ReDoDraw.redoList1.clear();
		UnDoDraw.saved1.clear();
		
		PenSizeBar_position = 0;
	    Transparency_position = 0;
		SVB_position = 0;
		Opacity_position = 0;
		super.onDestroy();
	}
	@Override
	public void onPause()
	{
		super.onPause();
		Log.i("MainActivity","onPause");
		if(myView != null)
			myView.setDrawTool(myView.actualRecord);
		
		if(isToGallery || isToLocal)
		{
			Log.i("MainActivity_onPause","dismiss");
			if(linearlayout != null)
			    linearlayout.removeAllViews();
			myView.alldismiss();
			System.gc();
		}
//		myView = null;
	}

	class ReceiveBroadCast extends BroadcastReceiver       //监听画笔变化
	{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO 自动生成的方法存根
			int color = arg1.getIntExtra("color", Color.RED);
			if(currentColor != color)
			{
				currentColor = color;
				myView.setPenColor();
			}
		
		    int size = 	arg1.getIntExtra("thickness", 15);
			if(MainActivity.graphicOrtext == MainActivity.GRAPHY)
			{
				if(thickness != size)
				{
					thickness = size;
					myView.setPenThickness();
				}
			}
			else
			{
				if(textSize != size)
				{
					textSize = size;
					myView.setPenThickness();
				}
			}
			
			int alpha = arg1.getIntExtra("alpha", 100);
			if(transparency != alpha)
			{
				transparency = alpha;
				myView.setPenAlpha();
			}
		}
	}
	
	 private void localsave()        //本地保存当前的涂鸦作品
	 {
		 if(myView == null)
				return;
			MyHandler myhandler = new MyHandler(this,0,from);
			if(imageFrom == Tools.LOCAL)
				myView.localsaveImg(screenHeight,null,myhandler);
			else
				myView.localsaveImg(screenHeight,initpic,myhandler);	
	 }
	 public void saveThemeImage()
	 {
		 setThemeImg task = new setThemeImg();
		 task.execute("");
	 }
	 @Override
	 public void onBackPressed()           //退出涂鸦界面 MainActivity
	{			
		    if(progressDialog != null)
		    {
		    	Tools.closeProgress(progressDialog);
		    	progressDialog = null;
		    	return ;
		    }
		    
			final MySubmit_handler handler = new MySubmit_handler(this,from);
			if(from == 2 || from == 4)                                          //from == 2 来自init-社区画廊-加号
			{
				if((submitRecord != UnDoDraw.currentsaved.size() || hasDrawn_submit == true ) && myView != null)
				{
					if(UnDoDraw.currentsaved.size() > 0)
					{
						if(Tools.isNetConnected(MainActivity.this))
						{
							showAlertDialog(R.string.submit_alert, new DialogInterface.OnClickListener() {					
								@Override
								public void onClick(DialogInterface arg0, int arg1){
									// TODO 自动生成的方法存根
									myView.submit(initpic,imageFrom,handler);
									arg0.dismiss();
									MainActivity.this.finish();
								}
							});
						}
						else
						{
							showNoSdCardOrInternet_AlertDialog(R.string.NoInternetNotSubmit);
						}
					}	
				}
				else
					MainActivity.this.finish();
				
				Log.i("from","2");		
			}
			if(from == 3 || from == 0 || from == 1)                                      //from == 3 来自 init-本地画廊-加号
			{
				if((saveRecord != UnDoDraw.currentsaved.size() || hasDrawn_save == true ) && myView != null)
				{
					if(UnDoDraw.currentsaved.size() > 0)
					{
						if(Tools.hasSdCard())
						{
							showAlertDialog(R.string.save_alert, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO 自动生成的方法存根
									localsave();
								    arg0.dismiss();
		                            MainActivity.this.finish();
								}
							});
						}
						else
						{
							showNoSdCardOrInternet_AlertDialog(R.string.NoSdCardNotSave);
						}
					}	
				}
				else
					MainActivity.this.finish();
			}
    }
	 private void showAlertDialog(int MessageResid ,DialogInterface.OnClickListener sureOnclick)
	 {
		 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.application_title))
			.setMessage(getString(MessageResid)).setPositiveButton(getString(R.string.yes),sureOnclick)
			.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO 自动生成的方法存根
					arg0.dismiss();
					MainActivity.this.finish();
				}
			});
//		 builder.setCancelable(false);
		 builder.show();
	 }
	 private void showNoSdCardOrInternet_AlertDialog(int MessageResid)
	 {
		 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.application_title))
					.setMessage(getString(MessageResid)).setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO 自动生成的方法存根
							arg0.dismiss();
							MainActivity.this.finish();
						}
					});
		 builder.show();
	 }
	 public void showAlertDialog(int MessageResid ,DialogInterface.OnClickListener sureOnclick,DialogInterface.OnClickListener cancelOnClick)
	 {
		 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.application_title))
			.setMessage(getString(MessageResid)).setPositiveButton(getString(R.string.yes),sureOnclick)
			.setNegativeButton(getString(R.string.no),cancelOnClick);
		 builder.setCancelable(false);
		 builder.show();
	 }
	 @Override
		public void finish()                        //activity推出前进行必要的清除操作
		{
		    super.finish();
		    Tools.refresh_arr.clear();
		    Tools.refresh_arr.add(0);
		    Tools.refresh_arr.add(1);
		    for(int i=0;i<UnDoDraw.saved.size();i++)
		    	Tools.refresh_arr.add(i + 2);
		    
		    for(int i=0;i<Tools.traceRecord.size();i++)
		    {
				DrawbsObject dbo = Tools.traceRecord.get(i);		
				int index = dbo.firstindex;
				Tools.refresh_arr.add(index + 2);
		    }
		    Tools.clearMemoryCache();
		  
			thickness = 15;
			currentColor = Color.RED; 
			transparency = 100;
			eraser_thickness = 30;
			textSize = 110;
			hasDrawn_save = false;
			hasDrawn_submit = false;
			
			Log.i("MainActivity","finish");
		}
	 
	
	
	 public void sendBroadcast(String flag,String str_extra,String path)   //涂鸦完成后同步到本地画廊 或 在线画廊
	 {
		 Intent intent = new Intent();
		 intent.putExtra("destination", str_extra);
		 if(str_extra.equals("local_gallery"))
			 intent.putExtra("path", path);
		 intent.setAction(flag);
		 sendBroadcast(intent);
	 }
	 
	    private LocalOnClickListener  local_onClick = new LocalOnClickListener(this);
	    private SocietyOnClickListener society_onClick = new SocietyOnClickListener(this);
	    private BottomSliderOnClickListener bottomslider_onClick = new BottomSliderOnClickListener(this);
	    private TitleBtnOnClickListener titlebtn_onClick = new TitleBtnOnClickListener(this);
		private SubmitOnClickListener submit_OnclickListener = new SubmitOnClickListener(this);
	 	private ThemeSetOnClickListener themeSet_OnclickListener = new ThemeSetOnClickListener(this);
		private LocaldeleteOnClickListener localdelete_OnclickListener = new LocaldeleteOnClickListener(this);
		private RecoverOnClickListener recoverOnclick = new RecoverOnClickListener(this);
		private SaveOnClickListener saveOnclick = new SaveOnClickListener(this);
		private LayerOnItemClickListener layerOnclick = new LayerOnItemClickListener(this);
		private SureOnClickListener sure_onClick = new SureOnClickListener(this);
		private TextOnClickListener text_onClick = new TextOnClickListener(this,Tools.WriteText);
		private TextOnClickListener name_onClick = new TextOnClickListener(this, Tools.SetName);
		private OnClickListener remoteUpload_onClick = new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(societysetWindow != null)
				{
					societysetWindow.dismiss();
					Intent intent = new Intent(MainActivity.this, GenerateCode.class);
					startActivity(intent);
				}
			}
		};
		private OnClickListener localsave_onClickListener = new OnClickListener() {   //监听社区分先窗口的本地保存button
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根

			    if((UnDoDraw.currentsaved.size() != saveRecord || hasDrawn_save == true) && myView != null)
			    {
			    	if(UnDoDraw.currentsaved.size() > 0)
			    	{
			    		if(Tools.hasSdCard())
			    		{
			    			Tools.showMyProgress(MainActivity.this);
				    		localsave();
			    		}
			    		else
			    			Toast.makeText(MainActivity.this, getString(R.string.SetSdCardBeforeSave),3000).show();
			    		return ;
			    	}
			    }
			    Toast.makeText(MainActivity.this, getString(R.string.DrawBeforSave), 3000).show();	
			}
		};
	    private OnLongClickListener titleset_onLongClick = new OnLongClickListener() {  //监听社区分享窗口的 头像长按 功能：自定义头像

			@Override
			public boolean onLongClick(View arg0) {
				// TODO 自动生成的方法存根
				if(societysetWindow != null)
				{
					societysetWindow.dismiss();
					myView.auto_setTitleImg();
					titlebtn.setBackgroundResource(R.drawable.correct);
				}
				return false;
			}
		};
		private OnLongClickListener name_onLongClick = new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO 自动生成的方法存根
				writeTextWindow = new WriteTextPopupwindow(MainActivity.this, name_onClick, screenWidth, screenHeight);
				writeTextWindow.showAtLocation(MainActivity.this.findViewById(R.id.theAll),Gravity.LEFT, 0, 0);
				
				return false;
			}
		};
		private class setThemeImg extends AsyncTask<String, Integer, String>
		{
			 @Override
			 protected void onPreExecute()
			 {
				if(progressDialog == null)
				{
					progressDialog = Tools.showProgress(MainActivity.this);
					progressDialog.setTask(this);
				}
			 }
			@Override
			protected String doInBackground(String... params) {
				// TODO 自动生成的方法存根
				if(myView != null)
					return myView.saveThemeImg();
				return null;
			}
			@Override
			protected void onPostExecute(String result)
			{
				Tools.closeProgress(progressDialog);
				progressDialog = null;
				Toast.makeText(MainActivity.this, result,3000).show();
			}
			
		}
}
