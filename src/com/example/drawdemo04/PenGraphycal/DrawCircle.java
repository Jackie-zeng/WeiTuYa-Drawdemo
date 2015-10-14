package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
/*
 * ��Բ
 * 
 * �����϶�Ҳ�ã�����Ҳ�ã���ʵ�������»�Բ��
 * ֻ�ǲ��ı仭Բ��Ҫ��ĳЩ���Խ��л��ƣ������������������ƶ����϶���
 */
public class DrawCircle extends DrawBS {

	private Point rDotsPoint;//Բ��
	private int radius = 0;//�뾶
	private Double dtance = 0.0;//��ǰ�㵽Բ�ĵľ���
	
	public DrawCircle() {
		// TODO Auto-generated constructor stub
		rDotsPoint = new Point();
		if(MainActivity.transparency > 30)
			paint.setAlpha(MainActivity.transparency + 50);
	}
	public boolean onTouchDown(Point point) {
		downPoint.set(point.x, point.y);

		if (radius != 0) {
			//���㵱ǰ������ĵ㵽Բ�ĵľ���
			dtance = Math.sqrt((downPoint.x - rDotsPoint.x)
					* (downPoint.x - rDotsPoint.x)
					+ (downPoint.y - rDotsPoint.y)
					* (downPoint.y - rDotsPoint.y));
			// �������뾶��20�ͼ�20����Χ�ڣ�����Ϊ�û������Բ��
			if (dtance >= radius - 20 && dtance <= radius + 20) {
				downState = 1;

			//�������С�ڰ뾶������Ϊ�û������Բ��
			} else if (dtance < radius) {
				downState = -1;

			// ��ǰ����ĵ���԰��
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
		//�����Բ�ڣ��������趨Բ������
		case -1:
			rDotsPoint.set(point.x, point.y);
			break;

		//�����Բ�ϣ���Բ�����겻�䣬�����趨Բ�İ뾶
		case 1:
			radius = (int) Math.sqrt((point.x - rDotsPoint.x)
					* (point.x - rDotsPoint.x)
					+ (point.y - rDotsPoint.y)
					* (point.y - rDotsPoint.y));
			break;

		//�����Բ�⣬���»�Բ
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
			canvas.drawCircle(rDotsPoint.x, rDotsPoint.y, radius, paint);// ��Բ
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
