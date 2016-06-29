package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * @author guanghui.han
 * 
 */
public class BankCardList implements Serializable {
	private static final long serialVersionUID = 2475300763811645453L;
	/**
	 * 银行卡id
	 */
	private int id;
	/**
	 * 银行卡图像
	 */
	private String bankImg;
	/**
	 * 银行卡名字
	 */
	private String bankName;
	/**
	 * bankCode
	 */
	private String bankCode;
	
	
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankImg() {
		return bankImg;
	}

	public void setBankImg(String bankImg) {
		this.bankImg = bankImg;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImgurl() {
		return bankImg;
	}

	public void setImgurl(String bankImg) {
		this.bankImg = bankImg;
	}

	public String getName() {
		return bankName;
	}

	public void setName(String bankName) {
		this.bankName = bankName;
	}

}
