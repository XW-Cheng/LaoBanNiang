package com.lianbi.mezone.b.receiver;

/**
 * 
 * @author guanghui.han 定位接口
 */
public interface BDLocation_interface {
	/**
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 */
	void location(double lng, double lat,String address);
}
