package com.lianbi.mezone.b.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScreenUtils;
import cn.com.hgh.utils.WebViewInit;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Dayincome;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 上周每日收入
 * 
 * @time 下午9:46:55
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class DailyShouRuActivity extends BaseActivity {
	LinearLayout dailyshouruactivity_llt_web_new,
			dailyshouruactivity_llt_web2_new;

	WebView dailyshouruactivity_webView_hgh_new,
			dailyshouruactivity_webView2_hgh_new;
	TextView online_shop_num, online_shop_tv_num2;
	private int height, height2;
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	Date now = new Date();
	String date = dateformat.format(now);
	ArrayList<Dayincome> dayincomes;
	/**
	 * 到店收入
	 */
	private double ddIncome;
	/**
	 * 线上收入
	 */
	private double xsIncome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailyshouruactivity, HAVETYPE);
		initView();
		sUp();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("上周每日收入");
		dailyshouruactivity_llt_web_new = (LinearLayout) findViewById(R.id.dailyshouruactivity_llt_web_new);
		dailyshouruactivity_llt_web2_new = (LinearLayout) findViewById(R.id.dailyshouruactivity_llt_web2_new);
		dailyshouruactivity_webView_hgh_new = (WebView) findViewById(R.id.dailyshouruactivity_webView_hgh_new);
		dailyshouruactivity_webView2_hgh_new = (WebView) findViewById(R.id.dailyshouruactivity_webView2_hgh_new);
		online_shop_num = (TextView) findViewById(R.id.online_shop_num);
		online_shop_tv_num2 = (TextView) findViewById(R.id.online_shop_tv_num2);
		height = web();
		height2 = web2();
		getincomeLastweekeverydayincome();
	}

	/**
	 * 上周每日收入
	 */
	public void getincomeLastweekeverydayincome() {
		okHttpsImp.getAmountByWeek(userShopInfoBean.getBusinessId(),
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
						getOnLineLastWeekCount();
					}

					@Override
					public void onResponseFailed(String msg) {
					}
				});
	}

	/**
	 * 获取上周的线上经营总收入
	 */
	private void getOnLineLastWeekCount() {
		okHttpsImp.getOnLineLastWeekCount(userShopInfoBean.getBusinessId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jsonObject = new JSONObject(result
									.getData());
							if (jsonObject.has("count")) {
								xsIncome = jsonObject.getDouble("count");
							} else {
								xsIncome = 0.00;
							}
							String price = MathExtend.roundNew(xsIncome, 2);
							online_shop_num.setText(price);
							getOfflineLastWeekCount();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						online_shop_num.setText(0 + "");
					}
				});
	}

	/**
	 * 获取上周的线下经营总收入
	 */
	private void getOfflineLastWeekCount() {
		okHttpsImp.getOfflineLastWeekCount(userShopInfoBean.getBusinessId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jsonObject = new JSONObject(result
									.getData());
							if (jsonObject.has("count")) {
								ddIncome = jsonObject.getDouble("count");
							} else {
								ddIncome = 0.00;
							}
							String xsPrice = MathExtend.roundNew(ddIncome, 2);
							online_shop_tv_num2.setText(xsPrice);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						loadUrl();
					}

					@Override
					public void onResponseFailed(String msg) {
						online_shop_tv_num2.setText(0 + "");
						loadUrl();
					}
				});
	}

	/**
	 * 加载图表
	 */
	private void loadUrl() {
		loadUrlPie();
		loadUrlLine();
	}

	private void loadUrlPie() {
		String myUrl = "";
		myUrl = pie(height);
		dailyshouruactivity_webView_hgh_new.loadUrl(API.HOST + API.URLJS
				+ myUrl);
	}

	private void loadUrlLine() {
		String myUrl = "";
		myUrl = line(height2);
		dailyshouruactivity_webView2_hgh_new.loadUrl(API.HOST + API.URLJS
				+ myUrl);
	}

	private int web() {
		WebViewInit.WebSettingInit(dailyshouruactivity_webView_hgh_new, this);
		int height = setH();
		return height;
	}

	private int web2() {
		WebViewInit.WebSettingInit(dailyshouruactivity_webView2_hgh_new, this);
		int height2 = setH2();
		return height2;
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
		dailyshouruactivity_llt_web_new.setLayoutParams(params);

		return height;
	}

	private int setH2() {
		int width = ScreenUtils.getScreenWidth(this);
		int height2 = width / 3;
		if (height2 < 360) {
			height2 = 360;
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, width);
		dailyshouruactivity_webView2_hgh_new.setLayoutParams(params);

		return height2;
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
			if (ddIncome == 0) {
				jsoYDataCOD.put("value", 0);
				jsoYDataCOD.put("name", "到店收入");
			} else {
				jsoYDataCOD.put("value", ddIncome);
				jsoYDataCOD.put("name", "到店收入");
			}
			JSONObject jsoYDataWX = new JSONObject();
			if (xsIncome == 0) {
				jsoYDataWX.put("value", 0);
				jsoYDataWX.put("name", "在线收入");
			} else {
				jsoYDataWX.put("value", xsIncome);
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

	/**
	 * 折线图
	 * 
	 * @param height2
	 * @return
	 */
	private String line(int height2) {
		/**
		 * var line_data = { type :"line",//line 折线图 pie 饼图 bar 柱状图 title :
		 * "XXX",//title文字 titleColor :"#ccc",//title 颜色 width :"",//宽度 height :
		 * "",//高度 xData :['周一','周二','周三','周四','周五','周六','周日'],//X轴 xName :"时间",
		 * yName :"金额",//Y轴轴名 color : "",//折线颜色, bgColor :"",//背景颜色 //柱状图&折线图
		 * yData: [ { name : "点击的title", data : [10, 12, 21, 54, 260, 830, 710]
		 * } ] }
		 */
		JSONObject jso = new JSONObject();
		if (dayincomes != null && dayincomes.size() > 0) {
			try {
				jso.put("type", "line");
				jso.put("width", height);
				jso.put("height", height);
				int listTimeSize = dayincomes.size();
				JSONArray xData = new JSONArray();
				JSONArray yData = new JSONArray();
				JSONObject jsoYData = new JSONObject();
				JSONArray ydata = new JSONArray();
				for (int i = 0; i < listTimeSize; i++) {
					xData.put(dayincomes.get(i).getDatetime());
					ydata.put(dayincomes.get(i).getCount());
				}
				jso.put("xData", xData);
				jso.put("xName", "时间");
				jso.put("yName", "金额");
				jso.put("color", "#ff7e00");
				jso.put("bgColor", "#ff7e00");
				jsoYData.put("name", "收入");
				jsoYData.put("data", ydata);
				yData.put(jsoYData);
				jso.put("yData", yData);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			try {
				jso.put("type", "line");
				jso.put("width", height);
				jso.put("height", height);

				JSONArray xData = new JSONArray();
				xData.put("周一");
				xData.put("周二");
				xData.put("周三");
				xData.put("周四");
				xData.put("周五");
				xData.put("周六");
				xData.put("周日");
				jso.put("xData", xData);
				jso.put("xName", "时间");
				jso.put("yName", "金额");
				jso.put("color", "#ff7e00");
				jso.put("bgColor", "#ff7e00");
				JSONArray yData = new JSONArray();
				JSONObject jsoYData = new JSONObject();
				JSONArray ydata = new JSONArray();
				for (int i = 0; i < 7; i++) {
					ydata.put(0);
				}
				jsoYData.put("name", "收入");
				jsoYData.put("data", ydata);
				yData.put(jsoYData);
				jso.put("yData", yData);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		String myUrl = jso.toString();
		try {
			myUrl = URLEncoder.encode(myUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return myUrl;
	}

}
