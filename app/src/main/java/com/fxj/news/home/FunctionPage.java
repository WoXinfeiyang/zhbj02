package com.fxj.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fxj.news.base.BasePage;
import com.fxj.slidingtest01.R;
import com.lidroid.xutils.ViewUtils;

public class FunctionPage extends BasePage{

	public FunctionPage(Context ct) {
		super(ct);	
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view=inflater.inflate(R.layout.news_center_frame,null);
		ViewUtils.inject(this,view);
		setSlidingMenuEnable(false);/*关闭侧滑菜单*/
		initTitleBar(view);
		/*TextView构造函数中传入来自BasePage的Context上下文对象ct,用于构造一个TextView对象*/
		TextView textView=new TextView(ct);
		textView.setText("我是首页");
		
//		FrameLayout mFrameLayout=(FrameLayout) view.findViewById(R.id.title_bar);
//		mFrameLayout.addView(textView);
		
		return view;
	}

	@Override
	public void initData() {
		
	}

}
