package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class ShouYeBannerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2688603839474104021L;

	private String bannerCode;
	private String bannerContent;
	private String bannerTitle;
	private String bannerUrl;
	private String createTime;
	private String createUser;
	private int id;
	private String imageUrl;
	private String isValid;
	public String getBannerCode() {
		return bannerCode;
	}
	public void setBannerCode(String bannerCode) {
		this.bannerCode = bannerCode;
	}
	public String getBannerContent() {
		return bannerContent;
	}
	public void setBannerContent(String bannerContent) {
		this.bannerContent = bannerContent;
	}
	public String getBannerTitle() {
		return bannerTitle;
	}
	public void setBannerTitle(String bannerTitle) {
		this.bannerTitle = bannerTitle;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	
}
