package com.fxj.news.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * ����SharedPreferences������
 * */
public class SharedPrefUtil {
	
	private static SharedPreferences sp;
	private static String SP_NAME="Config";

	/**
	 * ��������:saveString(Context ct,String key,String value)
	 * ����˵���������ַ���
	 * ��������:ct----Context��������,������,
	 *         key----String��������,��ֵ����
	 *         value----String��������,��ֵ
	 * ����ֵ����
	 * */
	public static void saveString(Context ct,String key,String value)
	{
		if(sp==null)/*��SharedPreferences����spΪ��ʱ�����ö���*/
		{
			sp = ct.getSharedPreferences(SP_NAME,ct.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();/*д�����ݲ��ύ����*/
	}
	
	/**
	 * ��������:getString(Context ct,String key)
	 * ����˵��:��ȡ�ַ���
	 * */
	public static String getString(Context ct,String key)
	{
		if(sp==null)/*��SharedPreferences����spΪ��ʱ�����ö���*/
		{
			sp = ct.getSharedPreferences(SP_NAME,ct.MODE_PRIVATE);
		}
		return sp.getString(key,"");
	}
}
