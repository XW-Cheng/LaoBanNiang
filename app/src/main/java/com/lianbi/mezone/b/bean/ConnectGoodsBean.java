package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 联系货源
 * 
 * @time 下午11:57:54
 * @date 2016-1-25
 * @author hongyu.yang
 * 
 */
public class ConnectGoodsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7814026064882982716L;
	/**
	 * 
	 */
	int id;
	/**
	 * 货源名称
	 */
	String contacts_sourceName;
	/**
	 * 联系电话
	 */
	String contacts_phone;
	/**
	 * 图片
	 */
	String contacts_img;
	/**
	 * 货源内容
	 */
	String contacts_srouceDetail;
	/**
	 * 用户名
	 */
	String user_id;
	/**
	 * 联系人名称
	 */
	String contacts_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContacts_sourceName() {
		return contacts_sourceName;
	}

	public void setContacts_sourceName(String contacts_sourceName) {
		this.contacts_sourceName = contacts_sourceName;
	}

	public String getContacts_phone() {
		return contacts_phone;
	}

	public void setContacts_phone(String contacts_phone) {
		this.contacts_phone = contacts_phone;
	}

	public String getContacts_img() {
		return contacts_img;
	}

	public void setContacts_img(String contacts_img) {
		this.contacts_img = contacts_img;
	}

	public String getContacts_srouceDetail() {
		return contacts_srouceDetail;
	}

	public void setContacts_srouceDetail(String contacts_srouceDetail) {
		this.contacts_srouceDetail = contacts_srouceDetail;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getContacts_name() {
		return contacts_name;
	}

	public void setContacts_name(String contacts_name) {
		this.contacts_name = contacts_name;
	}

}
