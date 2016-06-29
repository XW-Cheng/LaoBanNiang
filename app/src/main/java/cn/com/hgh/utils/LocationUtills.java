package cn.com.hgh.utils;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lianbi.mezone.b.receiver.BDLocation_interface;
import com.lianbi.mezone.b.receiver.MyLocationListener;

/**
 * 定位设置工具
 * 
 * @author guanghui.han
 * 
 */
public class LocationUtills {
	/**
	 * 初始化位置设置
	 */

	public static void initLocationClient(Context ct,
			BDLocation_interface bdLocation_interface) {
		LocationClient mLocationClient = new LocationClient(ct); // 声明LocationClient类
		MyLocationListener myListener = new MyLocationListener(ct,
				mLocationClient, bdLocation_interface);
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true);
		// option.setAddrType("all");// 返回的定位结果包含地址信息
		// option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		// option.setScanSpan(30000);// 设置发起定位请求的间隔时间为5000ms
		// option.disableCache(true);// 禁止启用缓存定位
		// option.setPoiNumber(5); // 最多返回POI个数
		// option.setPoiDistance(1000); // poi查询距离
		// option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
		option.setScanSpan(3000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setIsNeedLocationDescribe(true);// 可选，设置是否需要地址描述
		option.setNeedDeviceDirect(false);// 可选，设置是否需要设备方向结果
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		// 请求定位
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}
}
