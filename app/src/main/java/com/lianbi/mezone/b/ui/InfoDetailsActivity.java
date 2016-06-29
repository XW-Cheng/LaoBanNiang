package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.LinearLayout;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.PagerSlidingTabStrip;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.InfoMessageBean;
import com.lianbi.mezone.b.fragment.InfoMessageFragment;
import com.lianbi.mezone.b.fragment.LeaveMessageFragment;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

public class InfoDetailsActivity extends BaseActivity {

	private ViewPager pager;
	private PagerSlidingTabStrip tabs;
	private LinearLayout ll_top_tabs;
	public static final int POSITION0 = 0;
	public static final int POSITION1 = 1;
	public static final int POSITION2 = 2;
	public static final int POSITION3 = 3;
	final String[] titles = { "待接单", "服务", "买单", "留言" };
	InfoMessageFragment allMessageFragment;
	InfoMessageFragment sMessageFragment;
	InfoMessageFragment orderMessageFragment;
	LeaveMessageFragment leavemessagefragment;

	/**
	 * 数据列表
	 */
	ArrayList<InfoMessageBean> arrayList = new ArrayList<InfoMessageBean>();
	ArrayList<InfoMessageBean> arrayList0 = new ArrayList<InfoMessageBean>();
	ArrayList<InfoMessageBean> arrayList1 = new ArrayList<InfoMessageBean>();
	ArrayList<InfoMessageBean> arrayList2 = new ArrayList<InfoMessageBean>();
	/**
	 * 当前位置
	 */
	ArrayList<InfoMessageBean> mDatas = new ArrayList<InfoMessageBean>();

