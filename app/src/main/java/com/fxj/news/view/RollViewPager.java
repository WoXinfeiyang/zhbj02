package com.fxj.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**轮播图Viewpager*/
public class RollViewPager extends ViewPager {
	/**开始按下的X坐标*/
	private int startX;
	/**开始按下的Y坐标*/
	private int startY;

	public RollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * 重写dispatchKeyEvent事件分发方法
	 * 事件分发，请求父控件及祖宗控件是否拦截Touch事件
	 * 1、手指朝右滑,屏幕向左移动,且是第一个item页面,需要父控件拦截；
	 * 2、手指朝左滑,屏幕向右移动,且是最后一个item页面,需要父控件拦截
	 * 3、上下滑动,需要父控件拦截
	 * */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		switch(ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			/*请求父控件不要拦截Touch事件*/
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			int endX=(int) ev.getRawX();
			int endY=(int) ev.getRawY();
			if(Math.abs(endX-startX)>Math.abs(endY-startY))/*当左右滑动时*/
			{
				if(endX>startX)/*当手指朝右滑动时*/
				{
					if(getCurrentItem()==0)/*当是第一个item页面时,需要父控件拦截*/
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				else/*当手指朝左滑动时*/
				{
					if(getCurrentItem()==getAdapter().getCount()-1)/*当是最后一个item页面时,需要父控件拦截*/
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				
			}
			else/*当上下滑动时*/
			{
				/*当上下滑动时请求父控件拦截Touch事件*/
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}


	
	

	
	

}
