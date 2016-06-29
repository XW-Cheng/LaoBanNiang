package com.lianbi.mezone.b.fragment;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.AbAppUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.JumpIntent;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScreenUtils;
import cn.com.hgh.utils.TelPhoneUtills;
import cn.com.hgh.view.CircularImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lianbi.mezone.b.bean.MessageBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.impl.MyShopChange;
import com.lianbi.mezone.b.ui.AboutUsActivity;
import com.lianbi.mezone.b.ui.ActionProduceActivity;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.LoginActivity;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.MineMsgActivity;
import com.lianbi.mezone.b.ui.MineTextInfoActivity;
import com.lianbi.mezone.b.ui.MyShopActivity;
import com.xizhi.mezone.b.R;

/**
 * 
 * @author guanghui.han
 * @category我的
 */
public class MineFragment extends Fragment implements OnClickListener,
		MyShopChange {
	CircularImageView circularimageview_fm_mine;
	LinearLayout llt_fm_mine_nologin, llt_fm_mine_info, llt_mine_message,
			llt_mine_function, llt_mine_phone_vip, llt_mine_about_us,
			llt_mine_fm_title_bg;
	TextView fm_mine_name, fm_mine_vip, fm_mine_adress, mine_fm_tv_num_message,
			tv_mine_vision_fm, mine_fm_phone_num;
	ImageView fm_mine_textinfo, fm_mine_bussiness, fm_mine_product_f;

	private MainActivity maActivity;
	private final int LOGINACTIVITY_CODE = 2000;
	private final int MINETEXTINFOACTIVITY_CODE = 2002;
	private final int MINEMSGACTIVITY_CODE = 2003;

	private final int MYSUPPLYGOODSACTIVITY_CODE = 2005;
	private SwipeRefreshLayout swipe_mine;

	/**
	 * 刷新fm数据
	 */
	public void refreshFMData() {
		setDataLogin();
	}

	/**
	 * 得到消息列表
	 */
	public void getMessageList() {
		OkHttpsImp okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP
				.newInstance(maActivity);
		okHttpsImp.getMessageList(false,
				BaseActivity.userShopInfoBean.getBusinessId(), 0, 100,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						String mYresult = result.getData();
						org.json.JSONObject jb;
						try {
							jb = new org.json.JSONObject(mYresult);
							String jbRe = jb.getString("message");
							if (!TextUtils.isEmpty(jbRe)) {
								ArrayList<MessageBean> cuArrayList = (ArrayList<MessageBean>) JSONObject
										.parseArray(jbRe, MessageBean.class);
								if (cuArrayList != null) {
									int size = cuArrayList.size();
									if (size == 0) {
										mine_fm_tv_num_message
												.setVisibility(View.GONE);
										maActivity.setRedPoint(View.GONE);
									} else {
										int num = 0;
										for (MessageBean mb : cuArrayList) {
											if (mb.getIs_read() == 0) {
												num++;
											}
										}
										if (num == 0) {
											mine_fm_tv_num_message
													.setVisibility(View.GONE);
											maActivity.setRedPoint(View.GONE);
										} else {

											mine_fm_tv_num_message.setText(num
													+ "");
											mine_fm_tv_num_message
													.setVisibility(View.VISIBLE);
											maActivity
													.setRedPoint(View.VISIBLE);
										}
									}
								}
							}
						} catch (JSONException e) {
							mine_fm_tv_num_message.setVisibility(View.GONE);
							maActivity.setRedPoint(View.GONE);
							e.printStackTrace();
						}

					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		maActivity = (MainActivity) getActivity();
		View view = inflater.inflate(R.layout.fm_minefragment, null);
		initView(view);
		listen();
		return view;
	}

	private void setDataLogin() {
		if (ContentUtils.getLoginStatus(maActivity)) {
			llt_fm_mine_nologin.setVisibility(View.GONE);
			llt_fm_mine_info.setVisibility(View.VISIBLE);
			if (null == BaseActivity.userShopInfoBean.getName()
					|| TextUtils.isEmpty(BaseActivity.userShopInfoBean
							.getName())) {
				fm_mine_name.setText("您的昵称");
			} else {
				fm_mine_name.setText(BaseActivity.userShopInfoBean.getName());
			}
			Glide.with(maActivity)
					.load(BaseActivity.userShopInfoBean.getPersonHeadUrl())
					.error(R.mipmap.defaultpeson)
					.into(circularimageview_fm_mine);
			if (!TextUtils.isEmpty(BaseActivity.userShopInfoBean
					.getBusinessId())) {
//				getMessageList();
			}
		} else {
			circularimageview_fm_mine.setImageResource(R.mipmap.defaultpeson);
			mine_fm_tv_num_message.setVisibility(View.GONE);
			maActivity.setRedPoint(View.GONE);
			llt_fm_mine_nologin.setVisibility(View.VISIBLE);
			llt_fm_mine_info.setVisibility(View.GONE);
		}
	}

	private void listen() {
		llt_fm_mine_info.setOnClickListener(this);
		llt_mine_message.setOnClickListener(this);
		llt_mine_function.setOnClickListener(this);
		llt_mine_phone_vip.setOnClickListener(this);
		llt_mine_about_us.setOnClickListener(this);
		llt_fm_mine_nologin.setOnClickListener(this);
		// fm_mine_product_f.setOnClickListener(this);
		fm_mine_bussiness.setOnClickListener(this);
		fm_mine_textinfo.setOnClickListener(this);
	}

	private void initView(View view) {
		circularimageview_fm_mine = (CircularImageView) view
				.findViewById(R.id.circularimageview_fm_mine);
		llt_fm_mine_nologin = (LinearLayout) view
				.findViewById(R.id.llt_fm_mine_nologin);
		llt_mine_fm_title_bg = (LinearLayout) view
				.findViewById(R.id.llt_mine_fm_title_bg);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				ScreenUtils.getScreenWidth(maActivity) * 2 / 5);
		llt_mine_fm_title_bg.setLayoutParams(params);

		llt_fm_mine_info = (LinearLayout) view
				.findViewById(R.id.llt_fm_mine_info);
		llt_mine_message = (LinearLayout) view
				.findViewById(R.id.llt_mine_message);
		llt_mine_function = (LinearLayout) view
				.findViewById(R.id.llt_mine_function);
		llt_mine_phone_vip = (LinearLayout) view
				.findViewById(R.id.llt_mine_phone_vip);
		llt_mine_about_us = (LinearLayout) view
				.findViewById(R.id.llt_mine_about_us);
		fm_mine_name = (TextView) view.findViewById(R.id.fm_mine_name);
		fm_mine_vip = (TextView) view.findViewById(R.id.fm_mine_vip);
		fm_mine_adress = (TextView) view.findViewById(R.id.fm_mine_adress);
		mine_fm_tv_num_message = (TextView) view
				.findViewById(R.id.mine_fm_tv_num_message);
		tv_mine_vision_fm = (TextView) view
				.findViewById(R.id.tv_mine_vision_fm);
		mine_fm_phone_num = (TextView) view
				.findViewById(R.id.mine_fm_phone_num);
		fm_mine_textinfo = (ImageView) view.findViewById(R.id.fm_mine_textinfo);
		fm_mine_bussiness = (ImageView) view
				.findViewById(R.id.fm_mine_bussiness);
		// fm_mine_product_f = (ImageView) view
		// .findViewById(R.id.fm_mine_product_f);
		String vName = AbAppUtil.getAppVersionName(maActivity);
		tv_mine_vision_fm.setText(vName);
		swipe_mine = (SwipeRefreshLayout) view.findViewById(R.id.swipe_mine);
		swipe_mine.setColorSchemeResources(R.color.colores_news_01,R.color.black);
		swipe_mine.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				swipe_mine.setRefreshing(false);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		boolean isLogin = ContentUtils.getLoginStatus(maActivity);
		boolean re = false;
		switch (arg0.getId()) {
		case R.id.llt_fm_mine_info:// 切换店铺

			break;
		case R.id.llt_mine_message:// 我的消息
			re = JumpIntent.jumpLogin_addShop(isLogin, API.NEWS, maActivity);
			if (re) {
				startActivityForResult(new Intent(maActivity,
						MineMsgActivity.class), MINEMSGACTIVITY_CODE);
			}
			break;
		case R.id.llt_mine_function:// 功能介绍
			startActivity(new Intent(maActivity, ActionProduceActivity.class));

			break;
		case R.id.llt_mine_phone_vip:// 联系客服

			TelPhoneUtills.launchPhone(maActivity, mine_fm_phone_num.getText()
					.toString().trim());
			break;
		case R.id.llt_mine_about_us:// 关于我们
			startActivity(new Intent(maActivity, AboutUsActivity.class));

			break;
		case R.id.llt_fm_mine_nologin:// 注册| 登录
			maActivity.startActivityForResult(new Intent(maActivity,
					LoginActivity.class), MainActivity.REQUEST_CHANKAN);
			break;
		case R.id.fm_mine_textinfo:// 我的资料

			re = JumpIntent.jumpLogin_addShop1(isLogin, API.MATERIAL,
					maActivity);
			if (re) {
				startActivityForResult(new Intent(maActivity,
						MineTextInfoActivity.class), MINETEXTINFOACTIVITY_CODE);

			}
			break;
		case R.id.fm_mine_bussiness:// 我的商铺
			re = JumpIntent.jumpLogin_addShop(isLogin, API.STORE, maActivity);
			if (re) {
				MyShopActivity.setMyShopChange(this);
				maActivity
						.startActivityForResult(new Intent(maActivity,
								MyShopActivity.class),
								MainActivity.MYSHOPACTIVITY_CODE);
			}
			break;
		// case R.id.fm_mine_product_f:// 我的货源
		// re = JumpIntent
		// .jumpLogin_addShop(isLogin, API.MYSOURCE, maActivity);
		// if (re) {
		// startActivityForResult(new Intent(maActivity,
		// MySupplyGoodsActivity.class),
		// MYSUPPLYGOODSACTIVITY_CODE);
		// }
		// break;
		}

	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == maActivity.RESULT_OK) {
			switch (requestCode) {
			// case LOGINACTIVITY_CODE:// 登录回调
			// setDataLogin();
			// break;
			case MINETEXTINFOACTIVITY_CODE:// 我的资料回调
				setDataLogin();
				break;
			case MINEMSGACTIVITY_CODE:// 我的消息回调
				setDataLogin();
				break;
			// case MYSHOPACTIVITY_CODE:// 我的商户回调
			// maActivity.refreshFMData();
			// break;
			case MYSUPPLYGOODSACTIVITY_CODE:// 我的货源回调
				setDataLogin();
				break;

			}
		}
	}

	@Override
	public void reFresh() {
		maActivity.refreshFMData();
	}
}
