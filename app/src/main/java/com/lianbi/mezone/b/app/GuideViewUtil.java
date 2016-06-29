package com.lianbi.mezone.b.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author ghl
 * @category 设置浮层引导页工具类
 */
public class GuideViewUtil {
	private SharedPreferences mGuideView_SP;
	private Activity mActivity;
	private int mimageViewId;
	private int mimageViewId1;
	private ImageView mimageView;
	private ImageView mimageView1;
	private int num;

	public GuideViewUtil(Activity mActivity, Object... mimageViewId) {
		super();
		this.mActivity = mActivity;
		num = mimageViewId.length;
		switch (num) {
		case 1:
			this.mimageViewId = (Integer) mimageViewId[0];
			break;
		case 2:
			this.mimageViewId = (Integer) mimageViewId[0];
			this.mimageViewId1 = (Integer) mimageViewId[1];
			break;
		}
		mGuideView_SP = mActivity.getSharedPreferences(mActivity.getClass()
				.getName() + "GuideView", Context.MODE_PRIVATE);
		setGuideView();
	}

	/**
	 * @return 返回最顶层视图
	 */
	public View getDeCorView() {
		return (ViewGroup) mActivity.getWindow().getDecorView();
	}

	/**
	 * @return 返回内容区域根视图
	 */
	public View getRootView() {
		return (ViewGroup) mActivity.findViewById(android.R.id.content);
	}

	/**
	 * 设置浮层引导页
	 */
	public void setGuideView() {
		View view = getRootView();
		if (view == null) {
			return;
		}
		String guide_flag = mGuideView_SP.getString("Guide", "hgh");
		if (guide_flag.equals(mActivity.getClass().getName())) {
			return;
		}
		final FrameLayout frameLayout = (FrameLayout) view;
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		switch (num) {
		case 1:
			mimageView = new ImageView(mActivity);
			mimageView.setImageResource(mimageViewId);
			mimageView.setScaleType(ScaleType.FIT_XY);
			mimageView.setLayoutParams(layoutParams);
			mimageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					frameLayout.removeView(mimageView);
					mGuideView_SP.edit()
							.putString("Guide", mActivity.getClass().getName())
							.commit();
				}
			});
			frameLayout.addView(mimageView);
			break;
		case 2:
			mimageView1 = new ImageView(mActivity);
			mimageView1.setImageResource(mimageViewId1);
			mimageView1.setScaleType(ScaleType.FIT_XY);
			mimageView1.setLayoutParams(layoutParams);
			mimageView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					frameLayout.removeView(mimageView1);
					mGuideView_SP.edit()
							.putString("Guide", mActivity.getClass().getName())
							.commit();
				}
			});
			mimageView = new ImageView(mActivity);
			mimageView.setImageResource(mimageViewId);
			mimageView.setScaleType(ScaleType.FIT_XY);
			mimageView.setLayoutParams(layoutParams);
			mimageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					frameLayout.removeView(mimageView);
					frameLayout.addView(mimageView1);
					// mGuideView_SP.edit()
					// .putString("Guide", mActivity.getClass().getName())
					// .commit();
				}
			});
			frameLayout.addView(mimageView);
			break;
		}

	}

	/**
	 * 移除浮层引导页
	 */
	public void CancleGuideView() {
		String guide_flag = mGuideView_SP.getString("Guide", "hgh");
		if (!guide_flag.equals(mActivity.getClass().getName())) {
			return;
		}
		FrameLayout view = (FrameLayout) getRootView();
		if (view == null) {
			return;
		}
		view.removeView(mimageView);

	}
}
