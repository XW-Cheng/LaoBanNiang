package com.lianbi.mezone.b.fragment;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.playview.imp.TextSliderView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.CryptTool;
import cn.com.hgh.utils.JumpIntent;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScreenUtils;
import cn.com.hgh.utils.WebEncryptionUtil;
import cn.com.hgh.view.ErWeMaDialog;
import cn.com.hgh.view.GifView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.ShouYeBannerBean;
import com.lianbi.mezone.b.bean.WebProductManagementBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.ui.AddShopActivity;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.ChoiceDingdanInfoWayActivity;
import com.lianbi.mezone.b.ui.H5WebActivty;
import com.lianbi.mezone.b.ui.InfoDetailsActivity;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.WebActivty;
import com.xizhi.mezone.b.R;

/**
 * 
 * @author guanghui.han
 * @category管理中心
 */
public class JiaoYiGuanLiFragment extends Fragment implements
		OnSliderClickListener, OnClickListener {
	private OkHttpsImp httpsImp;
	private SliderLayout adeslltview_siderlayout;
	private MainActivity mMainActivity;
	private ImageView GLZX_iv_cpgl, GLZX_iv_dygl, iv_jygl_memberma,
			iv_jygl_baristasma, iv_jygl_infoma, iv_jygl_more,
			iv_jiaoyiguanli_datu;
	public static JiaoYiGuanLiFragment jiaoYiGuanLiFragment;
	private LinearLayout GLZX_chanpin_dianyuan_gl, adeslltview_llt;
	/**
	 * 广告
	 */
	private ArrayList<ShouYeBannerBean> ades_ImageEs = new ArrayList<ShouYeBannerBean>();
	ProgressBar adeslltview_siderlayout_progressBar;
	/**
	 * 是否广告请求成功
	 */
	private boolean isAdSucceedRequest = false;

	/**
	 * 刷新fm数据
	 */
	public void refreshFMDataService() {
		// getMoreServerMall();
	}

	/**
	 * 刷新fm数据
	 */
	public void refreshFMData() {
		if (ContentUtils.getLoginStatus(mMainActivity)) {
			if (BaseActivity.userShopInfoBean != null
					&& !TextUtils.isEmpty(BaseActivity.userShopInfoBean
							.getBusinessId())) {

			} else {
			}
		} else {
		}
		if (!isAdSucceedRequest) {
			getAadver();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fm_jiaoyiguanlifragment, null);
		jiaoYiGuanLiFragment = this;
		mMainActivity = (MainActivity) getActivity();
		initView(view);

		httpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(mMainActivity);
		setLisenter();
		return view;
	}

	/**
	 * 获取广告
	 */
	public void getAadver() {
		// adeslltview_siderlayout.removeAllSliders();
		// isAdSucceedRequest = true;
		// for (int i = 0; i < 3; i++) {
		// GuiderSliderView guiderSliderView = new GuiderSliderView(
		// mMainActivity, i);
		// guiderSliderView.setOnSliderClickListener(this);
		// guiderSliderView.setmRes(images[i]);
		// adeslltview_siderlayout.addSlider(guiderSliderView);
		// }
		// adeslltview_siderlayout
		// .setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
		// adeslltview_siderlayout.setVisibility(View.VISIBLE);

		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		String reqTime1 = AbDateUtil.getDateTimeNow();
		String uuid1 = AbStrUtil.getUUID();

		/**
		 * 轮播图
		 */
		try {
			httpsImp.getAdvert("M1", new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					adeslltview_siderlayout.removeAllSliders();
					String resString = result.getData();
					try {
						JSONObject jsonObject = new JSONObject(resString);
						resString = jsonObject.getString("list");

						ades_ImageEs = (ArrayList<ShouYeBannerBean>) JSON
								.parseArray(resString, ShouYeBannerBean.class);
						isAdSucceedRequest = true;

						if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
							for (int i = 0; i < ades_ImageEs.size(); i++) {
								TextSliderView textSliderView = new TextSliderView(
										mMainActivity, i);
								textSliderView.image(
										ades_ImageEs.get(i).getImageUrl())
										.error(R.mipmap.adshouye);
								textSliderView
										.setOnSliderClickListener(JiaoYiGuanLiFragment.this);
								adeslltview_siderlayout
										.addSlider(textSliderView);
							}
						} else {
							for (int i = 0; i < 3; i++) {
								TextSliderView textSliderView = new TextSliderView(
										mMainActivity, i);
								textSliderView.image(R.mipmap.adshouye);
								textSliderView
										.setOnSliderClickListener(JiaoYiGuanLiFragment.this);
								adeslltview_siderlayout
										.addSlider(textSliderView);
							}
						}
						adeslltview_siderlayout
								.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
						adeslltview_siderlayout_progressBar
								.setVisibility(View.GONE);
						adeslltview_siderlayout.setVisibility(View.VISIBLE);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onResponseFailed(String msg) {
					adeslltview_siderlayout.removeAllSliders();
					for (int i = 0; i < 3; i++) {
						TextSliderView textSliderView = new TextSliderView(
								mMainActivity, i);
						textSliderView.image(R.mipmap.adshouye);
						textSliderView
								.setOnSliderClickListener(JiaoYiGuanLiFragment.this);
						adeslltview_siderlayout.addSlider(textSliderView);
					}
					adeslltview_siderlayout_progressBar
							.setVisibility(View.GONE);
					adeslltview_siderlayout.setVisibility(View.VISIBLE);
					adeslltview_siderlayout
							.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);

				}
			}, uuid, "app", reqTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * gif
		 */
		try {
			httpsImp.getAdvert("M2", new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					String reString = result.getData();
					if (!TextUtils.isEmpty(reString)) {
						try {
							JSONObject jsonObject = new JSONObject(reString);
							reString = jsonObject.getString("list");

							ArrayList<ShouYeBannerBean> mDatas = (ArrayList<ShouYeBannerBean>) JSON
									.parseArray(reString,
											ShouYeBannerBean.class);

							if (mDatas.get(0).getImageUrl().endsWith("gif")) {
								Glide.with(mMainActivity).load(mDatas.get(0).getImageUrl()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_jiaoyiguanli_datu);
							} else {
								Glide.with(mMainActivity)
										.load(mDatas.get(0).getImageUrl())
										.error(R.mipmap.fm_shouye_in)
										.into(iv_jiaoyiguanli_datu);
							}


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onResponseFailed(String msg) {
					Glide.with(mMainActivity).load(R.mipmap.fm_jiaoyiguanli_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_jiaoyiguanli_datu);

				}
			}, uuid1, "app", reqTime1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static InputStream is;

	public static InputStream getHttpInputStream(final String url) {
		try {
			new Thread() {
				@Override
				public void run() {
					try {
						URL myFileURL = new URL(url);
						// 获得连接
						HttpURLConnection conn = (HttpURLConnection) myFileURL
								.openConnection();
						conn.setConnectTimeout(6000);
						conn.setDoInput(true);
						// 不使用缓
						conn.setUseCaches(false);
						is = conn.getInputStream();
						is.close();
						// 把网络访问的代码放在这里
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}.start();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private SwipeRefreshLayout swipe_jiaoyiguanli;

	/**
	 * 初始化试图
	 */
	private void initView(View view) {

		adeslltview_siderlayout = (SliderLayout) view
				.findViewById(R.id.adeslltview_siderlayout);// 广告条
		adeslltview_llt = (LinearLayout) view
				.findViewById(R.id.adeslltview_llt);// 广告条
		iv_jiaoyiguanli_datu = (ImageView) view
				.findViewById(R.id.iv_jiaoyiguanli_datu);
		GLZX_iv_cpgl = (ImageView) view.findViewById(R.id.GLZX_iv_cpgl);// 产品管理
		GLZX_iv_dygl = (ImageView) view.findViewById(R.id.GLZX_iv_dygl);// 店员管理
		iv_jygl_memberma = (ImageView) view.findViewById(R.id.iv_jygl_memberma);// 会员管理
		iv_jygl_baristasma = (ImageView) view
				.findViewById(R.id.iv_jygl_baristasma);// 店员管理
		iv_jygl_infoma = (ImageView) view.findViewById(R.id.iv_jygl_infoma);// 信息管理
		iv_jygl_more = (ImageView) view.findViewById(R.id.iv_jygl_more);// 更多
		GLZX_chanpin_dianyuan_gl = (LinearLayout) view
				.findViewById(R.id.GLZX_chanpin_dianyuan_gl);
		adeslltview_siderlayout_progressBar = (ProgressBar) view
				.findViewById(R.id.adeslltview_siderlayout_progressBar);
		initViewTitle(view);// 裁剪view
		initSliderView();// 广告条
		swipe_jiaoyiguanli = (SwipeRefreshLayout) view.findViewById(R.id.swipe_jiaoyiguanli);
		swipe_jiaoyiguanli.setColorSchemeResources(R.color.colores_news_01,R.color.black);
		swipe_jiaoyiguanli.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getAadver();
				swipe_jiaoyiguanli.setRefreshing(false);
			}
		});
	}

	/**
	 * 裁剪view
	 */
	private void initViewTitle(View view) {

		// 产品管理店员管理view
		LinearLayout.LayoutParams layoutParamsChanpinDianyuan = (android.widget.LinearLayout.LayoutParams) GLZX_chanpin_dianyuan_gl
				.getLayoutParams();
		layoutParamsChanpinDianyuan.width = ScreenUtils
				.getScreenWidth(mMainActivity);
		layoutParamsChanpinDianyuan.height = (int) (ScreenUtils
				.getScreenWidth(mMainActivity) / 3 - AbViewUtil.dip2px(
				mMainActivity, 20));

	}

	@Override
	public void onClick(View view) {
		boolean isLogin = ContentUtils.getLoginStatus(mMainActivity);
		boolean re = false;
		switch (view.getId()) {
		case R.id.GLZX_iv_cpgl:// 订单管理
			re = JumpIntent.jumpLogin_addShop(isLogin, API.VIP, mMainActivity);
			
			if (re) {
				startActivity(new Intent(mMainActivity,
						ChoiceDingdanInfoWayActivity.class));
			}
			break;
		case R.id.GLZX_iv_dygl:// 产品管理
			
			re = JumpIntent
					.jumpLogin_addShop(isLogin, API.TRADE, mMainActivity);
			if (re) {
				Intent intent_web = new Intent(mMainActivity,
						H5WebActivty.class);
				intent_web.putExtra(Constants.NEDDLOGIN, false);
				intent_web.putExtra("NEEDNOTTITLE", false);
				intent_web.putExtra("Re", true);
				intent_web.putExtra(WebActivty.T, "产品管理");
				intent_web.putExtra(WebActivty.U, getUrl());
				mMainActivity.startActivity(intent_web);
			}
			break;
		case R.id.iv_jygl_memberma:// 会员管理
			ContentUtils.showMsg(mMainActivity, "正在建设中");
			break;
		case R.id.iv_jygl_baristasma:// 店员管理
			ContentUtils.showMsg(mMainActivity, "正在建设中");
			break;
		case R.id.iv_jygl_infoma:// 信息管理
			re = JumpIntent.jumpLogin_addShop(isLogin, API.INFODETAILS,
					mMainActivity);

			if (re) {
				startActivity(new Intent(mMainActivity,
						InfoDetailsActivity.class));
			}
			break;
		case R.id.iv_jygl_more:// 更多
			break;

		}
	}

	// http://localhost:8080/lincomb-wcm-web/product
	// (com.alibaba.fastjson.JSONObject)
	// /enterIntoProductManager?businessId=BD2016050445678945621245

	public String getUrl() {
		String url = API.TOSTORE_PRODUCT_MANAGEMENT;
		String bussniessId = BaseActivity.userShopInfoBean.getBusinessId();
		WebProductManagementBean data = new WebProductManagementBean();
		data.setBusinessId(bussniessId);
		// String dataJson = JSONObject.fromObject(data).toString();
		String dataJson = com.alibaba.fastjson.JSONObject.toJSON(data)
				.toString();
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.

		url = encryptionUrl(url, dataJson);
		return url;
	}

	/**
	 * 加密
	 */
	private String encryptionUrl(String url, String dataJson) {
		try {
			// 获得的明文数据
			String desStr = dataJson;
			// 转成字节数组
			byte src_byte[] = desStr.getBytes();

			// MD5摘要
			byte[] md5Str = WebEncryptionUtil.md5Digest(src_byte);
			// 生成最后的SIGN
			String SING = WebEncryptionUtil.byteArrayToHexString(md5Str);

			desStr = CryptTool.getBASE64(dataJson);
			// http://localhost:8080/order/orderContler/?sing=key&data=密文
			return url + "sing=" + SING + "&&data=" + desStr + "&&auth=all";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 点击二维码放大
	 */
	private void MagnifyImg() {
		if (!TextUtils.isEmpty(BaseActivity.userShopInfoBean.getBusinessId())) {

			String shopname = BaseActivity.userShopInfoBean.getShopName();
			String businessId = BaseActivity.userShopInfoBean.getBusinessId();

			try {
				shopname = URLEncoder.encode(
						BaseActivity.userShopInfoBean.getShopName(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String payQR = API.PAYQR + businessId;

			Bitmap bitmap = ContentUtils
					.createQrBitmap(payQR, true, 1000, 1000);
			ErWeMaDialog dialog = new ErWeMaDialog(getActivity());
			dialog.setBitmap(bitmap);
			dialog.show();
		} else {
			Intent intent_more = new Intent(mMainActivity,
					AddShopActivity.class);
			mMainActivity.startActivity(intent_more);
		}

	}

	/**
	 * 初始化广告条
	 */
	private void initSliderView() {
		adeslltview_siderlayout
				.setPresetTransformer(SliderLayout.Transformer.Default);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				ScreenUtils.getScreenWidth(mMainActivity) / 4);
		adeslltview_llt.setLayoutParams(params);
	}

	private void setLisenter() {
		GLZX_iv_dygl.setOnClickListener(this);
		GLZX_iv_cpgl.setOnClickListener(this);
		iv_jygl_memberma.setOnClickListener(this);
		iv_jygl_baristasma.setOnClickListener(this);
		iv_jygl_infoma.setOnClickListener(this);
		iv_jygl_more.setOnClickListener(this);
	}

	/**
	 * 广告条监听
	 */
	@Override
	public void onSliderClick(BaseSliderView slider) {
		if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
			String url = ades_ImageEs.get(slider.getP()).getBannerUrl();
			Intent intent = new Intent(mMainActivity, WebActivty.class);
//			intent.putExtra(WebActivty.T, ades_ImageEs.get(slider.getP())
//					.getBannerTitle());
			intent.putExtra(WebActivty.U, url);
			intent.putExtra("Re", true);
			startActivity(intent);
		}
	}

}
