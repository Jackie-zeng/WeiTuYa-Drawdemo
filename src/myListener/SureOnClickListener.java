package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;
import com.example.drawdemo04.SocietyModel.AdjustSizeofInitpicWindow;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.initModel.Init_choose;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SureOnClickListener implements OnClickListener{

	private MainActivity activity;
//	private AdjustSizeofInitpicWindow adjustWindow;
//	private MyView myView;
//	private int viewWidth,viewHeight;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根

		AdjustSizeofInitpicWindow adjustWindow = activity.getAdjustWindow();
		final MyView myView = activity.getMyView();
		int viewWidth = activity.getViewWidth();
		int viewHeight = activity.getViewHeight();
		
		if(adjustWindow != null)
		{
			String initpicurl = adjustWindow.finishAdjustImg();
			activity.setInitPicUrl(initpicurl);
			activity.setImageFrom(Tools.LOCAL);
			activity.setInitPic("");
			adjustWindow.dismiss();
			System.gc();
			
			RecyclingBitmapDrawable value = (RecyclingBitmapDrawable) Init_choose.asynloader.loadBitmap(myView, initpicurl, Init_choose.fixedthreadpool, viewWidth, 
					viewHeight, true, null);
			Bitmap bit = value.getBitmap();

			if(bit != null && myView != null)
			{
			    myView.setImageBitmap(bit);
			    
			    Tools.refresh_arr.add(0);
			    Tools.refresh_arr.add(1);
			    for(int i=0;i<UnDoDraw.saved.size();i++)
			    	Tools.refresh_arr.add(i+2);
			    Tools.clearMemoryCache();
			    
			    if(bit != null && !bit.isRecycled())
				{
					bit.recycle();
					bit = null;
				}
			}
			
		}
	}
	
	public SureOnClickListener(MainActivity activity)
	{
		this.activity = activity;
//		this.adjustWindow = adjustWindow;
//		this.myView = myView;
//		this.viewWidth = viewwidth;
//		this.viewHeight = viewheight;
	}
	
	

	
}
