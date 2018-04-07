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



/*������һ��Fragment��һЩͨ�÷��������������һ��ͨ����BaseFragment
 * */
public abstract class BaseFragment extends Fragment
{
	String tag="BaseFragment";
	
	public View view;
	public Context ct;
	public SlidingMenu sm;/*����һ��SlidingMenu�������BaseFragment��ʵ�������ȡMainActivity��SlidingMenu*/

	/*������Fragmentʱ���ص����÷���ֻ���ص�һ��*/
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.v(tag,"onCreate");
		super.onCreate(savedInstanceState);
		ct=getActivity();
		sm=((MainActivity)getActivity()).getSlidingMenu();/*��ȡMainActivity�е�SlidingMenu����*/
	}

	/*ÿ�δ������ػ��Fragment��View���ʱ�ص��÷�������Fragment����ʾ�÷������ص�View���*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		Log.v(tag,"onCreateView");
		view=initView(inflater);
		return view;
	}
	
	/*����Fragment�����ڵ�Activity��������ɺ�ص��˷���*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		Log.v(tag,"onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}
	

	

	
	/**����һ�����󷽷������ڳ�ʼ��view,��BaseFragment�����й�onCreateView�����ص�*/
	public abstract View initView(LayoutInflater inflater);
	/**����һ�����󷽷������ڳ�ʼ������,��BaseFragment�����й�onActivityCreated�����ص�*/
	public abstract void initData(Bundle savedInstanceState);
}
