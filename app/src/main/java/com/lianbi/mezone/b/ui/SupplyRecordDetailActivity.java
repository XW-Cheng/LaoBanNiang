package com.lianbi.mezone.b.ui;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.utils.TelPhoneUtills;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.ProductSourceList;

/**
 * 下单记录详情
 * 
 * @time 下午2:56:58
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class SupplyRecordDetailActivity extends BaseActivity {
	private TextView tv_supply_record_detail_shopping_name,
			tv_supply_record_detail_sn, tv_supply_record_detail_name,
			tv_supply_record_detail_phone, tv_supply_record_detail_address,
			tv_supply_record_detail_shop_name, tv_supply_record_detail_price,
			tv_supply_record_detail_sum_price, tv_supply_record_call;
	private ImageView img_supply_record_detail;
	private ProductSourceList productSourceList;
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_record_detail, NOTYPE);
		productSourceList = (ProductSourceList) getIntent()
				.getSerializableExtra("product");
		initView();
		initWidget();
	}

	private void initWidget() {
		if (productSourceList != null) {
			tv_supply_record_detail_shopping_name.setText(productSourceList
					.getBusiness_name());
			tv_supply_record_detail_sn.setText(productSourceList.getOrder_id());
			tv_supply_record_detail_name.setText(productSourceList
					.getUser_name());
			phone = productSourceList.getVendorPhone();
			tv_supply_record_detail_phone.setText(productSourceList.getPhone());
			tv_supply_record_detail_address.setText(productSourceList
					.getAddress());
			tv_supply_record_detail_shop_name.setText(productSourceList
					.getProduct_source_title());
			tv_supply_record_detail_price.setText("x"
					+ productSourceList.getNum());
			DecimalFormat df = new DecimalFormat("######0.00");
			String price = df.format(productSourceList.getPrice());
			tv_supply_record_detail_sum_price.setText("¥" + price);
			Glide.with(this).load(productSourceList.getImage())
					.error(R.mipmap.defaultimg_11)
					.into(img_supply_record_detail);
		}
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("订单详细信息");
		tv_supply_record_detail_shopping_name = (TextView) findViewById(R.id.tv_supply_record_detail_shopping_name);
		tv_supply_record_detail_sn = (TextView) findViewById(R.id.tv_supply_record_detail_sn);
		tv_supply_record_detail_name = (TextView) findViewById(R.id.tv_supply_record_detail_name);
		tv_supply_record_detail_phone = (TextView) findViewById(R.id.tv_supply_record_detail_phone);
		tv_supply_record_detail_address = (TextView) findViewById(R.id.tv_supply_record_detail_address);

		tv_supply_record_detail_shop_name = (TextView) findViewById(R.id.tv_supply_record_detail_shop_name);
		tv_supply_record_detail_price = (TextView) findViewById(R.id.tv_supply_record_detail_price);
		tv_supply_record_detail_sum_price = (TextView) findViewById(R.id.tv_supply_record_detail_sum_price);
		tv_supply_record_call = (TextView) findViewById(R.id.tv_supply_record_call);
		tv_supply_record_call.setOnClickListener(this);
		img_supply_record_detail = (ImageView) findViewById(R.id.img_supply_record_detail);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.tv_supply_record_call:
			if (!TextUtils.isEmpty(phone)) {
				TelPhoneUtills.launchPhone(SupplyRecordDetailActivity.this,
						phone);
			}
			break;
		}
	}

}
