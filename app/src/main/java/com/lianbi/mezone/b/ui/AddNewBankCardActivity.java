package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.REGX;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;

/**
 * 添加银行卡
 * 
 * @time 下午12:23:12
 * @date 2015-10-28
 * @author hongyu.yang
 * 
 */
public class AddNewBankCardActivity extends BaseActivity {
	protected static final int CODE_PAYMENT_PWD = 6665;
	private static final int CODE_BABKLIST = 6667;
	EditText edit_add_new_bcard_cardholder, edit_add_new_bcard_cardnumber,
			edit_add_new_bcard_phonenumber;
	LinearLayout layout_add_new_bcard_opening_bank, layout_add_new_bank_card;

	TextView tv_add_new_bcard_next_step, tv_add_new_bcard_opening_bank;
	private String username, ownername, bankid, bankname, banknum, phone,
			bankImgUrl;
	private int key = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_bank_card, HAVETYPE);
		initView();
		setLisenter();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		layout_add_new_bcard_opening_bank.setOnClickListener(this);
		layout_add_new_bank_card.setOnClickListener(this);
		tv_add_new_bcard_next_step.setOnClickListener(this);
		tv_add_new_bcard_opening_bank.setOnClickListener(this);
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("添加银行卡");
		key = getIntent().getIntExtra("key", -1);
		edit_add_new_bcard_cardholder = (EditText) findViewById(R.id.edit_add_new_bcard_cardholder);
		edit_add_new_bcard_cardnumber = (EditText) findViewById(R.id.edit_add_new_bcard_cardnumber);
		edit_add_new_bcard_phonenumber = (EditText) findViewById(R.id.edit_add_new_bcard_phonenumber);
		layout_add_new_bcard_opening_bank = (LinearLayout) findViewById(R.id.layout_add_new_bcard_opening_bank);
		layout_add_new_bank_card = (LinearLayout) findViewById(R.id.layout_add_new_bank_card);
		tv_add_new_bcard_next_step = (TextView) findViewById(R.id.tv_add_new_bcard_next_step);
		tv_add_new_bcard_opening_bank = (TextView) findViewById(R.id.tv_add_new_bcard_opening_bank);
	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add_new_bcard_next_step:
			verify();
			break;
		case R.id.tv_add_new_bcard_opening_bank:
			Intent intent = new Intent(this, BankListActivity.class);
			startActivityForResult(intent, CODE_BABKLIST);
			break;
		case R.id.layout_add_new_bcard_opening_bank:
			Intent intent1 = new Intent(this, BankListActivity.class);
			startActivityForResult(intent1, CODE_BABKLIST);
			break;
		}
	}

	/**
	 * 校验
	 */
	private void verify() {
		ownername = edit_add_new_bcard_cardholder.getText().toString().trim();
		bankname = tv_add_new_bcard_opening_bank.getText().toString();
		banknum = edit_add_new_bcard_cardnumber.getText().toString().trim();
		phone = edit_add_new_bcard_phonenumber.getText().toString().trim();
		if (TextUtils.isEmpty(ownername)) {
			ContentUtils.showMsg(AddNewBankCardActivity.this,
					getString(R.string.input_card_holder_name));
			return;
		}
		if (TextUtils.isEmpty(bankname)) {
			ContentUtils.showMsg(AddNewBankCardActivity.this,
					getString(R.string.select_opening_bank));
			return;
		}
		if (TextUtils.isEmpty(banknum)) {
			ContentUtils.showMsg(AddNewBankCardActivity.this,
					getString(R.string.input_card_number));
			return;
		}
		if (banknum.length() < 16) {
			ContentUtils.showMsg(AddNewBankCardActivity.this,
					getString(R.string.bank_card_16));
			return;
		}
		phone = edit_add_new_bcard_phonenumber.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			ContentUtils.showMsg(AddNewBankCardActivity.this,
					getString(R.string.input_right_phone));
			return;
		}
		if (null != phone && !"".equals(phone)) {
			if (!phone.matches(REGX.REGX_MOBILE_FINAL)) {
				ContentUtils.showMsg(AddNewBankCardActivity.this,
						getString(R.string.input_right_phone));
				return;
			}
		}
		ContentUtils.putSharePre(AddNewBankCardActivity.this,
				Constants.SHARED_PREFERENCE_NAME, Constants.ADDBANKCARPHONE,
				phone);
		Intent intent = new Intent(AddNewBankCardActivity.this,
				PaymentPwdSetActivity.class);
		intent.putExtra("username", username);
		intent.putExtra("ownername", ownername);// 持卡人
		intent.putExtra("bankid", bankid);// 银行ID
		intent.putExtra("bankname", bankname);// 银行名称
		intent.putExtra("banknum", banknum);// 卡号
		intent.putExtra("phone", phone);// 手机号
		intent.putExtra("bankImgUrl", bankImgUrl);

		intent.putExtra("key", key);
		AddNewBankCardActivity.this.startActivityForResult(intent,
				CODE_PAYMENT_PWD);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CODE_PAYMENT_PWD:
				setResult(RESULT_OK);
				finish();
				break;
			case CODE_BABKLIST:
				bankid = data.getIntExtra("bankid", -1) + "";
				bankname = data.getStringExtra("bankname");
				bankImgUrl = data.getStringExtra("bankImg");
				tv_add_new_bcard_opening_bank.setText(bankname);
				break;
			}
		}
	}
}
