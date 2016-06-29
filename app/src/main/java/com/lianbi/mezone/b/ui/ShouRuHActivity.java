package com.lianbi.mezone.b.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.EditTextUtills;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.EditJDialogCommon;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.BankBoundInfo;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 提现
 * 
 * @time 下午6:18:46
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class ShouRuHActivity extends BaseActivity {
	TextView shouruhactivity_tv_band, shouruhactivity_tv_tijiao,
			tv_my_bank_card_name, tv_my_bank_card_number,
			shouruhactivity_tv_numtotale;
	EditText shouruhactivity_et_num;
	ImageView img_my_bank_card;
	LinearLayout shouruhactivity_ll_noband, shouruhactivity_ll_band;
	EditJDialogCommon dialogInputPassword;
	protected double totalamount;
	protected BankBoundInfo bankBoundInfo;
	String outerOrderId;
	BigDecimal amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shouruhactivity, HAVETYPE);
		initView();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("提现");
		shouruhactivity_tv_band = (TextView) findViewById(R.id.shouruhactivity_tv_band);
		shouruhactivity_tv_tijiao = (TextView) findViewById(R.id.shouruhactivity_tv_tijiao);
		tv_my_bank_card_name = (TextView) findViewById(R.id.tv_my_bank_card_name);
		tv_my_bank_card_number = (TextView) findViewById(R.id.tv_my_bank_card_number);
		shouruhactivity_tv_numtotale = (TextView) findViewById(R.id.shouruhactivity_tv_numtotale);
		shouruhactivity_et_num = (EditText) findViewById(R.id.shouruhactivity_et_num);
		img_my_bank_card = (ImageView) findViewById(R.id.img_my_bank_card);
		shouruhactivity_ll_noband = (LinearLayout) findViewById(R.id.shouruhactivity_ll_noband);
		shouruhactivity_ll_band = (LinearLayout) findViewById(R.id.shouruhactivity_ll_band);
		EditTextUtills.setPricePoint(shouruhactivity_et_num);
		isBand = getIntent().getBooleanExtra("isBand", false);
		setVisble();
		setLisenter();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		shouruhactivity_tv_band.setOnClickListener(this);
		shouruhactivity_tv_tijiao.setOnClickListener(this);
		tv_my_bank_card_name.setOnClickListener(this);
		tv_my_bank_card_number.setOnClickListener(this);
		shouruhactivity_tv_numtotale.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.shouruhactivity_tv_band:
			Intent intent = new Intent(this, AddNewBankCardActivity.class);
			intent.putExtra("key", 4);
			startActivity(intent);
			break;
		case R.id.shouruhactivity_tv_tijiao:
			String amount = shouruhactivity_et_num.getText().toString().trim();
			if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
				Toast.makeText(
						ShouRuHActivity.this,
						getResources().getString(
								R.string.input_card_many_number),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (amount.endsWith(".")) {
				ContentUtils.showMsg(this, "请正确输入提现金额");
				return;
			}
			totalamount = Double.parseDouble(shouruhactivity_tv_numtotale
					.getText().toString().trim());
			if (Double.parseDouble(amount) - totalamount > 0) {
				Toast.makeText(
						ShouRuHActivity.this,
						getResources().getString(
								R.string.input_card_many_numberbig),
						Toast.LENGTH_SHORT).show();
				return;
			}
			tijiao();
			break;
		}
	}

	/**
	 * 根据username取总金额
	 */
	private void getBankGettotalmount() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getBalance(OkHttpsImp.md5_key, uuid, "app", reqTime,
					userShopInfoBean.getUserId(),
					userShopInfoBean.getBusinessId(),
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
//							String resString = result.getData();
							totalamount = BigDecimal
									.valueOf(Long.valueOf(result.getData()))
									.divide(new BigDecimal(100)).doubleValue();
							
							shouruhactivity_tv_numtotale.setText(totalamount
									+ "");
						}

						@Override
						public void onResponseFailed(String msg) {
							shouruhactivity_tv_numtotale.setText(0 + "");
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	/**
//	 * 根据 username 查询绑定信息
//	 */
//	private void getBankBankque() {
//		String reqTime = AbDateUtil.getDateTimeNow();
//		String uuid = AbStrUtil.getUUID();
//
//		try {
//			okHttpsImp.getBankList(OkHttpsImp.md5_key,
//
//			userShopInfoBean.getUserId(), "01", uuid, "app", reqTime,
//					new MyResultCallback<String>() {
//
//						@Override
//						public void onResponseResult(Result result) {
//							String resString = result.getData();
//							System.out.println("绑定信息--------" + resString);
//							try {
//								BankBoundInfo bankBoundInfos = (BankBoundInfo) JSON
//										.parseObject(resString,
//												BankBoundInfo.class);
//								if (bankBoundInfos != null) {
//									isBand = true;
//									// bankBoundInfo = bankBoundInfos.get(0);
//									bankBoundInfo = bankBoundInfos;
//									updateView(bankBoundInfo);
//								} else {
//									isBand = false;
//									setVisble();
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//
//						@Override
//						public void onResponseFailed(String msg) {
//						}
//					});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void updateView(BankBoundInfo boundInfo) {
		setVisble();
		String num = boundInfo.getBanknum();
//		int size = num.length();
//		String num1 = num.substring(0, 4) + " ";
//		String num3 = " " + num.substring(size - 4, size);
//		num = num1 + "**** ****" + num3;
		tv_my_bank_card_name.setText(boundInfo.getBankname());
		tv_my_bank_card_number.setText(num);
		Glide.with(ShouRuHActivity.this).load(boundInfo.getImgurl())
				.error(R.mipmap.bankdefault).error(R.mipmap.bankdefault)
				.into(img_my_bank_card);
	}

	private void tijiao() {

		dialogInputPassword = new EditJDialogCommon(this) {

			@Override
			public void onOkClick() {
				tixian();
			}

			@Override
			public void onCheckClick() {
				dismiss();
			}
		};
		dialogInputPassword.show();
	}

	// 提现
	protected void tixian() {
		if (bankBoundInfo == null) {
			return;
		}
		String banknum = bankBoundInfo.getBanknum();
		String paypwd = dialogInputPassword.getInputPass();
		String amount = shouruhactivity_et_num.getText().toString().trim();
		amount = AbStrUtil.changeY2F(amount);
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		String outerOrderId = AbStrUtil.getUUID();
		if (TextUtils.isEmpty(paypwd)) {
			Toast.makeText(ShouRuHActivity.this,
					getResources().getString(R.string.please_input_6_pwd),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (paypwd.length() < 6) {
			Toast.makeText(ShouRuHActivity.this,
					getResources().getString(R.string.please_input_6_pwd),
					Toast.LENGTH_SHORT).show();
			return;
		}

		/**
		 * String outerOrderId, String accountNo, String storeNo, String
		 * amount,String product,String channel, String serNum,String
		 * source,String reqTime,
		 * 
		 */
		System.out.println("outerOrderId"+outerOrderId);
		try {
			okHttpsImp.withdraw(OkHttpsImp.md5_key,

			outerOrderId, userShopInfoBean.getUserId(),
					userShopInfoBean.getBusinessId(), amount, "01", "01", uuid,
					"app", reqTime,paypwd ,new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							dialogInputPassword.dismiss();
							shouruhactivity_et_num.setText("");
							shouruhactivity_et_num.setHint("请输入提现金额（元）");
							
							ContentUtils.putSharePre(ShouRuHActivity.this,
									Constants.SHARED_PREFERENCE_NAME, Constants.SEARCHFINANCIAL,
									true);
							
							Intent intent = new Intent(ShouRuHActivity.this,
									Sucess_FailledActivity.class);
							intent.putExtra("key", 1);
							ShouRuHActivity.this.startActivity(intent);
						}

						@Override
						public void onResponseFailed(String msg) {
							dialogInputPassword.dismiss();
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected Boolean isBand = false;

	private void getIsBand() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getIsTrue(OkHttpsImp.md5_key,
					userShopInfoBean.getUserId(), "01", uuid, "app", reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							System.out.println("邦卡信息---ShouRuHActivity"+resString);
							if(!TextUtils.isEmpty(resString)){
								BankBoundInfo bankBoundInfos = JSON.parseObject(resString,BankBoundInfo.class);
								isBand = true;
								bankBoundInfo = bankBoundInfos;
								updateView(bankBoundInfo);
							}else{
								isBand = false;
								setVisble();
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							isBand = false;
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setVisble() {
		if (isBand) {
			shouruhactivity_ll_band.setVisibility(View.VISIBLE);
			shouruhactivity_ll_noband.setVisibility(View.GONE);
		} else {
			shouruhactivity_ll_band.setVisibility(View.GONE);
			shouruhactivity_ll_noband.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getIsBand();
		getBankGettotalmount();
	}
}
