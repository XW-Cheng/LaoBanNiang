package com.lianbi.mezone.b.ui;

import java.math.BigDecimal;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.SpannableuUtills;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.WithDrawDeposite;

public class WithdrawDepositDesitionActivity extends BaseActivity {

	TextView withdrawdepositdesitionactivity_tv_sn,
			withdrawdepositdesitionactivity_tv_status,
			withdrawdepositdesitionactivity_tv_withdraw_content,
			withdrawdepositdesitionactivity_tv_withdraw_time,
			withdrawdepositdesitionactivity_tv_withdraw_money;
	WithDrawDeposite withDrawDeposite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withdrawdepositdesitionactivity, HAVETYPE);
		initView();
	}

	protected void initView() {
		setPageTitle("交易详情");
		withdrawdepositdesitionactivity_tv_sn = (TextView) findViewById(R.id.withdrawdepositdesitionactivity_tv_sn);
		withdrawdepositdesitionactivity_tv_status = (TextView) findViewById(R.id.withdrawdepositdesitionactivity_tv_status);
		withdrawdepositdesitionactivity_tv_withdraw_content = (TextView) findViewById(R.id.withdrawdepositdesitionactivity_tv_withdraw_content);
		withdrawdepositdesitionactivity_tv_withdraw_time = (TextView) findViewById(R.id.withdrawdepositdesitionactivity_tv_withdraw_time);
		withdrawdepositdesitionactivity_tv_withdraw_money = (TextView) findViewById(R.id.withdrawdepositdesitionactivity_tv_withdraw_money);
		withDrawDeposite = (WithDrawDeposite) getIntent().getSerializableExtra(
				"item");
		if (withDrawDeposite != null) {
			withdrawdepositdesitionactivity_tv_sn.setText(withDrawDeposite
					.getId());
			/**
			 * 01:审核中 02：以提现 03：未通过
			 */

			String status = withDrawDeposite.getStatus();
			String sStr = "";
			int colorRe = 0;
			if ("01".equals(status)) {
				sStr = "审核中";
				colorRe = R.color.colores_news_04;
			} else if ("02".equals(status)) {
				sStr = "已提现";
				colorRe = R.color.colores_news_06;
			} else {
				sStr = "未通过";
				colorRe = R.color.colores_news_12;
			}
			// switch (status) {
			// case 0:
			// sStr = "已提现";
			// colorRe = R.color.colores_news_06;
			// break;
			// case 1:
			// sStr = "审核中";
			// colorRe = R.color.colores_news_04;
			// break;
			// case 2:
			// sStr = "未通过";
			// colorRe = R.color.colores_news_12;
			// break;
			// }

			GradientDrawable drawable = (GradientDrawable) withdrawdepositdesitionactivity_tv_status
					.getBackground();
			drawable.setColor(getResources().getColor(colorRe));
			withdrawdepositdesitionactivity_tv_status
					.setBackgroundDrawable(drawable);
			withdrawdepositdesitionactivity_tv_status.setText(sStr);

			withdrawdepositdesitionactivity_tv_withdraw_content.setText("转出到"
					+ withDrawDeposite.getBanknum());
			// setPrice(withDrawDeposite.getAmount(),
			// withdrawdepositdesitionactivity_tv_withdraw_money);
			
			double money = BigDecimal
					.valueOf(Long.valueOf(withDrawDeposite.getAmount()))
					.divide(new BigDecimal(100))
					.doubleValue();
			withdrawdepositdesitionactivity_tv_withdraw_money.setText("-"+MathExtend.roundNew(
							money, 2));
//			withdrawdepositdesitionactivity_tv_withdraw_money
//					.setText(withDrawDeposite.getAmount());
			withdrawdepositdesitionactivity_tv_withdraw_time
					.setText(withDrawDeposite.getCreateTime());
		}
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
