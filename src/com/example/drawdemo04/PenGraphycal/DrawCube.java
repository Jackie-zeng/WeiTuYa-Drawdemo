package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
/*
 * 立方体
 * 
 * 思路：
 * 1、先画一个矩形
 * 2、画一个和第一个矩形平行的矩形
 * 3、连接两个矩形之间的某些顶点
 */
public class DrawCube extends DrawBS {

	// 声明立方体的8个顶点。前面矩形的4个顶点为1、2、3、4；后面矩形的4个顶点为5、6、7、8
	Point cubePoint1, cubePoint2, cubePoint3, cubePoint4, cubePoint5,
			cubePoint6, cubePoint7, cubePoint8;
	Rect rect1, rect2;
    private int distance=0;
    
	public DrawCube() {
		// TODO Auto-generated constructor stub
		// 实例化
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
		
		cubePoint1 = new Point();
		cubePoint2 = new Point();
		cubePoint3 = new Point();
		cubePoint4 = new Point();
		cubePoint5 = new Point();
		cubePoint6 = new Point();
		cubePoint7 = new Point();
		cubePoint8 = new Point();
		rect1 = new Rect();
		rect2 = new Rect();
		Log.i("cubealpha",String.valueOf(paint.getAlpha()));
	}

	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);
		return true;
	}

	public boolean onTouchMove(Point point) {
		//第一个矩形的4个点
		cubePoint1.set(downPoint.x, downPoint.y);
		cubePoint2.set(point.x, point.y);
		cubePoint3.set(cubePoint1.x, cubePoint2.y);
		cubePoint4.set(cubePoint2.x, cubePoint1.y);

		// 算出立方体的边长的一半
		distance = (int) Math.abs(Math.sqrt((cubePoint4.x - cubePoint1.x)
				* (cubePoint4.x - cubePoint1.x) + (cubePoint4.y - cubePoint1.y)
				* (cubePoint4.y - cubePoint1.y))) / 2;
		// 根据前面矩形的4个顶点，给后面矩形的4个顶点赋值
		cubePoint5.set(cubePoint1.x + distance, cubePoint1.y - distance);
		cubePoint6.set(cubePoint2.x + distance, cubePoint2.y - distance);
		cubePoint7.set(cubePoint3.x + distance, cubePoint3.y - distance);
		cubePoint8.set(cubePoint4.x + distance, cubePoint4.y - distance);

		//矩形rect1、rect2
		rect1.set(cubePoint1.x, cubePoint1.y, cubePoint2.x, cubePoint2.y);
		rect2.set(cubePoint5.x, cubePoint5.y, cubePoint6.x, cubePoint6.y);
        return true;
	}

	public boolean onTouchUp(Point point,Canvas canvas){
		if(savedcube==null){
			savedcube=new Cube(cubePoint1, cubePoint2, cubePoint3, cubePoint4, cubePoint5,
					cubePoint6, cubePoint7, cubePoint8, rect1, rect2, paint);
		}
		if(distance>0)
			ifDone=true;
		else
			ifDone=false;
		return true;
	}
	public void reDraw(Canvas canvas){
		if(savedcube != null)
	  {
		canvas.drawRect(savedcube.rect1, savedcube.paint);
		canvas.drawRect(savedcube.rect2, savedcube.paint);
		canvas.drawLine(savedcube.cubePoint1.x, savedcube.cubePoint1.y, savedcube.cubePoint5.x, savedcube.cubePoint5.y,
				savedcube.paint);
		canvas.drawLine(savedcube.cubePoint2.x, savedcube.cubePoint2.y, savedcube.cubePoint6.x, savedcube.cubePoint6.y,
				savedcube.paint);
		canvas.drawLine(savedcube.cubePoint3.x, savedcube.cubePoint3.y, savedcube.cubePoint7.x, savedcube.cubePoint7.y,
				savedcube.paint);
		canvas.drawLine(savedcube.cubePoint4.x, savedcube.cubePoint4.y, savedcube.cubePoint8.x, savedcube.cubePoint8.y,
				savedcube.paint);
	  }
	}
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		if(savedcube != null)
		{
			Paint paint = new Paint(savedcube.paint);
			paint.setStrokeWidth(savedcube.paint.getStrokeWidth()*height/Tools.screenHeight);
			
			if(paint.getStrokeWidth() < 5)
				paint.setStrokeWidth(5);
			
			Rect rectF1 = new Rect((int)((float)savedcube.rect1.left/Tools.screenWidth*width+dx), (int)((float)savedcube.rect1.top/Tools.screenHeight*height),
					(int)((float)savedcube.rect1.right/Tools.screenWidth*width+dx),(int)((float)savedcube.rect1.bottom/Tools.screenHeight*height));
			Rect rectF2 = new Rect((int)((float)savedcube.rect2.left/Tools.screenWidth*width+dx), (int)((float)savedcube.rect2.top/Tools.screenHeight*height),
					(int)((float)savedcube.rect2.right/Tools.screenWidth*width+dx), (int)((float)savedcube.rect2.bottom/Tools.screenHeight*height));
			
     
			canvas.drawRect(rectF1, paint);
			canvas.drawRect(rectF2, paint);
			canvas.drawLine((int)((float)savedcube.cubePoint1.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint1.y/Tools.screenHeight*height), 
					(int)((float)savedcube.cubePoint5.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint5.y/Tools.screenHeight*height), paint);
			
			canvas.drawLine((int)((float)savedcube.cubePoint2.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint2.y/Tools.screenHeight*height),
					(int)((float)savedcube.cubePoint6.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint6.y/Tools.screenHeight*height),paint);
			
			canvas.drawLine((int)((float)savedcube.cubePoint3.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint3.y/Tools.screenHeight*height),
					(int)((float)savedcube.cubePoint7.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint7.y/Tools.screenHeight*height),paint);
			
			canvas.drawLine((int)((float)savedcube.cubePoint4.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint4.y/Tools.screenHeight*height), 
					(int)((float)savedcube.cubePoint8.x/Tools.screenWidth*width+dx), (int)((float)savedcube.cubePoint8.y/Tools.screenHeight*height),paint);
			
			
			
		}
	}
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawRect(rect1, paint);
		canvas.drawRect(rect2, paint);
		canvas.drawLine(cubePoint1.x, cubePoint1.y, cubePoint5.x, cubePoint5.y,
				paint);
		canvas.drawLine(cubePoint2.x, cubePoint2.y, cubePoint6.x, cubePoint6.y,
				paint);
		canvas.drawLine(cubePoint3.x, cubePoint3.y, cubePoint7.x, cubePoint7.y,
				paint);
		canvas.drawLine(cubePoint4.x, cubePoint4.y, cubePoint8.x, cubePoint8.y,
				paint);
		return true;
	}

	public void autodraw(Canvas canvas,int width,int height){
		
		int dis = (int)(height*0.5);
		int xwid = (int)(width*0.25);
		int x1 = (int)(width*0.5-xwid*0.5);
		int y1 = (int)(height*0.2);
		
		int x2 = (int)(width*0.5+xwid*0.5);
		int y2 = y1;
		
		int x3 = x1;
		int y3 = y1+dis;
		
		int x4 = x2;
		int y4 = y2+dis;
		
		distance = (int) Math.abs(Math.sqrt((x2 - x1)
				* (x2 - x1) + (y2 - y1)
				* (y2 - y1))) / 2;
		
		int x5 = x1-distance;
		int y5 = y1+distance;
		
		int x6 = x2-distance;
		int y6 = y2+distance;
		
		int x7 = x3-distance;
		int y7 = y3+distance;
		
		int x8 = x4-distance;
		int y8 = y4+distance;
		
		rect1.set(x1,y1,x4,y4);
		rect2.set(x5,y5,x8,y8);
		
		canvas.drawRect(rect1, paint);
		canvas.drawRect(rect2, paint);
		
		canvas.drawLine(x1, y1, x5, y5,
				paint);
		canvas.drawLine(x2,y2,x6, y6,
				paint);
		canvas.drawLine(x3, y3, x7,y7,
				paint);
		canvas.drawLine(x4,y4, x8, y8,
				paint);
		
	}
	
	@Override
	public void setPenColor(){
		paint.setColor(MainActivity.currentColor);
	}
	public void setPenThickness(){
		paint.setStrokeWidth(MainActivity.thickness);
	}
	public void setPenAlpha(){
		paint.setAlpha(MainActivity.transparency);
	}
}
