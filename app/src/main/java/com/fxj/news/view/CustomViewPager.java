package com.fxj.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends LazyViewPager {
	/**给ViewPager设置左右滑动模式,当为true是ViewPager可以左右滑动,当为false时不能左右滑动*/
	private boolean setTouchModel=false;
	
	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(setTouchModel)
		{
			return super.onInterceptTouchEvent(ev);
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(setTouchModel)
		{
		return super.onTouchEvent(ev);
		}
		else
		{
			return false;
		}
	}
	
	
}
