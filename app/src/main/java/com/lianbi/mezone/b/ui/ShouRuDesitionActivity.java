package com.lianbi.mezone.b.ui;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 收入详情
 * 
 * @time 下午9:21:05
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class ShouRuDesitionActivity extends BaseActivity {

	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	Date now = new Date();
	String date = dateformat.format(now);

	TextView shourudesitionactivity_money_today,
			shourudesitionactivity_money_total;

	LinearLayout shourudesitionactivity_llt_today_online_jin,
			shourudesitionactivity_llt_today_shop_jin,
			shourudesitionactivity_llt_total_jin,
			shourudesitionactivity_llt_last_week_jin,
			shourudesitionactivity_llt_year_month_jin, llt_total_earnings;

	/**
	 * 今日收入总数
	 */
	private double totalamountToday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shourudesitionactivity, HAVETYPE);
		initView();
		setLisenter();
		getIncomeTodayincome();
		// getIncomeTotalincome();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		shourudesitionactivity_llt_today_online_jin.setOnClickListener(this);
		shourudesitionactivity_llt_today_shop_jin.setOnClickListener(this);
		shourudesitionactivity_llt_total_jin.setOnClickListener(this);
		shourudesitionactivity_llt_last_week_jin.setOnClickListener(this);
		shourudesitionactivity_llt_year_month_jin.setOnClickListener(this);
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("收入详情");
		llt_total_earnings = (LinearLayout) findViewById(R.id.llt_total_earnings);
		llt_total_earnings.setVisibility(View.GONE);
		shourudesitionactivity_money_today = (TextView) findViewById(R.id.shourudesitionactivity_money_today);
		shourudesitionactivity_money_total = (TextView) findViewById(R.id.shourudesitionactivity_money_total);
		shourudesitionactivity_llt_today_online_jin = (LinearLayout) findViewById(R.id.shourudesitionactivity_llt_today_online_jin);
		shourudesitionactivity_llt_today_shop_jin = (LinearLayout) findViewById(R.id.shourudesitionactivity_llt_today_shop_jin);
		shourudesitionactivity_llt_total_jin = (LinearLayout) findViewById(R.id.shourudesitionactivity_llt_total_jin);
		shourudesitionactivity_llt_last_week_jin = (LinearLayout) findViewById(R.id.shourudesitionactivity_llt_last_week_jin);
		shourudesitionactivity_llt_year_month_jin = (LinearLayout) findViewById(R.id.shourudesitionactivity_llt_year_month_jin);
	}

	/**
	 * 总收入
	 */
	private void getIncomeTotalincome() {
		double totalamount = 222.22;
		String price = MathExtend.roundNew(totalamount, 2);
		shourudesitionactivity_money_total.setText(price);
		// ActionImpl actionImpl = ActionImpl.newInstance(this);
		// actionImpl.incomeTotalincome(username, new ResultHandlerCallback() {
		// private double totalamount;
		//
		// @Override
		// public void rc999(RequestEntity entity, Result result) {
		// }
		//
		// @Override
		// public void rc3001(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc0(RequestEntity entity, Result result) {
		// // TODO Auto-generated method stub
		// String resString = result.getResult();
		// JSONObject jsonObject;
		// try {
		// jsonObject = new JSONObject(resString);
		// totalamount = jsonObject.getDouble("totalamount");
		// String price = MathExtend.roundNew(totalamount, 2);
		// shourudesitionactivity_money_total.setText(price);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// });
	}

	/**
	 * 今日总收入
	 * 
	 * @param type
	 *            string 收入方式 0：今天总收入 1： 今天在线收入 2：到店收入
	 */
	private void getIncomeTodayincome() {
		// okHttpsImp.getCountByDay(userShopInfoBean.getBusinessId(), date, "0",
		// new MyResultCallback<String>() {
		//
		// @Override
		// public void onResponseResult(Result result) {
		// try {
		// JSONObject jsonObject = new JSONObject(result
		// .getData());
		// totalamountToday = jsonObject.getDouble("count");
		// String price = MathExtend.roundNew(
		// totalamountToday, 2);
		// shourudesitionactivity_money_today.setText(price);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		//
		// }
		//
		// @Override
		// public void onResponseFailed(String msg) {
		// totalamountToday = 0;
		// String price = MathExtend.roundNew(totalamountToday, 2);
		// shourudesitionactivity_money_today.setText(price);
		// }
		// });
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.queryAccountTodayIncome(OkHttpsImp.md5_key,
					uuid,"app",reqTime,
					userShopInfoBean.getUserId(),
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reString = result.getData();
							if (!TextUtils.isEmpty(reString)) {
									totalamountToday = BigDecimal
											.valueOf(
													Long.valueOf(result.getData()))
											.divide(new BigDecimal(100))
											.doubleValue();
									if(totalamountToday==0){
										shourudesitionactivity_money_today.setText("0");
									}else{
									shourudesitionactivity_money_today
											.setText(MathExtend.roundNew(
													totalamountToday, 2));}
							} else {
								totalamountToday = 0;
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							totalamountToday = 0;
							String price = MathExtend.roundNew(
									totalamountToday, 2);
							shourudesitionactivity_money_today.setText("0");
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.shourudesitionactivity_llt_today_online_jin:// 今日在线收入
			startActivity(new Intent(this, ShouRuActivity.class).putExtra(
					"title", "今日在线收入"));
			break;
		case R.id.shourudesitionactivity_llt_today_shop_jin:// 今日到店收入
			startActivity(new Intent(this, ShouRuActivity.class).putExtra(
					"title", "今日到店收入"));
			break;
		case R.id.shourudesitionactivity_llt_total_jin:// 总收入
			startActivity(new Intent(this, TotalShouRuActivity.class));
			break;
		case R.id.shourudesitionactivity_llt_last_week_jin:// 上周每日收入分布

			startActivity(new Intent(this, DailyShouRuActivity.class));
			break;
		case R.id.shourudesitionactivity_llt_year_month_jin:// 本年月度收入
			startActivity(new Intent(this, MonthShouRuActivity.class));

			break;
		}
	}

}
