package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class DingdanInfoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2967196502814530477L;

	String orderStatus;
	int txnAmt;
	String createTime;
	String orderNo;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(int txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getStatus() {
		return orderStatus;
	}

	public void setStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getPayAmt() {
		return txnAmt;
	}

	public void setPayAmt(int txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getPayTime() {
		return createTime;
	}

	public void setPayTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderId() {
		return orderNo;
	}

	public void setOrderId(String orderNo) {
		this.orderNo = orderNo;
	}

}
