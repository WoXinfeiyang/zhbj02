package com.fxj.news.home;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fxj.news.base.BasePage;
import com.fxj.news.bean.NewsCenterCategories.ChildNewsCate;
import com.fxj.news.bean.NewsCenterCategories.NewsCategory;
import com.fxj.slidingtest01.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

public class NewsPage extends BasePage {

	/**�����š�Pageҳ������*/
	NewsCategory category;
	
	/**TabPageIndicator����*/
	@ViewInject(R.id.indicator)
	TabPageIndicator indicator;
		
	/**Tabָʾ����Ӧ����ҳItemNewsPage�༯��*/
	ArrayList<NewsTabItemPage> newsTabItemPages=new ArrayList<NewsTabItemPage>();
	
	/**Tabָʾ����Ӧ����ҳ�е�ViewPager�������*/
	@ViewInject(R.id.pager)
	private ViewPager viewPager;
	
	/**Tabָʾ����Ӧ����ҳ�е�ViewPager��������Adapter*/
	private NewsPageAdapter newsPageAdapter;
	
	/** "��������"�еġ����š�Pageҳ*/
	public NewsPage(Context ct,NewsCategory category) {
		super(ct);
		this.category=category;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		/**���ء��������ġ��С����š����沼�ֶ���*/
		View view=inflater.inflate(R.layout.frag_news,null);
		ViewUtils.inject(this, view);		
		return view;
		
//		/*TextView���캯���д�������BasePage��Context�����Ķ���ct,���ڹ���һ��TextView����*/
//		TextView textView=new TextView(ct);
//		textView.setText("��������");
//		return textView;
	}

	@Override
	public void initData() {
		initIndicator();
	}
	
	/**Tabָʾ����ǰ��ѡ�е�������ҳ*/
	private int currentTabIndex=0;
	
	/**��ʼ��Tabָʾ����ʵ�ֹ���:
	 * 1����ʼ��Tabָʾ��
	 * 2����Tabָʾ��������ViewPager�����
	 * 3����ʼ��Tabָʾ����Ӧѡ�е�����ҳ����
	 * */
	private void initIndicator() {
		this.newsTabItemPages.clear();	/*Tabָʾ����Ӧ����ҳItemNewsPage�༯�����*/
		
		for(ChildNewsCate childCate:this.category.children)
		{
			this.newsTabItemPages.add(new NewsTabItemPage(ct,childCate.url));
//			System.out.println("Tabҳ��URL:"+childCate.url);
		}
		/*����Tabָʾ����Ӧ����ҳViewPager�����Adapter*/
		this.newsPageAdapter=new NewsPageAdapter(ct, this.newsTabItemPages);
		this.viewPager.removeAllViews();
		/*��Tabָʾ����Ӧ����ҳViewPager�������Adapter*/
		this.viewPager.setAdapter(this.newsPageAdapter);
		
		/*��TabPageIndicatorָʾ���󶨼�����*/
		this.indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			
			/**��Tabָʾ�������л�ʱ�ص��˺���,ʵ�ֹ��ܣ�
			 * 1��Tabָʾ�������л�ʱ��ʼ��Tabָʾ����Ӧҳ�������
			 * 2����currentTabIndex����ֵ
			 * */
			@Override
			public void onPageSelected(int arg0) {
				
				/*
				 *�����жϣ������Pager������������Ǹ�Pager����Ի����໬�˵�;
				 *��֮���ֹ�໬�˵����Ի��� 
				 * */
				if(arg0==0)
				{
					sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else{
					sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
				
				
				NewsTabItemPage itemPage=newsTabItemPages.get(arg0);
//				if(itemPage.isLoadSuccess==false)
//				{
					itemPage.initData();
//				}
				
				currentTabIndex=arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		
		/*��ʼ��Tabָʾ�����Ϊ0����Ӧ����ҳItemNewsPage����*/
		this.newsTabItemPages.get(0).initData();
		
		
		/*��Tabָʾ������ViewPager����,����ViewPager����
		 * ��TabPageIndicator��*/
		this.indicator.setViewPager(this.viewPager);
		
		this.indicator.setCurrentItem(this.currentTabIndex);
		
		this.isLoadSuccess=true;
	}
	
	/**����Tabָʾ����Ӧ����ҳViewPager����,��ItemNewsPage���ϳ�ʼ��Adapter*/
	class NewsPageAdapter extends PagerAdapter
	{
		/**Tabָʾ����Ӧ����ҳItemNewsPage�༯�� */
		private ArrayList<NewsTabItemPage> itemPages=new ArrayList<NewsTabItemPage>();
		
		
		/**
		 * ����Tabָʾ����Ӧ����ҳViewPager�����Adapter,��ItemNewsPage���ϳ�ʼ��Adapter
		 * */
		public NewsPageAdapter(Context context,ArrayList<NewsTabItemPage> itemNewsPages) {
			this.itemPages = itemNewsPages;
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(this.itemPages.get(position).getRootView());
			return this.itemPages.get(position).getRootView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if(position>=this.itemPages.size())
			{
				return;
			}
			((ViewPager)container).removeView(this.itemPages.get(position).getRootView());
		}
		/**��ʼ��Tabָʾ����ǩ�ַ���*/
		@Override
		public CharSequence getPageTitle(int position) {
			int size=category.children.size();
			return category.children.get(position%size).title;
		}

		@Override
		public int getCount() {
			return category.children.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
	}
	
	
}
