package com.lianbi.mezone.b.ui;

import java.text.DecimalFormat;
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
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.ProductSourceList;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 下单记录
 * 
 * @time 下午2:13:30
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class SupplyGoodsRecordActivity extends BaseActivity {
	private ListView lv_supply_goods;
	private AbPullToRefreshView pullre_supply_goods;
	private ArrayList<ProductSourceList> mDatas = new ArrayList<ProductSourceList>();
	private int currentPageNum = 0;
	private ImageView img_goods_supply_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_goods_record, NOTYPE);
		initView();
		setLisenter();
		initAdapter();
		getPrductSourceOrderByBusiness(true);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		pullre_supply_goods.setPullRefreshEnable(true);
		pullre_supply_goods.setLoadMoreEnable(true);
		pullre_supply_goods
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getPrductSourceOrderByBusiness(true);
					}
				});

		pullre_supply_goods.setOnFooterLoadListener(new OnFooterLoadListener() {

			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				getPrductSourceOrderByBusiness(false);
			}
		});
		lv_supply_goods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent_detail = new Intent(
						SupplyGoodsRecordActivity.this,
						SupplyRecordDetailActivity.class);
				intent_detail.putExtra("product", mDatas.get(position));
				SupplyGoodsRecordActivity.this.startActivity(intent_detail);
			}
		});
	}

	/**
	 * 获取货源下单列表
	 */
	private void getPrductSourceOrderByBusiness(final boolean isRresh) {
		if (isRresh) {
			currentPageNum = 0;
			mDatas.clear();
			mAdapter.replaceAll(mDatas);
		}
		okHttpsImp.getPrductSourceOrderByBusiness(
				userShopInfoBean.getBusinessId() + "", currentPageNum, 20,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						currentPageNum++;
						String reString = result.getData();
						try {
							JSONObject jsonObject = new JSONObject(reString);
							reString = jsonObject
									.getString("productSourceList");
							if (!TextUtils.isEmpty(reString)) {
								ArrayList<ProductSourceList> mDatasL = (ArrayList<ProductSourceList>) JSON
										.parseArray(reString,
												ProductSourceList.class);
								if (mDatasL != null && mDatasL.size() > 0) {
									mDatas.addAll(mDatasL);
								}
								if (mDatas != null && mDatas.size() > 0) {
									img_goods_supply_empty
											.setVisibility(View.GONE);
									pullre_supply_goods
											.setVisibility(View.VISIBLE);
								} else {
									img_goods_supply_empty
											.setVisibility(View.VISIBLE);
									pullre_supply_goods
											.setVisibility(View.GONE);
								}
							}
						} catch (JSONException e) {
							if (isRresh) {
								img_goods_supply_empty
										.setVisibility(View.VISIBLE);
								pullre_supply_goods.setVisibility(View.GONE);
							}
							AbPullHide.hideRefreshView(isRresh,
									pullre_supply_goods);
							e.printStackTrace();
						}
						AbPullHide
								.hideRefreshView(isRresh, pullre_supply_goods);
						mAdapter.replaceAll(mDatas);
					}

					@Override
					public void onResponseFailed(String msg) {
						if (isRresh) {
							img_goods_supply_empty.setVisibility(View.VISIBLE);
							pullre_supply_goods.setVisibility(View.GONE);
						}
						AbPullHide
								.hideRefreshView(isRresh, pullre_supply_goods);
					}
				});
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("下单记录");
		img_goods_supply_empty = (ImageView) findViewById(R.id.img_goods_supply_empty);
		lv_supply_goods = (ListView) findViewById(R.id.lv_supply_goods);
		pullre_supply_goods = (AbPullToRefreshView) findViewById(R.id.pullre_supply_goods);
	}

	private QuickAdapter<ProductSourceList> mAdapter;

	/**
	 * 设置Adapter
	 */
	private void initAdapter() {
		mAdapter = new QuickAdapter<ProductSourceList>(this,
				R.layout.item_supply_record, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					ProductSourceList item) {
				ImageView img_supply_record = helper
						.getView(R.id.img_supply_record);
				TextView tv_supply_record_sn = helper
						.getView(R.id.tv_supply_record_sn);
				TextView tv_supply_record_shop_name = helper
						.getView(R.id.tv_supply_record_shop_name);
				TextView tv_supply_amount = helper
						.getView(R.id.tv_supply_amount);
				TextView tv_supply_record_sum_price = helper
						.getView(R.id.tv_supply_record_sum_price);
				tv_supply_record_sn.setText(item.getOrder_id());
				DecimalFormat df = new DecimalFormat("######0.00");
				String price = df.format(item.getPrice());
				tv_supply_record_sum_price.setText("¥" + price);
				tv_supply_amount.setText("x" + item.getNum());
				tv_supply_record_shop_name.setText(item
						.getProduct_source_title());
				// 图片没有字段
				Glide.with(SupplyGoodsRecordActivity.this)
						.load(item.getImage()).error(R.mipmap.defaultimg_11)
						.into(img_supply_record);
			}
		};
		// 设置适配器
		lv_supply_goods.setAdapter(mAdapter);
	}
}
