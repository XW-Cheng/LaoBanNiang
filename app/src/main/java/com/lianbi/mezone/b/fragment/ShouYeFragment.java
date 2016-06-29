package com.lianbi.mezone.b.fragment;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.ShouYeBannerBean;
import com.lianbi.mezone.b.bean.ShouyeServiceBean;
import com.lianbi.mezone.b.bean.WebProductManagementBean;
import com.lianbi.mezone.b.httpresponse.API;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.httpresponse.OkHttpsImp;
import com.lianbi.mezone.b.impl.MyShopChange;
import com.lianbi.mezone.b.ui.BaseActivity;
import com.lianbi.mezone.b.ui.H5WebActivty;
import com.lianbi.mezone.b.ui.MainActivity;
import com.lianbi.mezone.b.ui.ServiceMallActivity;
import com.lianbi.mezone.b.ui.TableSetActivity;
import com.lianbi.mezone.b.ui.WebActivty;
import com.xizhi.mezone.b.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.playview.imp.TextSliderView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.CryptTool;
import cn.com.hgh.utils.JumpIntent;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScreenUtils;
import cn.com.hgh.utils.WebEncryptionUtil;
import cn.com.hgh.view.ErWeMaDialog;
import cn.com.hgh.view.MyGridView;

/**
 * @author guanghui.han
 */
