package myListener;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.drawdemo04.DrawbsObject;
import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.SelectChooseLayerview;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;
import com.example.drawdemo04.SocietyModel.Society_gallery;

public class LayViewRemove {

	private MainActivity activity;
//	private SelectChooseLayerview chooseLayerWindow;
//	private MyView myView;
	private int viewWidth,viewHeight;
	private SelectChooseLayerview chooseLayerWindow;
	
	public LayViewRemove(MainActivity activity,int viewWidth,int viewHeight)
	{
		this.activity = activity;
	//	this.chooseLayerWindow = chooseLayerWindow;
	//	this.myView = myView;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	//	this.from = from;
	}
	 public boolean removeLayerView(int mtag,int tag){      //移除涂鸦层图片操作
	    	
		    int from = activity.getFrom();
		    chooseLayerWindow = activity.getChooseLayerWindow();
		    MyView myView = activity.getMyView();
		    
	    	if(mtag == -2)                 //mtag = -2  移除的是第一张涂鸦层图片，即删除所有涂鸦层操作
			{
	    		for(int i=0;i<UnDoDraw.saved1.size();i++)       //保存当前涂鸦操作到涂鸦操作缓存栈 Undo.saved中 
	    		{
					UnDoDraw.saved.add(tag+1+i, UnDoDraw.currentsaved.get(i));
				}
			    UnDoDraw.saved1.clear();
			    ReDoDraw.redoList1.clear();
			    
				for(int i=UnDoDraw.saved.size()-1;i>=0;i--)         //将涂鸦操作缓存栈的涂鸦保存到 涂鸦操作恢复栈 Tools.traceRecord中
				{
					Tools.traceRecord.add(new DrawbsObject(UnDoDraw.saved.get(i), i));
				}
				UnDoDraw.saved.clear();                   
	            ReDoDraw.redoList.clear();
	            activity.showMyView(tag, null, null);
	            
	            activity.showAlertDialog(R.string.ifDeleteAllLayers, sureOnClick, cancelOnClick);
	            
				return true;
			}
			if(mtag == -1)                 //移除第二张涂鸦层图，即背景图，表示重新从网络社区选择背景图
			{
		//		if(from == 4)              //from = 4 表示是以特定的涂鸦层图为背景图的，不能重新选择
		//		{
		//			return false;
		//		}
		//		else
		//		{
					if(chooseLayerWindow != null)
						chooseLayerWindow.dismiss();
					
					Intent intent = new Intent(activity, Society_gallery.class);
					Bundle bundle = new Bundle();
					bundle.putInt("from", 1);
					bundle.putInt("viewHeight", viewHeight);
					bundle.putInt("viewWidth", viewWidth);
					intent.putExtras(bundle);
					activity.startActivityForResult(intent, activity.REQUSTCODE);
					return true;
		//		}	
			}
			if(mtag >= 0)             //表示移除的是某个涂鸦操作层图
			{
				if(tag == -2)      //当前myView画板是完整的涂鸦图
				{
					Tools.traceRecord.add(new DrawbsObject(UnDoDraw.saved.get(mtag),mtag));
					UnDoDraw.saved.remove(mtag);
					myView.drawWork(-2);          //刷新画板

					return true;
				}
				else
				{
					if(mtag < tag)
					{
						
						Tools.traceRecord.add(new DrawbsObject(UnDoDraw.saved.get(mtag), mtag));
						UnDoDraw.saved.remove(mtag);
						tag -- ;
						activity.setViewTag(tag);
						Log.i("LayerViewRmove_mtag",String.valueOf(mtag));
					}
					else
					{
						if(mtag == tag)
						{						
							myView.drawWork(-1); 
						    Tools.traceRecord.add(new DrawbsObject(UnDoDraw.saved.get(mtag), mtag));
							UnDoDraw.saved.remove(mtag);
							activity.setPicid(tag);
							activity.setViewTag(-3);	
						}
						else
						{
							Log.i("LayerViewRemove_mtag>tag",String.valueOf(mtag));
							Tools.traceRecord.add(new DrawbsObject(UnDoDraw.saved.get(mtag), mtag));
							UnDoDraw.saved.remove(mtag);
							return true;
						}
					}	
				}
				
			}
			return false;
	    }
	 
	 private OnClickListener sureOnClick = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO 自动生成的方法存根
			Tools.refresh_arr.clear();
			Tools.refresh_arr.add(0);
			Tools.refresh_arr.add(1);
			for(int i=0;i<Tools.traceRecord.size();i++)
			{
				Tools.refresh_arr.add(i+2);
			}
			Tools.clearMemoryCache();
			Tools.traceRecord.clear();
			arg0.dismiss();
			if(chooseLayerWindow != null)
			{
				chooseLayerWindow.dismiss();
				chooseLayerWindow = null;
			}
		}
	};
	private OnClickListener cancelOnClick = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO 自动生成的方法存根
			for(int j=Tools.traceRecord.size()-1;j>=0;j--)
	    	{
	    		DrawbsObject db = Tools.traceRecord.get(j);
	    		int position = db.firstindex;
	    		UnDoDraw.saved.add(position,db.drawBS);
	    	}
	    	Tools.traceRecord.clear();
	    	
	    	Tools.refresh_arr.clear();                     
	    	Tools.refresh_arr.add(0);               //添加需要清除缓存的分层图的序号
	    	Tools.refresh_arr.add(1); 
	    	for(int i = 0;i<UnDoDraw.saved.size();i++)
	    		Tools.refresh_arr.add(i+2);
	    	Tools.clearMemoryCache();                     //清除之前分层图的缓存
	   
	    	activity.showLayerViewDialog();
		    activity.showMyView(-2,null,null);
		    arg0.dismiss();
		}
	};
}
