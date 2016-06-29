package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.PagerIndicator;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.imp.TextSliderView;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Ades_ImageEs;
import com.lianbi.mezone.b.bean.MyLiCaiBean;
import com.lianbi.mezone.b.fragment.ShouYeFragment;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 理财产品
 * 
 * @time 下午3:52:32
 * @date 2015-12-8
 * @author hongyu.yang
 * 
 */
public class ManageMoneyProductActivity extends BaseActivity implements
		OnSliderClickListener {
	TextView tv_manage_money_product_balance;
	private SliderLayout playview_manage_money_product;
	ProgressBar adeslltview_siderlayout_progressBar;
	ListView list_manage_money_product;
	LinearLayout llt_manage_money_product_play, adeslltview_llt,
			llt_my_managemoney;
	String username;
	/**
	 * 我的余额
	 */
	double total;
	/**
	 * 收益总额
	 */
	double totalprofit;

	private ArrayList<MyLiCaiBean> arrayList = new ArrayList<MyLiCaiBean>();

	/**
	 * 广告
	 */
	private ArrayList<Ades_ImageEs> ades_ImageEs = new ArrayList<Ades_ImageEs>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_money_product, HAVETYPE);
		initView();
	}

	private void initView() {
		setPageTitle("理财产品");
		tv_manage_money_product_balance = (TextView) findViewById(R.id.tv_manage_money_product_balance);
		list_manage_money_product = (ListView) findViewById(R.id.list_manage_money_product);
		llt_my_managemoney = (LinearLayout) findViewById(R.id.llt_my_managemoney);
		initAdesView();
		getFinancingFinancingtypelist();
		initAdapter();
		setLisenter();
		getFinancingMycount();
	}

	/**
	 * 初始化广告视图
	 */
	private void initAdesView() {
		playview_manage_money_product = (SliderLayout) findViewById(R.id.adeslltview_siderlayout);
		playview_manage_money_product
				.setPresetTransformer(SliderLayout.Transformer.Default);
		adeslltview_llt = (LinearLayout) findViewById(R.id.adeslltview_llt);
		adeslltview_siderlayout_progressBar = (ProgressBar) findViewById(R.id.adeslltview_siderlayout_progressBar);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenWidth / 4);
		adeslltview_llt.setLayoutParams(params);

	}

	/**
	 * 总收益、理财余额 接口
	 */
	private void getFinancingMycount() {
		total = 0.00;
		totalprofit = 0.00;
		tv_manage_money_product_balance.setText(MathExtend.roundNew(total, 2)
				+ "");
		// ActionImpl actionImpl = ActionImpl.newInstance(this);
		// actionImpl.getFinancingMycount(username, new ResultHandlerCallback()
		// {
		//
		// @Override
		// public void rc999(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc3001(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc0(RequestEntity entity, Result result) {
		// String resString = result.getResult();
		// try {
		// JSONObject jsonObject = new JSONObject(resString);
		// total = jsonObject.getDouble("total");
		// totalprofit = jsonObject.getDouble("totalprofit");
		// tv_manage_money_product_balance.setText(MathExtend
		// .roundNew(total, 2) + "");
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		// });
	}

	/**
	 * 理财类型列表
	 */
	private void getFinancingFinancingtypelist() {
//		MyLiCaiBean bean = new MyLiCaiBean();
//		bean.setAmount(100);
//		bean.setDate("2018-2-5");
//		bean.setDeadline(50);
//		bean.setDescription("你好我是描述");
//		bean.setId(64);
//		bean.setLable("d");
//		bean.setName("定期理财");
//		bean.setRate(5.0);
//		MyLiCaiBean bean1 = new MyLiCaiBean();
//		bean1.setAmount(1000);
//		bean1.setDate("2018-2-5");
//		bean1.setDeadline(50);
//		bean1.setDescription("你好我是描述");
//		bean1.setId(74);
//		bean1.setLable("h");
//		bean1.setName("活期理财");
//		bean1.setRate(3.0);
//		arrayList.add(bean1);
//		arrayList.add(bean);
		// ActionImpl actionImpl = ActionImpl.newInstance(this);
		// actionImpl.getFinancingFinancingtypelist(new ResultHandlerCallback()
		// {
		//
		// @Override
		// public void rc999(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc3001(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc0(RequestEntity entity, Result result) {
		// String resString = result.getResult();
		// ArrayList<MyLiCaiBean> arrayListL = (ArrayList<MyLiCaiBean>) JSON
		// .parseArray(resString, MyLiCaiBean.class);
		// if (arrayListL != null && arrayListL.size() > 0) {
		// arrayList.addAll(arrayListL);
		// adapter.notifyDataSetChanged();
		// ListViewHeight.setPullLvHeight(list_manage_money_product);
		// }
		// }
		// });
//		getAdes();
	}

	/**
	 * 设置监听
	 */
	private void setLisenter() {
		llt_my_managemoney.setOnClickListener(this);
		list_manage_money_product
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String label = arrayList.get(position).getLable();
						if (label.equals("d")) {// d定期
							ManageMoneyProductActivity.this
									.startActivity(new Intent(
											ManageMoneyProductActivity.this,
											RegularDemandManagementActivity.class)
											.putExtra("title", "定期理财产品"));
						}
						if (label.equals("h")) {// h活期
							ManageMoneyProductActivity.this
									.startActivity(new Intent(
											ManageMoneyProductActivity.this,
											RegularDemandManagementActivity.class)
											.putExtra("title", "活期理财产品"));

						}
					}
				});
	}

	QuickAdapter<MyLiCaiBean> mAdapter;

	/**
	 * 初始化Adapter
	 */
	private void initAdapter() {
		mAdapter = new QuickAdapter<MyLiCaiBean>(this,
				R.layout.item_manage_money_product, arrayList) {

			@Override
			protected void convert(BaseAdapterHelper helper, MyLiCaiBean item) {
				TextView tv_item_manage_product_title = helper
						.getView(R.id.tv_item_manage_product_title);
				TextView tv_item_manage_product_rate = helper
						.getView(R.id.tv_item_manage_product_rate);
				TextView tv_item_manage_product_content = helper
						.getView(R.id.tv_item_manage_product_content);
				String price = item.getRate() + "";
				if (price.contains(".")) {
					String two = price.substring(0, price.indexOf("."));
					String three = price.substring(price.indexOf("."),
							price.length());
					SpannableuUtills.setSpannableu(tv_item_manage_product_rate,
							"≈", two, three + "%");
				} else {
					tv_item_manage_product_rate.setText("≈" + price + "%");
				}

				String label = item.getLable();
				if (label.equals("d")) {// d定期
					tv_item_manage_product_title.setText("定期理财");
				} else {// h活期
					tv_item_manage_product_title.setText("活期理财");
				}
				tv_item_manage_product_content.setText(item.getDescription());
			}
		};
		// 设置适配器
		list_manage_money_product.setAdapter(mAdapter);
		AbViewUtil.setListViewHeight(list_manage_money_product);
	}

