package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class ServiceMallBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8322206716064145024L;

    //是否下载了
    String download;
	String url;
	String appName;
	//图标
	String icoUrl;
	String darkIcoUrl;
	String introduceUrl;
	String presentPrice;
	String originalPrice;
	String unit;
	int  id;
    int isVaild;
	String appCode;
	int url1;
	public String getDarkIcoUrl() {
		return darkIcoUrl;
	}
	public void setDarkIcoUrl(String darkIcoUrl) {
		this.darkIcoUrl = darkIcoUrl;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getIntroduceUrl() {
		return introduceUrl;
	}
	public void setIntroduceUrl(String introduceUrl) {
		this.introduceUrl = introduceUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUrl1() {
		return url1;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getIcoUrl() {
		return icoUrl;
	}
	public void setIcoUrl(String icoUrl) {
		this.icoUrl = icoUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIsVaild() {
		return isVaild;
	}
	public void setIsVaild(int isVaild) {
		this.isVaild = isVaild;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public void setUrl1(int url1) {
		this.url1 = url1;
	}
	
}
