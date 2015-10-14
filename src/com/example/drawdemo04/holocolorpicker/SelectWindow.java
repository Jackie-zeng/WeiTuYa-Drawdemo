package com.example.drawdemo04.holocolorpicker;

import com.example.drawdemo04.PenGraphycal.DisplayView;
import com.example.drawdemo04.PenSize.PenSizeBar;
import com.example.drawdemo04.PenSize.PenSizeBar.OnThicknessChangedListener;
import com.example.drawdemo04.holocolorpicker.ColorPicker;
import com.example.drawdemo04.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.example.drawdemo04.holocolorpicker.OpacityBar;
import com.example.drawdemo04.holocolorpicker.SVBar;
import com.example.drawdemo04.initModel.Init_choose;

import com.example.drawdemo04.MainActivity;
import com.example.drawdemo04.MyView;
import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SelectWindow extends Activity implements OnColorChangedListener {

	    private ColorPicker picker;
		private SVBar svBar;
		private OpacityBar opacityBar;
		private PenSizeBar transparencyBar,penSizebar;
        private MyView myview;
        private DisplayView display;
        private int currentTool;
        private TextView pensize;
        private String text = null;
        private boolean door = false;
        private int lastX ;
        private int moveDistance;
        
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.color_dialog);
			/*
			LayoutInflater factory = LayoutInflater  
		                .from(SelectWindow.this); 
			RelativeLayout selectColorView = (RelativeLayout)factory.inflate(R.layout.color_dialog, null);
				
			LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.colorpicker_layout_left_in); 
			selectColorView.setLayoutAnimation(controller);
			setContentView(selectColorView);
			*/
			currentTool = getIntent().getIntExtra("tool", 0);
			text = getIntent().getStringExtra("text");
			
			picker = (ColorPicker) findViewById(R.id.picker);
			svBar = (SVBar) findViewById(R.id.svbar);
			opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
			penSizebar = (PenSizeBar)findViewById(R.id.penSizebar);
			transparencyBar = (PenSizeBar)findViewById(R.id.penTransparencybar);
			pensize = (TextView)findViewById(R.id.penSize);
			
			svBar.setPosition(MainActivity.SVB_position);
			opacityBar.setPosition(MainActivity.Opacity_position);
			penSizebar.setPosition(MainActivity.PenSizeBar_position);
			transparencyBar.setPosition(MainActivity.Transparency_position);
			
			
			adjustWidget();
			picker.addSVBar(svBar);
			picker.addOpacityBar(opacityBar);
			picker.setOnColorChangedListener(this);
			penSizebar.setOnThicknessChangedListener(thickness_onChanged);
			transparencyBar.setOnThicknessChangedListener(transparency_onChanged);
			
			RelativeLayout backRelative = (RelativeLayout)findViewById(R.id.graphycaltype);
			backRelative.setOnTouchListener(moveOntouch);
			
			DisplayMetrics dm=new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(dm);
			int screenWidth=dm.widthPixels;
			moveDistance = (int)(screenWidth*(1.0/4));
		}
		
	    private void adjustWidget()
	    {
	    	/*
	    	RelativeLayout.LayoutParams params = (LayoutParams) picker.getLayoutParams();
	    	params.topMargin = (int)(Tools.screenHeight*0.02);
	    	picker.setLayoutParams(params);
	    	
	    	RelativeLayout layout = (RelativeLayout)findViewById(R.id.svb_layout);
	    	params = (LayoutParams)layout.getLayoutParams();
	    	params.topMargin = (int)(0.01*Tools.screenHeight);
	    	layout.setLayoutParams(params);
	    	
	    	layout = (RelativeLayout)findViewById(R.id.opacity_layout);
	    	params = (LayoutParams)layout.getLayoutParams();
	    	params.topMargin = (int)(0.01*Tools.screenHeight);
	    	layout.setLayoutParams(params);
	    	
	    	layout = (RelativeLayout)findViewById(R.id.pensize_layout);
	    	params = (LayoutParams)layout.getLayoutParams();
	    	params.topMargin = (int)(0.01*Tools.screenHeight);
	    	layout.setLayoutParams(params);
	    	
	    	layout = (RelativeLayout)findViewById(R.id.transparency_layout);
	    	params = (LayoutParams)layout.getLayoutParams();
	        
	    	params.topMargin = (int)(0.01*Tools.screenHeight);
	    	layout.setLayoutParams(params);
	    	*/
	    	
	    	LinearLayout displaylinear = (LinearLayout)findViewById(R.id.display_linear);
	    	display = new DisplayView(SelectWindow.this,Tools.screenWidth,(int)(Tools.screenHeight*0.2));
			display.setBackgroundColor(Color.TRANSPARENT);
			displaylinear.removeAllViews();
			displaylinear.addView(display);
			
			display.setDrawTool(currentTool, text);
			
			transparencyBar.setMaximum(200);
			
			float scale = getResources().getDisplayMetrics().density;
		   
		 	if(MainActivity.graphicOrtext == MainActivity.GRAPHY)
		 	{
		 	    penSizebar.setMaximum((int)(scale*5)*2);
		 	    pensize.setText(getString(R.string.paintsize));
		 	}
		 	else
		 	{
		 	    penSizebar.setMaximum((int)(scale*37)*2);
		 	    pensize.setText(getString(R.string.textsize));
		 	}
	    }


		@Override
		public void onColorChanged(int color) {
			//gives the color when it's changed.
			
			if(display != null)
				display.drawByColor(color);
			Intent intent = new Intent();
			intent.putExtra("color", color);
			
			if(MainActivity.graphicOrtext == MainActivity.GRAPHY)
				intent.putExtra("thickness", MainActivity.thickness);
			else
				intent.putExtra("thickness", MainActivity.textSize);
			
			intent.putExtra("alpha", MainActivity.transparency);
			intent.setAction(Tools.Colorchange_flag);
			sendBroadcast(intent);
		}
		
		private OnThicknessChangedListener thickness_onChanged = new OnThicknessChangedListener() {
			
			@Override
			public void onThicknessChanged(int opacity,int position) {
				// TODO 自动生成的方法存根
				if(display != null)
					display.drawByPenThickness(opacity);
				MainActivity.PenSizeBar_position = position;
				
				Intent intent = new Intent();
				
				intent.putExtra("color", MainActivity.currentColor);
				intent.putExtra("thickness", opacity);
				intent.putExtra("alpha", MainActivity.transparency);
				
				intent.setAction(Tools.Colorchange_flag);
				sendBroadcast(intent);
			}
		};
		private OnThicknessChangedListener transparency_onChanged = new OnThicknessChangedListener() {
			
			@Override
			public void onThicknessChanged(int opacity,int position) {
				// TODO 自动生成的方法存根
				if(display != null)
					display.drawByPenTransparency(opacity);
				MainActivity.Transparency_position = position;
				
				Intent intent = new Intent();
				intent.putExtra("color", MainActivity.currentColor);
				
				if(MainActivity.graphicOrtext == MainActivity.GRAPHY)
					intent.putExtra("thickness", MainActivity.thickness);
				else
					intent.putExtra("thickness", MainActivity.textSize);
				
				intent.putExtra("alpha", opacity);
				intent.setAction(Tools.Colorchange_flag);
				sendBroadcast(intent);
			}
		};
		
		private OnTouchListener moveOntouch = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO 自动生成的方法存根
				
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					lastX = (int)event.getRawX();

					break;
					
				case MotionEvent.ACTION_MOVE:
					int dx=(int)event.getRawX()-lastX;
					
					if(dx > moveDistance)
					{
						finish();
					}
					break;
				case MotionEvent.ACTION_UP:
			
					break;
				}
				
				return true;
			}
		};
		
	     @Override
	     public void finish()
	     {
	    	 Log.i("finish","finish");
	    	 if(door == false)
	    	 {
	    		 door = true;
	        	 super.finish();
	    	 }
	     }
	     
	     @Override
	     public void onPause()
	     {
	    	 MainActivity.Opacity_position = opacityBar.getPosition();
	    	 MainActivity.SVB_position = svBar.getPosition();
	    	 super.onPause();
	     }
	}