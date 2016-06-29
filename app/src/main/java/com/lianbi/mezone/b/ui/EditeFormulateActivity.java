package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.EditTextUtills;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.MemberDevelopmentBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 編輯制定
 * 
 * @time 上午11:16:08
 * @date 2016-1-18
 * @author hongyu.yang
 * 
 */
public class EditeFormulateActivity extends BaseActivity {
	private EditText edt_formulate_name, edt_formulate_grade,
			edt_formulate_price, edt_formulate_other;
	private ToggleButton tgl_member_formulate;
	private TextView tv_member_formulate;
	private String name, grade, price, other;
	/**
	 * 是否打开开关按钮；
	 */
	private boolean isSelect;
	MemberDevelopmentBean memberDevelopmentBean;
	/**
	 * 是否可编辑
	 */
	boolean isNoEdit;
	String detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edite_formulate, HAVETYPE);
		memberDevelopmentBean = (MemberDevelopmentBean) getIntent()
				.getSerializableExtra("bean");
		isNoEdit = getIntent().getBooleanExtra("isNOEDIT", false);
		initView();
		listen();
		if (memberDevelopmentBean == null) {
			memberDevelopmentBean = new MemberDevelopmentBean();
		}
	}

	private void listen() {
		tgl_member_formulate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean arg1 = ((ToggleButton) arg0).isChecked();
				close_open(arg1);

			}
		});

	}

	/**
	 * 开关控制0未开启 1开启
	 * 
	 * @param arg1
	 *            0/1 true/false
	 */
	protected void close_open(final boolean arg1) {
		int status = arg1 ? 1 : 0;
		Log.e("arg", status + "");
		okHttpsImp.postOpenOrCloseLevel(memberDevelopmentBean.getB_level_id()
				+ "", status + "", new MyResultCallback<String>() {

			@Override
			public void onResponseResult(Result result) {
				ContentUtils.showMsg(EditeFormulateActivity.this, "操作成功");
			}

			@Override
			public void onResponseFailed(String msg) {
				tgl_member_formulate.setChecked(!arg1);
			}
		});

	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("编辑制定");
		edt_formulate_name = (EditText) findViewById(R.id.edt_formulate_name);
		edt_formulate_grade = (EditText) findViewById(R.id.edt_formulate_grade);
		edt_formulate_price = (EditText) findViewById(R.id.edt_formulate_price);
		edt_formulate_other = (EditText) findViewById(R.id.edt_formulate_other);
		tgl_member_formulate = (ToggleButton) findViewById(R.id.tgl_member_formulate);
		if (isNoEdit) {
			edt_formulate_name.setFocusable(false);
			edt_formulate_grade.setFocusable(false);
			edt_formulate_price.setFocusable(false);
		}
		tv_member_formulate = (TextView) findViewById(R.id.tv_member_formulate);
		tv_member_formulate.setOnClickListener(this);
		EditTextUtills.setPricePoint(edt_formulate_price);

		if (memberDevelopmentBean != null) {
			if (isNoEdit) {

			} else {
				edt_formulate_name.setText(memberDevelopmentBean
						.getB_level_name());
				edt_formulate_grade.setText(memberDevelopmentBean
						.getB_discount());
				edt_formulate_price.setText(memberDevelopmentBean.getPrice());
			}
			int isO = memberDevelopmentBean.getStatus();
			if (isO == 0) {
				tgl_member_formulate.setChecked(false);
			} else {
				tgl_member_formulate.setChecked(true);

			}
			edt_formulate_other.setText(memberDevelopmentBean
					.getB_leve_detail());
		}
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.tv_member_formulate:
			saveMemberinfo();
			break;
		}
	}

	/**
	 * 保存会员等级信息
	 */
	private void saveMemberinfo() {
		name = edt_formulate_name.getText().toString().trim();
		grade = edt_formulate_grade.getText().toString().trim();
		price = edt_formulate_price.getText().toString().trim();
		other = edt_formulate_other.getText().toString().trim();
		isSelect = tgl_member_formulate.isChecked();
		if (!isSelect) {
			ContentUtils.showMsg(EditeFormulateActivity.this, "您还没有开启此功能");
			return;
		}
		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(EditeFormulateActivity.this, "会员级别名称不能为空");

			return;
		}
		if (TextUtils.isEmpty(grade)) {
			ContentUtils.showMsg(EditeFormulateActivity.this, "优惠折扣不能为空");
			return;

		}
		if (TextUtils.isEmpty(price)) {
			ContentUtils.showMsg(EditeFormulateActivity.this, "需消费不能为空");

			return;
		}
		if (TextUtils.isEmpty(other)) {
			ContentUtils.showMsg(EditeFormulateActivity.this, "描述不能为空");

			return;
		}
		okHttpsImp.postUpdateAssociatorLevel(
				memberDevelopmentBean.getB_level_id() + "", name, grade, price,
				other, new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						ContentUtils.showMsg(EditeFormulateActivity.this,
								"编辑成功");

						ContentUtils.putSharePre(EditeFormulateActivity.this,
								Constants.SHARED_PREFERENCE_NAME,
								Constants.ISOPENMEMBER, true);
						finish();
					}

					@Override
					public void onResponseFailed(String msg) {
						ContentUtils.showMsg(EditeFormulateActivity.this,
								"编辑失败");

					}
				});
	}

}
