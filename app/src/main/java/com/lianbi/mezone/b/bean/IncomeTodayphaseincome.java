package com.lianbi.mezone.b.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class IncomeTodayphaseincome implements Serializable {

	/**
	 * @discripes
	 */
	private static final long serialVersionUID = -6277762285201281391L;
	String price;
	String datetime;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
