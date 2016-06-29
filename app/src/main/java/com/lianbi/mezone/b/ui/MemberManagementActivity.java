package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xizhi.mezone.b.R;

/**
 * 会员管理
 * 
 * @time 上午10:30:48
 * @date 2016-1-18
 * @author hongyu.yang
 * 
 */
public class MemberManagementActivity extends BaseActivity {
	private LinearLayout llt_member_management_formulate,
			llt_member_management_newly_increase, llt_member_management;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_management, NOTYPE);
		initView();
		setLisenter();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("会员管理");
		llt_member_management_formulate = (LinearLayout) findViewById(R.id.llt_member_management_formulate);
		llt_member_management_newly_increase = (LinearLayout) findViewById(R.id.llt_member_management_newly_increase);
		llt_member_management = (LinearLayout) findViewById(R.id.llt_member_management);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		llt_member_management_formulate.setOnClickListener(this);
		llt_member_management_newly_increase.setOnClickListener(this);
		llt_member_management.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.llt_member_management_formulate:// 会员制定
			Intent intent_formulate = new Intent(this,
					MemberManagementFormulateActivity.class);
			startActivity(intent_formulate);
			break;
		case R.id.llt_member_management_newly_increase:// 新增会员
			Intent intent_formulateA = new Intent(this,
					AddNewMemberActivity.class);
			startActivity(intent_formulateA);

			break;
		case R.id.llt_member_management:// 会员管理
			Intent intent_formulateG = new Intent(this,
					MyMemberManagementActivity.class);
			startActivity(intent_formulateG);
			break;
		}
	}

}
