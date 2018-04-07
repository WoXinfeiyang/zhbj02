package com.fxj.slidingtest01;

import com.fxj.slidingtest01.R;
import com.fxj.news.fragment.ContentFragment;/*����HomeFragment���ڵ�jar��*/
import com.fxj.news.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity 
{
	private SlidingMenu sm;/*����һ�������˵�����*/
	
	private LeftMenuFragment leftMenuFragment;
	private ContentFragment contentFragment;
	
	private String tag="MainActivity";
	
	private static String Fragment_LeftMenu="leftMenuFragment";
	private static String Fragment_Content="contentFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.v(tag,"onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setBehindContentView(R.layout.menu);/*���ò�����Ĳ���*/
		setContentView(R.layout.content);
		
		sm=getSlidingMenu();/*�õ������˵�*/
		
		/*���û����˵�������ʽ*
		 * �����˵��Ǵ���߻����ұ߻�������
		 * ���������������LEFT��Ҳ���������ұ�RIGHT ��������������LEFT_RIGHT
		 */
		sm.setMode(SlidingMenu.LEFT);
		
		/*���û����˵�����֮������ҳ��ʾ��ʣ����,����Ϊdimension��ԴID*/
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		/*���û����˵�����Ӱ ������Ӱ����Ӱ��Ҫ�ڿ�ʼ��ʱ���ر𰵣������ı䵭*/
		sm.setShadowDrawable(R.drawable.shadow);
		/*������Ӱ�Ŀ��*/
		sm.setShadowWidthRes(R.dimen.shadow_width);
		
		/*���û����˵��ķ�Χ,��������Ϊ����ֵ��
		 * SlidingMenu.TOUCHMODE_FULLSCREEN ����ȫ������
		 * SlidingMenu.TOUCHMODE_MARGIN ֻ���ڱ��ػ���
		 * SlidingMenu.TOUCHMODE_NONE ���ܻ���
		 * */
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

			leftMenuFragment=new LeftMenuFragment();
			FragmentManager leftMenuFragmentManager=getSupportFragmentManager();
			FragmentTransaction leftMenuTransaction=leftMenuFragmentManager.beginTransaction();
			leftMenuTransaction.replace(R.id.menu_frame,leftMenuFragment,Fragment_LeftMenu);
			leftMenuTransaction.addToBackStack(null);
			leftMenuTransaction.commit();
			
			contentFragment=new ContentFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,contentFragment,Fragment_Content).commit();

	}

	
	/**��ȡ�໬�˵�Fragment����*/
	public LeftMenuFragment getLeftMenuFragment()
	{
		/*�Ա�ǩ�ķ�ʽ��ò໬�˵�����
		 * */
		LeftMenuFragment fm=(LeftMenuFragment)getSupportFragmentManager().findFragmentByTag(Fragment_LeftMenu);
		return fm;
	}

	
	/**��ȡ��ҳFragment����*/
	public ContentFragment getContentFragment()
	{
		/*�Ա�ǩ�ķ�ʽ��ò໬�˵�����
		 * */
		ContentFragment fm=(ContentFragment)getSupportFragmentManager().findFragmentByTag(Fragment_Content);
		return fm;
	}
	
	public void switchFragment(Fragment f)
	{
		Log.v(tag,"switchFragment");
		FragmentManager fragManager=getSupportFragmentManager();
		FragmentTransaction transaction=fragManager.beginTransaction();/*����һ��FragmentTransaction����*/
		transaction.replace(R.id.content_frame,f);
		transaction.addToBackStack(null);
		transaction.commit();
		sm.toggle();/*�Զ��л������ڻ����˵�ҳѡ���б�����Զ��л�������ҳ*/
	}
}
