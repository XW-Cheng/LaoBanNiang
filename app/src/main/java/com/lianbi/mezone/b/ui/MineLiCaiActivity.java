package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.SpannableuUtills;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MyLiCaiBean;

/**
 * 我的理财页面
 * 
 * @author qiuyu.lv
 * @date 2015-12-8
 */

public class MineLiCaiActivity extends BaseActivity {

	private TextView title_one_tv, title_two_tv,
			shourudesitionactivity_money_today,
			shourudesitionactivity_money_total;

	private TextView minelicai_tv_des;

	LinearLayout llt_total_earnings;
	private ListView activity_mine_li_cai_list;
	ArrayList<MyLiCaiBean> mDatas = new ArrayList<MyLiCaiBean>();
	/**
	 * 我的余额
	 */
	double total;
	/**
	 * 收益总额
	 */
	double totalprofit;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_li_cai, HAVETYPE);
		initView();
		initListAdapter();
	}

	protected void initView() {
		setPageTitle("我的理财");
		title_one_tv = (TextView) findViewById(R.id.title_one_tv);
		title_two_tv = (TextView) findViewById(R.id.title_two_tv);
		shourudesitionactivity_money_today = (TextView) findViewById(R.id.shourudesitionactivity_money_today);
		shourudesitionactivity_money_total = (TextView) findViewById(R.id.shourudesitionactivity_money_total);
		minelicai_tv_des = (TextView) findViewById(R.id.minelicai_tv_des);
		llt_total_earnings = (LinearLayout) findViewById(R.id.llt_total_earnings);
		activity_mine_li_cai_list = (ListView) findViewById(R.id.activity_mine_li_cai_list);
		minelicai_tv_des.setOnClickListener(this);
		llt_total_earnings.setOnClickListener(this);
		totalprofit = getIntent().getDoubleExtra("totalprofit", 0.00);
		total = getIntent().getDoubleExtra("total", 0.00);
		shourudesitionactivity_money_today.setText(MathExtend
				.roundNew(total, 2) + "");
		shourudesitionactivity_money_total.setText(MathExtend.roundNew(
				totalprofit, 2) + "");
		title_one_tv.setText("理财余额  (元)");
		title_two_tv.setText("总收益  (元)");
		getFinancingMyfinancingproduct();
	}

	/**
	 * 我购买的理财产品列表
	 */
	private void getFinancingMyfinancingproduct() {
//		MyLiCaiBean bean = new MyLiCaiBean();
//		bean.setAmount(100f);
//		bean.setDate("2015-2-5");
//		bean.setDeadline(50);
//		bean.setDescription("你好我是描述");
//		bean.setId(74);
//		bean.setLable("d");
//		bean.setName("定期理财");
//		bean.setRate(5.0);
//		MyLiCaiBean bean1 = new MyLiCaiBean();
//		bean1.setAmount(100);
//		bean1.setDate("2015-2-5");
//		bean1.setDeadline(50);
//		bean1.setDescription("你好我是描述");
//		bean1.setId(84);
//		bean1.setLable("h");
//		bean1.setName("活期理财");
//		bean1.setRate(3.0);
//		mDatas.add(bean1);
//		mDatas.add(bean);
		// ActionImpl actionImpl = ActionImpl.newInstance(this);
		// actionImpl.getFinancingMyfinancingproduct(username,
		// new ResultHandlerCallback() {
		//
		// @Override
		// public void rc999(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc3001(RequestEntity entity, Result result) {
		//
		// }
		//
		// @Override
		// public void rc0(RequestEntity entity, Result result) {
		// String resString = result.getResult();
		// ArrayList<MyLiCaiBean> arrayListL = (ArrayList<MyLiCaiBean>) JSON
		// .parseArray(resString, MyLiCaiBean.class);
		// if (arrayListL != null && arrayListL.size() > 0) {
		// mDatas.addAll(arrayListL);
		// initListAdapter();
		// ListViewHeight
		// .setPullLvHeight(activity_mine_li_cai_list);
		// }
		// }
		// });
	}
	QuickAdapter<MyLiCaiBean> mAdapter;
	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<MyLiCaiBean>(
				this, R.layout.regulardemandmanagementactivity_list_item,
				mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper, MyLiCaiBean item) {
				ImageView img = helper
						.getView(R.id.regulardemandmanagementactivity_list_item_iv);
				img.setScaleType(ScaleType.CENTER_INSIDE);
				LinearLayout.LayoutParams layoutParams = (LayoutParams) img
						.getLayoutParams();
				layoutParams.height = (int) AbViewUtil.dip2px(
						MineLiCaiActivity.this, 50);
				layoutParams.width = (int) AbViewUtil.dip2px(
						MineLiCaiActivity.this, 50);
				img.setImageResource(R.mipmap.service_new_right);
				TextView regulardemandmanagementactivity_list_item_yearlv = helper
						.getView(R.id.regulardemandmanagementactivity_list_item_yearlv);
				helper.setText(
						R.id.regulardemandmanagementactivity_list_item_name,
						item.getName());
				helper.setText(
						R.id.regulardemandmanagementactivity_list_item_dealline,
						item.getDeadline() + "个月");
				String price = item.getRate() + "";
				String two = price.substring(0, price.indexOf("."));
				String three = price.substring(price.indexOf("."),
						price.length());
				SpannableuUtills.setSpannableu(
						regulardemandmanagementactivity_list_item_yearlv, "",
						two, three + "%");
			}
		};
		// 设置适配器
		activity_mine_li_cai_list.setAdapter(mAdapter);
		AbViewUtil.setListViewHeight(activity_mine_li_cai_list);

	}

	@Override
	protected void onChildClick(View v) {
		switch (v.getId()) {
		case R.id.minelicai_tv_des:// 查看详情
			startActivity(new Intent(this, EveryDayMoneyActivity.class));
			break;
		case R.id.llt_total_earnings:// 收益详情
			// Intent intent_earnings = new Intent(this,
			// EarningsDetailActivity.class);
			// startActivity(intent_earnings);
			break;
		}

	}

}
