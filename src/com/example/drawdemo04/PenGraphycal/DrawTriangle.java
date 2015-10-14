package com.example.drawdemo04.PenGraphycal;


import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

/*
 * ��������
 * 
 * ��Ҫȷ��3���㣺
 * ��ʼ�㡢��ǰ�ƶ��㣬����ǰ������������һ����
 * 
 * ����3����ȷ��һ��������
 */
public class DrawTriangle extends DrawBS {

	private Path path;
	private Point point1, point2, point3;
	private Rect point1Rect, point2Rect, point3Rect;
    private Point apoint,bpoint,cpoint;
    
	public DrawTriangle() {
		// TODO Auto-generated constructor stub
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();

		point1Rect = null;
		point2Rect = null;
		point3Rect = null;

		path = new Path();
	}

	
	
	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);
        apoint = new Point();
        bpoint = new Point();
        cpoint = new Point();
		/*
		 * �ж�point3Ϊ���ĵľ���point3Rect�Ƿ�Ϊnull��
		 * ���=null�����û���û�п�ʼ�������Σ������=null�����û��Ѿ�������������
		 * 
		 * �ж��û����λ��
		 */
		if (point1Rect != null) {
			if (point1Rect.contains(downPoint.x, downPoint.y)) {
				downState = 1;
			} else if (point2Rect.contains(downPoint.x, downPoint.y)) {
				downState = 2;
			} else if (point3Rect.contains(downPoint.x, downPoint.y)) {
				downState = 3;
			} else if (panduan(point1, point2, point3, downPoint)) {
				downState = 4;
				
			}else {
				downState = 0;
			}
		}
		return true;
	}

	
	public boolean onTouchMove(Point point) {

		switch (downState) {
		case 1:
			point1.set(point.x, point.y);
			setPath();
			moveState = 1;
			break;
		case 2:
			point2.set(point.x, point.y);
			setPath();
			moveState = 2;
			break;
		case 3:
			point3.set(point.x, point.y);
			setPath();
			moveState = 3;
			break;
		case 4:
			/*
			 * �������������ڣ���ʼ�ƶ�������
			 * 
			 * �����������ε����ĵ�coreTanriglePointΪ���ĵ�����ƶ�
			 * 
			 */
			//���������ε�����
			Point coreTanriglePoint = new Point(
					(point1.x + point2.x + point3.x) / 3,
					(point1.y + point2.y + point3.y) / 3);
			//�����Ƶ��ľ���
			int movedX = point.x - coreTanriglePoint.x;
			int movedY = point.y - coreTanriglePoint.y;
			//�����趨�����ε�3������
			point1.set(point1.x + movedX, point1.y + movedY);
			point2.set(point2.x + movedX, point2.y + movedY);
			point3.set(point3.x + movedX, point3.y + movedY);
			moveState = 4;
			setPath();
			break;
		default:
			//���������ε�������
			point1.set(downPoint.x, downPoint.y);
			point2.set(point.x, point.y);
			//����point1��point2���м��
			Point cenPoint = new Point();
			cenPoint.x = (point2.x + point1.x) / 2;
			cenPoint.y = (point2.y + point1.y) / 2;
			//���õ�point3
			point3.set(cenPoint.x+100, cenPoint.y-100);
			setPath();
			break;
		}
		return true;
	}

	public boolean onTouchUp(Point point,Canvas canvas){
		
		savedtriangle=new Triangle(path, paint);
		savedtriangle.point1 = apoint;
		savedtriangle.point2 = bpoint;
		savedtriangle.point3 = cpoint;
		
		if(point1Rect!=null)
			ifDone=true;
		else
			ifDone=false;
		return true;
	}
	/*
	 * �ж��û�������ĵ��Ƿ�����������
	 * 
	 * �ж�ԭ��
	 * �����û�������ĵ�������ε���������������ɵ�3�������ε����
	 * ��ԭ�����ε�������бȽ�
	 * 
	 * �����ȣ������������ڣ�
	 * �������ȣ�������������
	 */
	public static boolean panduan(Point a, Point b, Point c, Point p) {
  
		double abc = triangleArea(a, b, c);
		double abp = triangleArea(a, b, p);
		double acp = triangleArea(a, c, p);
		double bcp = triangleArea(b, c, p);

		if (abc == abp + acp + bcp) {
			return true;
		} else {
			return false;
		}
	}

	// ������������������ε����       
	private static double triangleArea(Point a, Point b, Point c) {

		double result = Math.abs((a.x * b.y + b.x * c.y + c.x * a.y - b.x * a.y
				- c.x * b.y - a.x * c.y) / 2.0D);
		return result;
	}
	 
	
	public void setPath() {
		path = new Path();
		path.moveTo(point1.x, point1.y);
		path.lineTo(point2.x, point2.y);
		path.lineTo(point3.x, point3.y);
		path.close();
        
		apoint.x = point1.x;
		apoint.y = point1.y;
		bpoint.x = point2.x;
		bpoint.y = point2.y;
		cpoint.x = point3.x;
		cpoint.y = point3.y;
		
		point1Rect = new Rect(point1.x-20, point1.y-20, point1.x+20, point1.y+20);
		point2Rect = new Rect(point2.x-20, point2.y-20, point2.x+20, point2.y+20);
		point3Rect = new Rect(point3.x-20, point3.y-20, point3.x+20, point3.y+20);
	}
	
	public void reDraw(Canvas canvas){
		if(savedtriangle!=null)
			canvas.drawPath(savedtriangle.path, savedtriangle.paint);
	}
	public void postRedraw(Canvas canvas,int width ,int height,int dx)
	{
		if(savedtriangle != null)
		{
			Paint paint = new Paint(savedtriangle.paint);
			paint.setStrokeWidth(savedtriangle.paint.getStrokeWidth()*height/Tools.screenHeight);
			
			if(paint.getStrokeWidth() < 5)
				paint.setStrokeWidth(5);
			
			Path path = new Path();		
			path.moveTo((float)savedtriangle.point1.x/Tools.screenWidth*width+dx, (float)savedtriangle.point1.y/Tools.screenHeight*height);
			path.lineTo((float)savedtriangle.point2.x/Tools.screenWidth*width+dx, (float)savedtriangle.point2.y/Tools.screenHeight*height);
			path.lineTo((float)savedtriangle.point3.x/Tools.screenWidth*width+dx, (float)savedtriangle.point3.y/Tools.screenHeight*height);
			path.close();
			
	        canvas.drawPath(path, paint);
		}
	}
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		canvas.drawPath(path, paint);
		return true;
	}
	 public void autodraw(Canvas canvas,int width,int height){
		 
		 Path path =new Path();
		 float x1 = width*0.25f;
		 float y1 = (height*0.75f);
		 
		 float x2 = (width*0.75f);
		 float y2 = (height*0.75f);
		 
		 float x3 = (width*0.5f);
		 float y3 = (height*0.25f);
		 
		 path.moveTo(x1, y1);
		 path.lineTo(x2, y2);
		 path.lineTo(x3, y3);
		 path.close();
		 
		 canvas.drawPath(path, paint);		 
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
