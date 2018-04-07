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

/**带刷新功能的ListView组件*/
public class RefreshListView extends ListView implements android.widget.AdapterView.OnItemClickListener

{
	
	/**下拉刷新*/
	private static final int State_PullToRefresh=0;
	/**释放松开刷新*/
	private static final int State_ReleaseToRefresh=1;
	/**正在刷新*/
	private static final int State_Refreshing=2;
	/**当前刷新状态,默认值为下拉刷新*/
	private int mCurrentRefreshState=State_PullToRefresh;
	
	/*定义下拉刷新相关属性*/
	/**下拉刷新头布局对象*/
	private View mHeaderView;
	/**下拉刷新箭头图标*/
	private ImageView mArrow;
	/**正在刷新进度条*/
	private ProgressBar mRefreshProgressBar;
	/**下拉刷新标题(文字提示)*/
	private TextView mRefreshTitle;
	/**下拉刷新时间*/
	private TextView mRefreshTime;
	/**下拉刷新头布局高度*/
	private int mHeaderViewHeight;
	/**箭头向上旋转动画*/
	private RotateAnimation animUp;
	/**箭头向下旋转动画*/
	private RotateAnimation animDown;
	/**滑动起点Y坐标,默认值为-1*/
	private int startY=-1;
		
	/*定义上拉加载相关属性*/
	/**上拉加载更多脚布局对象*/
	private View mFooterView;
	/**上拉加载更多脚布局高度*/
	private int mFooterHeight;
	/**脚布局正在加载标志,默认值为false，true表示正在加载中*/
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
	/**初始化下拉刷新头布局*/
	private void initHeaderView() {
		this.mHeaderView=View.inflate(getContext(),R.layout.refresh_header,null);
		this.addHeaderView(mHeaderView);
		
		this.mArrow=(ImageView) this.mHeaderView.findViewById(R.id.iv_arrow);
		this.mRefreshProgressBar=(ProgressBar) this.mHeaderView.findViewById(R.id.refresh_progress);
		this.mRefreshTitle=(TextView) this.mHeaderView.findViewById(R.id.tv_refreshTitle);
		this.mRefreshTime=(TextView) this.mHeaderView.findViewById(R.id.tv_refreshTime);
		
		this.mHeaderView.measure(0,0);
		mHeaderViewHeight = this.mHeaderView.getMeasuredHeight();
		
		this.mHeaderView.setPadding(0,-this.mHeaderViewHeight,0,0);/*隐藏头布局*/
		
		initArrowAnim();/*初始化箭头动画*/
		this.mRefreshTime.setText("最后刷新时间:"+getCurrentTime());
		
	}	
	
