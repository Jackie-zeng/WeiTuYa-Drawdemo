package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MySubmit_handler;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SubmitOnClickListener implements OnClickListener{

	private MainActivity activity;
//	private MyView myView;
//	private int from ,imageFrom;
//	private String initpic;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		
		if(UnDoDraw.currentsaved.size() != activity.submitRecord || MainActivity.hasDrawn_submit == true)
		{
			if(UnDoDraw.currentsaved.size() > 0)
			{
				if(Tools.isNetConnected(activity))
				{
					MyView myView = activity.getMyView();
					int from = activity.getFrom();
					int imageFrom = activity.getImageFrom();
					String initpic = activity.getInitPic();
					
					activity.progressDialog = Tools.showProgress(activity);
					MySubmit_handler mhandler = new MySubmit_handler(activity,from);
					myView.submit(initpic,imageFrom,mhandler);
				}
				else
				{
					Toast.makeText(activity, activity.getString(R.string.NotInternet), 3000).show();
				}
				return ;
			}
		}
		Toast.makeText(activity, activity.getString(R.string.DrawBeforeSubmit), 3000).show();
	}
    
	public SubmitOnClickListener(MainActivity activity) {
		// TODO 自动生成的构造函数存根
		this.activity = activity;
	//	this.myView = myView;
	//	this.from = from;
	//	this.initpic = initpic;
	//	this.imageFrom = imageFrom;
	}
}
