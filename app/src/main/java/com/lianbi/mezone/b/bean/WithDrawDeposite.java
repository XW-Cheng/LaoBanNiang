package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class WithDrawDeposite implements Serializable {

	private static final long serialVersionUID = 6092270372321487489L;
	String amount;
	String checkStatus;
	
	String outOrderId;
	String bankName;
	String createTime;
	String bankAccountNo;
	String accountNo;
	String storeNo;
	String id;
	
	
	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
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

	public String getStatus() {
		return checkStatus;
	}

	public void setStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getBank_w_id() {
		return outOrderId;
	}

	public void setBank_w_id(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getBanknum() {
		return bankAccountNo;
	}

	public void setBanknum(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}


}
