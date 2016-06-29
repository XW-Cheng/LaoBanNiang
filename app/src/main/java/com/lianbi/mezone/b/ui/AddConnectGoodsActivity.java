package com.lianbi.mezone.b.ui;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.REGX;
import cn.com.hgh.utils.Result;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

/**
 * 新增联系人 --- 联系货源
 * 
 * @time 下午12:28:32
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class AddConnectGoodsActivity extends BaseActivity {
	private LinearLayout llt_add_connectgoods_img;
	private ImageView img_add_connect_goods;
	private EditText edt_add_connect_goods_name, edt_add_connect_goods_phone,
			edt_add_connect_goods_shop_name, edt_add_connect_goods_content;
	private MyPhotoUtills photoUtills;
	private TextView tv_add_connect_goods;
	private String phone, name, shop_name, content;
	private File file = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_connect_goods, HAVETYPE);
		initView();
		setLisenter();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("新增联系人");
		tv_add_connect_goods = (TextView) findViewById(R.id.tv_add_connect_goods);
		llt_add_connectgoods_img = (LinearLayout) findViewById(R.id.llt_add_connectgoods_img);
		img_add_connect_goods = (ImageView) findViewById(R.id.img_add_connect_goods);
		edt_add_connect_goods_name = (EditText) findViewById(R.id.edt_add_connect_goods_name);
		edt_add_connect_goods_phone = (EditText) findViewById(R.id.edt_add_connect_goods_phone);
		edt_add_connect_goods_content = (EditText) findViewById(R.id.edt_add_connect_goods_content);
		edt_add_connect_goods_shop_name = (EditText) findViewById(R.id.edt_add_connect_goods_shop_name);
		photoUtills = new MyPhotoUtills(this);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		llt_add_connectgoods_img.setOnClickListener(this);
		tv_add_connect_goods.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.llt_add_connectgoods_img:
			photoUtills.pickImage();
			break;
		case R.id.tv_add_connect_goods:
			phone = edt_add_connect_goods_phone.getText().toString().trim();
			name = edt_add_connect_goods_name.getText().toString().trim();
			shop_name = edt_add_connect_goods_shop_name.getText().toString()
					.trim();
			content = edt_add_connect_goods_content.getText().toString().trim();
			verify();
			break;
		}
	}

	/**
	 * 校验
	 */
	private void verify() {
		if (file == null) {
			ContentUtils.showMsg(AddConnectGoodsActivity.this, "请上传照片");
			return;
		}
		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(AddConnectGoodsActivity.this, "请输入联系人姓名");
			return;
		}
		if (!TextUtils.isEmpty(phone)) {
			if (!phone.matches(REGX.REGX_MOBILE)) {
				ContentUtils
						.showMsg(AddConnectGoodsActivity.this, "请输入正确的联系电话");
				return;
			}
		} else {
			ContentUtils.showMsg(AddConnectGoodsActivity.this, "请输入联系电话");
			return;
		}
		if (TextUtils.isEmpty(shop_name)) {
			ContentUtils.showMsg(AddConnectGoodsActivity.this, "请输入货源店名称");
			return;
		}
		if (TextUtils.isEmpty(content)) {
			ContentUtils.showMsg(AddConnectGoodsActivity.this, "请输入货源内容");
			return;
		}
		addProductSourceContacts();
	}

	/**
	 * 新增货源联系人
	 */
	private void addProductSourceContacts() {
		String imageStr = Picture_Base64.GetImageStr(file.toString());
		okHttpsImp.addProductSourceContacts(userShopInfoBean.getUserId(),
				imageStr, phone, name, shop_name, content,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						ContentUtils.showMsg(AddConnectGoodsActivity.this,
								"新增联系人成功");
						setResult(RESULT_OK);
						finish();
					}

					@Override
					public void onResponseFailed(String msg) {
						ContentUtils.showMsg(AddConnectGoodsActivity.this,
								"新增联系人失败");
					}
				});
	}

	/**
	 * 处理相册返回、照相返回、裁剪返回的图片
	 */
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
					img_add_connect_goods.setImageBitmap(bm);
					file = FilePathGet.saveBitmap(bm);
					return;
				}
			}
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
