package com.lianbi.mezone.b.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.HttpDialog;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.TableSetBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.ui.AddTablesetActivity;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.ScanningQRActivity;
import com.lianbi.mezone.b.ui.ServiceMallActivity;
import com.lianbi.mezone.b.ui.TableSetActivity;

public class InBusinessFragment extends Fragment {

	ArrayList<TableSetBean> mDatas = new ArrayList<TableSetBean>();
	TableSetActivity mActivity;
	public int choice = 1;
	GridView gridview;
	public int POSITION;
	ImageView fm_tablesetfragment_iv_empty;
	/**
	 * 是否删除
	 */
	boolean isDeted;

	boolean isDel = true;
	private TableSetActivity mTableSetActivity;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mActivity = (TableSetActivity) getActivity();
		View view = inflater.inflate(R.layout.fm_inbusinessfragment, null);
		mTableSetActivity = (TableSetActivity) getActivity();
		initview(view);
		initListAdapter();
		setLisenter();
		return view;
	}

	private void setLisenter() {
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

	}

	private void initview(View view) {
		gridview = (GridView) view.findViewById(R.id.gview_inbuss);
		fm_tablesetfragment_iv_empty = (ImageView) view
				.findViewById(R.id.fm_tablesetfragment_iv_empty);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

	}

	public QuickAdapter<TableSetBean> mAdapter;

	private void initListAdapter() {
		mAdapter = new QuickAdapter<TableSetBean>(mActivity,
				R.layout.grid_tableset_item, mDatas) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final TableSetBean item) {
				final ImageView iv_tableset_bk = helper
						.getView(R.id.iv_tableset_bk);
				final TextView tv_tableset_num = helper
						.getView(R.id.tv_tableset_num);
				tv_tableset_num.setText(item.getTableName());
				if (POSITION == 1) {
					iv_tableset_bk.setImageResource(R.mipmap.icon_rest);
					tv_tableset_num
							.setBackgroundResource(R.drawable.shap_rounded_gray);
				} else if (item.getTableStatus() == 1) {
					iv_tableset_bk.setImageResource(R.mipmap.icon_batch);
					tv_tableset_num.setBackgroundResource(R.drawable.shap_navy);
				} else if (item.getTableStatus() == 3) {
					iv_tableset_bk.setImageResource(R.mipmap.icon_pay);
					tv_tableset_num
							.setBackgroundResource(R.drawable.shap_orange);
				} else if (item.getTableStatus() == 4) {
					iv_tableset_bk.setImageResource(R.mipmap.icon_service);
					tv_tableset_num.setBackgroundResource(R.drawable.shap_blue);
				} else if (item.getTableStatus() == 2) {
					iv_tableset_bk.setImageResource(R.mipmap.icon_waiting);
					tv_tableset_num.setBackgroundResource(R.drawable.shap_red);
				} else {
					iv_tableset_bk.setImageResource(R.mipmap.icon_rest);
					tv_tableset_num
							.setBackgroundResource(R.drawable.shap_rounded_gray);
				}

				if (isDeted) {
					if (item.isS()) {
						helper.setImageResource(R.id.iv_tableset_delete,
								R.mipmap.icon_check);
					} else {
						helper.setImageResource(R.id.iv_tableset_delete,
								R.mipmap.message_unchecked);
					}
				} else {
					helper.setImageResource(R.id.iv_tableset_delete, -1);
				}
				helper.getView(R.id.tv_viewqrcode).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (!isDeted) {
									Intent intent = new Intent(mActivity,
											ScanningQRActivity.class);
									intent.putExtra("TABLENAME",
											item.getTableName());
									intent.putExtra("TABLEQR",
											item.getCodeUrl());
									item.getTableName();
									intent.putExtra("TABLEID",
											String.valueOf(item.getTableId()));
									startActivity(intent);

									// Intent intentqr=new Intent();
									// intentqr.setClass(mActivity,
									// ScanningQRActivity.class);
									// intentqr.setFlags(API.REQUEST_CODE_SCANNINGQR_RESULT);
									// startActivityForResult(intentqr);
								}
							}
						});
				helper.getView(R.id.iv_tableset_bk).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (!isDeted && item.getTableStatus() != 1
										&& item.getTableStatus() != 0) {
									// 通知服务器
									// modifyTableStatus(String.valueOf(item.getTableId()),false,String.valueOf(item.getTableStatus()));
									modifyTableStatus(
											String.valueOf(item.getTableId()),
											false, String.valueOf(1));
									iv_tableset_bk
											.setImageResource(R.mipmap.icon_batch);
									tv_tableset_num
											.setBackgroundResource(R.drawable.shap_navy);
								}
							}
						});
				helper.getView(R.id.iv_tableset_delete).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (item.isS()) {
									item.setS(false);
									mAdapter.replaceAll(mDatas);
								} else {
									item.setS(true);
									mAdapter.replaceAll(mDatas);
								}
							}
						});

			}
		};
		gridview.setAdapter(mAdapter);
	}

	public void modifyTableStatus(String tableIds, boolean isBatch,
			String tableStatus) {
		if (isBatch == true) {
			StringBuffer sb = null;
			sb = new StringBuffer(256);
			StringBuffer strtableStatus = null;
			strtableStatus = new StringBuffer(256);
			if (1 == mDatas.size()) {
				sb.append(mDatas.get(0).getTableId());
				strtableStatus.append(1);
			} else {
				for (int i = 0; i < mDatas.size(); i++) {
					sb.append(mDatas.get(i).getTableId());
					sb.append(",");
					// strtableStatus.append(mDatas.get(i).getTableStatus());
					strtableStatus.append(1);
					Log.i("tag", "220桌位状态------》" + i);
					Log.i("tag", "mDatas.size()------》" +mDatas.size());
					if(i+1!=mDatas.size()){
					strtableStatus.append(",");
				}
				}
			}
			tableIds = sb.toString();
			tableStatus = strtableStatus.toString();
		}
		Log.i("tag", "214桌位状态------》" + tableStatus);
		OkHttpsImp okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP
				.newInstance(mTableSetActivity);
		okHttpsImp
				.getModifyTableStatus(new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						Log.i("tag", "成功");
						String reString = result.getData();
						System.out.println("reString-----" + reString);
						if (reString != null) {
							try {
								ContentUtils.showMsg(mTableSetActivity,
										"桌位恢复无服务状态");

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						Log.i("tag", "失败" + msg);

					}
				}, BaseActivity.userShopInfoBean.getBusinessId(), tableIds,
						"1");

	}

	/**
	 * @param isD
	 *            是否是删除状态
	 * @param isDel
	 *            是否删除
	 * @param arraylist
	 * @param position
	 *            位置
	 */
	public void doSomthing(boolean isD, boolean isDel,
			ArrayList<TableSetBean> arraylist, int position) {
		this.POSITION = position;
		this.isDeted = isD;
		this.isDel = isDel;
		if (isDel) {
			deleteTable();
		}
		if (arraylist != null && arraylist.size() > 0) {
			mDatas = arraylist;
			mAdapter.replaceAll(mDatas);
			gridview.setVisibility(View.VISIBLE);
			fm_tablesetfragment_iv_empty.setVisibility(View.GONE);
		} else {
			if (mDatas.size() > 0) {
				gridview.setVisibility(View.VISIBLE);
				fm_tablesetfragment_iv_empty.setVisibility(View.GONE);
				mAdapter.replaceAll(mDatas);
			} else {
				gridview.setVisibility(View.GONE);
				fm_tablesetfragment_iv_empty.setVisibility(View.VISIBLE);
			}
		}
	}

	public void deleteTable() {
		// 获取删除id
		int s = mDatas.size();
		ArrayList<String> ids = new ArrayList<String>();
		for (int i = 0; i < s; i++) {
			if (mDatas.get(i).isS()) {
				ids.add(mDatas.get(i).getTableId() + "");
				System.out.println(mDatas.get(i).getTableId());
			}
		}
		mActivity.deleteTable(ids, mDatas);
		ids.clear();

	}

}
