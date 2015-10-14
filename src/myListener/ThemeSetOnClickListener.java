package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;

import android.view.View;
import android.view.View.OnClickListener;

public class ThemeSetOnClickListener implements OnClickListener{

	private MainActivity activity;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		if(activity != null)
		{
			activity.saveThemeImage();
		}
	}
    public ThemeSetOnClickListener(MainActivity activity)
    {
    	this.activity = activity;
    }
    
}
