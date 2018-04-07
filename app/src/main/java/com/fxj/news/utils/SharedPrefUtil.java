package com.fxj.news.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 操作SharedPreferences工具类
 * */
public class SharedPrefUtil {
	
	private static SharedPreferences sp;
	private static String SP_NAME="Config";

	/**
	 * 函数名称:saveString(Context ct,String key,String value)
	 * 函数说明：保存字符串
	 * 函数参数:ct----Context类型数据,上下文,
	 *         key----String类型数据,键值名称
	 *         value----String类型数据,键值
	 * 返回值：无
	 * */
	public static void saveString(Context ct,String key,String value)
	{
		if(sp==null)/*当SharedPreferences对象sp为空时创建该对象*/
		{
			sp = ct.getSharedPreferences(SP_NAME,ct.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();/*写入数据并提交数据*/
	}
	
	/**
	 * 函数名称:getString(Context ct,String key)
	 * 函数说明:读取字符串
	 * */
	public static String getString(Context ct,String key)
	{
		if(sp==null)/*当SharedPreferences对象sp为空时创建该对象*/
		{
			sp = ct.getSharedPreferences(SP_NAME,ct.MODE_PRIVATE);
		}
		return sp.getString(key,"");
	}
}
