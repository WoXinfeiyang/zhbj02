package com.fxj.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxj.news.base.BasePage;

public class GovAffairsPage extends BasePage{

	public GovAffairsPage(Context ct) {
		super(ct);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		setSlidingMenuEnable(false);/*关闭侧滑菜单*/
		/*TextView构造函数中传入来自BasePage的Context上下文对象ct,用于构造一个TextView对象*/
		TextView textView=new TextView(ct);
		textView.setText("我是政务指南");
		return textView;
	}

	@Override
	public void initData() {

		
	}

}
