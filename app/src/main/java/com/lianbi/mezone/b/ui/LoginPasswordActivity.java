package com.lianbi.mezone.b.ui;

import org.json.JSONException;

import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.REGX;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSONObject;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录密码页面
 * 
 * @author qiuyu.lv
 * @date 2016-1-13
 * @version
 */
@SuppressLint("ResourceAsColor")
public class LoginPasswordActivity extends BaseActivity {
	EditText oldpwd_et, newpwd_et, confirmpwd_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_loginpwd, HAVETYPE);
		initView();
	}

	private void initView() {
		setPageTitle("修改登录密码");
		setPageRightText("保存");
		setPageRightTextColor(R.color.colores_news_01);
		oldpwd_et = (EditText) findViewById(R.id.oldpwd_et);
		newpwd_et = (EditText) findViewById(R.id.newpwd_et);
		confirmpwd_et = (EditText) findViewById(R.id.confirmpwd_et);

	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		String oldPwd = oldpwd_et.getText().toString().trim();
		final String newPwd = newpwd_et.getText().toString().trim();
		String confirmPwd = confirmpwd_et.getText().toString().trim();
		String password = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.PASS_WORD);
		if (TextUtils.isEmpty(oldPwd)) {
			ContentUtils.showMsg(this, "请输入原密码");
			return;
		}
		if (!oldPwd.equals(password)) {
			ContentUtils.showMsg(this, "原密码输入错误");
			return;
		}
		if (TextUtils.isEmpty(newPwd)) {
			ContentUtils.showMsg(this, "请输入新密码");
			return;
		}
		if (TextUtils.isEmpty(confirmPwd)) {
			ContentUtils.showMsg(this, "请输入确认密码");
			return;
		}
		if (newPwd.equals(password)) {
			ContentUtils.showMsg(this, "新密码不能与原密码相同");
			return;
		}
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		if (newPwd.equals(confirmPwd)) {
			if (newpwd_et.getText().length() < 6) {
				ContentUtils.showMsg(LoginPasswordActivity.this, "新密码的格式不正确");
			} else {
				try {
					okHttpsImp.upDatePassword(userShopInfoBean.getUserId(), oldPwd,
							confirmPwd,uuid,"app",reqTime,new MyResultCallback<String>() {

								@Override
								public void onResponseResult(Result result) {
									String resString = result.getData();
									try {
//										org.json.JSONObject jsonObject = new org.json.JSONObject(resString);
//										org.json.JSONObject jsonIstrue = new org.json.JSONObject(jsonObject.getString("isTrue"));
//										String isTrue = jsonIstrue.getString("isTrue");
										if(resString!=null){
											ContentUtils.showMsg(
													LoginPasswordActivity.this, "修改成功");
										}
										
										ContentUtils.putSharePre(
												LoginPasswordActivity.this,
												Constants.SHARED_PREFERENCE_NAME,
												Constants.PASS_WORD, newPwd);
										finish();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								@Override
								public void onResponseFailed(String msg) {
									ContentUtils.showMsg(
											LoginPasswordActivity.this, "修改失败");
								}
							});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			ContentUtils.showMsg(this, "新密码和确认密码不一致");
		}
		
	}
}
