package com.lianbi.mezone.b.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter.OnItemMonthClickListener;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScreenUtils;
import cn.com.hgh.utils.WebViewInit;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.DateAndColor;
import com.lianbi.mezone.b.bean.Dayincome;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

public class MonthShouRuActivity extends BaseActivity {
	LinearLayout monthshouruactivity_llt_web_new;
	TextView year, head_desic_tv_shou, liyi_money, online_shop_num,
			online_shop_tv_num2;
	WebView monthshouruactivity_webView_hgh_new;
	RecyclerView horizontal_listview_shouru;
	private int height;
	Calendar time = Calendar.getInstance(Locale.CHINA);
	int cyear = time.get(Calendar.YEAR);
	int month = time.get(Calendar.MONTH);
	ArrayList<DateAndColor> arrayList = new ArrayList<DateAndColor>();

	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM");
	Date now = new Date();
	String date = dateformat.format(now);
	ArrayList<Dayincome> dayincomes = new ArrayList<Dayincome>();
	int nowMonth1;
	String nowMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthshouruactivity, HAVETYPE);
		initView();
		sUp();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("本年月度收入");
		horizontal_listview_shouru = (RecyclerView) findViewById(R.id.recyle_listview_shouru);
		monthshouruactivity_llt_web_new = (LinearLayout) findViewById(R.id.monthshouruactivity_llt_web_new);
		monthshouruactivity_webView_hgh_new = (WebView) findViewById(R.id.monthshouruactivity_webView_hgh_new);
		year = (TextView) findViewById(R.id.year);
		head_desic_tv_shou = (TextView) findViewById(R.id.head_desic_tv_shou);
		liyi_money = (TextView) findViewById(R.id.liyi_money);
		online_shop_num = (TextView) findViewById(R.id.online_shop_num);
		online_shop_tv_num2 = (TextView) findViewById(R.id.online_shop_tv_num2);
		head_desic_tv_shou.setText("本年" + (month + 1) + "月收入（元）");
		year.setText((month + 1) + "月");
		recl();
		height = web();
		nowMonth1 = month + 1;
		if (nowMonth1 < 10) {
			nowMonth = "0" + nowMonth1;
		}
		getincomeMonthtotalincome();
	}

	/**
	 * 在线总收入
	 */
	private double wxtotalamount;
	/**
	 * 到店总收入
	 */
	private double codtotalamount;

	/**
	 * 当月总收入
	 */
	private void getincomeMonthtotalincome() {
		okHttpsImp.getAmountByMonth(userShopInfoBean.getBusinessId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						String resString = result.getData();
						try {
							JSONObject jsonObject = new JSONObject(resString);
							resString = jsonObject.getString("amountList");
							dayincomes = (ArrayList<Dayincome>) JSON
									.parseArray(resString, Dayincome.class);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (dayincomes != null && dayincomes.size() > 0) {
							setTotalMonthprice();
						} else {
							String price = MathExtend.roundNew(0.00, 2);
							liyi_money.setText(price);
						}
						getOnLineCountByMonth();
					}

					@Override
					public void onResponseFailed(String msg) {
						String price = MathExtend.roundNew(0.00, 2);
						liyi_money.setText(price);
					}
				});
	}

	/**
	 * 设置某月数据
	 */
	private void setTotalMonthprice() {
		for (int i = 0; i < dayincomes.size(); i++) {
			if (dayincomes.get(i).getDatetime().equals(nowMonth)) {
				String price = MathExtend.roundNew(
						dayincomes.get(i).getPrice(), 2);
				liyi_money.setText(price);
				break;
			} else {
				String price = MathExtend.roundNew(0.00, 2);
				liyi_money.setText(price);
			}
		}
	}

	/**
	 * 获取某月线上经营总收入
	 */
	private void getOnLineCountByMonth() {
		okHttpsImp.getOnLineCountByMonth(userShopInfoBean.getBusinessId(),
				date, new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jsonObject = new JSONObject(result
									.getData());
							if (jsonObject.has("count")) {
								wxtotalamount = jsonObject.getDouble("count");
							} else {
								wxtotalamount = 0.00;
							}
							String dayPrice = MathExtend.roundNew(
									wxtotalamount, 2);
							online_shop_num.setText(dayPrice);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						getOffLineCountByMonth();
					}

					@Override
					public void onResponseFailed(String msg) {
						loadUrl();
					}
				});
	}

	/**
	 * 获取某月线下经营总收入
	 */
	private void getOffLineCountByMonth() {
		okHttpsImp.getOffLineCountByMonth(userShopInfoBean.getBusinessId(),
				date, new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jsonObject = new JSONObject(result
									.getData());
							if (jsonObject.has("count")) {
								codtotalamount = jsonObject.getDouble("count");
							} else {
								codtotalamount = 0.00;
							}
							String dayPrice = MathExtend.roundNew(
									codtotalamount, 2);
							online_shop_tv_num2.setText(dayPrice);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						loadUrl();
					}

					@Override
					public void onResponseFailed(String msg) {
						loadUrl();
					}
				});
	}

	private void recl() {
		for (int i = 1; i <= 12; i++) {
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
		dateAdapter.setNumDay(month);
		dateAdapter.setClickPosition(month);
		layoutManager.scrollToPosition(month);
		horizontal_listview_shouru.setAdapter(dateAdapter);
		dateAdapter.setOnItemClickListener(new OnItemMonthClickListener() {

			@Override
			public void onItemMonthClickListener(View view, int position) {
				if (position > month) {
				} else {
					String day = arrayList.get(position).getDay();
					year.setText(day + "月");
					head_desic_tv_shou.setText("本年" + day + "月收入（元）");
					date = cyear + "-" + day;
					dateAdapter.setClickPosition(position);
					dateAdapter.notifyDataSetChanged();
					nowMonth = day;
					if (dayincomes != null && dayincomes.size() > 0) {
						setTotalMonthprice();
					} else {
						String price = MathExtend.roundNew(0.00, 2);
						liyi_money.setText(price);
					}
					getOnLineCountByMonth();
				}
			}

		});
	}

	private void loadUrl() {
		monthshouruactivity_webView_hgh_new.clearHistory();
		String myUrl = "";
		myUrl = pie(height);
		monthshouruactivity_webView_hgh_new.loadUrl(API.HOST + API.URLJS
				+ myUrl);
	}

	/**
	 * 初始化web
	 * 
	 * @return
	 */
	private int web() {
		WebViewInit.WebSettingInit(monthshouruactivity_webView_hgh_new, this);
		int height = setH();
		return height;
	}

	/**
	 * 设置web高度
	 * 
	 * @return
	 */
	private int setH() {
		int width = ScreenUtils.getScreenWidth(this);
		int height = width / 3;
		if (height < 360) {
			height = 360;
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, width);
		monthshouruactivity_llt_web_new.setLayoutParams(params);
		return height;
	}

	/**
	 * 饼状图
	 * 
	 * @param height
	 * @return
	 */
	private String pie(int height) {
		/**
		 * type :"pie",//line 折线图 pie 饼图 bar 柱状图 title : "XXX",//title文字
		 * titleColor :"#ccc",//title 颜色 width :"",//宽度 height : "",//高度 name
		 * :"收入", xData :['周一','周二','周三','周四','周五','周六','周日'],//X轴 innerRadius
		 * :50,//内圆直径 outerRadius :80,//外圆直径 maxData : 1000,//最大值 yData: [//值 {
		 * value:335, name:'直达'}, {value:310, name:'邮件营销'}, {value:234,
		 * name:'联盟广告'}, {value:135, name:'视频广告'}, {value:1048, name:'百度'},
		 * {value:251, name:'谷歌'}, {value:147, name:'必应'}, {value:102,
		 * name:'其他'} ] }
		 */
		JSONObject jso = new JSONObject();
		try {
			jso.put("type", "pie");
			jso.put("width", height);
			jso.put("height", height);
			jso.put("name", "收入");
			jso.put("innerRadius", 50);
			jso.put("outerRadius", 80);
			jso.put("maxData", 1000);
			JSONArray yData = new JSONArray();
			JSONObject jsoYDataCOD = new JSONObject();
			if (codtotalamount == 0) {
				jsoYDataCOD.put("value", 0);
				jsoYDataCOD.put("name", "到店收入");
			} else {
				jsoYDataCOD.put("value", codtotalamount);
				jsoYDataCOD.put("name", "到店收入");
			}
			JSONObject jsoYDataWX = new JSONObject();
			if (wxtotalamount == 0) {
				jsoYDataWX.put("value", 0);
				jsoYDataWX.put("name", "在线收入");
			} else {
				jsoYDataWX.put("value", wxtotalamount);
				jsoYDataWX.put("name", "在线收入");
			}
			yData.put(jsoYDataWX);
			yData.put(jsoYDataCOD);
			jso.put("yData", yData);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		String myUrl = jso.toString();
		try {
			myUrl = URLEncoder.encode(myUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return myUrl;
	}

}
