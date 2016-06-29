package com.lianbi.mezone.b.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import cn.com.hgh.utils.WebViewInit;
import cn.com.hgh.view.HttpDialog;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.LoginBackBean;
import com.lianbi.mezone.b.httpresponse.API;

/**
 * 简单通用web
 * 
 * @author guanghui.han
 * 
 */

@SuppressLint("ResourceAsColor")
public class WebActivty extends BaseActivity {
	/**
	 * 传进来的标题
	 */
	private String title;
	/**
	 * 标题key
	 */
	public static String T = "title";
	/**
	 * url key
	 */
	public static String U = "url";
	/**
	 * 是否注册来
	 */
	public static boolean Re = false;
	WebView web_webactivty;
	protected HttpDialog dialog;
	String url;
	/**
	 * 需要登陆
	 */
	boolean needLogin = false;
	private final static int REQUEST_LOGIN = 4563;
	private boolean isNeedTitle = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_webactivty, NOTYPE);
		isNeedTitle = getIntent().getBooleanExtra("NEEDNOTTITLE", false);
		Re = getIntent().getBooleanExtra("Re", false);
		needLogin = getIntent().getBooleanExtra(Constants.NEDDLOGIN, false);
		title = getIntent().getStringExtra(T);
		url = getIntent().getStringExtra(U);
		initView();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		web_webactivty.loadUrl("javascript:clearCache();");

	}

	/**
	 * 初始化视图控件
	 */
	void initView() {
		if (isNeedTitle) {
			setPageTitleVisibility(View.GONE);
		} else {
			setPageTitle(title);
			if (needLogin) {
				setPageRightText("登录");
				setPageRightTextColor(R.color.colores_news_01);
			}
		}
		dialog = new HttpDialog(this);
		web_webactivty = (WebView) findViewById(R.id.web_webactivty);
		WebViewInit.WebSettingInit(web_webactivty, this);
		web_webactivty.addJavascriptInterface(new MyJs(), "LinktownB");
		web_webactivty.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				setPageTitle(view.getTitle());
				dialog.dismiss();
			}

			@Override
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
						&& view.canGoBack()) {
					view.goBack();
					return true;
				}
				return super.shouldOverrideKeyEvent(view, event);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				gobackurl = url;
				dialog.show();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				dialog.dismiss();
			}
		});
		if (TextUtils.isEmpty(url)) {
			web_webactivty.loadUrl(API.MYCOMPANEY);
		} else if (Re) {
			web_webactivty.loadUrl(url);
		} else {
			web_webactivty.loadUrl(API.HOSTWEBCUR + url);
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		Intent intent_login = new Intent(this, LoginActivity.class);
		startActivityForResult(intent_login, REQUEST_LOGIN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_LOGIN:
				LoginBackBean loginBackBean = (LoginBackBean) data.getExtras()
						.getSerializable("LoginBackBean");
				Intent intent = new Intent();
				intent.putExtra("LoginBackBean", loginBackBean);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}
		}
	}

	class MyJs {
		@JavascriptInterface
		public void getData(String type) {

		}
	}

	private String gobackurl="";
	
	@Override
	protected void onTitleLeftClick() {
//		if(gobackurl.contains("OrderInfo")){
//			web_webactivty.goBack();
//		}
//		if (isReturn) {
//			finish();
//		}
//		else if (web_webactivty.canGoBack()) {
//			web_webactivty.goBack();
//		} else {
//			finish();
//		}
		if(gobackurl.contains("OrderInfo")){
			web_webactivty.goBack();
		}else{
			finish();
		}
	}
	
	
}
