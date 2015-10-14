package com.example.drawdemo04.SocietyModel;

public class PicInfo {

	private String imageUrl;
	private String largepicUrl;
	private String largepicId;
	private String uniqueUrl;   //用作评论系统中上传评论的url
	private String backPath;
	private String surfacePath;
	private String littlePath;
	private String configInfo;
	private int position;
	
	public PicInfo(String imageUrl,String largepicUrl,String largepicId,String uniqueUrl)
	{
		this.imageUrl = imageUrl;
		this.largepicUrl = largepicUrl;
		this.largepicId = largepicId;
		this.uniqueUrl = uniqueUrl;
	}
	public PicInfo() {
		// TODO 自动生成的构造函数存根
	}
	public String getImageUrl()
	{
		return imageUrl;
	}
	public String getLargepicUrl()
	{
		return largepicUrl;
	}
	public String getLargepicId()
	{
		return largepicId;
	}
	public String getUniqueUrl()
	{
		return uniqueUrl;
	}
	public void setBackPath(String backPath)
	{
		this.backPath = backPath;
	}
	public void setSurfacePath(String surfacePath)
	{
		this.surfacePath = surfacePath;
	}
	public void setLittlePath(String littlePath)
	{
		this.littlePath = littlePath;
	}
	public void setConfigInfo(String configInfo)
	{
		this.configInfo = configInfo;
	}
	public String getBackPath()
	{
		return backPath;
	}
	public String getSurfacePath()
	{
		return surfacePath;
	}
	public String getLittlePath()
	{
		return littlePath;
	}
	public String getConfigInfo()
	{
		return configInfo;
	}
	public void setPosition(int pos)
	{
		position = pos;
	}
	public int getPosition()
	{
		return position;
	}	
}
