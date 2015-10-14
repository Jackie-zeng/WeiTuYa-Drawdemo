package com.example.drawdemo04.PenGraphycal;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.Tools;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

public class DrawText extends DrawBS{

	private int beginX,beginY;
	private int textWidth;
	private String text;
	public TextPaint textpaint;
//	public Text historyText;
	
	public DrawText(){
		
	}
	public DrawText(int beginX,int beginY,int textWidth,String text){
		textpaint = new TextPaint();
		textpaint.setTextSize(MainActivity.textSize);
		textpaint.setAlpha(MainActivity.transparency);
		textpaint.setColor(MainActivity.currentColor);
		textpaint.setMaskFilter(MainActivity.filter);
		
		this.beginX = beginX;
		this.beginY = beginY;
		this.textWidth = textWidth;
		this.text = text;
		Log.i("text","drawText");
	}
	
    public boolean onDraw(Canvas canvas) {

        StaticLayout layout = new StaticLayout(text, textpaint, textWidth, Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
 
        canvas.translate(beginX, beginY);
        layout.draw(canvas);
        ifDone = true;
        return true;
	}
	
	public void reDraw(Canvas canvas){
		
		StaticLayout layout = new StaticLayout(text, textpaint, textWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
		canvas.translate(beginX,beginY);
		layout.draw(canvas);
		canvas.translate(-beginX, -beginY);
	}
	public void postRedraw(Canvas canvas,int width ,int height,int dx){
		
		TextPaint littlepaint = new TextPaint();
		/*
		littlepaint.setStrokeWidth(paint.getStrokeWidth()*height/Tools.screenHeight);
		if(littlepaint.getStrokeWidth() < 5)
			littlepaint.setStrokeWidth(5);
		*/
		littlepaint.setTextSize(textpaint.getTextSize()*height/Tools.screenHeight);
		if(littlepaint.getTextSize() < 5)
			littlepaint.setTextSize(5);
	//	littlepaint.setAlpha(MainActivity.transparency);
		littlepaint.setAlpha(textpaint.getAlpha());
  //    littlepaint.setColor(MainActivity.currentColor);
		littlepaint.setColor(textpaint.getColor());
 //		littlepaint.setMaskFilter(MainActivity.filter);
	    littlepaint.setMaskFilter(textpaint.getMaskFilter());
	    
		StaticLayout layout = new StaticLayout(text, littlepaint, (int)((float)textWidth/Tools.screenHeight*height), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
		canvas.translate((int)((float)beginX/Tools.screenWidth*width+dx),(int)((float)beginY/Tools.screenHeight*height)); 
		layout.draw(canvas);
		canvas.translate(-(int)((float)beginX/Tools.screenWidth*width+dx), -(int)((float)beginY/Tools.screenHeight*height));
	     	
	}
	public void autodraw(Canvas canvas,int width,int height){
		/*
		 StaticLayout layout = new StaticLayout(text, textpaint, textWidth, Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);
	     canvas.translate(beginX, beginY);
	     layout.draw(canvas);
	     ifDone = true;
	     */
	 }
}
