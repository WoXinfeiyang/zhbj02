package com.fxj.news.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fxj.news.base.BaseFragment;
import com.fxj.news.base.BasePage;
import com.fxj.news.home.FunctionPage;
import com.fxj.news.home.GovAffairsPage;
import com.fxj.news.home.NewsCenterPage;
import com.fxj.news.home.SettingPage;
import com.fxj.news.home.SmartServicePage;
import com.fxj.news.view.CustomViewPager;
import com.fxj.news.view.LazyViewPager;
import com.fxj.news.view.LazyViewPager.OnPageChangeListener;
import com.fxj.slidingtest01.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**主页内容页,继承自BaseFragment*/
public class ContentFragment extends BaseFragment
{
	private View view;

	/*
	 * 使用ViewUtils类采用注解的方式进行UI控件的绑定，
	 * 相当于调用 View android.view.View.findViewById(int id)的方法进行UI控件的绑定
	 * */
	/**主界面CustomViewPager对象*/
	@ViewInject(R.id.viewpager)
	private CustomViewPager viewPager;
	/**单选按钮组*/
	@ViewInject(R.id.main_radio)
	private RadioGroup mainRadio;	
	
	/**主页中当前选中的5个内容子页,默认值为0*/
	private int currentContentPager=0;
	
//	private CustomViewPager viewPager;
//	private RadioGroup mainRadio;
	
	/**主界面CustomViewPager所容纳页面Page对象集合*/
	private List<BasePage> pageList=new ArrayList<BasePage>();
	
	@Override
	public View initView(LayoutInflater inflater) 
	{
		view=inflater.inflate(R.layout.frag_home2,null);
		
//		viewPager=(CustomViewPager) view.findViewById(R.id.viewpager);/*获取view对象中的控件LazyViewPager对象*/		
//		mainRadio=(RadioGroup)view.findViewById(R.id.main_radio);/*获取view对象中的控件RadioGroup对象*/
		
		ViewUtils.inject(this, view);//注入view和事件
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) 
	{
		/*拿到BaseFragment中的Context上下文对象ct作为创建FunctionPage时传入的参数,
		 * BaseFragment中的Context上下文对象ct为public公有属性
		 * */
		pageList.add(new FunctionPage(ct));
		pageList.add(new NewsCenterPage(ct));
		pageList.add(new SmartServicePage(ct));
		pageList.add(new GovAffairsPage(ct));		
		pageList.add(new SettingPage(ct));
		
		
		/**
		 * 定义一个PagerAdapter子类对象，传入Context上下文对象ct和集合类对象pageList
		 * ct:从BaseFragment拿到的Context上下文对象
		 * */
		ContentAdapter contentPageAdapter=new ContentAdapter(ct,pageList);
		
		viewPager.setAdapter(contentPageAdapter);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {			
				currentContentPager=position;
				System.out.println("主页内容页中选中的子页ID为="+currentContentPager);
				
				/*当选中ViewPager中相应的Tab页时进行数据初始化*/
				BasePage page=pageList.get(position);
				page.initData();/*当CustomViewPager中页面发生切换时初始化对应Page页的数据*/
							
				/*当ViewPager的位置发生改变时RadioButton的位置也发生改变*/
				switch(position)
				{
				case 0:
					mainRadio.check(R.id.rb_function);
					break;
				case 1:
					mainRadio.check(R.id.rb_news_center);
					break;
				case 2:
					mainRadio.check(R.id.rb_smart_service);
					break;
				case 3:
					mainRadio.check(R.id.rb_gov_affairs);
					break;
				case 4:
					mainRadio.check(R.id.rb_setting);
					break;
				default:
					mainRadio.check(R.id.rb_function);
					break;					
				}				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		
		mainRadio.check(R.id.rb_function);/*初始化RadioButton处于被选中的状态*/
		mainRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				switch(checkedId)
				{
				case R.id.rb_function:
					/*设置ViewPager当前选中项
					 * ViewPager.setCurrentItem(int item, boolean smoothScroll)
					 * 方法中当smoothScroll为true时平滑地滑动到下一个item,当为false
					 * 时,立即滑动到下一页
					 * */
					viewPager.setCurrentItem(0,false);
					checkedId=0;
					break;
				case R.id.rb_news_center:
					viewPager.setCurrentItem(1,false);
					checkedId=1;
					break;
				case R.id.rb_smart_service:
					viewPager.setCurrentItem(2,false);
					checkedId=2;
					break;
				case R.id.rb_gov_affairs:
					viewPager.setCurrentItem(3,false);
					checkedId=3;
					break;
				case R.id.rb_setting:
					viewPager.setCurrentItem(4,false);
					checkedId=4;
					break;

				}				
			}
		});
		
		this.pageList.get(0).initData();/*初始化首页数据*/
	}
	
	
	/*重写PagerAdapter*/
	class ContentAdapter extends PagerAdapter
	{
		private Context context;
		private List<BasePage> list;
		
		private String tag="ContentAdapter";
		
		public ContentAdapter(Context context, List<BasePage> list) {
			this.context = context;
			this.list = list;
		}
		
		/*返回集合类长度*/
		@Override
		public int getCount() {
			return list.size();
		}
				
		/*判断View和Object之间的对应关系*/
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		
		/*获得相应位置上的View,
		 * container:View的容器，其实就是Viewpager自身
		 * position:相应的位置
		 * */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("ContentAdapter="+position);
			Log.v(tag,"ContentAdapter="+position);
			
			((LazyViewPager)container).addView(list.get(position).getRootView(),0);
			return list.get(position).getRootView();
		}
		
		/*销毁对应位置上的object */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			((LazyViewPager)container).removeView(list.get(position).getRootView());
		}
		
	}
	
	
	/**获取主页中当期选中页面的ID*/
	public int getCurrentContentPager()
	{
		return this.currentContentPager;
	}
	
	
	/**获取主页内容页中首页页面(第0个页面)*/
	public FunctionPage getFunctionPage()
	{
		return (FunctionPage) this.pageList.get(0);
	}
	
	/**获取主页内容页中新闻中心页面(第1个页面)*/
	public NewsCenterPage getNewsCenterPage()
	{
		return (NewsCenterPage) this.pageList.get(1);
	}
	
	/**获取主页内容页中智慧服务页面(第2个页面)*/
	public SmartServicePage getSmartServicePage()
	{
		return (SmartServicePage) this.pageList.get(2);
	}
	
	/**获取主页内容页中政务中心页面(第3个页面)*/
	public GovAffairsPage getGovAffairsPage()
	{
		return (GovAffairsPage) this.pageList.get(3);
	}
	
	/**获取主页内容页中设置页面(第4个页面)*/
	public SettingPage getSettingPage()
	{
		return (SettingPage) this.pageList.get(4);
	}	
}
