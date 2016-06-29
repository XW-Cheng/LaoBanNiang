package com.lianbi.mezone.b.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;
import cn.com.hgh.utils.AbDateUtil;
import cn.com.hgh.utils.AbStrUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.Result;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.app.Constants;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

public class MapActivity extends BaseActivity {

	private MapView mapView;

	private BaiduMap baiduMap;

	BitmapDescriptor bitmap;

	MarkerOptions markerOption;

	/**
	 * 地图标注的点经纬度
	 */
	private LatLng selectedLatLng;
	/**
	 * 经度，纬度
	 */
	private double lng, lat;

	private GeoCoder mSearch;

	private EditText edt_map_location;
	private TextView tv_map_lng, tv_map_lat, tv_add_shop_summbit;
	private ImageView iv_map_delete;
	private String address;
	/**
	 * 是否修改地址
	 */
	boolean isUpdte = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplication());
		setContentView(R.layout.act_map, NOTYPE);
		isUpdte = getIntent().getBooleanExtra("isUpdte", false);
		initView();
		setLisenter();
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		iv_map_delete.setOnClickListener(this);
		tv_add_shop_summbit.setOnClickListener(this);
		edt_map_location.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					iv_map_delete.setVisibility(View.VISIBLE);
				} else {
					iv_map_delete.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("商铺地址");
		mapView = (MapView) this.findViewById(R.id.mapview);
		edt_map_location = (EditText) findViewById(R.id.edt_map_location);
		tv_map_lng = (TextView) findViewById(R.id.tv_map_lng);
		tv_map_lat = (TextView) findViewById(R.id.tv_map_lat);
		iv_map_delete = (ImageView) findViewById(R.id.iv_map_delete);
		tv_add_shop_summbit = (TextView) findViewById(R.id.tv_add_shop_summbit);
		baiduMap = mapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.pin_red_map);
		if (isUpdte) {
			lat = getIntent().getDoubleExtra("latitude", -1);
			lng = getIntent().getDoubleExtra("longitude", -1);
			address = getIntent().getStringExtra("address");
		} else {
			try {

				lat = Double.parseDouble(ContentUtils.getSharePreString(this,
						Constants.SHARED_PREFERENCE_NAME, Constants.LATITUDE));
				lng = Double.parseDouble(ContentUtils.getSharePreString(this,
						Constants.SHARED_PREFERENCE_NAME, Constants.LONGITUDE));
				address = ContentUtils.getSharePreString(this,
						Constants.SHARED_PREFERENCE_NAME, Constants.ADDRESS);
			} catch (Exception e) {
			}
		}
		if (lat != 0 && lng != 0) {
			selectedLatLng = new LatLng(lat, lng);
			initMark();
		}
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				selectedLatLng = arg0;
				lat = arg0.latitude;
				lng = arg0.longitude;
				tv_map_lng.setText(lng + "");
				tv_map_lat.setText(lat + "");
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(selectedLatLng));
				baiduMap.clear();
				markerOption = new MarkerOptions().position(selectedLatLng)
						.icon(bitmap);
				baiduMap.addOverlay(markerOption);
			}
		});
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(listener);
		tv_map_lng.setText(lng + "");
		tv_map_lat.setText(lat + "");
		edt_map_location.setText(address);

		mapView.showZoomControls(false);//隐藏放大缩小按钮
		mapView.showScaleControl(false);//隐藏比例尺
		//隐藏百度logo 地图正常显示 但是定位地址无法获得 得手动输入地址
		//View child = mapView.getChildAt(1);
		//if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
		//	child.setVisibility(View.INVISIBLE);
		//}

		/*
		// 隐藏放大缩小按钮
		int childCount = mapView.getChildCount();

		View zoom = null;

		for (int i = 0; i < childCount; i++) {

			View child = mapView.getChildAt(i);

			if (child instanceof ZoomControls) {

				zoom = child;

				break;

			}

		}

		zoom.setVisibility(View.GONE);
*/
	}

	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有检索到结果
			} else {
			}
			// 获取地理编码结果
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				// 没有找到检索结果
			} else {
				LatLng llg = result.getLocation();
				selectedLatLng = llg;
				lat = llg.latitude;
				lng = llg.longitude;
				tv_map_lng.setText(lng + "");
				tv_map_lat.setText(lat + "");
				address = result.getAddress();
				edt_map_location.setText(address);
			}
			// 获取反向地理编码结果
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		mSearch.destroy();
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.tv_add_shop_summbit:
			final String address1 = edt_map_location.getText().toString()
					.trim();
			final Intent intent = new Intent();
			if (isUpdte) {
				if (!TextUtils.isEmpty(address1)) {
					String reqTime = AbDateUtil.getDateTimeNow();
					String uuid = AbStrUtil.getUUID();
					try {
						okHttpsImp.updateBusinessAddress(uuid, "app", reqTime,
								userShopInfoBean.getBusinessId(), address1,
								new MyResultCallback<String>() {

									@Override
									public void onResponseResult(Result result) {
										ContentUtils.showMsg(MapActivity.this,
												"店铺地址修改成功");
										intent.putExtra("address", address1);
										intent.putExtra("lat", lat);
										intent.putExtra("lng", lng);
										setResult(RESULT_OK, intent);
										finish();
									}

									@Override
									public void onResponseFailed(String msg) {

										ContentUtils.showMsg(MapActivity.this,
												"店铺地址修改失败");
									}
								});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
				}

			} else {
				if (!TextUtils.isEmpty(address1)
						&& !TextUtils.isEmpty(lat + "")
						&& !TextUtils.isEmpty(lng + "")) {
					intent.putExtra("address", address1);
					intent.putExtra("lat", lat + "");
					intent.putExtra("lng", lng + "");
					setResult(RESULT_OK, intent);
					finish();
				} else {
					finish();
				}
			}

			break;

		case R.id.iv_map_delete:
			edt_map_location.setText("");
			break;
		}
	}

	private void initMark() {
		baiduMap.clear();
		markerOption = new MarkerOptions().position(selectedLatLng)
				.icon(bitmap);
		baiduMap.addOverlay(markerOption);
		MapStatus status = new MapStatus.Builder().target(selectedLatLng)
				.zoom(14).build();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(status);
		baiduMap.setMapStatus(mapStatusUpdate);
	}

}
