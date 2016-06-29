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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.ChangeShopBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.impl.MyShopChange;

/**
 * 切换商铺
 * 
 * @time 下午7:30:51
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
public class ChangeShopActivity extends BaseActivity {
	private ListView lv_change_shop;
	private TextView tv_change_shop_add;
	private ArrayList<ChangeShopBean> datas = new ArrayList<ChangeShopBean>();
	/**
	 * 默认选择位置
	 */
	private int position = -1;

	private ImageView img_change_shop_add_empty;
	private String business_id = "", address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_shop, NOTYPE);
		initView();
		initAdapter();
		setLisenter();
	}


	@Override
	protected void onResume() {
		super.onResume();
		getBusinessByUser();
	}
	
	/**
	 * singletast 返回
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		getBusinessByUser();

	}

	/**
	 * 取得用户下的店铺
	 */
	private void getBusinessByUser() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getBusinessByUser(userShopInfoBean.getUserId(), uuid,
					"app", reqTime, new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reSting = result.getData();
							try {
								JSONObject jsonObject = new JSONObject(reSting);
								reSting = jsonObject.getString("modelList");
								if (!TextUtils.isEmpty(reSting)) {
									datas = (ArrayList<ChangeShopBean>) JSON
											.parseArray(reSting,
													ChangeShopBean.class);
									if (datas != null && datas.size() > 0) {
										lv_change_shop
												.setVisibility(View.VISIBLE);
										img_change_shop_add_empty
												.setVisibility(View.GONE);
										if (TextUtils.isEmpty(userShopInfoBean
												.getBusinessId())) {
											datas.get(0).setSelect(true);
											position = 0;
											business_id = datas.get(0)
													.getBusiness_id();
											setBino();
										} else {
											for (int i = 0; i < datas.size(); i++) {
												if (userShopInfoBean
														.getBusinessId()
														.equals(datas
																.get(i)
																.getBusiness_id())) {
													datas.get(i)
															.setSelect(true);
													position = i;
													business_id = datas.get(i)
															.getBusiness_id();
												}
											}
										}
										initAdapter();
									} else {
										lv_change_shop.setVisibility(View.GONE);
										img_change_shop_add_empty
												.setVisibility(View.VISIBLE);
									}
								}

							} catch (JSONException e) {
								lv_change_shop.setVisibility(View.GONE);
								img_change_shop_add_empty
										.setVisibility(View.VISIBLE);
								e.printStackTrace();
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							lv_change_shop.setVisibility(View.GONE);
							img_change_shop_add_empty
									.setVisibility(View.VISIBLE);
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化View
	 */
	@SuppressLint("ResourceAsColor")
	private void initView() {
		setPageTitle("切换商铺");
		setPageRightText("保存");
		setPageRightTextColor(R.color.colores_news_01);
		lv_change_shop = (ListView) findViewById(R.id.lv_change_shop);
		tv_change_shop_add = (TextView) findViewById(R.id.tv_change_shop_add);
		img_change_shop_add_empty = (ImageView) findViewById(R.id.img_change_shop_add_empty);
	}

	@Override
	protected void onTitleLeftClick() {
		myShopChange = null;
		super.onTitleLeftClick();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		tv_change_shop_add.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.tv_change_shop_add:
			Intent intent_add = new Intent(this, AddShopActivity.class);// 添加商铺
			startActivity(intent_add);
			break;
		}
	}

	QuickAdapter<ChangeShopBean> mAdapter;

	/**
	 * 设置Adapter
	 */
	private void initAdapter() {
		mAdapter = new QuickAdapter<ChangeShopBean>(this,
				R.layout.item_chang_shop, datas) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final ChangeShopBean item) {
				CheckBox chbx_change_shop = helper
						.getView(R.id.chbx_change_shop);
				TextView tv_change_shop_title = helper
						.getView(R.id.tv_change_shop_title);
				TextView tv_change_shop_phone = helper
						.getView(R.id.tv_change_shop_phone);
				TextView tv_change_shop_address = helper
						.getView(R.id.tv_change_shop_address);
				boolean isSel = item.isSelect();
				if (isSel) {
					position = helper.getPosition();
					chbx_change_shop.setChecked(isSel);
					business_id = item.getBusiness_id();
				} else {
					chbx_change_shop.setChecked(isSel);
				}
				tv_change_shop_title.setText(item.getBusiness_name());
				tv_change_shop_phone.setText(item.getPhone());
				tv_change_shop_address.setText(item.getAddress());
				chbx_change_shop.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 0; i < datas.size(); i++) {
							datas.get(i).setSelect(false);
						}
						address = datas.get(helper.getPosition()).getAddress();
						item.setSelect(true);
						mAdapter.replaceAll(datas);
					}
				});
			}
		};
		lv_change_shop.setAdapter(mAdapter);
	}

	public static MyShopChange myShopChange;

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		changeBussinessOnApp();
	}

	/**
	 * B端切换店铺
	 */
	private void changeBussinessOnApp() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		if (!TextUtils.isEmpty(business_id)) {
			try {
				okHttpsImp.changeBussinessOnApp(uuid,"app",reqTime,userShopInfoBean.getUserId(),
						business_id, new MyResultCallback<String>() {

							@Override
							public void onResponseResult(Result result) {
								MainActivity.isChangSHpe = true;
								setBinfoC();
								ContentUtils.showMsg(ChangeShopActivity.this,
										"切换商铺成功");
								if (myShopChange != null) {
									myShopChange.reFresh();
									myShopChange = null;
								} else {
									Intent intent = new Intent(
											ChangeShopActivity.this,
											MyShopActivity.class);
									startActivity(intent);
								}
								postClientId();
								finish();
							}

							@Override
							public void onResponseFailed(String msg) {
								ContentUtils.showMsg(ChangeShopActivity.this,
										"切换商铺失败");
							}
						});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ContentUtils.showMsg(this, "请选择商铺");
		}
	}
	private String mClientId;
	/**
	 * 根据店铺发送手机唯一识别标识
	 */
	protected void postClientId() {
		/**
		 * 获取手机唯一识别码CID
		 */
		if (PushManager.getInstance().getClientid(this) != null) {
			mClientId = PushManager.getInstance().getClientid(this);
		}
		System.out.println("手机唯一识别标识----"+mClientId);
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.postPhoneClientId(uuid, "app", reqTime,
					userShopInfoBean.getUserId(),
					userShopInfoBean.getBusinessId(), mClientId, "01",
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {

						}

						@Override
						public void onResponseFailed(String msg) {

						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置店铺信息
	 */
	private void setBino() {
		setBinfoC();
		if (myShopChange != null) {
			myShopChange.reFresh();
			myShopChange = null;
		}
	}

	private void setBinfoC() {
		userShopInfoBean.setBusinessId(business_id);
		userShopInfoBean.setIndustry_id(datas.get(position).getIndustry_id());
		userShopInfoBean.setAddress(datas.get(position).getAddress());
		userShopInfoBean.setNikeName(datas.get(position).getContact_name());
		userShopInfoBean.setPhone(datas.get(position).getPhone());
		userShopInfoBean.setShopName(datas.get(position).getBusiness_name());
		ContentUtils.putSharePre(ChangeShopActivity.this,
				Constants.SHARED_PREFERENCE_NAME, Constants.ADDRESS, address);
	}
}
