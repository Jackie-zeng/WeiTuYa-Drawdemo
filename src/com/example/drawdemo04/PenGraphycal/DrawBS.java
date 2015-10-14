package com.example.drawdemo04.PenGraphycal;

import java.util.ArrayList;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.Log;

/*
 * 主view类。
 * 该类类似于一个接口，子类都继承实现了主类的这些方法
 */
public class DrawBS {
	
	public int downState;
	public int moveState;
	public Point downPoint = new Point();
	public Point movePoint = new Point();
	
	public Point eventPoint = new Point();
	public Paint paint;//声明画笔
	public ArrayList<paintPath> savedpath=new ArrayList<paintPath>();
	public Circle savedcircle = null;
	public Column savedcolumn=null;
	public Cube   savedcube=null;
	public Line   savedline=null;
	public Rectangle savedrectangle=null;
	public Triangle  savedtriangle=null;
	public boolean ifDone=false;
	public int tag;
	
	public DrawBS() {
		// 设置画笔
		paint = new Paint();
		paint.setStyle(Style.STROKE);// 设置非填充
		paint.setAntiAlias(true);// 锯齿不显示
		paint.setStrokeWidth(MainActivity.thickness);// 笔宽5像素
		Log.i("DrawBS",String.valueOf(MainActivity.thickness));
		paint.setColor(MainActivity.currentColor);// 设置为红笔
		paint.setMaskFilter(MainActivity.filter);
		paint.setAlpha(MainActivity.transparency);

	}
	
	public boolean onTouchDown(Point point) {
		return false;
	}
	 
	public boolean onTouchMove(Point point) {
		return false;
	}
	
	public boolean onTouchUp(Point point,Canvas canvas) {
		return false;
	}
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		return false;
	};
    public boolean eraserOnDraw(Canvas canvas){
    	
    	return false;
    }
	public class paintPath{
		public Path path;
		public Paint paint;
		public interPath interpath;
	
		public paintPath(Path path,Paint paint,interPath tracePath) {
			// TODO 自动生成的构造函数存根
			this.path=path;
			this.paint=paint;
			this.interpath = tracePath;
		}
		public interPath getInterPath(){
			return interpath;
		}
	}
	public class interPath{
		public ArrayList<Point> pointlist;
//		public Paint paint;
		
		public interPath(){
//			this.paint=paint;
			pointlist=new ArrayList<Point>();
		}
		public void add(Point point){
			pointlist.add(point);
		}
		public int getX(int index){
		    return  pointlist.get(index).x;
		}
		public int getY(int index){
	        return pointlist.get(index).y;
		}
		public void set(int index,Point newPoint){
			pointlist.set(index, newPoint);
		}
		
	}
	public class Circle{
		public Point rDotsPoint;
		public int radius;
		public Paint paint;
		public Circle(Point point,int radius,Paint paint){
			rDotsPoint=point;
			this.radius=radius;
			this.paint=paint;
		}
	}
	public class Column{
		public Point LeftPoint_oval1, RightPoint_oval1, 
		LeftPoint_oval2,RightPoint_oval2;
        public RectF rectF1, rectF2;
        public Paint paint;
        
        public Column(Point LeftPoint_oval1,Point RightPoint_oval1,
        		Point LeftPoint_oval2,Point RightPoint_oval2,RectF rectF1,RectF rectF2,Paint paint)
        {
        	this.LeftPoint_oval1=LeftPoint_oval1;
        	this.RightPoint_oval1=RightPoint_oval1;
        	this.LeftPoint_oval2=LeftPoint_oval2;
        	this.RightPoint_oval2=RightPoint_oval2;
        	this.rectF1=rectF1;
        	this.rectF2=rectF2;
        	this.paint=paint;
        }
	}
	public class Cube{
		public 	Point cubePoint1, cubePoint2, cubePoint3, cubePoint4,
		cubePoint5,cubePoint6, cubePoint7, cubePoint8;
        public Rect rect1, rect2;
        public Paint paint;
        public Cube(Point cubePoint1,Point cubePoint2,Point cubePoint3,Point cubePoint4,
        		Point cubePoint5,Point cubePoint6,Point cubePoint7,Point cubePoint8,Rect rect1,Rect rect2,Paint paint)
        {
        	this.cubePoint1=cubePoint1;
        	this.cubePoint2=cubePoint2;
        	this.cubePoint3=cubePoint3;
        	this.cubePoint4=cubePoint4;
        	this.cubePoint5=cubePoint5;
        	this.cubePoint6=cubePoint6;
        	this.cubePoint7=cubePoint7;
        	this.cubePoint8=cubePoint8;
        	this.rect1=rect1;
        	this.rect2=rect2;
        	this.paint=paint;
        }
	}
	public class Line{
		public Point point1,point2;
		public Paint paint;
		public Line(Point point1,Point point2,Paint paint){
			this.point1=point1;
			this.point2=point2;
			this.paint=paint;
		}
	}
	public class Rectangle{
		public Rect rect;
		public Paint paint;
		public Rectangle(Rect rect,Paint paint){
			this.rect=rect;
			this.paint=paint;
		}
	}
	public class Triangle{
		public Path path;
		public Paint paint;
		public Point point1;
		public Point point2;
		public Point point3;
		
		public Triangle(Path path,Paint paint){
			this.path=path;
			this.paint=paint;
			point1 = new Point();
			point2 = new Point();
			point3 = new Point();
		}
	}
	/*
	public void testdraw(Canvas canvas){
		   
	}
	*/
    public void reDraw(Canvas canvas){
    	
    }
    public void setTag(int tag){
    	this.tag=tag;
    }
    public int getTag(){
    	return tag;
    }
    public void undo(Canvas floorCanvas){
    	
    }
    public void redo(Canvas floorCanvas){
    	
    }
    public void autodraw(Canvas canvas,int width,int height){
    	
    }
    
    public void drawEdge(Canvas canvas,int width,int height,int edgeWidth,int edgeHeight){
  
    }
    
    public void dismiss(Canvas canvas){
    	
    }
    public void setPenColor(){
    	
    }
    public void setPenThickness(){
    	
    }
    public void setPenAlpha(){
    	
    }
    public void setEraserThickness(){
    	
    }
    public void setMaskFilter()
    {
    	if(this instanceof DrawPath)
    	{
    		if(((DrawPath)this).ifEraser == true)
    			return ;
    	}
    	paint.setMaskFilter(MainActivity.filter);
    }
    public void repaint(Canvas floorcanvas){
    	
    }
    public void postRedraw(Canvas canvas,int width ,int height,int dx){
    	
    }
    public void drawAdjustEdge(Canvas canvas,int x1,int y1,int x2,int y2)
    {
    	
    }
    /*
    public void autoDrawEraser(Canvas canvas)
    {
    	
    }
    */
}
