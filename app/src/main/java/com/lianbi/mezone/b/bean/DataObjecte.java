package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class DataObjecte implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6714667391761870608L;
	String orderStatus;
	String storeId;
	String flag;
	String sourceType;

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

}
