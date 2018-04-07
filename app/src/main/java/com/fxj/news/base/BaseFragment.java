package com.fxj.news.base;

import com.fxj.slidingtest01.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/*将创建一个Fragment的一些通用方法抽象出来定义一个通用类BaseFragment
 * */
public abstract class BaseFragment extends Fragment
{
	String tag="BaseFragment";
	
	public View view;
	public Context ct;
	public SlidingMenu sm;/*定义一个SlidingMenu对象便于BaseFragment的实现子类获取MainActivity中SlidingMenu*/

	/*创建该Fragment时被回调，该方法只被回调一次*/
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.v(tag,"onCreate");
		super.onCreate(savedInstanceState);
		ct=getActivity();
		sm=((MainActivity)getActivity()).getSlidingMenu();/*获取MainActivity中的SlidingMenu对象*/
	}

	/*每次创建、重绘该Fragment的View组件时回调该方法，该Fragment将显示该方法返回的View组件*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		Log.v(tag,"onCreateView");
		view=initView(inflater);
		return view;
	}
	
	/*当该Fragment做所在的Activity被启动完成后回调此方法*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		Log.v(tag,"onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}
	

	

	
	/**定义一个抽象方法，用于初始化view,在BaseFragment基类中供onCreateView方法回调*/
	public abstract View initView(LayoutInflater inflater);
	/**定义一个抽象方法，用于初始化数据,在BaseFragment基类中供onActivityCreated方法回调*/
	public abstract void initData(Bundle savedInstanceState);
}
