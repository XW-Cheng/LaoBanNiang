package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.hgh.indexscortlist.ClearEditText;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.ScrollerUtills;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.ActivityManager;
import com.lianbi.mezone.b.bean.UserShopInfoBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;

public class BaseActivity extends FragmentActivity implements OnClickListener {
	private LinearLayout lltTitle;

	public ImageView ivBack;

	private TextView tvTitle;

	public ImageView ivTitleRight, ivTitleRight1;

	public TextView tvTitleRight,tv_title_left;

	public TextView tvTitleRightSecond;

	public ClearEditText cet_title_center;
	public ScrollView slltContainer;
	/**
	 * 网络请求实现类
	 */
	public OkHttpsImp okHttpsImp;

	/**
	 * 网络连接状态
	 */
	public boolean ISCONNECTED;
	/**
	 * Activity堆栈管理工具
	 */
	protected ActivityManager activityManager = ActivityManager.getInstance();
	/**
	 * 屏幕宽度
	 */
	public int screenWidth = 0;
	/**
	 * 屏幕高度
	 */
	public int screenHeight = 0;

	public FragmentManager fm;
	public static UserShopInfoBean userShopInfoBean;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		fm = getSupportFragmentManager();
		getWindowWH();

		ISCONNECTED = AbAppUtil.isNetworkAvailable(this);
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(this);

		MyResultCallback.isShow = true;
		if (!ISCONNECTED) {
			ContentUtils.showMsg(this, "当前网络连接不可用");
		}
		activityManager.putActivity(this);
		if (userShopInfoBean == null) {
			userShopInfoBean = new UserShopInfoBean(this);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(this);
	}

	/**
	 * 获取屏幕的高和宽
	 */
	private void getWindowWH() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	public final int NOTYPE = 0;
	public final int HAVETYPE = 1;

	/**
	 * 设置布局
	 * 
	 * @param layoutResID
	 *            布局ID
	 * @param type
	 *            0：无任何效果用于复杂布局，1：滚动布局.
	 */
	public void setContentView(int layoutResID, int type) {
		super.setContentView(R.layout.act_base_layout);

		lltTitle = (LinearLayout) findViewById(R.id.base_layout_title);
		cet_title_center = (ClearEditText) findViewById(R.id.cet_title_center);
		ivBack = (ImageView) findViewById(R.id.iv_title_left);
		tvTitle = (TextView) findViewById(R.id.tv_title_center);
		ivTitleRight = (ImageView) findViewById(R.id.iv_title_right);
		ivTitleRight1 = (ImageView) findViewById(R.id.iv_title_right1);
		tvTitleRight = (TextView) findViewById(R.id.tv_title_right);
		tv_title_left = (TextView) findViewById(R.id.tv_title_left);
		ivBack.setOnClickListener(this);
		tvTitle.setOnClickListener(this);
		ivTitleRight.setOnClickListener(this);
		ivTitleRight1.setOnClickListener(this);
		tvTitleRight.setOnClickListener(this);
		View view = View.inflate(this, layoutResID, null);
		switch (type) {
		case NOTYPE:
			LinearLayout llt_container = (LinearLayout) findViewById(R.id.llt_container);
			llt_container.setVisibility(View.VISIBLE);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			llt_container.addView(view, lp);
			break;
		case HAVETYPE:
			slltContainer = (ScrollView) findViewById(R.id.sllt_container);
			slltContainer.setVisibility(View.VISIBLE);
			LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			slltContainer.addView(view, lps);
			break;
		}
	}

	/**
	 * 滚动（父视图为HAVETYPE时）到顶部
	 */
	public void sUp() {
		if (slltContainer != null) {
			ScrollerUtills.scrollerup(slltContainer);
		}
	}

	/**
	 * 滚动（父视图为HAVETYPE时）到底部
	 */
	public void sDown() {
		if (slltContainer != null) {
			ScrollerUtills.scrollerdown(slltContainer);
		}
	}

	/**
	 * 设置PageTitle 的可见性
	 * 
	 * @param visibility
	 */
	public void setPageTitleVisibility(int visibility) {
		lltTitle.setVisibility(visibility);
	}

	/**
	 * 设置背景颜色/圖片
	 * 
	 * @param color
	 *            r.color.cccc/r.drawle.ddddd
	 */
	public void setPageTitleBackgroundColor(int color) {
		lltTitle.setBackgroundResource(color);
	}

	/**
	 * 设置title
	 * 
	 * @param title
	 */
	public void setPageTitle(String title) {
		tvTitle.setText(title);
	}

	/**
	 * 设置title tv可见性
	 * 
	 * @param v
	 */
	public void setPageTitleV(int v) {
		tvTitle.setVisibility(v);
	}

	/**
	 * 设置title et可见性
	 * 
	 * @param v
	 */
	public void setPageTitleVET(int v) {
		cet_title_center.setVisibility(v);
	}

	/**
	 * 设置title et输入类型
	 * 
	 * @param type
	 */
	public void setPageTitleVETYPE(int type) {
		cet_title_center.setInputType(type);
	}

	/**
	 * 设置title et输入长度
	 * 
	 * @param type
	 */
	public void setPageTitleEtLenth(int type) {
		cet_title_center.setMaxlenth(type);
	}

	/**
	 * 设置title的颜色
	 * 
	 * @param color
	 *            r.color.ccccc
	 */
	public void setPageTitleColor(int color) {
		tvTitle.setTextColor(getResources().getColor(color));
	}

