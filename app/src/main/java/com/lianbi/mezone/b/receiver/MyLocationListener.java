package com.lianbi.mezone.b.receiver;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

public class MyLocationListener implements BDLocationListener {
	Context context;
	private LocationClient mLocationClient;
	private BDLocation_interface bdLocation_interface;

	public MyLocationListener(Context context, LocationClient mLocationClient,
			BDLocation_interface bdLocation_interface) {
		super();
		this.context = context;
		this.mLocationClient = mLocationClient;
		this.bdLocation_interface = bdLocation_interface;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		// 定位成功
		if (null != location
				&& location.getLocType() != BDLocation.TypeServerError) {
			mLocationClient.stop();
			double lng = location.getLongitude();
			double lat = location.getLatitude();
			String address = location.getAddrStr();
			if (bdLocation_interface != null) {
				bdLocation_interface.location(lng, lat, address);
			}
		} else {
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.requestLocation();
			} else {
				mLocationClient.start();
			}
		}
	}

}
