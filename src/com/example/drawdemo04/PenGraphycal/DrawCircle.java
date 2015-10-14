package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
/*
 * 画圆
 * 
 * 无论拖动也好，拉伸也好，其实都是重新画圆，
 * 只是不改变画圆需要的某些属性进行绘制，这样看起来就行是移动或拖动的
 */
public class DrawCircle extends DrawBS {

	private Point rDotsPoint;//圆心
	private int radius = 0;//半径
	private Double dtance = 0.0;//当前点到圆心的距离
	
	public DrawCircle() {
		// TODO Auto-generated constructor stub
		rDotsPoint = new Point();
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
	}
	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);

		if (radius != 0) {
			//计算当前所点击的点到圆心的距离
			dtance = Math.sqrt((downPoint.x - rDotsPoint.x)
					* (downPoint.x - rDotsPoint.x)
					+ (downPoint.y - rDotsPoint.y)
					* (downPoint.y - rDotsPoint.y));
			// 如果距离半径减20和加20个范围内，则认为用户点击在圆上
			if (dtance >= radius - 20 && dtance <= radius + 20) {
				downState = 1;

			//如果距离小于半径，则认为用户点击在圆内
			} else if (dtance < radius) {
				downState = -1;

			// 当前点击的点在园外
			} else if (dtance > radius) {
				downState = 0;
			}
		} else {
			downState = 0;
		}
		return true;
	}   
	  
	public boolean onTouchMove(Point point) {

		switch (downState) {
		//如果在圆内，则重新设定圆心坐标
		case -1:
			rDotsPoint.set(point.x, point.y);
			break;

		//如果在圆上，则圆心坐标不变，重新设定圆的半径
		case 1:
			radius = (int) Math.sqrt((point.x - rDotsPoint.x)
					* (point.x - rDotsPoint.x)
					+ (point.y - rDotsPoint.y)
					* (point.y - rDotsPoint.y));
			break;

		//如果在圆外，重新画圆
		default:
			rDotsPoint.set(downPoint.x, downPoint.y);
			radius = (int) Math.sqrt((point.x - rDotsPoint.x)
					* (point.x - rDotsPoint.x)
					+ (point.y - rDotsPoint.y)
					* (point.y - rDotsPoint.y));
			break;
		}
		return true;
	}   
	  
	public boolean onTouchUp(Point point,Canvas canvas)
	{
		if(savedcircle!=null)
			savedcircle.radius=radius;
		else
			savedcircle=new Circle(rDotsPoint, radius,paint);
		
		if(radius!=0)
			ifDone=true;
		else
			ifDone=false;
		return true;
	}
	
	public void reDraw(Canvas canvas){
		if(savedcircle!=null)
		  canvas.drawCircle(savedcircle.rDotsPoint.x, savedcircle.rDotsPoint.y, savedcircle.radius, savedcircle.paint);
	}
	
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		
		if(savedcircle != null){

			Paint paint = new Paint(savedcircle.paint);
			paint.setStrokeWidth(savedcircle.paint.getStrokeWidth()*height/Tools.screenHeight);
			
			if(paint.getStrokeWidth() < 5)
				paint.setStrokeWidth(5);
			
			canvas.drawCircle((int)((float)savedcircle.rDotsPoint.x/Tools.screenWidth*width+dx),
					(int)((float)savedcircle.rDotsPoint.y/Tools.screenHeight*height), (int)((float)savedcircle.radius/Tools.screenHeight*height), paint);

		}
	}
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(canvas != null)
		{
			canvas.drawCircle(rDotsPoint.x, rDotsPoint.y, radius, paint);// 画圆
			return true;
		}
		return false;
	}
	public void autodraw(Canvas canvas,int width,int height){
		int dis;
		if(width<height)
			dis=width;
		else
			dis=height;
		rDotsPoint.x=0+width/2;
		rDotsPoint.y=0+height/2;
	
		radius=(int)(dis/2*0.7);
		
		canvas.drawCircle(rDotsPoint.x, rDotsPoint.y,radius, paint);
		
	}
    public void setPenColor(){
    	paint.setColor(MainActivity.currentColor);
 //   	if(MainActivity.transparency > 30)
//			paint.setAlpha(MainActivity.transparency + 50);
    }
    public void setPenThickness(){
    	paint.setStrokeWidth(MainActivity.thickness);
    }
    public void setPenAlpha(){
    	paint.setAlpha(MainActivity.transparency);
    }
}
