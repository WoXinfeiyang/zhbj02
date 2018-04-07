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
		setSlidingMenuEnable(false);/*�رղ໬�˵�*/
		initTitleBar(view);
		/*TextView���캯���д�������BasePage��Context�����Ķ���ct,���ڹ���һ��TextView����*/
		TextView textView=new TextView(ct);
		textView.setText("������ҳ");
		
//		FrameLayout mFrameLayout=(FrameLayout) view.findViewById(R.id.title_bar);
//		mFrameLayout.addView(textView);
		
		return view;
	}

	@Override
	public void initData() {
		
	}

}
