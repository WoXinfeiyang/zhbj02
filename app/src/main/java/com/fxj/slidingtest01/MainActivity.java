package com.fxj.slidingtest01;

import com.fxj.slidingtest01.R;
import com.fxj.news.fragment.ContentFragment;/*引入HomeFragment所在的jar包*/
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
	private SlidingMenu sm;/*定义一个滑动菜单对象*/
	
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
		setBehindContentView(R.layout.menu);/*设置侧边栏的布局*/
		setContentView(R.layout.content);
		
		sm=getSlidingMenu();/*得到滑动菜单*/
		
		/*设置滑动菜单滑动方式*
		 * 滑动菜单是从左边还是右边滑出来，
		 * 参数可以设置左边LEFT，也可以设置右边RIGHT ，还能设置左右LEFT_RIGHT
		 */
		sm.setMode(SlidingMenu.LEFT);
		
		/*设置滑动菜单出来之后，内容页显示的剩余宽度,参数为dimension资源ID*/
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		
		/*设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡*/
		sm.setShadowDrawable(R.drawable.shadow);
		/*设置阴影的宽度*/
		sm.setShadowWidthRes(R.dimen.shadow_width);
		
		/*设置滑动菜单的范围,参数可以为以下值：
		 * SlidingMenu.TOUCHMODE_FULLSCREEN 可以全屏滑动
		 * SlidingMenu.TOUCHMODE_MARGIN 只能在边沿滑动
		 * SlidingMenu.TOUCHMODE_NONE 不能滑动
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

	
	/**获取侧滑菜单Fragment对象*/
	public LeftMenuFragment getLeftMenuFragment()
	{
		/*以标签的方式获得侧滑菜单对象
		 * */
		LeftMenuFragment fm=(LeftMenuFragment)getSupportFragmentManager().findFragmentByTag(Fragment_LeftMenu);
		return fm;
	}

	
	/**获取主页Fragment对象*/
	public ContentFragment getContentFragment()
	{
		/*以标签的方式获得侧滑菜单对象
		 * */
		ContentFragment fm=(ContentFragment)getSupportFragmentManager().findFragmentByTag(Fragment_Content);
		return fm;
	}
	
	public void switchFragment(Fragment f)
	{
		Log.v(tag,"switchFragment");
		FragmentManager fragManager=getSupportFragmentManager();
		FragmentTransaction transaction=fragManager.beginTransaction();/*开启一个FragmentTransaction事物*/
		transaction.replace(R.id.content_frame,f);
		transaction.addToBackStack(null);
		transaction.commit();
		sm.toggle();/*自动切换，当在滑动菜单页选中列表项后自动切换到内容页*/
	}
}
