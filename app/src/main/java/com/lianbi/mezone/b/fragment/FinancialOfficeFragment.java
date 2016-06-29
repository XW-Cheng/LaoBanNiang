package com.lianbi.mezone.b.fragment;

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
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.JumpIntent;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.BankBoundInfo;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.ui.AddBankCardActivity;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.ShouRuHActivity;
import com.lianbi.mezone.b.ui.WithdrawDepositActivity;

/**
 * 
 * @time 上午10:10:47
 * @date 2016-1-12
 * @author hongyu.yang 财务室
 */
public class FinancialOfficeFragment extends Fragment implements
		OnClickListener {

	private MainActivity mMainActivity;
	private OkHttpsImp okHttpsImp;
	private ImageView iv_recharge, iv_withdrawalsdetails, iv_withdrawals,
			iv_bankcard;
	private TextView tv_totalaccount, tv_shopaccount, tv_availablebalance,
			tv_takeinmoney, tv_shopincometoday;

	public double totalaccount = 0, shopaccount = 0, availablebalance = 0,
			takeinmoney = 0, shopincometoday = 0;

	/**
	 * 刷新fm数据
	 */
	public void refreshFMData() {
		if (ContentUtils.getLoginStatus(mMainActivity)) {
			getIsBand();
		} else {
			tv_totalaccount.setText(MathExtend.roundNew(0, 2));
			tv_shopaccount.setText(MathExtend.roundNew(0, 2));
			tv_availablebalance.setText(MathExtend.roundNew(0, 2));
			tv_takeinmoney.setText(MathExtend.roundNew(0, 2));
			tv_shopincometoday.setText(MathExtend.roundNew(0, 2));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fm_caiwushifragment, null);
		mMainActivity = (MainActivity) getActivity();// 获得activity实例
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(mMainActivity);
		initView(view);
		setLisenter();
		return view;
	}

	/**
	 * 查看银行卡信息
	 */
	protected boolean isBand = false;

	private void getIsBand() {

		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getIsTrue(OkHttpsImp.md5_key,
					BaseActivity.userShopInfoBean.getUserId(), "01", uuid,
					"app", reqTime, new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							if (!TextUtils.isEmpty(resString)) {
								BankBoundInfo bankBoundInfos = JSON
										.parseObject(resString,
												BankBoundInfo.class);
								if (null != bankBoundInfos.getBankAccountNo()
										&& !TextUtils.isEmpty(bankBoundInfos
												.getBankAccountNo())) {
									isBand = true;
								} else {
									isBand = false;
								}
							} else {
								isBand = false;
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							isBand = false;
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isReturn = false;
	public boolean isBank = false;
	private SwipeRefreshLayout swipe_caiwushi;
	
	@Override
	public void onResume() {
		super.onResume();
		
		isReturn = ContentUtils.getSharePreBoolean(mMainActivity,
				Constants.SHARED_PREFERENCE_NAME, Constants.SEARCHFINANCIAL);
		isBank =ContentUtils.getSharePreBoolean(mMainActivity,
				Constants.SHARED_PREFERENCE_NAME, Constants.ISBANKRETURN);
		if (ContentUtils.getLoginStatus(mMainActivity)&&isBank){
			isBank = false;
			ContentUtils.putSharePre(mMainActivity,
					Constants.SHARED_PREFERENCE_NAME, Constants.ISBANKRETURN,
					isBank);
			getIsBand();// 查看银行卡信息
		}
		
		if (ContentUtils.getLoginStatus(mMainActivity)&&isReturn) {
			isReturn = false;
			
			ContentUtils.putSharePre(mMainActivity,
					Constants.SHARED_PREFERENCE_NAME, Constants.SEARCHFINANCIAL,
					isReturn);
			
			mMainActivity.getCount();
		}
		
		
		
	}

	/**
	 * 初始化View
	 * 
	 * @param view
	 */
	private void initView(View view) {
		iv_recharge = (ImageView) view.findViewById(R.id.iv_recharge);// 充值
		iv_withdrawalsdetails = (ImageView) view
				.findViewById(R.id.iv_withdrawalsdetails);// 体现明细
		iv_withdrawals = (ImageView) view.findViewById(R.id.iv_withdrawals);// 提现
		iv_bankcard = (ImageView) view.findViewById(R.id.iv_bankcard);// 银行卡
		tv_totalaccount = (TextView) view.findViewById(R.id.tv_totalaccount);// 账户总额
		tv_shopaccount = (TextView) view.findViewById(R.id.tv_shopaccount);// 店铺总额
		tv_availablebalance = (TextView) view
				.findViewById(R.id.tv_availablebalance);// 可用余额
		tv_takeinmoney = (TextView) view.findViewById(R.id.tv_takeinmoney);// 提现中余额
		tv_shopincometoday = (TextView) view
				.findViewById(R.id.tv_shopincometoday);// 店铺今日收入
	   swipe_caiwushi = (SwipeRefreshLayout) view.findViewById(R.id.swipe_caiwushi);
	   swipe_caiwushi.setColorSchemeResources(R.color.colores_news_01,R.color.black);
	   swipe_caiwushi.setOnRefreshListener(new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
//			new Thread( new Runnable() {
				
//				@Override
//				public void run() {
					mMainActivity.getCount();
					swipe_caiwushi.setRefreshing(false);
//				}
//			}).start();
		}
	});
	}

	/**
	 * 设置财务室信息
	 */
	public void setPriceTotal(double money, int positon) {
		switch (positon) {
		case 0:// 账户总额
			this.totalaccount = money;
			tv_totalaccount.setText(MathExtend.roundNew(money, 2));
			break;
		case 1:// 店铺总额
			this.shopaccount = money;
			tv_shopaccount.setText(MathExtend.roundNew(money, 2));
			break;
		case 2:// 可用余额
			this.availablebalance = money;
			tv_availablebalance.setText(MathExtend.roundNew(money, 2));
			break;
		case 3:// 提现中余额
			this.takeinmoney = money;
			tv_takeinmoney.setText(MathExtend.roundNew(money, 2));
			break;
		case 4:// 店铺今日收入
			this.shopincometoday = money;
			tv_shopincometoday.setText(MathExtend.roundNew(money, 2));
			break;

		}
	}

	/**
	 * 设置店铺今日收入
	 */
	public void setShopPriceTotal(double ff) {
		tv_shopaccount.setText(MathExtend.roundNew(ff, 2));
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		iv_recharge.setOnClickListener(this);
		iv_withdrawalsdetails.setOnClickListener(this);
		iv_withdrawals.setOnClickListener(this);
		iv_bankcard.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		boolean isLogin = ContentUtils.getLoginStatus(mMainActivity);
		boolean re = false;
		switch (v.getId()) {
		case R.id.iv_recharge:// 充值
			ContentUtils.showMsg(mMainActivity, "正在建设中...");
			break;

		case R.id.iv_withdrawalsdetails:// 交易明细
			re = JumpIntent.jumpLogin_addShop(isLogin, API.TRDATEDETAIL,
					mMainActivity);
			if (re) {
				if (isBand) {
					Intent intent = new Intent(mMainActivity,
							WithdrawDepositActivity.class);
					intent.putExtra("totalamount", availablebalance);
					startActivity(intent);
				} else {
					ContentUtils.showMsg(mMainActivity, "请您先绑定银行卡!");
				}
			}
			break;
		case R.id.iv_withdrawals:// 提现
			re = JumpIntent.jumpLogin_addShop(isLogin, API.WITHDRAWDEPOSIT,
					mMainActivity);
			if (re) {
				startActivity(new Intent(mMainActivity, ShouRuHActivity.class)
						.putExtra("isBand", isBand));
			}
			break;
		case R.id.iv_bankcard:// 银行卡
			re = JumpIntent.jumpLogin_addShop(isLogin, API.BANKCARD,
					mMainActivity);
			if (re) {
				startActivity(new Intent(mMainActivity,
						AddBankCardActivity.class).putExtra("isBand", isBand));
			}
			break;

		}
	}

}
