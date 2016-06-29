package com.lianbi.mezone.b.ui;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter.OnItemMonthClickListener;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScrollerUtills;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.DateAndColor;
import com.lianbi.mezone.b.bean.EverydayIncomeBean;
import com.lianbi.mezone.b.bean.SalesX_bean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

public class ShouRuActivity extends BaseActivity {
	ImageView iv_server_other;
	LinearLayout llc_server_other, lla_server_other;
	TextView liyi_money_yue, liyi_money_nian, liyi_money, year,
			head_desic_tv_shou;
	// LinearLayout llt_web_new;
	ListView listshouru;
	// WebView webView_hgh_new;
	ScrollView scrollview_desition;
	RecyclerView horizontal_listview_shouru;
	boolean act_newservicedetail_iv_server_otherBoolean = false;
	ArrayList<SalesX_bean> consultantsm = new ArrayList<SalesX_bean>();
	ArrayList<DateAndColor> arrayList = new ArrayList<DateAndColor>();

	String currentPageNum, pageSize;
	/**
	 * 是否在线收入
	 */
	// private boolean isS = false;
	// private int height;
	Calendar time = Calendar.getInstance(Locale.CHINA);
	int cyear = time.get(Calendar.YEAR);
	int month = time.get(Calendar.MONTH);
	int day = time.get(Calendar.DAY_OF_MONTH);
	int maxDay = time.getActualMaximum(Calendar.DATE);

	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat dateformatC = new SimpleDateFormat("yyyyMM");
	Date now = new Date();
	String date = dateformat.format(now);
	String dateC = dateformatC.format(now);
	protected double totalamountToday;
	double totalamountNow = 0;

