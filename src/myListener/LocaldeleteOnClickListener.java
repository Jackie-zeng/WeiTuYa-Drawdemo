package myListener;

import java.io.File;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyHandler;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.initModel.Init_choose;

import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class LocaldeleteOnClickListener implements OnClickListener{

	private MainActivity activity;
//	private int from;
	
	@Override
	public void onClick(View arg0) {
		// TODO 自动生成的方法存根
		
		int from = activity.getFrom();
		activity.ProgressDisplay();
		final MyHandler handler = new MyHandler(activity,0,from);
		
		Init_choose.fixedthreadpool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				File saveFiles = new File(Tools.localSave_path);
	            Tools.DeleteFile(saveFiles);
	            saveFiles = new File(Tools.titleSaved_path);
	            Tools.DeleteFile(saveFiles);
	            saveFiles = new File(Tools.cacheDir_path);
	            Tools.DeleteFile(saveFiles);
	            saveFiles = new File(Tools.currentSaved_path);
	            Tools.DeleteFile(saveFiles);
	            saveFiles = new File(Tools.themeSaved_path);
	            Tools.DeleteFile(saveFiles);
	            Message msg = handler.obtainMessage(3);
	            handler.sendMessage(msg);
	            
			}
		});
	}
	public LocaldeleteOnClickListener(MainActivity activity)
	{
		this.activity = activity;
	//	this.from = from;
	}

}
