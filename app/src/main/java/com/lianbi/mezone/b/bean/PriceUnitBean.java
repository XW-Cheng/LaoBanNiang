package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class PriceUnitBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3261207514011676742L;
	String price;
	String priceUnit;
	String amount;
	String amountUnit;
	boolean isS;

	public boolean isS() {
		return isS;
	}

	public void setS(boolean isS) {
		this.isS = isS;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(String amountUnit) {
		this.amountUnit = amountUnit;
	}
}
