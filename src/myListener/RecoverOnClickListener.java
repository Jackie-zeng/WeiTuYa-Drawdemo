package myListener;

import com.example.drawdemo04.DrawbsObject;
import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.SelectChooseLayerview;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class RecoverOnClickListener implements OnClickListener{

//	private SelectChooseLayerview chooseLayerWindow;
	private MainActivity activity;
	private int num = 0;
//	private int tag;
//	private int screenHeight;
//	private MyView myView;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		int tag = activity.getTag();
		Log.i("RecoverOnclick",String.valueOf(tag));
		int screenHeight = activity.getScreenHeight();
		MyView myView = activity.getMyView();
		SelectChooseLayerview chooseLayerWindow = activity.getChooseLayerWindow();
		
		if(Tools.traceRecord.size() != 0)
		{	
			Log.i("chooselayerview","before");
			if(tag != -2 && UnDoDraw.saved1.size() != 0 && num < 1)
	    	{
	    		num ++;
	    		Toast.makeText(activity, activity.getString(R.string.AddBeforeUndo), 1000).show();
	    		return ;
	    	}
			
			DrawbsObject dbo = Tools.traceRecord.get(Tools.traceRecord.size()-1);  //Tools.traceRecord 被删除涂鸦操作的缓存栈
			
			Tools.traceRecord.remove(Tools.traceRecord.size()-1);
			
			Log.i("RemoveLayerview","dbobefore");
			int index = dbo.firstindex;
			
		    UnDoDraw.saved.add(index, dbo.drawBS);
		    Log.i("RemoveLayerview","dboafter");
		    
		    if(tag  != -2  || (tag == -2 && chooseLayerWindow.layerViews.size() == 0) || (tag == -2 && chooseLayerWindow.layerViews.size() == 2))
		    {
		    	for(int j=Tools.traceRecord.size()-1;j>=0;j--)
		    	{
		    		DrawbsObject db = Tools.traceRecord.get(j);
		    		int position = db.firstindex;
		    		UnDoDraw.saved.add(position,db.drawBS);
		    	}
		    	Tools.traceRecord.clear();
		    	
		    	Log.i("chooselayerview","after");
		    	Tools.refresh_arr.clear();                     
		    	Tools.refresh_arr.add(0);               //添加需要清除缓存的分层图的序号
		    	Tools.refresh_arr.add(1); 
		    	for(int i = 0;i<UnDoDraw.saved.size();i++)
		    		Tools.refresh_arr.add(i+2);
		    	Tools.clearMemoryCache();                     //清除之前分层图的缓存
		   
		    	activity.showMyView(-2,null,null);
		    	activity.showLayerViewDialog();
		        num = 0;
		    }
		    else
		    {
		    	Tools.refresh_arr.clear();
		    	Tools.refresh_arr.add(0);
		    	for(int i=index;i<UnDoDraw.saved.size();i++)
		    		Tools.refresh_arr.add(i+2);
		    	Tools.clearMemoryCache();
		
		    	chooseLayerWindow.addView(index+2, Tools.layer_width, screenHeight, tag+1);   //添加恢复的涂鸦层
		    	myView.preWork(tag);
				myView.invalidate();
				Log.i("dd","ddd");
				num = 0;
		    }
		    chooseLayerWindow.setRecoverVisibility();
		}
	}

	public void setNum(int number)
	{
		this.num = number;
	}
	public RecoverOnClickListener(MainActivity activity)
	{
		this.activity = activity;
	}
}
