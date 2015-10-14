package com.example.drawdemo04.SocietyModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.autoset_widget.RecyclingBitmapDrawable;
import com.example.drawdemo04.autoset_widget.RecyclingImageView;
import com.example.drawdemo04.initModel.Init_choose;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter{

	private int galleryItemBackground;
	private Context context;
	private ArrayList<String> imgUrls;
	public static int itemwidth;
	public static int itemheight;
	private AsyncBitmapLoader asynloader;
	private ExecutorService fixedthreadpool;
	private Bitmap initbit;
	private Bitmap add_bit;
	private RecyclingBitmapDrawable littledrawable;
	
	public GalleryAdapter(Context context,ArrayList<String> imgUrls,int screenwidth,
			int screenheight,ExecutorService fixedthreadpool,AsyncBitmapLoader asynloader,Bitmap initbit){
		this.context = context;
		this.imgUrls = imgUrls;
		this.initbit = initbit;
		
		itemwidth = Tools.itemWidth;
		itemheight = Tools.itemHeight;
		
		Log.i("itemWidth",String.valueOf(itemwidth));
		Log.i("itemHeight",String.valueOf(itemheight));
		
		this.fixedthreadpool = fixedthreadpool;
        this.asynloader = asynloader;
       
        int rectWidth = itemwidth;
        int rectHeight = itemheight;
        
//        Bitmap bitmap = Bitmap.createBitmap(rectWidth, rectHeight, Tools.config);
        Canvas canvas = new Canvas(initbit);
        Rect rect = new Rect(0, 0, rectWidth, rectHeight);
        
        /*
        Paint paint = new Paint();
		paint.setStyle(Style.STROKE);// 设置非填充
		paint.setStrokeWidth(20);// 笔宽5像素
		paint.setColor(Color.GRAY);// 设置为红笔
		paint.setAntiAlias(true);// 锯齿不显示
        canvas.drawRect(rect, paint);
        */
        
  //      canvas.drawBitmap(initbit, 20, 20, null);
  //      this.initbit = bitmap;
        
        if(littledrawable == null)
        	composeAddbit();
	}
	public void setInitBit(Bitmap initbit)
	{
		this.initbit = initbit;
	}
	private void composeAddbit()
	{
		RecyclingBitmapDrawable add_drawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMemCache(Tools.init_add_key);
		if(add_drawable != null && !add_drawable.getBitmap().isRecycled())
		{
			add_bit = add_drawable.getBitmap();
		}
		else
		{
			Bitmap add = Tools.decodeAdjustedBitmapFromSrc(context, R.drawable.passagelistbg2, itemwidth, itemheight);
			add_bit = Tools.zoomBitmap(add, itemwidth, itemheight);
			Init_choose.asynloader.addBitmapToMemoryCache(Tools.init_add_key, new RecyclingBitmapDrawable(context.getResources(), add_bit));
		}
		
		Canvas canvas = new Canvas(add_bit);
		Bitmap little_add;
		RecyclingBitmapDrawable little_drawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMemCache(Tools.init_little_key);
		
		if(little_drawable != null && !little_drawable.getBitmap().isRecycled())
			little_add = little_drawable.getBitmap();
		else
		{
			Bitmap little = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add_normal);
			little_add = Tools.zoomBitmap(little, (int)(itemwidth*0.3), (int)(itemwidth*0.3));
			Init_choose.asynloader.addBitmapToMemoryCache(Tools.init_little_key, new RecyclingBitmapDrawable(context.getResources(), little_add));
		}
		
		canvas.drawBitmap(little_add, itemwidth*0.3f, itemheight*0.35f, null);
		
        littledrawable = new RecyclingBitmapDrawable(context.getResources(), add_bit);
        Init_choose.asynloader.addBitmapToMemoryCache(Tools.add_key, littledrawable);
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if(imgUrls != null)
			return imgUrls.size() + 1;
		else
			return 1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		// TODO 自动生成的方法存根
	    RecyclingImageView displayview;
		Log.i("GalleryAdapter","getView");
		
		if(convertview == null)
		{
			displayview = new RecyclingImageView(context);
	//		displayview.setImageBitmap(null);
			
			displayview.setLayoutParams(new Gallery.LayoutParams(itemwidth,itemheight));
			displayview.setScaleType(ImageView.ScaleType.CENTER);
			displayview.setBackgroundResource(galleryItemBackground);		

		}
		else
		{
			displayview = (RecyclingImageView)convertview;
			displayview.setImageBitmap(null);
		}	
		
		if(position == 0)
		{
			if((littledrawable = (RecyclingBitmapDrawable) Init_choose.asynloader.getBitmapFromMem("add_bit")) != null && !littledrawable.getBitmap().isRecycled())
			{
				displayview.setImageDrawable(littledrawable);
			}	
			else
			{
				composeAddbit();
				displayview.setImageDrawable(littledrawable);
			}
		}
			
		else
		{
			RecyclingBitmapDrawable value;
			if(position == 1)
				value = (RecyclingBitmapDrawable) asynloader.loadBitmap(displayview, imgUrls.get(position-1), fixedthreadpool,itemwidth,itemheight,false,null);
			else
				value = (RecyclingBitmapDrawable) asynloader.loadBitmap(displayview, imgUrls.get(position-1), fixedthreadpool,itemwidth,itemheight,false,initbit);

			if(value == null || (value != null && (value.getBitmap() == null || value.getBitmap().isRecycled())))
			{
				Bitmap bit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
				displayview.setImageDrawable(new RecyclingBitmapDrawable(context.getResources(), bit));
			}
			else
				displayview.setImageDrawable(value);
		}
		return displayview;
	}


}