public class ShouYeFragment extends Fragment implements OnSliderClickListener,
		MyShopChange {

	private SliderLayout mDemoSlider;
	LinearLayout ad_llt;
	MainActivity mActivity;
	private OkHttpsImp okHttpsImp;
	private MyGridView gv_shouyeservice;
	private ImageView iv_shouye_datu;
	private SwipeRefreshLayout swipe_shouye;
	/**
	 * 广告
	 */
	private ArrayList<ShouYeBannerBean> ades_ImageEs = new ArrayList<ShouYeBannerBean>();
	/**
	 * 以下载数据
	 */
	ArrayList<ShouyeServiceBean> mData = new ArrayList<ShouyeServiceBean>();
	/**
	 * 是否广告请求成功
	 */
	private boolean isAdSucceedRequest = false;

	/**
	 * 刷新fm数据
	 */
	public void refreshFMData() {
		if (ContentUtils.getLoginStatus(mActivity)) {
			// mDatas.clear();
			mActivity.getServiceMall();
			// getServiceMallAll();

		} else {
			// mDatas.clear();
			mActivity.getServiceMall();

		}
		if (!isAdSucceedRequest) {
			getAadver();
		}
	}

	ProgressBar ad_siderlayout_progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fm_shouyefragment, null);
		mActivity = (MainActivity) getActivity();
		initView(view);
		okHttpsImp = OkHttpsImp.SINGLEOKHTTPSIMP.newInstance(mActivity);
		initListAdapter();
		listen();
		// getServiceMallAll();
		getAadver();
		return view;
	}

	private QuickAdapter<ShouyeServiceBean> mAdapter;

	public void initListAdapter() {
		mAdapter = new QuickAdapter<ShouyeServiceBean>(mActivity,
				R.layout.grid_item, mData) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
								   final ShouyeServiceBean item) {
				TextView tv_store_service_introduce = helper
						.getView(R.id.tv_store_service_introduce);
				final ImageView iv_store_service = helper
						.getView(R.id.iv_store_service);
				int serviceid = item.getDefaultservice();
				switch (serviceid) {
					case -1:
						Glide.with(mActivity).load("").error(null)
								.into(iv_store_service);
						// Glide.with(mActivity).load(R.drawable.icon_blank)
						// .error(R.drawable.default_head)
						// .into(iv_store_service);
						// iv_store_service.setBackgroundResource(R.drawable.icon_blank);
						break;
					case 1:
						Glide.with(mActivity).load(R.mipmap.icon_servicemall)
								.error(R.mipmap.default_head)
								.into(iv_store_service);
						break;
					case 2:
						Glide.with(mActivity).load(R.mipmap.icon_receivables)
								.error(R.mipmap.default_head)
								.into(iv_store_service);
						break;
					default:
						if (null != item.getIcoUrl()) {
							Glide.with(mActivity).load(item.getIcoUrl())
									.error(R.mipmap.default_head)
									.into(iv_store_service);
						}
						break;
				}

				tv_store_service_introduce.setText(item.getAppName());
			}
		};
		gv_shouyeservice.setAdapter(mAdapter);
	}

	private void initView(View view) {

		ad_siderlayout_progressBar = (ProgressBar) view
				.findViewById(R.id.adeslltview_siderlayout_progressBar);
		gv_shouyeservice = (MyGridView) view
				.findViewById(R.id.gv_shouyeservice);
		iv_shouye_datu = (ImageView) view.findViewById(R.id.iv_shouye_datu);
		ad_siderlayout_progressBar.setVisibility(View.GONE);
		intAdView(view);
		initViewTitle(view);
		swipe_shouye = (SwipeRefreshLayout) view.findViewById(R.id.swipe_shouye);
		swipe_shouye.setColorSchemeResources(R.color.colores_news_01, R.color.black);
		swipe_shouye.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getAadver();
				swipe_shouye.setRefreshing(false);
			}
		});
	}

	/**
	 * 裁剪view
	 */
	private void initViewTitle(View view) {
		// LinearLayout.LayoutParams layoutParamsTitle =
		// (android.widget.LinearLayout.LayoutParams) iv_banner
		// .getLayoutParams();
		// layoutParamsTitle.width = ScreenUtils.getScreenWidth(mActivity);
		// layoutParamsTitle.height = ScreenUtils.getScreenWidth(mActivity) / 4;

	}

	private void listen() {
		gv_shouyeservice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				int primaryID = 0;
				try {
					primaryID = mData.get(position).getId();
				} catch (Exception e) {
					ContentUtils.showMsg(mActivity, "数据异常,请退出重新登陆");
				}
				boolean isLogin = ContentUtils.getLoginStatus(mActivity);
				boolean re = false;
				switch (primaryID) {
					case 1:
						re = JumpIntent.jumpLogin_addShop(isLogin, API.SWEEP,
								mActivity);
						if (re) {// 到店服务
							startActivity(new Intent(mActivity,
									TableSetActivity.class));
						}
						break;
					case 2:
						if (isLogin) {// 微信商城
							Intent intent_web = new Intent(mActivity,
									H5WebActivty.class);
							intent_web.putExtra(Constants.NEDDLOGIN, false);
							intent_web.putExtra("NEEDNOTTITLE", false);
							intent_web.putExtra("Re", true);
							intent_web.putExtra(WebActivty.T, "微信商城");
							intent_web.putExtra(WebActivty.U, getUrl(API.TOSTORE_PRODUCT_MANAGEMENT));
							mActivity.startActivity(intent_web);
						}
						break;
					case 3:
						if (isLogin) {//货源批发
							Intent intent_web = new Intent(mActivity,
									H5WebActivty.class);
							intent_web.putExtra(Constants.NEDDLOGIN, false);
							intent_web.putExtra("NEEDNOTTITLE", false);
							intent_web.putExtra("Re", true);
							intent_web.putExtra(WebActivty.T, "货源批发");
							intent_web.putExtra(WebActivty.U, getSupplyWholesaleUrl(API.TOSTORE_Supply_Wholesale));
							mActivity.startActivity(intent_web);
						}
						break;
					case 99:
						re = JumpIntent.jumpLogin_addShop(isLogin, API.SWEEP,
								mActivity);
						if (re) {
							MagnifyImg();// 收款二维码放大
						}
						break;
					case 100:
						re = JumpIntent.jumpLogin_addShop(isLogin,
								API.SERVICESTORE, mActivity);
						if (re) {// 服务商城

							Intent intent_more = new Intent(mActivity,
									ServiceMallActivity.class);
							mActivity.startActivityForResult(intent_more,
									mActivity.SERVICEMALLSHOP_CODE);

						}
						break;
				}
			}
		});
	}

	public String getSupplyWholesaleUrl(String address) {
		String bussniessId = BaseActivity.userShopInfoBean.getBusinessId();
		return address + "storeId=" + bussniessId;
	}


	public String getUrl(String address) {
		String bussniessId = BaseActivity.userShopInfoBean.getBusinessId();
		String url = address;
		WebProductManagementBean data = new WebProductManagementBean();
		data.setBusinessId(bussniessId);
		String dataJson = com.alibaba.fastjson.JSONObject.toJSON(data)
				.toString();
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
			return url + "sing=" + SING + "&&data=" + desStr + "&&auth=wcm";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取广告
	 */
	public void getAadver() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		String reqTime1 = AbDateUtil.getDateTimeNow();
		String uuid1 = AbStrUtil.getUUID();

		/**
		 * 轮播图
		 */
		try {
			okHttpsImp.getAdvert("F1", new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					mDemoSlider.removeAllSliders();
					String resString = result.getData();
					try {
						JSONObject jsonObject = new JSONObject(resString);
						resString = jsonObject.getString("list");

						ades_ImageEs = (ArrayList<ShouYeBannerBean>) JSON
								.parseArray(resString,
										ShouYeBannerBean.class);
						isAdSucceedRequest = true;

						if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
							for (int i = 0; i < ades_ImageEs.size(); i++) {
								TextSliderView textSliderView = new TextSliderView(
										mActivity, i);
								textSliderView
										.image(ades_ImageEs.get(i).getImageUrl())
										.error(R.mipmap.adshouye);
								textSliderView
										.setOnSliderClickListener(ShouYeFragment.this);
								mDemoSlider.addSlider(textSliderView);
							}
						} else {
							for (int i = 0; i < 3; i++) {
								TextSliderView textSliderView = new TextSliderView(
										mActivity, i);
								textSliderView.image(R.mipmap.adshouye);
								textSliderView
										.setOnSliderClickListener(ShouYeFragment.this);
								mDemoSlider.addSlider(textSliderView);
							}
						}
						mDemoSlider
								.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
						ad_siderlayout_progressBar.setVisibility(View.GONE);
						mDemoSlider.setVisibility(View.VISIBLE);


					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void onResponseFailed(String msg) {
					mDemoSlider.removeAllSliders();
					for (int i = 0; i < 3; i++) {
						TextSliderView textSliderView = new TextSliderView(
								mActivity, i);
						textSliderView.image(R.mipmap.adshouye);
						textSliderView
								.setOnSliderClickListener(ShouYeFragment.this);
						mDemoSlider.addSlider(textSliderView);
					}
					mDemoSlider
							.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
					ad_siderlayout_progressBar.setVisibility(View.GONE);
					mDemoSlider.setVisibility(View.VISIBLE);

				}
			}, uuid, "app", reqTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * 大图片
		 */
		try {
			okHttpsImp.getAdvert("F2", new MyResultCallback<String>() {

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
								System.out.println("true");
								Glide.with(mActivity).load(mDatas.get(0).getImageUrl()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_shouye_datu);
							} else {
								Glide.with(mActivity)
										.load(mDatas.get(0).getImageUrl())
										.error(R.mipmap.fm_shouye_in)
										.into(iv_shouye_datu);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onResponseFailed(String msg) {
					Glide.with(mActivity).load(R.mipmap.fm_shouye_in)
							.into(iv_shouye_datu);
				}
			}, uuid1, "app", reqTime1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 广告轮播图
	 *
	 * @param view
	 */
	private void intAdView(View view) {
		mDemoSlider = (SliderLayout) view
				.findViewById(R.id.adeslltview_siderlayout);
		ad_llt = (LinearLayout) view.findViewById(R.id.adeslltview_llt);
		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				ScreenUtils.getScreenWidth(mActivity) / 4);
		ad_llt.setLayoutParams(params);

	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
			String url = ades_ImageEs.get(slider.getP()).getBannerUrl();
			Intent intent = new Intent(mActivity, WebActivty.class);
			//			intent.putExtra(WebActivty.T, ades_ImageEs.get(slider.getP())
			//					.getBannerTitle());
			intent.putExtra(WebActivty.U, url);
			intent.putExtra("Re", true);
			startActivity(intent);
		}
	}

	/**
	 * 点击二维码放大
	 */
	private void MagnifyImg() {

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
		Bitmap bitmap = ContentUtils.createQrBitmap(payQR, true, 1000, 1000);
		ErWeMaDialog dialog = new ErWeMaDialog(getActivity());
		dialog.setBitmap(bitmap);
		dialog.show();
	}

	@Override
	public void reFresh() {
		mActivity.refreshFMData();
	}

	public void getServiceMall(ArrayList<ShouyeServiceBean> arraylist) {
		if (arraylist != null && arraylist.size() > 0) {
			mData = arraylist;
			mAdapter.replaceAll(mData);
		}
	}

}
