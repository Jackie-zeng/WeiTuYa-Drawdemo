package com.example.drawdemo04.SocietyModel;

public class TuyaLayer {

	private String tuyaUrl;
	private String uniqueUrl;
	private String smallUrl;
	
	public TuyaLayer(String tuyaUrl,String smallUrl,String uniqueUrl)
	{
		this.tuyaUrl = tuyaUrl;
		this.smallUrl = smallUrl;
		this.uniqueUrl = uniqueUrl;
	}
	public String getTuyaUrl()
	{
		return tuyaUrl;
	}
	public String getUniqueUrl()
	{
		return uniqueUrl;
	}
	public String getSmallUrl()
	{
		return smallUrl;
	}
}
