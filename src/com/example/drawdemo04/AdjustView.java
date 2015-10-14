package com.example.drawdemo04;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.example.drawdemo04.PenGraphycal.DrawBS;
import com.example.drawdemo04.PenGraphycal.DrawPath;
import com.example.drawdemo04.PenGraphycal.DrawRect2;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.initModel.Init_choose;

public class AdjustView extends View{

	private Context context;
	private int width,height;
	private DrawBS drawBS;
	private Point evevtPoint;
	private Bitmap backBitmap;
//	private Bitmap floorBitmap;
	private Bitmap surfaceBitmap;
//	private Canvas floorCanvas;
	private Canvas surfaceCanvas;
	
	public AdjustView(Context context,int width,int height,RecyclingBitmapDrawable value) {
		super(context);
		this.context=context;
		
		this.width=width;
		this.height=height;
       
		drawBS = new DrawPath();
		evevtPoint = new Point();
//		backBitmap = Bitmap.createBitmap(width,height, Tools.config);
//		backBitmap = Bitmap.createBitmap(width,height, Tools.config);
		backBitmap = value.getBitmap();
		
 //     floorBitmap = Bitmap.createBitmap(width,height, Tools.config);
//		floorCanvas = new Canvas(floorBitmap);

		surfaceBitmap = Bitmap.createBitmap(width,height, Tools.config);
		surfaceCanvas = new Canvas(surfaceBitmap);
//		surfaceCanvas.drawColor(Color.TRANSPARENT);
//		value.setIsDisplayed(true);
	}

	 @SuppressLint("WrongCall") public void onDraw(Canvas canvas) 
	 {
		canvas.drawBitmap(backBitmap, 0, 0, null);
//		canvas.drawBitmap(floorBitmap, 0, 0, null);

	    drawBS.onDraw(surfaceCanvas);
	    canvas.drawBitmap(surfaceBitmap, 0, 0, null);
	    canvas.restore();
		super.onDraw(canvas);
			
     }
	 @Override
		public boolean onTouchEvent(MotionEvent event) {
			evevtPoint.set((int) event.getX(), (int) event.getY());

			if(drawBS == null){
				return true;
			}

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
				break;
			default:
				break;
			}
			return true;
		}
	 
	   public void auto_setAdjustImg(int x1,int y1,int x2,int y2){
	    	
	    	drawBS = new DrawRect2(width,height,(float)(x2-x1)/(y2-y1));
	    	drawBS.drawAdjustEdge(surfaceCanvas, x1, y1, x2, y2);
	    	invalidate();
	    }
	   
	   public String auto_finishAdjustImg(int newWidth,int newHeight)
	   {
	    	return getAdjustedImg(newWidth,newHeight);
	   }
	   
	   private String getAdjustedImg(int newWidth,int newHeight)
	   {
		   Tools.scaleX = ((DrawRect2)drawBS).point1.x;
		   Tools.scaleY = ((DrawRect2)drawBS).point1.y;
		   Tools.scaleWidth = ((DrawRect2)drawBS).point2.x-((DrawRect2)drawBS).point1.x;
		   Tools.scaleHeight =  ((DrawRect2)drawBS).point2.y-((DrawRect2)drawBS).point1.y;
		   
		   Bitmap bit = Tools.zoomSpecificImage(backBitmap, newWidth, newHeight,Tools.scaleX,Tools.scaleY, 
				   Tools.scaleWidth,Tools.scaleHeight);
		   
		   String bitmap_name = "pic" + String.valueOf(Tools.saveTag);
		   Init_choose.asynloader.removeBitmapFromCache(bitmap_name);
		   RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(context.getResources(), bit);
		   Init_choose.asynloader.addBitmapToMemoryCache(bitmap_name, value);
	//	   Tools.saveTag ++ ;
		   return bitmap_name;
	   }
	   /*
	   public void setImageBitmap(Bitmap bitmap)
	   {		
			if(bitmap != null){	     
				if(backBitmap != null)
					backBitmap.recycle();
	            
	            backBitmap = adjust(bitmap);
				invalidate();
			}
		}
		*/
	   /*
	   private Bitmap adjust(Bitmap bitmap)
	    {
	    	if(bitmap != null)
	    	{
	    		float initwidth = bitmap.getWidth();
				float initheight = bitmap.getHeight();
	            float newWidth = width;
	            float newHeight = height;
	            
	            Matrix matrix = new Matrix();
	            float scaleWidth = newWidth/initwidth;
	            float scaleHeight = newHeight/initheight;
	            
	            matrix.postScale(scaleWidth, scaleHeight);
	        
	            return Bitmap.createBitmap(bitmap, 0, 0, (int)initwidth, (int)initheight,matrix,true).copy(Tools.config, true);
	    	}
	    	return null;
	    }
	    */
	   public void recycle()
	   {
		   if(surfaceBitmap != null && !surfaceBitmap.isRecycled())
		   {
			   surfaceBitmap.recycle();
			   surfaceBitmap = null;
		   }		
	   }
}