	/**
	 * 设置title
	 * 
	 * @param resid
	 *            r.string.sssss
	 */
	public void setPageTitle(int resid) {
		tvTitle.setText(resid);
	}

	/**
	 * 设置返回键的可见性
	 * 
	 * @param visibility
	 */
	public void setPageBackVisibility(int visibility) {
		ivBack.setVisibility(visibility);
	}

	/**
	 * 设置返回键的图片
	 * 
	 * @param resid
	 */
	public void setPageBackResource(int resid) {
		ivBack.setImageResource(resid);
		ivBack.setVisibility(View.VISIBLE);
	}

	/**
	 * 設置右邊圖片
	 * 
	 * @param resid
	 */
	public void setPageRightResource(int resid) {
		ivTitleRight.setVisibility(View.VISIBLE);
		ivTitleRight1.setVisibility(View.GONE);
		ivTitleRight.setImageResource(resid);
	}

	/**
	 * 設置右邊圖片1
	 * 
	 * @param resid
	 */
	public void setPageRightResource1(int resid) {
		ivTitleRight1.setVisibility(View.VISIBLE);
		ivTitleRight1.setImageResource(resid);
	}

	/**
	 * 設置右邊文字
	 * 
	 * @param resid
	 */
	public void setPageRightText(int resid) {
		ivTitleRight.setVisibility(View.GONE);
		ivTitleRight1.setVisibility(View.GONE);
		tvTitleRight.setVisibility(View.VISIBLE);
		tvTitleRight.setText(resid);
	}

	/**
	 * 設置右邊文字顏色
	 * 
	 * @param color
	 */
	public void setPageRightTextColor(int color) {
		ivTitleRight.setVisibility(View.GONE);
		ivTitleRight1.setVisibility(View.GONE);
		tvTitleRight.setVisibility(View.VISIBLE);
		tvTitleRight.setTextColor(getResources().getColor(color));
	}

	/**
	 * 設置右邊文字大小
	 * 
	 * @param size
	 */
	public void setPageRightTextSize(float size) {
		ivTitleRight.setVisibility(View.GONE);
		ivTitleRight1.setVisibility(View.GONE);
		tvTitleRight.setVisibility(View.VISIBLE);
		tvTitleRight.setTextSize(size);
	}
	
	/**
	 * 設置右邊文字
	 * 
	 * @param txt
	 */
	public void setPageRightText(String txt) {
		ivTitleRight.setVisibility(View.GONE);
		ivTitleRight1.setVisibility(View.GONE);
		tvTitleRight.setVisibility(View.VISIBLE);
		tvTitleRight.setText(txt);
	}

	/**
	 * 设置右边图片隐藏
	 */
	public void setPageRightImageVisibility() {
		ivTitleRight.setVisibility(View.INVISIBLE);
	}

	/**
	 * 设置右边图片1隐藏
	 */
	public void setPageRightImage1Visibility() {
		ivTitleRight1.setVisibility(View.GONE);
	}

	/**
	 * 设置右边文本可见性
	 * 
	 * @param visibility
	 */
	public void setPageRightTextVisibility(int visibility) {
		tvTitleRight.setVisibility(visibility);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityManager.removeActivity(this);
	}

	/**
	 * 结束应用所有activity
	 * 
	 */
	public void exit() {
		activityManager.exit();
	}

	/**
	 * 结束当前应用除自己以外的所有activity
	 * 
	 */
	public void exitWithOutMe() {
		activityManager.exitWithOutActivity(this);
	}

	@Override
	public void onBackPressed() {
		onTitleLeftClick();
	}

	/**
	 * 返回键点击事件
	 * 
	 */
	protected void onTitleLeftClick() {
		finish();
	}

	/**
	 * 标题文字点击事件
	 * 
	 */
	protected void onTitleTextClick() {
	}

	/**
	 * 标题右侧按钮点击事件1
	 * 
	 */
	protected void onTitleRightClick1() {

	}

	/**
	 * 标题右侧按钮点击事件2
	 * 
	 */
	protected void onTitleRightClick2() {

	}

	/**
	 * 标题右侧按钮点击事件3
	 * 
	 */
	protected void onTitleRightClickTv() {

	}

	@Override
	public void onClick(View view) {
		AbAppUtil.closeSoftInput(this);
		switch (view.getId()) {
		case R.id.iv_title_left:
			onTitleLeftClick();
			return;
		case R.id.tv_title_center:
			onTitleTextClick();
			return;
		case R.id.iv_title_right:
			onTitleRightClick1();
			return;
		case R.id.iv_title_right1:
			onTitleRightClick2();
			return;
		case R.id.tv_title_right:
			onTitleRightClickTv();
			return;
		}
		ISCONNECTED = AbAppUtil.isNetworkAvailable(this);
		if (!ISCONNECTED) {
			ContentUtils.showMsg(this, "当前网络连接不可用");
			return;
		}
		onChildClick(view);
		// boolean isLogin = ContentUtils.getLoginStatus(this);
		// if (isLogin) {
		// // 已登录
		// onLoginChildClick(view);
		// } else {
		// // 到登陆页
		// }
	}

	/**
	 * 子类点击事件onClick
	 * 
	 * @param view
	 */
	protected void onChildClick(View view) {

	}

	// /**
	// * 子类需要登录点击事件onClick
	// *
	// * @param view
	// */
	// protected void onLoginChildClick(View view) {
	//
	// }

}
