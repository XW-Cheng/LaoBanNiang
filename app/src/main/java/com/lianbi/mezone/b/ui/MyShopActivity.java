package com.lianbi.mezone.b.ui;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.FuzzyUtil;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.ErWeMaDialog;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MyShopInfoBean;
import com.lianbi.mezone.b.bean.ShopIntroduceImageBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.impl.MyShopChange;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

/**
 * 我的商铺
 * 
 * @time 上午9:45:07
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
@SuppressLint("ResourceAsColor")
public class MyShopActivity extends BaseActivity {
	private final int REQUEST_CONNECT = 4526;
	private final int REQUEST_PHONE = 1652;
	private final int REQUEST_ADDRESS = 4523;
	private final int REQUEST_INTRODUCE = 13212;
	private ImageView img_my_shop_mohu, img_my_shop_logo, img_my_shop_qrcode;
	private TextView tv_my_shop_name, tv_my_shop_address, tv_my_shop_type,
			tv_my_shop_connect_name, tv_my_shop_connect_phone,
			tv_my_shop_connect_address;
	private LinearLayout llt_my_shop_introduce, llt_my_shop_connect,
			llt_my_shop_phone, llt_my_shop_address, myShop_logo;
	private String address;
	private MyPhotoUtills photoUtills;
	private ArrayList<ShopIntroduceImageBean> images = new ArrayList<ShopIntroduceImageBean>();
	private MyShopInfoBean myShopInfoBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_shop, HAVETYPE);
		setResult(RESULT_OK);
		photoUtills = new MyPhotoUtills(this);
		initView();
		initLayoutP();
		setLisenter();
		getBusinessInfo();
	}

	public static MyShopChange myShopChange;

	public static MyShopChange getMyShopChange() {
		return myShopChange;
	}

	public static void setMyShopChange(MyShopChange myShopChange) {
		MyShopActivity.myShopChange = myShopChange;
	}

	@Override
	protected void onTitleLeftClick() {
		setResult(RESULT_OK);
		if (myShopChange != null) {
			myShopChange.reFresh();
		}
		super.onTitleLeftClick();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setResult(RESULT_OK);
		getBusinessInfo();
	}

	/**
	 * 获取店铺详细信息
	 */
	private void getBusinessInfo() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getBusinessInfo(true, userShopInfoBean.getUserId(),
					uuid, "app", reqTime, userShopInfoBean.getBusinessId(),
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							if (!TextUtils.isEmpty(resString)) {
								myShopInfoBean = JSON.parseObject(resString,
										MyShopInfoBean.class);
								if (myShopInfoBean != null) {
									if (!TextUtils.isEmpty(myShopInfoBean
											.getBusinessId())) {
										userShopInfoBean
												.setBusinessId(myShopInfoBean
														.getBusinessId());
										userShopInfoBean
												.setIndustry_id(myShopInfoBean
														.getIndustryId());
										userShopInfoBean
												.setAddress(myShopInfoBean
														.getAddress());
										userShopInfoBean
												.setNikeName(myShopInfoBean
														.getContactName());
										userShopInfoBean
												.setPhone(myShopInfoBean
														.getMobile());
										userShopInfoBean
												.setShopName(myShopInfoBean
														.getBusinessName());
										initWidget(myShopInfoBean);
									}
								}
							}
						}

						@Override
						public void onResponseFailed(String msg) {
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 给控件填值
	 * 
	 * @param infoBean
	 */
	protected void initWidget(MyShopInfoBean infoBean) {
		tv_my_shop_name.setText(infoBean.getBusinessName());
		tv_my_shop_address.setText(infoBean.getAddress());
		tv_my_shop_connect_name.setText(infoBean.getContactName());
		tv_my_shop_connect_phone.setText(infoBean.getMobile());
		tv_my_shop_connect_address.setText(infoBean.getAddress());
//		String majorName = infoBean.getMajorName();
//		if (!TextUtils.isEmpty(majorName)) {
//			tv_my_shop_type.setVisibility(View.VISIBLE);
//			tv_my_shop_type.setText(infoBean.getMajorName());
//		} else {
//			tv_my_shop_type.setVisibility(View.GONE);
//		}
		images = infoBean.getIntroduceImgUrl();

		if (images != null && images.size() > 0) {
			String imageUrlBa = images.get(0).getImageUrl();
			Glide.with(MyShopActivity.this)
					.load(imageUrlBa)
					.asBitmap()
					.error(R.mipmap.adshouye)
					.into(new SimpleTarget<Bitmap>(screenWidth, screenWidth / 4) {

						@Override
						public void onResourceReady(Bitmap bitmap,
								GlideAnimation<? super Bitmap> arg1) {
//							bitmap = FuzzyUtil.fastblur(MyShopActivity.this,
//									bitmap, 50);
//							img_my_shop_mohu.setImageBitmap(bitmap);
						}
					});
		} else {
			img_my_shop_mohu.setImageBitmap(null);
		}
		// String logoImage = infoBean.getBusinessImage();
		String logoImage = infoBean.getLogoUrl();
		if (!TextUtils.isEmpty(logoImage)) {
			Glide.with(this).load(logoImage).error(R.mipmap.defaultimg_11)
					.into(img_my_shop_logo);
		} else {
			Glide.with(this).load(R.mipmap.defaultimg_11)
					.into(img_my_shop_logo);
		}
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("我的商铺");
		setPageRightText("切换");
		setPageRightTextColor(R.color.colores_news_01);
		img_my_shop_mohu = (ImageView) findViewById(R.id.img_my_shop_mohu);
		img_my_shop_logo = (ImageView) findViewById(R.id.img_my_shop_logo);
		img_my_shop_qrcode = (ImageView) findViewById(R.id.img_my_shop_qrcode);
		tv_my_shop_name = (TextView) findViewById(R.id.tv_my_shop_name_1);
		tv_my_shop_address = (TextView) findViewById(R.id.tv_my_shop_address_1);
		tv_my_shop_type = (TextView) findViewById(R.id.tv_my_shop_type1);
		tv_my_shop_connect_name = (TextView) findViewById(R.id.tv_my_shop_connect_name);
		tv_my_shop_connect_phone = (TextView) findViewById(R.id.tv_my_shop_connect_phone);
		tv_my_shop_connect_address = (TextView) findViewById(R.id.tv_my_shop_connect_address);

		llt_my_shop_address = (LinearLayout) findViewById(R.id.llt_my_shop_address1);
		llt_my_shop_introduce = (LinearLayout) findViewById(R.id.llt_my_shop_introduce);
		llt_my_shop_connect = (LinearLayout) findViewById(R.id.llt_my_shop_connect);
		llt_my_shop_phone = (LinearLayout) findViewById(R.id.llt_my_shop_phone);
		llt_my_shop_address = (LinearLayout) findViewById(R.id.llt_my_shop_address);
		myShop_logo = (LinearLayout) findViewById(R.id.myShop_logo);
	}

	private void initLayoutP() {
		FrameLayout.LayoutParams layoutParams = (LayoutParams) img_my_shop_mohu
				.getLayoutParams();
		layoutParams.width = screenWidth;
		layoutParams.height = screenWidth / 4;
		LinearLayout.LayoutParams layoutParams2 = (android.widget.LinearLayout.LayoutParams) img_my_shop_qrcode
				.getLayoutParams();
		layoutParams2.width = (int) (screenWidth / 4 - AbViewUtil.dip2px(this,
				20));
		layoutParams2.height = (int) (screenWidth / 4 - AbViewUtil.dip2px(this,
				20));

	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		img_my_shop_qrcode.setOnClickListener(this);
		llt_my_shop_introduce.setOnClickListener(this);
		llt_my_shop_connect.setOnClickListener(this);
		llt_my_shop_phone.setOnClickListener(this);
		llt_my_shop_address.setOnClickListener(this);
		myShop_logo.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		if (myShopInfoBean == null) {
			myShopInfoBean = new MyShopInfoBean();
		}
		switch (view.getId()) {
		case R.id.myShop_logo:// 商铺logo
			photoUtills.pickImage();
			break;
		case R.id.llt_my_shop_introduce:// 商铺介绍
			Intent intent_introduce = new Intent(this,
					MyShopIntroduceActivity.class);
			intent_introduce.putExtra("images", images);
			intent_introduce.putExtra("introduce",
					myShopInfoBean.getIntroduce());
			intent_introduce.putExtra("details", myShopInfoBean.getDetails());
			startActivityForResult(intent_introduce, REQUEST_INTRODUCE);
			break;
		case R.id.llt_my_shop_connect:// 联系人
			Intent shop_connect = new Intent(this, ShopConnectActivity.class);
			shop_connect.putExtra("name", myShopInfoBean.getContactName());
			startActivityForResult(shop_connect, REQUEST_CONNECT);
			break;
		case R.id.llt_my_shop_phone:// 联系电话
			Intent shop_phone = new Intent(this, ShopConnectPhoneActivity.class);
			shop_phone.putExtra("phone", myShopInfoBean.getMobile());
			startActivityForResult(shop_phone, REQUEST_PHONE);
			break;
		case R.id.llt_my_shop_address:// 店铺地址
			Intent intent_address = new Intent(this, MapActivity.class);
			intent_address.putExtra("isUpdte", true);
			if (TextUtils.isEmpty(myShopInfoBean.getAddress())) {
				return;
			}
			// intent_address.putExtra("latitude",
			// myShopInfoBean.getLatitude());
			// intent_address.putExtra("longitude",
			// myShopInfoBean.getLongitude());
			intent_address.putExtra("address", myShopInfoBean.getAddress());
			startActivityForResult(intent_address, REQUEST_ADDRESS);
			break;
		case R.id.img_my_shop_qrcode:// 点击放大二维码
			// MagnifyImg();
			break;
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		Intent intent_change = new Intent(this, ChangeShopActivity.class);
		startActivity(intent_change);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				case REQUEST_CONNECT:
					String name = data.getStringExtra("name");
					tv_my_shop_connect_name.setText(name);
					myShopInfoBean.setContactName(name);
					userShopInfoBean.setNikeName(name);
					break;
				case REQUEST_PHONE:
					String phone = data.getStringExtra("phone");
					tv_my_shop_connect_phone.setText(phone);
					myShopInfoBean.setMobile(phone);
					userShopInfoBean.setPhone(phone);
					break;
				case REQUEST_ADDRESS:
					address = data.getStringExtra("address");
					double lat = data.getDoubleExtra("lat", -1);
					double lng = data.getDoubleExtra("lng", -1);
					tv_my_shop_connect_address.setText(address);
					tv_my_shop_address.setText(address);
					myShopInfoBean.setAddress(address);
					// myShopInfoBean.setLatitude(lat);
					// myShopInfoBean.setLongitude(lng);
					myShopInfoBean.setAddress(address);
					userShopInfoBean.setAddress(address);
					break;
				case REQUEST_INTRODUCE:
					getBusinessInfo();
					break;
				}
			}
		} catch (Exception e) {

		} finally {
			if (resultCode == RESULT_OK) {
				if (requestCode == PhotoUtills.REQUEST_IMAGE_FROM_ALBUM_AND_CROP) {
					Uri uri = data.getData();
					String filePath = PhotoUtills.getPath(this, uri);
					FileUtils.copyFile(filePath,
							PhotoUtills.photoCurrentFile.toString(), true);
					photoUtills.startCropImage();
					return;
				} else if (requestCode == PhotoUtills.REQUEST_IMAGE_FROM_CAMERA_AND_CROP) {
					photoUtills.startCropImage();
					return;
				}
				if (requestCode == PhotoUtills.REQUEST_IMAGE_CROP) {
					Bitmap bm = PhotoUtills.getBitmap();
					onPickedPhoto(FilePathGet.saveBitmap(bm), bm);
					return;
				}
			}
		}
	}

	private void onPickedPhoto(File photoCurrentFile, Bitmap bm) {
		Glide.clear(img_my_shop_logo);
		img_my_shop_logo.setImageBitmap(bm);
		photoCurrentFile = FilePathGet.saveBitmap(bm);
		String pStr = Picture_Base64.GetImageStr(photoCurrentFile.toString());
		updateBusinessLogo(pStr);
	}

	/**
	 * 修改店铺LOGO
	 */
	private void updateBusinessLogo(String pStr) {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.updateBusinessLogo(uuid, "app", reqTime,
					userShopInfoBean.getBusinessId(), pStr,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
						ContentUtils.showMsg(MyShopActivity.this, "修改图像成功");
						}

						@Override
						public void onResponseFailed(String msg) {
//						ContentUtils.showMsg(MyShopActivity.this, "修改图像失败");

						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 图像裁剪实现类
	 * 
	 * @author guanghui.han
	 * 
	 */
	class MyPhotoUtills extends PhotoUtills {

		public MyPhotoUtills(Context ct) {
			super(ct);
			super.initPickView();
		}

		@Override
		protected PickImageDescribe getPickImageDescribe() {

			if (defaultImageDescribe == null) {
				defaultImageDescribe = new PickImageDescribe();
			}
			// 设置页设置头像，裁剪比例1:1
			defaultImageDescribe.setFile(photoCurrentFile);
			defaultImageDescribe.setOutputX(150);
			defaultImageDescribe.setOutputY(150);
			defaultImageDescribe.setAspectX(1);
			defaultImageDescribe.setAspectY(1);
			defaultImageDescribe.setOutputFormat(DEFAULT_IMG_FORMAT);
			return defaultImageDescribe;
		}
	}
}
