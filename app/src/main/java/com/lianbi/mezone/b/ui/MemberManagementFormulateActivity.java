package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MemberDevelopmentBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 会员制定
 * 
 * @time 上午10:48:55
 * @date 2016-1-18
 * @author hongyu.yang
 * 
 */
@SuppressLint("ResourceAsColor")
public class MemberManagementFormulateActivity extends BaseActivity {

	private LinearLayout lltMemberFormulate1, lltMemberFormulate2,
			lltMemberFormulate3, lltMemberFormulate4, lltMemberFormulate5;
	private TextView tvMemberFormulate1, tvMemberFormulate2,
			tvMemberFormulate3, tvMemberFormulate4, tvMemberFormulate5;
	/**
	 * 数据列表
	 */
	ArrayList<MemberDevelopmentBean> arrayList = new ArrayList<MemberDevelopmentBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_management_formulate, HAVETYPE);
		initView();
		setLisenter();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getAssociatorLevelList();
	}

	/**
	 * 获取数据
	 */
	private void getAssociatorLevelList() {
		okHttpsImp.getAssociatorLevelList(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				String mYresult = result.getData();
				try {
					org.json.JSONObject jb = new org.json.JSONObject(mYresult);
					String jbRe = jb.getString("associatorLevelList");
					if (!TextUtils.isEmpty(jbRe)) {
						arrayList = (ArrayList<MemberDevelopmentBean>) JSON
								.parseArray(jbRe, MemberDevelopmentBean.class);
						upView();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onResponseFailed(String msg) {

			}
		}, userShopInfoBean.getBusinessId());
	}

	/**
	 * 刷新界面
	 */
	protected void upView() {
		int s = arrayList.size();
		switch (s) {
		case 1:
			tvMemberFormulate1.setText(arrayList.get(0).getB_level_name());
			break;
		case 2:
			tvMemberFormulate1.setText(arrayList.get(0).getB_level_name());
			tvMemberFormulate2.setVisibility(View.VISIBLE);
			tvMemberFormulate2.setText(arrayList.get(1).getB_level_name());

			break;
		case 3:
			tvMemberFormulate1.setText(arrayList.get(0).getB_level_name());
			tvMemberFormulate2.setVisibility(View.VISIBLE);
			tvMemberFormulate3.setVisibility(View.VISIBLE);
			tvMemberFormulate2.setText(arrayList.get(1).getB_level_name());
			tvMemberFormulate3.setText(arrayList.get(2).getB_level_name());

			break;
		case 4:
			tvMemberFormulate1.setText(arrayList.get(0).getB_level_name());
			tvMemberFormulate2.setVisibility(View.VISIBLE);
			tvMemberFormulate3.setVisibility(View.VISIBLE);
			tvMemberFormulate4.setVisibility(View.VISIBLE);
			tvMemberFormulate2.setText(arrayList.get(1).getB_level_name());
			tvMemberFormulate3.setText(arrayList.get(2).getB_level_name());
			tvMemberFormulate4.setText(arrayList.get(3).getB_level_name());

			break;
		case 5:

			tvMemberFormulate1.setText(arrayList.get(0).getB_level_name());
			tvMemberFormulate2.setVisibility(View.VISIBLE);
			tvMemberFormulate3.setVisibility(View.VISIBLE);
			tvMemberFormulate4.setVisibility(View.VISIBLE);
			tvMemberFormulate5.setVisibility(View.VISIBLE);
			tvMemberFormulate2.setText(arrayList.get(1).getB_level_name());
			tvMemberFormulate3.setText(arrayList.get(2).getB_level_name());
			tvMemberFormulate4.setText(arrayList.get(3).getB_level_name());
			tvMemberFormulate5.setText(arrayList.get(4).getB_level_name());
			break;
		}
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("会员制定");
		setPageRightText("说明");
		setPageRightTextColor(R.color.colores_news_01);
		lltMemberFormulate1 = (LinearLayout) findViewById(R.id.llt_member_formulate_1);
		tvMemberFormulate1 = (TextView) findViewById(R.id.tv_member_formulate_1);
		lltMemberFormulate2 = (LinearLayout) findViewById(R.id.llt_member_formulate_2);
		tvMemberFormulate2 = (TextView) findViewById(R.id.tv_member_formulate_2);
		lltMemberFormulate3 = (LinearLayout) findViewById(R.id.llt_member_formulate_3);
		tvMemberFormulate3 = (TextView) findViewById(R.id.tv_member_formulate_3);
		lltMemberFormulate4 = (LinearLayout) findViewById(R.id.llt_member_formulate_4);
		tvMemberFormulate4 = (TextView) findViewById(R.id.tv_member_formulate_4);
		lltMemberFormulate5 = (LinearLayout) findViewById(R.id.llt_member_formulate_5);
		tvMemberFormulate5 = (TextView) findViewById(R.id.tv_member_formulate_5);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		lltMemberFormulate1.setOnClickListener(this);
		lltMemberFormulate2.setOnClickListener(this);
		lltMemberFormulate3.setOnClickListener(this);
		lltMemberFormulate4.setOnClickListener(this);
		lltMemberFormulate5.setOnClickListener(this);
	}

	/**
	 * 是否不能编辑
	 */
	boolean isNOEDIT;
	/**
	 * 位置
	 */
	int p;

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		int s = arrayList.size();
		MemberDevelopmentBean memberDevelopmentBean = null;
		switch (view.getId()) {
		case R.id.llt_member_formulate_1:
			isNOEDIT = true;
			p=0;
			if (s > p) {
				memberDevelopmentBean = arrayList.get(p);
			}
			Intent intent_0 = new Intent(this, EditeFormulateActivity.class);
			intent_0.putExtra("bean", memberDevelopmentBean);
			intent_0.putExtra("isNOEDIT", isNOEDIT);
			startActivity(intent_0);
			break;
		case R.id.llt_member_formulate_2:
			isNOEDIT = false;
			p = 1;
			if (s > p) {
				memberDevelopmentBean = arrayList.get(p);
			}
			Intent intent_1 = new Intent(this, EditeFormulateActivity.class);

			intent_1.putExtra("bean", memberDevelopmentBean);
			intent_1.putExtra("isNOEDIT", isNOEDIT);
			startActivity(intent_1);
			break;
		case R.id.llt_member_formulate_3:
			isNOEDIT = false;
			p = 2;
			if (s > p) {
				memberDevelopmentBean = arrayList.get(p);
			}
			Intent intent_2 = new Intent(this, EditeFormulateActivity.class);

			intent_2.putExtra("bean", memberDevelopmentBean);
			intent_2.putExtra("isNOEDIT", isNOEDIT);
			startActivity(intent_2);
			break;
		case R.id.llt_member_formulate_4:
			isNOEDIT = false;
			p = 3;
			if (s > p) {
				memberDevelopmentBean = arrayList.get(p);
			}
			Intent intent_3 = new Intent(this, EditeFormulateActivity.class);

			intent_3.putExtra("bean", memberDevelopmentBean);
			intent_3.putExtra("isNOEDIT", isNOEDIT);
			startActivity(intent_3);
			break;
		case R.id.llt_member_formulate_5:
			isNOEDIT = false;
			p = 4;
			if (s > p) {
				memberDevelopmentBean = arrayList.get(p);
			}
			Intent intent_4 = new Intent(this, EditeFormulateActivity.class);

			intent_4.putExtra("bean", memberDevelopmentBean);
			intent_4.putExtra("isNOEDIT", isNOEDIT);
			startActivity(intent_4);
			break;

		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		Intent intent_explain = new Intent(this, MemberExplainActivity.class);
		startActivity(intent_explain);
	}
}
