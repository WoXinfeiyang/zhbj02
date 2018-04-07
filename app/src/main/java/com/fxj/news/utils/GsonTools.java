package com.fxj.news.utils;

import com.google.gson.Gson;

/**
 * JSON���ݽ���������
 * */

public class GsonTools {

	public GsonTools() {
		
	}
	
	/*���������ϻ�ȡ����Json���ݽ�����ͨ�������
	 * gsonString-----�������ϻ�ȡ����JSON����
	 * clt------------��JSON���ݰ�ָ����T���н���
	 * */
	public static <T> T changeGsonToBean(String gsonString,Class <T> clt)
	{
		Gson gson=new Gson();
		
		T t=gson.fromJson(gsonString, clt);
		
		return t;		
	}
	
}
