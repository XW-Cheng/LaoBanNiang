package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class ProvinceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 885185601440441887L;

	String province;
	String provinceId;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

}
