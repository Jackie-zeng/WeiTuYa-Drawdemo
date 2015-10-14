package myListener;

import com.example.drawdemo04.BottomSlideView;
import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyHandler;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.HttpUtil;
import com.example.drawdemo04.SocietyModel.Society_set;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.initModel.Init_choose;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TitleBtnOnClickListener implements OnClickListener{

	private MainActivity activity;
	private int firstSucceed = 0;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		MyView myView = activity.getMyView();
		Button titlebtn = activity.getTitleBtn();
		int from = activity.getFrom();
		
		if(activity.writingText == true)
		{
			if(myView.auto_finishTextImg())
			{
				activity.writingText = false;
				MainActivity.graphicOrtext = MainActivity.GRAPHY;
				
				activity.choosed_graphic = activity.last_graphic;
				BottomSlideView bottomSlider = activity.getBottomSliderView();
				if(bottomSlider != null)
				{
					if(activity.choosed_graphic == 6)
						bottomSlider.setGraphyicalButtonResource(1, 0);
					else
						bottomSlider.setGraphyicalButtonResource(1, activity.choosed_graphic + 2);
				}
				
				Log.i("TitleBtnOnclickListener",String.valueOf(activity.choosed_graphic));
				
		//		activity.bottomslider_gone();
				titlebtn.setBackgroundResource(R.drawable.layers_but);
			}		   
		}
		else
		{
			Bitmap bit = myView.auto_finishTitleImg();
			if(bit != null)
		   {
				RecyclingBitmapDrawable value = new RecyclingBitmapDrawable(activity.getResources(), bit);
			    activity.showSocietySetDialog(value);
			    saveTitleImg(bit,from);
			    titlebtn.setBackgroundResource(R.drawable.layers_but);
		    }
		    else
			{
				if(myView != null)
					myView.setDrawTool(myView.actualRecord);
				
				Tools.refresh_arr.clear();
				Tools.refresh_arr.add(0);
				Tools.clearMemoryCache();
				activity.showLayerViewDialog();
			}
		}
	}
	public TitleBtnOnClickListener(MainActivity activity)
	{
		this.activity = activity;

	}
	
	private void saveTitleImg(final Bitmap title_bit,int from)            //保存头像图片到本地文件夹
	{
		final MyHandler mhandler = new MyHandler(activity,0,from);
		final Handler myhandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				case 0:
					Toast.makeText(activity, activity.getString(R.string.failTosubmit), 3000).show();
					break;
				case 1:
					Toast.makeText(activity, activity.getString(R.string.successTosubmit), 3000).show();
					break;
				}
			}
		};
		Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				RecyclingBitmapDrawable title_value = new RecyclingBitmapDrawable(activity.getResources(), title_bit);
				Init_choose.asynloader.addBitmapToMemoryCache(Tools.titleSaved_path + Tools.title_name + Tools.identity.substring(Tools.identity.indexOf("_") + 1) + ".png",title_value);
				
				Tools.saveTitleImg(title_bit, mhandler);

				HttpUtil.submitImage(activity,HttpUtil.submit_TitleImageURL, title_bit, myhandler, null,firstSucceed);
			}
		});
	}

	
}
