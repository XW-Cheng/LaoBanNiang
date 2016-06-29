package com.lianbi.mezone.b.fragment;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.view.SlideListView2;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.InfoMessageBean;
import com.lianbi.mezone.b.ui.InfoDetailsActivity;
import com.tencent.mm.sdk.modelmsg.GetMessageFromWX;

public class LeaveMessageFragment extends Fragment {

	private SlideListView2 fm_messagefragment_listView;
	private ImageView fm_messagefragment_iv_empty, iv_selectall;
	ArrayList<InfoMessageBean> mDatas = new ArrayList<InfoMessageBean>();
	private TextView tv_leavemessage;
	private TextView time;
	private TextView tv_tablename;
	private LinearLayout tv_info;
    private TextView tv_chshenhe;
	/**
	 * 是否删除
	 */
	boolean isDeted;
	private InfoDetailsActivity mActivity;
	// private OkHttpsImp okHttpsImp;
	private TextView tv_deletemessage, tv_toexamine, tv_seleteall;
  
	private boolean isSeleteAll = false;

	// private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fm_leavemessage, null);
		mActivity = (InfoDetailsActivity) getActivity();
		initView(view);
		listen();
		initListAdapter();
		return view;
	}
	private void initView(View view) {
		fm_messagefragment_listView = (SlideListView2) view
				.findViewById(R.id.fm_messagefragment_listView);
		fm_messagefragment_listView.initSlideMode(SlideListView2.MOD_RIGHT);

		fm_messagefragment_iv_empty = (ImageView) view
				.findViewById(R.id.fm_messagefragment_iv_empty);
		tv_deletemessage = (TextView) view.findViewById(R.id.tv_deletemessage);// 删除
		tv_toexamine = (TextView) view.findViewById(R.id.tv_toexamine);// 审核
		iv_selectall = (ImageView) view.findViewById(R.id.iv_selectall);// 全选
		tv_seleteall = (TextView) view.findViewById(R.id.tv_seleteall);// 全选
	}

	private void listen() {
		iv_selectall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isSeleteAll) {
					isSeleteAll = false;
					iv_selectall
							.setBackgroundResource(R.mipmap.message_unchecked);
					tv_seleteall.setText("全选");
					for (int i = 0; i < mDatas.size(); i++) {
						mDatas.get(i).setS(false);
					}
					mAdapter.replaceAll(mDatas);
				} else {
					isSeleteAll = true;
					iv_selectall
							.setBackgroundResource(R.mipmap.message_checked);
					tv_seleteall.setText("全不选");
					for (int i = 0; i < mDatas.size(); i++) {
						mDatas.get(i).setS(true);
					}
					mAdapter.replaceAll(mDatas);
				}

			}
		});
		tv_deletemessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setExamineAndDelete(true);
			
			}
			
		});
		
		
				
		tv_toexamine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i=0;i<mDatas.size();i++){
					if(!"0".equals(mDatas.get(i).getAuditStatus())){
						tv_tablename.setTextColor(Color.RED);
						tv_leavemessage.setTextColor(Color.RED);
						time.setTextColor(Color.RED);
					}
				}
				setExamineAndDelete(false);
			
			}
		});
		}
	


	private void setExamineAndDelete(boolean status) {
		int s = mDatas.size();
		ArrayList<String> ids = new ArrayList<String>();
		for (int i = 0; i < s; i++) {
			if (mDatas.get(i).isS()) {
				ids.add(mDatas.get(i).getId() + "");
			}
		}
		if (status) {// 删除
			mActivity.delteMsg(ids, status);
			iv_selectall
			.setBackgroundResource(R.mipmap.message_unchecked);
		} else {// 审核
			mActivity.delteMsg(ids, status);
			iv_selectall
			.setBackgroundResource(R.mipmap.message_unchecked);
			tv_seleteall.setText("全选");
		}
	}

	/**
	 * 逐条(或批量)审核留言 storeId, msgIds
	 * 
	 */

	private void auditMessages() {

	}

	/**
	 * 逐条(或批量)删除留言 getDeleteMessages
	 * 
	 */
	private void getDeleteMessages() {

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
      
		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public QuickAdapter<InfoMessageBean> mAdapter;

	int id;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<InfoMessageBean>(mActivity,
				R.layout.leavemessageitem, mDatas) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final InfoMessageBean item) {
				id = item.getId();
				tv_info = helper.getView(R.id.tv_info);
				ImageView iv_check = helper.getView(R.id.iv_check);
                tv_chshenhe=helper.getView(R.id.tv_chshenhe);
				tv_tablename = helper.getView(R.id.tv_tablename);
				tv_leavemessage = helper.getView(R.id.tv_leavemessage);
				time = helper.getView(R.id.tv_time);
               
				tv_leavemessage = helper.getView(R.id.tv_leavemessage);
				TextView time = helper.getView(R.id.tv_time);
				LinearLayout tv_info = helper.getView(R.id.tv_info);


				tv_tablename.setText(item.getTableName());
				time.setText(String.valueOf(item.getCreateTime()));
				tv_leavemessage.setText(item.getContent());
				
				
				helper.getView(R.id.tv_chdelete).setOnClickListener(// 删除
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								fm_messagefragment_listView.slideBack();
								// 通知服务器
								mDatas.remove(item);
								Toast.makeText(mActivity, "删除", 0).show();
								mAdapter.replaceAll(mDatas);
								ArrayList<String> ids = new ArrayList<String>();
								ids.add(String.valueOf(item.getId()));
								mActivity.delteMsg(ids,true);
							} 
						});
				if ("1".equals(item.getAuditStatus())) {
					tv_chshenhe.setVisibility(View.GONE);
	           
				}
				if ("0".equals(item.getAuditStatus())) {
					tv_tablename.setTextColor(0xff18b08a);
					tv_leavemessage.setTextColor(0xff18b08a);
					time.setTextColor(0xff18b08a);

				
					helper.getView(R.id.tv_chshenhe).setOnClickListener(// 审核
							new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									fm_messagefragment_listView.slideBack();
									// 通知服务器
									mDatas.remove(item);
									//Toast.makeText(mActivity, "审核", 0).show();
									mAdapter.replaceAll(mDatas);
									ArrayList<String> ids = new ArrayList<String>();
									ids.add(String.valueOf(item.getId()));
									mActivity.delteMsg(ids,false);
									
								}
							});
				}
					else {
						tv_tablename.setTextColor(Color.BLACK);
						tv_leavemessage.setTextColor(Color.BLACK);
						time.setTextColor(Color.BLACK);
				}
				if (item.isS()) {
					helper.setImageResource(R.id.iv_check,
							R.mipmap.message_checked);

				} else {
					helper.setImageResource(R.id.iv_check,
							R.mipmap.message_unchecked);
				}

				
				iv_check.setOnClickListener(new OnClickListener() {

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

		fm_messagefragment_listView.setAdapter(mAdapter);
	}

	/**
	 * 
	 * @param isD是否删除状态
	 * @param isDel
	 *            是否删除
	 * @param cuArrayList
	 */
	public void doSomething(boolean isD, boolean isDel,
			ArrayList<InfoMessageBean> cuArrayList) {
		isDeted = isD;
		
		if (cuArrayList != null && cuArrayList.size() > 0) {
			mDatas = cuArrayList;
			mAdapter.replaceAll(mDatas);
			fm_messagefragment_iv_empty.setVisibility(View.GONE);
			fm_messagefragment_listView.setVisibility(View.VISIBLE);
		} else {
			if (mDatas.size() > 0) {
				fm_messagefragment_iv_empty.setVisibility(View.GONE); 
				fm_messagefragment_listView.setVisibility(View.VISIBLE);
				mAdapter.replaceAll(mDatas);
			} else {
				fm_messagefragment_iv_empty.setVisibility(View.VISIBLE);
				fm_messagefragment_listView.setVisibility(View.GONE);
			}
		}
	}
}
