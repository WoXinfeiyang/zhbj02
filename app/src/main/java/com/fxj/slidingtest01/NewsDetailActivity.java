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


/**��������ҳ*/
public class NewsDetailActivity extends Activity {
	
	/**���ذ�ť*/
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	
	/**�������ִ�С��ť*/
	@ViewInject(R.id.btn_size)
	private ImageButton btnTextSize;
	
	/**����ť*/
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	
	/**���ڼ���������ҳ������*/
	@ViewInject(R.id.pb_newsLoading)
	private ProgressBar pbNewsLoading;
	
	/**WebView����*/
	@ViewInject(R.id.wv_webView)
	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);/*���ر�����*/
		setContentView(R.layout.activity_news_detail);/*���ز���*/
		
		ViewUtils.inject(this);/*��Activity��ע��View���¼�*/
		
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
		settings.setJavaScriptEnabled(true);/*֧��JavaScript*/
		settings.setBuiltInZoomControls(true);/*��ʾ�Ŵ���С��ť*/
		settings.setUseWideViewPort(true);/*֧��˫������*/
		
		this.mWebView.setWebViewClient(new WebViewClient(){
			/*��ҳ��ʼ����*/
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				System.out.println("��ʼ������,����url:"+url);
				pbNewsLoading.setVisibility(View.VISIBLE);
			} 
			/*��ҳ���ؽ���*/
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				System.out.println("��ʼ��������,����url:"+url);
				pbNewsLoading.setVisibility(View.INVISIBLE);
			}
			/*������ת�����Ӷ����ڴ˷����лص�*/
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("��תurl:"+url);
				view.loadUrl(url);
				return true;
			}
			
			
		});
		this.mWebView.setWebChromeClient(new WebChromeClient(){
			
			/*�����ȷ����仯ʱ�ص��˷���*/
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("��ǰ��ҳ���ؽ���Ϊ:"+newProgress);
				super.onProgressChanged(view, newProgress);
			}
			
			/*��ȡ��ҳ����*/
			@Override
			public void onReceivedTitle(WebView view, String title) {
				System.out.println("��ǰ��ҳ����Ϊ:"+title);
				super.onReceivedTitle(view, title);
			}			
		});
		
		this.mWebView.loadUrl(url);/*������ҳ*/
		
	}
	
	/**��¼��ǰѡ�е�item, ���ȷ��ǰ*/
	private int mCurrentChooseItem=0;	
	/**��¼��ǰѡ�е�item, ���ȷ����*/
	private int mCurrentItem=2;
	
	/**��ʾѡ������Ի���*/
	protected void showChooseDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		String[] items=new String[]{ "���������", "�������", "��������", "С������",
				"��С������" };
		
		builder.setTitle("��������");
		
		builder.setSingleChoiceItems(items, mCurrentItem,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("�Ի���ǰѡ�е�itemΪ:"+which);
				mCurrentChooseItem=which;
			}
		});
		
		builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {			
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
		
		builder.setNegativeButton("ȡ��",null);
		
		builder.show();
	}

	
	
}
