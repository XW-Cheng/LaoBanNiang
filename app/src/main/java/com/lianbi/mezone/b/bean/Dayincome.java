package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class Dayincome implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2387575495993261939L;

	double count;
	String datetime;
	double price;

	public double getCount() {
		return count;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
}
