package com.lianbi.mezone.b.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.CircularImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.SalesMan;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

public class SalseEmplyeeModifyActivity extends BaseActivity {
	static final String[] TITLE = { "修改店员信息" };
	CircularImageView img_sales_employee_modify;
	TextView tv_sales_employee_modify_number, tv_sales_employee_modify1_sure;

	EditText edit_sales_employee_modify_name, edit_sales_employee_modify_phone,
			edit_sales_employee_modify_position;
	LinearLayout layout_salse_employee_m_head;
	private String position;
	private String name;
	private String phone;
	private SalesMan sortModel;
	private MyPhotoUtills photoUtills;
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salse_emplyee_modify, HAVETYPE);
		photoUtills = new MyPhotoUtills(this);
		initView();
		sortModel = (SalesMan) getIntent().getSerializableExtra("sortmodel");
		if (sortModel != null) {
			upView();
		}
		listen();
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
					onPickedPhoto(PhotoUtills.photoCurrentFile, bm);
					return;
				}
			}
		}
	}

	private void onPickedPhoto(final File photoCurrentFile, Bitmap bm) {
		Glide.clear(img_sales_employee_modify);
		img_sales_employee_modify.setImageBitmap(bm);
		file = photoCurrentFile;
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

	private void listen() {
		tv_sales_employee_modify1_sure.setOnClickListener(this);
		layout_salse_employee_m_head.setOnClickListener(this);

	}

	private void initView() {
		setPageTitle("修改店员信息");
		tv_sales_employee_modify1_sure = (TextView) findViewById(R.id.tv_sales_employee_modify1_sure);
		tv_sales_employee_modify_number = (TextView) findViewById(R.id.tv_sales_employee_modify_number);
		img_sales_employee_modify = (CircularImageView) findViewById(R.id.img_sales_employee_modify);
		edit_sales_employee_modify_name = (EditText) findViewById(R.id.edit_sales_employee_modify_name);
		edit_sales_employee_modify_phone = (EditText) findViewById(R.id.edit_sales_employee_modify_phone);
		edit_sales_employee_modify_position = (EditText) findViewById(R.id.edit_sales_employee_modify_position);
		layout_salse_employee_m_head = (LinearLayout) findViewById(R.id.layout_salse_employee_m_head);

	}

	@Override
	protected void onChildClick(View v) {
		super.onChildClick(v);
		switch (v.getId()) {
		case R.id.layout_salse_employee_m_head:
			photoUtills.pickImage();
			break;
		case R.id.tv_sales_employee_modify1_sure:// 保存
			save();
			break;
		}
	}

	/**
	 * @保存修改
	 */
	private void save() {
		name = edit_sales_employee_modify_name.getText().toString().trim();
		phone = edit_sales_employee_modify_phone.getText().toString().trim();
		position = edit_sales_employee_modify_position.getText().toString()
				.trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "员工姓名不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "员工电话号码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!AbStrUtil.isMobileNo(phone)) {
			ContentUtils.showMsg(this, "请输入正确的手机号");
			return;
		}
		if (TextUtils.isEmpty(position)) {
			Toast.makeText(this, "员工职位不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			getemployeeModifyinfo();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @初实话界面
	 */

	private void upView() {
		tv_sales_employee_modify_number.setText(sortModel.getSalesclerk_id()
				+ "");
		edit_sales_employee_modify_name.setText(sortModel.getSalesclerk_name());
		edit_sales_employee_modify_phone.setText(sortModel
				.getSalesclerk_phone());
		edit_sales_employee_modify_position.setText(sortModel
				.getSalesclerk_job());
		String url = sortModel.getSalesclerk_image();
		Glide.with(this).load(url).error(R.mipmap.defaultpeson)
				.into(img_sales_employee_modify);
		Glide.with(this).load(url).asBitmap().error(R.mipmap.defaultpeson)
				.into(new SimpleTarget<Bitmap>(100, 100) {

					@Override
					public void onResourceReady(Bitmap bitmap,
							GlideAnimation<? super Bitmap> arg1) {
						file = FilePathGet.saveBitmap(bitmap);
					}
				});
	}

	/**
	 * 修改店员详情
	 */
	private void getemployeeModifyinfo() throws FileNotFoundException {
		String pStr = "";
		if (file != null) {
			pStr = Picture_Base64.GetImageStr(file.toString());
		}
		okHttpsImp.postUpdateSalesClerkbyid(
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						Toast.makeText(SalseEmplyeeModifyActivity.this,
								"修改店员成功", Toast.LENGTH_SHORT).show();
						setResult(RESULT_OK);
						finish();
					}

					@Override
					public void onResponseFailed(String msg) {
						Toast.makeText(SalseEmplyeeModifyActivity.this,
								"修改店员失败", Toast.LENGTH_SHORT).show();

					}
				}, name, sortModel.getSalesclerk_id(), phone, position, pStr,
				sortModel.getBusiness_id());
	}
}
