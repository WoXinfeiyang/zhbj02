package com.fxj.news.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxj.news.base.BasePage;

public class SmartServicePage extends BasePage{

	public SmartServicePage(Context ct) {
		super(ct);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		setSlidingMenuEnable(true);/*�򿪲໬�˵�*/
		/*TextView���캯���д�������BasePage��Context�����Ķ���ct,���ڹ���һ��TextView����*/
		TextView textView=new TextView(ct);
		textView.setText("�����ǻ۷�������");
		return textView;
	}

	@Override
	public void initData() {

		
	}

}
