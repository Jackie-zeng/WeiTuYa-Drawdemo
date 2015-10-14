package com.example.drawdemo04.SocietyModel;

import java.util.ArrayList;

public class Tuya {

	private ArrayList<String> totalurl = new ArrayList<String>();
	private ArrayList<String> thumburl = new ArrayList<String>();
	
	public ArrayList<String> getTotalurlList(){
		return totalurl;
	}
	public ArrayList<String> getThumburlList(){
		return thumburl;
	}
	
	public void addTotalurl(String url){
		totalurl.add(url);
	}
	public void addThumburl(String url){
		thumburl.add(url);
	}
	public void clear()
	{
		totalurl.clear();
		thumburl.clear();
	}
}
