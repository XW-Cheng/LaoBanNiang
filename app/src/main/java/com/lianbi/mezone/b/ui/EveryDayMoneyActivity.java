package com.lianbi.mezone.b.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter;
import cn.com.hgh.baseadapter.DateRecylerviewAdapter.OnItemMonthClickListener;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.SpannableuUtills;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Ades_ImageEs;
import com.lianbi.mezone.b.bean.DateAndColor;
import com.lianbi.mezone.b.bean.MyLiCaiBean;

/**
 * 
 * @author guanghui.han 收益详情
 */
public class EveryDayMoneyActivity extends BaseActivity {

	TextView tv_year_month_choice, tv_list_money_detail;
	RecyclerView year_month_choice_recycler;
	ListView listview_list_money_detail;
	ImageView img_list_money_detail;
	/**
	 * 时间列表
	 */
	ArrayList<DateAndColor> arrayList = new ArrayList<DateAndColor>();
	/**
	 * 列表数据
	 */
	ArrayList<Ades_ImageEs> mDatas = new ArrayList<Ades_ImageEs>();
	Calendar time = Calendar.getInstance(Locale.CHINA);
	int cyear = time.get(Calendar.YEAR);
	int month = time.get(Calendar.MONTH);
	int day = time.get(Calendar.DAY_OF_MONTH);
	/**
	 * 当前月
	 */
	String cM = month + 1 + "";

	String username;
	String date;
	ArrayList<MyLiCaiBean> liCaiList = new ArrayList<MyLiCaiBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.everydaymoneyactivity, HAVETYPE);
		initView();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("每月收益");
		tv_year_month_choice = (TextView) findViewById(R.id.tv_year_month_choice);
		tv_list_money_detail = (TextView) findViewById(R.id.tv_list_money_detail);
		year_month_choice_recycler = (RecyclerView) findViewById(R.id.year_month_choice_recycler);
		listview_list_money_detail = (ListView) findViewById(R.id.listview_list_money_detail);
		img_list_money_detail = (ImageView) findViewById(R.id.img_list_money_detail);
		tv_year_month_choice.setText(cyear + "年");
		tv_list_money_detail.setText((month + 1) + "收入记录");
		date = cyear + "-" + (month + 1);
		initDate();
		getFinancingMyfinancingprofit();
		initListAdapter();
	}

	/**
	 * 按月取 收益详细 不分页
	 */
	private void getFinancingMyfinancingprofit() {
//		MyLiCaiBean bean = new MyLiCaiBean();
//		bean.setAmount(100f);
//		bean.setDate("2015 2 5");
//		bean.setDeadline(50);
//		bean.setDescription("你好我是描述");
//		bean.setId(74);
//		bean.setLable("d");
//		bean.setName("顶起理财");
//		bean.setRate(5.0);
//		MyLiCaiBean bean1 = new MyLiCaiBean();
//		bean1.setAmount(100);
//		bean1.setDate("2015 2 5");
//		bean1.setDeadline(50);
//		bean1.setDescription("你好我是描述");
//		bean1.setId(74);
//		bean1.setLable("h");
//		bean1.setName("顶起理财");
//		bean1.setRate(5.0);
//		liCaiList.add(bean1);
//		liCaiList.add(bean);
		
		// ActionImpl actionImpl = ActionImpl.newInstance(this);
		// actionImpl.getFinancingMyfinancingprofit(date, username,
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
		// liCaiList.clear();
		// String resString = result.getResult();
		// ArrayList<MyLiCaiBean> liCaiListL = (ArrayList<MyLiCaiBean>) JSON
		// .parseArray(resString, MyLiCaiBean.class);
		// if (liCaiListL != null && liCaiListL.size() > 0) {
		// liCaiList.addAll(liCaiListL);
		// listview_list_money_detail
		// .setVisibility(View.VISIBLE);
		// img_list_money_detail.setVisibility(View.GONE);
		// initListAdapter();
		// } else {
		// listview_list_money_detail.setVisibility(View.GONE);
		// img_list_money_detail.setVisibility(View.VISIBLE);
		// }
		// }
		// });
	}

	private void initDate() {
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
		year_month_choice_recycler.setLayoutManager(layoutManager);
		final DateRecylerviewAdapter dateAdapter = new DateRecylerviewAdapter(
				arrayList, this);
		dateAdapter.setNumDay(month);
		dateAdapter.setClickPosition(month);
		layoutManager.scrollToPosition(month);
		year_month_choice_recycler.setAdapter(dateAdapter);
		dateAdapter.setOnItemClickListener(new OnItemMonthClickListener() {

			@Override
			public void onItemMonthClickListener(View view, int position) {
				if (position > month) {
				} else {
					date = cyear + "-" + arrayList.get(position).getDay();
					getFinancingMyfinancingprofit();
					tv_list_money_detail.setText(arrayList.get(position)
							.getDay() + "月收入记录");
					dateAdapter.setClickPosition(position);
					dateAdapter.notifyDataSetChanged();
				}
			}

		});
	}

	QuickAdapter<MyLiCaiBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<MyLiCaiBean>(this,
				R.layout.everydaymoneyactivity_list_item, liCaiList) {

			@Override
			protected void convert(BaseAdapterHelper helper, MyLiCaiBean item) {
				helper.setText(R.id.tv_everydaymoneyactivity_list_item_time,
						item.getDate());
				TextView tv_everydaymoneyactivity_list_item_price = helper
						.getView(R.id.tv_everydaymoneyactivity_list_item_price);
				String price = MathExtend.roundNew(item.getAmount(), 2) + "";
				String one = "+ ";
				String two = price.substring(0, price.indexOf("."));
				String three = price.substring(price.indexOf("."),
						price.length());
				SpannableuUtills.setSpannableu(
						tv_everydaymoneyactivity_list_item_price, one, two,
						three);
			}
		};
		// 设置适配器
		listview_list_money_detail.setAdapter(mAdapter);
		AbViewUtil.setListViewHeight(listview_list_money_detail);
	}

}
