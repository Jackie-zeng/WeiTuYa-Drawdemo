package com.example.drawdemo04.SocietyModel;

import android.test.suitebuilder.annotation.LargeTest;

public class MyPicInfo {

	private String image_url_small;
	private String url_small;
	private String uniqueUrl;
	private String url_origin;
	private String image_url_origin;
	
    public MyPicInfo(String image_url_small,String url_small,String uniqueUrl,String url_origin,String image_url_origin)
    {
    	this.image_url_small = image_url_small;
    	this.url_small = url_small;
    	this.uniqueUrl = uniqueUrl;
    	this.url_origin = url_origin;
    	this.image_url_origin = image_url_origin;
    }
    public String getImageUrlSmall(){
    	return image_url_small;
    }
    public String getUrlSmall(){
    	return url_small;
    }
    public String getUniqueUrl()
    {
    	return uniqueUrl;
    }
    public String getUrlOrigin()
    {
    	return url_origin;
    }
    public String getImageUrlOrigin()
    {
    	return image_url_origin;
    }
}
