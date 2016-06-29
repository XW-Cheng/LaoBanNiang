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
 * 支付管理 -- 修改密码
 * 
 * @time 上午9:56:40
 * @date 2015-11-17
 * @author hongyu.yang
 * 
 */
public class ChangePaymentPwdActivity extends BaseActivity {
	private GridPasswordView gpv_customUi_chang_old, gpv_customUi_chang_new,
			gpv_customUi_chang_new_sure;
	private TextView txt_change_payment_pwd;
	String oldPassWd,newPassWd,cofirmPassWd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_payment_pwd, HAVETYPE);
		bankId = getIntent().getStringExtra("bankId");
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("修改密码");
		gpv_customUi_chang_old = (GridPasswordView) findViewById(R.id.gpv_customUi_chang_old);
		gpv_customUi_chang_new = (GridPasswordView) findViewById(R.id.gpv_customUi_chang_new);
		gpv_customUi_chang_new_sure = (GridPasswordView) findViewById(R.id.gpv_customUi_chang_new_sure);
		txt_change_payment_pwd = (TextView) findViewById(R.id.txt_change_payment_pwd);
		txt_change_payment_pwd.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View v) {
		
		switch (v.getId()) {
		case R.id.txt_change_payment_pwd:
			old_pwd = gpv_customUi_chang_old.getPassWord();
			new_pwd = gpv_customUi_chang_new.getPassWord();
			new_sure_pwd = gpv_customUi_chang_new_sure.getPassWord();
			if (TextUtils.isEmpty(old_pwd) || old_pwd.length() < 6) {
				ContentUtils.showMsg(this, "请正确输入原交易密码");
				return;
			}
			if (TextUtils.isEmpty(new_pwd) || new_pwd.length() < 6) {
				ContentUtils.showMsg(this, "请正确输入新交易密码");
				return;
			}
			if (TextUtils.isEmpty(new_sure_pwd) || new_sure_pwd.length() < 6) {
				ContentUtils.showMsg(this, "请正确输入确认交易密码");
				return;
			}
			if (!new_pwd.equals(new_sure_pwd)) {
				ContentUtils.showMsg(this, "两次输入密码不一致");
				return;
			}
			if (old_pwd.equals(new_pwd)) {
				ContentUtils.showMsg(this, "新密码不能与旧密码相同");
				return;
			}
			pwdVerify();
			if (!flag_same && !flag_sequence && !flag_inverted) {
				postBankModifypwd();
			} else {
				ContentUtils
						.showMsg(this, getString(R.string.input_pwd_simple));
				return;
			}
			break;
		}
	}

	private String old_pwd, new_pwd, new_sure_pwd;
	/**
	 * 密码校验
	 */
	boolean flag_same = true, flag_sequence = true, flag_inverted = true;
	private String bankId;

	private void pwdVerify() {
		// 6个数字一样
		for (int i = 0; i < new_pwd.length() - 1; i++) {
			if (flag_same) {
				if (new_pwd.charAt(i) == new_pwd.charAt(i + 1)) {
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
		for (int i = 0; i < new_pwd.length() - 1; i++) {
			if (flag_sequence) {
				if (Integer.parseInt(new_pwd.charAt(i) + "") + 1 == Integer
						.parseInt(new_pwd.charAt(i + 1) + "")) {
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
		for (int i = 0; i < new_pwd.length() - 1; i++) {
			if (flag_inverted) {
				if (Integer.parseInt(new_pwd.charAt(i) + "") - 1 == Integer
						.parseInt(new_pwd.charAt(i + 1) + "")) {
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
	/**修改支付密码
	 * String accountNo, String product,
			String oldPassWd, String newPassWd,String cofirmPassWd
	 */
	
	
	
	private void postBankModifypwd() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.updateBankPassword(
					OkHttpsImp.md5_key,userShopInfoBean.getUserId(),"01",
					old_pwd,
					new_pwd,new_sure_pwd,
					uuid,"app",reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(ChangePaymentPwdActivity.this,
									"交易密码修改成功！");
							finish();
						}

						@Override
						public void onResponseFailed(String msg) {

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