//	/**
//	 * 获取广告 type
//	 */
//	private void getAdes() {
//		okHttpsImp.getAdvert("4", new MyResultCallback<String>() {
//			@Override
//			public void onResponseResult(Result result) {
//				playview_manage_money_product.removeAllSliders();
//				String resString = result.getData();
//				try {
//					JSONObject jsonObject = new JSONObject(resString);
//					resString = jsonObject.getString("advertList");
//					ades_ImageEs = (ArrayList<Ades_ImageEs>) JSON.parseArray(
//							resString, Ades_ImageEs.class);
//					if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
//						for (int i = 0; i < ades_ImageEs.size(); i++) {
//							TextSliderView textSliderView = new TextSliderView(
//									ManageMoneyProductActivity.this, i,4);
//							textSliderView
//									.image(ades_ImageEs.get(i).getImage())
//									.error(R.drawable.adshouye);
//							textSliderView
//									.setOnSliderClickListener(ManageMoneyProductActivity.this);
//							playview_manage_money_product
//									.addSlider(textSliderView);
//						}
//					} else {
//						for (int i = 0; i < 3; i++) {
//							TextSliderView textSliderView = new TextSliderView(
//									ManageMoneyProductActivity.this, i,4);
//							textSliderView.image(R.drawable.adshouye);
//							textSliderView
//									.setOnSliderClickListener(ManageMoneyProductActivity.this);
//							playview_manage_money_product
//									.addSlider(textSliderView);
//						}
//					}
//					playview_manage_money_product
//							.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
//					adeslltview_siderlayout_progressBar
//							.setVisibility(View.GONE);
//					playview_manage_money_product.setVisibility(View.VISIBLE);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onResponseFailed(String msg) {
//				playview_manage_money_product.removeAllSliders();
//				for (int i = 0; i < 3; i++) {
//					TextSliderView textSliderView = new TextSliderView(
//							ManageMoneyProductActivity.this, i,4);
//					textSliderView.image(R.drawable.adshouye);
//					textSliderView
//							.setOnSliderClickListener(ManageMoneyProductActivity.this);
//					playview_manage_money_product.addSlider(textSliderView);
//				}
//				playview_manage_money_product
//						.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
//				adeslltview_siderlayout_progressBar.setVisibility(View.GONE);
//				playview_manage_money_product.setVisibility(View.VISIBLE);
//			}
//		});
//
//	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.llt_my_managemoney: // 我的理财
			Intent intentL = new Intent(this, MineLiCaiActivity.class);
			intentL.putExtra("total", total);
			intentL.putExtra("totalprofit", totalprofit);
			startActivity(intentL);
			break;
		}
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
			String url = ades_ImageEs.get(slider.getP()).getUrl();
			Intent intent = new Intent(this, WebActivty.class);
			intent.putExtra(WebActivty.T, ades_ImageEs.get(slider.getP())
					.getName());
			intent.putExtra(WebActivty.U, url);
			intent.putExtra("Re", true);
			startActivity(intent);
		}
	}
}
