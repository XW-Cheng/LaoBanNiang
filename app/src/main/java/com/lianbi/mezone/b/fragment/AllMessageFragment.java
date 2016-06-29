package com.lianbi.mezone.b.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MessageBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.ui.MessageDetailActivty;
import com.lianbi.mezone.b.ui.MineMsgActivity;

/**
 * 
 * @author guanghui.han
 * @category消息全部
 */
public class AllMessageFragment extends Fragment {
	private OkHttpsImp httpsImp;
	MineMsgActivity mActivity;
	AbPullToRefreshView fm_messagefragment_abpulltorefreshview;
	ListView fm_messagefragment_listView;
	ArrayList<MessageBean> mDatas = new ArrayList<MessageBean>();
	/**
	 * 是否删除
	 */
	boolean isDeted;
	ImageView fm_messagefragment_iv_empty;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mActivity = (MineMsgActivity) getActivity();
		View view = inflater.inflate(R.layout.fm_messagefragment, null);
		httpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(mActivity);
		initView(view);
		initListAdapter();
		listen();
		return view;
	}

	QuickAdapter<MessageBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<MessageBean>(mActivity,
				R.layout.messageitem, mDatas) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final MessageBean item) {
				helper.setText(R.id.messageitem_tv, item.getMessage_title());
				helper.setText(R.id.messageitem_time_tv,
						item.getMessageCreateTime());
				helper.setText(R.id.messageitem_content_tv,
						item.getMessage_content());
				if (isDeted) {
					if (item.isS()) {
						helper.setImageResource(R.id.messageitem_iv,
								R.mipmap.message_checked);

					} else {
						helper.setImageResource(R.id.messageitem_iv,
								R.mipmap.message_unchecked);
					}
				} else {
					if (item.getIs_read() == 1) {
						helper.setImageResource(R.id.messageitem_iv, -1);
					} else {
						helper.setImageResource(R.id.messageitem_iv,
								R.mipmap.icon_redpoint);

					}

				}
				helper.getView(R.id.messageitem_iv).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
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
						mActivity.getMessageList(true);

					}
				});
		fm_messagefragment_abpulltorefreshview
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						mActivity.getMessageList(false);
					}
				});

		fm_messagefragment_listView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
//						httpsImp.updateMessage(
//								mDatas.get(arg2).getMessage_id(),
//								new MyResultCallback<String>() {
//
//									@Override
//									public void onResponseResult(Result result) {
//									}
//
//									@Override
//									public void onResponseFailed(String msg) {
//
//									}
//								});
						System.out.println("点击");
//						mActivity.startActivityForResult(new Intent(mActivity,
//								MessageDetailActivty.class).putExtra(
//								"MessageBean", mDatas.get(arg2)),
//								mActivity.MESSAGEDETAILACTIVTY_CODE);

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
		fm_messagefragment_abpulltorefreshview.setLoadMoreEnable(false);
		fm_messagefragment_abpulltorefreshview.setPullRefreshEnable(false);
	}

	/**
	 * 
	 * @param isD是否删除状态
	 * @param isDel
	 *            是否删除
	 * @param cuArrayList
	 */
	public void doSomething(boolean isD, boolean isDel,
			ArrayList<MessageBean> cuArrayList) {
		isDeted = isD;
		if (isDel) {
			// 获取id删除
			int s = mDatas.size();
			ArrayList<String> ids = new ArrayList<String>();
			for (int i = 0; i < s; i++) {
				if (mDatas.get(i).isS()) {
					ids.add(mDatas.get(i).getMessage_id() + "");
				}
			}
			mActivity.delteMsg(ids);
		}
		if (cuArrayList != null && cuArrayList.size() > 0) {
			mDatas = cuArrayList;
			mAdapter.replaceAll(mDatas);
		} else {
			if (mDatas.size() > 0) {
				fm_messagefragment_abpulltorefreshview
						.setVisibility(View.VISIBLE);
				fm_messagefragment_iv_empty.setVisibility(View.GONE);
				mAdapter.replaceAll(mDatas);
			} else {
				fm_messagefragment_abpulltorefreshview.setVisibility(View.GONE);
				fm_messagefragment_iv_empty.setVisibility(View.VISIBLE);
			}
		}
	}

}
