package com.lianbi.mezone.b.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.WebViewInit;
import cn.com.hgh.view.HttpDialog;

import com.alibaba.fastjson.JSONObject;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.ChanPinWebBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

/**
 * 产品发布
 * 
 * @author guanghui.han
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class ChanPinfbActivity extends BaseActivity {
	private MyPhotoUtills photoUtills;
	RelativeLayout relt_activity_chanpinfb;
	private ImageView imageView_activity_chanpinfb;
	WebView webView_activity_chanpinfb;
	protected HttpDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chanpinfb, NOTYPE);
		photoUtills = new MyPhotoUtills(this);
		initView();
		setLisenter();
	}

	class MyJs {
		@JavascriptInterface
		public void getData(String jStr) {
			ChanPinWebBean chanPinWebBean = JSONObject.parseObject(jStr,
					ChanPinWebBean.class);
			if (chanPinWebBean != null) {
				if (!chanPinWebBean.isStatus()) {
					ContentUtils.showMsg(ChanPinfbActivity.this,
							chanPinWebBean.getMsg());
				} else {// 发布
					File file = (File) imageView_activity_chanpinfb.getTag();
					String imageStr = "";
					if (file != null) {
						imageStr = Picture_Base64.GetImageStr(file.toString());

					} else {
						ContentUtils.showMsg(ChanPinfbActivity.this, "图片不能为空！");
						return;
					}
					okHttpsImp.addProductInfoByB(
							userShopInfoBean.getBusinessId(), imageStr,
							chanPinWebBean.getType(),
							chanPinWebBean.getServiceType(),
							chanPinWebBean.getStartTime(),
							chanPinWebBean.getEndTime(),
							chanPinWebBean.getIsPay(),
							chanPinWebBean.getProductName(),
							chanPinWebBean.getDetail(),
							chanPinWebBean.getPrice(),
							chanPinWebBean.getUnit(),
							chanPinWebBean.getTimeType(),
							new MyResultCallback<String>() {

								@Override
								public void onResponseResult(Result result) {
									ContentUtils.showMsg(
											ChanPinfbActivity.this, "产品发布成功");
									setResult(RESULT_OK);
									finish();
								}

								@Override
								public void onResponseFailed(String msg) {
									ContentUtils.showMsg(
											ChanPinfbActivity.this, "产品发布失败");

								}
							});

				}
			}
		}
	}

	private void initView() {
		setPageTitle("产品发布");
		dialog = new HttpDialog(this);
		relt_activity_chanpinfb = (RelativeLayout) findViewById(R.id.relt_activity_chanpinfb);
		imageView_activity_chanpinfb = (ImageView) findViewById(R.id.imageView_activity_chanpinfb);
		webView_activity_chanpinfb = (WebView) findViewById(R.id.webView_activity_chanpinfb);
		WebViewInit.WebSettingInit(webView_activity_chanpinfb, this);
		webView_activity_chanpinfb.addJavascriptInterface(new MyJs(),
				"LinktownB");
		webView_activity_chanpinfb.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
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
				dialog.show();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				dialog.dismiss();
			}
		});
		webView_activity_chanpinfb.loadUrl(API.WEBCUR);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView_activity_chanpinfb.clearHistory();
		webView_activity_chanpinfb.clearFormData();
		webView_activity_chanpinfb.loadUrl("javascript:clearCache();");

	}

	private void setLisenter() {
		relt_activity_chanpinfb.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		if (view == relt_activity_chanpinfb) {
			photoUtills.pickImage();
		}
	}

	@Override
	protected void onTitleLeftClick() {
		if (webView_activity_chanpinfb.canGoBack()) {
			webView_activity_chanpinfb.goBack();
		} else {
			finish();
		}
	}

	/**
	 * 处理相册返回、照相返回、裁剪返回的图片
	 */
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
		} finally {
			if (resultCode == RESULT_OK) {
				if (requestCode == PhotoUtills.REQUEST_IMAGE_FROM_ALBUM_AND_CROP) {
					Uri uri = data.getData();
					String filePath = PhotoUtills.getPath(this, uri);
					FileUtils.copyFile(filePath,
							PhotoUtills.photoCurrentFile.toString(), true);
					photoUtills.startCropImage();
					return;
				} else if (requestCode == PhotoUtills.REQUEST_IMAGE_FROM_CAMERA_AND_CROP) {
					photoUtills.startCropImage();
					return;
				}
				if (requestCode == PhotoUtills.REQUEST_IMAGE_CROP) {
					Bitmap bm = PhotoUtills.getBitmap(180,90);
					File file = photoUtills.photoCurrentFile;
					// File file = FilePathGet.saveBitmap(bm);
					onPickedPhoto(file, bm);
					return;
				}
			}
		}
	}

	private void onPickedPhoto(File photoCurrentFile, Bitmap bm) {
		imageView_activity_chanpinfb.setImageBitmap(bm);
		imageView_activity_chanpinfb.setTag(photoCurrentFile);
	}

	/**
	 * 图像裁剪实现类
	 * 
	 * @author guanghui.han
	 * 
	 */
	class MyPhotoUtills extends PhotoUtills {

		public MyPhotoUtills(Context ct) {
			super(ct);
			super.initPickView();
		}

		@Override
		protected PickImageDescribe getPickImageDescribe() {

			if (defaultImageDescribe == null) {
				defaultImageDescribe = new PickImageDescribe();
			}
			// 设置页设置头像，裁剪比例1:1
			defaultImageDescribe.setFile(photoCurrentFile);
			defaultImageDescribe.setOutputX(500);
			defaultImageDescribe.setOutputY(250);
			defaultImageDescribe.setAspectX(2);
			defaultImageDescribe.setAspectY(1);
			defaultImageDescribe.setOutputFormat(DEFAULT_IMG_FORMAT);
			return defaultImageDescribe;
		}
	}
}
