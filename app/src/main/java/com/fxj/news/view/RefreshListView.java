package com.fxj.news.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fxj.slidingtest01.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**��ˢ�¹��ܵ�ListView���*/
public class RefreshListView extends ListView implements android.widget.AdapterView.OnItemClickListener

{
	
	/**����ˢ��*/
	private static final int State_PullToRefresh=0;
	/**�ͷ��ɿ�ˢ��*/
	private static final int State_ReleaseToRefresh=1;
	/**����ˢ��*/
	private static final int State_Refreshing=2;
	/**��ǰˢ��״̬,Ĭ��ֵΪ����ˢ��*/
	private int mCurrentRefreshState=State_PullToRefresh;
	
	/*��������ˢ���������*/
	/**����ˢ��ͷ���ֶ���*/
	private View mHeaderView;
	/**����ˢ�¼�ͷͼ��*/
	private ImageView mArrow;
	/**����ˢ�½�����*/
	private ProgressBar mRefreshProgressBar;
	/**����ˢ�±���(������ʾ)*/
	private TextView mRefreshTitle;
	/**����ˢ��ʱ��*/
	private TextView mRefreshTime;
	/**����ˢ��ͷ���ָ߶�*/
	private int mHeaderViewHeight;
	/**��ͷ������ת����*/
	private RotateAnimation animUp;
	/**��ͷ������ת����*/
	private RotateAnimation animDown;
	/**�������Y����,Ĭ��ֵΪ-1*/
	private int startY=-1;
		
