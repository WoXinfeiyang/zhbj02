package com.fxj.news.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fxj.news.base.BaseFragment;
import com.fxj.news.base.DZBaseAdapter;
import com.fxj.news.home.FunctionPage;
import com.fxj.news.home.GovAffairsPage;
import com.fxj.news.home.NewsCenterPage;
import com.fxj.news.home.SettingPage;
import com.fxj.news.home.SmartServicePage;
import com.fxj.slidingtest01.MainActivity;
import com.fxj.slidingtest01.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;



/**�໬�˵�,�̳���BaseFragment*/
public class LeftMenuFragment extends BaseFragment
{
	private static final String Tag="LeftMenuFragment";
	
	public static final int NEWS_CENTER = 1;
	

	private int menuType = 0;
	
	/**�໬�˵�ListView����ǰ��ѡ�е�λ�ã���ʼ��Ϊ0*/
	public static int leftMenuCurrentPosition = 0;
	
	/**����໬�˵�ListView�����Adapter*/
	private LeftMenuAdapter leftMenuAdapter=null;
	/**�໬�˵�ListView����item���Ƽ�����*/
	private ArrayList<String> leftMenuListViewName=new ArrayList<String>();
	
	/**����ע��ķ�ʽ������������ListView����*/
	@ViewInject(R.id.lv_menu_news_center)
	private ListView newsCenterClassifyLV;
	
	/**����ע��ķ�ʽ�����ǻ۷���ListView����*/
	@ViewInject(R.id.lv_menu_smart_service)
	private ListView smartServiceClassifyLV;
	
	/**����ע��ķ�ʽ������������ListView����*/
	@ViewInject(R.id.lv_menu_govaffairs)
	private ListView govAffairsClassifyLV;
	
	/**
	 * ���ò໬�൥����,����ҳ������5����ҳǩ����
	 * ��Ҫ���ܣ�1�����ö�ӦListView�����Adapter,��ListView�����б����������
	 * listViewName----ListView�����б��������ַ���
	 * */
	public void setLeftMenuData(ArrayList<String> listViewName)
	{
		leftMenuListViewName.clear();/*���������Ĳ໬�˵��б������Ƽ���������������*/
		leftMenuListViewName.addAll(listViewName);/*��Ӳ໬�˵��б�������*/

		leftMenuAdapter=new LeftMenuAdapter(ct, listViewName);
		newsCenterClassifyLV.setAdapter(leftMenuAdapter);		
	}

	/**������ҳ����ҳ�е�ǰ�໬�˵�����ҳ*/
	protected void setCurrentLeftMenuDetailPager(int position)
	{
		/**�õ���ǰFragment��������Activity����*/
		MainActivity mainUI=(MainActivity)ct;
		ContentFragment fm=mainUI.getContentFragment();
		switch(fm.getCurrentContentPager())
		{
		case 0:/*��ǰ�ײ���ѡ��ťѡ�е�����ҳ*/
			FunctionPage functionPage=fm.getFunctionPage();
			break;
			
		case 1:/*��ǰ�ײ���ѡ��ťѡ�е�����������ҳ��*/
			NewsCenterPage newsCenterPage=fm.getNewsCenterPage();
			newsCenterPage.setNewsCenterPageCurrentLeftMenuDetailPager(leftMenuCurrentPosition);
			break;
			
		case 2:/*��ǰ�ײ���ѡ��ťѡ�е����ǻ۷���ҳ��*/
			SmartServicePage smartServicePage=fm.getSmartServicePage();
			break;
		case 3:/*��ǰ�ײ���ѡ��ťѡ�е�����������ҳ��*/
			GovAffairsPage  govAffairsPage=fm.getGovAffairsPage();
			break;
		
		case 4:/*��ǰ�ײ���ѡ��ťѡ�е�������ҳ��*/
			SettingPage settingPage=fm.getSettingPage();
			break;
		}
	}

		
	/**
	 * ����໬�˵�ListView�����Adapter 
	 * */	
	public class LeftMenuAdapter extends DZBaseAdapter<String,ListView>
	{		
		/**
		 * ����໬�˵�ListView�����Adapter���캯��
		 * @param context-----������
		 * @param list----���໬�˵�ListView�����б���������ַ���
		 * */
		public LeftMenuAdapter(Context context, List<String> list) {
			super(context, list);
		}

		public void setSelectedPosition(int position) {
			leftMenuCurrentPosition=position;
			notifyDataSetInvalidated();/*ÿ���������ݼ�����иı���������������ݼ�������Ч���ͻ���ô˷����ػ�ؼ�*/
		}		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView==null)
			{
				convertView=View.inflate(ct,R.layout.layout_item_menu,null);/*��ȡListView�б����*/
			}
			
			/*��ʼ��R.layout.layout_item_menu������ImageView��TextView*/
			ImageView iv=(ImageView)convertView.findViewById(R.id.iv_leftMenuItem); 
			TextView tv=(TextView)convertView.findViewById(R.id.tv_leftMenuItem);
			/*����ѡ��ListView�ؼ��е��б������װ���ַ����ļ�������ָ����Ԫ�أ������list��һ��װ���ַ����ļ�����*/
			tv.setText(list.get(position));
			
			
			if(leftMenuCurrentPosition==position)/*��ListView�б��ѡ��ʱ*/
			{
				convertView.setSelected(true);
				convertView.setPressed(true);
				convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
				tv.setTextColor(ct.getResources().getColor(R.color.menu_item_text_color));
				iv.setBackgroundResource(R.drawable.menu_arr_select);
			}
			else
			{
				convertView.setSelected(false);
				convertView.setPressed(false);
				convertView.setBackgroundColor(Color.TRANSPARENT);
				tv.setTextColor(ct.getResources().getColor(R.color.white));
				iv.setBackgroundResource(R.drawable.menu_arr_normal);				
			}
			
			return convertView;
		}
		
	}
	
	
	
	
	
	@Override
	public View initView(LayoutInflater inflater) {
		View view=inflater.inflate(R.layout.layout_left_menu,null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) 
	{
//		ArrayList<String> menuList=new ArrayList<String>();
//		menuList.add("����1");
//		menuList.add("�Ƽ�2");
//		menuList.add("�ƾ�3");
//		menuList.add("����4");
//		newsCenterClassifyLV.setVisibility(View.VISIBLE);
//		setLeftMenuData(menuList);
		
		this.newsCenterClassifyLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				
				leftMenuAdapter.setSelectedPosition(position);
				
				/*�����ڲ໬�˵���ѡ��item��Ӧ����ҳ�е�ҳ��*/
				setCurrentLeftMenuDetailPager(position);
				
				LogUtils.d(Tag+"��ǰ�ڲ໬�˵��е���ؼ�ID="+position+",this.leftMenuCurrentPosition="+leftMenuCurrentPosition);
				System.out.println(Tag+"��ǰ�ڲ໬�˵��е���ؼ�ID="+position+",this.leftMenuCurrentPosition="+leftMenuCurrentPosition);
				
				// ��ǰλ�õ��ڵ��λ��ֱ���л�
				if (position == leftMenuCurrentPosition) {
					sm.toggle();
					return;
				}		
			}
		});		
	}	
}
