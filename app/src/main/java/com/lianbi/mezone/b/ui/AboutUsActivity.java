package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.widget.ImageView;

import cn.com.hgh.utils.AbViewUtil;

import com.xizhi.mezone.b.R;

/**
 * 关于我们
 * 
 * @time 下午3:21:34
 * @date 2016-1-20
 * 
 * @author hongyu.yang
 * 
 */
public class AboutUsActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us, NOTYPE);
		setPageTitle("关于我们");
		ImageView iv = (ImageView) findViewById(R.id.about_us_iv);
		iv.setImageBitmap(AbViewUtil.readBitMap(this, R.mipmap.about_us));
	}
}
