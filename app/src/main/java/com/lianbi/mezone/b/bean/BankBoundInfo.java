package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 银行卡bean
 * 
 * @author hongyu.yang
 * 
 */
public class BankBoundInfo implements Serializable {
	private static final long serialVersionUID = -3347221502267757210L;
	String userbankId;
	String bankName;
	String bankAccountNo;
	int id;
	String bankId;
	String ownername;
	String username;
	int bankid;

	String userId;
	String branchBankName;
	String cityCode;
	String phone;
	String boundCardId;
	String payPasswd;
	String areaCode;
	String realName;
	
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPayPasswd() {
		return payPasswd;
	}

	public void setPayPasswd(String payPasswd) {
		this.payPasswd = payPasswd;
	}

	public String getBoundCardId() {
		return boundCardId;
	}

	public void setBoundCardId(String boundCardId) {
		this.boundCardId = boundCardId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBankid() {
		return bankid;
	}

	public String getUserbankId() {
		return userbankId;
	}

	public void setUserbankId(String userbankId) {
		this.userbankId = userbankId;
	}

	public void setBankid(int bankid) {
		this.bankid = bankid;
	}

	public String getBankname() {
		return bankName;
	}

	public void setBankname(String bankName) {
		this.bankName = bankName;
	}

	public String getBanknum() {
		return bankAccountNo;
	}

	public void setBanknum(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImgurl() {
		return bankId;
	}

	public void setImgurl(String bankId) {
		this.bankId = bankId;
	}

	public String getOwnername() {
		return ownername;
	}

	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
