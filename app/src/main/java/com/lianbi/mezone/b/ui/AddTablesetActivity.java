package com.lianbi.mezone.b.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

public class AddTablesetActivity extends BaseActivity {

	private TextView tv_addtableset;
	private EditText et_tablename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_addtableset, NOTYPE);
		initView();
		setListener();
	}

	private void initView() {
		setPageTitle("添加桌位");
		tv_addtableset = (TextView) findViewById(R.id.tv_addtableset);
		et_tablename = (EditText) findViewById(R.id.et_tablename);
	}

	private void setListener() {
		tv_addtableset.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.tv_addtableset:// 确定添加
			String tablename = et_tablename.getText().toString().trim();
			if (TextUtils.isEmpty(tablename)) {
				ContentUtils.showMsg(AddTablesetActivity.this, "桌位名称不能为空");
				return;
			}
			getAddTable(tablename);
			break;

		}
	}

	private void getAddTable(String tablename) {
		okHttpsImp.getAddTable(new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				Log.i("tag","成功");
				String reString = result.getData();
				System.out.println("reString369-----"+reString);
				if (reString != null) {
					JSONObject jsonObject;
					try {
						setResult(RESULT_OK);
						finish();
						ContentUtils.showMsg(AddTablesetActivity.this, "添加成功");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onResponseFailed(String msg) {
				Log.i("tag","失败"+msg);

			}
		}, userShopInfoBean.getBusinessId(),tablename);
		
		
		
		
		
		
	}
}
