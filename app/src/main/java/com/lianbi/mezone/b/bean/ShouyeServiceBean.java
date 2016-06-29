package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class ShouyeServiceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5888605498815568385L;

	String appCode;
	String appName;
	String createTime;
	String icoUrl;
	int id;
	int isVaild;
	String modifyTime;
	int defaultservice;
	boolean isEnd;
	String darkIcoUrl;
	public String getDarkIcoUrl() {
		return darkIcoUrl;
	}

	public void setDarkIcoUrl(String darkIcoUrl) {
		this.darkIcoUrl = darkIcoUrl;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public int getDefaultservice() {
		return defaultservice;
	}

	public void setDefaultservice(int defaultservice) {
		this.defaultservice = defaultservice;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}
