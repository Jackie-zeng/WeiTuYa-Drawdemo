package com.example.drawdemo04;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class SelectDialog extends Dialog{

	public int layoutId;
	
	public SelectDialog(Context context, int theme, int layoutId) {
	    super(context, theme);
	    this.layoutId = layoutId;
	}

	public SelectDialog(Context context, int layoutId) {
	    super(context);
	    this.layoutId = layoutId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(layoutId);
	}
}