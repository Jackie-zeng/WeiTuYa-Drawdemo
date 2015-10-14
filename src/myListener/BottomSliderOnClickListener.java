package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.holocolorpicker.SelectWindow;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class BottomSliderOnClickListener implements OnClickListener{

	private MainActivity activity;
	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
        switch (v.getId()){	
		
		case R.id.penOreraser:case R.id.penOreraser_linear:   //ѡ����Ƥ
			activity.bottomSlider_select((Integer)v.getTag(), 1,R.id.penOreraser,R.id.penOreraser_linear);
			break;
		case R.id.pen:case R.id.pen_linear:              //ѡ�񻭱�
			activity.bottomSlider_select((Integer)v.getTag(), 2,R.id.pen,R.id.pen_linear);
			break;
		case R.id.backgroundColor:case R.id.background_linear:   //ѡ�񱳾�ͼƬ
			activity.bottomSlider_select((Integer)v.getTag(), 3,R.id.backgroundColor,R.id.background_linear);
		    break;
		    
		case R.id.unDoOrreDo:case R.id.delete_linear:        //ѡ��undo��ť
			activity.bottomSlider_select((Integer)v.getTag(), 4,R.id.unDoOrreDo,R.id.delete_linear);
            break;
            
		case R.id.chooseColor:case R.id.chooseColor_linear:           //ѡ����ɫ
			Log.i("BottomSliderView","choosecolor");
			activity.startActivityToColorPicker();
			break;
		}
	}
	
	public BottomSliderOnClickListener(MainActivity activity)
	{
		this.activity = activity;
	}

}
