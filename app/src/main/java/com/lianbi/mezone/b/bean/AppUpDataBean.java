package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class AppUpDataBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2776886744672317502L;
	String version;
	String craete_time;
	String url;
	String edition_name;
	String coerceModify;
	String deviceType;

	public String getDeviceType(){
		return deviceType;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getCoerceModify() {
		return coerceModify;
	}

	public void setCoerceModify(String coerceModify) {
		this.coerceModify = coerceModify;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCraete_time() {
		return craete_time;
	}

	public void setCraete_time(String craete_time) {
		this.craete_time = craete_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEdition_name() {
		return edition_name;
	}

	public void setEdition_name(String edition_name) {
		this.edition_name = edition_name;
	}

	

}
