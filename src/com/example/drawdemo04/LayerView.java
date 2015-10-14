package com.example.drawdemo04;

import java.util.ArrayList;

import com.example.drawdemo04.PenGraphycal.DrawBS;
import com.example.drawdemo04.PenGraphycal.DrawPath;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class LayerView extends View{
	
	private Context context;
	private int width,height;
	private DrawBS drawBS;
	private Point evevtPoint;
	private Bitmap backbitmap;
	private Bitmap floorbitmap;
	private Bitmap tuyabitmap;
	private float scaleRate = 0.25f;
	private int index;
	private boolean isSaved = true;
	private Bitmap surfaceBitmap;
	private Bitmap back;
//	private ArrayList<DrawBS> dbs ;
	public LayerView(Activity context,int width,int height,int index,Bitmap backbitmap,Bitmap floorbitmap) {
		super(context);
//		this.context=context;
		
		this.width=width;
		this.height=height;
        this.index = index;
        this.backbitmap = backbitmap;
        this.floorbitmap = floorbitmap;
        
		if(index >= 0)
	  {
		  if(UnDoDraw.saved.size() > index)
			  drawBS = UnDoDraw.saved.get(index);
	  }
	   
		evevtPoint = new Point();
	//	invalidate();
        /*
		surfaceBitmap = Bitmap.createBitmap(width,height, Tools.config);
		surfaceCanvas = new Canvas(surfaceBitmap);
		surfaceCanvas.drawColor(Color.TRANSPARENT);
		*/
		
	//	composeBackbitmap(bitmap);
	}
	
	public void onDraw(Canvas canvas) 
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	//	drawLayerBitmap();
  
		/*
		canvas.drawBitmap(backbitmap, 0, 0, null);
		
		if(backbitmap != null)
		    back = backbitmap.createBitmap(backbitmap);
		else
			back = Bitmap.createBitmap(width, height, Tools.config);
		Canvas back_canvas = new Canvas(back);
		
		if(floorbitmap != null)
		{
			canvas.drawBitmap(floorbitmap,0,0,null);
			back_canvas.drawBitmap(floorbitmap, 0, 0, null);
		}
		
		float dx = (width-width*scaleRate/(0.6f*0.8f))/2;
		
		surfaceBitmap = Bitmap.createBitmap(width, height, Tools.config);
		Canvas surfaceCanvas = new Canvas(surfaceBitmap);
		
	    if(index < 0)
	    {
	    	if(index == -2)
	    	{
	    		if(begin >= 0)
	    		{
	    			for(int i=0;i<begin;i++){
		    			drawBS = UnDoDraw.saved.get(i);
		    			     drawBS.postRedraw(surfaceCanvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);
		    		}
		    		for(int i=begin;i<UnDoDraw.saved1.size()+begin;i++){
		    			drawBS = UnDoDraw.saved1.get(i-begin);
		    			drawBS.postRedraw(surfaceCanvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);
		    		}
		    		for(int i=UnDoDraw.saved1.size()+begin;i<(UnDoDraw.saved.size() + UnDoDraw.saved1.size());i++){
		    			drawBS = UnDoDraw.saved.get(i-UnDoDraw.saved1.size());
		    			drawBS.postRedraw(surfaceCanvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);
		    		}
	    		}
	    		else
	    		{
	    			for(int i=0;i<UnDoDraw.saved.size();i++){
		    			drawBS = UnDoDraw.saved.get(i);
		    			drawBS.postRedraw(surfaceCanvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);
		    		}
	    		}
	    		
	    	}
	    }
	    else
	    {
	    	drawBS.postRedraw(surfaceCanvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);    
	    }
    
	    canvas.drawBitmap(surfaceBitmap, 0, 0, null);
	    back_canvas.drawBitmap(surfaceBitmap, 0, 0, null);
		canvas.restore();
		*/

		
	}
	/*
	public void reDraw(int index)
	{
		this.index = index;
		if(index >= 0)
		{
			if(UnDoDraw.saved.size() > index)
				drawBS = UnDoDraw.saved.get(index);
			else
				drawBS = new DrawPath();
	    }
		invalidate();
    }
	*/
	public void drawLayerBitmap()
	{
		if(backbitmap != null)
		    back = backbitmap.createBitmap(backbitmap);
		else
			back = Bitmap.createBitmap(width, height, Tools.config);
		Canvas canvas = new Canvas(back);
		
        if(floorbitmap != null)
        	canvas.drawBitmap(floorbitmap, 0,0,null);
       
        float dx = (width-width*scaleRate/(0.6f*0.8f))/2;
	    if(index < 0)
	    {
	    	if(index == -2)
	    	{
	    		for(int i=0;i<UnDoDraw.saved.size();i++)
	    		{
	    			drawBS = UnDoDraw.saved.get(i);
	    			drawBS.postRedraw(canvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);
	    		}
	    	}
	    }
	    else
	    {
	    	drawBS.postRedraw(canvas, (int)(width*(0.25/(0.6*0.8))), height,(int)dx);
	//	    canvas.drawBitmap(surfaceBitmap, 0, 0, null);    
	    }
	}
	public Bitmap getBitmap()
	{
		drawLayerBitmap();
		return back;
		
	}
}
