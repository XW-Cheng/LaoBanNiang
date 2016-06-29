package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.EditTextUtills;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.SpannableuUtills;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.OrderBean;

/**
 * 订单列表
 * 
 * @time 下午3:21:34
 * @date 2016-1-20
 * 
 * @author hongyu.yang
 * 
 */
public class OrderProductListActivity extends BaseActivity {
	AbPullToRefreshView abpulltorefreshview_act_transactionmanagementactivity;
	ListView listView_act_transactionmanagementactivity;
	ImageView iv_empty_act_transactionmanagementactivity;
	ArrayList<OrderBean> mDatas = new ArrayList<OrderBean>();

	/**
	 * 是否从详情来
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderproductlistactivity, NOTYPE);
		initView();
		listen();
		initListAdapter();
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	};

	/**
	 * 初始化list Adapter
	 */
	QuickAdapter<OrderBean> mAdapter;

	private void initListAdapter() {
		mAdapter = new QuickAdapter<OrderBean>(this,
				R.layout.transactionmanagementactivityitem, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper, OrderBean item) {
				helper.setText(R.id.tv_title_transactionmanagementactivityitem,
						item.getOrder_id());
				helper.setText(R.id.tv_time_transactionmanagementactivityitem,
						item.getCreateTime());
				TextView tv = helper
						.getView(R.id.tv_price_transactionmanagementactivityitem);
				String price = MathExtend.round(item.getPrice(), 1) + "";
				String two = price.substring(0, price.indexOf("."));
				String three = price.substring(price.indexOf("."),
						price.length());
				SpannableuUtills.setSpannableu(tv, two, three);
			}
		};
		// 设置适配器
		listView_act_transactionmanagementactivity.setAdapter(mAdapter);
	}

	private void listen() {
		abpulltorefreshview_act_transactionmanagementactivity
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getData(true);
					}
				});
		abpulltorefreshview_act_transactionmanagementactivity
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						getData(false);

					}
				});

		listView_act_transactionmanagementactivity
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						OrderProductListActivity.this.startActivity(new Intent(
								OrderProductListActivity.this,
								OrderProductDetailActivity.class).putExtra(
								"bean", mDatas.get(arg2).getOrder_id()));
					}
				});

	}

	protected void getData(boolean b) {
		AbPullHide.hideRefreshView(b,
				abpulltorefreshview_act_transactionmanagementactivity);
	}

	private void initView() {
		setPageTitle("订单列表");
		abpulltorefreshview_act_transactionmanagementactivity = (AbPullToRefreshView) findViewById(R.id.abpulltorefreshview_act_orderproductlistactivity);
		iv_empty_act_transactionmanagementactivity = (ImageView) findViewById(R.id.iv_empty_act_orderproductlistactivity);
		listView_act_transactionmanagementactivity = (ListView) findViewById(R.id.listView_act_orderproductlistactivity);

	}
}
