package com.example.drawdemo04.SocietyModel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.widget.Toast;

import com.example.drawdemo04.R;
import com.example.drawdemo04.Tools;

public class HttpUtil 
{
	public static int SUCCESS = 1;
	public static int FAIL = 0;
	public static String judgeIsUrl = "http://";
	public static String imageURL = "http://tuya.webapp.163.com/tuya/images";
	public static String getPicURL = "http://tuya.webapp.163.com/images";   
	public static String getTuyaLayerURL = "http://tuya.webapp.163.com/pics";
	public static String CommentURL = "http://tower.dev.webapp.163.com:8006";
//	public static String url = "?url=tuya.webapp.163.com";
	public static String offerCommentURL = CommentURL + "/comment";            //。。。。/comment
	public static String getCommentsURL = CommentURL + "/get-comments";              //..../get-comments
    public static String submit_SocietyImageURL = "http://tuya.webapp.163.com/upload/pic";
    public static String submit_LocalImageURL = "http://tuya.webapp.163.com/upload/image";
    public static String submit_TitleImageURL = "http://tuya.webapp.163.com/upload/avatar";
    public static String getTitleImg = "http://tuya.webapp.163.com/avatar";
    public static String getUpLoadURL = "http://tuya.webapp.163.com/login/get";
    public static String getTUYAID_URL = "http://tuya.webapp.163.com/login?device_id=";
    public static String getMyPicInfoURL = "http://tuya.webapp.163.com/pics/my";
    public static String getThemePicURL = "";
    public static String getUserNameURL = "";
    
	public static InputStream getStreamFromURL(String imageURL)
	{
		HttpGet imageRequest = new HttpGet(imageURL);
		InputStream in = null;
		int TIME_OUT = 10000;
	    try 
      	{
	    	HttpClient httpclent = new DefaultHttpClient();
			httpclent.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			httpclent.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
			
			HttpResponse httpResponse = httpclent.execute(imageRequest);
//			Log.i("HttpUtil_getStreamFromURL",httpResponse.getStatusLine().toString());
		    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			{
				in=httpResponse.getEntity().getContent();
			} 
		    
      	 }catch(ConnectionPoolTimeoutException e){
      		 
 			return null;
 			
 		}catch(ConnectTimeoutException e){

 			return null;
 		}
 		catch (SocketTimeoutException e) {
 			
 			return null;
 			
 		} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  	
		return in;		
	}
	
	public static ArrayList<PicInfo> getPicInfoFromURL(String imageURL,int pagesize,int page,Handler handler)
	{
		int TIME_OUT = 3000;
		ArrayList<PicInfo> picInfo_arr = null;
		HttpGet request = new HttpGet(imageURL + "?page_size=" + String.valueOf(pagesize) + "&page=" + String.valueOf(page));
//		HttpGet request = new HttpGet(imageURL);
		request.setHeader("Cookie", "TUYA_ID=" + Tools.DEVICE_ID);
		
		String strResult = "";
		
		HttpResponse httpResponse;
		try {
			/*
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, TIME_OUT);
			request.setParams(params);
			*/
			HttpClient httpclent = new DefaultHttpClient();
			httpclent.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			httpclent.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
			
			httpResponse = httpclent.execute(request);
            Log.i("statusline",httpResponse.getStatusLine().toString());
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
	//			Log.i("statusline",httpResponse.getStatusLine().toString());
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}		
			else
			{
		//		Toast.makeText(context, context.getString(R.string.failTosuccess), 3000).show();
				Message msg = handler.obtainMessage(0);
				handler.sendMessage(msg);
				return picInfo_arr;
			}
			
		}catch(ConnectionPoolTimeoutException e){
			Message msg = handler.obtainMessage(5);
			handler.sendMessage(msg);
			return picInfo_arr;
			
		}catch(ConnectTimeoutException e){
			Message msg = handler.obtainMessage(5);
			handler.sendMessage(msg);
			return picInfo_arr;
		}
		catch (SocketTimeoutException e) {
			Log.i("e",e.toString());
			e.printStackTrace();
			Message msg = handler.obtainMessage(4);
			handler.sendMessage(msg);
			return picInfo_arr;
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
		
			e.printStackTrace();
		}
		
