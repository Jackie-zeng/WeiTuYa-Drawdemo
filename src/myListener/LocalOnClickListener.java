package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.Tools;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class LocalOnClickListener implements OnClickListener{

	private MainActivity activity;
	private MyView myView;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		activity.bottomslider_gone();
		myView = activity.getMyView();
		
		Handler mhandler = new Handler()
		{
			   @Override
			   public void handleMessage(Message msg)
			   {
				   switch(msg.what)
				   {
				   case 0:
					   Log.i("MainActivity_societyOnclick","finishCache");
					   Tools.closeMyProgress();
					   
					   Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
					   getAlbum.setType(activity.IMAGE_TYPE);
					   activity.startActivityForResult(getAlbum, activity.IMAGE_CODE);
					   break;
				   }
			   }
		};
		
		if(myView != null)
		{
			myView.save(mhandler);
			Tools.showMyProgress(activity);
			activity.setIsToLocal(true);
		}
		
		
	}
    public LocalOnClickListener(MainActivity activity)
    {
    	this.activity = activity;
    }
	
}
