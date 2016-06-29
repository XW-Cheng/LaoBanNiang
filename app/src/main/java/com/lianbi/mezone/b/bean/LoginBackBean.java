package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 用户登录返回
 * 
 * @time 下午4:20:21"user": {"id":3,"urgent_phone":"     ",
 *       "username":"","email":"    ","urgent":"    ", "userImage":"     ",
 *       "user_id":"UYH20AjQH716opw012547809",
 *       "password":"222222","is_flag":0,"mobile":"18980692055"}
 * @date 2016-1-21
 * @author hongyu.yang
 * 
 */
public class LoginBackBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2533465121794615138L;
	String lastLogin = "";
	String userImage = "";
	String isValid = "";
	String mobile = "";
	String memo = "";
	String defaultBusiness = "";
	String urgent = "";
	String urgentPhone = "";
	String userId = "";
	String email = "";
	String userImageThumb = "";
	String username = "";

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDefaultBusiness() {
		return defaultBusiness;
	}

	public void setDefaultBusiness(String defaultBusiness) {
		this.defaultBusiness = defaultBusiness;
	}

	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}

	public String getUrgentPhone() {
		return urgentPhone;
	}

	public void setUrgentPhone(String urgentPhone) {
		this.urgentPhone = urgentPhone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserImageThumb() {
		return userImageThumb;
	}

	public void setUserImageThumb(String userImageThumb) {
		this.userImageThumb = userImageThumb;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
