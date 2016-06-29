package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class DateAndColor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7707482323553007087L;
	int colorId = 0;
	String day;

	public int getColorId() {
		return colorId;
	}

	public void setColorId(int colorId) {
		this.colorId = colorId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getTextcolor() {
		return textcolor;
	}

	public void setTextcolor(int textcolor) {
		this.textcolor = textcolor;
	}

	int textcolor = 0;

}
