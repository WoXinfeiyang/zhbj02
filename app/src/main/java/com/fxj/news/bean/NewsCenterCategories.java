package com.fxj.news.bean;

import java.util.ArrayList;

public class NewsCenterCategories extends BaseBean {
	/**�໬�˵����ݼ���*/
	public ArrayList<NewsCategory> data;
	public int[] extend;
		
	@Override
	public String toString() {
		return "NewsCenterCategories [data=" + data + "]";
	}

	/**�໬�˵����ݶ���*/
	public class NewsCategory
	{
		public String id;
		public String title="";
		public int type;
		public String url="";
		public String url1="";
		public String dayurl;
		public String excurl;
		public String weekurl;
		public ArrayList<ChildNewsCate> children=new ArrayList<ChildNewsCate>();
		
		@Override
		public String toString() {
			return "NewsCategory [id=" + id + ", title=" + title + ", type="
					+ type + ", url=" + url + ", url1=" + url1 + ", dayurl="
					+ dayurl + ", excurl=" + excurl + ", weekurl=" + weekurl
					+ ", children=" + children + "]";
		}		
	
		
	}
	
	/**
	 * �����š�ListView�б����е�����
	 * */
	public class ChildNewsCate
	{
		public String id;
		public String title="";
		public int type;
		public String url="";
		
		
		@Override
		public String toString() {
			return "ChildNewsCate [id=" + id + ", title=" + title + ", type="
					+ type + ", url=" + url + "]";
		}
	}
}
