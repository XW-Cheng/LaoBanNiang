package com.lianbi.mezone.b.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.DingdanInfoBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 订单info
 */
public class DingdanInfoActivity extends BaseActivity {

	private AbPullToRefreshView act_dingdaninfo_abpulltorefreshview;
	private ListView act_dingdaninfo_listview;
	private ImageView act_dingdaninfo_iv_empty;
	private int page = 0;
	private ArrayList<DingdanInfoBean> mDatas = new ArrayList<DingdanInfoBean>();
	private QuickAdapter<DingdanInfoBean> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_dingdaninfo, NOTYPE);
		initView();
		setListen();
		initAdapter();
		getDingdanInfo(true);
	}

	private void initAdapter() {
		mAdapter = new QuickAdapter<DingdanInfoBean>(this,
				R.layout.item_order_info, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					DingdanInfoBean item) {
				TextView tv_item_orderinfo_num = helper
						.getView(R.id.tv_item_orderinfo_num);// 单号
				TextView tv_item_orderinfo_state = helper
						.getView(R.id.tv_item_orderinfo_state);// 状态
				TextView tv_item_orderinfo_price = helper
						.getView(R.id.tv_item_orderinfo_price);// 价格
				TextView tv_item_orderinfo_paytime = helper
						.getView(R.id.tv_item_orderinfo_paytime);// 支付时间
				// 代码填充
				// DecimalFormat df = new DecimalFormat("######0.00");// 用来格式化数字
				tv_item_orderinfo_num.setText(item.getOrderId()
						.replace("-", ""));
				if (item.getStatus().equals("03")) {
					tv_item_orderinfo_state.setText("支付成功");
				} else if (item.getStatus().equals("01")
						|| item.getStatus().equals("02")) {
					tv_item_orderinfo_state.setText("未支付");
				} else if (item.getStatus().equals("05")) {
					tv_item_orderinfo_state.setText("已关闭");
				} else if (item.getStatus().equals("04")) {
					tv_item_orderinfo_state.setText("支付失败");
				} else {
					tv_item_orderinfo_state.setText("未知状态");
				}

				String amt = BigDecimal.valueOf(Long.valueOf(item.getPayAmt()))
						.divide(new BigDecimal(100)).toString();
				tv_item_orderinfo_price.setText(amt + " 元");
				String time = item.getPayTime();
				String year = time.substring(0, 4);
				String months = time.substring(4, 6);
				String daytime = time.substring(6, 8);
				String hour = time.substring(8, 10);
				String minute = time.substring(10, 12);
				String second = time.substring(12, 14);
				tv_item_orderinfo_paytime.setText(year + "-" + months + "-"
						+ daytime + " " + hour + ":" + minute + ":" + second);
			}

		};
		// 设置适配器
		act_dingdaninfo_listview.setAdapter(mAdapter);
	}

	private void initView() {
		setPageTitle("订单明细");

		act_dingdaninfo_abpulltorefreshview = (AbPullToRefreshView) findViewById(R.id.act_dingdaninfo_abpulltorefreshview);
		act_dingdaninfo_listview = (ListView) findViewById(R.id.act_dingdaninfo_listview);
		act_dingdaninfo_iv_empty = (ImageView) findViewById(R.id.act_dingdaninfo_iv_empty);

	}

	private void setListen() {
		act_dingdaninfo_abpulltorefreshview.setLoadMoreEnable(true);
		act_dingdaninfo_abpulltorefreshview.setPullRefreshEnable(true);
		act_dingdaninfo_abpulltorefreshview
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getDingdanInfo(true);
					}
				});
		act_dingdaninfo_abpulltorefreshview
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						getDingdanInfo(false);
					}
				});
		act_dingdaninfo_listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});
	}

	/**
	 * 获取订单信息
	 * 
	 * @param isResh
	 */
	private void getDingdanInfo(final boolean isResh) {
		if (isResh) {
			page = 0;
			mDatas.clear();
			mAdapter.replaceAll(mDatas);
		}

		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getOrderInfoById(uuid, "app", reqTime, "Y", "app",
					OkHttpsImp.md5_key, userShopInfoBean.getBusinessId(), page
							+ "", 20 + "",

					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							page++;
							String reString = result.getData();
							if (!TextUtils.isEmpty(reString)) {
								try {
									JSONObject jsonObject = new JSONObject(reString);
									reString = jsonObject.getString("list");
									ArrayList<DingdanInfoBean> mDatasL = (ArrayList<DingdanInfoBean>) JSON
											.parseArray(reString,
													DingdanInfoBean.class);
									if (mDatasL != null && mDatasL.size() > 0) {
										mDatas.addAll(mDatasL);
									}
									if (mDatas != null && mDatas.size() > 0) {
										act_dingdaninfo_iv_empty
										.setVisibility(View.GONE);
										act_dingdaninfo_abpulltorefreshview
										.setVisibility(View.VISIBLE);
									} else {
										act_dingdaninfo_iv_empty
										.setVisibility(View.VISIBLE);
										act_dingdaninfo_abpulltorefreshview
										.setVisibility(View.GONE);
									}
									AbPullHide.hideRefreshView(isResh,
											act_dingdaninfo_abpulltorefreshview);
									mAdapter.replaceAll(mDatas);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							if (isResh) {
								act_dingdaninfo_iv_empty
										.setVisibility(View.VISIBLE);
								act_dingdaninfo_abpulltorefreshview
										.setVisibility(View.GONE);
							}
							AbPullHide.hideRefreshView(isResh,
									act_dingdaninfo_abpulltorefreshview);
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
