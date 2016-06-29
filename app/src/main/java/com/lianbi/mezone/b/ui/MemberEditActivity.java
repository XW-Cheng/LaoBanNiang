package com.lianbi.mezone.b.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hgh.indexscortlist.ClearEditText;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.AssociatorListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 会员编辑
 * 
 * @author qiuyu.lv
 * @date 2016-1-13
 * @version
 */
@SuppressLint("ResourceAsColor")
public class MemberEditActivity extends BaseActivity {
	Button submit;
	ClearEditText cet_member_act_phone;
	TextView tv_member_act_vipnum, tv_member_act_nickname,
			tv_member_act_vipnum_j;
	AssociatorListBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_membereditactivity, NOTYPE);
		bean = (AssociatorListBean) getIntent().getSerializableExtra("bean");
		initView();
		initW();
	}

	/**
	 * 刷新界面
	 */
	private void initW() {
		if (bean != null) {
			cet_member_act_phone.setText(bean.getAssociator_phone());
			tv_member_act_vipnum.setText(bean.getAssociator_id() + "");
			tv_member_act_nickname.setText(bean.getAssociator_name());
			tv_member_act_vipnum_j.setText(bean.getAssociator_level() + "");
		}
	}

	/**
	 * 删除
	 */
	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		if (bean != null) {
			okHttpsImp.delAssociator(new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					ContentUtils.showMsg(MemberEditActivity.this, "删除会员成功");
					setResult(RESULT_OK);
					finish();
				}

				@Override
				public void onResponseFailed(String msg) {
					ContentUtils.showMsg(MemberEditActivity.this, "删除会员失败");

				}
			}, bean.getAssociator_id() + "");
		}
	}

	private void initView() {
		setPageTitle("会员编辑");
		setPageRightText("删除");
		setPageRightTextColor(R.color.colores_news_01);
		submit = (Button) findViewById(R.id.submit_member);
		cet_member_act_phone = (ClearEditText) findViewById(R.id.cet_member_act_phone);
		tv_member_act_vipnum = (TextView) findViewById(R.id.tv_member_act_vipnum);
		tv_member_act_nickname = (TextView) findViewById(R.id.tv_member_act_nickname);
		tv_member_act_vipnum_j = (TextView) findViewById(R.id.tv_member_act_vipnum_j);
		submit.setOnClickListener(this);

	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		if (view == submit) {
			String phone = cet_member_act_phone.getText().toString();
			if (!TextUtils.isEmpty(phone)) {
				okHttpsImp.updateAssociatorr(bean.getAssociator_id() + "",
						phone, new MyResultCallback<String>() {

							@Override
							public void onResponseResult(Result result) {
								ContentUtils.showMsg(MemberEditActivity.this,
										"会员修改成功");
								setResult(RESULT_OK);
								finish();
							}

							@Override
							public void onResponseFailed(String msg) {
								ContentUtils.showMsg(MemberEditActivity.this,
										"会员修改失败");

							}
						});
			} else {
				ContentUtils.showMsg(this,
						getResources().getString(R.string.input));
			}
		}
	}

}
