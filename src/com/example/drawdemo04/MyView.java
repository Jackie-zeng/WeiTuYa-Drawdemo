package com.example.drawdemo04;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.example.drawdemo04.PenGraphycal.DrawBS;
import com.example.drawdemo04.PenGraphycal.DrawCircle;
import com.example.drawdemo04.PenGraphycal.DrawColumn;
import com.example.drawdemo04.PenGraphycal.DrawCube;
import com.example.drawdemo04.PenGraphycal.DrawLine;
import com.example.drawdemo04.PenGraphycal.DrawPath;
import com.example.drawdemo04.PenGraphycal.DrawRect2;
import com.example.drawdemo04.PenGraphycal.DrawRectangle;
import com.example.drawdemo04.PenGraphycal.DrawText;
import com.example.drawdemo04.PenGraphycal.DrawTriangle;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;
import com.example.drawdemo04.SocietyModel.HttpUtil;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.initModel.Init_choose;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

    public class MyView extends View implements Serializable{

	private DrawBS drawBS = new DrawBS();

	private Point evevtPoint;
	public Bitmap backBitmap;
	private Canvas backCanvas;
	public Bitmap floorBitmap;// 底层与表层bitmap
	private Canvas floorCanvas;// bitmap对应的canvas
    public Bitmap surfaceBitmap;
    private Canvas surfaceCanvas;
	private boolean isEraser = false;
	private int width,height;
	Bitmap newbm;
    private Canvas mcanvas;
    private int changeCount=0;
    private boolean unDo=false;
    private boolean reDo=false;
    private Context context;
    public int actualRecord = 0;
    private boolean door = true;
    private boolean textDoor = true;
    private Bitmap mbitmap;
    private int real_record = 0;
    private int tag;                  // 表示myView画板画的是图层， tag = -2 表示完整涂鸦图，tag >= 0表示某一涂鸦层
    private boolean isdrawSurface = true;
    private int beginX, beginY, textWidth;
    private String text;
    private int textAera = 0;
    private boolean drawingText = false;
    private MyHandler handler;
    public Bitmap floorBit = null;
    private int firstSucceed = 0;
 //   private RecyclingBitmapDrawable oldValue;
    
	public MyView(Context context,int width,int height,int tag,Bitmap backbit,Bitmap floorbit,boolean isCreated) {
		
		super(context);
		this.context=context;
		
		this.width=width;
		this.height=height;
        this.tag = tag;
        
        handler = new MyHandler();
//		drawBS = new DrawPath();
		evevtPoint = new Point();
		Log.i("MyView_constructor","MyView");
	
		if(isCreated == true)
		{
			if(backbit == null)
				backBitmap = Bitmap.createBitmap(width,height, Tools.config);
			else
				backBitmap = adjust(backbit);

			if(floorbit == null)
				floorBitmap = Bitmap.createBitmap(width,height, Tools.config);
			else
			{
				floorBitmap = adjust(floorbit);
				floorBit = Bitmap.createBitmap(floorBitmap);
			}
			floorCanvas = new Canvas(floorBitmap);
			
			surfaceBitmap = Bitmap.createBitmap(width,height, Tools.config);
			
			surfaceCanvas = new Canvas(surfaceBitmap);
			Log.i("MyView_constructor","surfaceCanvas");
			
			preWork(tag);
		}
	}
	public MyView(Context context,int width,int height,int tag)
	{
		super(context);
		this.width=width;
		this.height=height;
        this.tag = tag;
        
        handler = new MyHandler();
		drawBS = new DrawPath();
		evevtPoint = new Point();
	}
    public void preWork(int tag){      //根据tag 做一些预定于的涂鸦操作
    	
    	UnDoDraw.choosesaved(tag);
    	ReDoDraw.chooseredoList(tag);
    		
    	if(tag != -2 && tag != -1 && tag != -3)
    	{
    		if(UnDoDraw.saved.size() > tag)
    		{
    			drawBS = UnDoDraw.saved.get(tag);
    			drawBS.reDraw(floorCanvas);
    		}
    	}
    	if(tag == -2){
    		
    		for(int i=0;i<UnDoDraw.saved.size();i++){
    			drawBS = UnDoDraw.saved.get(i);
    			if(drawBS != null && floorCanvas != null)
    			   drawBS.reDraw(floorCanvas);
    		}
    	}
 //   	chooseTools();
    	
    }
    
    public void drawWork(int tag){
    	
  //  	surfaceBitmap.recycle();
  //	surfaceBitmap= Bitmap.createBitmap(width,height, Tools.config);                                             ////////////////////////////////////
    	surfaceBitmap.eraseColor(Color.TRANSPARENT);
		surfaceCanvas=new Canvas(surfaceBitmap);
		
		floorBitmap = getFloorBitmap();
	   	floorCanvas.setBitmap(floorBitmap);
	   	if(tag == -2)
	   	{
	   		if(UnDoDraw.saved.size() > 0)
	   		{
	   			for(int i=0;i<UnDoDraw.saved.size();i++){
	   				drawBS = UnDoDraw.saved.get(i);
	   				drawBS.reDraw(floorCanvas);
	   			}
	   		}
	   	}
    	if(tag == -1)
    	{
    		Log.i("index",String.valueOf(UnDoDraw.currentsaved.size()));
    		if(UnDoDraw.currentsaved.size() > 0)
    		{
    			for(int i=0;i<UnDoDraw.currentsaved.size();i++){
        	   		drawBS = UnDoDraw.currentsaved.get(i);
        	   		drawBS.reDraw(floorCanvas);
        	   	}
    		}
    		
    	}
    	else
    	{
    		if(tag != -2 && tag != -1)
        	{
        		if(UnDoDraw.saved.size() > tag)
        		{
        			drawBS = UnDoDraw.saved.get(tag);
        			drawBS.reDraw(floorCanvas);
        		}
        	}
    		for(int i=0;i<UnDoDraw.currentsaved.size();i++){
    	   		drawBS = UnDoDraw.currentsaved.get(i);
    	   		drawBS.reDraw(floorCanvas);
    	   	}	
    	}
    	
	   	chooseTools();
	   	invalidate();
    }
    private Bitmap getFloorBitmap()                //获取原始涂鸦层图片
    {
    	if(floorBit == null)
    		return Bitmap.createBitmap(width, height, Tools.config);
    	else
    		return Bitmap.createBitmap(floorBit);
    }
    public Bitmap getFloorBit()
    {
    	return floorBit;
    }
    public Bitmap adjust(Bitmap bitmap)        //根据myView的大小调节bitmap 大小
    {
    	if(bitmap != null)
    	{
    		float initwidth = bitmap.getWidth();
			float initheight = bitmap.getHeight();
            float newWidth = width;
            float newHeight = height;
            
            Matrix matrix = new Matrix();
            matrix.reset();
            float scaleWidth = newWidth/initwidth;
            float scaleHeight = newHeight/initheight;
            
            matrix.postScale(scaleWidth, scaleHeight);
        
            Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, (int)initwidth, (int)initheight,matrix,true);
            Log.i("MyView","adjust");
            return bit;
    	}
    	return null;
    }
    
	public void setImageBitmap(final Bitmap bitmap){               //设置背景图片
		
		if(bitmap != null){	 
			
			if(backBitmap != null)
				backBitmap.recycle();
			
			backBitmap = adjust(bitmap);
			
			if(backBitmap == null)
				Log.i("MyView","setImageBitmap");
			if(backBitmap == null || floorBitmap == null || surfaceBitmap == null)
			{
				return ;
			}
			invalidate();
		}
	}
	
	public void setBackBitmap(Bitmap bitmap)
	{
		if(bitmap != null){	     
			if(backBitmap != null)
				backBitmap.recycle();
			backBitmap = adjust(bitmap);
		}
	}
	
    @SuppressLint("WrongCall") public void onDraw(Canvas canvas) {
   
    	Log.i("MyView","onDraw");
    	// 将底层bitmap的图形绘制到主画布
    	if(drawingText)                 //如果是写文字 直接写在表层canvas surfaceCanvas
		{
    		drawBS.onDraw(surfaceCanvas);
			setDrawTool(real_record);
			drawingText = false;
			textAera = 0;
		}
		canvas.drawBitmap(backBitmap, 0, 0, null);     //画背景图
		canvas.drawBitmap(floorBitmap, 0, 0, null);    //画涂鸦层图

		// 判断选择的图形是否为橡皮
        if(isdrawSurface)
        {
        	if(!unDo && !reDo)
       	 {
       		if (isEraser) {

       			drawBS.onDraw(floorCanvas);
       		    canvas.drawBitmap(floorBitmap, 0, 0, null);
       			drawBS.eraserOnDraw(surfaceCanvas);
       		    canvas.drawBitmap(surfaceBitmap, 0, 0, null);
       			      //如果是橡皮擦，直接在涂鸦层画布floorCanvas上涂画
        //        canvas.restore();
       		} 
       		else 
       		{
       			Log.i("MyView_ondraw","isEraser=false");
       		    Paint paint = new Paint();
       		    drawBS.onDraw(surfaceCanvas);
       			canvas.drawBitmap(surfaceBitmap, 0, 0, null);     //画图先画在表层画布surfaceCanvas 上涂画
       			canvas.restore();
       		}
       	 }
       }
	    isdrawSurface = true;
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		evevtPoint.set((int) event.getX(), (int) event.getY());

		if(drawBS == null){
			return true;
		}

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			if(drawBS instanceof DrawRectangle)
			{
				if(((DrawRectangle)drawBS).isDrawingTextEdge == true)
				{
					if(drawBS.onTouchDown(evevtPoint) == true)
						invalidate();
					return true;
				}
			}
			drawBS.onTouchDown(evevtPoint);
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			if(drawBS instanceof DrawRectangle)
			{
				if(((DrawRectangle)drawBS).isDrawingTextEdge == true)
				{
					if(drawBS.onTouchMove(evevtPoint) == true)
					{
						if(drawBS.getTag()!=6)
						{
							Log.i("MyView","eraserColor_transparent");
						    surfaceBitmap.eraseColor(Color.TRANSPARENT);
						}
						invalidate();
					}
					return true;
				}
			}
            drawBS.onTouchMove(evevtPoint);
            
            if(drawBS.getTag()!=6)
			{
				Log.i("MyView","eraserColor_transparent");
			    surfaceBitmap.eraseColor(Color.TRANSPARENT);
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			if(drawBS.getTag()==10)
			{
			     drawBS.onTouchUp(evevtPoint,floorCanvas);
			     invalidate();
			}
			else
			{
				drawBS.onTouchUp(evevtPoint, surfaceCanvas);
			    invalidate();
			}
			MainActivity.hasDrawn_save = true;
			MainActivity.hasDrawn_submit = true;
			break;
		default:
			break;
		}
		return true;
	}
    
	private void chooseTools( ){
		  
		   if(drawBS instanceof DrawLine){
			   drawBS = new DrawLine();
			   drawBS.setTag(0);
			   actualRecord = 0;
		   }
		   else{
			   if(drawBS instanceof DrawRectangle){
				   drawBS = new DrawRectangle();
				   drawBS.setTag(1);
				   actualRecord = 1;
			   }
			   else{
				   if(drawBS instanceof DrawCircle){
					   drawBS = new DrawCircle();
					   drawBS.setTag(2);
					   actualRecord = 2;
				   }
				   else{
					   if(drawBS instanceof DrawTriangle){
						   drawBS = new DrawTriangle();
						   drawBS.setTag(3);
						   actualRecord = 3;
					   }
					   else{
						   if(drawBS instanceof DrawCube){
							   drawBS = new DrawCube();
							   drawBS.setTag(4);
							   actualRecord = 4;
						   }
						   else{
							   if(drawBS instanceof DrawColumn){
								   drawBS = new DrawColumn();
								   drawBS.setTag(5);
								   actualRecord = 5;
							   }
							   else{
								   drawBS = new DrawPath();
								   drawBS.setTag(6);
								   actualRecord = 6;
							   }
						   } 
					   }
				   }
			   }
		   }
		   changeCount ++;
		   isEraser = false;
	   }

	public void setDrawTool(int i)     //设置画笔工具
{
		if(door == true && textDoor == true)
	{
		if(changeCount>=1 && drawBS.getTag()!=7 && drawBS.getTag()!=8 && drawBS.getTag() != 9 && drawBS.getTag() != 11)
		{
			if(drawBS.ifDone)
			{
				UnDoDraw.currentsaved.add(drawBS);
				ReDoDraw.currentredoList.clear();
			}
		}
		if(i==7 && UnDoDraw.currentsaved.size()==0)
		{
			Toast.makeText(context, "No undo",3000).show();
			return ;
		}
		if(i==8 && ReDoDraw.currentredoList.size()==0)
		{
			Toast.makeText(context, "No redo",3000).show();
			return ;
		}
		if(drawBS.getTag() != 10)
		    floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
		
//		surfaceBitmap.recycle();                                                            ///////////////////////////////////////
//		surfaceBitmap= Bitmap.createBitmap(width,height, Tools.config);
		surfaceBitmap.eraseColor(Color.TRANSPARENT);
		surfaceCanvas=new Canvas(surfaceBitmap);
		
		switch (i) {
		case 0:
			drawBS = new DrawLine();
			break;
		case 1:
			drawBS = new DrawRectangle();
			break;
		case 2:
			drawBS = new DrawCircle();
			break;
		case 3:
			drawBS = new DrawTriangle();
			break;
		case 4:
			drawBS = new DrawCube();
			break;
		case 5:
			drawBS = new DrawColumn();
			break;
		case 6:
			drawBS = new DrawPath();
			break;
		case 7:
			//undo
			drawBS=new UnDoDraw();
			unDo=true;
			break;
		case 8:
			drawBS=new ReDoDraw();
			reDo=true;
			break;
		case 9:                       //自定义头像
			mbitmap = composeTitleImg();    //合成头像
			drawBS = new UnDoDraw(); 
			unDo = true;
			break;
		case 10:
			drawBS = new DrawPath(10,(int)(width/2),(int)(height/2));// 橡皮
	//		drawBS.autoDrawEraser(surfaceCanvas);
	//		invalidate();
			break;
		case 11:
			computeTextRange();         //计算文字方框的大小
			drawBS = new UnDoDraw();
			unDo = true;
			break;
		case 12:
			drawBS = new DrawText(beginX, beginY, textWidth, text);
			drawingText = true;
			invalidate();
			break;
		}
		
		drawBS.setTag(i);
		
        if(i!=7 && i!=8 && i!=10)
        {
        	changeCount++;
        	actualRecord=i;
        }

		if (i == 10) {
			isEraser = true;
		} else {
			isEraser = false;
		}
//		floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
		
		if(i==7)
		{
			undo();
		}
		if(i==8)
			redo();
		
		if(i == 9)          //去除长方形相框
			repaint(0);
		if(i == 11)         //去除长方形文字框
			repaint(1);
	}
}

	public void redo()       
	{
		drawBS.redo(floorCanvas);
		invalidate();
		reDo=false;
		setDrawTool(actualRecord);
	}
	public void undo()
	{
		floorBitmap.recycle();
		floorBitmap = getFloorBitmap();
	   	floorCanvas.setBitmap(floorBitmap);
	   	
	   	if(tag != -2 && tag != -1)
    	{
    		if(UnDoDraw.saved.size() > tag)
    		{
    			UnDoDraw.saved.get(tag).reDraw(floorCanvas);
    		}
    	}
	   	
	   	drawBS.undo(floorCanvas);
	   	isdrawSurface = false;
	    invalidate();
	    unDo=false;
	    setDrawTool(actualRecord);
	}
	
	private void  repaint(int judge){   //取消自定义头像的相框和文字功能的文字框
		
		floorBitmap.recycle();
		floorBitmap = getFloorBitmap();
	   	floorCanvas.setBitmap(floorBitmap);
	   	
	   	if(tag != -2 && tag != -1)
    	{
    		if(UnDoDraw.saved.size() > tag)
    		{
    			UnDoDraw.saved.get(tag).reDraw(floorCanvas);
    		}
    	}
	   	drawBS.repaint(floorCanvas);
	   	invalidate();
	   	unDo = false;
	   	
	   	if(judge == 0)
	   		setDrawTool(real_record);
	   	else
	   		setDrawTool(12);  	  
	}

    @Override
    public void onDetachedFromWindow(){
    	super.onDetachedFromWindow();
		
    } 
    public void submit(final String largepicid,final int imageFrom,final Handler handler)  //上传myView 画板图片
    {
    	floorCanvas.drawBitmap(surfaceBitmap, 0,0, null);
        
    	Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				if(imageFrom == Tools.SOCIETY)
				{
					Bitmap submit_bit = Tools.zoomBitmap(floorBitmap, Tools.scaleWidth, Tools.scaleHeight);
			        
			    	Bitmap PaintBitmap = Bitmap.createBitmap(Tools.initWidth,Tools.initHeight, Tools.config);
			        Canvas paintCanvas = new Canvas(PaintBitmap);      
			        paintCanvas.drawBitmap(submit_bit, Tools.scaleX, Tools.scaleY, null);
			        /*       
	    		    Matrix matrix = new Matrix();
			        matrix.setScale(Tools.scale, Tools.scale);
			        Bitmap mbit = Bitmap.createBitmap(PaintBitmap, 0, 0, PaintBitmap.getWidth(), PaintBitmap.getHeight(),matrix,true);
			        PaintBitmap.recycle();
			        PaintBitmap = mbit;
			        */
					HttpUtil.submitImage(context,HttpUtil.submit_SocietyImageURL, PaintBitmap, handler,largepicid,firstSucceed);
				}
				else
				{
					Bitmap bitmap = Bitmap.createBitmap(backBitmap).copy(Tools.config, true);
					Canvas canvas = new Canvas(bitmap);
					canvas.drawBitmap(floorBitmap, 0, 0, null);
					
					Bitmap submit_bit = Tools.zoomImage(bitmap, Tools.submitStandard_width, Tools.submitStandard_height);
					
					HttpUtil.submitImage(context,HttpUtil.submit_LocalImageURL, submit_bit, handler, null,firstSucceed);
					bitmap.recycle();
					submit_bit.recycle();       ///////
				}
			}
		});
    }
    public void localsaveImg(int screenHeight,String initpic,Handler mhandler) {   
    	
    	floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
    	String configInfo;
    	if(initpic != null)
    		configInfo = Tools.scaleWidth + "/" + Tools.scaleHeight + "/" +  
    		    	Tools.initWidth + "/" + Tools.initHeight + "/" + Tools.scaleX + "/" + Tools.scaleY + "/" + initpic;
    	else
    		configInfo = null;
    	
    	float scale = getResources().getDisplayMetrics().density;
    	
    	Bitmap backBit = Bitmap.createBitmap(backBitmap);
    	Bitmap floorBit = Bitmap.createBitmap(floorBitmap);
		Tools.saveImage(floorBit, backBit,configInfo,width,height,screenHeight,Tools.Dp2Px(6,scale),mhandler);
	}
    public String saveThemeImg()
    {
    	floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
    	Bitmap backBit = Bitmap.createBitmap(backBitmap);
    	Canvas canvas = new Canvas(backBit);
    	canvas.drawBitmap(floorBitmap, 0, 0, null);
    	
    	Bitmap bit = Tools.zoomBitmap(backBit, width*0.5, height*0.5);
    	backBit.recycle();
    	backBit = bit;
    	
    	String result = Tools.saveImg(backBit, Tools.themeSaved_path, Tools.themePic_path, null);
    	RecyclingBitmapDrawable theme_value = new RecyclingBitmapDrawable(context.getResources(), backBit);
    	String imgURL = Tools.themeSaved_path + Tools.themePic_path + ".png";
    	Init_choose.asynloader.addBitmapToMemoryCache(imgURL,theme_value);
    	
    	return result;
    }
    public void auto_setTitleImg(){
    	
    	real_record = actualRecord;
    	setDrawTool(1);	
    	door = false;

    	drawBS.drawEdge(surfaceCanvas, width, height,0,0);
    	invalidate();
    }
    public Bitmap auto_finishTitleImg()
    {
      	if(door == false)
      {     
        door = true;
        setDrawTool(9);
         
      	return mbitmap;
      }
      	return null;
   }
    private Bitmap composeTitleImg(){

    	Bitmap secondBackBitmap = Bitmap.createBitmap(backBitmap);
    	backCanvas = new Canvas(secondBackBitmap);
    		
    	backCanvas.drawBitmap(floorBitmap, 0, 0, null);
    	
    	float scale = getResources().getDisplayMetrics().density;
    	float newWidth = Tools.Dp2Px(70, scale);
    	float newHeight = Tools.Dp2Px(70, scale);
    	
    	int startX = ((DrawRectangle)drawBS).point1.x;
    	int startY = ((DrawRectangle)drawBS).point1.y;
    	int endX = ((DrawRectangle)drawBS).point2.x;
    	int endY = ((DrawRectangle)drawBS).point2.y;
    	
    	if(startX < 0)
    		startX = 0;
    	if(startY < 0)
    		startY = 0;
    	if(endX > width)
    		endX = width;
    	if(endY > height)
    		endY = height;
    	
     	Bitmap bit = Tools.zoomSpecificImage(secondBackBitmap, newWidth, newHeight, startX,startY ,
    			endX - startX,endY - startY);
     	return bit;
    	/*
    	if(!(((DrawRectangle)drawBS).point1.x >0 && ((DrawRectangle)drawBS).point1.y>0 &&
    			((DrawRectangle)drawBS).point2.x < width && ((DrawRectangle)drawBS).point2.y < height))
    		Toast.makeText(context, R.string.alert, 3000).show();
   // 	else
  //    {
     	Bitmap bit = Tools.zoomSpecificImage(secondBackBitmap, newWidth, newHeight, ((DrawRectangle)drawBS).point1.x,((DrawRectangle)drawBS).point1.y ,
    			((DrawRectangle)drawBS).point2.x-((DrawRectangle)drawBS).point1.x,((DrawRectangle)drawBS).point2.y-((DrawRectangle)drawBS).point1.y);
    	
 //   	backBitmap.recycle();
  //  	backBitmap = secondBackBitmap;
    	
    	return bit;
  //    }
        */
    }
    
    public void auto_setTextImg(String text){
    	
    	real_record = actualRecord;
    	setDrawTool(1);
    	textDoor = false;
    	
    	this.text = text;
    	
    	TextPaint textPaint = new TextPaint();
    	textPaint.setTextSize(MainActivity.textSize);
    	int textWidth = (int)(Math.ceil(textPaint.measureText(text)));
    	int textHeight = Tools.getFontHeight(MainActivity.textSize);
        textAera = textHeight*textWidth;
       
        surfaceBitmap.eraseColor(Color.TRANSPARENT);
    	drawBS.drawEdge(surfaceCanvas, width, height,textWidth,textHeight);
    	invalidate();
    }
    public boolean auto_finishTextImg(){
    	if(textDoor == false)
    	{
    		int actualWidth = ((DrawRectangle)drawBS).point2.x-((DrawRectangle)drawBS).point1.x;
    		int acutalHeight = ((DrawRectangle)drawBS).point2.y-((DrawRectangle)drawBS).point1.y;
    		if(actualWidth * acutalHeight < textAera)
    		{
    			Toast.makeText(context, context.getString(R.string.adjustRectangleSize), 3000).show();
    			return false;
    		}
    
    		textDoor = true;
    		setDrawTool(11);
    		return true;
    	}
    	return false;
    }
    
    public void setPenColor()
    {
    	drawBS.setPenColor();
    }
    public void setPenThickness()
    {
    	drawBS.setPenThickness();
    	
    	if(textAera > 0)
    	{
    		TextPaint textPaint = new TextPaint();
        	textPaint.setTextSize(MainActivity.textSize);
        	int textWidth = (int)(Math.ceil(textPaint.measureText(text)));
        	int textHeight = Tools.getFontHeight(MainActivity.textSize);
            textAera = textHeight*textWidth;
            
            surfaceBitmap.eraseColor(Color.TRANSPARENT);
        	drawBS.drawEdge(surfaceCanvas, width, height,textWidth,textHeight);
        	invalidate();
    	}
    	
    }
    public void setPenAlpha(){
    	drawBS.setPenAlpha();
    }
    public void setEraseThickness(){
    	drawBS.setEraserThickness();
//    	if(drawBS.getTag() == 10)
//   		invalidate();
    }
    public void setMaskFilter(){
    	drawBS.setMaskFilter();
    }
    
   private void computeTextRange()
   {
	   beginX = ((DrawRectangle)drawBS).point1.x;
	   beginY = ((DrawRectangle)drawBS).point1.y;
	   textWidth = ((DrawRectangle)drawBS).point2.x-((DrawRectangle)drawBS).point1.x;
   }
   public Bitmap getBackBitmap()
   {
	   return backBitmap;
   }

   private class MyHandler extends Handler
   {
	   @Override
	   public void handleMessage(Message msg)
	   {
		   switch(msg.what)
		   {
		   case 0:
			   Toast.makeText(context, context.getString(R.string.failTosubmit), 3000).show();
			   break;
		   case 1:
			   Toast.makeText(context, context.getString(R.string.successTosubmit), 3000).show();
			   break;
		   }
	   }
   }
   public void dismiss()
   {
	   if(!Tools.isHigherthan10())
	   {
		   if(floorBitmap != null && !floorBitmap.isRecycled())
		   {
			   floorBitmap.recycle();
			   floorBitmap = null;
		   }
		   if(surfaceBitmap != null && !surfaceBitmap.isRecycled())
		   {
			   surfaceBitmap.recycle();
			   surfaceBitmap = null;
		   }
	   }  
   }
   public void alldismiss()
   {
	   if(backBitmap != null && !backBitmap.isRecycled())
	   {
		   backBitmap.recycle();
		   backBitmap = null;
	   }
	   if(floorBit != null && !floorBit.isRecycled())
	   {
		   floorBit.recycle();
		   floorBit = null;
	   }
	   if(floorBitmap != null && !floorBitmap.isRecycled())
	   {
		   floorCanvas = null;
		   floorBitmap.recycle();
		   floorBitmap = null;
	   }
	   if(surfaceBitmap != null && !surfaceBitmap.isRecycled())
	   {
		   surfaceCanvas = null;
		   surfaceBitmap.recycle();
		   surfaceBitmap = null;
	   }
//	   dismiss();
   }
   
   public void save(Handler mhandler)
   {
	   Log.i("Myview_save","add");
	   /*
	   if(!Tools.isHigherthan10())
	   {
		   if(floorBit != null && !floorBit.isRecycled())
		   {
		       RecyclingBitmapDrawable floorBitDrawable = new RecyclingBitmapDrawable(context.getResources(), floorBit);
			   Init_choose.asynloader.addBitmapToMemoryCache(Tools.floor_bit_cache, floorBitDrawable);	   
		   }
		   
		   if(backBitmap != null && !backBitmap.isRecycled())
		   {
			   RecyclingBitmapDrawable backDrawable = new RecyclingBitmapDrawable(context.getResources(),backBitmap);
			   Init_choose.asynloader.addBitmapToMemoryCache(Tools.back_cache, backDrawable);
		   }
	   }
	   */
	   Init_choose.asynloader.addBitmapToDirCache(backBitmap,floorBit,mhandler);
   }
   
   public void restore()
   {
	   if(floorBit == null || floorBit.isRecycled())
	   { 
		   RecyclingBitmapDrawable floorDrawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMem(Tools.floor_bit_cache);
		   Bitmap bit = restore_getBit(floorDrawable,Tools.floor_bit_cache);
		   if(bit != null)
	  	   { 
	  		   Log.i("MyView_restoreGetBit","bitnotnull");
	  		   floorBit = Bitmap.createBitmap(bit).copy(Tools.config, true);
	  		   
	  		   if(!Tools.isHigherthan10() && floorDrawable == null)
	  		   {
	  			   Log.i("MyView_restoreGetBit","floorBitmapnotnull");
	  			   bit.recycle();
	  			   bit = null;
	  		   } 		   
	  	   }	       	   
	   }
	    if(backBitmap == null || backBitmap.isRecycled())
	    {
	    	RecyclingBitmapDrawable backDrawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMem(Tools.back_cache);
			Bitmap bit = restore_getBit(backDrawable,Tools.back_cache);
			if(bit != null)
		  	{ 
		  		 Log.i("MyView_restoreGetBit","bitnotnull");
		  		 backBitmap = Bitmap.createBitmap(bit).copy(Tools.config, true);
		  		   
		  		 if(!Tools.isHigherthan10() && backDrawable == null)
		  		{
		  			Log.i("MyView_restoreGetBit","floorBitmapnotnull");
		  			bit.recycle();
		  			bit = null;
		  		 }		   
		  	}
			else
			{
				backBitmap = Bitmap.createBitmap(width, height, Tools.config);
			}
			Log.i("MyView_restore","backBitmap");
	    }
	 
	   if(surfaceBitmap == null || surfaceBitmap.isRecycled())
	   {
			surfaceBitmap = Bitmap.createBitmap(width,height, Tools.config);                 ////////////////////////////////////////////
	  //    surfaceBitmap.eraseColor(Color.TRANSPARENT);
			surfaceCanvas = new Canvas(surfaceBitmap);
			Log.i("MyView_restore","surfaceBitmap");
	   }
	   if(floorBitmap == null || floorBitmap.isRecycled())
	   {
		   if(floorBit != null)
			   floorBitmap = Bitmap.createBitmap(floorBit);
		   else
			   floorBitmap = Bitmap.createBitmap(width, height, Tools.config);
		   
		   floorCanvas = new Canvas(floorBitmap);
	   }
   }
    private Bitmap restore_getBit(RecyclingBitmapDrawable floorDrawable,String path)
    {
    	Bitmap bit;
    	boolean judge = false;
        if(floorDrawable == null || (floorDrawable != null && (floorDrawable.getBitmap() == null || floorDrawable.getBitmap().isRecycled())))
  	   {
        	Log.i("MyView_restore_getBit","fromDirCache");
        	judge = true;
  		    bit = Init_choose.asynloader.getBitmapFromDirCache(path);
  	   }
  	   else
  	   {
  		   Log.i("MyView_restore_getBit","fromMemCache");  		
  		   bit = floorDrawable.getBitmap();
  	   }
  	   return bit;
    }
}
