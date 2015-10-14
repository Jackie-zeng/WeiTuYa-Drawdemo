package com.example.drawdemo04.PenGraphycal;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.DrawBS.Rectangle;

public class DrawRect2 extends DrawBS{

	public Point point1, point2, point3, point4, cenPoint;
	private Rect rect;
	private Rect point1Rect, point2Rect, point3Rect, point4Rect;
    private float rate;
    private ArrayList<Point> points;
    private int viewWidth,viewHeight;
    private int x1,y1,x2,y2;
    private boolean reTouch = true;
    
	public DrawRect2(int viewWidth,int viewHeight,float rate) {
		// TODO Auto-generated constructor stub	
		this.rate = rate; 
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
		point4 = new Point();
		cenPoint = new Point();
		rect = new Rect();
		
		points = new ArrayList<Point>();
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
	}

	public boolean onTouchDown(Point point) {
		
		downPoint.set(point.x, point.y);

		if (point2Rect != null) {
			//判断用户所点击的点是否在矩形顶点point1为中心的矩形point1Rect内，
			//如果在这个矩形内，则我们认为用户点击了该点
			if (point1Rect.contains(downPoint.x, downPoint.y)) {
				downState = 1;
				
			} else if (point2Rect.contains(downPoint.x, downPoint.y)) {
				downState = 2;

			} else if (point3Rect.contains(downPoint.x, downPoint.y)) {
				downState = 3;

			} else if (point4Rect.contains(downPoint.x, downPoint.y)) {
				downState = 4;

			} else if (rect.contains(downPoint.x, downPoint.y)) {
				downState = 5;

			}else
			{
				downState = 0;
			}
		}
        return true;
	}

	public boolean onTouchMove(Point point) {
		
		if((rect.width() * rect.height()) <= 1 )
		{
			point1.set(x1,y1);
			point2.set(x2,y2);
			point3.set(point1.x, point2.y);
			point4.set(point2.x, point1.y);
			setRect();
			reTouch = false;
			return true;
		}
		if(reTouch == true)
		{
			movePoint.set(point.x, point.y);
	        
			int mwidth;
			// 根据用户所点击的坐标点画相应的矩形
			switch (downState) {
			case 1:
				//点击点point1，则point2不变；根据point1、point2重新设置点point3,point4
				if(point.y < 0)
					point.y = 0;
				mwidth = (int)((point.y - point1.y)*rate);
				if(point1.x + mwidth < 0)
				{
					mwidth = -point1.x;
					point.y = (int)((float)mwidth/rate + point1.y);
				}
		
				point1.set(point1.x + mwidth,point.y);
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				
				moveState = 1;
				break;
				
			case 2:
				//点击点point2，则point1不变；根据point1、point2重新设置点point3,point4
	            if(point.y > viewHeight)
	            	point.y = viewHeight;
	            mwidth = (int)((point.y - point2.y)*rate);
	            if(point2.x + mwidth > viewWidth)
	            {
	            	mwidth = viewWidth - point2.x;
	            	point.y = (int)((float)mwidth/rate + point2.y);
	            }
	            
				point2.set(point2.x + mwidth, point.y);
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				moveState = 2;
				break;
				
			case 3:
				//点击点point3，则重新设置矩形点point3、1、2
				if(point.y > viewHeight)
					point.y = viewHeight;
				mwidth = (int)((point.y - point3.y)*rate);
				if(point3.x - mwidth < 0)
				{
					mwidth = point3.x;
					point.y = (int)((float)mwidth/rate + point3.y);
				}
				point3.set(point3.x - mwidth, point.y);
				point1.set(point3.x, point4.y);
				point2.set(point4.x, point3.y);
				moveState = 3;
				break;
				
			case 4:
				//点击点point4，则重新设置矩形点point4、1、2
				if(point.y < 0)
					point.y = 0;
				mwidth = (int)((point.y - point4.y)*rate);
				if(point4.x - mwidth > viewWidth)
				{
					mwidth = point4.x - viewWidth;
					point.y = (int)((float)mwidth/rate + point4.y);
				}
				point4.set(point4.x - mwidth, point.y);
				point1.set(point3.x, point4.y);
				point2.set(point4.x, point3.y);
				moveState = 4;
				break;
				
			case 5:
				// 计算矩形中间点
				cenPoint.x = (point1.x + point2.x) / 2;
				cenPoint.y = (point1.y + point2.y) / 2;
				// 移动距离
				int movedX = point.x - cenPoint.x;
				int movedY = point.y - cenPoint.y; 
				
				if(point1.x + movedX < 0 || point2.x + movedX > viewWidth)
				{
					if(point1.x + movedX < 0)
						movedX = -point1.x;			
					if(point2.x + movedX > viewWidth)
						movedX = viewWidth - point2.x;
					
			//		movedY = (int)(movedX/rate);
				}
				if(point1.y + movedY < 0 || point2.y + movedY > viewHeight)
				{
					if(point1.y + movedY < 0)
						movedY = - point1.y;
					if(point2.y + movedY > viewHeight)
						movedY = viewHeight - point2.y;
					
			//		movedX = (int)(movedY*rate);
				}
			    
				point1.x = point1.x + movedX;
				point1.y = point1.y + movedY;
				point2.x = point2.x + movedX;
				point2.y = point2.y + movedY;
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				moveState = 5;
				break;
	       default:
	    	    break;
			}
			//每次拖动完之后需要重新设定4个顶点小矩形。
			setRect();
			return true;
		}	
		return true;
	}

