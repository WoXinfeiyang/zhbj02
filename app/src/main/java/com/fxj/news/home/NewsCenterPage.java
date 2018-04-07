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

/**新闻中心内容页*/
public class NewsCenterPage extends BasePage{
	
	/**整个“新闻中心”侧滑菜单ListView列表项对应ViewPager集合*/
	private ArrayList<BasePage> pageList;
	
	/**“新闻中心”的帧布局对象 */
	@ViewInject(R.id.news_center_fl)
	private FrameLayout news_center_fl;
	
	/**新闻中心内容页*/
	public NewsCenterPage(Context ct) {
		super(ct);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view=inflater.inflate(R.layout.news_center_frame,null);
		ViewUtils.inject(this, view);
		setSlidingMenuEnable(true);/*打开侧滑菜单*/
		initTitleBar(view);
		return view;
		
//		/*TextView构造函数中传入来自BasePage的Context上下文对象ct,用于构造一个TextView对象*/
//		TextView textView=new TextView(ct);
//		textView.setText("我是新闻中心");
//		return textView;
		
		
	}

	@Override
	public void initData() {
		/*初始化整个“新闻中心”ListView列表项对应的ViewPager*/
		this.pageList=new ArrayList<BasePage>();
		
		/*读取本地缓存中的数据*/
		String cacheString=SharedPrefUtil.getString(ct,HMApi.NEWS_CENTER_CATEGORIES);
		
		/*当从本地缓存中读取的数据不为空时首先解析JSON数据*/
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
				System.out.println("JSON数据为="+responseInfo.result);
				
				/*将从服务器获取到的String类型数据缓存下来
				 * */
//				SharedPrefUtil.saveString(ct,HMApi.NEWS_CENTER_CATEGORIES,responseInfo.result);
				
				ProcessData(responseInfo.result);
			}}
		);
	}
	

	/**"新闻中心"侧滑菜单中ListView菜单列表项名称*/
	public ArrayList<String> newsCenterMenuList = new ArrayList<String>();	
	/**“新闻中心”侧滑菜单ListView列表项对应JSON数据集合*/
	public ArrayList<NewsCategory> categorieList;
	
	/*解析Json数据*/
	protected void ProcessData(String result)
	{
		/**将从服务器获取到的JSON数据解析成特定类*/
		NewsCenterCategories category=GsonTools.changeGsonToBean(result, NewsCenterCategories.class);
		
		System.out.println("新闻中心-新闻页面从服务器获取到的数据为="+category);
		
		if(category.retcode!=200)/*当请求数据不成功时直接返回*/
		{
			return;
		}
		/*请求数据成功*/
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
			 * 从categorieList中提取“新闻中心”各个页面的JSON数据，
			 * 并将其初始化到个各个页面
			 *==============================================
			 * */
			
			this.pageList.clear();
			BasePage newsPage=new NewsPage(ct,this.categorieList.get(0));/*从categorieList中提取数据给“新闻中心”中的“新闻”Page页面加载JSON数据*/
			BasePage topicsPage=new TopicsPage(ct,this.categorieList.get(1));
			BasePage picPage=new PicPage(ct,this.categorieList.get(2));
			BasePage interactPage=new InteractPage(ct,this.categorieList.get(3));
			BasePage votePage=new VotePage(ct,this.categorieList.get(4));
			
			/*将“新闻中心”中各个页面添加到侧滑菜单ListView列表项对应ViewPager集合pageList中*/
			this.pageList.add(newsPage);
			this.pageList.add(topicsPage);
			this.pageList.add(picPage);
			this.pageList.add(interactPage);
			this.pageList.add(votePage);
			
			/*默认新闻中心页面侧滑菜单中对应的详情页为第0个详情页面*/
			setNewsCenterPageCurrentLeftMenuDetailPager(0);
		}		
	}
	
	/**设置新闻中心页面侧滑菜单中对应的详情页面*/
	public void setNewsCenterPageCurrentLeftMenuDetailPager(int position)
	{
		/*从pageList集合中获取当前的内容页Fragment对应的Page*/
		BasePage currentPage=this.pageList.get(position);
		this.news_center_fl.removeAllViews();
		this.news_center_fl.addView(currentPage.getRootView());
		
		titleTextView.setText(this.categorieList.get(position).title);
		/*初始化ListView被选中列表项对应Page的数据*/
		currentPage.initData();	
		
		LogUtils.d("当前”新闻中心“选中页面ID="+position);
		System.out.println("当前”新闻中心“选中页面ID="+position);
		
	}	
}
