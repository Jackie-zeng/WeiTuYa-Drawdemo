package com.example.drawdemo04.PenGraphycal;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

public class ReDoDraw extends DrawBS{

	public static ArrayList<DrawBS> redoList = new ArrayList<DrawBS>();
	public static ArrayList<DrawBS> redoList1 = new ArrayList<DrawBS>();
	public static ArrayList<DrawBS> currentredoList;
	private DrawBS drawBS;
	private paintPath paintpath;
	public void redo(Canvas floorCanvas)
	{
		if(currentredoList!=null && currentredoList.size()>0)
		{
	//		chooseredoList(tag);
			
			drawBS = currentredoList.get(currentredoList.size()-1);
			if(drawBS instanceof DrawPath)
			{
				if(drawBS.savedpath.size()>0)
				{
					paintpath=drawBS.savedpath.get(drawBS.savedpath.size()-1);
					if(paintpath.paint.getAlpha() >= 30 && paintpath.paint.getAlpha() <= 170)
					{
						int alpha = paintpath.paint.getAlpha();
						paintpath.paint.setAlpha(alpha + 50);
						floorCanvas.drawPath(paintpath.path, paintpath.paint);
						paintpath.paint.setAlpha(alpha);
					}
					else
						floorCanvas.drawPath(paintpath.path, paintpath.paint);
				  
					drawBS.savedpath.remove(drawBS.savedpath.size()-1);
					if(UnDoDraw.currentsaved.size()!=0)
					{
					   if(UnDoDraw.currentsaved.get(UnDoDraw.currentsaved.size()-1) instanceof DrawPath)
					   {
						   UnDoDraw.currentsaved.get(UnDoDraw.currentsaved.size()-1).savedpath.add(paintpath);
					   }
					   else
					   {
						   DrawBS db=new DrawPath();
						   db.savedpath.add(paintpath);
						   UnDoDraw.currentsaved.add(db);
					   }
					}
					else
					{
						DrawBS db=new DrawPath();
						db.savedpath.add(paintpath);
						UnDoDraw.currentsaved.add(db);
					}
				}
				if(drawBS.savedpath.size()==0)
					currentredoList.remove(currentredoList.size()-1);
			}
			else
			{
				Log.i("redoDraw","RedoDraw");
				drawBS.reDraw(floorCanvas);
				UnDoDraw.currentsaved.add(drawBS);
				currentredoList.remove(currentredoList.size()-1);
			}
		}
	
	}
	
	public static void chooseredoList(int tag){
		
		if(tag == -2)
		    currentredoList = redoList;
		else
		{
			if(redoList1.size() != 0)
				redoList1.clear();
			currentredoList = redoList1;
		}
			
	}
}
