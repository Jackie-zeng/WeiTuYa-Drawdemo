package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.SelectChooseLayerview;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

public class SaveOnClickListener implements OnClickListener{

//	private int tag;
//	private int pic_id;
	private MainActivity activity;
	
	@Override
	public void onClick(View arg0) {
		// TODO �Զ����ɵķ������
		int tag = activity.getTag();
		int pic_id = activity.getPicId();
		
		if(tag != -2)
		{
			if(tag == -3)
				tag = pic_id - 1;
			Log.i("SaveOnclickListener_tag",String.valueOf(tag));
			Tools.refresh_arr.clear();      
			Tools.refresh_arr.add(0);                 
			for(int i=tag+1;i<UnDoDraw.saved.size();i++)
			{
				Tools.refresh_arr.add(i+2);                       //�����Ҫ�������ķֲ�ͼ�����
			}
			Tools.clearMemoryCache();                                    //���֮ǰ�ֲ�ͼ�Ļ���
			 
			for(int i=0;i<UnDoDraw.saved1.size();i++)
			{
				Log.i("SaveOnclickListener",String.valueOf(UnDoDraw.saved1.size()));
				if(tag+1+i <= UnDoDraw.saved.size())
					UnDoDraw.saved.add(tag+1+i, UnDoDraw.saved1.get(i));
				else
					UnDoDraw.saved.add(UnDoDraw.saved1.get(i));
				
			}
			
		    UnDoDraw.saved1.clear();                                 //��յ�ǰͿѻ�����Ļ���ջ
    	    ReDoDraw.redoList1.clear();                              //��յ�ǰͿѻ������redoջ
    	    
		    activity.showLayerViewDialog();
		    activity.showMyView(-2, null, null); 
		    activity.setRecoverOnClickNum(0);
		}	
	}
	public SaveOnClickListener(MainActivity activity)
	{
//		this.tag = tag;
//		this.pic_id = pic_id;
		this.activity = activity;
	}

}
