package com.example.drawdemo04.SocietyModel;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Comment {

	private String date;
	private String name;
	private String time;
	private String content;
	private String headimg;
//	private String initpic;
//	private String tuyapic;
	private String displayurl;
	private String url;
	
	public Comment(String name,String date,String time,String content,String headimg,String url){
		this.name = name;
		this.date = date;
		this.time = time;
		this.content = content;
		this.headimg = headimg;
		this.url = url;
	//	this.initpic = initpic;
	//	this.tuyapic = tuyapic;
	}
	public Comment (String displayurl){
		this.displayurl = displayurl;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	public String getDate(){
		return date;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	public String getTime(){
		return time;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	public String getContent(){
		return content;
	}
	
	public void setImageURL(String headimg){
		this.headimg = headimg;
	}
	public String getImageURL(){
		return headimg;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return url;
	}
	/*
	public void setInitpic(String initpic){
		this.initpic = initpic;
	}
	public String getInitpic(){
		return initpic;
	}
	
	public void setTuyapic(String tuyapic){
		this.tuyapic = tuyapic;
	}
	public String getTuyapic(){
		return tuyapic;
	}
	*/
}
