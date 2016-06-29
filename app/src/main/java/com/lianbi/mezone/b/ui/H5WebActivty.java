package com.lianbi.mezone.b.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.xizhi.mezone.b.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import Decoder.BASE64Decoder;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.WebViewInit;
import cn.com.hgh.view.HttpDialog;

/**
 * 简单通用web
 *
 * @author guanghui.han
 */

@SuppressLint("ResourceAsColor")
public class H5WebActivty extends BaseActivity {
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
	Boolean   isSave=false;

	/**
	 * 需要登陆
	 */
	boolean needLogin = false;
	private final static int REQUEST_LOGIN = 4563;
	private WebViewClient webs;
	private ValueCallback<Uri> mUploadMessage;
	private ValueCallback<Uri[]> mFilePathCallback;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private boolean isNeedTitle = false;
	private String MyMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_webactivty, NOTYPE);
		isNeedTitle = getIntent().getBooleanExtra("NEEDNOTTITLE", false);
		Re = getIntent().getBooleanExtra("Re", false);
		needLogin = getIntent().getBooleanExtra(Constants.NEDDLOGIN, false);
		url = getIntent().getStringExtra(U);
		title = getIntent().getStringExtra(T);
		webs = new WebViewClient();
		initView();

		initListener();
	}

	private void initListener() {
		boolean is = webs.shouldOverrideUrlLoading(web_webactivty, "");
		// webs.onPageStarted(view, url, favicon);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		web_webactivty.loadUrl("javascript:clearCache();");

	}

	//base64字符串转化成图片
	public   void GenerateImage(String imgStr)
	{   //对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null){ //图像数据为空
			return;}
		BASE64Decoder decoder = new BASE64Decoder();
		try
		{
			//Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for(int i=0;i<b.length;++i)
			{
				if(b[i]<0)
				{//调整异常数据
					b[i]+=256;
				}
			}
			//生成jpeg图片
			File appDir = new File(Environment.getExternalStorageDirectory(),
					"invitationcode");
			if (!appDir.exists()) {
				appDir.mkdir();
			}
			String imgFilePath = System.currentTimeMillis() + ".jpg";
			File file = new File(appDir, imgFilePath);
			OutputStream out = new FileOutputStream(file);
//			out.write(b);
			out.write(b,0,b.length);
			out.flush();
			out.close();
			// 其次把文件插入到系统图库
			try {
				MediaStore.Images.Media.insertImage(this.getContentResolver(),
						file.getAbsolutePath(), imgFilePath, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 最后通知图库更新
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.fromFile(new File(file.getPath()))));
			ContentUtils.showMsg(H5WebActivty.this, "保存图片成功");
			isSave=true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
//	public String getString(String str,String str1 ){
//		int index = str1.indexOf(str); //str1是想要开始截取的字符。str是被截取的字符。
//		return str.substring(index+1,str.length());
//	}
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


		if (url.contains("storeId")) {
			String bb[] = url.split("storeId");
			MyMsg = API.TOSTORESERVICE + "/wcm/sws/businessInfo/goBusinessInfo?storeId" + bb[1];
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
				if(indexOfString(Uri.parse(url).toString(),"data:image/octet-stream")){
					String  str=Uri.parse(url).toString();
					String jieguo = str.
								substring(str.indexOf(",")+1,
										str.length());
//					GenerateImage(jieguo+"8Agnx+0");
					if(isSave==true){
						ContentUtils.showMsg(H5WebActivty.this, "已保存");
					}else {
						GenerateImage(jieguo);
					}

				}
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

		web_webactivty.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onShowFileChooser(WebView webView,
											 ValueCallback<Uri[]> filePathCallback,
											 FileChooserParams fileChooserParams) {
				mFilePathCallback = filePathCallback;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "File Browser"),
						20000);
				return true;
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// showTitle(title);
			}

			// The undocumented magic method override
			// Eclipse will swear at you if you try to put @Override here
			// For Android 3.0+

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {

				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				H5WebActivty.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);

			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback uploadMsg,
										String acceptType) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				H5WebActivty.this.startActivityForResult(
						Intent.createChooser(i, "File Browser"),
						FILECHOOSER_RESULTCODE);
			}

			// For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
										String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				H5WebActivty.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);

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
	/*
         * 判断字符串是否包含一些字符 indexOf
         */
	public static boolean indexOfString(String src, String dest) {
		boolean flag = false;
		if (src.indexOf(dest)!=-1) {
			Log.i("trg","包含该字符");
			flag = true;
		}
		return flag;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage)
				return;
			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			if (result == null) {
				mUploadMessage.onReceiveValue(null);
				mUploadMessage = null;
				return;
			}
			if ("content".equals(result.getScheme())) {
				String path = PhotoUtills.getPath(this, result);
				// String path = ChoosePictureUtil.getRealPathFromURI(this,
				// result);
				if (path != null)
					result = Uri.fromFile(new File(path));
			}
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
		if (requestCode != 20000 || mFilePathCallback == null) {
			return;
		}
		Uri[] results = null;
		if (resultCode == Activity.RESULT_OK
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (intent == null) {
			} else {
				String dataString = intent.getDataString();
				ClipData clipData = intent.getClipData();
				if (clipData != null) {
					results = new Uri[clipData.getItemCount()];
					for (int i = 0; i < clipData.getItemCount(); i++) {
						ClipData.Item item = clipData.getItemAt(i);
						results[i] = item.getUri();
					}
				}
				if (dataString != null)
					results = new Uri[]{Uri.parse(dataString)};
			}
		}
		mFilePathCallback.onReceiveValue(results);
		mFilePathCallback = null;
	}

	class MyJs {
		@JavascriptInterface
		public void getData(String type) {

		}
	}

	private String gobackurl = "";


	@Override
	protected void onTitleLeftClick() {

		if (gobackurl.contains("getAllBannerInfo")
				|| gobackurl.contains("getAllProduct")
				|| gobackurl.contains("addProduct")
				|| gobackurl.contains("getProductById") || gobackurl.contains("sws/showOrderDetl") || gobackurl.contains("productsList")
				|| gobackurl.contains("goInvite") || gobackurl.contains("goBusinessInfo")) {
			web_webactivty.loadUrl(url);//返回一级目录
		} else if (gobackurl.contains("viewMyAuthenticationMsg")) {
			web_webactivty.loadUrl(MyMsg);
		} else if (gobackurl.contains("enterIntoProductManager") || gobackurl.contains("showMenu")) {
			finish();//退出
		} else {
			web_webactivty.goBack();//返回
		}
		// if (web_webactivty.canGoBack()) {
		// web_webactivty.goBack();
		// } else {
		// finish();
		// }
	}

}
