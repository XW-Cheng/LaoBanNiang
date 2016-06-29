package com.lianbi.mezone.b.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.com.hgh.utils.WebViewInit;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.ui.BaseActivity;

/**
 * 
 * @author guanghui.han
 * @category线上服务
 */
public class OnLineServiceFragment extends Fragment {

	private Activity mActivity;
	WebView view;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mActivity = getActivity();
		view = (WebView) inflater.inflate(R.layout.fm_onlineservicefragment,
				null);
		WebViewInit.WebSettingInit(view, mActivity);
		view.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
		view.loadUrl(API.HOST + API.TEMPLATE
				+ BaseActivity.userShopInfoBean.getUserId());
		return view;
	}




}
