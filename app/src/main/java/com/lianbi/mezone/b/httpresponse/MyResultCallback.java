package com.lianbi.mezone.b.httpresponse;

import okhttp3.Call;
import okhttp3.Request;
import android.content.Context;
import android.text.TextUtils;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.HttpDialog;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.callback.StringCallback;

public abstract class MyResultCallback<T> extends StringCallback {
	/**
	 * 请求的dialog
	 */
	private HttpDialog progressDialog;
	/**
	 * 请求的上下文
	 */
	public Context context;

	public static boolean isShow = true;

	@Override
	public void onBefore(Request request) {
		super.onBefore(request);
		showDialog();
	}

	public void showDialog() {
		try {
			if (progressDialog != null) {
				progressDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResponse(String responseresult) {
		System.out.println("服务器返回数据----->" + (String) responseresult);
		if (!TextUtils.isEmpty(responseresult)) {
			try {
				Result r = JSON.parseObject((String) responseresult,
						Result.class);
				if (r != null) {
					if (r.getCode() == Result.TWO00) {
						onResponseResult(r);
					} else {
						// 返回结果的判断
						onResponseFailed("RESULTERROR");
						ContentUtils.showMsg(context, r.getMsg());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onError(Call call, Exception e) {
		if (isShow) {
			isShow = false;
			// ContentUtils.showMsg(context, "请求超时！");
		}
		onResponseFailed("ERROR");
	}

	@Override
	public void onAfter() {
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 设置progressDialog
	 * 
	 * @param pg
	 */
	public void setDialog(String pg) {
		progressDialog = new HttpDialog(context);
		if (!TextUtils.isEmpty(pg)) {
			progressDialog.setMessage(pg);
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