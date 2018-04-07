package com.fxj.news.base;

import com.fxj.slidingtest01.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.fxj.slidingtest01.R;

/*
 * ������public abstract class BasePage
 * ˵����������һ��Page�ĳ��󷽷���ͨ���������ṩһЩ��ʵ�ֵĳ��󷽷���ʵ��
 * ���¹��ܣ�
 * a�������棬���ȴ�������Context�л�ȡ����Layout�����������ṩ��ʵ�ֵĳ��󷽷� View initView(LayoutInflater inflater)��ʼ������
 * b����ʼ�����ݣ��������ṩ��ʵ�ֵĳ��󷽷�void initData()��ʼ����������
 * */

public abstract class BasePage implements OnClickListener{	
	public Context ct;
	/**initView���������ص�View����*/
	private View view;
	/**���ڻ�ȡMainActivity�е�SlidingMenu����*/
	protected SlidingMenu sm;
	
	/*����һЩ��ɫ������������ؼ�*/
	private Button leftButton;
	/** �����໬�˵���ImageButton�ؼ�*/
	private ImageButton leftImageButton;
	/**��ɫ�������еı���*/
	protected TextView titleTextView;
	private ImageButton rightButton;
	private ImageButton rightImageButton;
	
	/**�໬�˵�������־,trueΪ����,falseΪ�ر�,Ĭ��ֵΪtrue*/
	private boolean isSlidingMenuEnable=true;
	
	public boolean isLoadSuccess=false;
	
	
	public BasePage(Context ct) {
		this.ct = ct;
		/*LayoutInflater ���ڻ�ȡ����Layout����ʵ��������������Context�л�ȡ����Layout����*/
		LayoutInflater inflater=(LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.view=initView(inflater);
		
		if(ct instanceof MainActivity)
		{			
			/*��ȡMainActivity�е�SlidingMenu����*/
			this.sm=((MainActivity) ct).getSlidingMenu();
		}
	}
	/**��ȡ������View����*/
	public View getRootView()
	{
		return this.view;
	}
	
	/**���ò໬�˵��������߹ر�
	 * @param isEnalbeΪtrueʱ��ʾ�����໬�˵�,Ϊfalseʱ��ʾ�رղ໬�˵�
	 * */
	public void setSlidingMenuEnable(boolean isEnalbe)
	{
		MainActivity mainUI=(MainActivity) ct;
		SlidingMenu sm=mainUI.getSlidingMenu();
		if (isEnalbe) {
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
		this.isSlidingMenuEnable=isEnalbe;
	}
	
	/**��ʼ����ɫ������*/
	protected void initTitleBar(View view)
	{
		this.leftButton=(Button)view.findViewById(R.id.btn_left);
		this.rightButton=(ImageButton) view.findViewById(R.id.btn_right);
		/*��ȡ��ɫ��������ImageButtonͼƬ��ť�ؼ�,���������໬�˵�*/
		this.leftImageButton=(ImageButton) view.findViewById(R.id.imgbtn_left);
		this.rightImageButton=(ImageButton) view.findViewById(R.id.imgbtn_right);
		
		/*����ɫ��������ImageButtonͼƬ��ť�ؼ�����ͼƬ,���������໬�˵�*/
		this.leftImageButton.setImageResource(R.drawable.img_menu);
		
		if(this.isSlidingMenuEnable)
		{
			this.leftImageButton.setVisibility(View.VISIBLE);
		}else{
			this.leftImageButton.setVisibility(View.INVISIBLE);
		}
		
		/*��ȡ��ɫ�������еı���TextView���*/
		this.titleTextView=(TextView)view.findViewById(R.id.txt_title);
		
		this.leftButton.setVisibility(View.GONE);
		this.rightButton.setVisibility(View.GONE);
		
		if(this.leftImageButton!=null)
		{
			this.leftImageButton.setOnClickListener(this);
		}
	}
	
	/**��дView.OnClickListener�ӿڵ�onClick����*/
	@Override
	public void onClick(View v) {
	 switch(v.getId())
	 {
	 /*��ɫ��������ImageButtonͼƬ��ť�ؼ�����¼�,���������໬�˵�*/
	 case R.id.imgbtn_left:
		 Handler handler=new Handler();
		 handler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				sm.toggle();
			}
		},100);
		 break;
	 }
		
	}
	/**��ʼ�����ֽ���,�÷�����BasePage�����б����캯������*/
	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();/*BasePage������ʵ�ָ÷���,��BaseFragment��ʵ������HomeFragement��initData����ʹ��*/
	
}
