package com.example.drawdemo04.initModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SingleSelectionDialog extends Dialog {
	
	public  static int checkedIndex = -1;
	
	public SingleSelectionDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) 
	{
		super(context, cancelable, cancelListener);
	}

	public SingleSelectionDialog(Context context, int theme) {
		super(context, theme);
	}

	public SingleSelectionDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private CharSequence title;
//		private CharSequence[] mListItem;
//		private int mClickedDialogEntryIndex;
		private ArrayList<HashMap<String, Object>> mHashMaps ; 
		private DialogInterface.OnClickListener mOnClickListener;
		private DialogInterface.OnClickListener addOnClickListener;
//		private android.view.View.OnClickListener addOnClickListener;
		private SingleSelectionDialog dialog;
//		private MyAdapter mAdapter ;
		
		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(CharSequence title) {
			this.title = title;
			return this;
		}

		public ArrayList<HashMap<String, Object>> getItems() {
			return mHashMaps;
		}

		public Builder setItems(ArrayList<HashMap<String, Object>> mHashMaps) {
			this.mHashMaps = mHashMaps;
			return this;
		}

		public Builder setSingleChoiceItems(ArrayList<HashMap<String, Object>> mHashMaps, int checkedItem, final OnClickListener listener,final OnClickListener add_listener) {
			this.mHashMaps = mHashMaps;
            this.mOnClickListener = listener;
            this.addOnClickListener = add_listener;
            checkedIndex = checkedItem;
            return this;
        } 

	
		public SingleSelectionDialog create() 
		{
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			dialog = new SingleSelectionDialog(
					context, R.style.Theme_Dialog_ListSelect);
			
			View layout = inflater.inflate(R.layout.single_selection_dialog,
					null);
			
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			AlertDialog ad;
			
			if(mHashMaps == null){
				throw new RuntimeException("Entries should not be empty");
			}
			ListView lvListItem = (ListView) layout.findViewById(R.id.lvListItem);
			Button addAccount = (Button)layout.findViewById(R.id.addMoreAccount);
//			lvListItem.setAdapter(new ArrayAdapter(context,  R.layout.single_selection_list_item, R.id.ctvListItem, mListItem));
//			SimpleAdapter mAdapter = new SimpleAdapter(context, getData(), R.layout.simpleitem, new String[]{"image", "title", "info"}, new int[]{R.id.img, R.id.title, R.id.info});
			
			final float scale = context.getResources().getDisplayMetrics().density;
			int length = Tools.Dp2Px(70, scale);
			MyAdapter mAdapter = new MyAdapter(context, mHashMaps, lvListItem,length);
			lvListItem.setAdapter(mAdapter);
			lvListItem.setOnItemClickListener(onItemClick);
			
			addAccount.setOnClickListener(addOnClick);
			
			TextView tvHeader = (TextView)layout.findViewById(R.id.title);
			tvHeader.setText(title);
	
			return dialog;
		}	
		
	    private android.view.View.OnClickListener addOnClick = new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				if(addOnClickListener != null)
					addOnClickListener.onClick(dialog, 0);
			}
		};
		
	    private OnItemClickListener onItemClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				
				  ListView lv = (ListView)arg0;
	              if(checkedIndex != arg2) 
	              {  
	                  int childId = checkedIndex - lv.getFirstVisiblePosition();  
	                  if(childId >= 0)
	                  {           //如果checked =true的radio在显示的窗口内，改变其状态为false
	                      View item = lv.getChildAt(childId);  
	                      if(item != null)
	                      {  
	                          RadioButton rb = (RadioButton)item.findViewById(checkedIndex);  
	                          if(rb != null)  
	                          rb.setChecked(false);  
	                      }  
	                  }
	                  //将当前点击的radio的checked变为true
	                  RadioButton rb1 = (RadioButton)arg1.findViewById(arg2);  
	                  if(rb1 != null)  
	                  rb1.setChecked(true);
	                  checkedIndex = arg2;
	              }
	              if(mOnClickListener != null)
	            	  mOnClickListener.onClick(dialog, arg2);
	       
			}
		};
		
		public class MyAdapter extends BaseAdapter{

			private LayoutInflater mInflater;
		    private ArrayList<HashMap<String, Object>> mData;
		    private ListView listView;
		    private int length;
		    
			public MyAdapter(Context context,ArrayList<HashMap<String, Object>> mData,ListView listView,int length){
				this.mInflater = LayoutInflater.from(context);
				this.mData = mData;
				this.listView = listView;
				this.length = length;
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mData.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				ViewHolder holder = null;
				if (convertView == null)     //convertView 存放item的
				{
					holder=new ViewHolder();  			
					convertView = mInflater.inflate(R.layout.simpleitem, null);
					holder.img = (RecyclingImageView)convertView.findViewById(R.id.img);
					holder.title = (TextView)convertView.findViewById(R.id.title);
					holder.info = (TextView)convertView.findViewById(R.id.info);
					holder.viewBtn = (RadioButton)convertView.findViewById(R.id.listview2_radiobutton);
					convertView.setTag(holder);
					
				}else {
					
					holder = (ViewHolder)convertView.getTag();
				}
				
				holder.title.setText((String)mData.get(position).get("title"));
				holder.info.setText((String)mData.get(position).get("info"));
				
				MyHandler handler = new MyHandler(holder.img);
				RecyclingBitmapDrawable mvalue = null;
				String imageUrl =  Tools.titleSaved_path + Tools.title_name + (String)mData.get(position).get("image") + ".png";
				mvalue = (RecyclingBitmapDrawable) Init_choose.asynloader.getAdustedBitmap(4,imageUrl,Init_choose.fixedthreadpool, handler, 
			    		length, length); 
				if(mvalue == null || mvalue.getBitmap() == null)
				{
					Bitmap titlebit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_tuya);
					RecyclingBitmapDrawable title_value = new RecyclingBitmapDrawable(context.getResources(), titlebit);
					holder.img.setBackgroundDrawable(title_value);
				}
				else
					holder.img.setBackgroundDrawable(mvalue);
				
				//让子控件button失去焦点  这样不会覆盖掉item的焦点  否则点击item  不会触发响应即onItemClick失效
				holder.viewBtn.setFocusable(false);//无此句点击item无响应的
				holder.viewBtn.setId(position);  
				holder.viewBtn.setChecked(position == checkedIndex);  
				
				holder.viewBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  
	                @Override  
	                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
	                    if(isChecked){  
	                        //set pre radio button  
	                        if(checkedIndex != -1)  
	                        {  
	                            int childId = checkedIndex - listView.getFirstVisiblePosition();  
	                            if(childId >= 0){  
	                                View item = listView.getChildAt(childId);  
	                                if(item != null){  
	                                    RadioButton rb = (RadioButton)item.findViewById(checkedIndex);  
	                                    if(rb != null)  
	                                    rb.setChecked(false);  
	                                }  
	                            }    
	                        }  
	                        //set cur radio button  
	                        checkedIndex = buttonView.getId();  
	                    }  
	                }  
	            });  
				return convertView;
			}
			
		}
		
		public final class ViewHolder{  
	        public RecyclingImageView img;  
	        public TextView title;  
	        public TextView info;  
	        public RadioButton viewBtn;  
	    } 
		
		private class MyHandler extends Handler
		{
			private RecyclingImageView view;
			public MyHandler(RecyclingImageView view)
			{
				this.view = view;
			}
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				case 0:
					RecyclingBitmapDrawable value = (RecyclingBitmapDrawable)(msg.obj);
					if(value != null && !value.getBitmap().isRecycled())
						view.setImageDrawable(value);
					break;
				}
			}
		}
		
	} 
}
