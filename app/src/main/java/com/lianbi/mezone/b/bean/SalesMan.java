package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class SalesMan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6783176503429971149L;
	/**
	 * 员工ID
	 */
	int salesclerk_id;
	/**
	 * 员工姓名
	 */
	String salesclerk_name;
	/**
	 * 员工卡号
	 */
	String alesclerkBank;
	/**
	 * 门店ID
	 */
	String business_id;
	/**
	 * 员工性别
	 */
	int alesclerkSex;
	/**
	 * 电话
	 */
	String salesclerk_phone;
	/**
	 * 地址
	 */
	String alesclerkAddress;
	/**
	 * 描述
	 */
	String alesclerkDetail;
	/**
	 * 店员头像
	 */
	String salesclerk_image;
	String sortLetters; // 显示数据拼音的首字母

	/**
	 * 职位
	 */
	String salesclerk_job;

	public int getSalesclerk_id() {
		return salesclerk_id;
	}

	public void setSalesclerk_id(int salesclerk_id) {
		this.salesclerk_id = salesclerk_id;
	}

	public String getSalesclerk_name() {
		return salesclerk_name;
	}

	public void setSalesclerk_name(String salesclerk_name) {
		this.salesclerk_name = salesclerk_name;
	}

	public String getAlesclerkBank() {
		return alesclerkBank;
	}

	public void setAlesclerkBank(String alesclerkBank) {
		this.alesclerkBank = alesclerkBank;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public int getAlesclerkSex() {
		return alesclerkSex;
	}

	public void setAlesclerkSex(int alesclerkSex) {
		this.alesclerkSex = alesclerkSex;
	}

	public String getSalesclerk_phone() {
		return salesclerk_phone;
	}

	public void setSalesclerk_phone(String salesclerk_phone) {
		this.salesclerk_phone = salesclerk_phone;
	}

	public String getAlesclerkAddress() {
		return alesclerkAddress;
	}

	public void setAlesclerkAddress(String alesclerkAddress) {
		this.alesclerkAddress = alesclerkAddress;
	}

	public String getAlesclerkDetail() {
		return alesclerkDetail;
	}

	public void setAlesclerkDetail(String alesclerkDetail) {
		this.alesclerkDetail = alesclerkDetail;
	}

	public String getSalesclerk_image() {
		return salesclerk_image;
	}

	public void setSalesclerk_image(String salesclerk_image) {
		this.salesclerk_image = salesclerk_image;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getSalesclerk_job() {
		return salesclerk_job;
	}

	public void setSalesclerk_job(String salesclerk_job) {
		this.salesclerk_job = salesclerk_job;
	}

}
