package com.fxj.news.bean;

import java.util.ArrayList;


/**Tab子页签数据*/
public class NewsTabBean extends BaseBean {

	public NewsTabDetail data;
	
	
	@Override
	public String toString() {
		return "NewsTabBean [data=" + data + "]";
	}


	/**Tab子页签详情页数据*/
	public class NewsTabDetail
	{
		/**Tab子页签标题*/
		public String title;
		/**头条新闻数据集合*/
		public ArrayList<TopNews> topnews;
		/**列表新闻数据集合*/
		public ArrayList<News> news;
		public String countcommenturl;
		public String more;
		
		@Override
		public String toString() {
			return "NewsTabDetail [title=" + title + ", topnews=" + topnews
					+ ", news=" + news + ", countcommenturl=" + countcommenturl
					+ ", more=" + more + "]";
		}	
	}
	
	/**头条新闻数据对象*/
	public class TopNews
	{
		/**头条新闻id*/
		public String id;
		/**头条新闻标题*/
		public String title;
		/**头条新闻图片链接地址*/
		public String topimage;
		/**头条新闻内容链接地址*/
		public String url;
		/**头条新闻发布时间*/
		public String pubdate;
		/**头条新闻是否有评论标志位*/
		public boolean comment;
		/**头条新闻评论链接地址(目前不可用)*/
		public String commenturl;
		/**头条新闻类型*/
		public String type;
		/**头条新闻评论列表*/
		public String commentlist;
		
		@Override
		public String toString() {
			return "TopNews [id=" + id + ", title=" + title + ", topimage="
					+ topimage + ", url=" + url + ", pubdate=" + pubdate + "]";
		}		
	}
	
	
	/**列表新闻数据对象*/
	public class News
	{
		/**列表新闻id*/
		public String id;
		/**列表新闻标题*/
		public String title;
		/**列表新闻内容链接*/
		public String url;
		/**列表新闻图片链接*/
		public String listimage;
		/**列表新闻发布时间*/
		public String pubdate;
		/**列表新闻是否有评论标志位*/
		public boolean comment;
		/**列表新闻评论链接(目前不可用)*/
		public String commenturl;
		/**列表新闻类型*/
		public String type;
		/**列表新闻评论列表*/
		public String commentlist;
		
		@Override
		public String toString() {
			return "News [id=" + id + ", title=" + title + ", url=" + url
					+ ", pubdate=" + pubdate + "]";
		}	
	}
	
}
