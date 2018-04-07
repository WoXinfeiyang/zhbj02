package com.fxj.news.home;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.fxj.news.base.BasePage;
import com.fxj.news.bean.NewsTabBean;
import com.fxj.news.bean.NewsTabBean.News;
import com.fxj.news.bean.NewsTabBean.TopNews;
import com.fxj.news.utils.GsonTools;
import com.fxj.news.utils.SharedPrefUtil;
import com.fxj.news.view.RefreshListView;
import com.fxj.news.view.RefreshListView.OnRefreshListener;
import com.fxj.news.view.RollViewPager;
import com.fxj.slidingtest01.NewsDetailActivity;
import com.fxj.slidingtest01.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/**新闻Tab指示器子内容页*/
public class NewsTabItemPage extends BasePage {


	/**Tab指示器子内容页JSON数据的URL*/
	private String childNewsCatUrl;
	
	/**Tab子页签数据数据*/
	private NewsTabBean mNewsTabDetailData;
	
	
	/*头条新闻相关成员变量(属性)*/
	/**RollViewPager对象*/
	@ViewInject(R.id.vp_topNews)
	private RollViewPager mViewPager;
	/**头条新闻标题*/
	@ViewInject(R.id.tv_topNewsTitle)
	private TextView mTopNewsTitle;
	/**头条新闻位置指示器*/
	@ViewInject(R.id.topNewsIndicator)
	private CirclePageIndicator mTopNewsIndicatior;	
	/**头条新闻数据集合*/
	private ArrayList<TopNews> mTopNewsList; 
	
	
	/*列表新闻相关成员变量(属性)*/
	/**列表新闻数据集合*/
	private ArrayList<News> mNewsList;
	/**RefreshListView对象*/
	@ViewInject(R.id.refreshListView)
	private RefreshListView refreshListView;
	/**适配RefreshListView的Adapter*/
	private NewsAdapter mNewsAdapter;
	
	
	/**更多列表新闻链接*/
	private String mMoreUrl;
	
	/**
	 * Tab指示器子内容页构造函数
	 * @param ct------上下文,childNewsCatUrl-----Tab指示器子内容页JSON数据的URL
	 */
	public NewsTabItemPage(Context ct,String childNewsCatUrl) {
		super(ct);
		this.childNewsCatUrl=childNewsCatUrl;
	}	
	
