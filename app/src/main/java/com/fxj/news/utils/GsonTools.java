package com.fxj.news.utils;

import com.google.gson.Gson;

/**
 * JSON数据解析工具类
 * */

public class GsonTools {

	public GsonTools() {
		
	}
	
	/*将从网络上获取到的Json数据解析成通用类对象，
	 * gsonString-----从网络上获取到的JSON数据
	 * clt------------将JSON数据按指定类T进行解析
	 * */
	public static <T> T changeGsonToBean(String gsonString,Class <T> clt)
	{
		Gson gson=new Gson();
		
		T t=gson.fromJson(gsonString, clt);
		
		return t;		
	}
	
}
