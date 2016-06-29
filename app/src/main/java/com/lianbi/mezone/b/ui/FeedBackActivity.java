package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 意见反馈
 * 
 * @author qiuyu.lv
 * @date 2016-1-13
 * @version
 */
public class FeedBackActivity extends BaseActivity {
	EditText opinion_title, opinion_content;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_feedback, NOTYPE);
		initView();
		setLisenter();
	}

	private void initView() {
		setPageTitle("意见反馈");
		opinion_title = (EditText) findViewById(R.id.opinion_title);
		opinion_content = (EditText) findViewById(R.id.opinion_content);
		submit = (Button) findViewById(R.id.submit);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		submit.setOnClickListener(this);
		opinion_title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 20) {
					ContentUtils.showMsg(FeedBackActivity.this, "标题最多20个字");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		opinion_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 500) {
					ContentUtils.showMsg(FeedBackActivity.this, "反馈内容最多500个字");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		if (view == submit) {
			String title = opinion_title.getText().toString();
			String content = opinion_content.getText().toString();
			if (!TextUtils.isEmpty(title.trim())
					&& !TextUtils.isEmpty(content.trim())) {
//				okHttpsImp.postAddFeedBack(content, title, userShopInfoBean.getUserId(),
//						new MyResultCallback<String>() {
//
//							@Override
//							public void onResponseResult(Result result) {
//								ContentUtils.showMsg(FeedBackActivity.this,
//										"反馈成功！");
//								finish();
//							}
//
//							@Override
//							public void onResponseFailed(String msg) {
//								ContentUtils.showMsg(FeedBackActivity.this,
//										"反馈失败！");
//
//							}
//						});
				ContentUtils.showMsg(FeedBackActivity.this,
				"反馈成功!");
				opinion_title.setText("");
				opinion_content.setText("");
			} else {
				if (TextUtils.isEmpty(title.trim())) {
					ContentUtils.showMsg(FeedBackActivity.this, "请输入标题");
				} else {
					ContentUtils.showMsg(this, "请输入反馈内容");
				}
			}
		}
	}
}
