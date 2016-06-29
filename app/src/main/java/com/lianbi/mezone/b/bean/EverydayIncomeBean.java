package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class EverydayIncomeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4519468895364944467L;

	String accountNo;
	String amount;
	String createTime;
	String outerOrderId;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
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

}
