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
import cn.com.hgh.utils.REGX;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SmsContentObserver;
import cn.com.hgh.view.CircularImageView;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 忘记密码（重置密码）
 * 
 * @author guanghui.han
 * 
 */

public class ForgetPassWordActivty extends BaseActivity {
	private EditText register_phone_edt;
	private EditText register_validtecode;
	private EditText register_password;
	private EditText register_pwd_confirm;
	private TextView register_next_step;
	private TextView register_get_verification_code;
	private SmsContentObserver smsObserver;
	private MyCount countDown;
	private CircularImageView civ_forgetpassword_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_forgetpassword, HAVETYPE);
		initView();
		// 短信观察者
		smsObserver = new SmsContentObserver(ForgetPassWordActivty.this,
				new Handler(), register_validtecode);
		ForgetPassWordActivty.this.getContentResolver()
				.registerContentObserver(Uri.parse("content://sms/"), true,
						smsObserver);

	}

	/**
	 * 初始化视图控件
	 */
	void initView() {
		setPageTitle("找回密码");
		register_phone_edt = (EditText) findViewById(R.id.phone);
		register_validtecode = (EditText) findViewById(R.id.validtecode);
		register_password = (EditText) findViewById(R.id.password);
		register_pwd_confirm = (EditText) findViewById(R.id.pwd_confirm);
		register_next_step = (TextView) findViewById(R.id.submit);
		register_get_verification_code = (TextView) findViewById(R.id.verification_code);
		civ_forgetpassword_head = (CircularImageView) findViewById(R.id.civ_forgetpassword_head);
		String userheadurl = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.USERHEADURL);

		Glide.with(this).load(userheadurl)
				.error(R.mipmap.default_head).into(civ_forgetpassword_head);
		register_next_step.setOnClickListener(this);
		register_get_verification_code.setOnClickListener(this);
	}

	@Override
	public void onChildClick(View view) {
		switch (view.getId()) {

		case R.id.verification_code:// 获取验证码
			if (!validateForm(false)) {
				return;
			}
			// 提交验证码
			register_get_verification_code.setClickable(false);
			fetchVerifyCode();
			break;
		case R.id.submit:
			if (validateForm(true)) {
				findPassword();
			}
			break;
		}
	}

	/**
	 * 表单校对
	 * 
	 * @param resgestPhone
	 *            注册/获取验证码 true/false
	 * @return
	 */
	private boolean validateForm(boolean resgestPhone) {
		String phone = register_phone_edt.getText().toString();
		String pass = register_password.getText().toString();
		String confirmPass = register_pwd_confirm.getText().toString();

		if (TextUtils.isEmpty(phone)) {
			ContentUtils.showMsg(this, "手机号不能为空");
			return false;
		} else {
			if (!phone.matches(REGX.REGX_MOBILE)) {
				ContentUtils.showMsg(this, "请输入正确的手机号");
				return false;
			}
		}
		if (resgestPhone) {

			if (TextUtils.isEmpty(pass)) {
				ContentUtils.showMsg(this, "密码不能为空");
				return false;
			} else {
				if (pass.length() < 6) {
					ContentUtils.showMsg(this, "密码不能小于6位");
					return false;
				}

				if (TextUtils.isEmpty(confirmPass)) {
					ContentUtils.showMsg(this, "确认密码不能为空");
					return false;
				}

				if (!confirmPass.equals(pass)) {
					ContentUtils.showMsg(this, "两次输入的密码不一致");
					return false;
				}
			}

			if (TextUtils.isEmpty(register_validtecode.getText().toString()
					.trim())) {
				ContentUtils.showMsg(this, "验证码不能为空");
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取短信验证码
	 */
	void fetchVerifyCode() {
		String phone = register_phone_edt.getText().toString();
		String orderpaytime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.fetchVerifyCodeRaF("2",new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					register_get_verification_code.setClickable(false);
					countDown = new MyCount(60000, 1000);
					countDown.start();
					ContentUtils.showMsg(ForgetPassWordActivty.this,
							getString(R.string.sms_already_send));
				}

				@Override
				public void onResponseFailed(String msg) {
					register_get_verification_code.setClickable(true);
				}
			}, uuid, "app", orderpaytime, phone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 倒计时功能
	 * 
	 */
	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			register_get_verification_code.setText("请等待(" + millisUntilFinished
					/ 1000 + "s)");
			register_get_verification_code.setClickable(false);
		}

		@Override
		public void onFinish() {
			// 在这里进行设置解决时间停留的问题
			register_get_verification_code.setText("重新获取");
			register_get_verification_code.setClickable(true);
		}
	}

	/**
	 * 找回密码
	 */
	private void findPassword() {
		final String phone = register_phone_edt.getText().toString();// 手机号
		String pass = register_password.getText().toString();// 密码
		String confirmPass = register_pwd_confirm.getText().toString();
		String validateCode = register_validtecode.getText().toString();// 验证码
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.forgetPassword(uuid, "app", reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							// TODO Auto-generated method stub
							ContentUtils.showMsg(ForgetPassWordActivty.this,
									"修改密码成功");
							Intent intent = new Intent();
							intent.putExtra("username", phone);
							setResult(RESULT_OK, intent);
							finish();
						}

						@Override
						public void onResponseFailed(String msg) {
							// TODO Auto-generated method stub

						}
					}, phone, pass, validateCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (smsObserver != null) {
			ForgetPassWordActivty.this.getContentResolver()
					.unregisterContentObserver(smsObserver);
		}
	}

}
