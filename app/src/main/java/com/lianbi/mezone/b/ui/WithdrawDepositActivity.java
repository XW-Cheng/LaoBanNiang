package com.lianbi.mezone.b.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;
import cn.com.hgh.view.AbPullToRefreshView;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.WithDrawDeposite;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

/**
 * 交易明細
 * 
 * @time 下午1:35:23
 * @date 2016-1-12
 * @author hongyu.yang
 * 
 */
public class WithdrawDepositActivity extends BaseActivity {
	TextView tv_withdraw_deposit_money, activity_withdraw_deposit_tv_one,
			activity_withdraw_deposit_tv_two;

	View activity_withdraw_deposit_line_one,
			activity_withdraw_deposit_line_two;
	AbPullToRefreshView pull_withdraw_deposit;
	ListView lv_withdraw_deposit;
	ImageView iv_empty_act_withdraw;
	ArrayList<WithDrawDeposite> arrayList = new ArrayList<WithDrawDeposite>();
	ArrayList<WithDrawDeposite> arrayListCur = new ArrayList<WithDrawDeposite>();
	private double totalamount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_deposit, NOTYPE);
		totalamount = getIntent().getDoubleExtra("totalamount", 0);
		initView();
		initAdapter();
		getBankWithdrawrecord();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("交易明细");
		tv_withdraw_deposit_money = (TextView) findViewById(R.id.tv_withdraw_deposit_money);
		activity_withdraw_deposit_tv_one = (TextView) findViewById(R.id.activity_withdraw_deposit_tv_one);
		activity_withdraw_deposit_tv_two = (TextView) findViewById(R.id.activity_withdraw_deposit_tv_two);
		activity_withdraw_deposit_line_one = findViewById(R.id.activity_withdraw_deposit_line_one);
		activity_withdraw_deposit_line_two = findViewById(R.id.activity_withdraw_deposit_line_two);
		pull_withdraw_deposit = (AbPullToRefreshView) findViewById(R.id.pull_withdraw_deposit);
		lv_withdraw_deposit = (ListView) findViewById(R.id.lv_withdraw_deposit);
		iv_empty_act_withdraw = (ImageView) findViewById(R.id.iv_empty_act_withdraw);
		String sum_income = MathExtend.roundNew(totalamount, 2);
		if (null != sum_income) {
			tv_withdraw_deposit_money.setText(sum_income);
		}else{
			tv_withdraw_deposit_money.setText("0.00");
		}
		initPullView();
		listen();
	}

	/**
	 * 添加监听
	 */
	private void listen() {
		activity_withdraw_deposit_tv_one.setOnClickListener(this);
		activity_withdraw_deposit_tv_two.setOnClickListener(this);
		lv_withdraw_deposit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				WithDrawDeposite withDrawDeposite = arrayListCur.get(arg2);
				startActivity(new Intent(WithdrawDepositActivity.this,
						WithdrawDepositDesitionActivity.class).putExtra("item",
						withDrawDeposite));
			}
		});

	}

	/**
	 * 初始化pullView
	 */
	private void initPullView() {
		pull_withdraw_deposit.setLoadMoreEnable(false);
		pull_withdraw_deposit.setPullRefreshEnable(false);
	}

	boolean one_two = true;

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.activity_withdraw_deposit_tv_one:
			oneT();
			arrayListCur.clear();
			arrayListCur.addAll(arrayList);
			if (arrayListCur.size() > 0) {

				ivG();
			} else {

				ivV();
			}
			break;
		case R.id.activity_withdraw_deposit_tv_two:
			twoT();
			arrayListCur.clear();
			ivV();
			break;
		}
		adapter.replaceAll(arrayListCur);
	}

	/**
	 * 空图片隐藏
	 */

	private void ivG() {
		iv_empty_act_withdraw.setVisibility(View.GONE);
		lv_withdraw_deposit.setVisibility(View.VISIBLE);
	}

	/**
	 * 空图片显示
	 */
	private void ivV() {
		iv_empty_act_withdraw.setVisibility(View.VISIBLE);
		lv_withdraw_deposit.setVisibility(View.GONE);
	}

	private void twoT() {
		one_two = false;
		activity_withdraw_deposit_tv_two.setTextColor(getResources().getColor(
				R.color.colores_news_01));
		activity_withdraw_deposit_tv_one.setTextColor(getResources().getColor(
				R.color.colores_news_10));
		activity_withdraw_deposit_line_two.setVisibility(View.VISIBLE);
		activity_withdraw_deposit_line_one.setVisibility(View.INVISIBLE);
	}

	private void oneT() {
		one_two = true;
		activity_withdraw_deposit_tv_one.setTextColor(getResources().getColor(
				R.color.colores_news_01));
		activity_withdraw_deposit_tv_two.setTextColor(getResources().getColor(
				R.color.colores_news_10));
		activity_withdraw_deposit_line_one.setVisibility(View.VISIBLE);
		activity_withdraw_deposit_line_two.setVisibility(View.INVISIBLE);
	}

	private void getBankWithdrawrecord() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getWithDrawByUserId(OkHttpsImp.md5_key,
					userShopInfoBean.getUserId(),
					userShopInfoBean.getBusinessId(), "0", 20 + "", uuid,
					"app", reqTime,

					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String reString = result.getData();
							if (!TextUtils.isEmpty(reString)) {
								arrayList = (ArrayList<WithDrawDeposite>) com.alibaba.fastjson.JSONObject
										.parseArray(reString,
												WithDrawDeposite.class);
								arrayListCur.clear();
								arrayListCur.addAll(arrayList);
								if (arrayListCur.size() > 0) {

									ivG();
								} else {

									ivV();
								}
								adapter.replaceAll(arrayListCur);

							}
						}

						@Override
						public void onResponseFailed(String msg) {

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	QuickAdapter<WithDrawDeposite> adapter;

	private void initAdapter() {
		adapter = new QuickAdapter<WithDrawDeposite>(this,
				R.layout.item_withdrawal, arrayListCur) {

			@Override
			protected void convert(BaseAdapterHelper helper,
					WithDrawDeposite item) {
				TextView tv_withdraw_content = helper
						.getView(R.id.tv_withdraw_content);
				TextView tv_withdraw_time = helper
						.getView(R.id.tv_withdraw_time);
				TextView tv_withdraw_money = helper
						.getView(R.id.tv_withdraw_money);
				tv_withdraw_content.setText("转出到" + item.getBanknum());
				tv_withdraw_time.setText(item.getCreateTime());
				// setPrice(item.getAmount(), tv_withdraw_money);
				double money = BigDecimal
						.valueOf(Long.valueOf(item.getAmount()))
						.divide(new BigDecimal(100)).doubleValue();
				tv_withdraw_money.setText("-" + MathExtend.roundNew(money, 2));
			}
		};
		lv_withdraw_deposit.setAdapter(adapter);
	}

	/**
	 * 设置价格
	 * 
	 * @param pz
	 */
	private void setPrice(String pz, TextView tv) {
		String price = "-" + MathExtend.round(pz, 2);
		if (price.contains(".")) {
			int p = price.indexOf(".");
			String head_big = price.substring(0, p);
			String end_small = price.substring(p, price.length());
			SpannableuUtills.setSpannableu(tv, head_big, end_small);
		} else {
			tv.setText(price);
		}
	}

}