	// 设置矩形的开始点与结束点pont1/point2
	public void getStartPoint() {

		if (downPoint.x < movePoint.x && downPoint.y < movePoint.y) {
			point1.set(downPoint.x, downPoint.y);
			point2.set(movePoint.x, movePoint.y);
			point3.set(point1.x, point2.y);
			point4.set(point2.x, point1.y);
		} else if (downPoint.x < movePoint.x && downPoint.y > movePoint.y) {
			point3.set(downPoint.x, downPoint.y);
			point4.set(movePoint.x, movePoint.y);
			point1.set(point3.x, point4.y);
			point2.set(point4.x, point3.y);
		} else if (downPoint.x > movePoint.x && downPoint.y > movePoint.y) {
			point2.set(downPoint.x, downPoint.y);
			point1.set(movePoint.x, movePoint.y);
			point3.set(point1.x, point2.y);
			point4.set(point2.x, point1.y);
		} else if (downPoint.x > movePoint.x && downPoint.y < movePoint.y) {
			point4.set(downPoint.x, downPoint.y);
			point3.set(movePoint.x, movePoint.y);
			point1.set(point3.x, point4.y);
			point2.set(point4.x, point3.y);
		}
		
		setRect();
		
	}
	
	public void setRect() {
		// 设置以矩形的4个顶点为中心的小矩形
		point1Rect = new Rect(point1.x - 30, point1.y - 30, point1.x + 30,
				point1.y + 30);
		point2Rect = new Rect(point2.x - 30, point2.y - 30, point2.x + 30,
				point2.y + 30);
		point3Rect = new Rect(point3.x - 30, point3.y - 30, point3.x + 30,
				point3.y + 30);
		point4Rect = new Rect(point4.x - 30, point4.y - 30, point4.x + 30,
				point4.y + 30);
		
		
		rect.set(point1.x, point1.y, point2.x, point2.y);
		
		
	}
	public boolean onTouchUp(Point point,Canvas canvas){
		reTouch = true;
		
		if(savedrectangle==null){
			savedrectangle=new Rectangle(rect, paint);
		}
		if(point2Rect!=null)
			ifDone=true;
		else
			ifDone=false;
		
		return true;
	}
	
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(canvas != null)
		{
		    canvas.drawRect(rect, paint);// 画矩形
		    return true;
		}
		return false;
	}
	public void drawAdjustEdge(Canvas canvas,int x1,int y1,int x2,int y2)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		
		paint.setStrokeWidth(10);
		paint.setAlpha(100);
		paint.setColor(Color.WHITE);
		
		point1.x = x1;
		point1.y = y1;
		
		point2.x= x2;
		point2.y = y2;
		
		setRect();
		ifDone = true;
		canvas.drawRect(rect, paint);
	}
	 
	 public void dismiss(Canvas canvas){
		rect.set(0, 0, 0, 0);
	//	paint.setStrokeWidth(0);
		canvas.drawRect(rect, paint);	
	 }
	 	
}
