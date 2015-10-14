package com.example.drawdemo04.SocietyModel;

import com.example.drawdemo04.Tools;

public class OfferCommentObject {

	private String content;
	private String url;
	private String title;
	private String synctower;   //Í·ÏñêÇ³ÆÐÅÏ¢
	
	public OfferCommentObject(String content,String url,String synctower)
	{
		this.content = content;
		this.url = url;
		this.synctower = synctower;
		this.title = Tools.TUYA_MODLE;
	}
	public String getContent()
	{
		return content;
	}
	public String getUrl()
	{
		return url;
	}
	public String getTitle()
	{
		return title;
	}
	public String getSynctower()
	{
		return synctower;
	}
}
