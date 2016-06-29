package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.TelPhoneUtills;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.ConnectGoodsBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 联系货源
 * 
 * @time 上午11:26:37
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
@SuppressLint("ResourceAsColor")
public class ConnectGoodsActivity extends BaseActivity {
	private ListView lv_connect_goods;
	private ImageView img_connect_goods_empty;
	private final int REQUEST_ADDCONNECT = 2005;

	private ArrayList<ConnectGoodsBean> arrayList = new ArrayList<ConnectGoodsBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_goods, NOTYPE);
		initView();
		initAdapter();
		getProductSourceContacts();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("联系货源");
		setPageRightText("新增");
		setPageRightTextColor(R.color.colores_news_01);
		lv_connect_goods = (ListView) findViewById(R.id.lv_connect_goods);
		img_connect_goods_empty = (ImageView) findViewById(R.id.img_connect_goods_empty);
	}

	QuickAdapter<ConnectGoodsBean> mAdapter;

	/**
	 * 设置Adapter
	 */
	private void initAdapter() {
		mAdapter = new QuickAdapter<ConnectGoodsBean>(this,
				R.layout.item_connect_goods, arrayList) {
			@Override
			protected void convert(BaseAdapterHelper helper,
					ConnectGoodsBean item) {
				ImageView img_connect_goods_img = helper
						.getView(R.id.img_connect_goods_img);
				LinearLayout llt_connect_goods_phone = helper
						.getView(R.id.llt_connect_goods_phone);
				TextView tv_connect_goods_shop_name = helper
						.getView(R.id.tv_connect_goods_shop_name);
				TextView tv_connect_goods_people_name = helper
						.getView(R.id.tv_connect_goods_people_name);
				TextView tv_connect_goods_people_phone = helper
						.getView(R.id.tv_connect_goods_people_phone);
				TextView tv_my_supplygoods__detail = helper
						.getView(R.id.tv_my_supplygoods__detail);
				final String phone = item.getContacts_phone();
				tv_connect_goods_shop_name.setText(item
						.getContacts_sourceName());
				tv_connect_goods_people_name.setText(item.getContacts_name());
				tv_connect_goods_people_phone.setText(phone);
				tv_my_supplygoods__detail.setText(item
						.getContacts_srouceDetail());
				llt_connect_goods_phone
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								TelPhoneUtills.launchPhone(
										ConnectGoodsActivity.this, phone);
							}
						});
				Glide.with(ConnectGoodsActivity.this)
						.load(item.getContacts_img())
						.error(R.mipmap.defaultimg_11)
						.into(img_connect_goods_img);
			}
		};
		// 设置适配器
		lv_connect_goods.setAdapter(mAdapter);
	}

	/**
	 * 显示货源联系人列表
	 */
	private void getProductSourceContacts() {
		okHttpsImp.getProductSourceContacts(userShopInfoBean.getUserId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						String resString = result.getData();

						if (!TextUtils.isEmpty(resString)) {
							try {
								JSONObject jb = new JSONObject(resString);
								String re = jb.getString("contactsList");

								arrayList = (ArrayList<ConnectGoodsBean>) JSON
										.parseArray(re, ConnectGoodsBean.class);
								if (arrayList != null && arrayList.size() > 0) {
									lv_connect_goods
											.setVisibility(View.VISIBLE);
									img_connect_goods_empty
											.setVisibility(View.GONE);
									mAdapter.replaceAll(arrayList);
								} else {
									lv_connect_goods.setVisibility(View.GONE);
									img_connect_goods_empty
											.setVisibility(View.VISIBLE);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}

					@Override
					public void onResponseFailed(String msg) {
						lv_connect_goods.setVisibility(View.GONE);
						img_connect_goods_empty.setVisibility(View.VISIBLE);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_ADDCONNECT:
				getProductSourceContacts();
				break;
			}
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		Intent intent_add = new Intent(this, AddConnectGoodsActivity.class);
		startActivityForResult(intent_add, REQUEST_ADDCONNECT);
	}
}