	/**初始化上啦加载脚布局*/
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.refresh_footer,null);
		this.addFooterView(mFooterView);
		this.mFooterView.measure(0,0);
		mFooterHeight = this.mFooterView.getMeasuredHeight();
		this.mFooterView.setPadding(0,-this.mFooterHeight,0,0);/*隐藏脚布局*/
		
		this.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				/*当滑动标志位滑动空闲或者快速滑动时*/
				if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING)
				{
					/**当滑动到最后,并且脚布局没有正在加载中,
					 * 脚布局正在加载标志位确保当滑动到底后脚布局只显示一次
					 * */
					if(getLastVisiblePosition()==getCount()-1&&!isLoadingMore)
					{
						System.out.println("已经滑到底了……");
						mFooterView.setPadding(0,0,0,0);/*显示脚布局*/
						setSelection(getCount()-1);/*改变listview显示位*/
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
	
	/*重写onTouchEvent方法*/	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if(this.startY==-1)/*确保startY有效*/
			{
				startY=(int) ev.getRawY();
			}
			if(this.mCurrentRefreshState==State_Refreshing)/*当正在刷新时不做处理*/
			{
				break;
			}
			
			int endY=(int) ev.getRawY();
			int dy=endY-startY;/*移动偏移量*/
			if(dy>0&&getFirstVisiblePosition()==0)/*只有下拉并且当前是第一个item,才允许下拉*/
			{
				int padding=dy-this.mHeaderViewHeight;
				this.mHeaderView.setPadding(0,padding,0,0);/*设置当前的padding*/
				
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
			startY=-1;/*将滑动起点Y坐标恢复为默认值*/
			
			if(this.mCurrentRefreshState==State_PullToRefresh)/*当抬起手指时还处于下拉刷新的状态时*/
			{
				this.mHeaderView.setPadding(0,-this.mHeaderViewHeight,0,0);/*将头布局隐藏*/
			}
			else if(this.mCurrentRefreshState==State_ReleaseToRefresh)/*当抬起手指时还处于释放松开刷新的状态时*/
			{
				this.mCurrentRefreshState=State_Refreshing;/*将状态修改为正在刷新*/
				this.mHeaderView.setPadding(0,0,0,0);/*将头布局显示*/
				refreshState();
			}			
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**初始化箭头动画*/
	private void initArrowAnim() {
		/*箭头向上旋转动画*/
		this.animUp = new RotateAnimation(0,-180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		this.animUp.setDuration(200);/*设置动画持续时间*/
		this.animUp.setFillAfter(true);/*动画完成后保持状态*/
		
		/*箭头向下旋转动画*/
		this.animDown = new RotateAnimation(-180,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		this.animDown.setDuration(200);/*设置动画持续时间*/
		this.animDown.setFillAfter(true);/*动画完成后保持状态*/		
	}
	
	/**获取当前时间*/
	private String getCurrentTime() {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}	
	
	/**根据下拉刷新状态修改下拉头View组件*/
	private void refreshState() {
		switch(this.mCurrentRefreshState)
		{
		case State_PullToRefresh:
			this.mRefreshTitle.setText("下拉刷新");
			this.mArrow.setVisibility(View.VISIBLE);
			this.mRefreshProgressBar.setVisibility(View.INVISIBLE);
			this.mArrow.startAnimation(animDown);
			break;
		case State_ReleaseToRefresh:
			this.mRefreshTitle.setText("松开刷新");
			this.mArrow.setVisibility(View.VISIBLE);
			this.mRefreshProgressBar.setVisibility(View.INVISIBLE);
			this.mArrow.startAnimation(animUp);
			break;
		case State_Refreshing:
			this.mRefreshTitle.setText("正在刷新中……");
			this.mArrow.clearAnimation();/*必须先清除动画,才能隐藏*/
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
	
	
	
	/**刷新监听器接口*/
	public interface OnRefreshListener
	{
		public void onRefresh();
		public void onLoadMore();// 加载下一页数据
	}
	/**
	 * 刷新完成标志
	 * @param isSuccess---下拉刷新成功标志,用于修改下拉刷新时间
	 * */
	public void onRefreshComplete(boolean isSuccess)
	{
		/*处理刷新完成后脚布局相关操作,将相关属性设置为默认值*/
		if(isLoadingMore)/*正在加载更多*/
		{
			this.mFooterView.setPadding(0,-this.mFooterHeight,0,0);/*隐藏脚布局*/
			this.isLoadingMore=false;/*将脚布局正在加载标志置为false,表明此刻没有使用脚布局夹杂*/
		}
		
		/*处理刷新完成后头布局相关操作,将相关属性设置为默认值*/
		this.mCurrentRefreshState=State_PullToRefresh;/*将当前下拉刷新状态设置为默认值*/
		this.mHeaderView.setPadding(0,-this.mHeaderViewHeight,0,0);/*将头布局隐藏*/
		
		this.mRefreshTitle.setText("下拉刷新");
		if(isSuccess)
		{
			this.mRefreshTime.setText(getCurrentTime());
		}
		this.mArrow.setVisibility(View.VISIBLE);
		this.mRefreshProgressBar.setVisibility(View.INVISIBLE);
		
	}
	
	/**刷新监听器接口对象*/
	OnRefreshListener mRefreshListener;
	
	/**设置(绑定)刷新监听器*/
	public void setOnRefreshListener(OnRefreshListener listener)
	{
		this.mRefreshListener=listener;
	}


	
	/*重新封装ListView的OnItemClickListener监听器*/
	
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
