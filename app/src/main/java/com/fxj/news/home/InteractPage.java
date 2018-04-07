package com.fxj.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxj.news.base.BasePage;
import com.fxj.news.bean.NewsCenterCategories.NewsCategory;

public class InteractPage extends BasePage {

	private NewsCategory category;
	/**"新闻中心"中的“互动”Page页*/
	public InteractPage(Context ct,NewsCategory category) {
		super(ct);
		this.category=category;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		/*TextView构造函数中传入来自BasePage的Context上下文对象ct,用于构造一个TextView对象*/
		TextView textView=new TextView(ct);
		textView.setText("我是互动");
		return textView;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
