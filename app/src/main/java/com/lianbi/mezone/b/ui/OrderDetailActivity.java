package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.utils.MoneyFlag;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.OrderBeanDetail;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 订单详情
 * 
 * @time 下午6:03:30
 * @date 2016-1-24
 * @author hongyu.yang
 * 
 */
public class OrderDetailActivity extends BaseActivity {

	TextView totle_money_tv, order_number_tv, order_name_tv, order_time_tv,
			order_phone_tv, order_address_tv, order_shopname_tv,
			order_shopnumber_tv, order_status_iv;
	ImageView order_icon;
	Button refuse_btn, accept_btn;
	private int status;
	/**
	 * 订单id
	 */
	String orderBean;
	protected ArrayList<OrderBeanDetail> orderBeanDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_orderdetail, NOTYPE);
		orderBean = getIntent().getStringExtra("bean");
		initView();
		getOrderById();
	}

	/**
	 * 訂單詳細
	 */
	private void getOrderById() {
		okHttpsImp.getOrderById(orderBean, new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				try {
					String reString = result.getData();
					org.json.JSONObject jb = new org.json.JSONObject(reString);
					String Str = jb.getString("data");
					orderBeanDetail = (ArrayList<OrderBeanDetail>) JSONObject
							.parseArray(Str, OrderBeanDetail.class);
					if (orderBeanDetail != null && orderBeanDetail.size() > 0) {
						status = orderBeanDetail.get(0).getStatus();
						upView(orderBeanDetail.get(0));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		});
	}

	/**
	 * 待处理： status = "1";
	 * 
	 * 已接受： status = "2";
	 * 
	 * 已完成：status = "3"; 已拒绝：status = "4";
	 * 
	 * @param ob
	 */
	protected void upView(OrderBeanDetail ob) {
		refuse_btn.setVisibility(View.GONE);
		accept_btn.setVisibility(View.GONE);
		order_number_tv.setText(ob.getOrder_id());
		int color = -1;
		switch (ob.getStatus()) {
		case 1:
			refuse_btn.setVisibility(View.VISIBLE);
			accept_btn.setVisibility(View.VISIBLE);
			order_status_iv.setText("待处理");
			color = getResources().getColor(R.color.colores_news_04);

			break;
		case 2:
			color = getResources().getColor(R.color.colores_news_06);
			order_status_iv.setText("已接受");

			break;
		case 3:
			color = getResources().getColor(R.color.colores_news_06);
			order_status_iv.setText("已完成");
			break;
		case 4:
			color = getResources().getColor(R.color.colores_news_12);
			order_status_iv.setText("已拒绝");

			break;
		default:
			order_status_iv.setText("未知");
			break;
		}
		order_status_iv.setBackgroundColor(color);
		String price = ob.getPrice();
		if (TextUtils.isEmpty(price)) {
			price = ob.getAmount();
		}
		if (price.contains("-")) {
			price = price.substring(0, price.indexOf("-"));
		}
		order_name_tv.setText(ob.getUser_name());
		order_time_tv.setText(ob.getCreateTime());
		order_phone_tv.setText(ob.getPhone());
		order_address_tv.setText(ob.getAddress());
		totle_money_tv.setText(MoneyFlag.MONEYFAAG
				+ (Double.parseDouble(price) * ob.getNum()) + "");
		order_shopname_tv.setText(ob.getProductName());
		order_shopnumber_tv.setText("x" + ob.getNum());
		Glide.with(this).load(ob.getIcon()).error(R.mipmap.defaultimg_11)
				.into(order_icon);

	}

	private void initView() {
		setPageTitle("订单详情");
		totle_money_tv = (TextView) findViewById(R.id.totle_money_tv);
		order_number_tv = (TextView) findViewById(R.id.order_number_tv);
		order_status_iv = (TextView) findViewById(R.id.order_status_iv);
		order_name_tv = (TextView) findViewById(R.id.order_name_tv);
		order_time_tv = (TextView) findViewById(R.id.order_time_tv);
		order_phone_tv = (TextView) findViewById(R.id.order_phone_tv);
		order_address_tv = (TextView) findViewById(R.id.order_address_tv);
		order_icon = (ImageView) findViewById(R.id.order_icon);
		order_shopname_tv = (TextView) findViewById(R.id.order_shopname_tv);
		order_shopnumber_tv = (TextView) findViewById(R.id.order_shopnumber_tv);
		totle_money_tv = (TextView) findViewById(R.id.totle_money_tv);
		refuse_btn = (Button) findViewById(R.id.refuse_btn);
		accept_btn = (Button) findViewById(R.id.accept_btn);
		MoneyFlag.addMoneyFlag(this, totle_money_tv);
		refuse_btn.setOnClickListener(this);
		accept_btn.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (status) {
		case 1:
			if (view == refuse_btn) {
				upDateOrderStatus("4");
			} else if (view == accept_btn) {
				upDateOrderStatus("2");
			}
			break;
		}
	}

	/**
	 * B端修改订单状态---接受订单，拒绝订单
	 */
	private void upDateOrderStatus(String status) {
		okHttpsImp.upDateOrderStatus(orderBean, status,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						getOrderById();
					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});
	}
}
