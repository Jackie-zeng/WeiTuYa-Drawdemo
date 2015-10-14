package myListener;

import com.example.drawdemo04.BottomSlideView;
import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.WriteTextPopupwindow;
import com.example.drawdemo04.SocietyModel.Society_set;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TextOnClickListener implements OnClickListener{

	private MainActivity activity;
	private int which;
//	private WriteTextPopupwindow writeTextWindow;
//	private MyView myView;
//	private Button titlebtn;
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		WriteTextPopupwindow writeTextWindow = activity.getWriteTextWindow();
		MyView myView = activity.getMyView();
		Button titlebtn = activity.getTitleBtn();
		
		switch(v.getId())
		{
		case R.id.head_offer:
			if(which == Tools.WriteText)
			{
				if(writeTextWindow != null)
				{
					String text = writeTextWindow.getText();
					activity.setText(text);
					Log.i("offer",text);
					myView.auto_setTextImg(text);
				    
				    activity.writingText = true;
				    MainActivity.graphicOrtext = MainActivity.TEXT;
				    titlebtn.setBackgroundResource(R.drawable.correct);
				    
				    writeTextWindow.setReturnType(writeTextWindow.sure_return);
				    writeTextWindow.dismiss();
				    writeTextWindow = null;
				}
			}
			else
			{
				if(writeTextWindow != null)
				{
					String text = writeTextWindow.getText();
					Society_set  societySetWindow = activity.getSocietySet();
					if(societySetWindow != null)
					   societySetWindow.setName(text);
					
					writeTextWindow.setReturnType(writeTextWindow.sure_return);   //自定义名字，默认不更新画具栏的图标
					writeTextWindow.dismiss();
					writeTextWindow = null;
				}
			}
		    break;
		    
		case R.id.head_cancel:
			if(which == Tools.WriteText)
			{
				if(writeTextWindow != null)
				{
					myView.setDrawTool(myView.actualRecord);
					MainActivity.graphicOrtext = MainActivity.GRAPHY;
					/*
					activity.choosed_graphic = activity.last_graphic;
					BottomSlideView bottomSlider = activity.getBottomSliderView();
					if(bottomSlider != null)
					    bottomSlider.setGraphyicalButtonResource(7, activity.choosed_graphic);
					*/
					writeTextWindow.setReturnType(writeTextWindow.unsure_return);
					writeTextWindow.dismiss();
					writeTextWindow = null;
				}
			}
			else
			{
				if(writeTextWindow != null)
				{
					writeTextWindow.setReturnType(writeTextWindow.sure_return);   //自定义名字，默认不更新画具栏的图标
					writeTextWindow.dismiss();
					writeTextWindow = null;
				}
			}		
			break;
		}
	}
    public TextOnClickListener(MainActivity activity,int which)
    {
    	this.activity = activity;
    	this.which = which;
//    	this.writeTextWindow = writeTextWindow; 
//   	this.myView = myView;
//    	this.titlebtn = titlebtn;
    }
    
	
}
