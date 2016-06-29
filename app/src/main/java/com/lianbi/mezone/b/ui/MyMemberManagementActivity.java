package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;
import cn.com.hgh.view.PagerSlidingTabStrip;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.AssociatorListBean;
import com.lianbi.mezone.b.fragment.AllMemberFragment;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

@SuppressLint("ResourceAsColor")
public class MyMemberManagementActivity extends BaseActivity {
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	final String[] titles = { "全部", "1级", "2级", "3级", "4级", "5级" };
	public static final int POSITION0 = 0;
	public static final int POSITION1 = 1;
	public static final int POSITION2 = 2;
	public static final int POSITION3 = 3;
	public static final int POSITION4 = 4;
	public static final int POSITION5 = 5;
	AllMemberFragment allMemberFragment, oneMemberFragment, twoMemberFragment,
			threeMemberFragment, fourMemberFragment, fiveMemberFragment;

	LinearLayout top_seracth_llt, top_tabs_llt;
	ListView top_seracth_llt_listView;
	ImageView top_seracth_llt_iv_empty;
	public final int REQUEST_ASSOCITOR = 2045;
	private int currentPageNum = 0;
	private AbPullToRefreshView top_seracth_abpulltorefreshview;
	private String searchString = "";
	/**
	 * 当前的位置
	 */
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_minemsg, NOTYPE);
		initView();
		initListAdapter();
		listen();
	}

	/**
	 * 搜索数据列表
	 */
	ArrayList<AssociatorListBean> arrayListSerach = new ArrayList<AssociatorListBean>();

	private void listen() {
		cet_title_center
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE
								|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
							searchString = cet_title_center.getText()
									.toString().trim();
							serach(true);
						}
						AbAppUtil
								.closeSoftInput(MyMemberManagementActivity.this);
						return false;
					}
				});
		top_seracth_abpulltorefreshview
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						serach(true);

					}
				});
		top_seracth_abpulltorefreshview
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						serach(false);
					}
				});
		top_seracth_llt_listView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						startActivityForResult(new Intent(
								MyMemberManagementActivity.this,
								MemberEditActivity.class).putExtra("bean",
								arrayListSerach.get(position)),
								REQUEST_ASSOCITOR);
					}
				});
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				position = arg0;
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
	 * 是否正在搜索
	 */
	boolean isDeteled = false;

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		if (isDeteled) {
			top_seracth_llt.setVisibility(View.GONE);
			top_tabs_llt.setVisibility(View.VISIBLE);
			isDeteled = false;
			setPageRightText("搜索");
			setPageTitleVET(View.GONE);
			setPageTitleV(View.VISIBLE);

		} else {
			isDeteled = true;
			setPageRightText("取消");
			setPageTitleVET(View.VISIBLE);
			setPageTitleV(View.GONE);
		}

	}

	QuickAdapter<AssociatorListBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<AssociatorListBean>(this,
				R.layout.item_memberfmlist, arrayListSerach) {

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
		top_seracth_llt_listView.setAdapter(mAdapter);
	}

	/**
	 * 搜索接口
	 * 
	 * @param isResh
	 */
	private void serach(final boolean isResh) {
		if (isResh) {
			currentPageNum = 0;
		}
		okHttpsImp.getAssociatorByPhone(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String resString = result.getData();
				try {
					JSONObject jsonObject = new JSONObject(resString);
					resString = jsonObject.getString("associatorList");
					if (isResh) {
						currentPageNum = 0;
						arrayListSerach.clear();
					}
					ArrayList<AssociatorListBean> associatorListBeansL = (ArrayList<AssociatorListBean>) JSON
							.parseArray(resString, AssociatorListBean.class);
					if (associatorListBeansL != null
							&& associatorListBeansL.size() > 0) {
						currentPageNum++;
						arrayListSerach.addAll(associatorListBeansL);
						top_seracth_llt_iv_empty.setVisibility(View.GONE);
						top_seracth_abpulltorefreshview
								.setVisibility(View.VISIBLE);
						mAdapter.replaceAll(arrayListSerach);
					} else {
						top_seracth_llt_iv_empty.setVisibility(View.VISIBLE);
						top_seracth_abpulltorefreshview
								.setVisibility(View.GONE);
					}
					hidle(isResh);
				} catch (JSONException e) {
					top_seracth_llt_iv_empty.setVisibility(View.VISIBLE);
					top_seracth_abpulltorefreshview.setVisibility(View.GONE);
					e.printStackTrace();
				}
				top_seracth_llt.setVisibility(View.VISIBLE);
				top_tabs_llt.setVisibility(View.GONE);
				top_seracth_llt_listView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onResponseFailed(String msg) {
				top_seracth_llt.setVisibility(View.VISIBLE);
				top_tabs_llt.setVisibility(View.GONE);
			}
		}, currentPageNum + "", searchString);
	}

	private void initView() {
		setPageTitle("会员管理");
		setPageRightText("搜索");
		setPageTitleVETYPE(InputType.TYPE_CLASS_NUMBER);
		setPageTitleEtLenth(11);
		cet_title_center.setHint("搜索会员");
		setPageRightTextColor(R.color.colores_news_01);

		pager = (ViewPager) findViewById(R.id.pager_act_minemsg);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_act_minemsg);
		tabs.setTextSize((int) AbViewUtil.sp2px(this, 14));
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		tabs.setViewPager(pager);

		top_seracth_llt = (LinearLayout) findViewById(R.id.top_seracth_llt);
		top_tabs_llt = (LinearLayout) findViewById(R.id.top_tabs_llt);
		top_seracth_llt_iv_empty = (ImageView) findViewById(R.id.top_seracth_llt_iv_empty);
		top_seracth_llt_listView = (ListView) findViewById(R.id.top_seracth_llt_listView);
		top_seracth_abpulltorefreshview = (AbPullToRefreshView) findViewById(R.id.top_seracth_abpulltorefreshview);
	}

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		super.onActivityResult(requestCode, arg1, arg2);
		if (arg1 == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_ASSOCITOR:
				if (allMemberFragment != null && position == POSITION0) {
					allMemberFragment.reFreshData();
					if (oneMemberFragment != null) {
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								oneMemberFragment.reFreshData();
							}
						}, 3000);
					}
				}
				if (oneMemberFragment != null && position == POSITION1) {
					oneMemberFragment.reFreshData();
				}
				if (twoMemberFragment != null && position == POSITION2) {
					twoMemberFragment.reFreshData();
				}
				if (threeMemberFragment != null && position == POSITION3) {
					threeMemberFragment.reFreshData();
				}
				if (fourMemberFragment != null && position == POSITION4) {
					fourMemberFragment.reFreshData();
				}
				if (fiveMemberFragment != null && position == POSITION5) {
					fiveMemberFragment.reFreshData();
				}
				if (isDeteled) {
					serach(true);
				}
				break;
			}
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
				if (allMemberFragment == null) {
					allMemberFragment = new AllMemberFragment();
					allMemberFragment.setLevel("");
				} else {
					fiveMemberFragment.setLevel("");

				}
				return allMemberFragment;
			case POSITION1:
				if (oneMemberFragment == null) {
					oneMemberFragment = new AllMemberFragment();
					oneMemberFragment.setLevel("1");
				} else {
					fiveMemberFragment.setLevel("1");

				}
				return oneMemberFragment;
			case POSITION2:
				if (twoMemberFragment == null) {
					twoMemberFragment = new AllMemberFragment();
					twoMemberFragment.setLevel("2");
				} else {
					fiveMemberFragment.setLevel("2");

				}
				return twoMemberFragment;
			case POSITION3:
				if (threeMemberFragment == null) {
					threeMemberFragment = new AllMemberFragment();
					threeMemberFragment.setLevel("3");
				} else {
					fiveMemberFragment.setLevel("3");

				}
				return threeMemberFragment;
			case POSITION4:
				if (fourMemberFragment == null) {
					fourMemberFragment = new AllMemberFragment();
					fourMemberFragment.setLevel("4");
				} else {
					fiveMemberFragment.setLevel("4");

				}
				return fourMemberFragment;
			case POSITION5:
				if (fiveMemberFragment == null) {
					fiveMemberFragment = new AllMemberFragment();
					fiveMemberFragment.setLevel("5");
				} else {
					fiveMemberFragment.setLevel("5");

				}
				return fiveMemberFragment;
			}
			return null;
		}
	}

	/**
	 * 隐藏搜索刷新
	 * 
	 * @param isResh
	 */
	public void hidle(boolean isResh) {
		AbPullHide.hideRefreshView(isResh, top_seracth_abpulltorefreshview);
	}
}
