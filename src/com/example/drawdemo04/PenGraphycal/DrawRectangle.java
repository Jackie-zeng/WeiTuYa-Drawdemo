package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class DrawRectangle extends DrawBS {

	public Point point1, point2, point3, point4, cenPoint;
	private Rect rect;
	private Rect point1Rect, point2Rect, point3Rect, point4Rect;
    private Paint drawEdge_paint ;
    private boolean isDrawingEdge = false;
    public boolean isDrawingTextEdge = false;
    
	public DrawRectangle() {
		// TODO Auto-generated constructor stub
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
		
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();
		point4 = new Point();
		cenPoint = new Point();
		rect = new Rect();
		
		drawEdge_paint = new Paint();
		drawEdge_paint.setStyle(Style.STROKE);// 设置非填充
		drawEdge_paint.setAntiAlias(true);// 锯齿不显示
	}

	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);

		/*
		 * 判断以矩形顶点point2为中心的小矩形point2Rect是否为空，
		 * 
		 * 为什么要判断point2Rect而不是point1Rect？
		 * 如果用户在当前页面只点击一下，也会产生point1Rect而不会产生point2Rect。
		 * 只有point1Rect而没有point2Rect判断是没有意义的
		 * 而如果point2Rect != null，则说明当前页面已经有画好的矩形了，可见进行判断用户多点击的点和矩形的关系
		 */
		if (point2Rect != null) 
		{
			//判断用户所点击的点是否在矩形顶点point1为中心的矩形point1Rect内，
			//如果在这个矩形内，则我们认为用户点击了该点
			
			if(isDrawingTextEdge == true)
			{
				if(rect.contains(downPoint.x, downPoint.y))
				{
					downState = 5;
					return true;
				}
				else
				{
					downState = 5;
					return false;
				}
				
			}
			else
			{
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

				} else {
					downState = 0;
				}
				return true;
			}	
		}
		return true;
	}

	public boolean onTouchMove(Point point) {
		movePoint.set(point.x, point.y);

		if(isDrawingTextEdge == true)
		{
			if(downState == 5)
			{
				cenPoint.x = (point1.x + point2.x) / 2;
				cenPoint.y = (point1.y + point2.y) / 2;
				// 移动距离
				int movedX = point.x - cenPoint.x;
				int movedY = point.y - cenPoint.y;

				point1.x = point1.x + movedX;
				point1.y = point1.y + movedY;
				point2.x = point2.x + movedX;
				point2.y = point2.y + movedY;
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				moveState = 5;
				setRect();
				return true;
			}
			return false;
		}
		else
		{
			switch (downState) {
			case 1:
				//点击点point1，则point2不变；根据point1、point2重新设置点point3,point4
				point1.set(point.x, point.y);
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				moveState = 1;
				break;
			case 2:
				//点击点point2，则point1不变；根据point1、point2重新设置点point3,point4
				point2.set(point.x, point.y);
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				moveState = 2;
				break;
			case 3:
				//点击点point3，则重新设置矩形点point3、1、2
				point3.set(point.x, point.y);
				point1.set(point3.x, point4.y);
				point2.set(point4.x, point3.y);
				moveState = 3;
				break;
			case 4:
				//点击点point4，则重新设置矩形点point4、1、2
				point4.set(point.x, point.y);
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

				point1.x = point1.x + movedX;
				point1.y = point1.y + movedY;
				point2.x = point2.x + movedX;
				point2.y = point2.y + movedY;
				point3.set(point1.x, point2.y);
				point4.set(point2.x, point1.y);
				moveState = 5;
				break;
			default:
				getStartPoint();
				moveState = 0;
				break;
			}
			//每次拖动完之后需要重新设定4个顶点小矩形。
			setRect();
			return true;
		}
		
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
		if(savedrectangle==null){
			savedrectangle=new Rectangle(rect, paint);
		}
		if(point2Rect!=null)
			ifDone=true;
		else
			ifDone=false;
		return true;
	}
	
	public void reDraw(Canvas canvas){
		if(savedrectangle!=null)
			canvas.drawRect(savedrectangle.rect, savedrectangle.paint);
	}
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		if(savedrectangle != null)
		{
			Paint paint = new Paint(savedrectangle.paint);
			paint.setStrokeWidth(savedrectangle.paint.getStrokeWidth()*height/Tools.screenHeight);
			
			if(paint.getStrokeWidth() < 5)
				paint.setStrokeWidth(5);
			
			Rect rect = new Rect((int)((float)savedrectangle.rect.left/Tools.screenWidth*width+dx), (int)((float)savedrectangle.rect.top/Tools.screenHeight*height),
					(int)((float)savedrectangle.rect.right/Tools.screenWidth*width+dx),(int)((float)savedrectangle.rect.bottom/Tools.screenHeight*height));
			canvas.drawRect(rect, paint);
			
		}
	}
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(isDrawingEdge == false)
			canvas.drawRect(rect, paint);// 画矩形
		else
			canvas.drawRect(rect, drawEdge_paint);
		return true;
	}
	
	public void drawEdge(Canvas canvas,int width,int height,int edgeWidth,int edgeHeight){
	
		isDrawingEdge = true;
		if(edgeWidth == 0 && edgeHeight == 0)
		{
			drawEdge_paint.setStrokeWidth(40);
		    drawEdge_paint.setAlpha(100);
		    drawEdge_paint.setColor(Color.RED);
		    
		    point1.x = (int)(width*0.25);
			point1.y = (int)(height*0.25);
			     
			point2.x = (int)(width*0.75);
			point2.y = (int)(height*0.75);
			 
			point3.x = point1.x;
			point3.y = point2.y;
			 
			point4.x = point2.x;
			point4.y = point1.y;
			
			setRect();
			ifDone = true;
		}
		else
		{
			isDrawingTextEdge = true;
			drawEdge_paint.setStrokeWidth(5);
		    drawEdge_paint.setAlpha(100);
		    drawEdge_paint.setColor(Color.RED);
		    
		    if(edgeWidth <= (int)(0.8*width))
		    {
		    	point1.x = (int)(Math.floor(width*0.5-edgeWidth*0.5));
		    	point1.y = (int)(Math.floor(height*0.5));
		    	
		    	point2.x = (int)(Math.ceil(width*0.5+edgeWidth*0.5));
		    	point2.y = (int)(Math.ceil(height*0.5+edgeHeight));
		    	
		    	point3.x = point1.x;
		    	point3.y = point2.y;
		    	
		    	point4.x = point2.x;
		    	point4.y = point1.y;
		    }
		    else
		    {
		    	edgeHeight = (int)(Math.ceil((edgeWidth*edgeHeight/(0.8*width))));
		    	point1.x = (int)(Math.floor((0.1*width)));
		    	point1.y = (int)(Math.floor(height*0.5 - edgeHeight*0.5));
		    	
		    	point2.x = (int)(Math.ceil(0.9*width));
		    	point2.y = (int)(Math.ceil(height*0.5 + edgeHeight*0.5));
		    	
		    	point3.x = point1.x;
		    	point3.y = point2.y;
		    	
		    	point4.x = point2.x;
		    	point4.y = point1.y;
		    }
		    setRect();
			ifDone = true;
		}
			
		canvas.drawRect(rect, drawEdge_paint);
	}
	
	 public void autodraw(Canvas canvas,int width,int height){
		 
		 point1.x = (int)(width*0.25);
		 point1.y = (int)(height*0.25);
		     
		 point2.x = (int)(width*0.75);
		 point2.y = (int)(height*0.75);
		 
		 point3.x = point1.x;
		 point3.y = point2.y;
		 
		 point4.x = point2.x;
		 point4.y = point1.y;
		 
		 /*
		 rect.set(point1.x,point1.y,point2.x, point2.y);
		 
		 point1Rect = new Rect(x1 - 30, y1 - 30, x1 + 30,
					y1 + 30);
		 point2Rect = new Rect(x4 - 30, y4 - 30, x4 + 30,
					y4 + 30);
		 point3Rect = new Rect(x1- 30, y4 - 30, x1 + 30,
					y4 + 30);
		 point4Rect = new Rect(x4 - 30, y1 - 30, x4 + 30,
					y1 + 30);
					*/
		 setRect();
		 ifDone = true;
		 canvas.drawRect(rect, paint);
	 }
	 
	 public void dismiss(Canvas canvas){
		rect.set(0, 0, 0, 0);
	//	paint.setStrokeWidth(0);
		canvas.drawRect(rect, paint);
		
	 }
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
