package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.PagerSlidingTabStrip;

import com.alibaba.fastjson.JSONObject;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.MessageBean;
import com.lianbi.mezone.b.bean.TableSetBean;
import com.lianbi.mezone.b.fragment.AllMessageFragment;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

@SuppressLint("ResourceAsColor")
public class MineMsgActivity extends BaseActivity {
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	final String[] titles = { "全部", "系统消息", "订单消息", "定制消息" };
	AllMessageFragment allMessageFragment;
	AllMessageFragment sMessageFragment;
	AllMessageFragment orderMessageFragment;
	AllMessageFragment personMadeMessageFragment;
	public static final int POSITION0 = 0;
	public static final int POSITION1 = 1;
	public static final int POSITION2 = 2;
	public static final int POSITION3 = 3;
	public final int MESSAGEDETAILACTIVTY_CODE = 3001;
	/**
	 * 当前的位置
	 */
	public int curPosition = 0;

	private int page = 0;

	@Override
	protected void onTitleLeftClick() {
		setResult(RESULT_OK);
		super.onTitleLeftClick();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case MESSAGEDETAILACTIVTY_CODE:
				getMessageList(true);
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_minemsg, NOTYPE);
		initView();
		listen();
		getMessageList(true);
	}

	private void listen() {
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				curPosition = arg0;
				switch (arg0) {
				case 0:
					swtFmDo(arg0, isDeteled, false, arrayList);
					break;
				case 1:
					swtFmDo(arg0, isDeteled, false, arrayList0);
					break;
				case 2:
					swtFmDo(arg0, isDeteled, false, arrayList1);

					break;
				case 3:
					swtFmDo(arg0, isDeteled, false, arrayList2);

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

	/**
	 * 数据列表
	 */
	ArrayList<MessageBean> arrayList = new ArrayList<MessageBean>();
	ArrayList<MessageBean> arrayList0 = new ArrayList<MessageBean>();
	ArrayList<MessageBean> arrayList1 = new ArrayList<MessageBean>();
	ArrayList<MessageBean> arrayList2 = new ArrayList<MessageBean>();

	/**
	 * 得到消息列表
	 */
	public void getMessageList(final boolean isResh) {
		if (isResh) {
			page = 0;
		}
		okHttpsImp.getMessageList(true, userShopInfoBean.getBusinessId(), page,
				20, new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						page++;
						String mYresult = result.getData();
						org.json.JSONObject jb;
						try {
							jb = new org.json.JSONObject(mYresult);
							String jbRe = jb.getString("message");
							if (!TextUtils.isEmpty(jbRe)) {
								ArrayList<MessageBean> cuArrayList = (ArrayList<MessageBean>) JSONObject
										.parseArray(jbRe, MessageBean.class);
								if (isResh) {
									arrayList.clear();
									arrayList0.clear();
									arrayList1.clear();
									arrayList2.clear();
								}
								arrayList.addAll(cuArrayList);
								if (arrayList.size() > 0) {
									for (MessageBean mb : arrayList) {
										switch (mb.getMessage_type()) {
										case POSITION0:
											arrayList0.add(mb);
											break;
										case POSITION1:
											arrayList1.add(mb);

											break;
										case POSITION2:
											arrayList2.add(mb);
											break;
										}
									}
								}
								swtFmDo(POSITION0, false, false, arrayList);
								swtFmDo(POSITION1, false, false, arrayList0);
								swtFmDo(POSITION2, false, false, arrayList1);
								swtFmDo(POSITION3, false, false, arrayList2);
								swtFmHide(curPosition, isResh);
							}

						} catch (JSONException e) {
							swtFmDo(curPosition, false, false, null);
							swtFmHide(curPosition, isResh);
							e.printStackTrace();
						}

					}

					@Override
					public void onResponseFailed(String msg) {
						swtFmDo(curPosition, false, false, null);
						swtFmHide(curPosition, isResh);
					}
				});
		
	}

	boolean isDeteled = false;

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		if (isDeteled) {
			isDeteled = false;
			setPageRightText("编辑");
			setPageRightTextColor(R.color.colores_news_11);
			swtFmDo(curPosition, isDeteled, true, null);
		} else {
			isDeteled = true;
			setPageRightText("删除");
			setPageRightTextColor(R.color.colores_news_01);
			swtFmDo(curPosition, isDeteled, false, null);
		}

	}

	/**
	 * 删除消息
	 * 
	 * @param ids
	 *            消息id
	 */
	public void delteMsg(ArrayList<String> ids) {
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
//		okHttpsImp.postDelMessage(sb.toString(),
//				new MyResultCallback<String>() {
//
//					@Override
//					public void onResponseResult(Result result) {
//						getMessageList(true);
//					}
//
//					@Override
//					public void onResponseFailed(String msg) {
//
//					}
//				});
	}

	private void initView() {
		setPageTitle("我的消息");
		setPageRightText("编辑");
		setPageRightTextColor(R.color.colores_news_11);

		pager = (ViewPager) findViewById(R.id.pager_act_minemsg);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_act_minemsg);
		tabs.setTextSize((int) AbViewUtil.sp2px(this, 13));
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		tabs.setViewPager(pager);
	}

	/**
	 * fm隐藏
	 * 
	 * @param arg0
	 *            位置
	 * @param isD
	 *            隐藏显示
	 */
	private void swtFmHide(int arg0, boolean isD) {
		switch (arg0) {
		case POSITION0:
			if (allMessageFragment != null) {
				allMessageFragment.hidle(isD);
			}
			break;
		case POSITION1:
			if (sMessageFragment != null) {
				sMessageFragment.hidle(isD);
			}
			break;
		case POSITION2:
			if (orderMessageFragment != null) {
				orderMessageFragment.hidle(isD);
			}
			break;
		case POSITION3:
			if (personMadeMessageFragment != null) {
				personMadeMessageFragment.hidle(isD);
			}
		}
	}

	/**
	 * fm做一些事
	 * 
	 * @param arg0
	 */
	private void swtFmDo(int arg0, boolean isD, boolean isDel,
			ArrayList<MessageBean> cuArrayList) {
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
			if (personMadeMessageFragment != null) {
				personMadeMessageFragment.doSomething(isD, isDel, cuArrayList);
			}
			break;
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
					allMessageFragment = new AllMessageFragment();
				}
				return allMessageFragment;
			case POSITION1:
				if (sMessageFragment == null) {
					sMessageFragment = new AllMessageFragment();
				}
				return sMessageFragment;
			case POSITION2:
				if (orderMessageFragment == null) {
					orderMessageFragment = new AllMessageFragment();
				}
				return orderMessageFragment;
			case POSITION3:
				if (personMadeMessageFragment == null) {
					personMadeMessageFragment = new AllMessageFragment();
				}
				return personMadeMessageFragment;
			}
			return null;
		}
	}
}
