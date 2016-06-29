package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 会员
 * 
 * @author hongyu.yang
 * 
 */
public class AssociatorListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7682250052689142766L;

	int id;
	String associator_address;
	int associator_sex;
	String associator_phone;
	int status;
	String business_id;
	int associator_level;
	int associator_id;
	int is_flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAssociator_address() {
		return associator_address;
	}

	public void setAssociator_address(String associator_address) {
		this.associator_address = associator_address;
	}

	public int getAssociator_sex() {
		return associator_sex;
	}

	public void setAssociator_sex(int associator_sex) {
		this.associator_sex = associator_sex;
	}

	public String getAssociator_phone() {
		return associator_phone;
	}

	public void setAssociator_phone(String associator_phone) {
		this.associator_phone = associator_phone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public int getAssociator_level() {
		return associator_level;
	}

	public void setAssociator_level(int associator_level) {
		this.associator_level = associator_level;
	}

	public int getAssociator_id() {
		return associator_id;
	}

	public void setAssociator_id(int associator_id) {
		this.associator_id = associator_id;
	}

	public int getIs_flag() {
		return is_flag;
	}

	public void setIs_flag(int is_flag) {
		this.is_flag = is_flag;
	}

	public String getAssociator_detail() {
		return associator_detail;
	}

	public void setAssociator_detail(String associator_detail) {
		this.associator_detail = associator_detail;
	}

	public String getAssociator_name() {
		return associator_name;
	}

	public void setAssociator_name(String associator_name) {
		this.associator_name = associator_name;
	}

	String associator_detail;
	String associator_name;
}
