package com.example.drawdemo04.PenGraphycal;

import java.util.ArrayList;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;

public class DrawPath extends DrawBS {

	private Path path;
    private Bundle bundle;
	private float mX, mY;
	public paintPath paintpath;
    private int distance = 5;
    private int initx;
    private int inity;
    public boolean ifEraser = false;
    
    private Paint eraserpaint ;
    private int radius;
    private int eraserX,eraserY;
//    private int downState = 0;
    
	public DrawPath() {
		// TODO Auto-generated constructor stub
		ifEraser = false;
		paint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	//如果选择橡皮，则需要给画笔重新赋值
	@SuppressLint("ResourceAsColor") public DrawPath(int i,int X,int Y) {

		paint.setAntiAlias(true);
		paint.setDither(true);
//		paint.setColor(Color.BLUE);
//		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.SQUARE);
		paint.setStrokeWidth(MainActivity.eraser_thickness);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		paint.setMaskFilter(null);
        ifEraser = true;
	//	bundle = new Bundle();
        
        eraserpaint = new Paint();
		eraserpaint.setStyle(Style.FILL);// 设置非填充
		eraserpaint.setColor(R.color.transparent2);// 设置为红笔
		eraserpaint.setAntiAlias(true);// 锯齿不显示
	//  radius = (int)(MainActivity.eraser_thickness/2.0);
		radius = (int)(Tools.scale*10)*2;
		Log.i("DrawPath_scale1",String.valueOf(Tools.scale));
		eraserX = X;
		eraserY = Y;
		
	}
	

	public boolean onTouchDown(Point point) {

		eraserX = point.x;
		eraserY = point.y;
		Log.i("DrawPath_scale2",String.valueOf(Tools.scale));
		radius = (int)(Tools.scale*10)*2;
		
//		downState = 1;
		path=new Path();
		interPath interpath = new interPath();
		paintpath=new paintPath(path, paint,interpath);
		
		path.moveTo(point.x, point.y);
		mX = point.x;
		mY = point.y;
		initx = point.x;
		inity = point.y;
		Log.i("autualTouchAlpha",String.valueOf(paint.getAlpha()));
		return true;
	}

	public boolean onTouchMove(Point point) {
		
		eraserX = point.x;
		eraserY = point.y;
		
		int mx = Math.abs(point.x-initx);
		int my = Math.abs(point.y-inity);
		
		if(mx >distance || my>distance)
		{
			paintpath.getInterPath().add(new Point(initx, inity));
			initx = point.x;
			inity = point.y;
		}
		float dx = Math.abs(point.x - mX);
		float dy = Math.abs(point.y - mY);
		
		if (dx > 0 || dy > 0) {
			path.quadTo(mX, mY, (point.x + mX) / 2, (point.y + mY) / 2);
			mX = point.x;
			mY = point.y;
		} else if (dx == 0 || dy == 0) {
			path.quadTo(mX, mY, (point.x + 1 + mX) / 2, (point.y + 1 + mY) / 2);
			mX = point.x + 1;
			mY = point.y + 1;
		}
		Log.i("autualMoveAlpha",String.valueOf(paint.getAlpha()));
		return true;
	}

	public boolean onTouchUp(Point point,Canvas canvas){
		
		eraserX = point.x;
		eraserY = point.y;
		radius = 0;
		
		paintpath.getInterPath().add(new Point((int)mX,(int)mY));
		path.lineTo(mX, mY);   
		canvas.drawPath(path, paint);
		savedpath.add(paintpath);
	//	path.close();
		path=null;
		ifDone=true;
		return true;
	}
	
	public boolean onDraw(Canvas canvas) {
		
		// TODO Auto-generated method stub
		if(canvas != null && path != null)
		{
		    canvas.drawPath(path, paint);
		    return true;
		}
		return false;
	}
	
	public boolean eraserOnDraw(Canvas canvas){
		
		if(canvas != null && ifEraser == true)
		{
			canvas.drawCircle(eraserX, eraserY, radius, eraserpaint);
			return true;
		}
		return false;
	}

	public void reDraw(Canvas canvas){
		
		for(int i=0;i<savedpath.size();i++){
			
			if(savedpath.get(i).paint.getAlpha() >= 30 && savedpath.get(i).paint.getAlpha() <= 170)
			{
				int alpha = savedpath.get(i).paint.getAlpha();
				savedpath.get(i).paint.setAlpha(alpha + 50);	
				canvas.drawPath(savedpath.get(i).path, savedpath.get(i).paint);
				savedpath.get(i).paint.setAlpha(alpha);
			}
			else
			{
				canvas.drawPath(savedpath.get(i).path, savedpath.get(i).paint);
			}		
		}
	}
	
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		
		if(savedpath != null)
		{
			Log.i("savedpath.size",String.valueOf(savedpath.size()));
			for(int i=0;i<savedpath.size();i++)
			{
				Paint paint = new Paint(savedpath.get(i).paint);
				paint.setStrokeWidth(savedpath.get(i).paint.getStrokeWidth()*height/Tools.screenHeight);
				
				if(paint.getStrokeWidth() < 5)
					paint.setStrokeWidth(5);
	//			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
				testdraw(canvas, savedpath.get(i).interpath,paint,width,height,dx);
				
			}
		}
	}
	