	private ArrayList<EverydayIncomeBean> mDatas = new ArrayList<EverydayIncomeBean>();
	private QuickAdapter<EverydayIncomeBean> mAdapter;
	// private AbPullToRefreshView act_dingdaninfo_abpulltorefreshview;
	private int page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shouruactivity, NOTYPE);
		initView();
		getTodayIncomeDetail();
		getDayincome(true, date);
	}

	private void getTodayIncomeDetail() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();

		try {
			okHttpsImp.queryAccountTodayIncome(OkHttpsImp.md5_key, uuid, "app",
					reqTime, userShopInfoBean.getUserId(),
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reString = result.getData();
							if (!TextUtils.isEmpty(reString)) {
								totalamountToday = BigDecimal
										.valueOf(Long.valueOf(result.getData()))
										.divide(new BigDecimal(100))
										.doubleValue();
								if (totalamountToday == 0) {
									liyi_money.setText("0");
								} else {
									liyi_money.setText(MathExtend.roundNew(
											totalamountToday, 2));
								}
							} else {
								totalamountToday = 0;
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							totalamountToday = 0;
							String price = MathExtend.roundNew(
									totalamountToday, 2);
							liyi_money.setText("0");
						}
					});
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("今日收入明细");

		iv_server_other = (ImageView) findViewById(R.id.iv_server_other);
		scrollview_desition = (ScrollView) findViewById(R.id.scrollview_desition);
		scrollview_desition.smoothScrollTo(0, 20);
		llc_server_other = (LinearLayout) findViewById(R.id.llc_server_other);
		lla_server_other = (LinearLayout) findViewById(R.id.lla_server_other);
		liyi_money_yue = (TextView) findViewById(R.id.liyi_money_yue);
		liyi_money_nian = (TextView) findViewById(R.id.liyi_money_nian);
		liyi_money = (TextView) findViewById(R.id.liyi_money);
		year = (TextView) findViewById(R.id.year);
		head_desic_tv_shou = (TextView) findViewById(R.id.head_desic_tv_shou);
		listshouru = (ListView) findViewById(R.id.listshouru);
		listshouru.setFocusable(false);
		horizontal_listview_shouru = (RecyclerView) findViewById(R.id.recyle_listview_shouru);
		iv_server_other.setOnClickListener(this);
		recl();
		ScrollerUtills.scrollerup(scrollview_desition);
		// webView_hgh_new = (WebView) findViewById(R.id.webView_hgh_new);
		// llt_web_new = (LinearLayout) findViewById(R.id.llt_web_new);
		// String title = title();
		// head_desic_tv_shou.setText(title);
		// initAdapter();
		// height = web();
		// if (title.equals("今日在线收入")) {
		// isS = true;
		// getIncomeTodaywxincome();
		// } else {
		// isS = false;
		// getIncomeTodaycodincome();
		// }
	}

	// /**
	// * 今日总收入
	// *
	// * @param type
	// * string 收入方式 0：今天总收入 1： 今天在线收入 2：到店收入
	// */
	// private void getIncomeTodayincome(String type) {
	// okHttpsImp.getCountByDay(userShopInfoBean.getBusinessId(), date, type,
	// new MyResultCallback<String>() {
	//
	// @Override
	// public void onResponseResult(Result result) {
	// try {
	// JSONObject jsonObject = new JSONObject(result
	// .getData());
	// if (jsonObject.has("count")) {
	// totalamountToday = jsonObject
	// .getDouble("count");
	// String price = MathExtend.roundNew(
	// totalamountToday, 2);
	// liyi_money.setText(price);
	// } else {
	// totalamountToday = 0.00;
	// liyi_money.setText("0");
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// if (isS) {
	// getDataLine();
	// } else {
	// getDataDLine();
	// }
	// }
	//
	// @Override
	// public void onResponseFailed(String msg) {
	// totalamountToday = 0.00;
	// liyi_money.setText("0");
	// if (isS) {
	// getDataLine();
	// } else {
	//
	// getDataDLine();
	// }
	// }
	// });
	// }
	//
	// /**
	// * 今日在线收入
	// */
	// private void getIncomeTodaywxincome() {
	// getIncomeTodayincome("1");
	// }
	//
	// /**
	// * 今日到店收入
	// */
	// private void getIncomeTodaycodincome() {
	// getIncomeTodayincome("2");
	// }

	/**
	 * 初始化日期
	 */
	private void recl() {
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
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		horizontal_listview_shouru.setLayoutManager(layoutManager);
		final DateRecylerviewAdapter dateAdapter = new DateRecylerviewAdapter(
				arrayList, this);
		dateAdapter.setNumDay(day - 1);
		dateAdapter.setClickPosition(day - 1);
		layoutManager.scrollToPosition(day - 1);
		horizontal_listview_shouru.setAdapter(dateAdapter);
		dateAdapter.setOnItemClickListener(new OnItemMonthClickListener() {

			@Override
			public void onItemMonthClickListener(View view, int position) {
				if (position > day) {
				} else {
					// page = 1;
					if (position > day - 1) {
					} else {
						dateAdapter.setClickPosition(position);
						dateAdapter.notifyDataSetChanged();
						String day = arrayList.get(position).getDay();
						if (Integer.parseInt(day) == ShouRuActivity.this.day) {
							year.setText("今天");
							date = dateformat.format(now);
							// refreshData();
							getDayincome(true, date);
							getTodayIncomeDetail();
						} else {
							year.setText((month + 1) + "月" + day + "日");
							date = dateC + day;
							// refreshData();
							getDayincome(true, date);
						}
					}
				}
			}

		});
		initAdapter();
	}

	private void initAdapter() {
		mAdapter = new QuickAdapter<EverydayIncomeBean>(this,
				R.layout.item_order_info, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					EverydayIncomeBean item) {
				TextView tv_item_orderinfo_num = helper
						.getView(R.id.tv_item_orderinfo_num);// 单号
				TextView tv_item_orderinfo_state = helper
						.getView(R.id.tv_item_orderinfo_state);// 状态
				TextView tv_item_orderinfo_price = helper
						.getView(R.id.tv_item_orderinfo_price);// 价格
				TextView tv_item_orderinfo_paytime = helper
						.getView(R.id.tv_item_orderinfo_paytime);// 支付时间
				tv_item_orderinfo_num.setText(item.getOuterOrderId());
					tv_item_orderinfo_state.setText("支付成功");
				String amt = BigDecimal.valueOf(Long.valueOf(item.getAmount()))
						.divide(new BigDecimal(100)).toString();
				tv_item_orderinfo_price.setText(amt + " 元");
//				String time = item.getPayTime();
//				String year = time.substring(0, 4);
//				String months = time.substring(4, 6);
//				String daytime = time.substring(6, 8);
//				String hour = time.substring(8, 10);
//				String minute = time.substring(10, 12);
//				String second = time.substring(12, 14);
//				tv_item_orderinfo_paytime.setText(year + "-" + months + "-"
//						+ daytime + " " + hour + ":" + minute + ":" + second);
				tv_item_orderinfo_paytime.setText(item.getCreateTime());
			}
		};
		// 设置适配器
		listshouru.setAdapter(mAdapter);
	}

	/**
	 * 获取用户点击什么时间日期的显示订单
	 * 
	 * @param date
	 */
	private void getDayincome(final boolean isResh, String settleDate) {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		if (isResh) {
			page = 0;
			mDatas.clear();
			mAdapter.replaceAll(mDatas);
		}
		try {
			okHttpsImp.getDataOrderInfoById(uuid, "app", reqTime,
					OkHttpsImp.md5_key, userShopInfoBean.getUserId(), date,
					page + "", 2000 + "", new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reString = result.getData();
							if (!TextUtils.isEmpty(reString)) {
								ArrayList<EverydayIncomeBean> mDatasL = (ArrayList<EverydayIncomeBean>) JSON
										.parseArray(reString,
												EverydayIncomeBean.class);
								if (mDatasL != null && mDatasL.size() > 0) {
									mDatas.addAll(mDatasL);
								}

							}
							// AbPullHide.hideRefreshView(isResh,
							// act_dingdaninfo_abpulltorefreshview);
							mAdapter.replaceAll(mDatas);
						}

						@Override
						public void onResponseFailed(String msg) {
							if (isResh) {
								// act_dingdaninfo_abpulltorefreshview
								// .setVisibility(View.GONE);
							}
							// AbPullHide.hideRefreshView(isResh,
							// act_dingdaninfo_abpulltorefreshview);
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /**
	// * 加载图表
	// */
	// private void loadUrl() {
	// WebViewInit.WebSettingInit(webView_hgh_new, this);
	// String myUrl = line(height);
	// webView_hgh_new.loadUrl(API.HOST + API.URLJS + myUrl);
	// }
	//
	// private String title() {
	// String title = getIntent().getStringExtra("title");
	// setPageTitle(title);
	// return title;
	// }
	//
	// /**
	// * 设置web宽度
	// *
	// * @return
	// */
	// private int web() {
	// WebViewInit.WebSettingInit(webView_hgh_new, this);
	// int height = setH();
	// return height;
	// }
	//
	// private int setH() {
	// int width = ScreenUtils.getScreenWidth(this);
	// int height = width / 3;
	// if (height < 360) {
	// height = (int) (360 - AbViewUtil.dip2px(this, 10));
	// } else {
	// height = (int) (height - AbViewUtil.dip2px(this, 10));
	//
	// }
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.MATCH_PARENT,
	// (int) (width + AbViewUtil.dip2px(this, 20)));
	// llt_web_new.setLayoutParams(params);
	// return height;
	// }
	//
	// private String line(int height) {
	// /**
	// * var line_data = { type :"line",//line 折线图 pie 饼图 bar 柱状图 title :
	// * "XXX",//title文字 titleColor :"#ccc",//title 颜色 width :"",//宽度 height :
	// * "",//高度 xData :['周一','周二','周三','周四','周五','周六','周日'],//X轴 xName :"时间",
	// * yName :"金额",//Y轴轴名 color : "",//折线颜色, bgColor :"",//背景颜色 //柱状图&折线图
	// * yData: [ { name : "点击的title", data : [10, 12, 21, 54, 260, 830, 710]
	// * } ] }
	// */
	// JSONObject jso = new JSONObject();
	// if (incomeTodayphaseincome != null) {
	// try {
	// jso.put("type", "line");
	// jso.put("width", height);
	// jso.put("height", height);
	// int listTimeSize = incomeTodayphaseincome.size();
	//
	// JSONArray xData = new JSONArray();
	// for (int i = 0; i < listTimeSize; i++) {
	// xData.put(incomeTodayphaseincome.get(i).getDatetime());
	// }
	// jso.put("xData", xData);
	// jso.put("xName", "时间");
	// jso.put("yName", "金额");
	// jso.put("color", "#ff7e00");
	// jso.put("bgColor", "#ff7e00");
	// JSONArray yData = new JSONArray();
	// JSONObject jsoYData = new JSONObject();
	// JSONArray ydata = new JSONArray();
	// for (int i = 0; i < listTimeSize; i++) {
	// ydata.put(incomeTodayphaseincome.get(i).getPrice());
	// }
	// jsoYData.put("name", "收入");
	// jsoYData.put("data", ydata);
	// yData.put(jsoYData);
	// jso.put("yData", yData);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// } else {
	//
	// try {
	// jso.put("type", "line");
	// jso.put("width", height);
	// jso.put("height", height);
	//
	// JSONArray xData = new JSONArray();
	// xData.put("09:00:00-11:00:00");
	// xData.put("11:00:00-13:00:00");
	// xData.put("13:00:00-15:00:00");
	// xData.put("15:00:00-17:00:00");
	// xData.put("17:00:00-19:00:00");
	// xData.put("19:00:00-21:00:00");
	// jso.put("xData", xData);
	// jso.put("xName", "时间");
	// jso.put("yName", "金额");
	// jso.put("color", "#ff7e00");
	// jso.put("bgColor", "#ff7e00");
	// JSONArray yData = new JSONArray();
	// JSONObject jsoYData = new JSONObject();
	// JSONArray ydata = new JSONArray();
	// for (int i = 0; i < 6; i++) {
	// ydata.put(0);
	// }
	// jsoYData.put("name", "收入");
	// jsoYData.put("data", ydata);
	// yData.put(jsoYData);
	// jso.put("yData", yData);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// String myUrl = jso.toString();
	// try {
	// myUrl = URLEncoder.encode(myUrl, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// }
	// return myUrl;
	// }

	// private void setlisten() {
	// listshouru.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	// long arg3) {
	// // String value = consultantsm.get(arg2).getId() + "";
	// // startActivity(new Intent(ShouRuActivity.this,
	// // OrderDetailActivity.class).putExtra("order_id", value));
	// }
	// });
	//
	// }

	// /**
	// * 在线line图表
	// */
	// private void getDataLine() {
	// okHttpsImp.getOnlineCountByTwoHours(userShopInfoBean.getBusinessId(),
	// date, new MyResultCallback<String>() {
	//
	// @Override
	// public void onResponseResult(Result result) {
	// try {
	// JSONObject jbArray = new JSONObject(result
	// .getData());
	// String listStr = jbArray.getString("detailList");
	// ArrayList<IncomeTodayphaseincome> curincomeTodayphaseincome =
	// (ArrayList<IncomeTodayphaseincome>) com.alibaba.fastjson.JSONObject
	// .parseArray(listStr,
	// IncomeTodayphaseincome.class);
	// incomeTodayphaseincome.clear();
	// incomeTodayphaseincome
	// .addAll(curincomeTodayphaseincome);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// loadUrl();
	// getDataL();
	//
	// }
	//
	// @Override
	// public void onResponseFailed(String msg) {
	// getDataL();
	// loadUrl();
	// }
	// });
	// }

	// /**
	// * 到店line图表
	// */
	// private void getDataDLine() {
	// okHttpsImp.getOfflineCountByTwoHours(userShopInfoBean.getBusinessId(),
	// date, new MyResultCallback<String>() {
	//
	// @Override
	// public void onResponseResult(Result result) {
	// try {
	// JSONObject jbArray = new JSONObject(result
	// .getData());
	// String listStr = jbArray.getString("detailList");
	// ArrayList<IncomeTodayphaseincome> curincomeTodayphaseincome =
	// (ArrayList<IncomeTodayphaseincome>) com.alibaba.fastjson.JSONObject
	// .parseArray(listStr,
	// IncomeTodayphaseincome.class);
	// incomeTodayphaseincome.clear();
	// incomeTodayphaseincome
	// .addAll(curincomeTodayphaseincome);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// loadUrl();
	// getDataLX();
	// }
	//
	// @Override
	// public void onResponseFailed(String msg) {
	// getDataLX();
	// loadUrl();
	// }
	// });
	// }

	// ArrayList<IncomeTodayphaseincome> incomeTodayphaseincome = new
	// ArrayList<IncomeTodayphaseincome>();
	// int page = 1;
	//
	// /**
	// * 在线明细
	// */
	// private void getDataL() {
	// okHttpsImp.getDetailOnLine(userShopInfoBean.getBusinessId(), date,
	// new MyResultCallback<String>() {
	//
	// @Override
	// public void onResponseResult(Result result) {
	// String res = result.getData();
	// try {
	// JSONObject jsonObject = new JSONObject(res);
	// res = jsonObject.getString("detailList");
	// consultantsm.clear();
	// List<SalesX_bean> temp = JSON.parseArray(res,
	// SalesX_bean.class);
	// consultantsm.addAll(temp);
	// mAdapter.replaceAll(consultantsm);
	// AbViewUtil.setListViewHeight(listshouru);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// @Override
	// public void onResponseFailed(String msg) {
	//
	// }
	// });
	//
	// }
	//
	// /**
	// * 到店明细
	// */
	// private void getDataLX() {
	// okHttpsImp.getDetailOffLine(userShopInfoBean.getBusinessId(), date,
	// new MyResultCallback<String>() {
	//
	// @Override
	// public void onResponseResult(Result result) {
	// String res = result.getData();
	// try {
	// JSONObject jsonObject = new JSONObject(res);
	// res = jsonObject.getString("detailList");
	// consultantsm.clear();
	// List<SalesX_bean> temp = JSON.parseArray(res,
	// SalesX_bean.class);
	// consultantsm.addAll(temp);
	// mAdapter.replaceAll(consultantsm);
	// AbViewUtil.setListViewHeight(listshouru);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Override
	// public void onResponseFailed(String msg) {
	//
	// }
	// });
	// }
	//
	// QuickAdapter<SalesX_bean> mAdapter;
	//
	// private void initAdapter() {
	// mAdapter = new QuickAdapter<SalesX_bean>(this,
	// R.layout.adapter_shouruactivity_new, consultantsm) {
	//
	// @Override
	// protected void convert(BaseAdapterHelper helper, SalesX_bean item) {
	// TextView tvSn = helper
	// .getView(R.id.adapter_shouruactivity_new_sn);
	// TextView tvNum = helper
	// .getView(R.id.adapter_shouruactivity_new_num);
	// TextView tvTime = helper
	// .getView(R.id.adapter_shouruactivity_new_time);
	// tvSn.setText(item.getOrder_id());
	// tvTime.setText(item.getDatetime());
	//
	// String total = MathExtend.roundNew(item.getPrice(), 2);
	// String one = "";
	// String two = "";
	// if (total.contains(".")) {
	// one = total.substring(0, total.indexOf("."));
	// two = total.substring(total.indexOf("."));
	// } else {
	// one = total;
	// two = "";
	// }
	// SpannableuUtills.setSpannableu(tvNum, one, two);
	// }
	// };
	// // 设置适配器
	// listshouru.setAdapter(mAdapter);
	// }
	//
	// @Override
	// protected void onChildClick(View v) {
	// switch (v.getId()) {
	// case R.id.iv_server_other:
	// if (act_newservicedetail_iv_server_otherBoolean) {
	// shang_other();
	// } else {
	// xia_other();
	// }
	// break;
	// }
	// }
	//
	// /**
	// * 想上动画
	// */
	//
	// private void shang_other() {
	// ObjectAnimator.ofFloat(iv_server_other, "rotation", 180.0F, 0.0F)
	// .setDuration(200).start();
	// act_newservicedetail_iv_server_otherBoolean = false;
	// llc_server_other.setVisibility(View.GONE);
	// }
	//
	// /**
	// * 想下动画
	// */
	// private void xia_other() {
	// ObjectAnimator.ofFloat(iv_server_other, "rotation", 0.0F, 180.0F)
	// .setDuration(200).start();
	// act_newservicedetail_iv_server_otherBoolean = true;
	// llc_server_other.setVisibility(View.VISIBLE);
	// }
	//
	// /**
	// * 刷新数据
	// */
	// private void refreshData() {
	// if (isS) {
	// getDataLine();
	// } else {
	// getDataDLine();
	// }
	// }

}
