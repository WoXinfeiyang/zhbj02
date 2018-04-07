package com.fxj.news.base;

import com.fxj.slidingtest01.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.fxj.slidingtest01.R;

/*
 * 类名：public abstract class BasePage
 * 说明：定义了一个Page的抽象方法，通过向子类提供一些待实现的抽象方法来实现
 * 以下功能：
 * a、画界面，首先从上下文Context中获取界面Layout对象，向子类提供待实现的抽象方法 View initView(LayoutInflater inflater)初始化界面
 * b、初始化数据，向子类提供待实现的抽象方法void initData()初始化界面数据
 * */

public abstract class BasePage implements OnClickListener{	
	public Context ct;
	/**initView方法所返回的View对象*/
	private View view;
	/**用于获取MainActivity中的SlidingMenu对象*/
	protected SlidingMenu sm;
	
	/*定义一些红色标题栏上所需控件*/
	private Button leftButton;
	/** 引出侧滑菜单的ImageButton控件*/
	private ImageButton leftImageButton;
	/**红色标题栏中的标题*/
	protected TextView titleTextView;
	private ImageButton rightButton;
	private ImageButton rightImageButton;
	
	/**侧滑菜单开启标志,true为开启,false为关闭,默认值为true*/
	private boolean isSlidingMenuEnable=true;
	
	public boolean isLoadSuccess=false;
	
	
	public BasePage(Context ct) {
		this.ct = ct;
		/*LayoutInflater 用于获取界面Layout对象并实例化，从上下文Context中获取界面Layout对象*/
		LayoutInflater inflater=(LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.view=initView(inflater);
		
		if(ct instanceof MainActivity)
		{			
			/*获取MainActivity中的SlidingMenu对象*/
			this.sm=((MainActivity) ct).getSlidingMenu();
		}
	}
	/**获取根布局View对象*/
	public View getRootView()
	{
		return this.view;
	}
	
	/**设置侧滑菜单开启或者关闭
	 * @param isEnalbe为true时表示开启侧滑菜单,为false时表示关闭侧滑菜单
	 * */
	public void setSlidingMenuEnable(boolean isEnalbe)
	{
		MainActivity mainUI=(MainActivity) ct;
		SlidingMenu sm=mainUI.getSlidingMenu();
		if (isEnalbe) {
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
		this.isSlidingMenuEnable=isEnalbe;
	}
	
	/**初始化红色标题栏*/
	protected void initTitleBar(View view)
	{
		this.leftButton=(Button)view.findViewById(R.id.btn_left);
		this.rightButton=(ImageButton) view.findViewById(R.id.btn_right);
		/*获取红色标题栏上ImageButton图片按钮控件,用于引出侧滑菜单*/
		this.leftImageButton=(ImageButton) view.findViewById(R.id.imgbtn_left);
		this.rightImageButton=(ImageButton) view.findViewById(R.id.imgbtn_right);
		
		/*给红色标题栏上ImageButton图片按钮控件设置图片,用于引出侧滑菜单*/
		this.leftImageButton.setImageResource(R.drawable.img_menu);
		
		if(this.isSlidingMenuEnable)
		{
			this.leftImageButton.setVisibility(View.VISIBLE);
		}else{
			this.leftImageButton.setVisibility(View.INVISIBLE);
		}
		
		/*获取红色标题栏中的标题TextView组件*/
		this.titleTextView=(TextView)view.findViewById(R.id.txt_title);
		
		this.leftButton.setVisibility(View.GONE);
		this.rightButton.setVisibility(View.GONE);
		
		if(this.leftImageButton!=null)
		{
			this.leftImageButton.setOnClickListener(this);
		}
	}
	
	/**重写View.OnClickListener接口的onClick方法*/
	@Override
	public void onClick(View v) {
	 switch(v.getId())
	 {
	 /*红色标题栏上ImageButton图片按钮控件点击事件,用于引出侧滑菜单*/
	 case R.id.imgbtn_left:
		 Handler handler=new Handler();
		 handler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				sm.toggle();
			}
		},100);
		 break;
	 }
		
	}
	/**初始化布局界面,该方法在BasePage基类中被构造函数调用*/
	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();/*BasePage的子类实现该方法,供BaseFragment的实现子类HomeFragement的initData方法使用*/
	
}
