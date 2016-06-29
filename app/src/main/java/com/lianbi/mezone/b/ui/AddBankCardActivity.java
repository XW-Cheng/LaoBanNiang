package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.DialogCommon;
import cn.com.hgh.view.EditJDialogCommon;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.BankBoundInfo;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 银行卡界面
 * 
 * @author hongyu.yang
 * 
 */
public class AddBankCardActivity extends BaseActivity {
	private static final int CODE_NO_CARD = 6666;
	RelativeLayout layout_no_my_bank_card, activity_add_bank_card_password_re;
	LinearLayout layout_my_bank_card;
	ImageView img_my_bank_card;
	TextView tv_my_bank_card_name, tv_my_bank_card_number;
	TextView activity_add_bank_card_tv_open_del,
			activity_add_bank_card_tv_forgetpassword,
			activity_add_bank_card_tv_respassword;
	boolean isBand = false;// 用户是否绑定银行卡
	String outerOrderId;
	BankBoundInfo bankBoundInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_bank_card, HAVETYPE);
		initView();
		setLisenter();
	}
    
	/**
	 * 获取银行卡
	 */
	private void getBankList() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getBankList(OkHttpsImp.md5_key,
					userShopInfoBean.getUserId(),"01",
					uuid,"app",reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							try {
//								JSONObject jsonObject = new JSONObject(resString);
//								resString = jsonObject.getString("bankList");
								BankBoundInfo bankBoundInfos = (BankBoundInfo) JSON
										.parseObject(resString, BankBoundInfo.class);
								if (bankBoundInfos != null) {
									isBand = true;
//									bankBoundInfo = bankBoundInfos.get(0);
									bankBoundInfo = bankBoundInfos;
									updateView(bankBoundInfos);
								} else {
									isBand = false;
									isBandViser();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
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
	 * 添加监听
	 */
	private void setLisenter() {
		layout_no_my_bank_card.setOnClickListener(this);
		img_my_bank_card.setOnClickListener(this);
		activity_add_bank_card_tv_open_del.setOnClickListener(this);
		activity_add_bank_card_tv_forgetpassword.setOnClickListener(this);
		activity_add_bank_card_tv_respassword.setOnClickListener(this);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		layout_no_my_bank_card = (RelativeLayout) findViewById(R.id.layout_no_my_bank_card);
		activity_add_bank_card_password_re = (RelativeLayout) findViewById(R.id.activity_add_bank_card_password_re);
		layout_my_bank_card = (LinearLayout) findViewById(R.id.layout_my_bank_card);
		img_my_bank_card = (ImageView) findViewById(R.id.img_my_bank_card);
		tv_my_bank_card_name = (TextView) findViewById(R.id.tv_my_bank_card_name);
		tv_my_bank_card_number = (TextView) findViewById(R.id.tv_my_bank_card_number);
		activity_add_bank_card_tv_open_del = (TextView) findViewById(R.id.activity_add_bank_card_tv_open_del);
		activity_add_bank_card_tv_forgetpassword = (TextView) findViewById(R.id.activity_add_bank_card_tv_forgetpassword);
		activity_add_bank_card_tv_respassword = (TextView) findViewById(R.id.activity_add_bank_card_tv_respassword);
		isBand = getIntent().getBooleanExtra("isBand", false);
		if (isBand) {
			getBankBankque();
		} else {
			isBandViser();
		}
	}

	private void updateView(BankBoundInfo boundInfo) {
		isBandViser();
		String num = boundInfo.getBanknum();
//		int size = num.length();
//		String num1 = num.substring(0, 4) + " ";
//		String num3 = " " + num.substring(size - 4, size);
//		num = num1 + "**** ****" + num3;
		tv_my_bank_card_name.setText(boundInfo.getBankname());
		tv_my_bank_card_number.setText(num);
		Glide.with(AddBankCardActivity.this).load(boundInfo.getImgurl())
				.error(R.mipmap.bankdefault).error(R.mipmap.bankdefault)
				.into(img_my_bank_card);
	}

	private void isBandViser() {
		if (isBand) {
			setPageTitle(getString(R.string.bank_card));
			layout_no_my_bank_card.setVisibility(View.GONE);
			layout_my_bank_card.setVisibility(View.VISIBLE);
			activity_add_bank_card_password_re.setVisibility(View.VISIBLE);
		} else {
			layout_no_my_bank_card.setVisibility(View.VISIBLE);
			layout_my_bank_card.setVisibility(View.GONE);
			activity_add_bank_card_password_re.setVisibility(View.GONE);
			setPageTitle("添加银行卡");
		}
	}
   
	/**
	 * 根据 username 查询绑定信息
	 */
	private void getBankBankque() {
		getBankList();
	}
           
	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.activity_add_bank_card_tv_open_del:// 解除绑定
			DialogCommon dialogCommon = new DialogCommon(this) {

				@Override
				public void onOkClick() {
					EditJDialogCommon dialogInputPassword = new EditJDialogCommon(
							AddBankCardActivity.this) {

						@Override
						public void onOkClick() {
							String pwd = getInputPass();
							if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
								ContentUtils.showMsg(AddBankCardActivity.this,
										getString(R.string.please_input_6_pwd));
							} else {
								deleteBankCard(pwd);
								dismiss();
							}
						}

						@Override
						public void onCheckClick() {
							dismiss();
						}
					};
					dialogInputPassword.show();
					dismiss();
				}

				@Override
				public void onCheckClick() {
					dismiss();
				}
			};
			dialogCommon
					.setTextTitle(getString(R.string.yes_or_no_delete_bankcard));
			dialogCommon.show();

			break;
		case R.id.layout_no_my_bank_card:
			Intent intent = new Intent(this, AddNewBankCardActivity.class);
			startActivityForResult(intent, CODE_NO_CARD);
			break;
		case R.id.activity_add_bank_card_tv_forgetpassword:// 忘记密码
			Intent intentw = new Intent(this, ForgetPaymentPwdActivity.class);
		
			startActivity(intentw);
			break;
		case R.id.activity_add_bank_card_tv_respassword:// 修改密码
			if (bankBoundInfo == null) {
				return;
			}
			Intent intentx = new Intent(this, ChangePaymentPwdActivity.class);
			intentx.putExtra("bankId", bankBoundInfo.getBankid() + "");
			startActivity(intentx);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CODE_NO_CARD:
				getBankBankque();
				break;
			}
		}
	}

	/**
	 * 删除银行卡String outerOrderId, String accountNo, String passWd,String product,
	 */
	private void deleteBankCard(String pwd) {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		String uuid2 = AbStrUtil.getUUID();

		if (bankBoundInfo == null) {
			return;
		}
		
		try {
			okHttpsImp.delBank(OkHttpsImp.md5_key,
					uuid2, userShopInfoBean.getUserId(),pwd,"01",
					uuid,"app",reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							isBand = false;
							isBandViser();
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
