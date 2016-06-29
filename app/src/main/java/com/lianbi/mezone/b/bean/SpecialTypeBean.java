package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class SpecialTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7253307036663576420L;
	String type;
	String price;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
