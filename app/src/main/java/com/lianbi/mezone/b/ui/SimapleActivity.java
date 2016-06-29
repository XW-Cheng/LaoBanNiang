package com.lianbi.mezone.b.ui;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.PagerIndicator;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;
import cn.com.hgh.view.AbPullToRefreshView;
import cn.com.hgh.view.CircularImageView;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Test;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

@SuppressLint("ResourceAsColor")
public class SimapleActivity extends BaseActivity implements
		OnSliderClickListener {

	TextView tv;
	AbPullToRefreshView abpulltorefreshview;
	ListView listView;
	CircularImageView selectableroundedimageview;

	ArrayList<Test> mDatas = new ArrayList<Test>();

	MyPhotoUtills photoUtills;
	private SliderLayout mDemoSlider;
	LinearLayout adeslltview_llt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		photoUtills = new MyPhotoUtills(this);

		setContentView(R.layout.simaplelayout, HAVETYPE);

		initView();

		tv.setOnClickListener(this);
		// for (int i = 0; i < 100; i++) {
		// mDatas.add(new Test("hgh" + i));
		// }
		// initListAdapter();
		// AbViewUtil.setListViewHeight(listView);
	}

	private void initView() {

		initAdesView();

		tv = (TextView) findViewById(R.id.tv);
		abpulltorefreshview = (AbPullToRefreshView) findViewById(R.id.abpulltorefreshview);
		listView = (ListView) findViewById(R.id.listView);
		selectableroundedimageview = (CircularImageView) findViewById(R.id.selectableroundedimageview);
		tv.setText("hgh");
		setPageTitle("one");
	}

	/**
	 * 初始化广告视图
	 */
	private void initAdesView() {
		mDemoSlider = (SliderLayout) findViewById(R.id.adeslltview_siderlayout);
		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
		mDemoSlider
				.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
		adeslltview_llt = (LinearLayout) findViewById(R.id.adeslltview_llt);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenWidth / 4);
		adeslltview_llt.setLayoutParams(params);
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
		selectableroundedimageview.setImageBitmap(bm);
		String pStr = Picture_Base64.GetImageStr(photoCurrentFile.toString());
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

	QuickAdapter<Test> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<Test>(this, R.layout.base_layout_title,
				mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper, Test item) {
				helper.setText(R.id.tv_title_center, item.getName());
				// ImageView img = helper
				// .getView(R.id.regulardemandmanagementactivity_list_item_iv);
				// img.setScaleType(ScaleType.CENTER_INSIDE);
				// LinearLayout.LayoutParams layoutParams = (LayoutParams) img
				// .getLayoutParams();
				// layoutParams.height = DevUtils.dip2Pix(50);
				// layoutParams.width = DevUtils.dip2Pix(50);
				// img.setImageResource(R.drawable.service_new_right);
				// TextView regulardemandmanagementactivity_list_item_yearlv =
				// helper
				// .getView(R.id.regulardemandmanagementactivity_list_item_yearlv);
				// helper.setText(
				// R.id.regulardemandmanagementactivity_list_item_name,
				// item.getName());
				// helper.setText(
				// R.id.regulardemandmanagementactivity_list_item_dealline,
				// item.getDeadline() + "个月");
				// String price = item.getRate() + "";
				// String two = price.substring(0, price.indexOf("."));
				// String three = price.substring(price.indexOf("."),
				// price.length());
				// SpannableuUtills.setSpannableu(
				// regulardemandmanagementactivity_list_item_yearlv, "",
				// two, three + "%");
			}
		};
		// 设置适配器
		listView.setAdapter(mAdapter);
		// AbViewUtil.setListViewHeight(listView);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.tv:
			photoUtills.pickImage();
			// startActivity(new Intent(this, SActivity.class));
			break;
		}

	}

	@Override
	public void onSliderClick(BaseSliderView slider) {

	}

}
