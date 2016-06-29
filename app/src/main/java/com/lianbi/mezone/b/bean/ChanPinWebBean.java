package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 产品发布
 * 
 * @author guanghui.han
 * 
 */
public class ChanPinWebBean implements Serializable {
	private static final long serialVersionUID = 3940360382267206122L;

	String serviceType = " ";// 发布类型
	String timeType;// 时间类型
	String startTime;// 开始时间
	String endTime;// 结束时间
	String isPay;// 是否预订
	String type = " ";// 产品类型
	String productName;// 服务名称
	String detail;// 服务详情
	String price = "";// 价格
	String unit = "";// 单位
	boolean status;// 是否有异常数据 false:显示msg true 不显示
	String msg; // 错误信息

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
