package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
/*
 * ��Բ��
 * 
 * android�Լ�û�л�Բ���ķ�����������Ҫ�Լ�����취ʵ�֡�
 * ˼·��
 * 1���Ȼ�һ����Բ��
 * 2�����ݵ�һ����Բ���ڶ�����Բ����������Բ����ƽ��
 * 3����������Բ����ߺ��ұ�������������������������Բ����
 */
public class DrawColumn extends DrawBS {

	//������Բ1��2��4��������
	private Point ovalPoint1, ovalPoint2, ovalPoint3, ovalPoint4;
	private Point LeftPoint_oval1, RightPoint_oval1, LeftPoint_oval2,
			RightPoint_oval2;
	private RectF rectF1, rectF2;
    private int distance;
    
	public DrawColumn() {
		// TODO Auto-generated constructor stub
		// ʵ����
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
		
		LeftPoint_oval1 = new Point();
		RightPoint_oval1 = new Point();
		LeftPoint_oval2 = new Point();
		RightPoint_oval2 = new Point();
		ovalPoint1 = new Point();
		ovalPoint2 = new Point();
		ovalPoint3 = new Point();
		ovalPoint4 = new Point();
		rectF1 = new RectF();
		rectF2 = new RectF();
		Log.i("columnalpha",String.valueOf(paint.getAlpha()));
	}

	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);
		return true;
	}

	public boolean onTouchMove(Point point) {

		/*
		 * ����Բ�뻭��������
		 * ��ȷ��������1��Ҫ�������������oval1Point1��oval1Point2
		 */
		ovalPoint1.set(downPoint.x, downPoint.y);
		ovalPoint2.set(point.x, point.y);
		
		/*
		 * ��Բ1 ��ߺ��ұߵĵ������LeftPoint_oval1��RightPoint_oval1
		 */
		//������
		int y1 = ovalPoint1.y + (ovalPoint2.y - ovalPoint1.y) / 2; 
		LeftPoint_oval1 = new Point(ovalPoint1.x, y1);
		RightPoint_oval1 = new Point(ovalPoint2.x, y1);

		//����һ�����룬  
	     distance = (int) Math.abs(Math.sqrt((ovalPoint2.x - ovalPoint1.x)
				* (ovalPoint2.x - ovalPoint1.x) + (ovalPoint2.y - ovalPoint1.y)
				* (ovalPoint2.y - ovalPoint1.y)));


		/*
		 * ������Բ1����Բ2
		 * ��Բ1����Բ2����ƽ�У������겻�䣬�ı�������
		 * ȷ������Բ2��Ҫ������������oval2Point1��oval2Point2
		 */
		ovalPoint3.set(ovalPoint1.x, ovalPoint1.y + distance);
		ovalPoint4.set(ovalPoint2.x, ovalPoint2.y + distance);
		/*
		 * ��Բ2 ��ߺ��ұߵĵ������LeftPoint_oval1��RightPoint_oval1
		 */
		//������
		int y2 = ovalPoint3.y + (ovalPoint4.y - ovalPoint3.y) / 2;
		LeftPoint_oval2 = new Point(ovalPoint1.x, y2);
		RightPoint_oval2 = new Point(ovalPoint2.x, y2);

		rectF1.set(ovalPoint1.x, ovalPoint1.y, ovalPoint2.x, ovalPoint2.y);
		rectF2.set(ovalPoint3.x, ovalPoint3.y, ovalPoint4.x, ovalPoint4.y);
        
		return true;
	}

	public boolean onTouchUp(Point point,Canvas canvas)
	{
		if(savedcolumn!=null){
			savedcolumn.LeftPoint_oval1=LeftPoint_oval1;
			savedcolumn.LeftPoint_oval2=LeftPoint_oval2;
			savedcolumn.RightPoint_oval1=RightPoint_oval1;
			savedcolumn.RightPoint_oval2=RightPoint_oval2;
		}
		else
			savedcolumn=new Column(LeftPoint_oval1, RightPoint_oval1, LeftPoint_oval2, RightPoint_oval2, rectF1, rectF2,paint);
		
		if(distance>0)
			ifDone=true;    
		else
			ifDone=false;
		return true;
	}
	public void reDraw(Canvas canvas){
		if(savedcolumn!=null)
	  {
		canvas.drawOval(savedcolumn.rectF1, savedcolumn.paint);		
		canvas.drawOval(savedcolumn.rectF2, savedcolumn.paint);		
		canvas.drawLine(savedcolumn.LeftPoint_oval1.x, savedcolumn.LeftPoint_oval1.y,
				savedcolumn.LeftPoint_oval2.x, savedcolumn.LeftPoint_oval2.y, savedcolumn.paint);
		canvas.drawLine(savedcolumn.RightPoint_oval1.x, savedcolumn.RightPoint_oval1.y,
				savedcolumn.RightPoint_oval2.x, savedcolumn.RightPoint_oval2.y, savedcolumn.paint);
	  }
	}
	
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		
		if(savedcolumn != null)
		{
			RectF rectF1 = new RectF(savedcolumn.rectF1.left/Tools.screenWidth*width+dx, savedcolumn.rectF1.top/Tools.screenHeight*height,
					savedcolumn.rectF1.right/Tools.screenWidth*width+dx, savedcolumn.rectF1.bottom/Tools.screenHeight*height);
			RectF rectF2 = new RectF(savedcolumn.rectF2.left/Tools.screenWidth*width+dx, savedcolumn.rectF2.top/Tools.screenHeight*height,
					savedcolumn.rectF2.right/Tools.screenWidth*width+dx, savedcolumn.rectF2.bottom/Tools.screenHeight*height);
			
			Paint paint = new Paint(savedcolumn.paint);
			paint.setStrokeWidth(savedcolumn.paint.getStrokeWidth()*height/Tools.screenHeight);
			
			if(paint.getStrokeWidth() < 5)
				paint.setStrokeWidth(5);
			
			canvas.drawOval(rectF1, paint);		
			canvas.drawOval(rectF2, paint);		
			
		    Point left1 = new Point((int)((float)savedcolumn.LeftPoint_oval1.x/Tools.screenWidth*width+dx), (int)((float)savedcolumn.LeftPoint_oval1.y/Tools.screenHeight*height));
		    Point left2 = new Point((int)((float)savedcolumn.LeftPoint_oval2.x/Tools.screenWidth*width+dx), (int)((float)savedcolumn.LeftPoint_oval2.y/Tools.screenHeight*height));
		    Point right1 = new Point((int)((float)savedcolumn.RightPoint_oval1.x/Tools.screenWidth*width+dx),(int)((float)savedcolumn.RightPoint_oval1.y/Tools.screenHeight*height));
		    Point right2 = new Point((int)((float)savedcolumn.RightPoint_oval2.x/Tools.screenWidth*width+dx),(int)((float)savedcolumn.RightPoint_oval2.y/Tools.screenHeight*height));
		    
		    canvas.drawLine(left1.x, left1.y, left2.x, left2.y, paint);
		    canvas.drawLine(right1.x, right1.y, right2.x, right2.y, paint);
		}
	}

	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(canvas != null)
		{
			canvas.drawOval(rectF1, paint);
			canvas.drawOval(rectF2, paint);
			canvas.drawLine(LeftPoint_oval1.x, LeftPoint_oval1.y,
					LeftPoint_oval2.x, LeftPoint_oval2.y, paint);
			canvas.drawLine(RightPoint_oval1.x, RightPoint_oval1.y,
					RightPoint_oval2.x, RightPoint_oval2.y, paint);
			return true;
		}
		return false;
	}
    public void autodraw(Canvas canvas,int width,int height){
    	
    	int dis=(int)(height*0.7);
    	
    	double dx = dis-5;
    	double dy = Math.sqrt((dis*dis-dx*dx));
    	int y1 = (int)(height*0.5-dis*0.5-dy*0.5);
    	int x1 = (int)(width*0.5-dx*0.5);
    	
    	int y2 = (int)(y1+dy);
    	int x2 = (int)(x1+dx);
    	
    	int x3 = x1;
    	int y3 = (int)(y1+dis);
    	
    	int x4 = x2;
    	int y4 = (int)(y2+dis);
    	
    	int leftx1 = x1;
    	int lefty1 = (int)(y1+dy*0.5);
    	
    	int rightx1 = x2;
    	int righty1 = lefty1;
    	
    	int leftx2 = x1;
    	int lefty2 = lefty1+dis;
    	
    	int rightx2 = x2;
    	int righty2 = righty1+dis;
    	
    	rectF1.set(x1, y1, x2, y2);
    	rectF2.set(x3, y3, x4, y4);
    	
    	canvas.drawOval(rectF1, paint);
		canvas.drawOval(rectF2, paint);
		
		canvas.drawLine(leftx1, lefty1,
				leftx2, lefty2, paint);
		canvas.drawLine(rightx1, righty1,
				rightx2, righty2, paint);
    	
    }
    public void setPenColor()
    {
    	paint.setColor(MainActivity.currentColor);
    }
    public void setPenThickness(){
    	paint.setStrokeWidth(MainActivity.thickness);
    }
    public void setPenAlpha(){
    	paint.setAlpha(MainActivity.transparency);
    }
}
