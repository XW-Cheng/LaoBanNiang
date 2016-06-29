package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class GoodsJiBieBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3901630218795887535L;
	boolean isS;
	/**
	 * 起批量
	 */
	String numFen;
	/**
	 * 终止起批量
	 */
	String numFenEnd;
	int price;

	public String getNumFenEnd() {
		return numFenEnd;
	}

	public void setNumFenEnd(String numFenEnd) {
		this.numFenEnd = numFenEnd;
	}

	public GoodsJiBieBean(boolean isS, String numFen, String numFenEnd,
			int price) {
		this.isS = isS;
		this.numFen = numFen;
		this.numFenEnd = numFenEnd;
		this.price = price;
	}

	public boolean isS() {
		return isS;
	}

	public void setS(boolean isS) {
		this.isS = isS;
	}

	public String getNumFen() {
		return numFen;
	}

	public void setNumFen(String numFen) {
		this.numFen = numFen;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
