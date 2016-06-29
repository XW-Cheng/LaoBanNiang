package com.lianbi.mezone.b.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 商铺 -- 联系人
 * 
 * @time 下午6:21:28
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
@SuppressLint("ResourceAsColor")
public class ShopConnectActivity extends BaseActivity {
	private EditText edt_shop_connect;
	private ImageView iv_shop_connect_delete;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_connect, NOTYPE);
		name = getIntent().getStringExtra("name");
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("联系人");
		setPageRightText("保存");
		setPageRightTextColor(R.color.colores_news_01);
		edt_shop_connect = (EditText) findViewById(R.id.edt_shop_connect);
		edt_shop_connect.setText(name);
		iv_shop_connect_delete = (ImageView) findViewById(R.id.iv_shop_connect_delete);
		setLisenter();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		iv_shop_connect_delete.setOnClickListener(this);
		edt_shop_connect.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					iv_shop_connect_delete.setVisibility(View.VISIBLE);
				} else {
					iv_shop_connect_delete.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.iv_shop_connect_delete:
			edt_shop_connect.setText("");
			break;
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		final String contactName = edt_shop_connect.getText().toString().trim();
		if (TextUtils.isEmpty(contactName)) {
			return;
		}
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.updateBusinessContacts(uuid, "app", reqTime,
					userShopInfoBean.getBusinessId(), contactName,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(ShopConnectActivity.this,
									"修改成功");
							Intent intent = new Intent();
							intent.putExtra("name", contactName);
							setResult(RESULT_OK, intent);
							finish();
						}

						@Override
						public void onResponseFailed(String msg) {
							ContentUtils.showMsg(ShopConnectActivity.this,
									"修改失败");

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
