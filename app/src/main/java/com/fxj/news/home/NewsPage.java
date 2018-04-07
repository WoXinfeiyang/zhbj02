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

	/**“新闻”Page页的数据*/
	NewsCategory category;
	
	/**TabPageIndicator对象*/
	@ViewInject(R.id.indicator)
	TabPageIndicator indicator;
		
	/**Tab指示器对应内容页ItemNewsPage类集合*/
	ArrayList<NewsTabItemPage> newsTabItemPages=new ArrayList<NewsTabItemPage>();
	
	/**Tab指示器对应内容页中的ViewPager组件对象*/
	@ViewInject(R.id.pager)
	private ViewPager viewPager;
	
	/**Tab指示器对应内容页中的ViewPager组件对象的Adapter*/
	private NewsPageAdapter newsPageAdapter;
	
	/** "新闻中心"中的“新闻”Page页*/
	public NewsPage(Context ct,NewsCategory category) {
		super(ct);
		this.category=category;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		/**加载“新闻中心”中“新闻”界面布局对象*/
		View view=inflater.inflate(R.layout.frag_news,null);
		ViewUtils.inject(this, view);		
		return view;
		
//		/*TextView构造函数中传入来自BasePage的Context上下文对象ct,用于构造一个TextView对象*/
//		TextView textView=new TextView(ct);
//		textView.setText("我是新闻");
//		return textView;
	}

	@Override
	public void initData() {
		initIndicator();
	}
	
	/**Tab指示器当前被选中的子内容页*/
	private int currentTabIndex=0;
	
	/**初始化Tab指示器，实现功能:
	 * 1、初始化Tab指示器
	 * 2、将Tab指示器对象与ViewPager对象绑定
	 * 3、初始化Tab指示器对应选中的内容页数据
	 * */
	private void initIndicator() {
		this.newsTabItemPages.clear();	/*Tab指示器对应内容页ItemNewsPage类集合清空*/
		
		for(ChildNewsCate childCate:this.category.children)
		{
			this.newsTabItemPages.add(new NewsTabItemPage(ct,childCate.url));
//			System.out.println("Tab页面URL:"+childCate.url);
		}
		/*创建Tab指示器对应内容页ViewPager组件的Adapter*/
		this.newsPageAdapter=new NewsPageAdapter(ct, this.newsTabItemPages);
		this.viewPager.removeAllViews();
		/*给Tab指示器对应内容页ViewPager组件设置Adapter*/
		this.viewPager.setAdapter(this.newsPageAdapter);
		
		/*给TabPageIndicator指示器绑定监听器*/
		this.indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			
			/**当Tab指示器发生切换时回调此函数,实现功能：
			 * 1、Tab指示器发生切换时初始化Tab指示器对应页面的数据
			 * 2、给currentTabIndex赋新值
			 * */
			@Override
			public void onPageSelected(int arg0) {
				
				/*
				 *进行判断，如果子Pager滑动到最左侧那个Pager则可以滑动侧滑菜单;
				 *反之则禁止侧滑菜单可以滑动 
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
		
		
		/*初始化Tab指示器序号为0所对应内容页ItemNewsPage数据*/
		this.newsTabItemPages.get(0).initData();
		
		
		/*给Tab指示器设置ViewPager对象,即将ViewPager对象
		 * 与TabPageIndicator绑定*/
		this.indicator.setViewPager(this.viewPager);
		
		this.indicator.setCurrentItem(this.currentTabIndex);
		
		this.isLoadSuccess=true;
	}
	
	/**适配Tab指示器对应内容页ViewPager对象,用ItemNewsPage集合初始化Adapter*/
	class NewsPageAdapter extends PagerAdapter
	{
		/**Tab指示器对应内容页ItemNewsPage类集合 */
		private ArrayList<NewsTabItemPage> itemPages=new ArrayList<NewsTabItemPage>();
		
		
		/**
		 * 适配Tab指示器对应内容页ViewPager对象的Adapter,用ItemNewsPage集合初始化Adapter
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
		/**初始化Tab指示器标签字符串*/
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
