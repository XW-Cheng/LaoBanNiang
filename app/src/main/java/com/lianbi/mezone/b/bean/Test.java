package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class Test implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6476774425259686012L;
	String name;

	public Test(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
