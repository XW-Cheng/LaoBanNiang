package com.lianbi.mezone.b.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter.OnItemMonthClickListener;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.timeselector.TimeSelector;
import cn.com.hgh.timeselector.TimeSelector.MODE;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbPullHide;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.AbPullToRefreshView.OnFooterLoadListener;
import cn.com.hgh.view.AbPullToRefreshView.OnHeaderRefreshListener;

import com.alibaba.fastjson.JSONObject;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.DateAndColor;
import com.lianbi.mezone.b.bean.OrderBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

@SuppressLint("ResourceAsColor")
public class TransactionManagementActivity extends BaseActivity {
	ImageView iv_left_act_transactionmanagementactivity,
			iv_right_act_transactionmanagementactivity;
	TextView tv_time_act_transactionmanagementactivity;
	RecyclerView recyle_act_transactionmanagementactivity;
	/**
	 * 日历
	 */
	Calendar time = Calendar.getInstance(Locale.CHINA);
	/**
	 * 今年
	 */
	int cyear = time.get(Calendar.YEAR);
	/**
	 * 今月
	 */
	int month = time.get(Calendar.MONTH);
	/**
	 * 今天
	 */
	int day = time.get(Calendar.DAY_OF_MONTH);
	/**
	 * 这个月有几天
	 */
	int maxDay = time.getActualMaximum(Calendar.DATE);
	/**
	 * 用于计算今天是几号
	 */
	int mday = -1;
	int mYear = -1;
	int mMonth = -1;
	Calendar mTime = Calendar.getInstance(Locale.CHINA);
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	Date now = new Date();
	String date = dateformat.format(now);
	/**
	 * 时间列表
	 */
	ArrayList<DateAndColor> arrayList = new ArrayList<DateAndColor>();
	CheckedTextView tv_all_act_transactionmanagementactivity,
			tv_daido_act_transactionmanagementactivity,
			tv_accepte_act_transactionmanagementactivity,
			tv_over_act_transactionmanagementactivity,
			tv_retulls_act_transactionmanagementactivity,
			tv_cancle_act_transactionmanagementactivity;

	AbPullToRefreshView abpulltorefreshview_act_transactionmanagementactivity;
	ListView listView_act_transactionmanagementactivity;
	ImageView iv_empty_act_transactionmanagementactivity;
	LinearLayout llt_pop_act_transactionmanagementactivity;

	TextView tv_tma_open_time, tv_tma_over_time;
	LinearLayout llt_tma_owner_time, llt_tma_open_time, llt_tma_over_time;
	/**
	 * 今天年月日
	 */
	private String today;

