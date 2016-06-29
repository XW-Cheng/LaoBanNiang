package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.com.hgh.gridpasswordview.GridPasswordView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 我的银行卡 -- 设置密码
 * 
 * @time 上午9:32:15
 * @date 2015-11-17
 * @author hongyu.yang
 * 
 */
public class SettingPaymentPwdActivity extends BaseActivity {
	private GridPasswordView gridPasswordView, gpv_customUi_sure;
	private String pwd, pwd_sure, username, code;
	TextView txt_setting_payment_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_payment_pwd, HAVETYPE);
		code = getIntent().getStringExtra("code");
		username = getIntent().getStringExtra("bank_number");
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("设置新密码");
		gridPasswordView = (GridPasswordView) findViewById(R.id.gridPasswordView);
		gpv_customUi_sure = (GridPasswordView) findViewById(R.id.gpv_customUi_sure);
		txt_setting_payment_pwd = (TextView) findViewById(R.id.txt_setting_payment_pwd);
		txt_setting_payment_pwd.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.txt_setting_payment_pwd:
			pwd = gridPasswordView.getPassWord();
			pwd_sure = gpv_customUi_sure.getPassWord();
			if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
				ContentUtils.showMsg(this, "请正确输入交易密码");
				return;
			}
			if (TextUtils.isEmpty(pwd_sure) || pwd_sure.length() < 6) {
				ContentUtils.showMsg(this, "请正确输入确认交易密码");
				return;
			}
			if (!pwd_sure.equals(pwd)) {
				ContentUtils.showMsg(this, "两次输入密码不一致");
				return;
			}
			pwdVerify();
			if (!flag_same && !flag_sequence && !flag_inverted) {
				postPwd();
			} else {
				ContentUtils
						.showMsg(this, getString(R.string.input_pwd_simple));
				return;
			}
			break;
		}
	}

	/**
	 * 密码校验
	 */
	boolean flag_same = true, flag_sequence = true, flag_inverted = true;

	private void pwdVerify() {
		// 6个数字一样
		for (int i = 0; i < pwd.length() - 1; i++) {
			if (flag_same) {
				if (pwd.charAt(i) == pwd.charAt(i + 1)) {
					flag_same = true;
				} else {
					flag_same = false;
					break;
				}
			} else {
				flag_same = false;
				break;
			}
		}
		if (flag_same) {
			flag_sequence = true;
			flag_inverted = true;
			return;
		}
		// 6个字母顺序
		for (int i = 0; i < pwd.length() - 1; i++) {
			if (flag_sequence) {
				if (Integer.parseInt(pwd.charAt(i) + "") + 1 == Integer
						.parseInt(pwd.charAt(i + 1) + "")) {
					flag_sequence = true;
				} else {
					flag_sequence = false;
					break;
				}
			} else {
				flag_sequence = false;
				break;
			}
		}
		if (flag_sequence) {
			flag_same = true;
			flag_inverted = true;
			return;
		}
		// 6个字母倒序
		for (int i = 0; i < pwd.length() - 1; i++) {
			if (flag_inverted) {
				if (Integer.parseInt(pwd.charAt(i) + "") - 1 == Integer
						.parseInt(pwd.charAt(i + 1) + "")) {
					flag_inverted = true;
				} else {
					flag_inverted = false;
					break;
				}
			} else {
				flag_inverted = false;
				break;
			}
		}
		if (flag_inverted) {
			flag_same = true;
			flag_sequence = true;
			return;
		}
	}

	/**
	 * 上传支付密码 请求接口
	 */
	

	private void postPwd() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.restPayPassWd(OkHttpsImp.md5_key,
					userShopInfoBean.getUserId(),
					pwd, pwd_sure,
					uuid,"app",reqTime,
				 new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(SettingPaymentPwdActivity.this,
									"密码设置成功");
							finish();

						}

						@Override
						public void onResponseFailed(String msg) {
							ContentUtils.showMsg(SettingPaymentPwdActivity.this,
									"密码设置失败");

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
