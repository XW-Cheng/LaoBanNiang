package cn.com.hgh.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.fragment.JiaoYiGuanLiFragment;
import com.lianbi.mezone.b.ui.AddShopActivity;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.ChangeShopActivity;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.WebActivty;

public class JumpIntent {
	/**
	 * 跳转介绍2次
	 * 
	 * @param isLogin
	 *            是否登录
	 * @param type
	 *            类型
	 * @param activity
	 *            class 跳转的类
	 * @param at
	 */
	public static boolean jumpLogin_addShop(boolean isLogin, String type,
			Activity at) {
		if (isLogin) {
			if (TextUtils
					.isEmpty(BaseActivity.userShopInfoBean.getBusinessId())) {
//				ChangeShopActivity.myShopChange = JiaoYiGuanLiFragment.jiaoYiGuanLiFragment;
				Intent intent_more = new Intent(at, AddShopActivity.class);
				at.startActivity(intent_more);
			} else {
				return isLogin;
			}
		} else {
			Intent intent_web = new Intent(at, WebActivty.class);
			intent_web.putExtra(Constants.NEDDLOGIN, true);
			intent_web.putExtra(WebActivty.T, "功能介绍");
			intent_web.putExtra(WebActivty.U, type);
			at.startActivityForResult(intent_web, MainActivity.REQUEST_CHANKAN);
		}
		return false;
	}

	/**
	 * 跳转介绍1次
	 * 
	 * @param isLogin
	 *            是否登录
	 * @param type
	 *            类型
	 * @param activity
	 *            class 跳转的类
	 * @param at
	 */
	public static boolean jumpLogin_addShop1(boolean isLogin, String type,
			Activity at) {
		if (isLogin) {
			return isLogin;
		} else {
			Intent intent_web = new Intent(at, WebActivty.class);
			intent_web.putExtra(Constants.NEDDLOGIN, true);
			intent_web.putExtra(WebActivty.T, "功能介绍");
			intent_web.putExtra(WebActivty.U, type);
			at.startActivityForResult(intent_web, MainActivity.REQUEST_CHANKAN);
		}
		return false;
	}
}
