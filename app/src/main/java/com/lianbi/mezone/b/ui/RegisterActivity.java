package com.lianbi.mezone.b.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 注册页面
 * 
 * @author guanghui.han
 * 
 */

public class RegisterActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private EditText register_phone_edt;
	private EditText register_validtecode;
	private EditText register_password;
	private EditText register_pwd_confirm;
	private TextView register_next_step, tv_register_agent,
			tv_register_agent_shop;
	private TextView register_get_verification_code;
	private MyCount countDown;
	private SmsContentObserver smsObserver;
	private CheckBox checkBox;
	private CircularImageView civ_regist_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_register, HAVETYPE);
		initView();
		// 注册自动获取短信验证码
		smsObserver = new SmsContentObserver(RegisterActivity.this,
				new Handler(), register_validtecode);
		RegisterActivity.this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, smsObserver);
	}

	/**
	 * 初始化View
	 */
	void initView() {
		setPageTitle(getString(R.string.register));
		tv_register_agent = (TextView) findViewById(R.id.tv_register_agent);
		tv_register_agent_shop = (TextView) findViewById(R.id.tv_register_agent_shop);
		register_phone_edt = (EditText) findViewById(R.id.register_phone_edt);
		register_validtecode = (EditText) findViewById(R.id.register_validtecode);
		register_password = (EditText) findViewById(R.id.register_password);
		register_pwd_confirm = (EditText) findViewById(R.id.register_pwd_confirm);
		register_next_step = (TextView) findViewById(R.id.register_next_step);
		register_get_verification_code = (TextView) findViewById(R.id.register_get_verification_code);
		civ_regist_head = (CircularImageView) findViewById(R.id.civ_regist_head);

		String userheadurl = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.USERHEADURL);

		Glide.with(this).load(userheadurl)
				.error(R.mipmap.default_head).into(civ_regist_head);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		register_next_step.setOnClickListener(this);
		tv_register_agent.setOnClickListener(this);
		tv_register_agent_shop.setOnClickListener(this);
		register_get_verification_code.setOnClickListener(this);
		checkBox.setOnCheckedChangeListener(this);
		addUnderLineSpan();
	}

	/**
	 * 下划线
	 */
	private void addUnderLineSpan() {
		SpannableString spanString = new SpannableString(
				getString(R.string.click_register_agree1));
		UnderlineSpan span = new UnderlineSpan();
		spanString.setSpan(span, 0, spanString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_register_agent.append(spanString);

		SpannableString spanStringshop = new SpannableString(
				getString(R.string.click_register_agree_shop));
		UnderlineSpan spanshop = new UnderlineSpan();
		spanStringshop.setSpan(spanshop, 0, spanStringshop.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_register_agent_shop.append(spanStringshop);
	}

	/**
	 * 点击事件
	 */
	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.register_next_step:// 注册
			if (!validateForm(true)) {
				return;
			}
			userRegister();

			break;
		case R.id.register_get_verification_code:// 获取验证码
			if (!validateForm(false)) {
				return;
			}
			register_get_verification_code.setClickable(false);
			fetchVerifyCode();
			break;
		case R.id.tv_register_agent:// 使用协议
			Intent intent = new Intent(this, WebActivty.class);
			intent.putExtra(WebActivty.T,
					getString(R.string.click_register_agree1));
			intent.putExtra(WebActivty.U, API.HOST + API.PROTOCOL);
			intent.putExtra("Re", true);
			startActivity(intent);
			break;
		case R.id.tv_register_agent_shop:// 使用协议
			Intent intentshop = new Intent(this, WebActivty.class);
			intentshop.putExtra(WebActivty.T,
					getString(R.string.click_register_agree_shop));
			intentshop.putExtra(WebActivty.U, API.HOST + API.PROTOCOL_SHOP);
			intentshop.putExtra("Re", true);
			startActivity(intentshop);
			break;
		}

	}

	/**
	 * 获取短信验证码
	 */
	void fetchVerifyCode() {
		String userName = register_phone_edt.getText().toString().trim();
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.fetchVerifyCodeRaF("1",new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					register_get_verification_code.setClickable(false);
					countDown = new MyCount(60000, 1000);
					countDown.start();
					ContentUtils.showMsg(RegisterActivity.this, "验证已经发送");
				}

				@Override
				public void onResponseFailed(String msg) {
					if (countDown != null) {
						countDown.cancel();
						register_get_verification_code.setText("重新获取");
					}
					register_get_verification_code.setClickable(true);
				}
			}, uuid, "app", reqTime, userName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * 注册用户
	 */
	private void userRegister() {
		String userName = register_phone_edt.getText().toString().trim();
		final String pwd1 = register_password.getText().toString().trim();
		// final String pwd2 = register_pwd_confirm.getText().toString().trim();
		String captcha = register_validtecode.getText().toString().trim();
		String orderpaytime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.registerUser(new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					ContentUtils.showMsg(RegisterActivity.this, "注册成功!");
					String data = result.getData();
					try {
						JSONObject jb = new JSONObject(data);
						userShopInfoBean.setUserId(jb.getString("userId"));
						finish();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onResponseFailed(String msg) {
					// ContentUtils.showMsg(RegisterActivity.this, "注册失败！");
				}
			}, uuid, "app", orderpaytime, userName, pwd1, captcha);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 计时
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (smsObserver != null) {
			RegisterActivity.this.getContentResolver()
					.unregisterContentObserver(smsObserver);
		}
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onCheckedChanged(CompoundButton cb, boolean check) {
		if (check) {
			register_next_step.setBackgroundResource(R.drawable.shape_login);
			register_next_step.setClickable(true);
		} else {
			register_next_step
					.setBackgroundResource(R.drawable.shape_check_login);
			register_next_step.setClickable(false);
		}
	}

}