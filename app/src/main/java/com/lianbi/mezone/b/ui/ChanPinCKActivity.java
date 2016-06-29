package com.lianbi.mezone.b.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.MoneyFlag;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MyProductBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 产品仓库
 * 
 * @author guanghui.han
 * 
 */
public class ChanPinCKActivity extends BaseActivity {
	AbPullToRefreshView abpulltorefreshview_act_myshopchanpinfbactivity;
	ListView listView_act_myshopchanpinfbactivity;
	protected ArrayList<MyProductBean> list = new ArrayList<MyProductBean>();
	ImageView iv_act_myshopchanpinfbactivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myshopchanpinfbactivity, NOTYPE);
		initView();
		initaDapter();
		setLisenter();
		getProductPoolByBusiness(true);
	}

	int page = 0;

	private void getProductPoolByBusiness(final boolean isRe) {
		if (isRe) {
			page = 0;
		}
		okHttpsImp.getProductPoolByBusiness(userShopInfoBean.getUserId(), page
				+ "", new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reS = result.getData();
				if (!TextUtils.isEmpty(reS)) {
					page++;
					try {
						JSONObject jb = new JSONObject(reS);
						String sP = jb.getString("productPoolList");
						List<MyProductBean> cl = JSON.parseArray(sP,
								MyProductBean.class);
						if (isRe) {
							list.clear();
						}
						list.addAll(cl);
						if (list.size() > 0) {

							iv_act_myshopchanpinfbactivity
									.setVisibility(View.GONE);
							abpulltorefreshview_act_myshopchanpinfbactivity
									.setVisibility(View.VISIBLE);
							mAdapter.replaceAll(list);

						} else {
							iv_act_myshopchanpinfbactivity
									.setVisibility(View.VISIBLE);
							abpulltorefreshview_act_myshopchanpinfbactivity
									.setVisibility(View.GONE);

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					iv_act_myshopchanpinfbactivity.setVisibility(View.VISIBLE);
					abpulltorefreshview_act_myshopchanpinfbactivity
							.setVisibility(View.GONE);
				}
				AbPullHide.hideRefreshView(isRe,
						abpulltorefreshview_act_myshopchanpinfbactivity);
			}

			@Override
			public void onResponseFailed(String msg) {
				iv_act_myshopchanpinfbactivity.setVisibility(View.VISIBLE);
				abpulltorefreshview_act_myshopchanpinfbactivity
						.setVisibility(View.GONE);
				AbPullHide.hideRefreshView(isRe,
						abpulltorefreshview_act_myshopchanpinfbactivity);
			}
		});

	}

	QuickAdapter<MyProductBean> mAdapter;

	private void initaDapter() {
		mAdapter = new QuickAdapter<MyProductBean>(this,
				R.layout.item_producthome, list) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					final MyProductBean item) {
				ImageView iv = helper
						.getView(R.id.imageView_setingzhuohaoactivity_tv_name);
				Glide.with(ChanPinCKActivity.this).load(item.getIcon())
						.error(R.mipmap.defaultimg_11).into(iv);
				helper.setText(R.id.item_setingzhuohaoactivity_tv_name,
						item.getProduct_pool_name());
				helper.setText(R.id.item_setingzhuohaoactivity_tv_leis,
						item.getIndustry_name());
				helper.setText(R.id.tv_setingzhuohaoactivity_time,
						item.getPool_create_time());
				TextView tvPrice = helper
						.getView(R.id.tv_setingzhuohaoactivity_price);
				String two = item.getPrice();
				String three = "/" + item.getUnit();
				SpannableuUtills.setSpannableu(tvPrice, MoneyFlag.MONEYFAAG,
						two, three);
				TextView tvS = helper
						.getView(R.id.item_setingzhuohaoactivity_tv_status);
				if (item.getStatus() == 1) {
					tvS.setVisibility(View.VISIBLE);
					tvS.setBackgroundResource(R.drawable.shap_color_12);
				} else {
					tvS.setVisibility(View.INVISIBLE);
				}
				tvS.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (item.getStatus() == 1) {

							downOnline(item);
						}
					}

				});
			}
		};
		// 设置适配器
		listView_act_myshopchanpinfbactivity.setAdapter(mAdapter);

	}

	private void downOnline(final MyProductBean productPoolId) {
		okHttpsImp.updateProductPoolStatus(productPoolId.getProduct_pool_id(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						// getProductPoolByBusiness(true);
						productPoolId.setStatus(0);
						mAdapter.replaceAll(list);

					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});
	}

	private void initView() {
		setPageTitle("产品仓库");
		abpulltorefreshview_act_myshopchanpinfbactivity = (AbPullToRefreshView) findViewById(R.id.abpulltorefreshview_act_myshopchanpinfbactivity);
		listView_act_myshopchanpinfbactivity = (ListView) findViewById(R.id.listView_act_myshopchanpinfbactivity);
		iv_act_myshopchanpinfbactivity = (ImageView) findViewById(R.id.iv_act_myshopchanpinfbactivity);

	}

	private void setLisenter() {
		abpulltorefreshview_act_myshopchanpinfbactivity
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getProductPoolByBusiness(true);
					}
				});
		abpulltorefreshview_act_myshopchanpinfbactivity
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						getProductPoolByBusiness(false);

					}
				});
	}
}
