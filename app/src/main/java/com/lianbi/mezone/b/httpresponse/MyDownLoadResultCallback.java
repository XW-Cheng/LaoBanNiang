package com.lianbi.mezone.b.httpresponse;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import android.content.Context;
import android.widget.ProgressBar;

import cn.com.hgh.utils.Result;

import com.zhy.http.okhttp.callback.FileCallBack;

public abstract class MyDownLoadResultCallback<T> extends FileCallBack {

	public MyDownLoadResultCallback(String destFileDir, String destFileName) {
		super(destFileDir, destFileName);
	}

	/**
	 * 请求的进度条
	 */
	private ProgressBar progressDialog;
	/**
	 * 请求的上下文
	 */
	private Context context;

	@Override
	public void onBefore(Request request) {
		super.onBefore(request);
	}

	@Override
	public void onResponse(File response) {

	}

	@Override
	public void onError(Call request, Exception e) {
		onResponseFailed("ERROR");
	}

	@Override
	public void onAfter() {
		super.onAfter();
	}

	@Override
	public void inProgress(float progress) {
		super.inProgress(progress);
		if (progressDialog != null) {
			progressDialog.setProgress((int) (100 * progress));
		}
	}

	/**
	 * 设置progressDialog
	 * 
	 * @param pg
	 */
	public void setDialog(ProgressBar pg) {
		if (pg != null) {
			progressDialog = pg;
		}
	}

	/**
	 * 设置上下文
	 * 
	 * @param ct
	 */
	public void setContext(Context ct) {
		if (ct != null) {
			context = ct;
		}
	}

	/**
	 * 请求成功回调
	 * 
	 * @param result
	 *            返回结果
	 */
	public abstract void onResponseResult(Result result);

	/**
	 * 请求失败回调
	 */
	public abstract void onResponseFailed(String msg);
}