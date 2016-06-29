package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.AssociatorListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 会员新增页面
 * 
 * @author qiuyu.lv
 * @date 2016-1-18
 * @version
 */
public class AddNewMemberActivity extends BaseActivity {

	ListView addmember_list;
	AbPullToRefreshView add_member_abpulltorefreshview;
	private ArrayList<AssociatorListBean> associatorListBeans = new ArrayList<AssociatorListBean>();
	private ImageView iv_add_member_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_menber, NOTYPE);
		initView();
		initListAdapter();
		getAssociator();
	}

	/**
	 * 查询门店下的所有会员
	 */
	private void getAssociator() {
		okHttpsImp.getAssociator(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String resString = result.getData();
				try {
					JSONObject jsonObject = new JSONObject(resString);
					resString = jsonObject.getString("associatorList");
				} catch (JSONException e) {
					iv_add_member_empty.setVisibility(View.VISIBLE);
					add_member_abpulltorefreshview.setVisibility(View.GONE);
					e.printStackTrace();
				}
				associatorListBeans.clear();
				associatorListBeans = (ArrayList<AssociatorListBean>) JSON
						.parseArray(resString, AssociatorListBean.class);
				if (associatorListBeans != null
						&& associatorListBeans.size() > 0) {
					iv_add_member_empty.setVisibility(View.GONE);
					add_member_abpulltorefreshview.setVisibility(View.VISIBLE);
					mAdapter.replaceAll(associatorListBeans);
				} else {
					iv_add_member_empty.setVisibility(View.VISIBLE);
					add_member_abpulltorefreshview.setVisibility(View.GONE);
				}
			}

			@Override
			public void onResponseFailed(String msg) {
				iv_add_member_empty.setVisibility(View.VISIBLE);
				add_member_abpulltorefreshview.setVisibility(View.GONE);
			}
		}, userShopInfoBean.getBusinessId());
	}

	private void initView() {
		setPageTitle("会员新增");
		add_member_abpulltorefreshview = (AbPullToRefreshView) findViewById(R.id.add_member_abpulltorefreshview);
		addmember_list = (ListView) findViewById(R.id.addmember_list);
		iv_add_member_empty = (ImageView) findViewById(R.id.iv_add_member_empty);
		setLisenter();
	}

	QuickAdapter<AssociatorListBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<AssociatorListBean>(this,
				R.layout.item_addmember, associatorListBeans) {
			@Override
			protected void convert(BaseAdapterHelper helper,
					final AssociatorListBean item) {
				TextView tv_phone = helper.getView(R.id.member_phone_tv);
				TextView member_accept_btn = helper
						.getView(R.id.member_accept_btn);// 同意
				TextView member_refuse_btn = helper
						.getView(R.id.member_refuse_btn);// 拒绝
				tv_phone.setText(item.getAssociator_phone());
				member_refuse_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						updateApplyStatus(item.getAssociator_id(), 0);
					}
				});
				member_accept_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						updateApplyStatus(item.getAssociator_id(), 2);
					}
				});
			}
		};
		// 设置适配器
		addmember_list.setAdapter(mAdapter);
	}

	private void setLisenter() {
		add_member_abpulltorefreshview.setPullRefreshEnable(false);
		add_member_abpulltorefreshview.setLoadMoreEnable(false);
		// add_member_abpulltorefreshview
		// .setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
		//
		// @Override
		// public void onHeaderRefresh(AbPullToRefreshView view) {
		// AbPullHide.hideRefreshView(true,
		// add_member_abpulltorefreshview);
		// }
		// });
		// add_member_abpulltorefreshview
		// .setOnFooterLoadListener(new OnFooterLoadListener() {
		//
		// @Override
		// public void onFooterLoad(AbPullToRefreshView view) {
		// AbPullHide.hideRefreshView(false,
		// add_member_abpulltorefreshview);
		//
		// }
		// });
	}

	protected void updateApplyStatus(int associatorId, int status) {
		okHttpsImp.updateApplyStatus(associatorId + "", status + "",
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						ContentUtils.showMsg(AddNewMemberActivity.this, "操作成功");
						getAssociator();
					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});
	}

}