	public int curPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_infodetails, NOTYPE);
		initView();
		listen();
		getMessageList(true);
		getJumpInfo();
		getPushMessages();

	}

	private void getJumpInfo() {
		String jumpinfo = getIntent().getStringExtra("TIAOZHUANXIAOXI");
		if ("fuwu".equals(jumpinfo)) {
			pager.setCurrentItem(1);
			getPushMessages1();
		} else if ("maidan".equals(jumpinfo)) {
			pager.setCurrentItem(2);
			getPushMessages2();
		} else {

		}
	}

	private void initView() {
		setPageTitle("信息详情");
		ll_top_tabs = (LinearLayout) findViewById(R.id.ll_top_tabs);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.ps_tabs_act_infodetails);
		pager = (ViewPager) findViewById(R.id.pager_act_infodetails);
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		tabs.setViewPager(pager);
	}

	boolean isDeteled = false;
	String position;

	private void listen() {
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				curPosition = arg0;
				position = String.valueOf(arg0);
				switch (arg0) {
				case 0:
					swtFmDo(arg0, isDeteled, false, arrayList);
					getPushMessages();
					break;
				case 1:
					swtFmDo(arg0, isDeteled, false, arrayList0);
					getPushMessages2();
					break;
				case 2:
					swtFmDo(arg0, isDeteled, false, arrayList1);
					getPushMessages1();

					break;
				case 3:
					swtFmDo(arg0, isDeteled, false, mDatas);
					getShowMessages();

					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void swtFmDo(int arg0, boolean isD, boolean isDel,
			ArrayList<InfoMessageBean> cuArrayList) {
		switch (arg0) {
		case POSITION0:
			if (allMessageFragment != null) {
				allMessageFragment.doSomething(isD, isDel, cuArrayList);
			}

			break;
		case POSITION1:

			if (sMessageFragment != null) {
				sMessageFragment.doSomething(isD, isDel, cuArrayList);
			}
			break;
		case POSITION2:
			if (orderMessageFragment != null) {
				orderMessageFragment.doSomething(isD, isDel, cuArrayList);
			}
			break;
		case POSITION3:
			if (leavemessagefragment != null) {
				leavemessagefragment.doSomething(isD, isDel, mDatas);
			}
			break;
		}
	}

	/**
	 * 
	 * 查询所有待审核的留言
	 */
	private void getShowMessages() {
		okHttpsImp.getShowMessages(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reString = result.getData();

				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						String listData = jsonObject.getString("msgList");
						// 清除数据
						mDatas.clear();
						ArrayList<InfoMessageBean> infoMessage = (ArrayList<InfoMessageBean>) JSON
								.parseArray(listData, InfoMessageBean.class);

						mDatas.addAll(infoMessage);
						leavemessagefragment.doSomething(false, false, mDatas);

					} catch (Exception e) {

						e.printStackTrace();
					}

				}

			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, userShopInfoBean.getBusinessId());
		{

		}

	}

	/**
	 * 4.13 查询推送消息
	 */

	private void getPushMessages() {
		okHttpsImp.getPushMessages(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						reString = jsonObject.getString("msgList");
						arrayList.clear();
						ArrayList<InfoMessageBean> pullMessage = (ArrayList<InfoMessageBean>) JSON
								.parseArray(reString, InfoMessageBean.class);

						for (int i = 0; i < pullMessage.size(); i++) {

						}
						arrayList.addAll(pullMessage);
						swtFmDo(POSITION0, false, false, arrayList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, userShopInfoBean.getBusinessId(), "0");

	}

	/**
	 * 服务
	 */
	private void getPushMessages2() {
		okHttpsImp.getPushMessages(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						reString = jsonObject.getString("msgList");
						arrayList0.clear();
						ArrayList<InfoMessageBean> pullMessage = (ArrayList<InfoMessageBean>) JSON
								.parseArray(reString, InfoMessageBean.class);
						for (int i = 0; i < pullMessage.size(); i++) {

						}
						arrayList0.addAll(pullMessage);
						swtFmDo(POSITION1, false, false, arrayList0);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, userShopInfoBean.getBusinessId(), "2");

	}

	/**
	 * 买单
	 */
	private void getPushMessages1() {
		okHttpsImp.getPushMessages(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String reString = result.getData();
				if (reString != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(reString);
						reString = jsonObject.getString("msgList");
						arrayList1.clear();
						ArrayList<InfoMessageBean> pullMessage = (ArrayList<InfoMessageBean>) JSON
								.parseArray(reString, InfoMessageBean.class);
						for (int i = 0; i < pullMessage.size(); i++) {
							/* pullMessage.get(i).setOrder("呼叫买单了"); */
						}

						arrayList1.addAll(pullMessage);
						swtFmDo(POSITION2, false, false, arrayList1);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, userShopInfoBean.getBusinessId(), "1");

	}

	private void getMessageList(boolean b) {
		// getShowMessages();
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// for (int i = 0; i < 20; i++) {
		// InfoMessageBean data = new InfoMessageBean();
		// data.setTablenum(i + "号桌");
		// data.setStatus(i % 2);
		// data.setOrder("有新订单");
		// data.setTime("10:00");
		// arrayList.add(data);
		// }
		// for (int i = 0; i < 20; i++) {
		// InfoMessageBean data = new InfoMessageBean();
		// data.setTablenum(i + "号桌");
		// data.setStatus(1);
		// data.setOrder("呼叫服务");
		// data.setTime("10:00");
		// arrayList0.add(data);
		// }
		// for (int i = 0; i < 20; i++) {
		// InfoMessageBean data = new InfoMessageBean();
		// data.setTablenum(i + "号桌");
		// data.setStatus(1);
		// data.setOrder("呼叫买单啦");
		// data.setTime("10:00");
		// arrayList1.add(data);
		// }
		// for (int i = 0; i < 20; i++) {
		// InfoMessageBean data = new InfoMessageBean();
		// data.setStatus(1);
		// /*data.setTablenum(i + "号桌" + "炒饭很好吃");
		// data.setTime("10:00");*/
		// arrayList2.add(data);
		// }
		// swtFmDo(POSITION0, false, false, arrayList);
		// swtFmDo(POSITION1, false, false, arrayList0);
		// swtFmDo(POSITION2, false, false, arrayList1);
		// swtFmDo(POSITION3, false, false, arrayList2);
		//
		// }
		// }, 80);

	}

	/**
	 * 修改推送消息状态
	 * 
	 * @param id
	 */

	public void changStatus(String id) {
		okHttpsImp.postChangeStatus(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				ContentUtils.showMsg(InfoDetailsActivity.this,"成功");
				getPushMessages();
				getPushMessages1();
				getPushMessages2();
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, BaseActivity.userShopInfoBean.getBusinessId(), id);
	}

	/**
	 * 删除审核消息
	 * 
	 * @param ids
	 *            消息id
	 */
	public void delteMsg(ArrayList<String> ids, boolean status) {
		StringBuffer sb = new StringBuffer();
		int s = ids.size();
		if (s > 0) {
			for (int i = 0; i < s; i++) {
				if (i == (s - 1)) {
					sb.append(ids.get(i));
				} else {
					sb.append(ids.get(i) + ",");

				}
			}
		} else {
			return;
		}
		if (status) {

			okHttpsImp.getDeleteMessages(new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {

					ContentUtils.showMsg(InfoDetailsActivity.this, "删除留言成功");
					// 刷新页面
					getShowMessages();
					
				}

				@Override
				public void onResponseFailed(String msg) {

				}
			}, BaseActivity.userShopInfoBean.getBusinessId(), sb.toString());

		} else {

			okHttpsImp.getAuditMessages(new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {

					ContentUtils.showMsg(InfoDetailsActivity.this, "审核留言成功");
					getShowMessages();
                      
				}

				@Override
				public void onResponseFailed(String msg) {

				}
			}, BaseActivity.userShopInfoBean.getBusinessId(), sb.toString());

		}
	}

	public class MyAdapter extends FragmentPagerAdapter {
		String[] _titles;

		public MyAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			_titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}

		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case POSITION0:
				if (allMessageFragment == null) {
					allMessageFragment = new InfoMessageFragment();
				}
				return allMessageFragment;
			case POSITION1:
				if (sMessageFragment == null) {
					sMessageFragment = new InfoMessageFragment();
				}
				return sMessageFragment;
			case POSITION2:
				if (orderMessageFragment == null) {
					orderMessageFragment = new InfoMessageFragment();
				}
				return orderMessageFragment;
			case POSITION3:
				if (leavemessagefragment == null) {
					leavemessagefragment = new LeaveMessageFragment();
				}
				return leavemessagefragment;
			}
			return null;
		}
	}
}
