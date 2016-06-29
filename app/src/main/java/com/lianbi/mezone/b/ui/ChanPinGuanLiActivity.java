package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xizhi.mezone.b.R;

public class ChanPinGuanLiActivity extends BaseActivity {

	private RelativeLayout mRlChanpinfb;
	private RelativeLayout mRlChanpinchigl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chanpingl, NOTYPE);

		initView();
		setLisenter();
	}

	private void initView() {
		setPageTitle("产品管理");
		mRlChanpinfb = (RelativeLayout) findViewById(R.id.rl_chanpinfb);
		mRlChanpinchigl = (RelativeLayout) findViewById(R.id.rl_chanpinchigl);
	}

	private void setLisenter() {
		mRlChanpinfb.setOnClickListener(this);
		mRlChanpinchigl.setOnClickListener(this);
	}

	public void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.rl_chanpinfb://本店产品
			Intent intent1 = new Intent(this, MyShopChanPinfbActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_chanpinchigl://产品仓库
			Intent intent = new Intent(this, ChanPinCKActivity.class);
			startActivity(intent);
			break;
		}
	}

}
