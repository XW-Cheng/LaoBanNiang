package com.lianbi.mezone.b.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.bean.ProvinceBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;
import com.lianbi.mezone.b.photo.PopupWindowHelper;
import com.xizhi.mezone.b.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.REGX;
import cn.com.hgh.utils.Result;

/**
 * 新增商铺
 * 
 * @time 下午12:28:32
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class AddShopActivity extends BaseActivity {
	private static final int REQUEST_TYPE = 1245;
	private static final int REQUEST_ADDRESS = 7845;
	private LinearLayout llt_my_shop_type, llt_my_shop_address,
			llt_add_shop_img, llt_my_shop_address2;
	private ImageView img_add_shop;
	private EditText edt_add_shop_name, edt_add_shop_connect_name,
			edt_add_shop_phone,edt_add_shop_detail_address;
	private MyPhotoUtills photoUtills;
	private TextView tv_my_shop_type, tv_my_shop_address, tv_add_shop_summbit,
			tv_my_shop_address2;
	private String phone, name, address, parant_id, connect_name, parant_name,shop_detailaddress;
	private File file = null;
	private String latitude, longitude;
	private List<ProvinceBean> provinceBeans = new ArrayList<ProvinceBean>();
	private String province;
	private String provinceId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_shop, HAVETYPE);
		initView();
		initPickView();
		initAdapter();
		setLisenter();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("新增商铺");
		tv_add_shop_summbit = (TextView) findViewById(R.id.tv_add_shop_summbit);
		llt_add_shop_img = (LinearLayout) findViewById(R.id.llt_add_shop_img);
		tv_my_shop_type = (TextView) findViewById(R.id.tv_my_shop_type);
		tv_my_shop_address = (TextView) findViewById(R.id.tv_my_shop_address1);
		llt_my_shop_type = (LinearLayout) findViewById(R.id.llt_my_shop_type);
		llt_my_shop_address = (LinearLayout) findViewById(R.id.llt_my_shop_address1);
		img_add_shop = (ImageView) findViewById(R.id.img_add_shop);
		edt_add_shop_name = (EditText) findViewById(R.id.edt_add_shop_name);
		edt_add_shop_connect_name = (EditText) findViewById(R.id.edt_add_shop_connect_name);
		edt_add_shop_detail_address = (EditText) findViewById(R.id.edt_add_shop_detail_address);
		edt_add_shop_phone = (EditText) findViewById(R.id.edt_add_shop_phone);
		llt_my_shop_address2 = (LinearLayout) findViewById(R.id.llt_my_shop_address2);
		tv_my_shop_address2 = (TextView) findViewById(R.id.tv_my_shop_address2);
		photoUtills = new MyPhotoUtills(this);
		address = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.ADDRESS);
		latitude = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.LATITUDE);
		longitude = ContentUtils.getSharePreString(this,
				Constants.SHARED_PREFERENCE_NAME, Constants.LONGITUDE);
		tv_my_shop_address.setText(address);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		llt_add_shop_img.setOnClickListener(this);
		llt_my_shop_type.setOnClickListener(this);
		llt_my_shop_address.setOnClickListener(this);
		tv_add_shop_summbit.setOnClickListener(this);
		llt_my_shop_address2.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.llt_my_shop_type:// 行业分类
			Intent intent_type = new Intent(this, SelectTypeActivity.class);
			intent_type.putExtra("isMyShop", false);
			startActivityForResult(intent_type, REQUEST_TYPE);
			break;
		case R.id.llt_my_shop_address1:
			Intent intent_map = new Intent(this, MapActivity.class);
			startActivityForResult(intent_map, REQUEST_ADDRESS);
			break;
		case R.id.llt_my_shop_address2:
			getProvince();
			pickImage();
			break;
		case R.id.llt_add_shop_img:
			photoUtills.pickImage();
			break;
		case R.id.tv_add_shop_summbit:
			phone = edt_add_shop_phone.getText().toString().trim();
			name = edt_add_shop_name.getText().toString().trim();
			connect_name = edt_add_shop_connect_name.getText().toString()
					.trim();
			shop_detailaddress = edt_add_shop_detail_address.getText().toString().trim();
			verify();
			break;
		}
	}

	/**
	 * pop
	 */
	PopupWindow pw = null;

	/**
	 * popView
	 */
	View pickView;

	// ScreenUtils.getScreenHeight(AddShopActivity.this) / 3
	private void pickImage() {
		if (pw == null) {
			pw = PopupWindowHelper.createPopupWindow(pickView,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			pw.setAnimationStyle(R.style.slide_up_in_down_out);
		}
		pw.showAtLocation(this.getWindow().getDecorView(),
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
	}

	public void initPickView() {
		pickView = View.inflate(this, R.layout.provincepop, null);
		lv_province_pop = (ListView) pickView
				.findViewById(R.id.lv_province_pop);
		lv_province_pop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				province = provinceBeans.get(position).getProvince();
				provinceId = provinceBeans.get(position).getProvinceId();
				tv_my_shop_address2.setText(province);
				pw.dismiss();
			}
		});
		pickView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
	}

	/**
	 * 获取省
	 */
	private void getProvince() {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.getProvinceList(uuid, "app", reqTime,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							try {
								JSONObject jsonObject = new JSONObject(
										resString);
								resString = jsonObject.getString("modelList");
								provinceBeans = JSON.parseArray(resString,
										ProvinceBean.class);
								if (provinceBeans != null
										&& provinceBeans.size() > 0) {
									mAdapter.replaceAll(provinceBeans);
								}
							} catch (Exception e) {
								e.printStackTrace();
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

	QuickAdapter<ProvinceBean> mAdapter;
	private ListView lv_province_pop;

	private void initAdapter() {
		mAdapter = new QuickAdapter<ProvinceBean>(this,
				R.layout.item_province_pop, provinceBeans) {

			@Override
			protected void convert(BaseAdapterHelper helper, ProvinceBean item) {
				TextView tv_province_pop = helper.getView(R.id.tv_province_pop);
				tv_province_pop.setText(item.getProvince());

			}
		};
		lv_province_pop.setAdapter(mAdapter);
	}

	/**
	 * 新增店铺B端
	 */
	private void addBusinessByB() {
		String license = Picture_Base64.GetImageStr(file.toString());
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.addBusinessByB(name, address,
					userShopInfoBean.getUserId(), parant_id, license,
					connect_name, phone, provinceId,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							String resString = result.getData();
							try {
								JSONObject jsonObject = new JSONObject(
										resString);
								String businessId = jsonObject
										.getString("businessId");
								if (TextUtils.isEmpty(userShopInfoBean
										.getBusinessId())) {
									changeBussinessOnApp(businessId);
								} else {
									Intent intent = new Intent(
											AddShopActivity.this,
											ChangeShopActivity.class);
									AddShopActivity.this.startActivity(intent);
									ContentUtils.showMsg(AddShopActivity.this,
											"新增商铺成功");
									finish();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onResponseFailed(String msg) {
							Log.i("tag","294-----AAAAA  "+msg);
							ContentUtils
									.showMsg(AddShopActivity.this, "新增商铺失败");
						}
					}, uuid, "app", reqTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * B端切换店铺
	 */
	private void changeBussinessOnApp(final String business_id) {
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		if (!TextUtils.isEmpty(business_id)) {
			try {
				okHttpsImp.changeBussinessOnApp(uuid, "app", reqTime,
						userShopInfoBean.getUserId(), business_id,
						new MyResultCallback<String>() {

							@Override
							public void onResponseResult(Result result) {
								Intent intent = new Intent(
										AddShopActivity.this,
										ChangeShopActivity.class);
								AddShopActivity.this.startActivity(intent);
								ContentUtils.showMsg(AddShopActivity.this,
										"新增商铺成功");
								finish();
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
	}

	/**
	 * 校验
	 */
	private void verify() {

		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(AddShopActivity.this, "请输入商铺名称");
			return;
		}
		if (file == null) {
			ContentUtils.showMsg(AddShopActivity.this, "请上传照片");
			return;
		}
		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(AddShopActivity.this, "请输入联系人姓名");
			return;
		}
		if (!TextUtils.isEmpty(phone)) {
			if (!phone.matches(REGX.REGX_MOBILE)) {
				ContentUtils.showMsg(AddShopActivity.this, "请输入正确的联系电话");
				return;
			}
		} else {
			ContentUtils.showMsg(AddShopActivity.this, "请输入联系电话");
			return;
		}
		if (TextUtils.isEmpty(connect_name)) {
			ContentUtils.showMsg(AddShopActivity.this, "请输入联系人名称");
			return;
		}
		String pro = tv_my_shop_address2.getText().toString().trim();
		if (TextUtils.isEmpty(pro)) {
			ContentUtils.showMsg(AddShopActivity.this, "请选择商铺所在省");
			return;
		}
		if (TextUtils.isEmpty(parant_name)) {
			ContentUtils.showMsg(AddShopActivity.this, "请选择类型");
			return;
		}
		if (TextUtils.isEmpty(address)) {
			ContentUtils.showMsg(AddShopActivity.this, "请选择地址");
			return;
		}
//		if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
//			ContentUtils.showMsg(AddShopActivity.this, "定位失败,请到首页定位");
//			return;
//		}
		if (TextUtils.isEmpty(userShopInfoBean.getUserId())
				|| null==userShopInfoBean.getUserId()) {
			ContentUtils.showMsg(AddShopActivity.this, "为了保证您的数据安全请退出重新登录");
			return;
		}
		addBusinessByB();
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
				switch (requestCode) {
				case PhotoUtills.REQUEST_IMAGE_FROM_ALBUM_AND_CROP:
				try {
					Uri uri = data.getData();
					String filePath = PhotoUtills.getPath(this, uri);
					FileUtils.copyFile(filePath,
							PhotoUtills.photoCurrentFile.toString(), true);
					photoUtills.startCropImage();
				}
				catch (Exception  e){
					ContentUtils.showMsg(AddShopActivity.this, "图片过大，加载失败");
					e.printStackTrace();
				}
					break;
				case PhotoUtills.REQUEST_IMAGE_FROM_CAMERA_AND_CROP:
					photoUtills.startCropImage();
					break;
				case PhotoUtills.REQUEST_IMAGE_CROP:
					Bitmap bm = PhotoUtills.getBitmap(200, 150);
					img_add_shop.setImageBitmap(bm);
					file = FilePathGet.saveBitmap(bm);
					break;
				case REQUEST_TYPE:
					parant_id = data.getStringExtra("parant_id");
					parant_name = data.getStringExtra("parant_name");
					tv_my_shop_type.setText(parant_name);
					break;
				case REQUEST_ADDRESS:
					address = data.getStringExtra("address");
					latitude = data.getStringExtra("lat");
					longitude = data.getStringExtra("lng");
					tv_my_shop_address.setText(address);
					break;
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
			// 设置页设置头像，裁剪比例4:3
			defaultImageDescribe.setFile(photoCurrentFile);
			defaultImageDescribe.setOutputX(480);
			defaultImageDescribe.setOutputY(360);
			defaultImageDescribe.setAspectX(4);
			defaultImageDescribe.setAspectY(3);
			defaultImageDescribe.setOutputFormat(DEFAULT_IMG_FORMAT);
			return defaultImageDescribe;
		}
	}

}
