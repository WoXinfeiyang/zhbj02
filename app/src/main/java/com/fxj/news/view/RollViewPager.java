package com.fxj.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**�ֲ�ͼViewpager*/
public class RollViewPager extends ViewPager {
	/**��ʼ���µ�X����*/
	private int startX;
	/**��ʼ���µ�Y����*/
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
	 * ��дdispatchKeyEvent�¼��ַ�����
	 * �¼��ַ������󸸿ؼ������ڿؼ��Ƿ�����Touch�¼�
	 * 1����ָ���һ�,��Ļ�����ƶ�,���ǵ�һ��itemҳ��,��Ҫ���ؼ����أ�
	 * 2����ָ����,��Ļ�����ƶ�,�������һ��itemҳ��,��Ҫ���ؼ�����
	 * 3�����»���,��Ҫ���ؼ�����
	 * */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		switch(ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			/*���󸸿ؼ���Ҫ����Touch�¼�*/
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			int endX=(int) ev.getRawX();
			int endY=(int) ev.getRawY();
			if(Math.abs(endX-startX)>Math.abs(endY-startY))/*�����һ���ʱ*/
			{
				if(endX>startX)/*����ָ���һ���ʱ*/
				{
					if(getCurrentItem()==0)/*���ǵ�һ��itemҳ��ʱ,��Ҫ���ؼ�����*/
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				else/*����ָ���󻬶�ʱ*/
				{
					if(getCurrentItem()==getAdapter().getCount()-1)/*�������һ��itemҳ��ʱ,��Ҫ���ؼ�����*/
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
				
			}
			else/*�����»���ʱ*/
			{
				/*�����»���ʱ���󸸿ؼ�����Touch�¼�*/
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
