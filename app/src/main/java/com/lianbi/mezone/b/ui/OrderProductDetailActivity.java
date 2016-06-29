package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.MoneyFlag;

import com.xizhi.mezone.b.R;
import com.zbar.lib.CaptureActivity;

/**
 * 订单详情
 * 
 * @time 下午3:21:34
 * @date 2016-1-20
 * 
 * @author hongyu.yang
 * 
 */
public class OrderProductDetailActivity extends BaseActivity {
	ImageView orderproductdetailactivity_iv_up,
			orderproductdetailactivity_iv_qrce;
	TextView orderproductdetailactivity_iv_shou,
			orderproductdetailactivity_iv_list;
	TextView orderproductdetailactivity_order_tv,
			orderproductdetailactivity_name_tv,
			orderproductdetailactivity_time_tv,
			orderproductdetailactivity_phone_tv,
			orderproductdetailactivity_address_tv,
			orderproductdetailactivity_price_tv;
	/**
	 * 是否从生成订单来
	 */
	private boolean isCreate;
	String urlCode;
	String amount;
	String orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderproductdetailactivity, HAVETYPE);
		setResult(RESULT_OK);
		isCreate = getIntent().getBooleanExtra("isCreate", false);
		urlCode = getIntent().getStringExtra("urlCode");
		orderId = getIntent().getStringExtra("orderId");
		amount = getIntent().getStringExtra("amount");
		initView();
	}

	private void initView() {
		setPageTitle("订单详情");
		orderproductdetailactivity_iv_up = (ImageView) findViewById(R.id.orderproductdetailactivity_iv_up);
		LinearLayout.LayoutParams llp = (LayoutParams) orderproductdetailactivity_iv_up
				.getLayoutParams();
		llp.height = (screenWidth - (int) AbViewUtil.dip2px(this, 60)) / 10;

		orderproductdetailactivity_iv_qrce = (ImageView) findViewById(R.id.orderproductdetailactivity_iv_qrce);
		orderproductdetailactivity_iv_qrce.setImageBitmap(ContentUtils
				.createQrBitmap(urlCode, true, 1000, 1000));
		orderproductdetailactivity_price_tv = (TextView) findViewById(R.id.orderproductdetailactivity_price_tv);
		orderproductdetailactivity_price_tv.setText(MoneyFlag.MONEYFAAG
				+ amount);
		orderproductdetailactivity_iv_shou = (TextView) findViewById(R.id.orderproductdetailactivity_iv_shou);
		orderproductdetailactivity_iv_list = (TextView) findViewById(R.id.orderproductdetailactivity_iv_list);
		orderproductdetailactivity_iv_shou.setOnClickListener(this);
		orderproductdetailactivity_iv_list.setOnClickListener(this);
		orderproductdetailactivity_order_tv = (TextView) findViewById(R.id.orderproductdetailactivity_order_tv);
		orderproductdetailactivity_name_tv = (TextView) findViewById(R.id.orderproductdetailactivity_name_tv);
		orderproductdetailactivity_time_tv = (TextView) findViewById(R.id.orderproductdetailactivity_time_tv);
		orderproductdetailactivity_phone_tv = (TextView) findViewById(R.id.orderproductdetailactivity_phone_tv);
		orderproductdetailactivity_address_tv = (TextView) findViewById(R.id.orderproductdetailactivity_address_tv);
		orderproductdetailactivity_order_tv.setText("订单号:" + orderId);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.orderproductdetailactivity_iv_list: {// 去订单列表
			Intent intent_more = new Intent(this,
					OrderProductListActivity.class);
			intent_more.putExtra("IsDetail", true);
			startActivity(intent_more);
			if (isCreate) {
				finish();
			}
		}

			break;
		case R.id.orderproductdetailactivity_iv_shou: {// 扫码付款
			// orderproductdetailactivity_iv_qrce.setImageBitmap(ContentUtils
			// .createQrBitmap("付款码已失效！", true, 1000, 1000));
			Intent intent_more = new Intent(this, CaptureActivity.class);
			intent_more.putExtra("amount", amount);
			intent_more.putExtra("orderId", orderId);
			startActivity(intent_more);
		}
			break;

		}
	}
}
