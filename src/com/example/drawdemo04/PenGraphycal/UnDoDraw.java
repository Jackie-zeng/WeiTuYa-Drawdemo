package com.example.drawdemo04.PenGraphycal;

import java.util.ArrayList;  

import com.example.drawdemo04.DrawbsObject;
import com.example.drawdemo04.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;
 
public class UnDoDraw extends DrawBS{
	
	public static ArrayList<DrawBS> saved=new ArrayList<DrawBS>();
	public static ArrayList<DrawBS> saved1 = new ArrayList<DrawBS>();
	public static ArrayList<DrawBS> currentsaved; 
 	private DrawBS drawBS=null;
	private paintPath paintpath;
	private DrawBS db=null;
	
	public void undo(Canvas floorCanvas){	

	//	choosesaved(tag);
		
		if(currentsaved!=null && currentsaved.size()>0)
		{	
			if(currentsaved.get(currentsaved.size()-1) instanceof DrawPath)
			{
				drawBS=(DrawPath)currentsaved.get(currentsaved.size()-1);
				Log.i("drawpath",String.valueOf(drawBS.savedpath.size()));
				
			    if(drawBS.savedpath.size()>0)
			    {
			    	paintpath=drawBS.savedpath.get(drawBS.savedpath.size()-1);
			    	if(ReDoDraw.currentredoList!=null && ReDoDraw.currentredoList.size()>0)
			    	{	    		
			    		if(ReDoDraw.currentredoList.get(ReDoDraw.currentredoList.size()-1) instanceof DrawPath)			    			
			    			ReDoDraw.currentredoList.get(ReDoDraw.currentredoList.size()-1).savedpath.add(paintpath);
			    		else
			    		{
			    			db=new DrawPath();
			    		    db.savedpath.add(paintpath);
			    		    ReDoDraw.currentredoList.add(db);
			    		}		    				    		
			    	}
			    	else
			    	{
			    		db=new DrawPath();
			    		db.savedpath.add(paintpath);
 			    		ReDoDraw.currentredoList.add(db);
			    	}
			    				    	
			    	drawBS.savedpath.remove(drawBS.savedpath.size()-1);	
			   	
			    	if(drawBS.savedpath.size()==0)
			    		currentsaved.remove(currentsaved.size()-1);
			    }
			    
			    for(int i=0;i<currentsaved.size();i++)
			    {
			    	Log.i("redraw0","undoredraw");
			    	currentsaved.get(i).reDraw(floorCanvas);
			    }
			    
			}
			else
			{
				ReDoDraw.currentredoList.add(currentsaved.get(currentsaved.size()-1));
				currentsaved.remove(currentsaved.size()-1);
				for(int i=0;i<currentsaved.size();i++)
				{
					currentsaved.get(i).reDraw(floorCanvas);
					Log.i("redraw1","undoredraw");
				}			
			}	
	
		}	 
	}
	
	public  void repaint(Canvas floorCanvas){
		
		if(currentsaved!=null && currentsaved.size()>0)
		{	
            Log.i("repaintsize",String.valueOf(currentsaved.size()));
            currentsaved.remove(currentsaved.size()-1);
        	for(int i=0;i<currentsaved.size();i++)
			{
				drawBS = currentsaved.get(i);
				drawBS.reDraw(floorCanvas);
			}			
		}
	}
	public static void choosesaved(int tag){
		if(tag == -2)
		    currentsaved = saved;
		else
		{
			if(saved1.size() != 0)
				saved1.clear();
			currentsaved = saved1;
		}		
	}
}
