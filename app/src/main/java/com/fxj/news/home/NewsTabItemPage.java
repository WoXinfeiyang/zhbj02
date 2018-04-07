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

/**����Tabָʾ��������ҳ*/
public class NewsTabItemPage extends BasePage {


	/**Tabָʾ��������ҳJSON���ݵ�URL*/
	private String childNewsCatUrl;
	
	/**Tab��ҳǩ��������*/
	private NewsTabBean mNewsTabDetailData;
	
	
	/*ͷ��������س�Ա����(����)*/
	/**RollViewPager����*/
	@ViewInject(R.id.vp_topNews)
	private RollViewPager mViewPager;
	/**ͷ�����ű���*/
	@ViewInject(R.id.tv_topNewsTitle)
	private TextView mTopNewsTitle;
	/**ͷ������λ��ָʾ��*/
	@ViewInject(R.id.topNewsIndicator)
	private CirclePageIndicator mTopNewsIndicatior;	
	/**ͷ���������ݼ���*/
	private ArrayList<TopNews> mTopNewsList; 
	
	
	/*�б�������س�Ա����(����)*/
	/**�б��������ݼ���*/
	private ArrayList<News> mNewsList;
	/**RefreshListView����*/
	@ViewInject(R.id.refreshListView)
	private RefreshListView refreshListView;
	/**����RefreshListView��Adapter*/
	private NewsAdapter mNewsAdapter;
	
	
	/**�����б���������*/
	private String mMoreUrl;
	
	/**
	 * Tabָʾ��������ҳ���캯��
	 * @param ct------������,childNewsCatUrl-----Tabָʾ��������ҳJSON���ݵ�URL
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
		/*��ͷ��������ͷ���ֵ���ʽ�Ӹ�RefreshListView */
		this.refreshListView.addHeaderView(headView);
		
		this.refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("��ǰ���ListView��λ��Ϊ:"+position);
				
				/*���Ѷ�����ͨ������ID��¼������*/
				String readIds=SharedPrefUtil.getString(ct,"readIds");
				String currentReadId=mNewsList.get(position).id;
				
				boolean readState=readIds.contains(currentReadId);
				System.out.println("��ǰ����ID="+currentReadId+",�Ƿ��Ѷ�:"+readState);
				
				if(!readState/*readIds.contains(currentReadId)*/){
					readIds=readIds+currentReadId+",";
					SharedPrefUtil.saveString(ct,"readIds",readIds);
					changeReadState(view);
				}
				
//				changeReadState(view);
				
				/*��ת����������ҳ��*/
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
				if(mMoreUrl!=null)/*�Ϸ��Լ��*/
				{
					getMoreDataFromService();
				}else{
					Toast.makeText(ct,"���һҳ�ˡ���",Toast.LENGTH_LONG).show();
					refreshListView.onRefreshComplete(false);
				}
			}
		});
		
		return view;
	}
	
	/**�ı��Ѷ����ŵ���ɫ*/
	protected void changeReadState(View view) {
		TextView newsTitle=(TextView) view.findViewById(R.id.tv_itemNewsTitle);
		newsTitle.setTextColor(Color.GRAY);
	}

	@Override
	public void initData() {
		/**�ӱ����õ���������*/
		String cache=SharedPrefUtil.getString(ct, childNewsCatUrl);
		
		if(!TextUtils.isEmpty(cache))/*�����ػ������ݲ�Ϊ��ʱ,ֱ�ӽ�������*/
		{
			parseData(cache,false);
		}
		getDataFromService();
		
	}
	/**�ӷ�������ȡ��һҳ����*/
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

	/**�ӷ�������ȡ�ڶ�ҳ����,����ȡ��������*/
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
	 * �����ӷ������õ�������
	 * @param result---�ӷ������õ�������,
	 * @param isMore--�Ƿ���ظ�������,trueΪ���ظ�������,falseΪ�����ظ�������
	 * */
	private void parseData(String result, boolean isMore) {
		
		this.mNewsTabDetailData=GsonTools.changeGsonToBean(result,NewsTabBean.class);
		
		/*��ȡ�����б���������*/
		if(!TextUtils.isEmpty(mNewsTabDetailData.data.more))
		{
			this.mMoreUrl=mNewsTabDetailData.data.more;
		}else{
			this.mMoreUrl=null;
		}
				
		if(!isMore)/*�������ظ�������,��ֻ���ص�һҳ����*/
		{
			/*��ȡͷ����������*/
			this.mTopNewsList=this.mNewsTabDetailData.data.topnews;
			/*��ȡ�б���������*/
			this.mNewsList=this.mNewsTabDetailData.data.news;
			
			System.out.println("��ȡ��ͷ����������Ϊ="+this.mTopNewsList);
			
			System.out.println("��ȡ���б���������Ϊ="+this.mNewsList);
			
			/*����ͷ����������*/
			if(this.mTopNewsList!=null)
			{
				this.mViewPager.setAdapter(new TopNewsAdapter());
				this.mTopNewsIndicatior.setViewPager(mViewPager);
				this.mTopNewsIndicatior.setSnap(true);/*֧�ֿ���*/
				this.mTopNewsIndicatior.onPageSelected(0);/*��ָʾ�����¶�λ����һ����*/
				this.mTopNewsTitle.setText(this.mTopNewsList.get(0).title);
			}
			/*�����б���������*/
			if(this.mNewsList!=null)
			{
				mNewsAdapter = new NewsAdapter();
				refreshListView.setAdapter(mNewsAdapter);
			}			
		}
		else/*�����ظ�������ʱ,��������һҳ����ʱ*/
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
			bitmapUtils=new BitmapUtils(ct);/*����Bitmap������*/
			/*��������ʧ�ܺ�Ĭ�ϵ�ͼƬ*/
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
			image.setScaleType(ScaleType.FIT_XY);/*���ڿؼ���С���ͼƬ*/
			/**����ͷ����������*/
			TopNews topNewsData=mTopNewsList.get(position);/*��ȡ����ͷ����������*/
			bitmapUtils.display(image,topNewsData.topimage);/*��������ͼƬ*/
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
			
			/*��ȡ�Ѷ�����ID,�����Ѷ�����ID�����б����ű���*/
			String readIds=SharedPrefUtil.getString(ct,"readIds");
			if(readIds.contains(mNewsList.get(position).id)){
				holder.itemNewsTitle.setTextColor(Color.GRAY);
			}else{
				holder.itemNewsTitle.setTextColor(Color.BLACK);
			}
			
			
			return convertView;
		}
		
		/**����һ���ڲ���*/
		private class ViewHolder
		{
			private ImageView itemNewsPic;
			private TextView itemNewsTitle;
			private TextView itemNewsDate;
		}		
	}
}