		if(!strResult.equals(""))
		{
			try
			{
				JSONObject result = new JSONObject(strResult);
				String success = result.getString("success");
				if(success.equals("1"))
				{
					picInfo_arr = new ArrayList<PicInfo>();
					
					JSONArray pic_datas = result.getJSONArray("images");
					for(int i=0;i<pic_datas.length();i++)
					{
						JSONObject jso = pic_datas.getJSONObject(i);
						String imageurl = jso.getString("url_small");
						String uniqueurl = jso.getString("html_url");
						String largepicurl = jso.getString("url_origin");
						String largepicid = jso.getString("id");
						picInfo_arr.add(new PicInfo(imageurl, largepicurl, largepicid,uniqueurl));
					}
				}
				else
				{
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
//					Toast.makeText(context, context.getString(R.string.failToget), 3000).show();
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else
		{
			Message msg = handler.obtainMessage(1);
			handler.sendMessage(msg);
	//		Toast.makeText(context, context.getString(R.string.failToget), 3000).show();
		}
		
		return picInfo_arr;
	}
	
	public static ArrayList<TuyaLayer> getTuyaLayerFromURL(String URL,String picid,Handler handler)
	{
		ArrayList<TuyaLayer> tuyaLayer_arr = new ArrayList<TuyaLayer>();
		HttpGet request = new HttpGet(URL + "?image_id=" + picid);
		request.setHeader("Cookie", "TUYA_ID=" + Tools.DEVICE_ID);
		
		String strResult = "";
		HttpResponse httpResponse;
		try
		{
			httpResponse = new DefaultHttpClient().execute(request);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				strResult = EntityUtils.toString(httpResponse.getEntity());
			else
			{
		//		Toast.makeText(context, context.getString(R.string.failTosuccess), 3000).show();
				Message msg = handler.obtainMessage(0);
				handler.sendMessage(msg);
				return tuyaLayer_arr;
			}
			
		} catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(!strResult.equals(""))
		{
			try
			{
				JSONObject result = new JSONObject(strResult);
				String success = result.getString("success");
				if(success.equals("1"))
				{
					if(result.has("pics"))
					{
						JSONArray tuyaDatas = result.getJSONArray("pics");
				//		Log.i("nums",String.valueOf(tuyaDatas.length()));
						for(int i=0;i<tuyaDatas.length();i++)
						{
							JSONObject jso = tuyaDatas.getJSONObject(i);
							String orginurl = jso.getString("url_origin");
							String uniqueUrl = jso.getString("html_url");
							String smallurl = jso.getString("url_small");
							tuyaLayer_arr.add(new TuyaLayer(orginurl,smallurl,uniqueUrl));
						}
					}
					else
					{
						Message msg = handler.obtainMessage(2);
						handler.sendMessage(msg);
					}
					
				}
				else
				{
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else
		{
			Message msg = handler.obtainMessage(1);
			handler.sendMessage(msg);
		}
		
		return tuyaLayer_arr;	
	}
	
	public static void offerComment(String offerComment_url,OfferCommentObject commentOb,Handler handler)
	{
//		String url = offerComment_url + commentOb.getSynctower();
		String url = offerComment_url + "?url=" + commentOb.getUrl() + "&content=" + commentOb.getContent() + "&title=" + commentOb.getTitle();
		HttpGet request = new HttpGet(url);

	    String strResult = "";
	    
	    try
	    {
	    	HttpResponse httpResponse = new DefaultHttpClient().execute(request);
	    	Log.i("statusline",httpResponse.getStatusLine().toString());
	    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	    	{
	    		strResult = EntityUtils.toString(httpResponse.getEntity());
	    	}
	    	else
	    	{
	    		Message msg = handler.obtainMessage(0);   //上传失败
	    		handler.sendMessage(msg);
	    	}
	    	
	    }catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
	    Log.i("strresult",String.valueOf(strResult));
	    if(!strResult.equals(""))
	    {
	    	try {
				JSONObject result = new JSONObject(strResult);
				String status = result.getString("status");
				if(status != null && status.equals("ok"))
				{
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				else
				{
					Message msg = handler.obtainMessage(0);
					handler.sendMessage(msg);
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	    	
	    }
	    else
	    {
	    	Message msg = handler.obtainMessage(0);
			handler.sendMessage(msg);
	    }
	}
	
	public static ArrayList<Comment> getCommentsFromURL(String getComments_URL,String url,Handler handler)
	{
		int TIME_OUT = 3000;
		ArrayList<Comment> comments_arr = new ArrayList<Comment>();
		Log.i("HttpUtl_getCommentsFromURL",getComments_URL + "?url=" + url);
		HttpGet request = new HttpGet(getComments_URL + "?url=" + url);
	
		String strResult = "";
		try
		{
			/*
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, TIME_OUT);
			request.setParams(params);
			*/
		    HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
			HttpResponse httpResponse = httpclient.execute(request);
			
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				Log.i("getStatusline",String.valueOf(httpResponse.getStatusLine().toString()));
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				//连接失败
				Message msg = handler.obtainMessage(0);
				handler.sendMessage(msg);
				return comments_arr;
				// 连接失败
			}
		} catch(ConnectionPoolTimeoutException e){
			Message msg = handler.obtainMessage(6);
			handler.sendMessage(msg);
			return comments_arr;
			
		}catch(ConnectTimeoutException e){
			Message msg = handler.obtainMessage(6);
			handler.sendMessage(msg);
			return comments_arr;
		}
		catch (SocketTimeoutException e) {
			Message msg = handler.obtainMessage(6);
			handler.sendMessage(msg);
			return comments_arr;
			
		}catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if(!strResult.equals(""))
		{
			try
			{
				JSONObject result = new JSONObject(strResult);
				Log.i("HttpUtil_getComments",result.toString());
				String status = result.getString("status");
				if(status != null && status.equals("ok"))
				{
					if(result.has("top_comments"))
					{
						JSONArray topComments = result.getJSONArray("top_comments");
						if(topComments != null)
						{
							if(topComments.length() > 0)
							{
								for(int i=0;i<topComments.length();i++)
								{
									JSONObject jso = topComments.getJSONObject(i);
									String content = jso.getString("content");
									String nickname = jso.getString("nickname");
									String avatar = jso.getString("avatar");
									String allTime = jso.getString("time");
									
									String date = null;
									String time = null;
									
									Log.i("allTime",allTime);
									if(allTime != null && (!allTime.equals("")))
									{
										int firstT = allTime.indexOf("T");
										date = allTime.substring(5, firstT);
										time = allTime.substring(firstT+1, firstT+1+5);
										/*
										int firstDevide = time.indexOf(":");
										String str1 = time.substring(0, firstDevide);
										String str2 = time.substring(firstDevide + 1);
										str1 = String.valueOf(Integer.parseInt(str1) - 4);
										time = str1 + ":" + str2;
										*/
									}
									comments_arr.add(new Comment(nickname, date, time, content,avatar, url));
								}
							}
							else
							{
								Message msg = handler.obtainMessage(7);
								handler.sendMessage(msg);
							}
						}
						else
						{
							Message msg = handler.obtainMessage(1);
							handler.sendMessage(msg);
						}				
					}
					else
					{
						//没有数据
						Message msg = handler.obtainMessage(1);
						handler.sendMessage(msg);
					}
				}
				else
				{
					//获取失败
					Message msg = handler.obtainMessage(2);
					handler.sendMessage(msg);
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else
		{
			//获取失败
			Message msg = handler.obtainMessage(2);
			handler.sendMessage(msg);
		}
		return comments_arr;
		
	}
	
	public static String getTitleURL(Context context,String getImage_url,Handler handler,int times)
	{
		SharedPreferences mySharePreference = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		String cookie_str = mySharePreference.getString(Tools.identity,  "TUYA_ID=" + Tools.DEVICE_ID);
		int index = cookie_str.indexOf("=");
		cookie_str = cookie_str.substring(index + 1);
		
		String url = getImage_url + "?uid=" + cookie_str;
//		String url = getImage_url + "?uid=zhangzekai";
		HttpGet request = new HttpGet(url);
        String titleUrl = null;
	    String strResult = "";
	    
	    try
	    {
	    	HttpResponse httpResponse = new DefaultHttpClient().execute(request);
	    	Log.i("statusline",httpResponse.getStatusLine().toString());
	    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	    	{
	    		strResult = EntityUtils.toString(httpResponse.getEntity());
	    	}
	    	else
	    	{
	    		if(handler != null)
	    		{
	    			Message msg = handler.obtainMessage(0);   //上传失败
		    		handler.sendMessage(msg);
	    		}
	    		return titleUrl;
	    	}
	    	
	    }catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
	    if(!strResult.equals(""))
	    {
	    	Log.i("strResult",strResult);
	    	try {
				JSONObject result = new JSONObject(strResult);
				Log.i("HttpUtil_getTitileURL",result.toString());
				String status = result.getString("success");
				if(status != null && status.equals("1"))
				{
					titleUrl = result.getString("url_origin");
				}
				else
				{
					if(result.getString("err_msg").equals("login_failed") && times == 0)
					{
						times ++;
						int cookieResult = getTUYA_ID(getTUYAID_URL, context);
						if(cookieResult == 1)
						{
							return getTitleURL(context, getImage_url, handler, times);
						}
						else
						{
							if(handler != null)
							{
								Message msg = handler.obtainMessage(0);
				            	handler.sendMessage(msg);
							}
						}
					}
					if(handler != null)
					{
						Message msg = handler.obtainMessage(0);
						handler.sendMessage(msg);
					}	
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	    	
	    }
	    else
	    {
	    	if(handler != null)
	    	{
	    		Message msg = handler.obtainMessage(0);
				handler.sendMessage(msg);
	    	}
	    }
	    return titleUrl;
	}
	
	public static void submitImage(Context context,String submitImage_url,Bitmap bitmap,Handler handler,String image_id,int times)
	{
		int TIME_OUT = 10000;
		String CHARSET = "utf-8";
		String strResult = "";
		String BOUNDARY =  UUID.randomUUID().toString();
		String PREFIX = "--";
		String LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data";
		SharedPreferences mySharePreference = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		String cookie_str = mySharePreference.getString(Tools.identity, "TUYA_ID=" + Tools.DEVICE_ID);
		
		try {
			URL url;
			if(submitImage_url.equals(HttpUtil.submit_TitleImageURL))
				url = new URL(submitImage_url);
			else
			    url = new URL(submitImage_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			
			conn.setRequestProperty("Cookie", cookie_str);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			
			if(submitImage_url.equals(HttpUtil.submit_SocietyImageURL))
			{
				StringBuffer sb = new StringBuffer();
	            sb.append(PREFIX);
	            sb.append(BOUNDARY);
	            sb.append(LINE_END);
	            
	            String name="image_id";
	            sb.append("Content-Disposition: form-data; name=\"" + name  
	                    + "\"\r\n");  
	            sb.append("\r\n");  
	            sb.append(image_id + "\r\n"); 
	            dos.write(sb.toString().getBytes());
			}
            
			StringBuffer sa = new StringBuffer();
			sa.append(PREFIX);
			sa.append(BOUNDARY);
			sa.append(LINE_END);
			
			sa.append("Content-Disposition: form-data; name=\"upload\"; filename=\""+
			Tools.filename + "\""+LINE_END); 
			sa.append("Content-Type: image/png"+LINE_END);
			sa.append(LINE_END);
			dos.write(sa.toString().getBytes());
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] bytes = baos.toByteArray();
			
			dos.write(bytes);
			dos.write(LINE_END.getBytes());
			
			byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            
            int res = conn.getResponseCode();
            Log.i("res",String.valueOf(res));
            
            if(res == 200)
            {
            	InputStream input = conn.getInputStream();
            	StringBuffer resultbuff = new StringBuffer();
            	int s;
            	while((s = input.read()) != -1)
            	{
            		resultbuff.append((char)(s));
            	}
            	strResult = resultbuff.toString();
            }
            else
            {
            	Message msg = handler.obtainMessage(2);
            	handler.sendMessage(msg);
            	return ;
            }
			
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		if(!strResult.equals(""))
		{
			JSONObject result;
			try {
				result = new JSONObject(strResult);
				Log.i("HttpUtl_submitImg_result",result.toString());
				String success = result.getString("success");
				Log.i("success",String.valueOf(success));
				if(success.equals("1"))
				{
					Message msg = handler.obtainMessage(1);
					handler.sendMessage(msg);
				}
				else
				{
					if(result.getString("err_msg").equals("login_failed") && times == 0)
					{
						times ++;
						int cookieResult = getTUYA_ID(getTUYAID_URL, context);
						if(cookieResult == 1)
						{
							submitImage(context, submitImage_url, bitmap, handler, image_id, times);
						}
						else
						{
						 	Message msg = handler.obtainMessage(0);
			            	handler.sendMessage(msg);
						}
					}
					else
					{
					 	Message msg = handler.obtainMessage(0);
		            	handler.sendMessage(msg);
					}
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		else
		{
			Message msg = handler.obtainMessage(0);
        	handler.sendMessage(msg);
		}
	}
	public static String getUpLoadQRurl (String getUploadURL,Context context,int times)
	{
		int TIME_OUT = 3000;
		HttpPost request = new HttpPost(getUploadURL);
		
		SharedPreferences mySharePreferences = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		String cookie_str = mySharePreferences.getString(Tools.identity, "TUYA_ID=" + Tools.DEVICE_ID);
		
		Log.i("cookie",cookie_str);
		request.setHeader("Cookie", cookie_str);
		String strResult = "";
		
		HttpResponse httpResponse;
		try {		
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
			
			httpResponse = httpclient.execute(request);
            Log.i("statusline",httpResponse.getStatusLine().toString());
            
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}		
			else
			{
				return context.getString(R.string.failTosuccess);
			}
			
		}catch(ConnectionPoolTimeoutException e){
			return context.getString(R.string.ConnectTimeout);
			
		}catch(ConnectTimeoutException e){
			return context.getString(R.string.ConnectTimeout);
		}
		catch (SocketTimeoutException e) {
			return context.getString(R.string.ConnectTimeout);
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(!strResult.equals(""))
		{
			try
			{
				JSONObject result = new JSONObject(strResult);
				String success = result.getString("success");
				if(success.equals("1"))
				{
					return result.getString("url");
				}
				else
				{
					if(result.getString("err_msg").equals("login_failed") && times == 0)
					{
						times ++ ;
						int cookieResult = getTUYA_ID(getTUYAID_URL, context);
						if(cookieResult == 1)
						{
							return getUpLoadQRurl(getUploadURL, context, times);
						}
						else
						{
							return context.getString(R.string.failToget);
						}
					}
					else
						return context.getString(R.string.failToget);
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else
		{
			return context.getString(R.string.failToget);
		}
		return context.getString(R.string.failToget);
	}
	
	public static int getTUYA_ID(String getTUYAID_URL,Context context)
	{
		if(getTUYAID_URL == null || getTUYAID_URL.equals(""))
			return 0;
		String url = getTUYAID_URL + Tools.DEVICE_ID;
		return getCookieId(context,url);
	}
	
	public static int getNewTUYA_ID(String newTUYAID_URL,Context context,int account)
	{
		if(newTUYAID_URL == null || newTUYAID_URL.equals(""))
			return 0;
		
		SharedPreferences mySharePreference = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharePreference.edit();
		int num = mySharePreference.getInt("num", 1);
		if(account == 0)
		{
			num ++;
			Tools.identity = "cookie_" + String.valueOf(num);
			editor.putInt("num", num);
		}
		else
		{
			if(account <= num)
			{
				Tools.identity = "cookie_" + String.valueOf(account);
			}
		}
		editor.putString("Identity", Tools.identity);
		editor.commit();
		
		return getCookieId(context, newTUYAID_URL);
	}
	
	private static int getCookieId(Context context,String url)
	{
		HttpGet request = new HttpGet(url);
//		HttpGet request = new HttpGet(getTUYAID_URL);
		int TIME_OUT = 3000;
		
		HttpResponse httpResponse;
		try {
				
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
			
			httpResponse = httpclient.execute(request);
			String strResult;
            Log.i("statusline",httpResponse.getStatusLine().toString());
            
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				strResult = EntityUtils.toString(httpResponse.getEntity());
				if(httpResponse.getHeaders("Set-Cookie") != null)
				{
					List<Cookie> cookies = httpclient.getCookieStore().getCookies();
					Log.i("HttpUtil_getUpLoadQRurl",String.valueOf(cookies.size()));
					for(int i=0;i<cookies.size();i++)
					{
						Cookie mcookie = cookies.get(i);

						if("TUYA_ID".equals(cookies.get(i).getName()))
						{
							String cookie_str = null;
							if((cookie_str = cookies.get(i).getValue()) != null)
							{
								SharedPreferences mySharePreferences = context.getSharedPreferences("Cookie", Activity.MODE_PRIVATE); 
								SharedPreferences.Editor editor = mySharePreferences.edit();
								Log.i("HttpUtil_cookie",cookies.get(i).getValue());
								editor.putString(Tools.identity, "TUYA_ID=" + cookies.get(i).getValue());
								editor.commit();
								
								return SUCCESS;
							}
						}
					}
				}
			}		
			else
			{
				return FAIL;
			}
			
		}catch(ConnectionPoolTimeoutException e){
			return FAIL;
			
		}catch(ConnectTimeoutException e){
			return FAIL;
		}
		catch (SocketTimeoutException e) {
			return FAIL;
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return FAIL;
	}
	
	public static GetfromNetWorkOb getMyPicInfo (Context context,String myPicInfoUrl,int pagesize,int page,int times)
	{
		int TIME_OUT = 3000;
		ArrayList<MyPicInfo> picInfos = new ArrayList<MyPicInfo>();
		
		HttpGet request = new HttpGet(myPicInfoUrl + "?page_size=" + String.valueOf(pagesize) + "&page=" + String.valueOf(page));
		SharedPreferences mySharePreference = context.getSharedPreferences("Cookie",Activity.MODE_PRIVATE);
		String cookie_str = mySharePreference.getString(Tools.identity, "TUYA_ID=" + Tools.DEVICE_ID);
		request.setHeader("Cookie", cookie_str);
		
		String strResult = "";
		String toastText = null;
		
		HttpResponse httpResponse;
		try {
			
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT);
			
			httpResponse = httpclient.execute(request);
            Log.i("statusline",httpResponse.getStatusLine().toString());
            
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}		
			else
			{
				return new GetfromNetWorkOb(picInfos, context.getString(R.string.failTosuccess));
			}
			
		}catch(ConnectionPoolTimeoutException e){
			
			return new GetfromNetWorkOb(picInfos, context.getString(R.string.ConnectTimeout));
			
		}catch(ConnectTimeoutException e){
			
			return new GetfromNetWorkOb(picInfos, context.getString(R.string.ConnectTimeout));
		}catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(!strResult.equals(""))
		{
			try
			{
				JSONObject result = new JSONObject(strResult);
				Log.i("HttpUtil_getMyPicInfo",result.toString());
				String success = result.getString("success");
				if(success.equals("1"))
				{
					picInfos = new ArrayList<MyPicInfo>();
					
					JSONArray pic_datas = result.getJSONArray("pics");
					for(int i=0;i<pic_datas.length();i++)
					{
						JSONObject jso = pic_datas.getJSONObject(i);
						String  image_url_small = jso.getString("image_url_small");
						String url_small = jso.getString("url_small");
						String uniqueurl = jso.getString("html_url");
						String url_origin = jso.getString("url_origin");
						String image_url_origin = jso.getString("image_url_origin");
						picInfos.add(new MyPicInfo(image_url_small, url_small, uniqueurl,url_origin,image_url_origin));
					}
				}
				else
				{
					if(result.getString("err_msg").equals("login_failed") && times == 0)
					{
						times ++ ;
						int cookieResult = getTUYA_ID(getTUYAID_URL, context);
						if(cookieResult == 1)
						{
							return getMyPicInfo(context, myPicInfoUrl, pagesize, page, times);
						}
						else
						{
							return new GetfromNetWorkOb(picInfos, toastText);
						}
					}
					else
						return new GetfromNetWorkOb(picInfos, toastText);
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else
		{
            toastText = context.getString(R.string.failToget);
		}
		
		return new GetfromNetWorkOb(picInfos, toastText);
	}
	public static String getThemePic(String imgUrl)
	{
		return null;
	}
	public static String getUserName(String nameUrl)
	{
		return null;
	}
	
}
