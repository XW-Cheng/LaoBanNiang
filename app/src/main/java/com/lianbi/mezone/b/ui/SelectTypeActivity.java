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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.IndustryListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 行业分类
 * 
 * @time 下午1:35:21
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
@SuppressLint("ResourceAsColor")
public class SelectTypeActivity extends BaseActivity {
	private ListView lv_add_shop_type;
	private ArrayList<IndustryListBean> industryListBeans = new ArrayList<IndustryListBean>();
	private ImageView img_add_shop_type_empty;
	/**
	 * 所选择行业分类在列表中的位置
	 */
	private int position = -1;
	/**
	 * 是否从新增商铺过来
	 */
	boolean isMyShop = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_type, NOTYPE);
		isMyShop = getIntent().getBooleanExtra("isMyShop", false);
		initView();
		initAdapter();
		setLisenter();
		getIndustryListofB();
	}

	/**
	 * 行业分类
	 */
	private void getIndustryListofB() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getIndustryListofB(uuid,"app",reqTime,new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					String resString = result.getData();
					try {
						JSONObject jsonObject = new JSONObject(resString);
						resString = jsonObject.getString("modelList");
						if (!TextUtils.isEmpty(resString)) {
							industryListBeans = (ArrayList<IndustryListBean>) JSON
									.parseArray(resString, IndustryListBean.class);
							if (industryListBeans != null
									&& industryListBeans.size() > 0) {
								img_add_shop_type_empty.setVisibility(View.GONE);
								lv_add_shop_type.setVisibility(View.VISIBLE);
								mAdapter.replaceAll(industryListBeans);
							} else {
								img_add_shop_type_empty.setVisibility(View.VISIBLE);
								lv_add_shop_type.setVisibility(View.GONE);
							}
						} else {
							img_add_shop_type_empty.setVisibility(View.VISIBLE);
							lv_add_shop_type.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						img_add_shop_type_empty.setVisibility(View.VISIBLE);
						lv_add_shop_type.setVisibility(View.GONE);
						e.printStackTrace();
					}
				}

				@Override
				public void onResponseFailed(String msg) {
					img_add_shop_type_empty.setVisibility(View.VISIBLE);
					lv_add_shop_type.setVisibility(View.GONE);
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
	private void initView() {
		setPageTitle("行业分类");
		setPageRightText("提交");
		setPageRightTextColor(R.color.colores_news_01);
		img_add_shop_type_empty = (ImageView) findViewById(R.id.img_add_shop_type_empty);
		lv_add_shop_type = (ListView) findViewById(R.id.lv_add_shop_type);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
	}

	QuickAdapter<IndustryListBean> mAdapter;

	/**
	 * 设置Adapter
	 */
	private void initAdapter() {
		mAdapter = new QuickAdapter<IndustryListBean>(this,
				R.layout.item_select_type, industryListBeans) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final IndustryListBean item) {
				LinearLayout llt_select_type_detail = helper
						.getView(R.id.llt_select_type_detail);
				CheckBox chbx_select_type = helper
						.getView(R.id.chbx_select_type);
				TextView tv_select_type_name = helper
						.getView(R.id.tv_select_type_name);
				tv_select_type_name.setText(item.getParant_name() + "-"
						+ item.getMaj_name());
				boolean isSel = item.isSelect();
				if (isSel) {
					position = helper.getPosition();
					chbx_select_type.setChecked(isSel);
				} else {
					chbx_select_type.setChecked(isSel);
				}
				chbx_select_type.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 0; i < industryListBeans.size(); i++) {
							industryListBeans.get(i).setSelect(false);
						}
						item.setSelect(true);
						mAdapter.replaceAll(industryListBeans);
					}
				});
				if (isMyShop) {
					ImageView iv = helper.getView(R.id.right_selecttype_iv);
					iv.setVisibility(View.VISIBLE);
					llt_select_type_detail
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent_detail = new Intent(
											SelectTypeActivity.this,
											TypeDetailActivity.class);
									intent_detail.putExtra("title",
											item.getParant_name());
									SelectTypeActivity.this
											.startActivity(intent_detail);
								}
							});
				}
			}
		};
		lv_add_shop_type.setAdapter(mAdapter);
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		if (position != -1) {
			Intent data = new Intent();
			String parant_id = industryListBeans.get(position).getIndustryId()
					+ "";
			String parant_name = industryListBeans.get(position)
					.getIndustryName();
			data.putExtra("parant_id", parant_id);
			data.putExtra("parant_name", parant_name);
			data.putExtra("bean", industryListBeans.get(position));
			setResult(RESULT_OK, data);
			finish();
		} else {
			ContentUtils.showMsg(this, "请选择行业类别");
		}
	}
}
