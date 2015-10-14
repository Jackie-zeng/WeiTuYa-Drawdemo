package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
/*
 * ���߶�
 * 
 * �쳤�����̡��ƶ���ʵ�������»���
 */
public class DrawLine extends DrawBS {

	private Point cenPoint;//ֱ�ߵ��м��
	private Point lPoint1, lPoint2;//ֱ�ߵ�������յ�
	private Rect lPoint1Rect, lPoint2Rect;//��ֱ�������յ�Ϊ���ĵľ���
	private double lDis;
	
	public DrawLine() {
		// TODO Auto-generated constructor stub
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
		
		cenPoint = new Point();
		lPoint1 = new Point();
		lPoint2 = new Point();
		lPoint1Rect = new Rect();
		lPoint2Rect = new Rect();
	}
	  
	
	
	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);

		//���������
		if (lPoint1Rect.contains(point.x, point.y)) {
			Log.i("onTouchDown", "downState = 1");
			downState = 1;
		//�������յ�
		} else if (lPoint2Rect.contains(point.x, point.y)) {
			Log.i("onTouchDown", "downState = 2");
			downState = 2;
		//�����ֱ����
		} else if (panduan(point)) {
			Log.i("onTouchDown", "downState = 3");
			downState = 3;
		//��ֱ����
		} else {
			Log.i("onTouchDown", "downState = 0");
			downState = 0;
		}
		return true;
	}   
	  
	public boolean onTouchMove(Point point) {

		switch (downState) 
		{
		//����϶�ֱ����㣬��ֱ�ߵ��յ㲻��
		case 1:
			lPoint1.set(point.x, point.y);
			moveState = 1;
			break;
		//����϶�ֱ���յ㣬��ֱ�ߵ���㲻��
		case 2:
			lPoint2.set(point.x, point.y);
			moveState = 2;
			break;
		//�����סֱ�߽����϶���������ƶ��ľ��������趨ֱ�ߵ�������յ�
		case 3:
			//����ֱ�ߵ��м��
			cenPoint.x = (lPoint2.x + lPoint1.x) / 2;
			cenPoint.y = (lPoint2.y + lPoint1.y) / 2;
			//�ƶ�����
			int movedX = point.x - cenPoint.x;
			int movedY = point.y - cenPoint.y;

			lPoint1.x = lPoint1.x + movedX;
			lPoint1.y = lPoint1.y + movedY;
			lPoint2.x = lPoint2.x + movedX;
			lPoint2.y = lPoint2.y + movedY;
			moveState = 3;
			break;
		//�������ֱ���ϣ������»�ֱ��
		default:
			lPoint1.set(downPoint.x, downPoint.y);
			lPoint2.set(point.x, point.y);
			break;
		}
		return true;
	}   
	public boolean onTouchUp(Point point,Canvas canvas) {
		//�����趨ֱ�������յ�Ϊ���ĵľ���
		lPoint1Rect.set(lPoint1.x - 25, lPoint1.y - 25, lPoint1.x + 25,
				lPoint1.y + 25);
		lPoint2Rect.set(lPoint2.x - 25, lPoint2.y - 25, lPoint2.x + 25,
				lPoint2.y + 25);
		
		if(savedline==null){
			savedline=new Line(lPoint1, lPoint2, paint);
		}
		
		lDis = Math.sqrt((lPoint1.x - lPoint2.x)
				* (lPoint1.x - lPoint2.x) + (lPoint1.y - lPoint2.y)
				* (lPoint1.y - lPoint2.y));
		if(lDis > 0)
			ifDone = true;
		else
			ifDone = false;
		return true;
	} 
	  
	  
	 /*
	  * �жϵ�ǰ������ĵ��Ƿ���ֱ����
	  * 
	  * �����û�������ĵ㵽�߶������˵�ľ���֮��
	  * ���߶εľ�����бȽ� ���ж�
	  */
	public boolean panduan(Point point) {

		lDis = Math.sqrt((lPoint1.x - lPoint2.x)
				* (lPoint1.x - lPoint2.x) + (lPoint1.y - lPoint2.y)
				* (lPoint1.y - lPoint2.y));

		double lDis1 = Math.sqrt((point.x - lPoint1.x) * (point.x - lPoint1.x)
				+ (point.y - lPoint1.y) * (point.y - lPoint1.y));
		double lDis2 = Math.sqrt((point.x - lPoint2.x) * (point.x - lPoint2.x)
				+ (point.y - lPoint2.y) * (point.y - lPoint2.y));


		if (lDis1 + lDis2 >= lDis + 0.00f && lDis1 + lDis2 <= lDis + 5.00f) {
			return true;
		} else {
			return false;
		}
	}
	
	public void reDraw(Canvas canvas){
		if(savedline!=null)
			canvas.drawLine(savedline.point1.x, savedline.point1.y, savedline.point2.x, savedline.point2.y, savedline.paint);
	}
	
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		
		if(savedline != null)
		{
			Log.i("savedline","drawline");
			Paint paint = new Paint(savedline.paint);
			paint.setStrokeWidth(savedline.paint.getStrokeWidth()*height/Tools.screenHeight);
			
			if(paint.getStrokeWidth() < 5)
				paint.setStrokeWidth(5);
			
			canvas.drawLine((int)((float)savedline.point1.x/Tools.screenWidth*width+dx),(int)((float)savedline.point1.y/Tools.screenHeight*height),
					(int)((float)savedline.point2.x/Tools.screenWidth*width+dx), (int)((float)savedline.point2.y/Tools.screenHeight*height), paint);
		}
	}
	public boolean onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// ��ֱ��
		if(canvas != null)
		{
			canvas.drawLine(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y, paint);
		    return true;
		}
		return false;
	}
	 public void autodraw(Canvas canvas,int width,int height){
		 int x1 = (int)(width*0.25);
		 int y1 = (int)(height*0.5);
		 
		 int x2 = (int)(width*0.75);
		 int y2 = y1;
		 
		 canvas.drawLine(x1, y1, x2, y2, paint);   			 
	 }
	 @Override
	 public void setPenColor(){
		 paint.setColor(MainActivity.currentColor);
	 }
	 @Override
	 public void setPenThickness(){
		 paint.setStrokeWidth(MainActivity.thickness);
	 }
	 public void setPenAlpha(){
		 paint.setAlpha(MainActivity.transparency);
	 }
}
