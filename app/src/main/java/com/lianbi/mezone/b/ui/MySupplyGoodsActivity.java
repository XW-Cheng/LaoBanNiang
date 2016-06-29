package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.PagerIndicator;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.playview.imp.TextSliderView;
import cn.com.hgh.utils.Result;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Ades_ImageEs;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 我的货源
 * 
 * @time 上午11:12:23
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class MySupplyGoodsActivity extends BaseActivity implements
		OnSliderClickListener {
	private LinearLayout llt_my_supplygoods_connect,
			llt_my_supplygoods_publish, llt_my_supplygoods_record,
			llt_my_supplygoods_mall, adeslltview_llt;
	private SliderLayout sliderLayout;
	ProgressBar adeslltview_siderlayout_progressBar;

	/**
	 * 广告
	 */
	private ArrayList<Ades_ImageEs> ades_ImageEs = new ArrayList<Ades_ImageEs>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_supply_goods, HAVETYPE);
		initView();
		initAdesView();
		setLisenter();
		getAadver();
	}

	@Override
	protected void onTitleLeftClick() {
		setResult(RESULT_OK);
		super.onTitleLeftClick();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		llt_my_supplygoods_connect.setOnClickListener(this);
		llt_my_supplygoods_publish.setOnClickListener(this);
		llt_my_supplygoods_record.setOnClickListener(this);
		llt_my_supplygoods_mall.setOnClickListener(this);
	}

	/**
	 * 获取广告
	 */
	public void getAadver() {
//		okHttpsImp.getAdvert("1", new MyResultCallback<String>() {
//
//			@Override
//			public void onResponseResult(Result result) {
//				sliderLayout.removeAllSliders();
//				String resString = result.getData();
//				try {
//					JSONObject jsonObject = new JSONObject(resString);
//					resString = jsonObject.getString("advertList");
//					ades_ImageEs = (ArrayList<Ades_ImageEs>) JSON.parseArray(
//							resString, Ades_ImageEs.class);
//					if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
//						for (int i = 0; i < ades_ImageEs.size(); i++) {
//							TextSliderView textSliderView = new TextSliderView(
//									MySupplyGoodsActivity.this, i,5);
//							textSliderView
//									.image(ades_ImageEs.get(i).getImage())
//									.error(R.drawable.adshouye);
//							textSliderView
//									.setOnSliderClickListener(MySupplyGoodsActivity.this);
//							sliderLayout.addSlider(textSliderView);
//						}
//					} else {
//						for (int i = 0; i < 3; i++) {
//							TextSliderView textSliderView = new TextSliderView(
//									MySupplyGoodsActivity.this, i,5);
//							textSliderView.image(R.drawable.adshouye);
//							textSliderView
//									.setOnSliderClickListener(MySupplyGoodsActivity.this);
//							sliderLayout.addSlider(textSliderView);
//						}
//					}
//					sliderLayout
//							.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
//					adeslltview_siderlayout_progressBar
//							.setVisibility(View.GONE);
//					sliderLayout.setVisibility(View.VISIBLE);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onResponseFailed(String msg) {
//				sliderLayout.removeAllSliders();
//				for (int i = 0; i < 3; i++) {
//					TextSliderView textSliderView = new TextSliderView(
//							MySupplyGoodsActivity.this, i,5);
//					textSliderView.image(R.drawable.adshouye);
//					textSliderView
//							.setOnSliderClickListener(MySupplyGoodsActivity.this);
//					sliderLayout.addSlider(textSliderView);
//				}
//				sliderLayout
//						.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
//				adeslltview_siderlayout_progressBar.setVisibility(View.GONE);
//				sliderLayout.setVisibility(View.VISIBLE);
//			}
//		});
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("我的货源");
		llt_my_supplygoods_connect = (LinearLayout) findViewById(R.id.llt_my_supplygoods_connect);
		llt_my_supplygoods_publish = (LinearLayout) findViewById(R.id.llt_my_supplygoods_publish);
		llt_my_supplygoods_record = (LinearLayout) findViewById(R.id.llt_my_supplygoods_record);
		llt_my_supplygoods_mall = (LinearLayout) findViewById(R.id.llt_my_supplygoods_mall);
	}

	/**
	 * 初始化广告视图
	 */
	private void initAdesView() {
		sliderLayout = (SliderLayout) findViewById(R.id.adeslltview_siderlayout);
		adeslltview_siderlayout_progressBar = (ProgressBar) findViewById(R.id.adeslltview_siderlayout_progressBar);
		sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
		adeslltview_llt = (LinearLayout) findViewById(R.id.adeslltview_llt);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenWidth / 4);
		adeslltview_llt.setLayoutParams(params);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.llt_my_supplygoods_connect:// 联系货源
			Intent intent_connect = new Intent(this, ConnectGoodsActivity.class);
			startActivity(intent_connect);
			break;

		case R.id.llt_my_supplygoods_publish:// 发布货源
			Intent intent_Publish = new Intent(this,
					PublishSupplyGoodsActivity.class);
			startActivity(intent_Publish);
			break;
		case R.id.llt_my_supplygoods_record:// 下单记录
			Intent intent_record = new Intent(this,
					SupplyGoodsRecordActivity.class);
			startActivity(intent_record);
			break;
		case R.id.llt_my_supplygoods_mall:// 货源商城
			Intent intent_goods_mall = new Intent(this, GoodsMallActivity.class);
			startActivity(intent_goods_mall);
			break;
		}
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
			String url = ades_ImageEs.get(slider.getP()).getUrl();
			Intent intent = new Intent(this, WebActivty.class);
			intent.putExtra(WebActivty.T, ades_ImageEs.get(slider.getP())
					.getName());
			intent.putExtra(WebActivty.U, url);
			intent.putExtra("Re", true);
			startActivity(intent);
		}
	}

}
