package com.lianbi.mezone.b.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.LoginBackBean;
import com.lianbi.mezone.b.bean.MyShopInfoBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 启动页
 * 
 * @author guanghui.han
 * 
 */
public class FirstActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_first);
		ImageView act_first_iv = (ImageView) findViewById(R.id.act_first_iv);

		act_first_iv.setImageBitmap(AbViewUtil.readBitMap(this,
				R.mipmap.first));

		/**
		 * 如果登录过自动登录
		 */
		boolean isLogin = ContentUtils.getLoginStatus(this);
		if (isLogin) {
			autoLogin();
		}
		/**
		 * 是否第一次
		 */
		final boolean isNoFirstUse = ContentUtils.getSharePreBoolean(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.IS_FIRST);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isNoFirstUse) {
					ContentUtils.putSharePre(FirstActivity.this,
							Constants.SHARED_PREFERENCE_NAME,
							Constants.IS_FIRST, true);
					startActivity(new Intent(FirstActivity.this,
							GuiderActivity.class));
				} else {
					Intent intent = new Intent();
					intent.setClass(FirstActivity.this, MainActivity.class);
					startActivity(intent);
				}
				finish();
			}
		}, 3000);
	}

	/**
	 * 自动登录
	 */
	private void autoLogin() {
		String username = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.USER_NAME);
		String password = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.PASS_WORD);
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return;
		}
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.postUserLogin(false, new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					String reString = result.getData();
					ContentUtils.putSharePre(FirstActivity.this,
							Constants.SHARED_PREFERENCE_NAME, Constants.LOGINED_IN,
							true);
					try {
						JSONObject jsonObject = new JSONObject(reString);
						String user = (String) jsonObject.getString("userModel");
						LoginBackBean backBean = JSON.parseObject(user,
								LoginBackBean.class);
						String businessInfo = (String) jsonObject
								.getString("businessModel");
						MyShopInfoBean myShopInfoBean = JSON.parseObject(
								businessInfo, MyShopInfoBean.class);
						if (myShopInfoBean != null) {
							userShopInfoBean.setAddress(myShopInfoBean.getAddress());
							userShopInfoBean.setShopName(myShopInfoBean
									.getBusinessName());
							userShopInfoBean.setIndustry_id(myShopInfoBean
									.getIndustryId());
							userShopInfoBean.setNikeName(myShopInfoBean
									.getContactName());
							userShopInfoBean.setPhone(myShopInfoBean.getMobile());
						}
						if (backBean != null) {
							userShopInfoBean.setUserId(backBean.getUserId());
							userShopInfoBean.setBusinessId(backBean
									.getDefaultBusiness());
							userShopInfoBean.setName(backBean.getUsername());
							userShopInfoBean.setPersonHeadUrl(backBean
									.getUserImage());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onResponseFailed(String msg) {
					ContentUtils.putSharePre(FirstActivity.this,
							Constants.SHARED_PREFERENCE_NAME, Constants.LOGINED_IN,
							false);
				}
			},uuid,"app",reqTime,  username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
