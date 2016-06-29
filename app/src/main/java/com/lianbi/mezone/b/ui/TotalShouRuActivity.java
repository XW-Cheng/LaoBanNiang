package com.lianbi.mezone.b.ui;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScreenUtils;
import cn.com.hgh.utils.WebViewInit;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 总收入
 * 
 * @time 下午9:33:22
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class TotalShouRuActivity extends BaseActivity {

	LinearLayout totalshouruactivity_llt_web_new;
	TextView totalshouruactivity_money_today, online_shop_num,
			online_shop_tv_num2;
	WebView totalshouruactivity_webView_hgh_new;

	private int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.totalshouruactivity, HAVETYPE);
		initView();
		sUp();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("总收入");
		totalshouruactivity_llt_web_new = (LinearLayout) findViewById(R.id.totalshouruactivity_llt_web_new);
		totalshouruactivity_money_today = (TextView) findViewById(R.id.totalshouruactivity_money_today);
		online_shop_num = (TextView) findViewById(R.id.online_shop_num);
		online_shop_tv_num2 = (TextView) findViewById(R.id.online_shop_tv_num2);
		totalshouruactivity_webView_hgh_new = (WebView) findViewById(R.id.totalshouruactivity_webView_hgh_new);
		height = web();
		//getCount();
	}

	/**
	 * 总收入
	 */
	private double totalamount;
	/**
	 * 在线总收入totalamount :09, wxtotalamount codtotalamount :11
	 */
	private double wxtotalamount;
	/**
	 * 到店总收入
	 */
	private double codtotalamount;

	/**
	 * 获取总收入
	 *//*
	private void getCount() {
		totalamount = 0;
		wxtotalamount = 0;
		codtotalamount = 0;
		String dayPrice = MathExtend.roundNew(totalamount, 2);
		String dayPriceCOD = MathExtend.roundNew(codtotalamount, 2);
		String dayPriceWX = MathExtend.roundNew(wxtotalamount, 2);
		totalshouruactivity_money_today.setText("0");
		online_shop_num.setText("0");
		online_shop_tv_num2.setText("0");
		// okHttpsImp.getCountByBusiness(userShopInfoBean.getBusinessId(),true,
		// new MyResultCallback<String>() {
		//
		// @Override
		// public void onResponseResult(Result result) {
		// try {
		// JSONObject jsonObject = new JSONObject(result
		// .getData());
		// if (jsonObject.has("count")) {
		// totalamount = jsonObject.getDouble("count");
		// } else {
		// totalamount = 0;
		// }
		// String dayPrice = MathExtend.roundNew(totalamount,
		// 2);
		// totalshouruactivity_money_today.setText(dayPrice);
		// getOfflineCountByBusiness();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public void onResponseFailed(String msg) {
		// loadUrl();
		// }
		// });
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getStoreRevenue1(OkHttpsImp.md5_key,
					uuid,"app",reqTime,
					userShopInfoBean.getUserId(),
				
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reString = result.getData();
							if (!TextUtils.isEmpty(reString)) {
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(reString);
									totalamount = BigDecimal
											.valueOf(
													Long.valueOf(jsonObject
															.getInt("totalAmt")))
											.divide(new BigDecimal(100))
											.doubleValue();
									if (totalamount == 0) {
										totalshouruactivity_money_today
												.setText("0");
									} else {
										totalshouruactivity_money_today
												.setText(MathExtend.roundNew(
														totalamount, 2));
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} else {
								totalamount = 0;
							}
							getOfflineCountByBusiness();
						}

						@Override
						public void onResponseFailed(String msg) {
							loadUrl();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
*/
	/**
	 * 获取线下经营总收入
	 */
	private void getOfflineCountByBusiness() {
		okHttpsImp.getOfflineCountByBusiness(userShopInfoBean.getBusinessId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jsonObject = new JSONObject(result
									.getData());
							if (jsonObject.has("count")) {
								codtotalamount = jsonObject.getDouble("count");
							} else {
								codtotalamount = 0;
							}
							if(codtotalamount==0){
								online_shop_tv_num2.setText("0");
							}else{
							String dayPriceCOD = MathExtend.roundNew(
									codtotalamount, 2);
							online_shop_tv_num2.setText(dayPriceCOD);}
							getOnLineCountByBusiness();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						loadUrl();
					}
				});
	}

	/**
	 * 获取线上经营总收入
	 */
	private void getOnLineCountByBusiness() {
		okHttpsImp.getOnLineCountByBusiness(userShopInfoBean.getBusinessId(),
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						try {
							JSONObject jsonObject = new JSONObject(result
									.getData());
							if (jsonObject.has("count")) {
								wxtotalamount = jsonObject.getDouble("count");
							} else {
								wxtotalamount = 0;
							}
							if(wxtotalamount==0){
								online_shop_num.setText("0");
							}else{
								
							
							String dayPriceWX = MathExtend.roundNew(
									wxtotalamount, 2);
							online_shop_num.setText(dayPriceWX);}
							loadUrl();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onResponseFailed(String msg) {
						loadUrl();
					}
				});
	}

	private void loadUrl() {
		String myUrl = "";
		myUrl = pie(height);
		totalshouruactivity_webView_hgh_new.loadUrl(API.HOST + API.URLJS
				+ myUrl);
	}

	private int web() {
		WebViewInit.WebSettingInit(totalshouruactivity_webView_hgh_new, this);
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
		totalshouruactivity_llt_web_new.setLayoutParams(params);
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
