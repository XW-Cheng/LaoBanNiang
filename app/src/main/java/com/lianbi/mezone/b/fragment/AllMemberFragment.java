package com.lianbi.mezone.b.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.AssociatorListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.MemberEditActivity;
import com.lianbi.mezone.b.ui.MyMemberManagementActivity;

/**
 * 
 * @author guanghui.han 会员fm
 */
public class AllMemberFragment extends Fragment {

	private OkHttpsImp okHttpsImp;
	MyMemberManagementActivity mActivity;
	AbPullToRefreshView fm_messagefragment_abpulltorefreshview;
	ListView fm_messagefragment_listView;
	ArrayList<AssociatorListBean> associatorListBeans = new ArrayList<AssociatorListBean>();
	ImageView fm_messagefragment_iv_empty;
	private int currentPageNum = 0;
	String level = "";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mActivity = (MyMemberManagementActivity) getActivity();

		View view = inflater.inflate(R.layout.fm_messagefragment, null);
		initView(view);
		listen();
		initListAdapter();
		getAssociator(true);
		return view;
	}

	/**
	 * 获取数据MemberBean
	 */
	public void getAssociator(final boolean isResh) {
		if (isResh) {
			currentPageNum = 0;
		}
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(mActivity);
		okHttpsImp.getAssociacorByBusinessAndLevel(
				BaseActivity.userShopInfoBean.getBusinessId(), currentPageNum
						+ "", level, new MyResultCallback<String>() {
					@Override
					public void onResponseResult(Result result) {
						String resString = result.getData();
						try {
							JSONObject jsonObject = new JSONObject(resString);
							resString = jsonObject.getString("associatorList");

							if (isResh) {
								currentPageNum = 0;
								associatorListBeans.clear();
							}
							ArrayList<AssociatorListBean> associatorListBeansL = (ArrayList<AssociatorListBean>) JSON
									.parseArray(resString,
											AssociatorListBean.class);
							if (associatorListBeansL != null
									&& associatorListBeansL.size() > 0) {
								currentPageNum++;
								associatorListBeans
										.addAll(associatorListBeansL);
								fm_messagefragment_iv_empty
										.setVisibility(View.GONE);
								fm_messagefragment_abpulltorefreshview
										.setVisibility(View.VISIBLE);
								mAdapter.replaceAll(associatorListBeans);
							} else {
								fm_messagefragment_iv_empty
										.setVisibility(View.VISIBLE);
								fm_messagefragment_abpulltorefreshview
										.setVisibility(View.GONE);
							}
							hidle(isResh);
						} catch (JSONException e) {
							fm_messagefragment_iv_empty
									.setVisibility(View.VISIBLE);
							fm_messagefragment_abpulltorefreshview
									.setVisibility(View.GONE);
							hidle(isResh);
							e.printStackTrace();
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						fm_messagefragment_iv_empty.setVisibility(View.VISIBLE);
						fm_messagefragment_abpulltorefreshview
								.setVisibility(View.GONE);
						hidle(isResh);
					}
				});
	}

	QuickAdapter<AssociatorListBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<AssociatorListBean>(mActivity,
				R.layout.item_memberfmlist, associatorListBeans) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final AssociatorListBean item) {
				TextView member_phone_num = helper
						.getView(R.id.member_phone_num);
				TextView member_vip_num = helper.getView(R.id.member_vip_num);
				member_phone_num.setText(item.getAssociator_phone());
				member_vip_num.setText(item.getAssociator_level() + "");
			}
		};
		// 设置适配器
		fm_messagefragment_listView.setAdapter(mAdapter);
	}

	/**
	 * 隐藏刷新
	 * 
	 * @param isResh
	 */
	public void hidle(boolean isResh) {
		AbPullHide.hideRefreshView(isResh,
				fm_messagefragment_abpulltorefreshview);

	}

	private void listen() {
		fm_messagefragment_abpulltorefreshview
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getAssociator(true);

					}
				});
		fm_messagefragment_abpulltorefreshview
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						getAssociator(false);
					}
				});

		fm_messagefragment_listView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						mActivity.startActivityForResult(new Intent(mActivity,
								MemberEditActivity.class).putExtra("bean",
								associatorListBeans.get(arg2)),
								mActivity.REQUEST_ASSOCITOR);
					}
				});
	}

	private void initView(View view) {
		fm_messagefragment_abpulltorefreshview = (AbPullToRefreshView) view
				.findViewById(R.id.fm_messagefragment_abpulltorefreshview);
		fm_messagefragment_listView = (ListView) view
				.findViewById(R.id.fm_messagefragment_listView);
		fm_messagefragment_iv_empty = (ImageView) view
				.findViewById(R.id.fm_messagefragment_iv_empty);
	}

	/**
	 * 请求参数
	 * 
	 * @param string
	 */
	public void setLevel(String string) {
		this.level = string;
	}

	/**
	 * 刷新数据
	 */
	public void reFreshData() {
		getAssociator(true);
	}
}
