package com.fxj.news.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fxj.news.base.BasePage;
import com.fxj.news.bean.NewsCenterCategories;
import com.fxj.news.bean.NewsCenterCategories.NewsCategory;
import com.fxj.news.fragment.LeftMenuFragment;
import com.fxj.news.utils.GsonTools;
import com.fxj.news.utils.HMApi;
import com.fxj.news.utils.SharedPrefUtil;
import com.fxj.slidingtest01.MainActivity;
import com.fxj.slidingtest01.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**������������ҳ*/
public class NewsCenterPage extends BasePage{
	
	/**�������������ġ��໬�˵�ListView�б����ӦViewPager����*/
	private ArrayList<BasePage> pageList;
	
	/**���������ġ���֡���ֶ��� */
	@ViewInject(R.id.news_center_fl)
	private FrameLayout news_center_fl;
	
	/**������������ҳ*/
	public NewsCenterPage(Context ct) {
		super(ct);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view=inflater.inflate(R.layout.news_center_frame,null);
		ViewUtils.inject(this, view);
		setSlidingMenuEnable(true);/*�򿪲໬�˵�*/
		initTitleBar(view);
		return view;
		
//		/*TextView���캯���д�������BasePage��Context�����Ķ���ct,���ڹ���һ��TextView����*/
//		TextView textView=new TextView(ct);
//		textView.setText("������������");
//		return textView;
		
		
	}

	@Override
	public void initData() {
		/*��ʼ���������������ġ�ListView�б����Ӧ��ViewPager*/
		this.pageList=new ArrayList<BasePage>();
		
		/*��ȡ���ػ����е�����*/
		String cacheString=SharedPrefUtil.getString(ct,HMApi.NEWS_CENTER_CATEGORIES);
		
		/*���ӱ��ػ����ж�ȡ�����ݲ�Ϊ��ʱ���Ƚ���JSON����*/
		if(!TextUtils.isEmpty(cacheString))
		{
			ProcessData(cacheString);
		}
		
		getNewsCenterCategories();
	}
	
	private void getNewsCenterCategories()
	{
		HttpUtils http=new HttpUtils();
		
		http.send(HttpRequest.HttpMethod.GET,HMApi.NEWS_CENTER_CATEGORIES,
				new RequestCallBack<String>(){
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.d(responseInfo.result);
				Log.v("HttpRequestResponse",responseInfo.result);
				System.out.println("JSON����Ϊ="+responseInfo.result);
				
				/*���ӷ�������ȡ����String�������ݻ�������
				 * */
//				SharedPrefUtil.saveString(ct,HMApi.NEWS_CENTER_CATEGORIES,responseInfo.result);
				
				ProcessData(responseInfo.result);
			}}
		);
	}
	

	/**"��������"�໬�˵���ListView�˵��б�������*/
	public ArrayList<String> newsCenterMenuList = new ArrayList<String>();	
	/**���������ġ��໬�˵�ListView�б����ӦJSON���ݼ���*/
	public ArrayList<NewsCategory> categorieList;
	
	/*����Json����*/
	protected void ProcessData(String result)
	{
		/**���ӷ�������ȡ����JSON���ݽ������ض���*/
		NewsCenterCategories category=GsonTools.changeGsonToBean(result, NewsCenterCategories.class);
		
		System.out.println("��������-����ҳ��ӷ�������ȡ��������Ϊ="+category);
		
		if(category.retcode!=200)/*���������ݲ��ɹ�ʱֱ�ӷ���*/
		{
			return;
		}
		/*�������ݳɹ�*/
		else if(category.retcode==200)
		{
			this.categorieList=category.data;
			newsCenterMenuList.clear();
			for(NewsCategory cat:this.categorieList)
			{
				newsCenterMenuList.add(cat.title);			
			}
			
			((MainActivity)ct).getLeftMenuFragment().setLeftMenuData(newsCenterMenuList);
			
			/*
			 *==========================================
			 * ��categorieList����ȡ���������ġ�����ҳ���JSON���ݣ�
			 * �������ʼ����������ҳ��
			 *==============================================
			 * */
			
			this.pageList.clear();
			BasePage newsPage=new NewsPage(ct,this.categorieList.get(0));/*��categorieList����ȡ���ݸ����������ġ��еġ����š�Pageҳ�����JSON����*/
			BasePage topicsPage=new TopicsPage(ct,this.categorieList.get(1));
			BasePage picPage=new PicPage(ct,this.categorieList.get(2));
			BasePage interactPage=new InteractPage(ct,this.categorieList.get(3));
			BasePage votePage=new VotePage(ct,this.categorieList.get(4));
			
			/*�����������ġ��и���ҳ����ӵ��໬�˵�ListView�б����ӦViewPager����pageList��*/
			this.pageList.add(newsPage);
			this.pageList.add(topicsPage);
			this.pageList.add(picPage);
			this.pageList.add(interactPage);
			this.pageList.add(votePage);
			
			/*Ĭ����������ҳ��໬�˵��ж�Ӧ������ҳΪ��0������ҳ��*/
			setNewsCenterPageCurrentLeftMenuDetailPager(0);
		}		
	}
	
	/**������������ҳ��໬�˵��ж�Ӧ������ҳ��*/
	public void setNewsCenterPageCurrentLeftMenuDetailPager(int position)
	{
		/*��pageList�����л�ȡ��ǰ������ҳFragment��Ӧ��Page*/
		BasePage currentPage=this.pageList.get(position);
		this.news_center_fl.removeAllViews();
		this.news_center_fl.addView(currentPage.getRootView());
		
		titleTextView.setText(this.categorieList.get(position).title);
		/*��ʼ��ListView��ѡ���б����ӦPage������*/
		currentPage.initData();	
		
		LogUtils.d("��ǰ���������ġ�ѡ��ҳ��ID="+position);
		System.out.println("��ǰ���������ġ�ѡ��ҳ��ID="+position);
		
	}	
}