	/**
	 * 今年今月
	 */
	private String tM;
	private String startTime;
	private String endTime = "";
	private String status = " ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_transactionmanagementactivity, NOTYPE);
		today = AbDateUtil.getDateG(0);
		startTime = today;
		endTime = today;
		tM = cyear + "" + month;
		mday = day;
		mMonth = month;
		mYear = cyear;
		initCalender();
		initView();
		initListAdapter();
		listen();
		getData(true);
	}

	private void getData(final boolean b) {
		if (b) {
			page = 0;
		}
		okHttpsImp.getOrderListByBusinessIdDateStatu(
				userShopInfoBean.getBusinessId(), startTime + " 00:00:00",
				endTime + " 23:59:59", status, page + "",
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						page++;
						try {
							String res = result.getData();
							org.json.JSONObject jb = new org.json.JSONObject(
									res);
							String Str = jb.getString("orderList");
							ArrayList<OrderBean> mDatasdd = (ArrayList<OrderBean>) JSONObject
									.parseArray(Str, OrderBean.class);
							if (b) {
								mDatas.clear();
							}
							mDatas.addAll(mDatasdd);
							mAdapter.replaceAll(mDatas);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mDatas.size() > 0) {
							iv_empty_act_transactionmanagementactivity
									.setVisibility(View.GONE);
							listView_act_transactionmanagementactivity
									.setVisibility(View.VISIBLE);
						} else {
							iv_empty_act_transactionmanagementactivity
									.setVisibility(View.VISIBLE);
							listView_act_transactionmanagementactivity
									.setVisibility(View.GONE);

						}
						AbPullHide
								.hideRefreshView(b,
										abpulltorefreshview_act_transactionmanagementactivity);
					}

					@Override
					public void onResponseFailed(String msg) {
						iv_empty_act_transactionmanagementactivity
								.setVisibility(View.VISIBLE);
						listView_act_transactionmanagementactivity
								.setVisibility(View.GONE);
						AbPullHide
								.hideRefreshView(b,
										abpulltorefreshview_act_transactionmanagementactivity);

					}
				});
	}

	ArrayList<OrderBean> mDatas = new ArrayList<OrderBean>();

	/**
	 * 初始化list Adapter
	 */
	QuickAdapter<OrderBean> mAdapter;

	private void initListAdapter() {
		mAdapter = new QuickAdapter<OrderBean>(this,
				R.layout.transactionmanagementactivityitem, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper, OrderBean item) {
				helper.setText(R.id.tv_title_transactionmanagementactivityitem,
						item.getOrder_id());
				helper.setText(R.id.tv_time_transactionmanagementactivityitem,
						item.getCreateTime());
				TextView tv = helper
						.getView(R.id.tv_price_transactionmanagementactivityitem);
				String ip = item.getPrice();
				if (TextUtils.isEmpty(ip)) {
					ip = item.getAmount();
				}
				if (ip.contains("-")) {
					ip = ip.substring(0, ip.indexOf("-"));
				}
				String price = MathExtend.round(ip, 2) + "";
				String two = price.substring(0, price.indexOf("."));
				String three = price.substring(price.indexOf("."),
						price.length());
				SpannableuUtills.setSpannableu(tv, two, three);
			}
		};
		// 设置适配器
		listView_act_transactionmanagementactivity.setAdapter(mAdapter);
	}

	/**
	 * 初始化日历
	 */
	private void initCalender() {
		arrayList.clear();
		time.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		time.set(Calendar.MONTH, mMonth); // 设置月
		time.set(Calendar.YEAR, mYear); // 设置年
		maxDay = time.getActualMaximum(Calendar.DATE);
		if (mday > maxDay) {
			mday = maxDay;
		}
		for (int i = 1; i <= maxDay; i++) {
			DateAndColor dateAndColor = new DateAndColor();
			if (i < 10) {
				dateAndColor.setDay("0" + i);
			} else {
				dateAndColor.setDay("" + i);
			}
			dateAndColor.setTextcolor(Color.parseColor("#ff3c25"));
			dateAndColor.setColorId(Color.parseColor("#fbfbfb"));
			arrayList.add(dateAndColor);
		}
		if (dateAdapter != null) {
			dateAdapter.notifyDataSetChanged();
		}
	}

	DateRecylerviewAdapter dateAdapter;
	protected int page = 0;

	/**
	 * 初始化日期
	 */
	private void recl() {

		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyle_act_transactionmanagementactivity
				.setLayoutManager(layoutManager);
		dateAdapter = new DateRecylerviewAdapter(arrayList, this);
		dateAdapter.setNumDay(day - 1);
		dateAdapter.setClickPosition(day - 1);
		layoutManager.scrollToPosition(day - 1);
		recyle_act_transactionmanagementactivity.setAdapter(dateAdapter);
		dateAdapter.setOnItemClickListener(new OnItemMonthClickListener() {

			@Override
			public void onItemMonthClickListener(View view, int position) {
				if (position > dateAdapter.getNumDay()) {
				} else {
					if (position > dateAdapter.getNumDay()) {
					} else {
						dateAdapter.setClickPosition(position);
						dateAdapter.notifyDataSetChanged();
						String day = arrayList.get(position).getDay();
						mday = Integer.parseInt(day);
						setDataC();
						date = dateformat.format(now);
						endTime = startTime = mYear + "-" + (mMonth + 1) + "-"
								+ mday;
						getData(true);
					}
				}
			}

		});
	}

	private void listen() {

		abpulltorefreshview_act_transactionmanagementactivity
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						getData(true);
					}
				});
		abpulltorefreshview_act_transactionmanagementactivity
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						getData(false);

					}
				});

		listView_act_transactionmanagementactivity
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						TransactionManagementActivity.this.startActivity(new Intent(
								TransactionManagementActivity.this,
								OrderDetailActivity.class).putExtra("bean",
								mDatas.get(arg2).getOrder_id()));
					}
				});
		llt_tma_over_time.setOnClickListener(this);
		llt_tma_open_time.setOnClickListener(this);
		iv_left_act_transactionmanagementactivity.setOnClickListener(this);

		iv_right_act_transactionmanagementactivity.setOnClickListener(this);
		tv_time_act_transactionmanagementactivity.setOnClickListener(this);
		final int s = aCheckedTextViews.size();
		for (int i = 0; i < s; i++) {
			final CheckedTextView ct = aCheckedTextViews.get(i);
			ct.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					for (int j = 0; j < s; j++) {
						aCheckedTextViews.get(j).setChecked(false);
					}
					ct.setChecked(true);
					setBgCt();
					// 条件帅选刷新列表
					String ll = ct.getText().toString().trim();
					if (ll.equals("全部")) {
						status = " ";
					} else if (ll.equals("待处理")) {
						status = "1";

					} else if (ll.equals("已接受")) {
						status = "2";

					} else if (ll.equals("已完成")) {

						status = "3";
					} else if (ll.equals("已拒绝")) {

						status = "4";
					}
					getData(true);
				}
			});
		}

	}

	/**
	 * 试图缓存
	 */
	ArrayList<CheckedTextView> aCheckedTextViews = new ArrayList<CheckedTextView>();

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("交易管理");
		initPopView();
		initFilterPopupWindow();
		tv_tma_open_time = (TextView) findViewById(R.id.tv_tma_open_time);
		tv_tma_open_time.setText(AbDateUtil.getDateG(0));
		tv_tma_over_time = (TextView) findViewById(R.id.tv_tma_over_time);
		tv_tma_over_time.setText(AbDateUtil.getDateG(0));
		llt_tma_owner_time = (LinearLayout) findViewById(R.id.llt_tma_owner_time);
		llt_tma_open_time = (LinearLayout) findViewById(R.id.llt_tma_open_time);
		llt_tma_over_time = (LinearLayout) findViewById(R.id.llt_tma_over_time);
		llt_pop_act_transactionmanagementactivity = (LinearLayout) findViewById(R.id.llt_pop_act_transactionmanagementactivity);
		iv_left_act_transactionmanagementactivity = (ImageView) findViewById(R.id.iv_left_act_transactionmanagementactivity);
		iv_right_act_transactionmanagementactivity = (ImageView) findViewById(R.id.iv_right_act_transactionmanagementactivity);
		tv_time_act_transactionmanagementactivity = (TextView) findViewById(R.id.tv_time_act_transactionmanagementactivity);
		recyle_act_transactionmanagementactivity = (RecyclerView) findViewById(R.id.recyle_act_transactionmanagementactivity);
		abpulltorefreshview_act_transactionmanagementactivity = (AbPullToRefreshView) findViewById(R.id.abpulltorefreshview_act_transactionmanagementactivity);
		iv_empty_act_transactionmanagementactivity = (ImageView) findViewById(R.id.iv_empty_act_transactionmanagementactivity);
		listView_act_transactionmanagementactivity = (ListView) findViewById(R.id.listView_act_transactionmanagementactivity);
		checkedTV();
		tv_time_act_transactionmanagementactivity.setText(today);
		recl();
	}

	/**
	 * 初始化pop view
	 */
	private void initPopView() {
		filterWindow = View.inflate(this,
				R.layout.window_filter_transactionmanagement, null);
		final Button pop_tma_today;
		final Button pop_tma_three;
		final Button pop_tma_seven;
		final Button pop_tma_month;
		final Button pop_tma_owner_time;
		pop_tma_today = (Button) filterWindow.findViewById(R.id.pop_tma_today);
		pop_tma_three = (Button) filterWindow.findViewById(R.id.pop_tma_three);
		pop_tma_seven = (Button) filterWindow.findViewById(R.id.pop_tma_seven);
		pop_tma_month = (Button) filterWindow.findViewById(R.id.pop_tma_month);
		pop_tma_owner_time = (Button) filterWindow
				.findViewById(R.id.pop_tma_owner_time);

		pop_tma_today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
				startTime = endTime = today;
				tv_time_act_transactionmanagementactivity.setText(today);
				llt_tma_owner_time.setVisibility(View.GONE);
				disDU();
				pop_tma_today.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_three.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time
						.setTextColor(TransactionManagementActivity.this
								.getResources().getColor(R.color.color_c6c6c6));
				getData(true);
			}
		});
		pop_tma_three.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
				disD();
				llt_tma_owner_time.setVisibility(View.GONE);
				tv_time_act_transactionmanagementactivity.setText(AbDateUtil
						.getDateG(2) + "至" + today);
				endTime = today;
				startTime = AbDateUtil.getDateG(2);
				pop_tma_today.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_seven.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time
						.setTextColor(TransactionManagementActivity.this
								.getResources().getColor(R.color.color_c6c6c6));
				getData(true);
			}
		});
		pop_tma_seven.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
				disD();
				llt_tma_owner_time.setVisibility(View.GONE);
				tv_time_act_transactionmanagementactivity.setText(AbDateUtil
						.getDateG(6) + "至" + today);
				endTime = today;
				startTime = AbDateUtil.getDateG(6);
				pop_tma_today.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_month.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time
						.setTextColor(TransactionManagementActivity.this
								.getResources().getColor(R.color.color_c6c6c6));
				getData(true);
			}
		});
		pop_tma_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
				disD();
				llt_tma_owner_time.setVisibility(View.GONE);
				tv_time_act_transactionmanagementactivity.setText(AbDateUtil
						.getDateG(29) + "至" + today);
				endTime = today;
				startTime = AbDateUtil.getDateG(29);
				pop_tma_today.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_owner_time
						.setTextColor(TransactionManagementActivity.this
								.getResources().getColor(R.color.color_c6c6c6));
				getData(true);
			}
		});
		pop_tma_owner_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
				disD();
				startTime = today;
				llt_tma_owner_time.setVisibility(View.VISIBLE);
				tv_time_act_transactionmanagementactivity.setText("自定义时间");
				pop_tma_today.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(TransactionManagementActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time
						.setTextColor(TransactionManagementActivity.this
								.getResources().getColor(
										R.color.colores_news_01));
			}
		});
		filterWindow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
			}
		});

	}

	/**
	 * 隐藏左右以及下边
	 */
	protected void disD() {
		iv_left_act_transactionmanagementactivity.setVisibility(View.INVISIBLE);
		iv_right_act_transactionmanagementactivity
				.setVisibility(View.INVISIBLE);
		recyle_act_transactionmanagementactivity.setVisibility(View.GONE);
	}

	/**
	 * 隐藏左右以及下边
	 */
	protected void disDU() {
		dateAdapter.setNumDay(day - 1);
		dateAdapter.setClickPosition(day - 1);
		time = Calendar.getInstance(Locale.CHINA);
		maxDay = time.getActualMaximum(Calendar.DATE);
		if (day > maxDay) {
			day = maxDay;
		}
		arrayList.clear();
		for (int i = 1; i <= maxDay; i++) {
			DateAndColor dateAndColor = new DateAndColor();
			if (i < 10) {
				dateAndColor.setDay("0" + i);
			} else {
				dateAndColor.setDay("" + i);
			}
			dateAndColor.setTextcolor(Color.parseColor("#ff3c25"));
			dateAndColor.setColorId(Color.parseColor("#fbfbfb"));
			arrayList.add(dateAndColor);
		}
		if (dateAdapter != null) {
			dateAdapter.notifyDataSetChanged();

		}
		iv_right_act_transactionmanagementactivity
				.setVisibility(View.INVISIBLE);
		iv_left_act_transactionmanagementactivity.setVisibility(View.VISIBLE);
		recyle_act_transactionmanagementactivity.setVisibility(View.VISIBLE);
	}

	private void checkedTV() {
		tv_all_act_transactionmanagementactivity = (CheckedTextView) findViewById(R.id.tv_all_act_transactionmanagementactivity);
		tv_daido_act_transactionmanagementactivity = (CheckedTextView) findViewById(R.id.tv_daido_act_transactionmanagementactivity);
		tv_accepte_act_transactionmanagementactivity = (CheckedTextView) findViewById(R.id.tv_accepte_act_transactionmanagementactivity);
		tv_over_act_transactionmanagementactivity = (CheckedTextView) findViewById(R.id.tv_over_act_transactionmanagementactivity);
		tv_retulls_act_transactionmanagementactivity = (CheckedTextView) findViewById(R.id.tv_retulls_act_transactionmanagementactivity);
		tv_cancle_act_transactionmanagementactivity = (CheckedTextView) findViewById(R.id.tv_cancle_act_transactionmanagementactivity);
		aCheckedTextViews.add(tv_all_act_transactionmanagementactivity);
		aCheckedTextViews.add(tv_daido_act_transactionmanagementactivity);
		aCheckedTextViews.add(tv_accepte_act_transactionmanagementactivity);
		aCheckedTextViews.add(tv_over_act_transactionmanagementactivity);
		aCheckedTextViews.add(tv_retulls_act_transactionmanagementactivity);
		aCheckedTextViews.add(tv_cancle_act_transactionmanagementactivity);
		setBgCt();
	}

	/**
 * 
 */
	private void setBgCt() {
		int s = aCheckedTextViews.size();
		for (int i = 0; i < s; i++) {
			CheckedTextView ct = aCheckedTextViews.get(i);
			GradientDrawable drawable = (GradientDrawable) ct.getBackground();
			if (ct.isChecked()) {
				drawable.setColor(Color.parseColor("#ff3c25"));
				ct.setTextColor(Color.parseColor("#ffffff"));
			} else {
				drawable.setColor(Color.parseColor("#ededed"));
				ct.setTextColor(Color.parseColor("#b4b4b4"));
			}
			ct.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * 图标切换标记
	 */
	private boolean tv_common_filter_two_boolean;
	private PopupWindow popFilter;
	private View filterWindow;
	private final String TIMESTART = "2013-01-01 00:00";

	/**
	 * 按钮图标设置
	 */
	private void xia() {
		tv_common_filter_two_boolean = true;
		Drawable drawable = getResources().getDrawable(R.mipmap.tma_down);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		tv_time_act_transactionmanagementactivity.setCompoundDrawables(null,
				null, drawable, null);
	}

	/**
	 * 按钮图标设置
	 */
	private void nomall() {
		tv_common_filter_two_boolean = false;
		Drawable drawable = getResources().getDrawable(R.mipmap.tma_up);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		tv_time_act_transactionmanagementactivity.setCompoundDrawables(null,
				null, drawable, null);
	}

	/**
	 * 初始化pop
	 */
	private void initFilterPopupWindow() {
		popFilter = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		popFilter.setContentView(filterWindow);
		popFilter.setFocusable(true);
		popFilter.setOutsideTouchable(true);
		popFilter.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				nomall();
			}
		});
	}

	@Override
	protected void onChildClick(View v) {
		super.onChildClick(v);
		switch (v.getId()) {
		case R.id.tv_time_act_transactionmanagementactivity:// time
			if (tv_common_filter_two_boolean) {
				popFilter.dismiss();
			} else {
				xia();
				popFilter.showAsDropDown(
						llt_pop_act_transactionmanagementactivity, 0, 2);
			}
			break;

		case R.id.iv_left_act_transactionmanagementactivity:
			if (mMonth == 0) {
				mMonth = 11;
				mYear = mYear - 1;
			} else {
				mMonth = mMonth - 1;
			}
			iv_right_act_transactionmanagementactivity
					.setVisibility(View.VISIBLE);
			maxDay = time.getActualMaximum(Calendar.DATE);
			initCalender();
			dateAdapter.setNumDay(maxDay - 1);
			setDataC();
			startTime = mYear + "-" + (mMonth + 1) + "-" + mday;
			endTime = today;
			getData(true);
			break;
		case R.id.iv_right_act_transactionmanagementactivity:
			if (mMonth == 11) {
				mMonth = 0;
				mYear = mYear + 1;

			} else {
				mMonth = mMonth + 1;
			}
			initCalender();
			if (tM.equals(mYear + "" + mMonth)) {
				iv_right_act_transactionmanagementactivity
						.setVisibility(View.INVISIBLE);
				dateAdapter.setNumDay(day - 1);
				mday = day;
			} else {
				dateAdapter.setNumDay(maxDay - 1);
			}
			setDataC();
			startTime = mYear + "-" + (mMonth + 1) + "-" + mday;
			endTime = today;
			if (startTime.equals(endTime)) {
				// endTime = "";
			}
			getData(true);
			break;
		case R.id.llt_tma_open_time:// 开始时间
			TimeSelector timeSelector = new TimeSelector(this,
					new TimeSelector.ResultHandler() {
						@Override
						public void handle(String time) {
							tv_tma_open_time.setText(time);
							startTime = time;
							if (!AbDateUtil.compareTime(startTime, endTime)) {
								return;
							}
							getData(true);
						}
					}, TIMESTART,
					AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHM));
			timeSelector.setMode(MODE.YMD);
			timeSelector.setTitle("起始时间");
			timeSelector.show();
			break;
		case R.id.llt_tma_over_time:// 结束时间
			TimeSelector timeSelectorO = new TimeSelector(this,
					new TimeSelector.ResultHandler() {
						@Override
						public void handle(String time) {
							tv_tma_over_time.setText(time);
							endTime = time;
							if (!AbDateUtil.compareTime(startTime, endTime)) {
								ContentUtils.showMsg(
										TransactionManagementActivity.this,
										"起始时间不能大于结束时间");
								return;
							}
							// if (startTime.equals(endTime)) {
							// endTime = "";
							// }
							getData(true);
						}
					}, TIMESTART,
					AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHM));
			timeSelectorO.setMode(MODE.YMD);
			timeSelectorO.setTitle("结束时间");
			timeSelectorO.show();

			break;
		}
	}

	/**
	 * 设置时间
	 */
	private void setDataC() {
		dateAdapter.setClickPosition(mday - 1);
		if (mMonth < 9) {
			if (mday < 10) {
				tv_time_act_transactionmanagementactivity.setText(mYear + "-0"
						+ (mMonth + 1) + "-0" + mday);
			} else {
				tv_time_act_transactionmanagementactivity.setText(mYear + "-0"
						+ (mMonth + 1) + "-" + mday);
			}
		} else {
			if (mday < 10) {
				tv_time_act_transactionmanagementactivity.setText(mYear + "-"
						+ (mMonth + 1) + "-0" + mday);
			} else {
				tv_time_act_transactionmanagementactivity.setText(mYear + "-"
						+ (mMonth + 1) + "-" + mday);
			}

		}
	}

}
