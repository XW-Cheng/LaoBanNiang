package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SmsContentObserver;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

public class ForgetPaymentPwdActivity extends BaseActivity {
	EditText edit_forget_payment_number, edit_forget_payment_code;
	TextView tv_forget_payment_next, tv_forget_payment_send;
	private String bank_number, code, username;
	private MyCount countDown;
	private SmsContentObserver smsObserver;
	private Handler mHandler;
	String product, smsCode, phone;
	private String code2;
	private String TLphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_payment_pwd, HAVETYPE);
		initView();
		initData();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("忘记密码");
		edit_forget_payment_number = (EditText) findViewById(R.id.edit_forget_payment_number);
		edit_forget_payment_code = (EditText) findViewById(R.id.edit_forget_payment_code);
		tv_forget_payment_next = (TextView) findViewById(R.id.tv_forget_payment_next);
		tv_forget_payment_send = (TextView) findViewById(R.id.tv_forget_payment_send);
		tv_forget_payment_next.setOnClickListener(this);
		tv_forget_payment_send.setOnClickListener(this);
	}

	/**
	 * 注册短信监测
	 */
	protected void initData() {
		mHandler = new Handler();
		smsObserver = new SmsContentObserver(ForgetPaymentPwdActivity.this,
				mHandler, edit_forget_payment_code);
		ForgetPaymentPwdActivity.this.getContentResolver()
				.registerContentObserver(Uri.parse("content://sms/"), true,
						smsObserver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (smsObserver != null) {
			ForgetPaymentPwdActivity.this.getContentResolver()
					.unregisterContentObserver(smsObserver);
		}
	}

	/**
	 * 点击事件
	 * 
	 * @param v
	 */
	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.tv_forget_payment_next:// 下一步
			bank_number = edit_forget_payment_number.getText().toString()
					.trim();
			code = edit_forget_payment_code.getText().toString().trim();
			if (TextUtils.isEmpty(bank_number) || bank_number.length() < 16) {
				ContentUtils.showMsg(this, "请正确输入银行卡帐号");
				return;
			}
			if (TextUtils.isEmpty(code) || code.length() < 6) {
				ContentUtils.showMsg(this, "请正确输入验证码");
				return;
			}
			//
			CheckPasswordCode();

			break;

		case R.id.tv_forget_payment_send:// 发送验证码
			bank_number = edit_forget_payment_number.getText().toString()
					.trim();
			if (TextUtils.isEmpty(bank_number) || bank_number.length() < 16) {
				ContentUtils.showMsg(this, "请正确输入银行卡帐号");
				return;
			}
			tv_forget_payment_send.setClickable(false);
			getBankList(); // 查询绑卡信息

			break;
		}
	}

	/**
	 * 查询绑卡信息
	 * 
	 */
	private void getBankList() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();

		try {
			okHttpsImp.getBankList(OkHttpsImp.md5_key,
					userShopInfoBean.getUserId(), "01", uuid, "app", reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							fetchVerifyCode();// 获取验证码

						}

						@Override
						public void onResponseFailed(String msg) {

						}

					});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 短信校验
	 */
	private void CheckPasswordCode() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		phone = ContentUtils.getSharePreString(ForgetPaymentPwdActivity.this,
				Constants.SHARED_PREFERENCE_NAME, Constants.ADDBANKCARPHONE);
		try {
			okHttpsImp.CheckUpdatePasswordCode(OkHttpsImp.md5_key, 
					code,TLphone,
					uuid, "app", reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reString =result.getData();
							postBankForgetSubmitCaptcha(code);
							ContentUtils.showMsg(ForgetPaymentPwdActivity.this,
									"验证码验证成功");
						}

						@Override
						public void onResponseFailed(String msg) {

							ContentUtils.showMsg(ForgetPaymentPwdActivity.this,
									"验证码验证失败");
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 我的银行卡 提交验证码
	 * 
	 * @param code2
	 */
	private void postBankForgetSubmitCaptcha(String code) {
		Intent intent = new Intent(ForgetPaymentPwdActivity.this,
				SettingPaymentPwdActivity.class);
		intent.putExtra("code", code);
		intent.putExtra("bank_number", bank_number);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * 
	 */

	/**
	 * 获取验证码,
	 */
	private void fetchVerifyCode() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.sendUpdatePasswordCode(OkHttpsImp.md5_key,
					userShopInfoBean.getUserId(), bank_number, "01", uuid,
					"app", reqTime, new MyResultCallback<String>() {
						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							TLphone = resString;
							tv_forget_payment_send.setClickable(false);
							countDown = new MyCount(60000, 1000);
							countDown.start();
							ContentUtils.showMsg(ForgetPaymentPwdActivity.this,
									"验证码已经发送");
						}

						@Override
						public void onResponseFailed(String msg) {
							if (countDown != null) {
								countDown.cancel();
								tv_forget_payment_send.setText("重新获取");
							}
							tv_forget_payment_send.setClickable(true);

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_forget_payment_send.setText("请等待(" + millisUntilFinished / 1000
					+ "s)");
			tv_forget_payment_send.setClickable(false);
		}

		@Override
		public void onFinish() {
			// 在这里进行设置解决时间停留的问题
			tv_forget_payment_send.setText("重新获取");
			tv_forget_payment_send.setClickable(true);
		}
	}
}
