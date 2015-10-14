package myListener;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.SelectChooseLayerview;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.PenGraphycal.ReDoDraw;
import com.example.drawdemo04.PenGraphycal.UnDoDraw;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class LayerOnItemClickListener implements OnItemClickListener{

//	private int tag;
//	private int pic_id;
	private MainActivity activity;
//	private SelectChooseLayerview chooseLayerWindow;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		int tag = activity.getTag();
		int pic_id = activity.getPicId();
		SelectChooseLayerview chooseLayerWindow = activity.getChooseLayerWindow();
		// TODO �Զ����ɵķ������
		if(tag == -2)        //tag == -2 ��ʾ��ǰ myView������ʾ����������Ϳѻͼ
		{			
			activity.showMyView(arg2 - 2, null, null);	
		}
		else               //tag !=-2 ��ʾ��ǰmyView������ʾ����ĳһ�ֲ�ͼ�����û���һ��ѡ��ķֲ�ͼ
		{
			if(tag == -3)
				tag = pic_id - 1;
			
			Tools.refresh_arr.clear();
			Tools.refresh_arr.add(0);
			for(int i=tag+1;i<UnDoDraw.saved.size();i++)
			{
				Tools.refresh_arr.add(i+2);
			}
			Tools.clearMemoryCache();            //�����Ӧͼ��Ļ���
			
			for(int i=0;i<UnDoDraw.currentsaved.size();i++)        //���ͼ��󣬻Ὣ��ǰ��Ϳѻ�������浽 Ϳѻ�����Ļ���ջ UndoDraw.saved��
			{
				UnDoDraw.saved.add(tag+1+i, UnDoDraw.currentsaved.get(i));
			}
					    
		    if((arg2 - 2) > tag)
		        tag = arg2 - 2 + UnDoDraw.currentsaved.size();
		    else
		    	tag = arg2 - 2;
		    UnDoDraw.currentsaved.clear();                     //�����ǰͿѻ�����Ļ���վ UndoDraw.currentsaved
		    ReDoDraw.currentredoList.clear();                  //�����ǰͿѻ������redo����ջ
		    
		    activity.showMyView(tag, null, null);
		    activity.setRecoverOnClickNum(0);
		}
		if(chooseLayerWindow != null)
			chooseLayerWindow.dismiss();
	
	}
	public LayerOnItemClickListener(MainActivity activity)
	{
		this.activity = activity;

	}
}
