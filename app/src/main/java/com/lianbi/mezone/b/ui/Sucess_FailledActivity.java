package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xizhi.mezone.b.R;

public class Sucess_FailledActivity extends BaseActivity {

	ImageView sucess_failledactivity_iv_failed,
			sucess_failledactivity_iv_sucess_failed;
	TextView sucess_failledactivity_tv1, sucess_failledactivity_tv2;
	LinearLayout layout_sucess_failed;
	private final long TIME = 2000;
	int key;
	private int isFailed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sucess_failledactivity, HAVETYPE);
		initView();
	}

	/**
	 * 初始化VIew
	 */
	protected void initView() {
		key = getIntent().getIntExtra("key", -1);
		isFailed = getIntent().getIntExtra("st", 0);
		sucess_failledactivity_iv_failed = (ImageView) findViewById(R.id.sucess_failledactivity_iv_failed);
		sucess_failledactivity_iv_sucess_failed = (ImageView) findViewById(R.id.sucess_failledactivity_iv_sucess_failed);
		sucess_failledactivity_tv1 = (TextView) findViewById(R.id.sucess_failledactivity_tv1);
		sucess_failledactivity_tv2 = (TextView) findViewById(R.id.sucess_failledactivity_tv2);
		layout_sucess_failed = (LinearLayout) findViewById(R.id.layout_sucess_failed);
		layout_sucess_failed.setVisibility(View.VISIBLE);
		getIsBand();
	}

	/**
	 * 切换界面
	 * 
	 * @param key
	 *            1:提现结果，2:绑订结果 3：添加结果
	 * @param isSucess
	 */
	private void switchInterface(int key, boolean isSucess) {
		switch (key) {
		case 1:
			if (isSucess) {
				setPageTitle(R.string.hgh_tixianresult);
				setPageBackVisibility(View.VISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_finish);
				sucess_failledactivity_iv_failed.setVisibility(View.GONE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_sucess_tixian1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_tixian2));
			} else {
				setPageTitle(R.string.hgh_tixianresult);
				setPageBackVisibility(View.VISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_fail);
				sucess_failledactivity_iv_failed.setVisibility(View.VISIBLE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_failed_tixian1));
			}

			break;
		case 2:
			if (isSucess) {
				setPageTitle(R.string.hgh_sucess_bangding);
				setPageBackVisibility(View.INVISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_finish);
				sucess_failledactivity_iv_failed.setVisibility(View.GONE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_sucess_bangding1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_bangding2));
			} else {
				setPageTitle(R.string.hgh_failed_bangding);
				setPageBackVisibility(View.INVISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_fail);
				sucess_failledactivity_iv_failed.setVisibility(View.VISIBLE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_failed_bangding1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_bangding2));
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(Sucess_FailledActivity.this,
							MainActivity.class);
					startActivity(intent);
					setResult(RESULT_OK);
					finish();
				}
			}, TIME);
			break;
		case 4:
			if (isSucess) {
				setPageTitle(getResources().getString(
						R.string.hgh_sucess_tianjia));
				setPageBackVisibility(View.INVISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_finish);
				sucess_failledactivity_iv_failed.setVisibility(View.INVISIBLE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_sucess_tianjia1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_tianjia24));
			} else {
				setPageTitle(getResources().getString(
						R.string.hgh_failed_tianjia));
				setPageBackVisibility(View.GONE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_fail);
				sucess_failledactivity_iv_failed.setVisibility(View.VISIBLE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_failed_tianjia1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_tianjia24));
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					setResult(RESULT_OK);
					finish();
				}
			}, TIME);
			break;
		case 3:
			if (isSucess) {
				setPageTitle(R.string.hgh_sucess_tianjia);
				setPageBackVisibility(View.INVISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_finish);
				sucess_failledactivity_iv_failed.setVisibility(View.GONE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_sucess_tianjia1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_tianjia2));
			} else {
				setPageTitle(R.string.hgh_failed_tianjia);
				setPageBackVisibility(View.INVISIBLE);
				sucess_failledactivity_iv_sucess_failed
						.setImageResource(R.mipmap.add_bank_card_fail);
				sucess_failledactivity_iv_failed.setVisibility(View.VISIBLE);
				sucess_failledactivity_tv1.setText(getResources().getString(
						R.string.hgh_failed_tianjia1));
				sucess_failledactivity_tv2.setText(getResources().getString(
						R.string.hgh_sucess_tianjia2));
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					setResult(RESULT_OK);
					finish();
				}
			}, TIME);
			break;
		}
	}

	/**
	 * 根据用户名判断 该用户是否已经绑定了银行卡
	 */
	private void getIsBand() {
		switchInterface(key, true);
	}
}
