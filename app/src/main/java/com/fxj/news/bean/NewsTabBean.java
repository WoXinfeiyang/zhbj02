package com.fxj.news.bean;

import java.util.ArrayList;


/**Tab��ҳǩ����*/
public class NewsTabBean extends BaseBean {

	public NewsTabDetail data;
	
	
	@Override
	public String toString() {
		return "NewsTabBean [data=" + data + "]";
	}


	/**Tab��ҳǩ����ҳ����*/
	public class NewsTabDetail
	{
		/**Tab��ҳǩ����*/
		public String title;
		/**ͷ���������ݼ���*/
		public ArrayList<TopNews> topnews;
		/**�б��������ݼ���*/
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
	
	/**ͷ���������ݶ���*/
	public class TopNews
	{
		/**ͷ������id*/
		public String id;
		/**ͷ�����ű���*/
		public String title;
		/**ͷ������ͼƬ���ӵ�ַ*/
		public String topimage;
		/**ͷ�������������ӵ�ַ*/
		public String url;
		/**ͷ�����ŷ���ʱ��*/
		public String pubdate;
		/**ͷ�������Ƿ������۱�־λ*/
		public boolean comment;
		/**ͷ�������������ӵ�ַ(Ŀǰ������)*/
		public String commenturl;
		/**ͷ����������*/
		public String type;
		/**ͷ�����������б�*/
		public String commentlist;
		
		@Override
		public String toString() {
			return "TopNews [id=" + id + ", title=" + title + ", topimage="
					+ topimage + ", url=" + url + ", pubdate=" + pubdate + "]";
		}		
	}
	
	
	/**�б��������ݶ���*/
	public class News
	{
		/**�б�����id*/
		public String id;
		/**�б����ű���*/
		public String title;
		/**�б�������������*/
		public String url;
		/**�б�����ͼƬ����*/
		public String listimage;
		/**�б����ŷ���ʱ��*/
		public String pubdate;
		/**�б������Ƿ������۱�־λ*/
		public boolean comment;
		/**�б�������������(Ŀǰ������)*/
		public String commenturl;
		/**�б���������*/
		public String type;
		/**�б����������б�*/
		public String commentlist;
		
		@Override
		public String toString() {
			return "News [id=" + id + ", title=" + title + ", url=" + url
					+ ", pubdate=" + pubdate + "]";
		}	
	}
	
}
