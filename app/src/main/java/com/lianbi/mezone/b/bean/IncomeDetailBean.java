package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class IncomeDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2967196502814530477L;

	String orderStatus;
	int amount;
	String createTime;
	String outerOrderId;

	
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOuterOrderId() {
		return outerOrderId;
	}

	public void setOuterOrderId(String outerOrderId) {
		this.outerOrderId = outerOrderId;
	}

	public String getStatus() {
		return orderStatus;
	}

	public void setStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getPayAmt() {
		return amount;
	}

	public void setPayAmt(int amount) {
		this.amount = amount;
	}

	public String getPayTime() {
		return createTime;
	}

	public void setPayTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderId() {
		return outerOrderId;
	}

	public void setOrderId(String outerOrderId) {
		this.outerOrderId = outerOrderId;
	}

}