	/*�������������������*/
	/**�������ظ���Ų��ֶ���*/
	private View mFooterView;
	/**�������ظ���Ų��ָ߶�*/
	private int mFooterHeight;
	/**�Ų������ڼ��ر�־,Ĭ��ֵΪfalse��true��ʾ���ڼ�����*/
	private boolean isLoadingMore=false;
	
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}



	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}
	/**��ʼ������ˢ��ͷ����*/
	private void initHeaderView() {
		this.mHeaderView=View.inflate(getContext(),R.layout.refresh_header,null);
		this.addHeaderView(mHeaderView);
		
		this.mArrow=(ImageView) this.mHeaderView.findViewById(R.id.iv_arrow);
		this.mRefreshProgressBar=(ProgressBar) this.mHeaderView.findViewById(R.id.refresh_progress);
		this.mRefreshTitle=(TextView) this.mHeaderView.findViewById(R.id.tv_refreshTitle);
		this.mRefreshTime=(TextView) this.mHeaderView.findViewById(R.id.tv_refreshTime);
		
		this.mHeaderView.measure(0,0);
		mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
		
		this.mHeaderView.setPadding(0,-this.mHeaderViewHeight,0,0);/*����ͷ����*/
		
		initArrowAnim();/*��ʼ����ͷ����*/
		this.mRefreshTime.setText("���ˢ��ʱ��:"+getCurrentTime());
		
	}	
	
	/**��ʼ���������ؽŲ���*/
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.refresh_footer,null);
		this.addFooterView(mFooterView);
		this.mFooterView.measure(0,0);
		mFooterHeight = this.mFooterView.getMeasuredHeight();
		this.mFooterView.setPadding(0,-this.mFooterHeight,0,0);/*���ؽŲ���*/
		
		this.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				/*��������־λ�������л��߿��ٻ���ʱ*/
				if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING)
				{
					/**�����������,���ҽŲ���û�����ڼ�����,
					 * �Ų������ڼ��ر�־λȷ�����������׺�Ų���ֻ��ʾһ��
					 * */
					if(getLastVisiblePosition()==getCount()-1&&!isLoadingMore)
					{
						System.out.println("�Ѿ��������ˡ���");
						mFooterView.setPadding(0,0,0,0);/*��ʾ�Ų���*/
						setSelection(getCount()-1);/*�ı�listview��ʾλ*/
						isLoadingMore=true;
						
						if(mRefreshListener!=null)
						{
							mRefreshListener.onLoadMore();
						}
					}
					
				}
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}	
	
	/*��дonTouchEvent����*/	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(this.startY==-1)/*ȷ��startY��Ч*/
			{
				startY=(int) ev.getRawY();
			}
			if(this.mCurrentRefreshState==State_Refreshing)/*������ˢ��ʱ��������*/
			{
				break;
			}
			
			int endY=(int) ev.getRawY();
			int dy=endY-startY;/*�ƶ�ƫ����*/
			if(dy>0&&getFirstVisiblePosition()==0)/*ֻ���������ҵ�ǰ�ǵ�һ��item,����������*/
			{
				int padding=dy-this.mHeaderViewHeight;
				this.mHeaderView.setPadding(0,padding,0,0);/*���õ�ǰ��padding*/
				
				if(padding<0&&this.mCurrentRefreshState!=State_PullToRefresh)
				{
					this.mCurrentRefreshState=State_PullToRefresh;
					refreshState();
				}else if(padding>0&&this.mCurrentRefreshState!=State_ReleaseToRefresh){
					this.mCurrentRefreshState=State_ReleaseToRefresh;
					refreshState();
				}				
				return true;
			}			
			break;
		case MotionEvent.ACTION_UP:
			startY=-1;/*���������Y����ָ�ΪĬ��ֵ*/
			
			if(this.mCurrentRefreshState==State_PullToRefresh)/*��̧����ָʱ����������ˢ�µ�״̬ʱ*/
			{
				this.mHeaderView.setPadding(0,-this.mHeaderViewHeight,0,0);/*��ͷ��������*/
			}
			else if(this.mCurrentRefreshState==State_ReleaseToRefresh)/*��̧����ָʱ�������ͷ��ɿ�ˢ�µ�״̬ʱ*/
			{
				this.mCurrentRefreshState=State_Refreshing;/*��״̬�޸�Ϊ����ˢ��*/
				this.mHeaderView.setPadding(0,0,0,0);/*��ͷ������ʾ*/
				refreshState();
			}			
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**��ʼ����ͷ����*/
	private void initArrowAnim() {
		/*��ͷ������ת����*/
		this.animUp = new RotateAnimation(0,-180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		this.animUp.setDuration(200);/*���ö�������ʱ��*/
		this.animUp.setFillAfter(true);/*������ɺ󱣳�״̬*/
		
		/*��ͷ������ת����*/
		this.animDown = new RotateAnimation(-180,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		this.animDown.setDuration(200);/*���ö�������ʱ��*/
		this.animDown.setFillAfter(true);/*������ɺ󱣳�״̬*/		
	}
	
	/**��ȡ��ǰʱ��*/
	private String getCurrentTime() {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}	
	
	/**��������ˢ��״̬�޸�����ͷView���*/
	private void refreshState() {
		switch(this.mCurrentRefreshState)
		{
		case State_PullToRefresh:
			this.mRefreshTitle.setText("����ˢ��");
			this.mArrow.setVisibility(View.VISIBLE);
			this.mRefreshProgressBar.setVisibility(View.INVISIBLE);
			this.mArrow.startAnimation(animDown);
			break;
		case State_ReleaseToRefresh:
			this.mRefreshTitle.setText("�ɿ�ˢ��");
			this.mArrow.setVisibility(View.VISIBLE);
			this.mRefreshProgressBar.setVisibility(View.INVISIBLE);
			this.mArrow.startAnimation(animUp);
			break;
		case State_Refreshing:
			this.mRefreshTitle.setText("����ˢ���С���");
			this.mArrow.clearAnimation();/*�������������,��������*/
			this.mArrow.setVisibility(View.INVISIBLE);
			this.mRefreshProgressBar.setVisibility(View.VISIBLE);
			
			if(this.mRefreshListener!=null)
			{
				this.mRefreshListener.onRefresh();
			}
			
			break;
		default:
			break;			
		}
		
	}
	
	
	
	/**ˢ�¼������ӿ�*/
	public interface OnRefreshListener
	{
		public void onRefresh();
		public void onLoadMore();// ������һҳ����
	}
	/**
	 * ˢ����ɱ�־
	 * @param isSuccess---����ˢ�³ɹ���־,�����޸�����ˢ��ʱ��
	 * */
	public void onRefreshComplete(boolean isSuccess)
	{
		/*����ˢ����ɺ�Ų�����ز���,�������������ΪĬ��ֵ*/
		if(isLoadingMore)/*���ڼ��ظ���*/
		{
			this.mFooterView.setPadding(0,-this.mFooterHeight,0,0);/*���ؽŲ���*/
			this.isLoadingMore=false;/*���Ų������ڼ��ر�־��Ϊfalse,�����˿�û��ʹ�ýŲ��ּ���*/
		}
		
		/*����ˢ����ɺ�ͷ������ز���,�������������ΪĬ��ֵ*/
		this.mCurrentRefreshState=State_PullToRefresh;/*����ǰ����ˢ��״̬����ΪĬ��ֵ*/
		this.mHeaderView.setPadding(0,-this.mHeaderViewHeight,0,0);/*��ͷ��������*/
		
		this.mRefreshTitle.setText("����ˢ��");
		if(isSuccess)
		{
			this.mRefreshTime.setText(getCurrentTime());
		}
		this.mArrow.setVisibility(View.VISIBLE);
		this.mRefreshProgressBar.setVisibility(View.INVISIBLE);
		
	}
	
	/**ˢ�¼������ӿڶ���*/
	OnRefreshListener mRefreshListener;
	
	/**����(��)ˢ�¼�����*/
	public void setOnRefreshListener(OnRefreshListener listener)
	{
		this.mRefreshListener=listener;
	}


	
	/*���·�װListView��OnItemClickListener������*/
	
	OnItemClickListener mItemClickListener;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated method stub
		super.setOnItemClickListener(this);
		this.mItemClickListener=listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(this.mItemClickListener!=null)
		{
			this.mItemClickListener.onItemClick(parent, view,position-getHeaderViewsCount(), id);
		}		
	}

}
