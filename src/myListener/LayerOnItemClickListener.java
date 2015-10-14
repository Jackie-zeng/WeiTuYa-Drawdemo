package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.SelectChooseLayerview;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class LayerOnItemClickListener implements OnItemClickListener{

//	private int tag;
//	private int pic_id;
	private MainActivity activity;
//	private SelectChooseLayerview chooseLayerWindow;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		int tag = activity.getTag();
		int pic_id = activity.getPicId();
		SelectChooseLayerview chooseLayerWindow = activity.getChooseLayerWindow();
		// TODO 自动生成的方法存根
		if(tag == -2)        //tag == -2 表示当前 myView画板显示的是完整的涂鸦图
		{			
			activity.showMyView(arg2 - 2, null, null);	
		}
		else               //tag !=-2 表示当前myView画板显示的是某一分层图，即用户上一次选择的分层图
		{
			if(tag == -3)
				tag = pic_id - 1;
			
			Tools.refresh_arr.clear();
			Tools.refresh_arr.add(0);
			for(int i=tag+1;i<UnDoDraw.saved.size();i++)
			{
				Tools.refresh_arr.add(i+2);
			}
			Tools.clearMemoryCache();            //清除相应图层的缓存
			
			for(int i=0;i<UnDoDraw.currentsaved.size();i++)        //点击图层后，会将当前的涂鸦操作保存到 涂鸦操作的缓存栈 UndoDraw.saved中
			{
				UnDoDraw.saved.add(tag+1+i, UnDoDraw.currentsaved.get(i));
			}
					    
		    if((arg2 - 2) > tag)
		        tag = arg2 - 2 + UnDoDraw.currentsaved.size();
		    else
		    	tag = arg2 - 2;
		    UnDoDraw.currentsaved.clear();                     //清除当前涂鸦操作的缓存站 UndoDraw.currentsaved
		    ReDoDraw.currentredoList.clear();                  //清除当前涂鸦操作的redo缓存栈
		    
		    activity.showMyView(tag, null, null);
		    activity.setRecoverOnClickNum(0);
		}
		if(chooseLayerWindow != null)
			chooseLayerWindow.dismiss();
	
	}
	public LayerOnItemClickListener(MainActivity activity)
	{
		this.activity = activity;

	}
}
