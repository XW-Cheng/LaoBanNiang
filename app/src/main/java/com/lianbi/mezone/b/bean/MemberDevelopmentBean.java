package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 会员制定
 * 
 * @author guanghui.han
 * 
 */
public class MemberDevelopmentBean implements Serializable {

	private static final long serialVersionUID = 91578671538798340L;

	/**
	 * 会员卡编号
	 */
	int b_level_id;
	/**
	 * 门店ID
	 */
	String business_id;

	/**
	 * 0关1开
	 */
	int status = 0;
	/**
	 * 会员等级名称
	 */
	String b_level_name = "";
	/**
	 * 描述
	 */
	String b_leve_detail = "";
	/**
	 * 折扣
	 */
	String b_discount = "";
	/**
	 * 需消费
	 */
	String price;

	public String getB_leve_detail() {
		return b_leve_detail;
	}

	public void setB_leve_detail(String b_leve_detail) {
		this.b_leve_detail = b_leve_detail;
	}

	public String getB_discount() {
		return b_discount;
	}

	public void setB_discount(String b_discount) {
		this.b_discount = b_discount;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public int getB_level_id() {
		return b_level_id;
	}

	public void setB_level_id(int b_level_id) {
		this.b_level_id = b_level_id;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getB_level_name() {
		return b_level_name;
	}

	public void setB_level_name(String b_level_name) {
		this.b_level_name = b_level_name;
	}

}
