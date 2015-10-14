package com.example.drawdemo04;

import java.util.ArrayList;

import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.util.Insertable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class LayerAdapter extends BaseAdapter implements Insertable<LayerView>{

	private ArrayList<LayerView> layerviews;
	private Context context;
	private int width,height;
//	private ArrayList<LayerView> saved_layerviews;
	private Activity activity;
	
	public LayerAdapter(ArrayList<LayerView> layerviews,Context context,int width,int height) 
	{
		this.context = context;
		this.layerviews = layerviews;
		this.width = width;
		this.height = height;
	}
	
	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}
	
	@Override
    public void add(int index,LayerView layerview)
    {
 //   	layerviews.add(index, layerview);
    	notifyDataSetChanged();
    	
    }
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return layerviews.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return layerviews.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return 0;
	}
 
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		// TODO 自动生成的方法存根	
		ViewHolder viewholder;
		RecyclingBitmapDrawable value;
		if(convertview == null)
		{
			View v = LayoutInflater.from(context).inflate(R.layout.display_item, null);
			viewholder = new ViewHolder();
			viewholder.image = (RecyclingImageView)v.findViewById(R.id.image);
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewholder.image.getLayoutParams();
			params.width = (int)(width*0.8);
			params.height = (int)(height*0.25);
			viewholder.image.setLayoutParams(params);
			
			convertview = v;
			convertview.setTag(viewholder);
		}
		else
		{
			viewholder = (ViewHolder)convertview.getTag();
		}
	   
		if((value = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMem(String.valueOf(position))) != null)
		{
			if(position == 0)
			     Log.i("position",String.valueOf(position));
		    viewholder.image.setImageDrawable(value);
		}
		else
		{
			Bitmap bit = layerviews.get(position).getBitmap();
			RecyclingBitmapDrawable mvalue = new RecyclingBitmapDrawable(context.getResources(), bit);
			viewholder.image.setImageDrawable(mvalue);
			Init_choose.asynloader.addBitmapToMemoryCache(String.valueOf(position), mvalue);
		}		
        
		
//		bit = layerviews.get(position).getBitmap();
//		viewholder.image.setImageBitmap(bit);
		return convertview;
		
	}
	
	public void remove(int index)
	{
	//	saved_layerviews.add(layerviews.get(index));
		
		/*
		if(index == 0)
		{
	//		notifyDataSetChanged();
			return ;
		}
		*/
		if(layerviews.size() > index && index != 1)
		{
			
			for(int i=index;i<layerviews.size()-1;i++)
			{
				Init_choose.asynloader.removeBitmapFromCache(String.valueOf(i));
				RecyclingBitmapDrawable mvalue = new RecyclingBitmapDrawable(context.getResources(), layerviews.get(i+1).getBitmap());
				Init_choose.asynloader.addBitmapToMemoryCache(String.valueOf(i), mvalue);
			}
			Init_choose.asynloader.removeBitmapFromCache(String.valueOf(layerviews.size()-1));
			
			if(index >= 2)
			{
				Init_choose.asynloader.removeBitmapFromCache(String.valueOf(0));
				RecyclingBitmapDrawable mvalue = new RecyclingBitmapDrawable(context.getResources(), layerviews.get(0).getBitmap());
				Init_choose.asynloader.addBitmapToMemoryCache(String.valueOf(0), mvalue);
			}
			
		    layerviews.remove(index);
		    notifyDataSetChanged();
		}
	}
	
	class ViewHolder 
	{
		RecyclingImageView image;
	}
}
