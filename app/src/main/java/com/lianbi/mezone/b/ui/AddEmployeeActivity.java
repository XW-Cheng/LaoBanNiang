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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.CircularImageView;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

public class AddEmployeeActivity extends BaseActivity {
	static final String[] TITLE = { "新增店员" };
	private EditText edit_add_sales_employee_modify_name,
			edit_add_sales_employee_modify_phone,
			edit_add_sales_employee_modify_position;

	CircularImageView img_add_sales_employee_modify;
	LinearLayout layout_add_salse_employee_head;
	TextView tv_sales_employee_modify_sure;
	private String position;
	private String phone;
	private String name;
	private MyPhotoUtills photoUtills;
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_employee, HAVETYPE);

		photoUtills = new MyPhotoUtills(this);
		initView();
		listen();
	}

	private void listen() {
		layout_add_salse_employee_head.setOnClickListener(this);
		tv_sales_employee_modify_sure.setOnClickListener(this);
	}

	private void initView() {
		setPageTitle("新增店员");
		layout_add_salse_employee_head = (LinearLayout) findViewById(R.id.layout_add_salse_employee_head);
		img_add_sales_employee_modify = (CircularImageView) findViewById(R.id.img_add_sales_employee_modify);
		edit_add_sales_employee_modify_name = (EditText) findViewById(R.id.edit_add_sales_employee_modify_name);
		edit_add_sales_employee_modify_phone = (EditText) findViewById(R.id.edit_add_sales_employee_modify_phone);
		edit_add_sales_employee_modify_position = (EditText) findViewById(R.id.edit_add_sales_employee_modify_position);
		tv_sales_employee_modify_sure = (TextView) findViewById(R.id.tv_sales_employee_modify_sure);

	}

	@Override
	protected void onChildClick(View v) {
		super.onChildClick(v);
		switch (v.getId()) {
		case R.id.layout_add_salse_employee_head:// 上传图片
			photoUtills.pickImage();
			break;
		case R.id.tv_sales_employee_modify_sure:// 保存资料
			save();
			break;
		}
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
					File file = FilePathGet.saveBitmap(bm);
					onPickedPhoto(file, bm);
					return;
				}
			}
		}
	}

	private void onPickedPhoto(final File photoCurrentFile, Bitmap bm) {
		img_add_sales_employee_modify.setImageBitmap(bm);
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

	private void save() {
		name = edit_add_sales_employee_modify_name.getText().toString().trim();
		phone = edit_add_sales_employee_modify_phone.getText().toString()
				.trim();
		position = edit_add_sales_employee_modify_position.getText().toString()
				.trim();
		if (file == null) {
			Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
			return;
		}
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
		getemployeeEmployeeadd();
	}

	/**
	 * 新增店员
	 */
	private void getemployeeEmployeeadd() {
		String pStr = Picture_Base64.GetImageStr(file.toString());
		okHttpsImp.postAddsalesClerk(new MyResultCallback<String>() {
			@Override
			public void onResponseResult(Result result) {
				Toast.makeText(AddEmployeeActivity.this, "新增店员成功", 0).show();
				finish();
			}

			@Override
			public void onResponseFailed(String msg) {
				Toast.makeText(AddEmployeeActivity.this, "新增店员失败", 0).show();
			}
		}, name, userShopInfoBean.getBusinessId(), phone, position, pStr);
	}

}
