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

/**��ҳ����ҳ,�̳���BaseFragment*/
public class ContentFragment extends BaseFragment
{
	private View view;

	/*
	 * ʹ��ViewUtils�����ע��ķ�ʽ����UI�ؼ��İ󶨣�
	 * �൱�ڵ��� View android.view.View.findViewById(int id)�ķ�������UI�ؼ��İ�
	 * */
	/**������CustomViewPager����*/
	@ViewInject(R.id.viewpager)
	private CustomViewPager viewPager;
	/**��ѡ��ť��*/
	@ViewInject(R.id.main_radio)
	private RadioGroup mainRadio;	
	
	/**��ҳ�е�ǰѡ�е�5��������ҳ,Ĭ��ֵΪ0*/
	private int currentContentPager=0;
	
//	private CustomViewPager viewPager;
//	private RadioGroup mainRadio;
	
	/**������CustomViewPager������ҳ��Page���󼯺�*/
	private List<BasePage> pageList=new ArrayList<BasePage>();
	
	@Override
	public View initView(LayoutInflater inflater) 
	{
		view=inflater.inflate(R.layout.frag_home2,null);
		
//		viewPager=(CustomViewPager) view.findViewById(R.id.viewpager);/*��ȡview�����еĿؼ�LazyViewPager����*/		
//		mainRadio=(RadioGroup)view.findViewById(R.id.main_radio);/*��ȡview�����еĿؼ�RadioGroup����*/
		
		ViewUtils.inject(this, view);//ע��view���¼�
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) 
	{
		/*�õ�BaseFragment�е�Context�����Ķ���ct��Ϊ����FunctionPageʱ����Ĳ���,
		 * BaseFragment�е�Context�����Ķ���ctΪpublic��������
		 * */
		pageList.add(new FunctionPage(ct));
		pageList.add(new NewsCenterPage(ct));
		pageList.add(new SmartServicePage(ct));
		pageList.add(new GovAffairsPage(ct));		
		pageList.add(new SettingPage(ct));
		
		
		/**
		 * ����һ��PagerAdapter������󣬴���Context�����Ķ���ct�ͼ��������pageList
		 * ct:��BaseFragment�õ���Context�����Ķ���
		 * */
		ContentAdapter contentPageAdapter=new ContentAdapter(ct,pageList);
		
		viewPager.setAdapter(contentPageAdapter);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {			
				currentContentPager=position;
				System.out.println("��ҳ����ҳ��ѡ�е���ҳIDΪ="+currentContentPager);
				
				/*��ѡ��ViewPager����Ӧ��Tabҳʱ�������ݳ�ʼ��*/
				BasePage page=pageList.get(position);
				page.initData();/*��CustomViewPager��ҳ�淢���л�ʱ��ʼ����ӦPageҳ������*/
							
				/*��ViewPager��λ�÷����ı�ʱRadioButton��λ��Ҳ�����ı�*/
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
		
		mainRadio.check(R.id.rb_function);/*��ʼ��RadioButton���ڱ�ѡ�е�״̬*/
		mainRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				switch(checkedId)
				{
				case R.id.rb_function:
					/*����ViewPager��ǰѡ����
					 * ViewPager.setCurrentItem(int item, boolean smoothScroll)
					 * �����е�smoothScrollΪtrueʱƽ���ػ�������һ��item,��Ϊfalse
					 * ʱ,������������һҳ
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
		
		this.pageList.get(0).initData();/*��ʼ����ҳ����*/
	}
	
	
	/*��дPagerAdapter*/
	class ContentAdapter extends PagerAdapter
	{
		private Context context;
		private List<BasePage> list;
		
		private String tag="ContentAdapter";
		
		public ContentAdapter(Context context, List<BasePage> list) {
			this.context = context;
			this.list = list;
		}
		
		/*���ؼ����೤��*/
		@Override
		public int getCount() {
			return list.size();
		}
				
		/*�ж�View��Object֮��Ķ�Ӧ��ϵ*/
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		
		/*�����Ӧλ���ϵ�View,
		 * container:View����������ʵ����Viewpager����
		 * position:��Ӧ��λ��
		 * */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("ContentAdapter="+position);
			Log.v(tag,"ContentAdapter="+position);
			
			((LazyViewPager)container).addView(list.get(position).getRootView(),0);
			return list.get(position).getRootView();
		}
		
		/*���ٶ�Ӧλ���ϵ�object */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			((LazyViewPager)container).removeView(list.get(position).getRootView());
		}
		
	}
	
	
	/**��ȡ��ҳ�е���ѡ��ҳ���ID*/
	public int getCurrentContentPager()
	{
		return this.currentContentPager;
	}
	
	
	/**��ȡ��ҳ����ҳ����ҳҳ��(��0��ҳ��)*/
	public FunctionPage getFunctionPage()
	{
		return (FunctionPage) this.pageList.get(0);
	}
	
	/**��ȡ��ҳ����ҳ����������ҳ��(��1��ҳ��)*/
	public NewsCenterPage getNewsCenterPage()
	{
		return (NewsCenterPage) this.pageList.get(1);
	}
	
	/**��ȡ��ҳ����ҳ���ǻ۷���ҳ��(��2��ҳ��)*/
	public SmartServicePage getSmartServicePage()
	{
		return (SmartServicePage) this.pageList.get(2);
	}
	
	/**��ȡ��ҳ����ҳ����������ҳ��(��3��ҳ��)*/
	public GovAffairsPage getGovAffairsPage()
	{
		return (GovAffairsPage) this.pageList.get(3);
	}
	
	/**��ȡ��ҳ����ҳ������ҳ��(��4��ҳ��)*/
	public SettingPage getSettingPage()
	{
		return (SettingPage) this.pageList.get(4);
	}	
}
