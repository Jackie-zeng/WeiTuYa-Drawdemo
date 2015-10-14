package com.example.drawdemo04.DataBase;

import com.example.drawdemo04.SocietyModel.Comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBadapter{
	public static final String KEY_ID = "_id";
	public static final String KEY_Date = "date";   //日月
	public static final String KEY_TIME = "time";   //时间
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_HEADIMG = "headimg";
//	private static final String KEY_INITID = "initpic";
//	private static final String KEY_TUYAID = "tuyapic";
	private static final String KEY_UNIQUEURL = "uniqueUrl";
	private static final String DATABASE_NAME = "Comment";
	private static final String TAG = "DBadapter";
	private static final String  DATABASE_TABLE = "commentrecord";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE ="create table commentrecord (_id integer primary key autoincrement,"+ "date text not null," +
			"time text not null,author text not null,content text not null,headimg text not null,"+ "uniqueUrl text not null);";
	private final Context context;
	private SQLiteDatabase db;
	public  DatabaseHelper DBHelper;
	
	public DBadapter(Context context){
		this.context = context;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO 自动生成的方法存根
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVerrsion) {
			// TODO 自动生成的方法存根
			
		}
	}
	public DBadapter open() 
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		db.close();
		DBHelper.close();
	}
	
    public long insertComment(Comment comment){
    	ContentValues initialValue = new ContentValues();
    	initialValue.put(KEY_Date, comment.getDate());
    	initialValue.put(KEY_TIME,comment.getTime());
    	initialValue.put(KEY_AUTHOR, comment.getName());
    	initialValue.put(KEY_CONTENT, comment.getContent());
    	initialValue.put(KEY_HEADIMG, comment.getImageURL());
    	initialValue.put(KEY_UNIQUEURL, comment.getUrl());
   // 	initialValue.put(KEY_INITID, comment.getInitpic());
   // 	initialValue.put(KEY_TUYAID,comment.getTuyapic());
    	
		return db.insert(DATABASE_TABLE, null, initialValue);	
    }
    
    public Cursor getCommentByID(String uniqueUrl){
    	String selection = KEY_UNIQUEURL + "=" + uniqueUrl ;
    	return db.query(DATABASE_TABLE, new String[] {KEY_Date,KEY_TIME,KEY_AUTHOR,KEY_CONTENT,KEY_HEADIMG},selection, null, null, null, null);
    }
    public void deleteAll()
    {
    	db.delete(DATABASE_TABLE, null, null);
    }	
}