	@Override
	public View initView(LayoutInflater inflater) {	
		
		View view=inflater.inflate(R.layout.layout_tab_item_pager,null);	
		View headView=inflater.inflate(R.layout.layout_header_topnews,null);
		
		ViewUtils.inject(this,view);
		ViewUtils.inject(this,headView);
		/*将头条新闻以头布局的形式加给RefreshListView */
		this.refreshListView.addHeaderView(headView);
		
		this.refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("当前点击ListView的位置为:"+position);
				
				/*将已读新闻通过新闻ID记录到本地*/
				String readIds=SharedPrefUtil.getString(ct,"readIds");
				String currentReadId=mNewsList.get(position).id;
				
				boolean readState=readIds.contains(currentReadId);
				System.out.println("当前新闻ID="+currentReadId+",是否已读:"+readState);
				
				if(!readState/*readIds.contains(currentReadId)*/){
					readIds=readIds+currentReadId+",";
					SharedPrefUtil.saveString(ct,"readIds",readIds);
					changeReadState(view);
				}
				
//				changeReadState(view);
				
				/*跳转到新闻详情页面*/
				Intent intent=new Intent();
				intent.setClass(ct,NewsDetailActivity.class);
				intent.putExtra("url",mNewsList.get(position).url);
				ct.startActivity(intent);
			}
		});
		
		this.refreshListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDataFromService();
			}
			
			@Override
			public void onLoadMore() {
				if(mMoreUrl!=null)/*合法性检测*/
				{
					getMoreDataFromService();
				}else{
					Toast.makeText(ct,"最后一页了……",Toast.LENGTH_LONG).show();
					refreshListView.onRefreshComplete(false);
				}
			}
		});
		
		return view;
	}
	
	/**改变已读新闻的颜色*/
	protected void changeReadState(View view) {
		TextView newsTitle=(TextView) view.findViewById(R.id.tv_itemNewsTitle);
		newsTitle.setTextColor(Color.GRAY);
	}

	@Override
	public void initData() {
		/**从本地拿到缓存数据*/
		String cache=SharedPrefUtil.getString(ct, childNewsCatUrl);
		
		if(!TextUtils.isEmpty(cache))/*当本地缓存数据不为空时,直接解析数据*/
		{
			parseData(cache,false);
		}
		getDataFromService();
		
	}
	/**从服务器获取第一页数据*/
	private void getDataFromService() {
		
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.GET,this.childNewsCatUrl,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				parseData(result, false);
				refreshListView.onRefreshComplete(true);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(ct, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				refreshListView.onRefreshComplete(false);
			}
		});
	}

	/**从服务器获取第二页数据,即获取更多数据*/
	private void getMoreDataFromService() {
		
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.GET,this.mMoreUrl,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				parseData(result, false);
				refreshListView.onRefreshComplete(true);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(ct, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				refreshListView.onRefreshComplete(false);
			}
		});
	}
	
	/**
	 * 解析从服务器拿到的数据
	 * @param result---从服务器拿到的数据,
	 * @param isMore--是否加载更多数据,true为加载更多数据,false为不加载更多数据
	 * */
	private void parseData(String result, boolean isMore) {
		
		this.mNewsTabDetailData=GsonTools.changeGsonToBean(result,NewsTabBean.class);
		
		/*获取更多列表新闻链接*/
		if(!TextUtils.isEmpty(mNewsTabDetailData.data.more))
		{
			this.mMoreUrl=mNewsTabDetailData.data.more;
		}else{
			this.mMoreUrl=null;
		}
				
		if(!isMore)/*当不加载更多数据,即只加载第一页数据*/
		{
			/*获取头条新闻数据*/
			this.mTopNewsList=this.mNewsTabDetailData.data.topnews;
			/*获取列表新闻数据*/
			this.mNewsList=this.mNewsTabDetailData.data.news;
			
			System.out.println("获取的头条新闻数据为="+this.mTopNewsList);
			
			System.out.println("获取的列表新闻数据为="+this.mNewsList);
			
			/*加载头条新闻数据*/
			if(this.mTopNewsList!=null)
			{
				this.mViewPager.setAdapter(new TopNewsAdapter());
				this.mTopNewsIndicatior.setViewPager(mViewPager);
				this.mTopNewsIndicatior.setSnap(true);/*支持快照*/
				this.mTopNewsIndicatior.onPageSelected(0);/*让指示器重新定位到第一个点*/
				this.mTopNewsTitle.setText(this.mTopNewsList.get(0).title);
			}
			/*加载列表新闻数据*/
			if(this.mNewsList!=null)
			{
				mNewsAdapter = new NewsAdapter();
				refreshListView.setAdapter(mNewsAdapter);
			}			
		}
		else/*当加载更多数据时,即加载下一页数据时*/
		{
			ArrayList<News> mNews=mNewsTabDetailData.data.news;
			this.mNewsList.addAll(mNews);
			this.mNewsAdapter.notifyDataSetChanged();
		}		
	}

	
	private class TopNewsAdapter extends PagerAdapter
	{
		BitmapUtils bitmapUtils;
				
		public TopNewsAdapter() {
			bitmapUtils=new BitmapUtils(ct);/*创建Bitmap工具类*/
			/*设置下载失败后默认的图片*/
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			int size=0;
			if(mTopNewsList!=null)
			{
				size=mTopNewsList.size();
			}else{
				size=0;
			}
			
			return size;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image=new ImageView(ct);
			image.setScaleType(ScaleType.FIT_XY);/*基于控件大小填充图片*/
			/**单个头条新闻数据*/
			TopNews topNewsData=mTopNewsList.get(position);/*获取单个头条新闻数据*/
			bitmapUtils.display(image,topNewsData.topimage);/*加载网络图片*/
			mTopNewsTitle.setText(topNewsData.title);
			
			container.addView(image);
			
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}		
	}

	
	private class NewsAdapter extends BaseAdapter
	{
		BitmapUtils bitmapUtils;
		
		
		public NewsAdapter() {
			bitmapUtils=new BitmapUtils(ct);
			bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}


		@Override
		public int getCount() {
			return mNewsList.size();
		}


		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mNewsList.get(position);
		}


		@Override
		public long getItemId(int position) {
			return position;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null)
			{
				convertView=View.inflate(ct, R.layout.list_news_item,null);
				holder=new ViewHolder();
				holder.itemNewsPic=(ImageView) convertView.findViewById(R.id.iv_itemNewsPic);
				holder.itemNewsTitle=(TextView) convertView.findViewById(R.id.tv_itemNewsTitle);
				holder.itemNewsDate=(TextView) convertView.findViewById(R.id.tv_itemNewsDate);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			News item=(News)getItem(position);
			
			holder.itemNewsTitle.setText(item.title);
			holder.itemNewsDate.setText(item.pubdate);
			bitmapUtils.display(holder.itemNewsPic,item.listimage);
			
			/*读取已读新闻ID,根据已读新闻ID设置列表新闻标题*/
			String readIds=SharedPrefUtil.getString(ct,"readIds");
			if(readIds.contains(mNewsList.get(position).id)){
				holder.itemNewsTitle.setTextColor(Color.GRAY);
			}else{
				holder.itemNewsTitle.setTextColor(Color.BLACK);
			}
			
			
			return convertView;
		}
		
		/**定义一个内部类*/
		private class ViewHolder
		{
			private ImageView itemNewsPic;
			private TextView itemNewsTitle;
			private TextView itemNewsDate;
		}		
	}
}
