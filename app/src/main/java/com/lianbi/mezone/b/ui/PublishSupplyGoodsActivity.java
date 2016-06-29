package com.lianbi.mezone.b.ui;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.hgh.timeselector.TimeSelectorE;
import cn.com.hgh.timeselector.TimeSelectorE.MODE;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.FilePathGet;
import cn.com.hgh.utils.Picture_Base64;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.ScrollerUtills;
import cn.com.hgh.view.MyContainsViewJJ;
import cn.com.hgh.view.MyContainsViewJJ.ImOnclick;
import cn.com.hgh.view.MyContainsViewJJ.UnitChange;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.IndustryListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;
import com.lianbi.mezone.b.photo.FileUtils;
import com.lianbi.mezone.b.photo.PhotoUtills;
import com.lianbi.mezone.b.photo.PickImageDescribe;

/**
 * 货源发布
 * 
 * @author hongyu.yang
 * 
 */
public class PublishSupplyGoodsActivity extends BaseActivity {

	LinearLayout llt_publish_goods_shuliangandjiage_contain,
			llt_publish_goods_img, llt_publish_goods_leibie,
			llt_publish_goods_time, llt_publish_goods_owner_time,
			llt_publish_goods_open_time, llt_publish_goods_over_time;
	/**
	 * 视图位置
	 */
	protected int p;
	ScrollView pubgoods_scroll;
	MyContainsViewJJ child1, child2, child3, child4, child5,
			mycontainsviewjj_anima;
	ImageView img_goodes_publish_introduce;
	TextView tv_publish_goods_leibie, tv_publish_goods_time,
			tv_publish_goods_open_time, tv_publish_goods_over_time,
			tv_publish_goods_bottom;
	EditText et_publish_goods_name, et_publish_goods_desition;
	private PopupWindow popFilter;
	private View filterWindow;
	private MyPhotoUtills photoUtills;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_publishsupplygoodsactivity, NOTYPE);
		photoUtills = new MyPhotoUtills(this);
		sDay = AbDateUtil.getDate(0);
		eDay = AbDateUtil.getDate(6);
		initView();
		initPopView();
		initFilterPopupWindow();

		Listener();
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
					Bitmap bm = PhotoUtills.getBitmap(180, 90);
					// File file = FilePathGet.saveBitmap(bm);
					File file = photoUtills.photoCurrentFile;
					onPickedPhoto(file, bm);
					return;
				}
				if (requestCode == REQUEST_TYPE) {
					IndustryListBean curBean = (IndustryListBean) data
							.getSerializableExtra("bean");
					child_id = curBean.getMajor_id() + "";
					child_name = curBean.getMaj_name();
					tv_publish_goods_leibie.setText(child_name);
					return;
				}
			}
		}
	}

	private void onPickedPhoto(final File photoCurrentFile, Bitmap bm) {
		img_goodes_publish_introduce.setImageBitmap(bm);
		img_goodes_publish_introduce.setTag(photoCurrentFile);

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
			defaultImageDescribe.setOutputX(500);
			defaultImageDescribe.setOutputY(250);
			defaultImageDescribe.setAspectX(2);
			defaultImageDescribe.setAspectY(1);
			defaultImageDescribe.setOutputFormat(DEFAULT_IMG_FORMAT);
			return defaultImageDescribe;
		}
	}

	/**
	 * 初始化pop
	 */
	private void initFilterPopupWindow() {
		popFilter = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		popFilter.setContentView(filterWindow);
		popFilter.setFocusable(true);
		popFilter.setOutsideTouchable(true);
		popFilter.setAnimationStyle(R.style.slide_up_in_down_out);
		popFilter.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});

	}

	private final String one1 = "1周内";
	private final String one2 = "1月内";
	private final String one3 = "1年内";
	private final String one4 = "长期有效";
	private final String one5 = "自定义时间";
	private final int REQUEST_TYPE = 3000;
	private final String ENDTIME = "2030-01-01 00:00";
	/**
	 * 开始时间
	 */
	String sDay = "";
	/**
	 * 结束时间
	 */
	String eDay = "";

	/**
	 * 是否是自定义时间
	 */
	boolean isSelf = false;
	/**
	 * 是否长期
	 */
	protected boolean isLong = false;
	/**
	 * 子行业id
	 */
	private String child_id;
	private String child_name;

	/**
	 * 初始化pop view
	 */
	private void initPopView() {

		filterWindow = View.inflate(this,
				R.layout.window_filter_transactionmanagement, null);
		((LinearLayout) filterWindow).setGravity(Gravity.BOTTOM);
		final Button pop_tma_today;
		final Button pop_tma_three;
		final Button pop_tma_seven;
		final Button pop_tma_month;
		final Button pop_tma_owner_time;
		pop_tma_today = (Button) filterWindow.findViewById(R.id.pop_tma_today);
		pop_tma_three = (Button) filterWindow.findViewById(R.id.pop_tma_three);
		pop_tma_seven = (Button) filterWindow.findViewById(R.id.pop_tma_seven);
		pop_tma_month = (Button) filterWindow.findViewById(R.id.pop_tma_month);
		pop_tma_today.setText(one1);
		pop_tma_three.setText(one2);
		pop_tma_seven.setText(one3);
		pop_tma_month.setText(one4);
		pop_tma_owner_time = (Button) filterWindow
				.findViewById(R.id.pop_tma_owner_time);

		pop_tma_today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
				sDay = AbDateUtil.getDate(0);
				eDay = AbDateUtil.getDate(6);
				isSelf = false;
				tv_publish_goods_time.setText(one1);
				llt_publish_goods_owner_time.setVisibility(View.GONE);
				pop_tma_today.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_three.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
			}
		});
		pop_tma_three.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sDay = AbDateUtil.getDate(0);
				eDay = AbDateUtil.getDate(30);
				isSelf = false;
				popFilter.dismiss();
				llt_publish_goods_owner_time.setVisibility(View.GONE);
				tv_publish_goods_time.setText(one2);
				pop_tma_today.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_seven.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
			}
		});
		pop_tma_seven.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sDay = AbDateUtil.getDate(0);
				eDay = AbDateUtil.getDate(365);
				isSelf = false;
				popFilter.dismiss();
				llt_publish_goods_owner_time.setVisibility(View.GONE);
				tv_publish_goods_time.setText(one3);
				pop_tma_today.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_month.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
			}
		});
		pop_tma_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sDay = AbDateUtil.getDate(0);
				eDay = AbDateUtil.getDate(365 * 5);
				isSelf = false;
				popFilter.dismiss();
				llt_publish_goods_owner_time.setVisibility(View.GONE);
				tv_publish_goods_time.setText(one4);
				pop_tma_today.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.colores_news_01));
				pop_tma_owner_time.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
			}
		});
		pop_tma_owner_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isSelf = true;
				popFilter.dismiss();
				llt_publish_goods_owner_time.setVisibility(View.VISIBLE);
				tv_publish_goods_time.setText(one5);
				pop_tma_today.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_three.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_seven.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_month.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.color_c6c6c6));
				pop_tma_owner_time.setTextColor(PublishSupplyGoodsActivity.this
						.getResources().getColor(R.color.colores_news_01));
			}
		});
		filterWindow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popFilter.dismiss();
			}
		});

	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.tv_publish_goods_bottom:// 发布
			publishGO();
			break;
		case R.id.llt_publish_goods_img:// 图片上传
			photoUtills.pickImage();
			break;
		case R.id.llt_publish_goods_leibie:// 货源类别
			Intent intent_type = new Intent(this, SelectTypeActivity.class);
			intent_type.putExtra("isMyShop", false);
			startActivityForResult(intent_type, REQUEST_TYPE);
			break;
		case R.id.llt_publish_goods_time:// 有效时间
			popFilter.showAtLocation(getWindow().getDecorView(),
					Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
			break;
		case R.id.llt_publish_goods_open_time:// 起始时间
			TimeSelectorE timeSelector = new TimeSelectorE(this,
					new TimeSelectorE.ResultHandler() {
						@Override
						public void handle(String time) {
							tv_publish_goods_open_time.setText(time);
						}
					}, AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHM),
					ENDTIME);
			timeSelector.setMode(MODE.YMD);
			timeSelector.setTitle("起始时间");
			timeSelector.show();
			break;
		case R.id.llt_publish_goods_over_time:// 结束时间
			TimeSelectorE timeSelectorO = new TimeSelectorE(this,
					new TimeSelectorE.ResultHandler() {
						@Override
						public void handle(String time) {
							tv_publish_goods_over_time.setText(time);
						}
					}, AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHM),
					ENDTIME);
			timeSelectorO.setMode(MODE.YMD);
			timeSelectorO.setTitle("结束时间");
			timeSelectorO.show();
			break;
		}
	}

	/**
	 * 发布货源
	 */
	private void publishGO() {
		File cFile = (File) img_goodes_publish_introduce.getTag();
		String imageStr = null;
		if (cFile != null) {
			imageStr = Picture_Base64.GetImageStr(cFile.toString());
		}
		String name = et_publish_goods_name.getText().toString().trim();
		String desition = et_publish_goods_desition.getText().toString().trim();
		if (isSelf) {
			sDay = tv_publish_goods_open_time.getText().toString().trim();
			eDay = tv_publish_goods_over_time.getText().toString().trim();
		}
		if (cFile == null) {
			ContentUtils.showMsg(this, "请选择图片");
			return;
		}
		if (TextUtils.isEmpty(name)) {
			ContentUtils.showMsg(this, "货源名称不能为空");
			return;
		}
		if (TextUtils.isEmpty(desition)) {
			ContentUtils.showMsg(this, "货源描述不能为空");
			return;
		}
		if (TextUtils.isEmpty(child_id)) {
			ContentUtils.showMsg(this, "行业类型不能为空");
			return;
		}
		if (!TextUtils.isEmpty(eDay)) {

			if (!AbDateUtil.compareTime(sDay, eDay)) {
				ContentUtils.showMsg(this, "起始时间不能大于结束时间");
				return;
			}
		}
		String unit = "";
		String price = "";

		// 价格
		switch (p) {
		case 0: {
			String num1 = child1.getEt_publish_goods_num1();
			String nU1 = child1.getEt_publish_goods_num_unit1();
			String p1 = child1.getEt_publish_goods_price1();
			String pU1 = child1.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num1)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU1)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p1)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU1)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			unit = num1 + "-" + nU1;
			price = p1 + "-" + pU1;
		}
			break;
		case 1: {
			String num1 = child1.getEt_publish_goods_num1();
			String nU1 = child1.getEt_publish_goods_num_unit1();
			String p1 = child1.getEt_publish_goods_price1();
			String pU1 = child1.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num1)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU1)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p1)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU1)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num2 = child2.getEt_publish_goods_num1();
			String nU2 = child2.getEt_publish_goods_num_unit1();
			String p2 = child2.getEt_publish_goods_price1();
			String pU2 = child2.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num2)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU2)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p2)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU2)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			unit = num1 + "-" + nU1 + "," + num2 + "-" + nU2;
			price = p1 + "-" + pU1 + "," + p2 + "-" + pU2;
		}
			break;
		case 2: {
			String num1 = child1.getEt_publish_goods_num1();
			String nU1 = child1.getEt_publish_goods_num_unit1();
			String p1 = child1.getEt_publish_goods_price1();
			String pU1 = child1.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num1)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU1)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p1)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU1)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num2 = child2.getEt_publish_goods_num1();
			String nU2 = child2.getEt_publish_goods_num_unit1();
			String p2 = child2.getEt_publish_goods_price1();
			String pU2 = child2.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num2)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU2)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p2)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU2)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num3 = child3.getEt_publish_goods_num1();
			String nU3 = child3.getEt_publish_goods_num_unit1();
			String p3 = child3.getEt_publish_goods_price1();
			String pU3 = child3.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num3)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU3)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p3)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU3)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			unit = num1 + "-" + nU1 + "," + num2 + "-" + nU2 + "," + num3 + "-"
					+ nU3;
			price = p1 + "-" + pU1 + "," + p2 + "-" + pU2 + "," + p3 + "-"
					+ pU3;
		}
			break;
		case 3: {
			String num1 = child1.getEt_publish_goods_num1();
			String nU1 = child1.getEt_publish_goods_num_unit1();
			String p1 = child1.getEt_publish_goods_price1();
			String pU1 = child1.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num1)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU1)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p1)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU1)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num2 = child2.getEt_publish_goods_num1();
			String nU2 = child2.getEt_publish_goods_num_unit1();
			String p2 = child2.getEt_publish_goods_price1();
			String pU2 = child2.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num2)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU2)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p2)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU2)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num3 = child3.getEt_publish_goods_num1();
			String nU3 = child3.getEt_publish_goods_num_unit1();
			String p3 = child3.getEt_publish_goods_price1();
			String pU3 = child3.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num3)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU3)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p3)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU3)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num4 = child4.getEt_publish_goods_num1();
			String nU4 = child4.getEt_publish_goods_num_unit1();
			String p4 = child4.getEt_publish_goods_price1();
			String pU4 = child4.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num4)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU4)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p4)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU4)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			unit = num1 + "-" + nU1 + "," + num2 + "-" + nU2 + "," + num3 + "-"
					+ nU3 + "," + num4 + "-" + nU4;
			price = p1 + "-" + pU1 + "," + p2 + "-" + pU2 + "," + p3 + "-"
					+ pU3 + "," + p4 + "-" + pU4;
		}
			break;
		case 4: {

			String num1 = child1.getEt_publish_goods_num1();
			String nU1 = child1.getEt_publish_goods_num_unit1();
			String p1 = child1.getEt_publish_goods_price1();
			String pU1 = child1.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num1)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU1)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p1)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU1)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num2 = child2.getEt_publish_goods_num1();
			String nU2 = child2.getEt_publish_goods_num_unit1();
			String p2 = child2.getEt_publish_goods_price1();
			String pU2 = child2.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num2)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU2)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p2)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU2)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num3 = child3.getEt_publish_goods_num1();
			String nU3 = child3.getEt_publish_goods_num_unit1();
			String p3 = child3.getEt_publish_goods_price1();
			String pU3 = child3.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num3)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU3)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p3)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU3)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num4 = child4.getEt_publish_goods_num1();
			String nU4 = child4.getEt_publish_goods_num_unit1();
			String p4 = child4.getEt_publish_goods_price1();
			String pU4 = child4.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num4)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU4)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p4)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU4)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			String num5 = child5.getEt_publish_goods_num1();
			String nU5 = child5.getEt_publish_goods_num_unit1();
			String p5 = child5.getEt_publish_goods_price1();
			String pU5 = child5.getEt_publish_goods_price_unit1();
			if (TextUtils.isEmpty(num5)) {
				ContentUtils.showMsg(this, "数量不能为空");
				return;
			}
			if (TextUtils.isEmpty(nU5)) {
				ContentUtils.showMsg(this, "数量单位不能为空");
				return;
			}
			if (TextUtils.isEmpty(p5)) {
				ContentUtils.showMsg(this, "价格不能为空");
				return;
			}
			if (TextUtils.isEmpty(pU5)) {
				ContentUtils.showMsg(this, "价格单位不能为空");
				return;
			}
			unit = num1 + "-" + nU1 + "," + num2 + "-" + nU2 + "," + num3 + "-"
					+ nU3 + "," + num4 + "-" + nU4 + "," + num5 + "-" + nU5;
			price = p1 + "-" + pU1 + "," + p2 + "-" + pU2 + "," + p3 + "-"
					+ pU3 + "," + p4 + "-" + pU4 + "," + p5 + "-" + pU5;
		}
			break;
		}
		okHttpsImp.addProductSource(imageStr, name, eDay, sDay, child_id, unit,
				price, userShopInfoBean.getBusinessId(), desition,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						ContentUtils.showMsg(PublishSupplyGoodsActivity.this,
								"货源发布成功");
						finish();
					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});

	}

	/**
	 * 加减监听
	 */
	private void Listener() {
		tv_publish_goods_bottom.setOnClickListener(this);
		llt_publish_goods_img.setOnClickListener(this);
		llt_publish_goods_leibie.setOnClickListener(this);
		llt_publish_goods_open_time.setOnClickListener(this);
		llt_publish_goods_time.setOnClickListener(this);
		llt_publish_goods_over_time.setOnClickListener(this);

		child1.setImOnclick(new ImOnclick() {

			@Override
			public void imOnclick(boolean isPlus) {
				if (isPlus) {
					switch (p) {
					case 1:
						child2.setVisibility(View.GONE);
						child1.setText(child2.getEt_publish_goods_num1(),
								child2.getEt_publish_goods_num_unit1(),
								child2.getEt_publish_goods_price1(),
								child2.getEt_publish_goods_price_unit1());
						child2.clear();
						child1.setImg_publish_goods_num1JJ();
						break;
					case 2:
						child3.setVisibility(View.GONE);
						child1.setText(child2.getEt_publish_goods_num1(),
								child2.getEt_publish_goods_num_unit1(),
								child2.getEt_publish_goods_price1(),
								child2.getEt_publish_goods_price_unit1());
						child2.setText(child3.getEt_publish_goods_num1(),
								child3.getEt_publish_goods_num_unit1(),
								child3.getEt_publish_goods_price1(),
								child3.getEt_publish_goods_price_unit1());
						child3.clear();
						child2.setImg_publish_goods_num1JJ();
						break;
					case 3:
						child4.setVisibility(View.GONE);
						child1.setText(child2.getEt_publish_goods_num1(),
								child2.getEt_publish_goods_num_unit1(),
								child2.getEt_publish_goods_price1(),
								child2.getEt_publish_goods_price_unit1());
						child2.setText(child3.getEt_publish_goods_num1(),
								child3.getEt_publish_goods_num_unit1(),
								child3.getEt_publish_goods_price1(),
								child3.getEt_publish_goods_price_unit1());
						child3.setText(child4.getEt_publish_goods_num1(),
								child4.getEt_publish_goods_num_unit1(),
								child4.getEt_publish_goods_price1(),
								child4.getEt_publish_goods_price_unit1());
						child4.clear();
						child3.setImg_publish_goods_num1JJ();
						break;
					case 4:
						child5.setVisibility(View.GONE);
						child1.setText(child2.getEt_publish_goods_num1(),
								child2.getEt_publish_goods_num_unit1(),
								child2.getEt_publish_goods_price1(),
								child2.getEt_publish_goods_price_unit1());
						child2.setText(child3.getEt_publish_goods_num1(),
								child3.getEt_publish_goods_num_unit1(),
								child3.getEt_publish_goods_price1(),
								child3.getEt_publish_goods_price_unit1());
						child3.setText(child4.getEt_publish_goods_num1(),
								child4.getEt_publish_goods_num_unit1(),
								child4.getEt_publish_goods_price1(),
								child4.getEt_publish_goods_price_unit1());
						child4.setText(child5.getEt_publish_goods_num1(),
								child5.getEt_publish_goods_num_unit1(),
								child5.getEt_publish_goods_price1(),
								child5.getEt_publish_goods_price_unit1());
						child5.clear();
						child4.setImg_publish_goods_num1JJ();
						break;
					}
					p--;
				} else {
					p++;
					child1.setImg_publish_goods_num1J();
					startAnima(child2);
				}
			}
		});
		child2.setImOnclick(new ImOnclick() {

			@Override
			public void imOnclick(boolean isPlus) {
				if (isPlus) {
					switch (p) {
					case 1:
						child2.setVisibility(View.GONE);
						child2.clear();
						child1.setImg_publish_goods_num1JJ();
						break;
					case 2:
						child3.setVisibility(View.GONE);
						child2.setText(child3.getEt_publish_goods_num1(),
								child3.getEt_publish_goods_num_unit1(),
								child3.getEt_publish_goods_price1(),
								child3.getEt_publish_goods_price_unit1());
						child3.clear();
						child2.setImg_publish_goods_num1JJ();
						break;
					case 3:
						child4.setVisibility(View.GONE);
						child2.setText(child3.getEt_publish_goods_num1(),
								child3.getEt_publish_goods_num_unit1(),
								child3.getEt_publish_goods_price1(),
								child3.getEt_publish_goods_price_unit1());
						child3.setText(child4.getEt_publish_goods_num1(),
								child4.getEt_publish_goods_num_unit1(),
								child4.getEt_publish_goods_price1(),
								child4.getEt_publish_goods_price_unit1());
						child4.clear();
						child3.setImg_publish_goods_num1JJ();
						break;
					case 4:
						child5.setVisibility(View.GONE);
						child2.setText(child3.getEt_publish_goods_num1(),
								child3.getEt_publish_goods_num_unit1(),
								child3.getEt_publish_goods_price1(),
								child3.getEt_publish_goods_price_unit1());
						child3.setText(child4.getEt_publish_goods_num1(),
								child4.getEt_publish_goods_num_unit1(),
								child4.getEt_publish_goods_price1(),
								child4.getEt_publish_goods_price_unit1());
						child4.setText(child5.getEt_publish_goods_num1(),
								child5.getEt_publish_goods_num_unit1(),
								child5.getEt_publish_goods_price1(),
								child5.getEt_publish_goods_price_unit1());
						child5.clear();
						child4.setImg_publish_goods_num1JJ();
						break;
					}
					p--;
				} else {
					p++;
					child2.setImg_publish_goods_num1J();
					startAnima(child3);
				}
			}
		});
		child3.setImOnclick(new ImOnclick() {

			@Override
			public void imOnclick(boolean isPlus) {
				if (isPlus) {
					switch (p) {
					case 2:
						child3.setVisibility(View.GONE);
						child3.clear();
						child2.setImg_publish_goods_num1JJ();
						break;
					case 3:
						child4.setVisibility(View.GONE);
						child3.setText(child4.getEt_publish_goods_num1(),
								child4.getEt_publish_goods_num_unit1(),
								child4.getEt_publish_goods_price1(),
								child4.getEt_publish_goods_price_unit1());
						child4.clear();
						child3.setImg_publish_goods_num1JJ();
						break;
					case 4:
						child5.setVisibility(View.GONE);
						child3.setText(child4.getEt_publish_goods_num1(),
								child4.getEt_publish_goods_num_unit1(),
								child4.getEt_publish_goods_price1(),
								child4.getEt_publish_goods_price_unit1());
						child4.setText(child5.getEt_publish_goods_num1(),
								child5.getEt_publish_goods_num_unit1(),
								child5.getEt_publish_goods_price1(),
								child5.getEt_publish_goods_price_unit1());
						child5.clear();
						child4.setImg_publish_goods_num1JJ();
						break;
					}
					p--;
				} else {
					p++;
					child3.setImg_publish_goods_num1J();
					startAnima(child4);
				}
			}
		});
		child4.setImOnclick(new ImOnclick() {

			@Override
			public void imOnclick(boolean isPlus) {
				if (isPlus) {
					switch (p) {
					case 3:
						child4.setVisibility(View.GONE);
						child4.clear();
						child3.setImg_publish_goods_num1JJ();
						break;
					case 4:
						child5.setVisibility(View.GONE);
						child4.setText(child5.getEt_publish_goods_num1(),
								child5.getEt_publish_goods_num_unit1(),
								child5.getEt_publish_goods_price1(),
								child5.getEt_publish_goods_price_unit1());
						child5.clear();
						child4.setImg_publish_goods_num1JJ();
						break;
					}
					p--;
				} else {
					p++;
					child4.setImg_publish_goods_num1J();
					child5.setImg_publish_goods_num1J();
					startAnima(child5);
				}
			}
		});
		child5.setImOnclick(new ImOnclick() {

			@Override
			public void imOnclick(boolean isPlus) {
				if (isPlus) {
					switch (p) {
					case 4:
						child4.setImg_publish_goods_num1JJ();
						child5.setVisibility(View.GONE);
						child5.clear();
						break;
					}
					p--;
				}
			}
		});
	}

	/**
	 * 启动动画
	 */
	public void startAnima(final MyContainsViewJJ child) {
		Animation am = new TranslateAnimation(0, 0, 0, screenHeight
				+ AbViewUtil.dip2px(this, 300));
		am.setDuration(650);
		am.setFillAfter(true);
		mycontainsviewjj_anima.startAnimation(am);
		am.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				child.setVisibility(View.VISIBLE);
				mycontainsviewjj_anima.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				ScrollerUtills.scrollerdown(pubgoods_scroll);
			}
		});
	}

	private void initView() {
		setPageTitle("货源发布");
		pubgoods_scroll = (ScrollView) findViewById(R.id.pubgoods_scroll);
		img_goodes_publish_introduce = (ImageView) findViewById(R.id.img_goodes_publish_introduce);
		llt_publish_goods_img = (LinearLayout) findViewById(R.id.llt_publish_goods_img);
		llt_publish_goods_leibie = (LinearLayout) findViewById(R.id.llt_publish_goods_leibie);
		llt_publish_goods_time = (LinearLayout) findViewById(R.id.llt_publish_goods_time);
		llt_publish_goods_owner_time = (LinearLayout) findViewById(R.id.llt_publish_goods_owner_time);
		llt_publish_goods_open_time = (LinearLayout) findViewById(R.id.llt_publish_goods_open_time);
		llt_publish_goods_over_time = (LinearLayout) findViewById(R.id.llt_publish_goods_over_time);
		tv_publish_goods_bottom = (TextView) findViewById(R.id.tv_publish_goods_bottom);
		tv_publish_goods_leibie = (TextView) findViewById(R.id.tv_publish_goods_leibie);
		tv_publish_goods_time = (TextView) findViewById(R.id.tv_publish_goods_time);
		tv_publish_goods_open_time = (TextView) findViewById(R.id.tv_publish_goods_open_time);
		tv_publish_goods_open_time.setText(AbDateUtil
				.getCurrentDate(AbDateUtil.dateFormatYMD));
		tv_publish_goods_over_time = (TextView) findViewById(R.id.tv_publish_goods_over_time);
		tv_publish_goods_over_time.setText(AbDateUtil
				.getCurrentDate(AbDateUtil.dateFormatYMD));
		et_publish_goods_desition = (EditText) findViewById(R.id.et_publish_goods_desition);
		et_publish_goods_name = (EditText) findViewById(R.id.et_publish_goods_name);
		mycontainsviewjj_anima = (MyContainsViewJJ) findViewById(R.id.mycontainsviewjj_anima);
		childEdit();

	}

	/**
	 * 价格单位设置
	 */
	private void childEdit() {
		child1 = (MyContainsViewJJ) findViewById(R.id.mycontainsviewjj1);
		child2 = (MyContainsViewJJ) findViewById(R.id.mycontainsviewjj2);
		child3 = (MyContainsViewJJ) findViewById(R.id.mycontainsviewjj3);
		child4 = (MyContainsViewJJ) findViewById(R.id.mycontainsviewjj4);
		child5 = (MyContainsViewJJ) findViewById(R.id.mycontainsviewjj5);
		child1.setUnitChange(new UnitChange() {

			@Override
			public void change(String unitC) {
				child2.setEt_publish_goods_num_unit1(unitC);
				child3.setEt_publish_goods_num_unit1(unitC);
				child4.setEt_publish_goods_num_unit1(unitC);
				child5.setEt_publish_goods_num_unit1(unitC);
			}
		});
		child2.setUnitChange(new UnitChange() {

			@Override
			public void change(String unitC) {
				child1.setEt_publish_goods_num_unit1(unitC);
				child3.setEt_publish_goods_num_unit1(unitC);
				child4.setEt_publish_goods_num_unit1(unitC);
				child5.setEt_publish_goods_num_unit1(unitC);
			}
		});
		child3.setUnitChange(new UnitChange() {

			@Override
			public void change(String unitC) {
				child2.setEt_publish_goods_num_unit1(unitC);
				child1.setEt_publish_goods_num_unit1(unitC);
				child4.setEt_publish_goods_num_unit1(unitC);
				child5.setEt_publish_goods_num_unit1(unitC);
			}
		});
		child4.setUnitChange(new UnitChange() {

			@Override
			public void change(String unitC) {
				child2.setEt_publish_goods_num_unit1(unitC);
				child3.setEt_publish_goods_num_unit1(unitC);
				child1.setEt_publish_goods_num_unit1(unitC);
				child5.setEt_publish_goods_num_unit1(unitC);
			}
		});
		child5.setUnitChange(new UnitChange() {

			@Override
			public void change(String unitC) {
				child2.setEt_publish_goods_num_unit1(unitC);
				child3.setEt_publish_goods_num_unit1(unitC);
				child4.setEt_publish_goods_num_unit1(unitC);
				child1.setEt_publish_goods_num_unit1(unitC);
			}
		});
	}

}
