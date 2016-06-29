package com.lianbi.mezone.b.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.DialogCommon;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.AppUpDataBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.receiver.Downloader;

/**
 * 其他页面
 * 
 * @author qiuyu.lv
 * @date 2016-1-13
 * @version
 */
public class OtherActivity extends BaseActivity {
	LinearLayout other_feedback_ll, other_update_ll;
	Button other_exit_btn;
	/**
	 * 是否比更
	 */
	protected boolean mustUp = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_other, HAVETYPE);
		initView();
	}

	private void initView() {
		setPageTitle("其他");
		other_exit_btn = (Button) findViewById(R.id.other_exit_btn);
		other_feedback_ll = (LinearLayout) findViewById(R.id.other_feedback_ll);
		other_update_ll = (LinearLayout) findViewById(R.id.other_update_ll);

		other_feedback_ll.setOnClickListener(this);
		other_update_ll.setOnClickListener(this);
		other_exit_btn.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		if (view == other_feedback_ll) {// 意见反馈
			Intent intent = new Intent(this, FeedBackActivity.class);
			startActivity(intent);
		} else if (view == other_update_ll) {// 检查更新
			getUpData();
		} else if (view == other_exit_btn) {// 退出登录
			ContentUtils.putSharePre(OtherActivity.this,
					Constants.SHARED_PREFERENCE_NAME, Constants.USERHEADURL,
					userShopInfoBean.getPersonHeadUrl());

			ContentUtils.putSharePre(OtherActivity.this,
					Constants.SHARED_PREFERENCE_NAME, Constants.LOGINED_IN,
					false);

			userShopInfoBean = null;
			setResult(RESULT_OK);
			finish();
		}
	}

	/**
	 * status 1:已更新；2：自定义更新；3：必须更新
	 */
	private void getUpData() {
		final String vName = AbAppUtil.getAppVersionName(this);
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getEdition(uuid, "app", reqTime, vName,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {

							final AppUpDataBean uB = com.alibaba.fastjson.JSONObject
									.parseObject(result.getData(),
											AppUpDataBean.class);
							String status = uB.getCoerceModify();

							if (status.equals("Y")) {
								mustUp = true;
								DialogCommon dialogCommon = new DialogCommon(
										OtherActivity.this) {

									@Override
									public void onOkClick() {
										mustUp = false;
										downApp(uB);
										dismiss();
									}

									@Override
									public void onCheckClick() {
										dismiss();

									}
								};
								dialogCommon
										.setOnDismissListener(new OnDismissListener() {

											@Override
											public void onDismiss(
													DialogInterface arg0) {
												if (mustUp) {
													OtherActivity.this.exit();
												}

											}
										});
								dialogCommon.setTextTitle("必须更新了:"
										+ uB.getVersion());
								dialogCommon.setTv_dialog_common_ok("更新");
								dialogCommon
										.setTv_dialog_common_cancelV(View.GONE);
								dialogCommon.show();
							} else if(status.equals("N")){
								String edition = "V"+vName;
								if(!edition.equals(uB.getVersion())) {
									DialogCommon dialogCommon = new DialogCommon(OtherActivity.this) {

										@Override
										public void onOkClick() {
											dismiss();
											downApp(uB);
										}

										@Override
										public void onCheckClick() {
											dismiss();
										}
									};
									dialogCommon.setTextTitle("有更新了:" + uB.getVersion());
									dialogCommon.setTv_dialog_common_ok("更新");
									dialogCommon.setTv_dialog_common_cancel("取消");
									dialogCommon.show();
								}else{
									ContentUtils.showMsg(OtherActivity.this,"当前已是最新版本");
								}
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							ContentUtils.showMsg(OtherActivity.this, "获取更新失败");
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void downApp(AppUpDataBean uB) {
		String url = uB.getUrl();
		Downloader downloader = Downloader.getInstance(OtherActivity.this);
		long id = downloader.download(url);
		ContentUtils.putSharePre(OtherActivity.this,
				Constants.SHARED_PREFERENCE_NAME, Constants.APPDOWNLOAD_ID, id);
	}
}
