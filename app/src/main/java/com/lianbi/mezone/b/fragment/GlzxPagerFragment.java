package com.lianbi.mezone.b.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.JumpIntent;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MoreServerMallBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.ui.ChoiceDingdanInfoWayActivity;
import com.lianbi.mezone.b.ui.DingdanInfoActivity;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.MemberManagementActivity;
import com.lianbi.mezone.b.ui.OrderProductActivity;
import com.lianbi.mezone.b.ui.OrderProductListActivity;
import com.lianbi.mezone.b.ui.ServiceMallActivity;
import com.lianbi.mezone.b.ui.ServiceShopActivity;
import com.lianbi.mezone.b.ui.TransactionManagementActivity;
import com.lianbi.mezone.b.ui.WebActivty;

public class GlzxPagerFragment extends Fragment implements OnClickListener {

	private MainActivity mMainActivity;
	private LinearLayout mGLZXJiaoyiGl1, mGLZXHuiyuanGl2, mGLZXPingjiaGl3,
			mGLZXShebeiGl4;
	private TextView mTv1Top, mTv1Bottom, mTv2Top, mTv2Bottom, mTv3Top,
			mTv3Bottom, mTv4Top, mTv4Bottom;
	ImageView iv1, iv2, iv3, iv4;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();

		View view = inflater.inflate(R.layout.glzx_firstpager, null);

		initView(view);

		setLisenter();

		initData();

