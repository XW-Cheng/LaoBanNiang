package com.lianbi.mezone.b.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import cn.com.hgh.baseadapter.CeHuaDeleteAdapter;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MyProductBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 本店产品
 * 
 * @author guanghui.han
 * 
 */
@SuppressLint("ResourceAsColor")
public class MyShopChanPinfbActivity extends BaseActivity {

	AbPullToRefreshView abpulltorefreshview_act_myshopchanpinfbactivity;
	ListView listView_act_myshopchanpinfbactivity;
	private CeHuaDeleteAdapter listAdapter;
	protected ArrayList<MyProductBean> list = new ArrayList<MyProductBean>();
	ImageView iv_act_myshopchanpinfbactivity;
	private final int CHANPINFBACTIVITY_CODE = 1005;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myshopchanpinfbactivity, NOTYPE);
		initView();
		initaDapter();
		setLisenter();
		initData();
	}

	private void initData() {
		okHttpsImp.getProductListByBusiness(userShopInfoBean.getBusinessId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jb = new JSONObject(result.getData());
							String pStr = jb.getString("product");
							ArrayList<MyProductBean> listc = (ArrayList<MyProductBean>) com.alibaba.fastjson.JSONObject
									.parseArray(pStr, MyProductBean.class);
							list.clear();
							list.addAll(listc);
							if (list.size() > 0) {
								iv_act_myshopchanpinfbactivity
										.setVisibility(View.GONE);
								abpulltorefreshview_act_myshopchanpinfbactivity
										.setVisibility(View.VISIBLE);

								listAdapter.notifyDataSetChanged();
							} else {
								iv_act_myshopchanpinfbactivity
										.setVisibility(View.VISIBLE);
								abpulltorefreshview_act_myshopchanpinfbactivity
										.setVisibility(View.GONE);

							}
						} catch (JSONException e) {
							iv_act_myshopchanpinfbactivity
									.setVisibility(View.VISIBLE);
							abpulltorefreshview_act_myshopchanpinfbactivity
									.setVisibility(View.GONE);
							e.printStackTrace();
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						iv_act_myshopchanpinfbactivity
								.setVisibility(View.VISIBLE);
						abpulltorefreshview_act_myshopchanpinfbactivity
								.setVisibility(View.GONE);
					}
				});

	}

	private void initaDapter() {
		listAdapter = new CeHuaDeleteAdapter(this, true) {

			@Override
			public void onItemLock(int p) {
				MyProductBean c = list.get(p);
				deleteItem(c);
			}

			@Override
			public List<? extends MyProductBean> getData() {
				return list;
			}
		};
		listView_act_myshopchanpinfbactivity.setAdapter(listAdapter);

	}

	/**
	 * 删除一个
	 */
	protected void deleteItem(final MyProductBean c) {
		String productSourceId = c.getProduct_id();
		okHttpsImp.postDelShop(productSourceId, new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				list.remove(c);
				listAdapter.closeAllItems();
				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void onResponseFailed(String msg) {
				listAdapter.closeAllItems();

			}
		});
	}

	private void initView() {
		setPageTitle("本店产品");
		setPageRightText("新增");
		setPageRightTextColor(R.color.colores_news_01);
		abpulltorefreshview_act_myshopchanpinfbactivity = (AbPullToRefreshView) findViewById(R.id.abpulltorefreshview_act_myshopchanpinfbactivity);
		abpulltorefreshview_act_myshopchanpinfbactivity
				.setPullRefreshEnable(false);
		abpulltorefreshview_act_myshopchanpinfbactivity
				.setLoadMoreEnable(false);
		listView_act_myshopchanpinfbactivity = (ListView) findViewById(R.id.listView_act_myshopchanpinfbactivity);
		iv_act_myshopchanpinfbactivity = (ImageView) findViewById(R.id.iv_act_myshopchanpinfbactivity);

	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		Intent intent = new Intent(this, ChanPinfbActivity.class);
		startActivityForResult(intent, CHANPINFBACTIVITY_CODE);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case CHANPINFBACTIVITY_CODE:
				initData();
				break;
			}
		}
	}

	private void setLisenter() {
		abpulltorefreshview_act_myshopchanpinfbactivity
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						AbPullHide
								.hideRefreshView(true,
										abpulltorefreshview_act_myshopchanpinfbactivity);
					}
				});
		abpulltorefreshview_act_myshopchanpinfbactivity
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						AbPullHide
								.hideRefreshView(false,
										abpulltorefreshview_act_myshopchanpinfbactivity);

					}
				});
	}
}
