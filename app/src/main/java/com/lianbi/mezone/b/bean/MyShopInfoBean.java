package com.lianbi.mezone.b.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 我的商铺信息
 * 
 * @time 上午11:16:04
 * @date 2016-2-19
 * @author hongyu.yang
 * 
 */
public class MyShopInfoBean implements Serializable {
	private static final long serialVersionUID = -781412377334071024L;

	private String industryName;
	private String licenseUrl;
	private String salesmanName;
	private String address;
	private String contactName;
	private String introduce;
	private String isValid;
	private String businessId;
	private String businessName;
	private String mobile;
	private String salesmanCode;
	private String headerUrl;
	private String userName;
	private String healthUrl;
	private String provinceId;
	private String userId;
	private String logoUrl;
	private String checkStatus;
	private String industryId;
	private String province;
	private String details;
	ArrayList<ShopIntroduceImageBean> introduceImgUrl;
	private String contactPhone;
	private String qrcodeUrl;

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSalesmanCode() {
		return salesmanCode;
	}

	public void setSalesmanCode(String salesmanCode) {
		this.salesmanCode = salesmanCode;
	}

	public String getHeaderUrl() {
		return headerUrl;
	}

	public void setHeaderUrl(String headerUrl) {
		this.headerUrl = headerUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHealthUrl() {
		return healthUrl;
	}

	public void setHealthUrl(String healthUrl) {
		this.healthUrl = healthUrl;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public ArrayList<ShopIntroduceImageBean> getIntroduceImgUrl() {
		return introduceImgUrl;
	}

	public void setIntroduceImgUrl(
			ArrayList<ShopIntroduceImageBean> introduceImgUrl) {
		this.introduceImgUrl = introduceImgUrl;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

}
