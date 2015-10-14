package com.example.drawdemo04.initModel;

import java.util.ArrayList;

import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;
import com.example.drawdemo04.SocietyModel.Comment;
import com.example.drawdemo04.SocietyModel.HttpUtil;
import com.example.drawdemo04.autoset_widget.SelectDialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class GenerateCode extends Activity{

	private int firstSucceed = 0;
	private ImageView displayCode_view;
	private Bitmap QRbit = null;
	private getUploadURL_AsyncTask asynctask;
	private Comment comment;
	private SelectDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_qrcode);
	    displayCode_view = (ImageView)findViewById(R.id.QrImage);   
	}
	
	@Override
	public void onResume()
	{
		float scale = getResources().getDisplayMetrics().density;
		asynctask = new getUploadURL_AsyncTask(this);
		asynctask.execute(HttpUtil.getUpLoadURL);
		super.onResume();
	}
	@Override
	public void onPause()
	{
		if(QRbit != null && !QRbit.isRecycled())
		{
			if(displayCode_view != null)
				displayCode_view.setImageBitmap(null);
			QRbit.recycle();
			QRbit = null;
		}
		super.onPause();
	}
	
	private class getUploadURL_AsyncTask extends AsyncTask<String, Integer, String>
	{
        Context context;
        public getUploadURL_AsyncTask(Context context)
        {
        	this.context = context;
        }
		@Override
		protected String doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String getUpLoadURL = params[0];
			return HttpUtil.getUpLoadQRurl(getUpLoadURL, context,firstSucceed);
		}
		@Override
		protected void onPostExecute(String result)
		{
			if(progress != null)
			   Tools.closeProgress(progress);
			if(result.startsWith(HttpUtil.judgeIsUrl))
			{
				QRbit = Tools.createQRImage(result,Tools.Dp2Px(100, Tools.scale),Tools.Dp2Px(100, Tools.scale));
				if(displayCode_view != null)
				   displayCode_view.setImageBitmap(QRbit);
			}
			else
			{
				Toast.makeText(context, result, 3000).show();
			}
		}
		@Override
		protected void onPreExecute()
		{
			progress = Tools.showProgress(GenerateCode.this);
		}
	}

}
