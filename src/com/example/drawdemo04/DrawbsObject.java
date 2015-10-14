package com.example.drawdemo04;

import com.example.drawdemo04.PenGraphycal.DrawBS;

public class DrawbsObject {

	public DrawBS drawBS;
	public int firstindex = -1;
//	public int secondindex = -1;
	public DrawbsObject(DrawBS drawBS,int firstindex)
	{
		this.drawBS = drawBS;
		this.firstindex = firstindex;
	}
	/*
	public DrawbsObject(DrawBS drawBS,int firstindex,int secondindex)
	{
		this.drawBS = drawBS;
		this.firstindex = firstindex;
		this.secondindex = secondindex;
	}
	*/
}
