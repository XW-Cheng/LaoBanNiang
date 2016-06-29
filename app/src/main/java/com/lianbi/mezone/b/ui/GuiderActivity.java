package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.playview.imp.GuiderSliderView;

import com.xizhi.mezone.b.R;

/**
 * 初次使用导航页面
 * 
 * @author guanghui.han
 * 
 */

public class GuiderActivity extends BaseActivity implements
		OnSliderClickListener {

	SliderLayout pager_act_guider;
	final int[] imges = { R.mipmap.guider1,
			R.mipmap.guider3, R.mipmap.guider2 };
	TextView act_guideractivity_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_guideractivity);
		pager_act_guider = (SliderLayout) findViewById(R.id.pager_act_guider);
		act_guideractivity_tv = (TextView) findViewById(R.id.act_guideractivity_tv);
		act_guideractivity_tv.setOnClickListener(this);
		for (int i = 0; i < 3; i++) {
			GuiderSliderView guiderSliderView = new GuiderSliderView(this, i);
			guiderSliderView.setOnSliderClickListener(this);
			guiderSliderView.setmRes(imges[i]);
			pager_act_guider.addSlider(guiderSliderView);
		}
		pager_act_guider
		.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		startMain();
	}

	/**
	 * 进入主页
	 */
	private void startMain() {
		Intent intent = new Intent(GuiderActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		if (slider.getmRes() == imges[2]) {
			startMain();
		}
	}

}
