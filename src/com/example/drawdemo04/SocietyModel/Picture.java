package com.example.drawdemo04.SocietyModel;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Picture {

	private String commentinfo;
	private String agreeinfo;
//	private Bitmap bitmap;
	private String imageUrl;
	
	public Picture(String agreeinfo,String commentinfo,String imageUrl){
		this.agreeinfo = agreeinfo;
		this.commentinfo = commentinfo;
		this.imageUrl = imageUrl;
	}

	public void setAgreeinfo(String agreeinfo){
		this.agreeinfo = agreeinfo;
	}
	public String getAgreeinfo(){
		return agreeinfo;
	}
	
	public void setCommentinfo(String commentinfo){
		this.commentinfo = commentinfo;
	}
	public String getCommentinfo(){
		return commentinfo;
	}
	
	public void setUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	public String getUrl(){
		return imageUrl;
	}
}
