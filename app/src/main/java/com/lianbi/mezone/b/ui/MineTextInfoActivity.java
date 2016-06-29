package com.lianbi.mezone.b.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.REGX;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.CircularImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.LoginBackBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

/**
 * 我的资料页面
 * 
 * @author qiuyu.lv
 * @date 2016-1-13
 * @version
 */
@SuppressLint("ResourceAsColor")
public class MineTextInfoActivity extends BaseActivity {
	private CircularImageView touxiang;
	MyPhotoUtills photoUtills;
	LinearLayout loginpwd_ll, llt_minetextinfo_img;
	EditText mineinfo_name_et, mineinfo_phone_et, mineinfo_contactname_et,
			mineinfo_contactphone_et;
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_mineinfo, NOTYPE);
		photoUtills = new MyPhotoUtills(this);
		initView();
		getUseByiId();
	}

	private void initView() {
		setPageTitle("我的资料");
		setPageRightText("保存");
		setPageRightTextColor(R.color.colores_news_01);
		touxiang = (CircularImageView) findViewById(R.id.touxiangimageview);
		loginpwd_ll = (LinearLayout) findViewById(R.id.loginpwd_ll);
		llt_minetextinfo_img = (LinearLayout) findViewById(R.id.llt_minetextinfo_img);
		mineinfo_name_et = (EditText) findViewById(R.id.mineinfo_name_et);
		mineinfo_phone_et = (EditText) findViewById(R.id.mineinfo_phone_et);
		mineinfo_contactname_et = (EditText) findViewById(R.id.mineinfo_contactname_et);
		mineinfo_contactphone_et = (EditText) findViewById(R.id.mineinfo_contactphone_et);
		loginpwd_ll.setOnClickListener(this);
		llt_minetextinfo_img.setOnClickListener(this);
	}

	/**
	 * 获取用户详细
	 */
	private void getUseByiId() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getUseByiId(new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					String reString = result.getData();
					LoginBackBean backBean = JSON.parseObject(reString,
							LoginBackBean.class);
					if (backBean != null) {
						if (!TextUtils.isEmpty(backBean.getUsername())) {
							mineinfo_name_et.setText(backBean.getUsername());
						}
						if (!TextUtils.isEmpty(backBean.getMobile())) {
							mineinfo_phone_et.setText(backBean.getMobile());
						}
						if (!TextUtils.isEmpty(backBean.getUrgent())) {
							mineinfo_contactname_et.setText(backBean
									.getUrgent());
						}
						if (!TextUtils.isEmpty(backBean.getMobile())) {
							mineinfo_contactphone_et.setText(backBean
									.getMobile());
						}
						String url = backBean.getUserImage();
						if (!TextUtils.isEmpty(url)) {
							userShopInfoBean.setPersonHeadUrl(url);
							Glide.with(MineTextInfoActivity.this).load(url)
									.asBitmap().error(R.mipmap.defaultpeson)
									.into(new SimpleTarget<Bitmap>(150, 150) {
										@Override
										public void onLoadFailed(Exception e,
												Drawable errorDrawable) {
											super.onLoadFailed(e, errorDrawable);
											touxiang.setImageResource(R.mipmap.defaultpeson);
										}

										@Override
										public void onResourceReady(
												Bitmap bitmap,
												GlideAnimation<? super Bitmap> arg1) {
											touxiang.setImageBitmap(bitmap);
											file = FilePathGet
													.saveBitmap(bitmap);
										}
									});
						}
					}
				}

				@Override
				public void onResponseFailed(String msg) {

				}
			}, uuid, "app", reqTime, userShopInfoBean.getUserId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		if (view == llt_minetextinfo_img) {
			photoUtills.pickImage();
		} else if (view == loginpwd_ll) {
			startActivity(new Intent(this, LoginPasswordActivity.class));
		}
	}

	@Override
	protected void onTitleRightClickTv() {
		super.onTitleRightClickTv();
		String imageStr = null;
		if (file != null) {
			imageStr = Picture_Base64.GetImageStr(file.toString());
		}
		final String name = mineinfo_name_et.getText().toString().trim();
		String phone = mineinfo_phone_et.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			phone = ContentUtils.getSharePreString(this,
					Constants.SHARED_PREFERENCE_NAME, Constants.USER_NAME);
		}
		String contactName = mineinfo_contactname_et.getText().toString()
				.trim();
		String contactPhone = mineinfo_contactphone_et.getText().toString()
				.trim();

		if (TextUtils.isEmpty(imageStr)) {
			ContentUtils.showMsg(this, "头像不能为空");
			return;
		}
		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(this, "昵称不能为空");
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			ContentUtils.showMsg(this, "联系电话不能为空");
			return;
		}
		if (!AbStrUtil.isMobileNo(phone)) {
			ContentUtils.showMsg(this, "联系电话格式不正确");
			return;
		}
		if (contactPhone.length() < 7) {
			ContentUtils.showMsg(this, "紧急联系人电话小于11位");
			return;
		}
		if(!contactPhone.matches(REGX.REGX_MOBILE)){
			ContentUtils.showMsg(this, "紧急联系人电话格式不正确");
			return;
		}
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		String password = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.PASS_WORD);
		try {
			okHttpsImp.postUpdateUseById(new MyResultCallback<String>() {

				@Override
				public void onResponseResult(Result result) {
					ContentUtils.showMsg(MineTextInfoActivity.this, "修改成功！");
					userShopInfoBean.setName(name);
					userShopInfoBean.setPersonHeadUrl(file.toString());
					setResult(RESULT_OK);
					finish();

				}

				@Override
				public void onResponseFailed(String msg) {
					ContentUtils.showMsg(MineTextInfoActivity.this, "设置失败！");
				}
			}, uuid, "app", reqTime, userShopInfoBean.getUserId(), name,
					imageStr, phone, contactName, contactPhone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 处理相册返回、照相返回、裁剪返回的图片
	 */
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
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
					file = photoUtills.photoCurrentFile;
					// file = FilePathGet.saveBitmap(bm);
					onPickedPhoto(bm);
					return;
				}
			}
		}
	}

	private void onPickedPhoto(Bitmap bm) {
		Glide.with(this).load(file).into(touxiang);

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
