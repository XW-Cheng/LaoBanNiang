package com.lianbi.mezone.b.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.EditTextUtills;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.zbar.lib.CaptureActivity;

/**
 * 订单生成
 * 
 * @time 下午3:21:34
 * @date 2016-1-20
 * 
 * @author hongyu.yang
 * 
 */
public class OrderProductActivity extends BaseActivity {
	EditText orderproductactivity_et_num;
	TextView text_orderproductactivity, textright_orderproductactivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderproductactivity, NOTYPE);

		initView();
	}

	private void initView() {
		setPageTitle("订单生成");
		text_orderproductactivity = (TextView) findViewById(R.id.text_orderproductactivity);
		textright_orderproductactivity = (TextView) findViewById(R.id.text_right_orderproductactivity);
		orderproductactivity_et_num = (EditText) findViewById(R.id.orderproductactivity_et_num);
		EditTextUtills.setPricePoint(orderproductactivity_et_num);
		text_orderproductactivity.setOnClickListener(this);
		textright_orderproductactivity.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		final String amount = orderproductactivity_et_num.getText().toString()
				.trim();
		if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
			ContentUtils.showMsg(this, "请正确输入金额");
			return;
		}
		if (amount.endsWith(".")) {
			ContentUtils.showMsg(this, "请正确输入金额");
			return;
		}
		if (Double.parseDouble(amount) > 9999) {
			ContentUtils.showMsg(this, "输入金额不能大于9999");
			return;
		}
		if (view.getId() == R.id.text_orderproductactivity) {
			okHttpsImp.sweepPay(userShopInfoBean.getUserId(), amount,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String data = result.getData();
							try {
								JSONObject jb = new JSONObject(data);
								String urlCode = jb.getString("urlCode");
								String orderId = jb.getString("orderId");
								startActivityForResult(
										new Intent(
												OrderProductActivity.this,
												OrderProductDetailActivity.class)
												.putExtra("isCreate", true)
												.putExtra("urlCode", urlCode)
												.putExtra("amount", amount)
												.putExtra("orderId", orderId),
										HUIDIAO);
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onResponseFailed(String msg) {

						}
					});
		} else if (view.getId() == R.id.text_right_orderproductactivity) {
			Intent intent_more = new Intent(this, CaptureActivity.class);
			intent_more.putExtra("amount", amount);
			intent_more.putExtra("orderId", "1");
			startActivityForResult(intent_more, HUIDIAO);

		}
	}

	private final int HUIDIAO = 5006;

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			if (arg0 == HUIDIAO) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}
}
