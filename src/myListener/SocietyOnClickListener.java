package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.Society_gallery;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.SelectDialog;
import com.example.drawdemo04.initModel.Init_choose;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SocietyOnClickListener implements OnClickListener{

	private MainActivity activity;
//	private SelectDialog dialog = null;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		final MyView myView = activity.getMyView();
		int viewHeight = activity.getViewHeight();
		int viewWidth = activity.getViewWidth();
		
		activity.bottomslider_gone();
		final Intent intent = new Intent(activity, Society_gallery.class);
		Bundle bundle = new Bundle();
		bundle.putInt("from", 1);
		bundle.putInt("viewHeight", viewHeight);
		bundle.putInt("viewWidth", viewWidth);
		intent.putExtras(bundle);
		
		 Handler mhandler = new Handler(){
			   @Override
			   public void handleMessage(Message msg)
			   {
				   switch(msg.what)
				   {
				   case 0:
					   Log.i("MainActivity_societyOnclick","finishCache");
					   Tools.closeMyProgress();
					   activity.startActivityForResult(intent,activity.REQUSTCODE);
					   break;
				   }
			   }
		   };
		if(myView != null)
		{
			myView.save(mhandler);
			Tools.showMyProgress(activity);
			activity.setIsToGallery(true);
		}
	}

	public SocietyOnClickListener(MainActivity activity)
	{
		this.activity = activity;
	}
	
	
}
