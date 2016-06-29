package com.lianbi.mezone.b.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.view.HttpDialog;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MoreServerMallBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.WebActivty;

/**
 * 
 * @author guanghui.han
 * @category我的服务
 */
public class MineServiceFragment extends Fragment {
	private OkHttpsImp okHttpsImp;
	Activity mActivity;
	/**
	 * 服务商城
	 */
	ArrayList<MoreServerMallBean> mDatas = new ArrayList<MoreServerMallBean>();
	private GridView grid_fm_mineservicefragment;
	private ImageView img_fm_mineservicefragment_empty;
	HttpDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mActivity = getActivity();
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(mActivity);
		View view = inflater.inflate(R.layout.fm_mineservicefragment, null);
		initView(view);
		initListAdapter();
		dialog = new HttpDialog(mActivity);
		dialog.show();
		getMoreServerMall();
		return view;
	}

	public void reFresh() {
		getMoreServerMall();

	}

	/**
	 * 获取已有服务商城
	 */
	private void getMoreServerMall() {
		okHttpsImp.getMoreServerMall(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						reString = jsonObject.getString("serverMallList");
						ArrayList<MoreServerMallBean> arrayListMall = (ArrayList<MoreServerMallBean>) JSON
								.parseArray(reString, MoreServerMallBean.class);
						if (arrayListMall != null && arrayListMall.size() > 0) {
							img_fm_mineservicefragment_empty
									.setVisibility(View.GONE);
							grid_fm_mineservicefragment
									.setVisibility(View.VISIBLE);
							updateView(arrayListMall);
						} else {
							img_fm_mineservicefragment_empty
									.setVisibility(View.VISIBLE);
							grid_fm_mineservicefragment
									.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
			}

			@Override
			public void onResponseFailed(String msg) {
				img_fm_mineservicefragment_empty.setVisibility(View.VISIBLE);
				grid_fm_mineservicefragment.setVisibility(View.GONE);
				dialog.dismiss();
			}
		}, BaseActivity.userShopInfoBean.getUserId());
	}

	/**
	 * 更新界面
	 * 
	 * @param arrayListMall2
	 */
	protected void updateView(ArrayList<MoreServerMallBean> arrayListMall2) {
		mDatas.clear();
		mDatas.addAll(arrayListMall2);
		mAdapter.replaceAll(mDatas);
	}

	QuickAdapter<MoreServerMallBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<MoreServerMallBean>(mActivity,
				R.layout.item_fm_mineservicefragment, mDatas) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final MoreServerMallBean item) {
				TextView tv1_item_fm_mineservicefragment = helper
						.getView(R.id.tv1_item_fm_mineservicefragment);
				ImageView iv_item_fm_mineservicefragment = helper
						.getView(R.id.iv_item_fm_mineservicefragment);
				tv1_item_fm_mineservicefragment.setText(item.getMall_name());
				String url = item.getIcon();
				Glide.with(mActivity).load(url).error(R.mipmap.defaultimg_11)
						.into(iv_item_fm_mineservicefragment);
			}
		};
		// 设置适配器
		grid_fm_mineservicefragment.setAdapter(mAdapter);
	}

	private void initView(View view) {
		img_fm_mineservicefragment_empty = (ImageView) view
				.findViewById(R.id.img_fm_mineservicefragment_empty);
		grid_fm_mineservicefragment = (GridView) view
				.findViewById(R.id.grid_fm_mineservicefragment);
		grid_fm_mineservicefragment
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String url = mDatas.get(arg2).getUrl();
						Intent intent = new Intent(mActivity, WebActivty.class);
						intent.putExtra(WebActivty.T, mDatas.get(arg2)
								.getMall_name());
						intent.putExtra(WebActivty.U, url);
						intent.putExtra("Re", true);
						startActivity(intent);
					}
				});
	}
}
