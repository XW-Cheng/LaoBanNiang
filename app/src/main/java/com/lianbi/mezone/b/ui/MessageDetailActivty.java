package com.lianbi.mezone.b.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MessageBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 消息详情
 * 
 * @author guanghui.han
 * 
 */

@SuppressLint("ResourceAsColor")
public class MessageDetailActivty extends BaseActivity {
	MessageBean messageBean;
	TextView act_messagedetailactivty_tv_lx, act_messagedetailactivty_tv_title,
			act_messagedetailactivty_tv_time,
			act_messagedetailactivty_tv_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_messagedetailactivty, HAVETYPE);
		messageBean = (MessageBean) getIntent().getSerializableExtra(
				"MessageBean");
		setResult(RESULT_OK);
		initView();
		upView();
	}

	private void upView() {
		if (messageBean != null) {
			switch (messageBean.getMessage_type()) {
			case 0:
				act_messagedetailactivty_tv_lx.setText("系统");
				break;
			case 1:
				act_messagedetailactivty_tv_lx.setText("订单");
				break;
			case 2:
				act_messagedetailactivty_tv_lx.setText("定制");
				break;
			}

			act_messagedetailactivty_tv_title.setText(messageBean
					.getMessage_title());
			act_messagedetailactivty_tv_time.setText(messageBean
					.getMessageCreateTime());
			act_messagedetailactivty_tv_content.setText(messageBean
					.getMessage_content());
		}
	}

	/**
	 * 初始化视图控件
	 */
	void initView() {
		setPageTitle("我的消息");
		setPageRightText("删除");
		setPageRightTextColor(R.color.colores_news_01);
		act_messagedetailactivty_tv_lx = (TextView) findViewById(R.id.act_messagedetailactivty_tv_lx);
		act_messagedetailactivty_tv_title = (TextView) findViewById(R.id.act_messagedetailactivty_tv_title);
		act_messagedetailactivty_tv_time = (TextView) findViewById(R.id.act_messagedetailactivty_tv_time);
		act_messagedetailactivty_tv_content = (TextView) findViewById(R.id.act_messagedetailactivty_tv_content);
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		okHttpsImp.postDelMessage(messageBean.getMessage_id() + "",
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						setResult(RESULT_OK);
						finish();
					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});
	}

}
