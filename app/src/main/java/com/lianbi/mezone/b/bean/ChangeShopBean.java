package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 商铺；列表
 * 
 * @time 下午8:25:35
 * @date 2016-1-23
 * @author hongyu.yang
 * 
 */
public class ChangeShopBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4176868402777995367L;

	/**
	 * 是否选择
	 */
	boolean isSelect;
	String contactPhone;
	String industryId;
	String introduce;

	String id;

	String businessName;
	String is_pause;
	String details;
	String address;
	String businessId;
	String contactName;
	String longitude;
	String userId;
	String latitude;
	String license;

	String QRcodeUrl;
	String provinceId;

	String logoUrl;
	String headerUrl;
	String healthUrl;

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getHeaderUrl() {
		return headerUrl;
	}

	public void setHeaderUrl(String headerUrl) {
		this.headerUrl = headerUrl;
	}

	public String getHealthUrl() {
		return healthUrl;
	}

	public void setHealthUrl(String healthUrl) {
		this.healthUrl = healthUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQRcodeUrl() {
		return QRcodeUrl;
	}

	public void setQRcodeUrl(String qRcodeUrl) {
		QRcodeUrl = qRcodeUrl;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
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

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getPhone() {
		return contactPhone;
	}

	public void setPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getIndustry_id() {
		return industryId;
	}

	public void setIndustry_id(String industryId) {
		this.industryId = industryId;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusiness_name() {
		return businessName;
	}

	public void setBusiness_name(String businessName) {
		this.businessName = businessName;
	}

	public String getIs_pause() {
		return is_pause;
	}

	public void setIs_pause(String is_pause) {
		this.is_pause = is_pause;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBusiness_id() {
		return businessId;
	}

	public void setBusiness_id(String businessId) {
		this.businessId = businessId;
	}

	public String getContact_name() {
		return contactName;
	}

	public void setContact_name(String contactName) {
		this.contactName = contactName;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getUser_id() {
		return userId;
	}

	public void setUser_id(String userId) {
		this.userId = userId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
}
