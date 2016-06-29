package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class MyLiCaiBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5412171303360880469L;
	/**
	 * 产品名称
	 */
	String name;
	/**
	 * d定期 h活期
	 */
	String lable;
	/**
	 * 收益率
	 */
	double rate;
	/**
	 * 产品期限
	 */
	int deadline;
	/**
	 * 产品描述
	 */
	String description;
	/**
	 * N 未开售 Z销售中 O售完
	 */
	String status;
	/**
	 * 产品id
	 */
	int id;
	/**
	 * 价格
	 */
	double amount;
	/**
	 * 时间
	 */
	String date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
