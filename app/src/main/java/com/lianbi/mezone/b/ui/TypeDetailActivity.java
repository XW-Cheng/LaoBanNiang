package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.xizhi.mezone.b.R;

/**
 * 类型详情
 * 
 * @time 下午2:24:11
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
public class TypeDetailActivity extends BaseActivity {
	private TextView tv_typedetail_name, tv_typedetail_content;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_type_detail, NOTYPE);
		title = getIntent().getStringExtra("title");
		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle(title);
		tv_typedetail_name = (TextView) findViewById(R.id.tv_typedetail_name);
		tv_typedetail_content = (TextView) findViewById(R.id.tv_typedetail_content);
	}

}
