package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 貨源商城右面数据
 * 
 * @time 下午4:53:36
 * @date 2016-1-23
 * @author hongyu.yang
 * 
 */
public class ProductSourceListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8176100742079962433L;
	/**
	 * 商铺ID
	 */
	String business_id;
	String industry_id;
	int cate_id;
	/**
	 * 详细
	 */
	String detail;
	int id;
	/**
	 * 图片
	 */
	String image;
	/**
	 * 金额
	 */
	String price;
	/**
	 * 货源ID
	 */
	String product_source_id;
	String product_source_title;
	int status;
	/**
	 * 单位
	 */
	String unit;

	public String getBusiness_id() {
		return business_id;
	}

	public String getIndustry_id() {
		return industry_id;
	}

	public void setIndustry_id(String industry_id) {
		this.industry_id = industry_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public int getCate_id() {
		return cate_id;
	}

	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProduct_source_id() {
		return product_source_id;
	}

	public void setProduct_source_id(String product_source_id) {
		this.product_source_id = product_source_id;
	}

	public String getProduct_source_title() {
		return product_source_title;
	}

	public void setProduct_source_title(String product_source_title) {
		this.product_source_title = product_source_title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
