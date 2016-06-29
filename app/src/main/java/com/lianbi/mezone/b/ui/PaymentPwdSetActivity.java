package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 支付密码设置
 * 
 * @time 下午12:23:00
 * @date 2015-10-28
 * @author hongyu.yang
 * 
 */
public class PaymentPwdSetActivity extends BaseActivity {
	protected static final int CODE_SUCESS_FAILED = 6664;
	EditText edit_payment_pwd_sure, edit_payment_pwd;

	TextView tv_payment_pwd_sure;
	/*String pwd, pwd_twice, username, ownername, bankid, bankname, banknum,
			phone;*/

	boolean flag_same = true, flag_sequence = true, flag_inverted = true;
	private int key = -1;
	String pwd, pwd_twice;
    String  outerOrderId, bankId,  bankName,
	 branchBankName,  areaCode, cityCode,
      realName,  bankAccountNo,  phone,  payPasswd,bankImgUrl;
    String username,ownername,bankid,bankname,banknum;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_pwd_set, HAVETYPE);
		initView();
	}
/**
 * intent.putExtra("username", username);
		intent.putExtra("ownername", ownername);
		intent.putExtra("bankid", bankid);
		intent.putExtra("bankname", bankname);
		intent.putExtra("banknum", banknum);
		intent.putExtra("phone", phone);
		
 * 
 *      username = getIntent().getStringExtra("username");
		ownername = getIntent().getStringExtra("ownername");
		bankid = getIntent().getStringExtra("bankid");
		bankname = getIntent().getStringExtra("bankname");
		banknum = getIntent().getStringExtra("banknum");
 */
	protected void initView() {
		setPageTitle("提现密码设置");
		username = getIntent().getStringExtra("username");
		ownername = getIntent().getStringExtra("ownername");
		bankid = getIntent().getStringExtra("bankid");
		bankname = getIntent().getStringExtra("bankname");
		banknum = getIntent().getStringExtra("banknum");
		phone = getIntent().getStringExtra("phone");
		bankImgUrl = getIntent().getStringExtra("bankImgUrl");
		key = getIntent().getIntExtra("key", -1);
		edit_payment_pwd_sure = (EditText) findViewById(R.id.edit_payment_pwd_sure);
		edit_payment_pwd = (EditText) findViewById(R.id.edit_payment_pwd);
		tv_payment_pwd_sure = (TextView) findViewById(R.id.tv_payment_pwd_sure);
		tv_payment_pwd_sure.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.tv_payment_pwd_sure:
			flag_same = true;
			flag_sequence = true;
			flag_inverted = true;
			pwd = edit_payment_pwd.getText().toString().trim();
			pwd_twice = edit_payment_pwd_sure.getText().toString().trim();
			if (TextUtils.isEmpty(pwd)) {
				ContentUtils
						.showMsg(this, getString(R.string.please_input_pwd));
			} else if (pwd.toString().trim().length() < 6) {
				ContentUtils.showMsg(this,
						getString(R.string.please_input_6_pwd));
			} else if (!TextUtils.isEmpty(pwd) && TextUtils.isEmpty(pwd_twice)) {
				ContentUtils.showMsg(this,
						getString(R.string.please_input_sure_pwd));
			} else if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwd_twice)) {
				pwdVerify();
				if (!pwd.equals(pwd_twice)) {
					ContentUtils.showMsg(this,
							getString(R.string.twice_input_pwd_not));
				} else {
					if (!flag_same && !flag_sequence && !flag_inverted) {
						postBankBankAddbase();
					} else {
						ContentUtils.showMsg(this,
								getString(R.string.input_pwd_simple));
						return;
					}
				}
			}
			break;
		}
	}

	/**
	 * 提交信息，绑卡
	 */
	private void postBankBankAddbase() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.addBank(OkHttpsImp.md5_key,
					uuid, bankImgUrl,  bankname,
					userShopInfoBean.getUserId(),
					 ownername,  banknum,  phone,  pwd, pwd_twice,"01",
				     uuid,"app",reqTime,
					 new MyResultCallback<String>() {
			           
						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(PaymentPwdSetActivity.this,
									getString(R.string.add_bank_card_succeed));
							
							ContentUtils.putSharePre(PaymentPwdSetActivity.this,
									Constants.SHARED_PREFERENCE_NAME, Constants.ISBANKRETURN,
									true);
							
							Intent intent = new Intent(PaymentPwdSetActivity.this,
									Sucess_FailledActivity.class);
							if (key == -1) {
								intent.putExtra("key", 3);
							} else {
								intent.putExtra("key", key);
							}
							PaymentPwdSetActivity.this.startActivityForResult(
									intent, CODE_SUCESS_FAILED);
							
							
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

	/**
	 * 密码校验
	 */
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CODE_SUCESS_FAILED:
				setResult(RESULT_OK);
				finish();
				break;
			}
		}
	}
}
