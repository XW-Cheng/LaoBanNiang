package com.lianbi.mezone.b.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.REGX;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 商铺 -- 联系人电话
 * 
 * @time 下午7:05:55
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
@SuppressLint("ResourceAsColor")
public class ShopConnectPhoneActivity extends BaseActivity {
	private EditText edt_shop_connect_phone;
	private ImageView iv_shop_connect_phone_delete;
	int key;
	String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_connect_phone, NOTYPE);
		phone = getIntent().getStringExtra("phone");
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("联系电话");
		setPageRightText("保存");
		setPageRightTextColor(R.color.colores_news_01);
		edt_shop_connect_phone = (EditText) findViewById(R.id.edt_shop_connect_phone);
		edt_shop_connect_phone.setText(phone);
		iv_shop_connect_phone_delete = (ImageView) findViewById(R.id.iv_shop_connect_phone_delete);
		if (key == 2) {
			edt_shop_connect_phone
					.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		}
		setLisenter();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		iv_shop_connect_phone_delete.setOnClickListener(this);
		edt_shop_connect_phone.addTextChangedListener(new TextWatcher() {

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
					iv_shop_connect_phone_delete.setVisibility(View.VISIBLE);
				} else {
					iv_shop_connect_phone_delete.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.iv_shop_connect_phone_delete:
			edt_shop_connect_phone.setText("");
			break;
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		final String content = edt_shop_connect_phone.getText().toString()
				.trim();
		if (!TextUtils.isEmpty(content)) {
			if (!content.matches(REGX.REGX_MOBILE)) {
				ContentUtils.showMsg(ShopConnectPhoneActivity.this,
						"请输入正确的联系电话");
				return;
			}
		} else {
			ContentUtils.showMsg(ShopConnectPhoneActivity.this, "请输入联系电话");
			return;
		}
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.updateBusinessPhone(uuid,"app",reqTime,userShopInfoBean.getBusinessId(),
					content, new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(ShopConnectPhoneActivity.this,
									"修改联系电话成功");
							Intent intent = new Intent();
							intent.putExtra("phone", content);
							setResult(RESULT_OK, intent);
							finish();
						}

						@Override
						public void onResponseFailed(String msg) {

							ContentUtils.showMsg(ShopConnectPhoneActivity.this,
									"修改联系电话失败");
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
