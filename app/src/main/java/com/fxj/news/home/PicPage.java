package com.fxj.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxj.news.base.BasePage;
import com.fxj.news.bean.NewsCenterCategories.NewsCategory;

public class PicPage extends BasePage {
	
	private NewsCategory category;
	
	/**���������ġ��еġ���ͼ��Pageҳ*/
	public PicPage(Context ct,NewsCategory category) {
		super(ct);
		this.category=category;
	}
	
	@Override
	public View initView(LayoutInflater inflater) {
		/*TextView���캯���д�������BasePage��Context�����Ķ���ct,���ڹ���һ��TextView����*/
		TextView textView=new TextView(ct);
		textView.setText("������ͼ");
		return textView;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
