package com.example.drawdemo04.SocietyModel;

public class GetfromNetWorkOb {

	private Object obj;
	private String toast_text;
	
	public GetfromNetWorkOb(Object o,String text) {
		// TODO 自动生成的构造函数存根
		obj = o;
		toast_text = text;
	}
	public void setObj(Object obj){
		this.obj = obj;
	}
	public void setToastText(String toastText)
	{
		this.toast_text = toastText;
	}
	public Object getObj(){
		return obj;
	}
	public String getToastText()
	{
		return toast_text;
	}
}
