package com.example.drawdemo04.PenGraphycal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.DrawBS;
import com.example.drawdemo04.PenGraphycal.DrawCircle;
import com.example.drawdemo04.PenGraphycal.DrawColumn;
import com.example.drawdemo04.PenGraphycal.DrawCube;
import com.example.drawdemo04.PenGraphycal.DrawLine;
import com.example.drawdemo04.PenGraphycal.DrawPath;
import com.example.drawdemo04.PenGraphycal.DrawRectangle;
import com.example.drawdemo04.PenGraphycal.DrawTriangle;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

/*
 * ʵ�ַ�ʽ��
 * ����������bitmap�ķ�ʽ��
 * �ײ�����bitmap��floorBitmap, surfaceBitmap;���bitmapΪ͸��ɫ������Ḳ�ǵ��ײ�bitmap��ͼ��
 * ��ǰ��ͼ�����ڱ��bitmap��surfaceBitmap������ı仭�ʣ��򽫵�ǰsurfaceBitmap�����ݻ��Ƶ��ײ�bitmap��floorBitmap
 * ���ѡ����Ƥ������Ҫ�ڵײ�bitmap�Ͻ��л��ƣ�
 * �鿴ԭͼƬ��Ҳ�ǽ�ͼƬ���Ƶ��ײ�bitmap
 */
@SuppressLint("WrongCall") 
public class DisplayView extends View {

	private DrawBS drawBS = new DrawBS();
	private Point evevtPoint;
    private Bitmap surfaceBitmap;
    private Canvas surfaceCanvas;
	private int width,height;
	Bitmap newbm;
    private Canvas mcanvas;
    private Context context;
    private int actualRecord=0;
    
	@SuppressLint("ParserError")
	public DisplayView(Context context,int width,int height) {
		super(context);
		this.context=context;
		
		this.width=width;
		this.height=height;
   
		// ��ʼ��drawBS����drawBSĬ��ΪDrawPath��
		drawBS = new DrawPath();
		evevtPoint = new Point();
        
		// �ײ�bitmap��canvas��  
	//	floorBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
	
	//	floorCanvas = new Canvas(floorBitmap);

		
		// ����bitmap�����ڵײ�bitmap֮�ϣ����ڸ�ֵ���Ƶ�ǰ��������ͼ�Σ���Ҫ����Ϊ͸�������򸲸ǵײ�bitmap
		surfaceBitmap = Bitmap.createBitmap(width,height,Tools.config);
		surfaceCanvas = new Canvas(surfaceBitmap);
		surfaceCanvas.drawColor(Color.TRANSPARENT);
		
//		setDrawTool(6);
	}

	public void onDraw(Canvas canvas) 
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
  
	    drawBS.onDraw(surfaceCanvas);
	    canvas.drawBitmap(surfaceBitmap, 0, 0, null);    
		canvas.restore();
	}

	// �����¼���������Ӧ�Ļ�ͼ��������в���
	@Override    
	public boolean onTouchEvent(MotionEvent event) {
		
		/*
		 * �϶������в�ͣ�Ľ�bitmap����ɫ����Ϊ͸������ձ��bitmap��
		 * ���������϶����̵Ĺ켣���ử����
		 */ 
		/*
		evevtPoint.set((int) event.getX(), (int) event.getY());
       
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			drawBS.onTouchDown(evevtPoint);
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			drawBS.onTouchMove(evevtPoint);
		
			surfaceBitmap.eraseColor(Color.TRANSPARENT);
			invalidate();
			break;

		case MotionEvent.ACTION_UP:	
			drawBS.onTouchUp(evevtPoint, surfaceCanvas);
			invalidate();
			drawBS.setTag(1);
			break;
		default:
			break;
		}
	  */
		return false;
	}

	// ѡ��ͼ�Σ�ʵ������Ӧ����
	public void setDrawTool(int i,String text) {
		Log.i("i",String.valueOf(i));
		clear();
		switch (i) {
		case 0:
			drawBS = new DrawLine();
			break;
		case 1:
			drawBS = new DrawRectangle();
			break;
		case 2:
			drawBS = new DrawCircle();
			break;
		case 3:
			drawBS = new DrawTriangle();
			break;
		case 4:
			drawBS = new DrawCube();
			break;
		case 5:
			drawBS = new DrawColumn();
			break;
		case 6:
    		drawBS = new DrawPath();	
			break;
		case 7:
			if(text != null)
			{
				TextPaint textPaint = new TextPaint();
		    	textPaint.setTextSize(MainActivity.textSize);
		    	int textWidth = (int)(Math.ceil(textPaint.measureText(text)));
		    	int beginY = (int)(height*0.05),beginX;
		    	
		    	if(textWidth < width*0.8)
		    		beginX = (int)(width*0.5 - textWidth*0.5);
		    	else
		    		beginX = (int)(width*0.2);
		    	
		    	drawBS = new DrawText(beginX, beginY, textWidth, text);
		    	Log.i("DisplayView","drawText");
			}
			break;
		}
		drawBS.paint.setMaskFilter(MainActivity.filter);
		drawBS.autodraw(surfaceCanvas, width, height);
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		final int intrinsicSize = 2 * (mPreferredColorWheelRadius + mColorPointerHaloRadius);
        final int instriwidth = width;
        final int instriheight = height;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(instriwidth, widthSize);
		} else {
			width = instriwidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(instriheight, heightSize);
		} else {
			height = instriheight;
		}

	//	int min = Math.min(width, height);
		setMeasuredDimension(width,height);
		
	}
	private void clear()
	{
		surfaceBitmap.recycle();
		surfaceBitmap = Bitmap.createBitmap(width,height, Tools.config);
		surfaceCanvas = new Canvas(surfaceBitmap);
		surfaceCanvas.drawColor(Color.TRANSPARENT);
	}
	public void drawByColor(int color)
	{
		if(drawBS != null)
		{
			clear();
			if(drawBS instanceof DrawText)
				((DrawText)drawBS).textpaint.setColor(color);
			else
			    drawBS.paint.setColor(color);
			drawBS.autodraw(surfaceCanvas, width, height);
			invalidate();
		}
	}
	public void drawByPenThickness(int thickness)
	{
		if(drawBS != null)
		{
			clear();
			if(drawBS instanceof DrawText)
				((DrawText)drawBS).textpaint.setTextSize(thickness);
			else
				drawBS.paint.setStrokeWidth(thickness);		
			drawBS.autodraw(surfaceCanvas, width, height);
			invalidate();
		}
	}
	public void drawByPenTransparency(int alpha)
	{
		if(drawBS != null)
		{
			clear();
			if(drawBS instanceof DrawText)
				((DrawText)drawBS).textpaint.setAlpha(alpha);
			else
			     drawBS.paint.setAlpha(alpha);
			drawBS.autodraw(surfaceCanvas,width,height);
			invalidate();
		}
	}
	
}
