package com.lianbi.mezone.b.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.ShopIntroduceImageBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

/**
 * 商铺介绍
 * 
 * @time 下午5:39:27
 * @date 2016-1-14
 * @author hongyu.yang
 * 
 */
public class MyShopIntroduceActivity extends BaseActivity {
	private static final int REQUEST_PHOTO_DELETE = 8945;
	private ImageView img_shop_introduce1, img_shop_introduce2,
			img_shop_introduce3;
	private EditText edt_shop_introduce_intro, edt_shop_introduce_detail;
	private MyPhotoUtills photoUtills;
	private List<File> files;
	private List<Bitmap> biMaps;
	private TextView tv_add_shop_introduce_summbit;
	private int img_flag;
	private HashMap<String, Bitmap> hashMap;
	private HashMap<String, File> mapFile;
	private boolean flag = true;
	private ArrayList<ShopIntroduceImageBean> images;
	private ArrayList<ShopIntroduceImageBean> imagesDel = new ArrayList<ShopIntroduceImageBean>();
	private String introduce, details;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_introduce, HAVETYPE);
		 images = (ArrayList<ShopIntroduceImageBean>) getIntent()
		 .getSerializableExtra("images");
		introduce = getIntent().getStringExtra("introduce");
		details = getIntent().getStringExtra("details");
		if(null==details){
			details="";
		}
		if(null==introduce){
			introduce="";
		}
		initView();
		setLisenter();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("商铺介绍");
		tv_add_shop_introduce_summbit = (TextView) findViewById(R.id.tv_add_shop_introduce_summbit);
		img_shop_introduce1 = (ImageView) findViewById(R.id.img_shop_introduce1);
		img_shop_introduce2 = (ImageView) findViewById(R.id.img_shop_introduce2);
		img_shop_introduce3 = (ImageView) findViewById(R.id.img_shop_introduce3);
		edt_shop_introduce_intro = (EditText) findViewById(R.id.edt_shop_introduce_intro);
		edt_shop_introduce_detail = (EditText) findViewById(R.id.edt_shop_introduce_detail);
		edt_shop_introduce_intro.setText(introduce.trim());
		edt_shop_introduce_detail.setText(details.trim());
		photoUtills = new MyPhotoUtills(this);
		files = new ArrayList<File>();
		biMaps = new ArrayList<Bitmap>();
		hashMap = new HashMap<String, Bitmap>();
		mapFile = new HashMap<String, File>();
		if (images != null && images.size() > 0) {
			int s = images.size();
			if (s > 3) {
				s = 3;
			}
			for (int i = 0; i < s; i++) {
				loadImage(images.get(i).getImageUrl(), i);
			}
			if (images.size() == 1) {
				img_shop_introduce1.setVisibility(View.VISIBLE);
				img_shop_introduce2
						.setImageResource(R.mipmap.add_connect_goods_ing);
				img_shop_introduce2.setVisibility(View.VISIBLE);
			}
			if (images.size() == 2) {
				img_shop_introduce1.setVisibility(View.VISIBLE);
				img_shop_introduce2.setVisibility(View.VISIBLE);
				img_shop_introduce3.setVisibility(View.VISIBLE);
				img_shop_introduce3
						.setImageResource(R.mipmap.add_connect_goods_ing);
			}
			if (s == 3) {
				img_shop_introduce1.setVisibility(View.VISIBLE);
				img_shop_introduce2.setVisibility(View.VISIBLE);
				img_shop_introduce3.setVisibility(View.VISIBLE);
			}
		} else {
			img_shop_introduce1
					.setImageResource(R.mipmap.add_connect_goods_ing);
		}
	}

	/**
	 * 加载图片
	 */
	private void loadImage(String imageUrl, final int i) {
		Glide.with(MyShopIntroduceActivity.this).load(imageUrl).asBitmap()
				.error(R.mipmap.defaultimg_11)
				.into(new SimpleTarget<Bitmap>(100, 100) {
					@Override
					public void onResourceReady(Bitmap bitmap,
							GlideAnimation<? super Bitmap> arg1) {
						FilePathGet.saveBitmap(bitmap,
								File.separator + System.currentTimeMillis()
										+ ".jpg", i, mapFile);
						hashMap.put(i + "", bitmap);
						if (i == 0) {
							img_shop_introduce1.setVisibility(View.VISIBLE);
							img_shop_introduce1.setImageBitmap(bitmap);
						}
						if (i == 1) {
							img_shop_introduce2.setVisibility(View.VISIBLE);
							img_shop_introduce2.setImageBitmap(bitmap);
						}
						if (i == 2) {
							img_shop_introduce3.setVisibility(View.VISIBLE);
							img_shop_introduce3.setImageBitmap(bitmap);
						}
					}
				});
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		try {
			//
			if (flag) {
				// for (int i = 0; i < 3; i++) {
				// for (Entry<String, File> entry : mapFile.entrySet()) {
				// String key = entry.getKey();
				// File value = entry.getValue();
				// if (key.equals(i + "")) {
				// files.add(value);
				// }
				// }
				// }
				for (int i = 0; i < 3; i++) {
					for (Entry<String, Bitmap> entry : hashMap.entrySet()) {
						String key = entry.getKey();
						Bitmap value = entry.getValue();
						if (key.equals(i + "")) {
							biMaps.add(value);
						}
					}
				}
				flag = false;
			}
			switch (view.getId()) {
			case R.id.img_shop_introduce1:
				img_flag = 1;
				if (biMaps.size() == 0) {
					photoUtills.pickImage();
				} else {
					Intent intent_delete = new Intent(this,
							PhotoDeleteActivity.class);
					intent_delete.putExtra("image", biMaps.get(0));
					startActivityForResult(intent_delete, REQUEST_PHOTO_DELETE);
				}
				break;
			case R.id.img_shop_introduce2:
				img_flag = 2;
				if (biMaps.size() == 0) {
					photoUtills.pickImage();

				} else if (biMaps.size() == 1) {
					photoUtills.pickImage();
				} else {
					Intent intent_delete = new Intent(this,
							PhotoDeleteActivity.class);
					intent_delete.putExtra("image", biMaps.get(1));
					startActivityForResult(intent_delete, REQUEST_PHOTO_DELETE);
				}
				break;
			case R.id.img_shop_introduce3:
				img_flag = 3;
				if (biMaps.size() == 0) {
					photoUtills.pickImage();

				} else if (biMaps.size() == 1) {
					photoUtills.pickImage();
				} else if (biMaps.size() == 2) {
					photoUtills.pickImage();
				} else {
					Intent intent_delete = new Intent(this,
							PhotoDeleteActivity.class);
					intent_delete.putExtra("image", biMaps.get(2));
					startActivityForResult(intent_delete, REQUEST_PHOTO_DELETE);
				}
				break;
			case R.id.tv_add_shop_introduce_summbit:
				String shop_jianjie = edt_shop_introduce_intro.getText()
						.toString().trim();
				String shop_detail = edt_shop_introduce_detail.getText()
						.toString().trim();
				if (TextUtils.isEmpty(shop_jianjie)) {
					ContentUtils.showMsg(this, "请输入商铺简介");
					return;
				}
				if (TextUtils.isEmpty(shop_detail)) {
					ContentUtils.showMsg(this, "请输入商铺详细介绍");
					return;
				}
				updateBusinessIntroduce(shop_jianjie, shop_detail);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改店铺介绍
	 */
	private void updateBusinessIntroduce(String shop_jianjie, String shop_detail) {
		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder stringBuilderDel = new StringBuilder();
		String images = "";
		String delImageUrls = "";
		if (imagesDel != null && imagesDel.size() > 0) {
			for (int i = 0; i < imagesDel.size(); i++) {
				if (i + 1 == imagesDel.size()) {
					stringBuilderDel.append(imagesDel.get(i).getImageUrl());
				} else {
					stringBuilderDel.append(imagesDel.get(i).getImageUrl()
							+ ",");
				}
			}
		}
		delImageUrls = stringBuilderDel.toString();
		if (files != null && files.size() > 0) {
			for (int i = 0; i < files.size(); i++) {
				if (i + 1 == files.size()) {
					stringBuilder.append(Picture_Base64.GetImageStr(files
							.get(i).toString()));
				} else {
					stringBuilder.append(Picture_Base64.GetImageStr(files
							.get(i).toString()) + ",");
				}
			}
		}
		images = stringBuilder.toString();
		String reqTime = AbDateUtil.getDateTimeNow();
		String uuid = AbStrUtil.getUUID();
		try {
			okHttpsImp.updateBusinessIntroduce(uuid, "app", reqTime,
					userShopInfoBean.getBusinessId(), shop_jianjie,
					shop_detail, images, delImageUrls,
					new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(MyShopIntroduceActivity.this,
									"商铺介绍修改成功");
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void onResponseFailed(String msg) {
							ContentUtils.showMsg(MyShopIntroduceActivity.this,
									"商铺介绍修改失败");
						}
					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		img_shop_introduce1.setOnClickListener(this);
		img_shop_introduce2.setOnClickListener(this);
		img_shop_introduce3.setOnClickListener(this);
		tv_add_shop_introduce_summbit.setOnClickListener(this);
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
				switch (requestCode) {
				case PhotoUtills.REQUEST_IMAGE_FROM_ALBUM_AND_CROP:
					Uri uri = data.getData();
					String filePath = PhotoUtills.getPath(this, uri);
					FileUtils.copyFile(filePath,
							PhotoUtills.photoCurrentFile.toString(), true);
					photoUtills.startCropImage();
					break;

				case PhotoUtills.REQUEST_IMAGE_FROM_CAMERA_AND_CROP:
					photoUtills.startCropImage();
					break;
				case PhotoUtills.REQUEST_IMAGE_CROP:
					Bitmap bm = PhotoUtills.getBitmap(200, 150);
					if (img_flag == 1) {
						img_shop_introduce1.setImageBitmap(bm);
						img_shop_introduce2.setVisibility(View.VISIBLE);
					} else if (img_flag == 2) {
						img_shop_introduce2.setImageBitmap(bm);
						img_shop_introduce3
								.setImageResource(R.mipmap.add_connect_goods_ing);
						img_shop_introduce3.setVisibility(View.VISIBLE);
					} else if (img_flag == 3) {
						img_shop_introduce3.setImageBitmap(bm);
					}
					files.add(photoUtills.photoCurrentFile);
					// files.add(FilePathGet.saveBitmap(bm));
					biMaps.add(bm);
					break;
				case REQUEST_PHOTO_DELETE:
					switch (biMaps.size()) {
					case 1:
						imageDeal(0);
						img_shop_introduce1
								.setImageResource(R.mipmap.add_connect_goods_ing);
						img_shop_introduce2.setVisibility(View.GONE);
						break;

					case 2:
						if (img_flag == 1) {
							imageDeal(0);
							img_shop_introduce1.setImageBitmap(biMaps.get(0));
							img_shop_introduce2
									.setImageResource(R.mipmap.add_connect_goods_ing);
							img_shop_introduce3.setVisibility(View.GONE);
						} else if (img_flag == 2) {
							imageDeal(1);
							img_shop_introduce2
									.setImageResource(R.mipmap.add_connect_goods_ing);
							img_shop_introduce3.setVisibility(View.GONE);
						}
						break;
					case 3:
						if (img_flag == 1) {
							imageDeal(0);
							img_shop_introduce1.setImageBitmap(biMaps.get(0));
							img_shop_introduce2.setImageBitmap(biMaps.get(1));
							img_shop_introduce3
									.setImageResource(R.mipmap.add_connect_goods_ing);
						} else if (img_flag == 2) {
							imageDeal(1);
							img_shop_introduce1.setImageBitmap(biMaps.get(0));
							img_shop_introduce2.setImageBitmap(biMaps.get(1));
							img_shop_introduce3
									.setImageResource(R.mipmap.add_connect_goods_ing);
						} else if (img_flag == 3) {
							imageDeal(2);
							img_shop_introduce3
									.setImageResource(R.mipmap.add_connect_goods_ing);
						}
						break;
					}
					break;
				}
			}
		}
	}

	private void imageDeal(int position) {
		try {
			imagesDel.add(images.get(position));
			files.remove(position);
		} catch (Exception e2) {
		}
		try {
			files.remove(position);
		} catch (Exception e2) {
		}
		biMaps.remove(position);
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
			defaultImageDescribe.setOutputX(480);
			defaultImageDescribe.setOutputY(360);
			defaultImageDescribe.setAspectX(4);
			defaultImageDescribe.setAspectY(3);
			defaultImageDescribe.setOutputFormat(DEFAULT_IMG_FORMAT);
			return defaultImageDescribe;
		}
	}
}