	 public void autodraw(Canvas canvas, int width,int height){
		 Path path = new Path();
		 
		 float[][] arr = new float[16][2];
		 float dx,dy;
		 arr[0][0] = width*0.2f;
		 arr[0][1] = height*0.5f;
		 
		 dx = width*0.2f*0.2f;
	     dy = height*0.25f*0.2f;
	     for(int i=1;i<=4;i++)
	     {
	    	 arr[i][0] = arr[i-1][0] + dx;
	    	 arr[i][1] = arr[i-1][1] +dy;
	     }
	     
		 arr[5][0] = width*0.4f;
		 arr[5][1] = height*0.75f;
		 
		 dy = height*0.5f*0.2f;
		 for(int i=6;i<=9;i++)
	     {
	    	 arr[i][0] = arr[i-1][0] + dx;
	    	 arr[i][1] = arr[i-1][1] - dy;
	     }
		 arr[10][0] = width*0.6f;
		 arr[10][1] = height*0.25f;
		 
		 dy = height*0.25f*0.2f;
		 for(int i=11;i<=14;i++)
	     {
	    	 arr[i][0] = arr[i-1][0] + dx;
	    	 arr[i][1] = arr[i-1][1] +dy;
	     }
		 arr[15][0] = width*0.8f;
		 arr[15][1] = height*0.5f;
		 
		 path.moveTo(arr[0][0],arr[0][1]);
		 for(int i=1;i<16;i++){
			 path.lineTo(arr[i][0], arr[i][1]);
		 }
		 path.close();
		 canvas.drawPath(path, paint);
	 }

	 private void testdraw(Canvas canvas,interPath interpath,Paint paint,int width,int height,int DX){

		 Path path = new Path();
		 mX = (float)interpath.getX(0)/Tools.screenWidth*width+DX;
		 mY = (float)interpath.getY(0)/Tools.screenHeight*height;
		 
		 path.moveTo(mX, mY);
		 for(int i=1;i<interpath.pointlist.size();i++)
		 {
			    float dx = Math.abs((float)interpath.getX(i)/Tools.screenWidth*width+DX - mX);
				float dy = Math.abs((float)interpath.getY(i)/Tools.screenHeight*height - mY);
				
				if (dx > 0 || dy > 0) {
					path.quadTo(mX, mY, ((float)interpath.getX(i)/Tools.screenWidth*width+DX + mX) / 2, 
							((float)interpath.getY(i)/Tools.screenHeight*height + mY) / 2);
					mX = (float)interpath.getX(i)/Tools.screenWidth*width+DX;
					mY = (float)interpath.getY(i)/Tools.screenHeight*height;
					
				} else if (dx == 0 || dy == 0) {
					
					path.quadTo(mX, mY, ((float)interpath.getX(i)/Tools.screenWidth*width+DX + 1 + mX) / 2, ((float)interpath.getY(i)/Tools.screenHeight*height + 1 + mY) / 2);
					mX = (float)interpath.getX(i)/Tools.screenWidth*width + DX + 1;
					mY = (float)interpath.getY(i)/Tools.screenHeight*height + 1;
				}
		 }
	
		 int k = interpath.pointlist.size()-1;
		 path.lineTo((float)interpath.getX(k)/Tools.screenWidth*width+DX, (float)interpath.getY(k)/Tools.screenHeight*height);
		 canvas.drawPath(path, paint);
//		 Log.i("size",String.valueOf(interpath.pointlist.size()));
	 }
	 public void setPenColor(){
		 
		 if(ifEraser == true)
			 return ;
		 paint = new Paint();
		 paint.setStyle(Style.STROKE);// 设置非填充
		 paint.setStrokeWidth(MainActivity.thickness);// 笔宽5像素
		 paint.setColor(MainActivity.currentColor);// 设置为红笔
		 paint.setAlpha(MainActivity.transparency);
		 paint.setMaskFilter(MainActivity.filter);
		 paint.setAntiAlias(true);// 锯齿不显示
	 }
	 
	 public void setPenThickness(){
		 if(ifEraser == true)
			 return ;
		 paint = new Paint();
		 paint.setStyle(Style.STROKE);// 设置非填充
		 paint.setStrokeWidth(MainActivity.thickness);// 笔宽5像素
		 paint.setColor(MainActivity.currentColor);// 设置为红笔
		 paint.setAlpha(MainActivity.transparency);
		 paint.setMaskFilter(MainActivity.filter);
		 paint.setAntiAlias(true);// 锯齿不显示
//		 MaskFilter maskFilter = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);
//		 paint.setMaskFilter(maskFilter);
	 }
	 public void setPenAlpha(){
		 if(ifEraser == true)
			 return ;
		 paint = new Paint();
		 paint.setStyle(Style.STROKE);// 设置非填充
		 paint.setStrokeWidth(MainActivity.thickness);// 笔宽5像素
		 paint.setColor(MainActivity.currentColor);// 设置为红笔
		 paint.setAlpha(MainActivity.transparency);
		 paint.setMaskFilter(MainActivity.filter);
		 paint.setAntiAlias(true);// 锯齿不显示
	 }
	 public void setEraserThickness(){
		 
		 if(ifEraser == false)
			 return ;
		 
		 paint = new Paint();
		 paint.setAntiAlias(true);
		 paint.setDither(true);
		 paint.setColor(Color.BLUE);
		 paint.setStyle(Paint.Style.STROKE);
		 paint.setStrokeJoin(Paint.Join.ROUND);
		 paint.setStrokeCap(Paint.Cap.SQUARE);
		 paint.setStrokeWidth(MainActivity.eraser_thickness);
		 paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		 paint.setMaskFilter(null);
		 
//		 radius = MainActivity.eraser_thickness*2; 
	 }
	/*
	 public void autoDrawEraser(Canvas canvas,int eraserX,int eraserY)
	 {
		 canvas.drawCircle(eraserX, eraserY, radius, eraserpaint);
	 }
	 */
	 
}
