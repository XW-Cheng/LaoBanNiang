package com.lianbi.mezone.b.ui;


import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.xizhi.mezone.b.R;

import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.WebViewInit;
import cn.com.hgh.view.HttpDialog;

public class MallIntroduceActivity extends BaseActivity {

	HttpDialog dialog;
	private  TextView tv_download;
	private  WebView web_WebView;

	private Intent  getIntent;;
	private String  serviceId="";
	private String  introduceurl="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_mallintroduceactivity, NOTYPE);
		initViewAndData();
		initListen();
	}
	private void initViewAndData() {
		setPageTitle("介绍");
		tv_download = (TextView) findViewById(R.id.tv_download);//点击下载
		web_WebView = (WebView) findViewById(R.id.web_mallintroduce);
		getIntent=getIntent();
		serviceId=getIntent.getStringExtra("serviceid");
		introduceurl=getIntent.getStringExtra("introduceurl");
		WebViewInit.WebSettingInit(web_WebView, this);
		web_WebView.loadUrl(introduceurl);
	}
	private void initListen() {
		tv_download.setOnClickListener(this);
	}
	@Override
	protected void onTitleLeftClick() {
		Intent intent = new Intent(this,ServiceMallActivity.class);
		setResult(RESULT_OK, intent);
		super.onTitleLeftClick();
	}
	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		getDownLoad();
	}
	private void  getDownLoad(){
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
			 goDownloadMall(serviceId);
			
			}
		}, 1000);
    }
	private void  goDownloadMall(String  serviceId){
		okHttpsImp.getdownloadServer(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag","成功");
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
					ContentUtils.showMsg(MallIntroduceActivity.this, "下载成功");
					tv_download.setCompoundDrawables(null, null,null,null);
					tv_download.setText("已下载");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag","失败"+msg);
				dialog.dismiss();
			}
		}, userShopInfoBean.getBusinessId(),serviceId);
		
	}
}
