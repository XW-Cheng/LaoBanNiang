package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class SalesX_bean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 562492796666442461L;
	double price;
	String datetime;
	String order_id;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

}