		return view;
	}

	/**
	 * 初始化View
	 */
	private void initView(View view) {

		mGLZXJiaoyiGl1 = (LinearLayout) view.findViewById(R.id.GLZX_jiaoyi_gl1);// 订单明细
		mGLZXHuiyuanGl2 = (LinearLayout) view
				.findViewById(R.id.GLZX_huiyuan_gl2);// 会员管理
		mGLZXPingjiaGl3 = (LinearLayout) view
				.findViewById(R.id.GLZX_pingjia_gl3);// 
		mGLZXShebeiGl4 = (LinearLayout) view.findViewById(R.id.GLZX_shebei_gl4);// 添加更多
		mTv1Top = (TextView) view.findViewById(R.id.tv1_top);
		mTv1Bottom = (TextView) view.findViewById(R.id.tv1_bottom);
		mTv2Top = (TextView) view.findViewById(R.id.tv2_top);
		mTv2Bottom = (TextView) view.findViewById(R.id.tv2_bottom);
		mTv3Top = (TextView) view.findViewById(R.id.tv3_top);
		mTv3Bottom = (TextView) view.findViewById(R.id.tv3_bottom);
		mTv4Top = (TextView) view.findViewById(R.id.tv4_top);
		mTv4Bottom = (TextView) view.findViewById(R.id.tv4_bottom);
		iv1 = (ImageView) view.findViewById(R.id.iv_fm_glzx1);
		iv2 = (ImageView) view.findViewById(R.id.iv_fm_glzx2);
		iv3 = (ImageView) view.findViewById(R.id.iv_fm_glzx3);
		iv4 = (ImageView) view.findViewById(R.id.iv_fm_glzx4);

	}

	/**
	 * 
	 * @param arrayList
	 *            数据源
	 * @param isF
	 *            是否第一个
	 */
	public void initDataList(ArrayList<MoreServerMallBean> arrayList,
			boolean isF) {
		this.arrayList.clear();
		this.arrayList.addAll(arrayList);
		this.isF = isF;

	}

	ArrayList<MoreServerMallBean> arrayList = new ArrayList<MoreServerMallBean>();
	boolean isF;
	final String GD = "添加更多";
	public final static int OrderProductActivity_code = 3009;

	public void initData() {
		if (arrayList == null) {
			return;
		}
		if (isF) {
			int s = arrayList.size();
			switch (s) {
			case 1:
				mGLZXShebeiGl4.setVisibility(View.VISIBLE);
				if (arrayList.get(0).isJ()) {
					iv4.setImageResource(R.mipmap.morelandly);
					mTv4Top.setText(GD);
				} else {
					Glide.with(mMainActivity).load(arrayList.get(0).getIcoUrl())
							.error(R.mipmap.defaultimg_11).into(iv4);
					mTv4Top.setText(arrayList.get(0).getAppName());
				}
				break;
			}
		} else {
			int s = arrayList.size();
			switch (s) {
			case 1:
				mGLZXJiaoyiGl1.setVisibility(View.VISIBLE);
				mGLZXHuiyuanGl2.setVisibility(View.INVISIBLE);
				mGLZXPingjiaGl3.setVisibility(View.INVISIBLE);
				if (arrayList.get(0).isJ()) {
					iv1.setImageResource(R.mipmap.morelandly);
					mTv1Top.setText(GD);
				} else {
					Glide.with(mMainActivity).load(arrayList.get(0).getIcoUrl())
							.error(R.mipmap.defaultimg_11).into(iv1);
					mTv1Top.setText(arrayList.get(0).getAppName());
				}
				break;
			case 2:
				mGLZXJiaoyiGl1.setVisibility(View.VISIBLE);
				mGLZXHuiyuanGl2.setVisibility(View.VISIBLE);
				mGLZXPingjiaGl3.setVisibility(View.INVISIBLE);
				Glide.with(mMainActivity).load(arrayList.get(0).getIcoUrl())
						.error(R.mipmap.defaultimg_11).into(iv1);
				mTv1Top.setText(arrayList.get(0).getAppName());

				if (arrayList.get(1).isJ()) {
					iv2.setImageResource(R.mipmap.morelandly);
					mTv2Top.setText(GD);
				} else {
					Glide.with(mMainActivity).load(arrayList.get(1).getIcoUrl())
							.error(R.mipmap.defaultimg_11).into(iv2);
					mTv2Top.setText(arrayList.get(1).getAppName());
				}
				break;
			case 3:
				mGLZXJiaoyiGl1.setVisibility(View.VISIBLE);
				mGLZXHuiyuanGl2.setVisibility(View.VISIBLE);
				mGLZXPingjiaGl3.setVisibility(View.VISIBLE);
				Glide.with(mMainActivity).load(arrayList.get(0).getIcoUrl())
						.error(R.mipmap.defaultimg_11).into(iv1);
				mTv1Top.setText(arrayList.get(0).getAppName());

				Glide.with(mMainActivity).load(arrayList.get(1).getIcoUrl())
						.error(R.mipmap.defaultimg_11).into(iv2);
				mTv2Top.setText(arrayList.get(1).getAppName());

				if (arrayList.get(2).isJ()) {
					iv3.setImageResource(R.mipmap.morelandly);
					mTv3Top.setText(GD);
				} else {
					Glide.with(mMainActivity).load(arrayList.get(2).getIcoUrl())
							.error(R.mipmap.defaultimg_11).into(iv3);
					mTv3Top.setText(arrayList.get(2).getAppName());
				}
				break;
			case 4:
				mGLZXJiaoyiGl1.setVisibility(View.VISIBLE);
				mGLZXHuiyuanGl2.setVisibility(View.VISIBLE);
				mGLZXPingjiaGl3.setVisibility(View.VISIBLE);
				mGLZXShebeiGl4.setVisibility(View.VISIBLE);
				Glide.with(mMainActivity).load(arrayList.get(0).getIcoUrl())
						.error(R.mipmap.defaultimg_11).into(iv1);
				mTv1Top.setText(arrayList.get(0).getAppName());

				Glide.with(mMainActivity).load(arrayList.get(1).getIcoUrl())
						.error(R.mipmap.defaultimg_11).into(iv2);
				mTv2Top.setText(arrayList.get(1).getAppName());

				Glide.with(mMainActivity).load(arrayList.get(2).getIcoUrl())
						.error(R.mipmap.defaultimg_11).into(iv3);
				mTv3Top.setText(arrayList.get(2).getAppName());

				if (arrayList.get(3).isJ()) {
					iv4.setImageResource(R.mipmap.morelandly);
					mTv4Top.setText(GD);
				} else {
					Glide.with(mMainActivity).load(arrayList.get(3).getIcoUrl())
							.error(R.mipmap.defaultimg_11).into(iv4);
					mTv4Top.setText(arrayList.get(3).getAppName());
				}

				break;
			}
		}
	}

	private void setLisenter() {
		mGLZXJiaoyiGl1.setOnClickListener(this);
		mGLZXHuiyuanGl2.setOnClickListener(this);
		mGLZXPingjiaGl3.setOnClickListener(this);
		mGLZXShebeiGl4.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		boolean isLogin = ContentUtils.getLoginStatus(mMainActivity);
		boolean re = false;
		switch (view.getId()) {
		case R.id.GLZX_jiaoyi_gl1:// 交易管理1
			if (isF) {
				re = JumpIntent.jumpLogin_addShop(isLogin, API.TRADE,
						mMainActivity);
				if (re) {
					// Intent intent_more = new Intent(mMainActivity,
					// TransactionManagementActivity.class);
					// startActivity(intent_more);
					Intent intent = new Intent(mMainActivity,
							ChoiceDingdanInfoWayActivity.class);
					startActivity(intent);
				}
			} else {

				choiceJumpe(P0);
			}
			break;
		case R.id.GLZX_huiyuan_gl2:// 会员管理2
			if (isF) {

				re = JumpIntent.jumpLogin_addShop(isLogin, API.VIP,
						mMainActivity);
				if (re) {
					Intent intent_more = new Intent(mMainActivity,
							MemberManagementActivity.class);
					startActivity(intent_more);
				}
			} else {

				choiceJumpe(P1);
			}
			break;
		case R.id.GLZX_pingjia_gl3: {// 订单列表3
			if (isF) {

				re = JumpIntent.jumpLogin_addShop(isLogin, API.ORDER,
						mMainActivity);
				if (re) {
//					 Intent intent_more = new Intent(mMainActivity,
//					 OrderProductListActivity.class);
//					 startActivity(intent_more);
//					mMainActivity.startActivityForResult((new Intent(
//							mMainActivity, OrderProductActivity.class)),
//							OrderProductActivity_code);
					ContentUtils.showMsg(mMainActivity, "正在建设中,敬请期待...");
				}
			} else {

				choiceJumpe(P2);
			}
		}
			break;
		case R.id.GLZX_shebei_gl4: {// 设备管理4
			re = JumpIntent.jumpLogin_addShop1(isLogin, API.SERVER,
					mMainActivity);
			
			if (re) {
				if (isF) {
					choiceJumpe(P0);
				} else {
					choiceJumpe(P3);

				}
			} else {

			}
		}
			break;

		}
	}

	public final int P0 = 0;
	public final int P1 = 1;
	public final int P2 = 2;
	public final int P3 = 3;

	/**
	 * 
	 * @param p
	 *            位置
	 */
	private void choiceJumpe(int p) {
		boolean isJ = arrayList.get(p).isJ();
		switch (p) {
		case P0:
			if (isJ) {
				Intent intent_more = new Intent(mMainActivity,
						ServiceMallActivity.class);
				mMainActivity.startActivityForResult(intent_more,
						mMainActivity.SERVICESHOPACTIVITY_CODE);
			} else {
				String url = arrayList.get(p).getUrl();
				Intent intent = new Intent(mMainActivity, WebActivty.class);
				intent.putExtra(WebActivty.T, arrayList.get(p).getAppName());
				intent.putExtra(WebActivty.U, url);
				intent.putExtra("Re", true);
				startActivity(intent);
			}
			break;
		case P1:
			if (isJ) {
				Intent intent_more = new Intent(mMainActivity,
						ServiceMallActivity.class);
				mMainActivity.startActivityForResult(intent_more,
						mMainActivity.SERVICESHOPACTIVITY_CODE);
			} else {
				String url = arrayList.get(p).getUrl();
				Intent intent = new Intent(mMainActivity, WebActivty.class);
				intent.putExtra(WebActivty.T, arrayList.get(p).getAppName());
				intent.putExtra(WebActivty.U, url);
				intent.putExtra("Re", true);
				startActivity(intent);
			}

			break;
		case P2:
			if (isJ) {
				Intent intent_more = new Intent(mMainActivity,
						ServiceMallActivity.class);
				mMainActivity.startActivityForResult(intent_more,
						mMainActivity.SERVICESHOPACTIVITY_CODE);
			} else {
				String url = arrayList.get(p).getUrl();
				Intent intent = new Intent(mMainActivity, WebActivty.class);
				intent.putExtra(WebActivty.T, arrayList.get(p).getAppName());
				intent.putExtra(WebActivty.U, url);
				intent.putExtra("Re", true);
				startActivity(intent);
			}

			break;
		case P3:
			if (isJ) {
				Intent intent_more = new Intent(mMainActivity,
						ServiceMallActivity.class);
				mMainActivity.startActivityForResult(intent_more,
						mMainActivity.SERVICESHOPACTIVITY_CODE);
			} else {
				String url = arrayList.get(p).getUrl();
				Intent intent = new Intent(mMainActivity, WebActivty.class);
				intent.putExtra(WebActivty.T, arrayList.get(p).getAppName());
				intent.putExtra(WebActivty.U, url);
				intent.putExtra("Re", true);
				startActivity(intent);
			}

			break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == MainActivity.RESULT_OK) {
			if (requestCode == OrderProductActivity_code) {
				mMainActivity.reFShouP();
			}
		}
	}
}
