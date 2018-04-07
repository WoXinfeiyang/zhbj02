package com.fxj.slidingtest01;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;


/**新闻详情页*/
public class NewsDetailActivity extends Activity {
	
	/**返回按钮*/
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	
	/**设置文字大小按钮*/
	@ViewInject(R.id.btn_size)
	private ImageButton btnTextSize;
	
	/**分享按钮*/
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	
	/**正在加载新闻网页进度条*/
	@ViewInject(R.id.pb_newsLoading)
	private ProgressBar pbNewsLoading;
	
	/**WebView对象*/
	@ViewInject(R.id.wv_webView)
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);/*隐藏标题栏*/
		setContentView(R.layout.activity_news_detail);/*加载布局*/
		
		ViewUtils.inject(this);/*在Activity中注入View和事件*/
		
		this.btnBack.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		this.btnTextSize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showChooseDialog();
			}
		});
		
		this.btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	
		String url=getIntent().getStringExtra("url");
		
		WebSettings settings=this.mWebView.getSettings();
		settings.setJavaScriptEnabled(true);/*支持JavaScript*/
		settings.setBuiltInZoomControls(true);/*显示放大缩小按钮*/
		settings.setUseWideViewPort(true);/*支持双击缩放*/
		
		this.mWebView.setWebViewClient(new WebViewClient(){
			/*网页开始加载*/
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				System.out.println("开始加载中,加载url:"+url);
				pbNewsLoading.setVisibility(View.VISIBLE);
			} 
			/*网页加载结束*/
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				System.out.println("开始结束加载,加载url:"+url);
				pbNewsLoading.setVisibility(View.INVISIBLE);
			}
			/*所有跳转的链接都会在此方法中回调*/
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("跳转url:"+url);
				view.loadUrl(url);
				return true;
			}
			
			
		});
		this.mWebView.setWebChromeClient(new WebChromeClient(){
			
			/*当进度发生变化时回到此方法*/
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("当前网页加载进度为:"+newProgress);
				super.onProgressChanged(view, newProgress);
			}
			
			/*获取网页标题*/
			@Override
			public void onReceivedTitle(WebView view, String title) {
				System.out.println("当前网页标题为:"+title);
				super.onReceivedTitle(view, title);
			}			
		});
		
		this.mWebView.loadUrl(url);/*加载网页*/
		
	}
	
	/**记录当前选中的item, 点击确定前*/
	private int mCurrentChooseItem=0;	
	/**记录当前选中的item, 点击确定后*/
	private int mCurrentItem=2;
	
	/**显示选择字体对话框*/
	protected void showChooseDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		String[] items=new String[]{ "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		
		builder.setTitle("字体设置");
		
		builder.setSingleChoiceItems(items, mCurrentItem,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("对话框当前选中的item为:"+which);
				mCurrentChooseItem=which;
			}
		});
		
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings setting=mWebView.getSettings();
				switch(mCurrentChooseItem)
				{
				case 0:
					setting.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					setting.setTextSize(TextSize.LARGER);
					break;
				case 2:
					setting.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					setting.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					setting.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}
				mCurrentItem=mCurrentChooseItem;
			}
		});
		
		builder.setNegativeButton("取消",null);
		
		builder.show();
	}

	
	
}
