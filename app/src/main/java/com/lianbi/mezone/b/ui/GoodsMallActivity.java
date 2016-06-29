package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lianbi.mezone.b.bean.IndustryListBean;
import com.lianbi.mezone.b.bean.ProductSourceListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 货源商城
 * 
 * @time 下午4:03:50
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class GoodsMallActivity extends BaseActivity {
	private ListView lv_goods_mall_father, lv_goods_mall_sun;
	private AbPullToRefreshView pullre_goods_mall;
	private ImageView img_goods_mall_empty;
	/**
	 * 当前页面数
	 */
	private int currentPageNum = 0;
	/**
	 * 行业类别 行业类别 多个用英文“,”逗号隔开
	 */
	private String cateId;
	private ArrayList<ProductSourceListBean> arrayListRight = new ArrayList<ProductSourceListBean>();
	private ArrayList<IndustryListBean> industryListBeans = new ArrayList<IndustryListBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_mall, NOTYPE);
		initView();
		setLisenter();
		initAdapterFather();
		initAdapterSun();
		getIndustryListofB();
	}

	/**
	 * 行业分类
	 */
	private void getIndustryListofB() {
//		okHttpsImp.getIndustryListofB(new MyResultCallback<String>() {
//
//			@Override
//			public void onResponseResult(Result result) {
//				String resString = result.getData();
//				try {
//					JSONObject jsonObject = new JSONObject(resString);
//					resString = jsonObject.getString("industryList");
//					if (!TextUtils.isEmpty(resString)) {
//						industryListBeans = (ArrayList<IndustryListBean>) JSON
//								.parseArray(resString, IndustryListBean.class);
//						if (industryListBeans != null
//								&& industryListBeans.size() > 0) {
//							industryListBeans.get(0).setSelect(true);
//							cateId = industryListBeans.get(0).getMajor_id();
//							getProductSourceList(true);
//							mAdapter.replaceAll(industryListBeans);
//						} else {
//						}
//					} else {
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onResponseFailed(String msg) {
//
//			}
//		});
	}

	/**
	 * 获取货源列表
	 */
	private void getProductSourceList(final boolean isRefresh) {
		if (isRefresh) {
			currentPageNum = 0;
			arrayListRight.clear();
			maAdapter.replaceAll(arrayListRight);
		}
		okHttpsImp.getProductSourceList(cateId + "", currentPageNum, 20,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						String reString = result.getData();
						try {
							JSONObject jsonObject = new JSONObject(reString);
							currentPageNum++;
							reString = jsonObject
									.getString("productSourceList");
							ArrayList<ProductSourceListBean> arrayListRightLi = (ArrayList<ProductSourceListBean>) JSON
									.parseArray(reString,
											ProductSourceListBean.class);
							arrayListRight.addAll(arrayListRightLi);
							if (arrayListRightLi != null
									&& arrayListRightLi.size() > 0) {
								img_goods_mall_empty.setVisibility(View.GONE);
								pullre_goods_mall.setVisibility(View.VISIBLE);
							} else {
								img_goods_mall_empty
										.setVisibility(View.VISIBLE);
								pullre_goods_mall.setVisibility(View.GONE);
							}
							AbPullHide.hideRefreshView(isRefresh,
									pullre_goods_mall);
							maAdapter.replaceAll(arrayListRight);
						} catch (JSONException e) {
							AbPullHide.hideRefreshView(isRefresh,
									pullre_goods_mall);
							e.printStackTrace();
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						if (isRefresh) {
							img_goods_mall_empty.setVisibility(View.VISIBLE);
							pullre_goods_mall.setVisibility(View.GONE);
						}
						AbPullHide.hideRefreshView(true, pullre_goods_mall);
					}
				});
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("货源商城");
		img_goods_mall_empty = (ImageView) findViewById(R.id.img_goods_mall_empty);
		lv_goods_mall_father = (ListView) findViewById(R.id.lv_goods_mall_father);
		lv_goods_mall_sun = (ListView) findViewById(R.id.lv_goods_mall_sun);
		pullre_goods_mall = (AbPullToRefreshView) findViewById(R.id.pullre_goods_mall);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		pullre_goods_mall
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getProductSourceList(true);
					}

				});
		pullre_goods_mall.setOnFooterLoadListener(new OnFooterLoadListener() {

			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				getProductSourceList(false);
			}
		});
		lv_goods_mall_sun.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent_detail = new Intent(GoodsMallActivity.this,
						GoodsDetailActivity.class);
				intent_detail.putExtra("productSourceId",
						arrayListRight.get(position).getProduct_source_id());
				GoodsMallActivity.this.startActivity(intent_detail);
			}
		});
		lv_goods_mall_father.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position1, long id) {
				for (int i = 0; i < industryListBeans.size(); i++) {
					industryListBeans.get(i).setSelect(false);
				}
				cateId = industryListBeans.get(position1).getMajor_id();
				position = position1;
				industryListBeans.get(position1).setSelect(true);
				mAdapter.replaceAll(industryListBeans);
				getProductSourceList(true);
			}
		});
	}

	int position = 0;
	QuickAdapter<IndustryListBean> mAdapter;
	/**
	 * 设置Adapter Father
	 */
	private void initAdapterFather() {
		mAdapter = new QuickAdapter<IndustryListBean>(
				this, R.layout.item_goods_mall_fa, industryListBeans) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					IndustryListBean item) {
				TextView tv_goods_fa_red = helper.getView(R.id.tv_goods_fa_red);
				// TODO 没有图片
				ImageView img_goods_fa = helper.getView(R.id.img_goods_fa);
				TextView tv_goods_fa = helper.getView(R.id.tv_goods_fa);
				tv_goods_fa.setText(item.getParant_name() + "-"
						+ item.getMaj_name());
				LinearLayout llt_goods_fa = helper.getView(R.id.llt_goods_fa);
				if (item.isSelect()) {
					tv_goods_fa_red.setVisibility(View.VISIBLE);
					llt_goods_fa.setBackgroundResource(R.color.white);
				} else {
					tv_goods_fa_red.setVisibility(View.INVISIBLE);
					llt_goods_fa.setBackgroundResource(R.color.color_f2f2f2);
				}
			}
		};
		// 设置适配器
		lv_goods_mall_father.setAdapter(mAdapter);
	}
	QuickAdapter<ProductSourceListBean> maAdapter;
	/**
	 * 设置Adapter sun
	 */
	private void initAdapterSun() {
		maAdapter= new QuickAdapter<ProductSourceListBean>(
				this, R.layout.item_goods_mall_sun, arrayListRight) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					ProductSourceListBean item) {
				ImageView img_goods_mall_sun = helper
						.getView(R.id.img_goods_mall_sun);
				TextView tv_goods_mall_name = helper
						.getView(R.id.tv_goods_mall_name);
				TextView tv_goods_mall_price = helper
						.getView(R.id.tv_goods_mall_price);
				tv_goods_mall_name.setText(item.getProduct_source_title());
				try {
					String p = item.getPrice();
					String[] dd = p.split(",");
					if (dd.length > 0) {
						String[] pp = dd[0].split("-");
						p = pp[0] + pp[1];
					}
					String amount = item.getUnit();// 50-hh,100-gg
					String[] amountD = amount.split(",");
					if (amountD.length > 0) {
						String[] pp = amountD[0].split("-");
						amount = pp[1];
					}
					SpannableuUtills.setSpannableu(tv_goods_mall_price,
							MoneyFlag.MONEYFAAG, p, "/" + amount);
				} catch (Exception e) {
				}
				Glide.with(GoodsMallActivity.this).load(item.getImage())
						.error(R.mipmap.source_default)
						.into(img_goods_mall_sun);
			}
		};
		// 设置适配器
		lv_goods_mall_sun.setAdapter(maAdapter);
	}
}
