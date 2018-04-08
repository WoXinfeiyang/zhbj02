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



/**侧滑菜单,继承自BaseFragment*/
public class LeftMenuFragment extends BaseFragment
{
	private static final String Tag="LeftMenuFragment";
	
	public static final int NEWS_CENTER = 1;
	

	private int menuType = 0;
	
	/**侧滑菜单ListView对象当前被选中的位置，初始化为0*/
	public static int leftMenuCurrentPosition = 0;
	
	/**适配侧滑菜单ListView对象的Adapter*/
	private LeftMenuAdapter leftMenuAdapter=null;
	/**侧滑菜单ListView对象item名称集合类*/
	private ArrayList<String> leftMenuListViewName=new ArrayList<String>();
	
	/**采用注解的方式创建新闻中心ListView对象*/
	@ViewInject(R.id.lv_menu_news_center)
	private ListView newsCenterClassifyLV;
	
	/**采用注解的方式创建智慧服务ListView对象*/
	@ViewInject(R.id.lv_menu_smart_service)
	private ListView smartServiceClassifyLV;
	
	/**采用注解的方式创建政务中心ListView对象*/
	@ViewInject(R.id.lv_menu_govaffairs)
	private ListView govAffairsClassifyLV;
	
	/**
	 * 设置侧滑侧单数据,供主页中下面5个子页签调用
	 * 主要功能：1、设置对应ListView对象的Adapter,给ListView对象列表项填充数据
	 * listViewName----ListView对象列表项名称字符串
	 * */
	public void setLeftMenuData(ArrayList<String> listViewName)
	{
		leftMenuListViewName.clear();/*将新闻中心侧滑菜单列表项名称集合类对象首先清空*/
		leftMenuListViewName.addAll(listViewName);/*添加侧滑菜单列表项名称*/

		leftMenuAdapter=new LeftMenuAdapter(ct, listViewName);
		newsCenterClassifyLV.setAdapter(leftMenuAdapter);		
	}

	/**设置主页内容页中当前侧滑菜单详情页*/
	protected void setCurrentLeftMenuDetailPager(int position)
	{
		/**拿到当前Fragment所依附的Activity对象*/
		MainActivity mainUI=(MainActivity)ct;
		ContentFragment fm=mainUI.getContentFragment();
		switch(fm.getCurrentContentPager())
		{
		case 0:/*当前底部单选按钮选中的是首页*/
			FunctionPage functionPage=fm.getFunctionPage();
			break;
			
		case 1:/*当前底部单选按钮选中的是新闻中心页面*/
			NewsCenterPage newsCenterPage=fm.getNewsCenterPage();
			newsCenterPage.setNewsCenterPageCurrentLeftMenuDetailPager(leftMenuCurrentPosition);
			break;
			
		case 2:/*当前底部单选按钮选中的是智慧服务页面*/
			SmartServicePage smartServicePage=fm.getSmartServicePage();
			break;
		case 3:/*当前底部单选按钮选中的是政务中心页面*/
			GovAffairsPage  govAffairsPage=fm.getGovAffairsPage();
			break;
		
		case 4:/*当前底部单选按钮选中的是设置页面*/
			SettingPage settingPage=fm.getSettingPage();
			break;
		}
	}

		
	/**
	 * 适配侧滑菜单ListView对象的Adapter 
	 * */	
	public class LeftMenuAdapter extends DZBaseAdapter<String,ListView>
	{		
		/**
		 * 适配侧滑菜单ListView对象的Adapter构造函数
		 * @param context-----上下文
		 * @param list----填充侧滑菜单ListView各个列表项的名称字符串
		 * */
		public LeftMenuAdapter(Context context, List<String> list) {
			super(context, list);
		}

		public void setSelectedPosition(int position) {
			leftMenuCurrentPosition=position;
			notifyDataSetInvalidated();/*每当发现数据集监控有改变的情况，比如该数据集不再有效，就会调用此方法重绘控件*/
		}		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView==null)
			{
				convertView=View.inflate(ct,R.layout.layout_item_menu,null);/*获取ListView列表项布局*/
			}
			
			/*初始化R.layout.layout_item_menu布局中ImageView和TextView*/
			ImageView iv=(ImageView)convertView.findViewById(R.id.iv_leftMenuItem); 
			TextView tv=(TextView)convertView.findViewById(R.id.tv_leftMenuItem);
			/*根据选中ListView控件中的列表项填充装有字符串的集合类中指定的元素，这里的list是一个装有字符串的集合类*/
			tv.setText(list.get(position));
			
			
			if(leftMenuCurrentPosition==position)/*当ListView列表项被选中时*/
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
//		menuList.add("新闻1");
//		menuList.add("科技2");
//		menuList.add("财经3");
//		menuList.add("娱乐4");
//		newsCenterClassifyLV.setVisibility(View.VISIBLE);
//		setLeftMenuData(menuList);
		
		this.newsCenterClassifyLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				
				leftMenuAdapter.setSelectedPosition(position);
				
				/*设置在侧滑菜单中选中item对应在主页中的页面*/
				setCurrentLeftMenuDetailPager(position);
				
				LogUtils.d(Tag+"当前在侧滑菜单中点击控件ID="+position+",this.leftMenuCurrentPosition="+leftMenuCurrentPosition);
				System.out.println(Tag+"当前在侧滑菜单中点击控件ID="+position+",this.leftMenuCurrentPosition="+leftMenuCurrentPosition);
				
				// 当前位置等于点击位置直接切换
				if (position == leftMenuCurrentPosition) {
					sm.toggle();
					return;
				}		
			}
		});		
	}	
}
