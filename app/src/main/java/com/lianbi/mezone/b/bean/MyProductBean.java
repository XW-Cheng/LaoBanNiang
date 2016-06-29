package com.lianbi.mezone.b.bean;

import java.io.Serializable;

import com.lianbi.mezone.b.httpresponse.API;

public class MyProductBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5624908462202327266L;
	/**
	 * 商品池编号
	 * 
	 */
	String product_pool_id;
	/**
	 * string 商品编号,
	 * 
	 */
	String product_id;
	String pool_create_time;

	public String getPool_create_time() {
		return pool_create_time;
	}

	public void setPool_create_time(String pool_create_time) {
		this.pool_create_time = pool_create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 创建时间
	 */
	String create_time;
	/**
	 * 修改时间
	 */
	String updateTime;
	/**
	 * 商品名称,
	 */
	String product_pool_name;
	/**
	 * 商品名称,my
	 */
	String product_name;
	/**
	 * 商品类型名称,my
	 */
	String industry_name;
	/**
	 * 商品显示位置,
	 */
	int position;

	public String getIndustry_name() {
		return industry_name;
	}

	public void setIndustry_name(String industry_name) {
		this.industry_name = industry_name;
	}

	/**
	 * 所属行业类别,
	 */
	int industryId;
	/**
	 * 所属行业类别,my
	 */
	int majorInfoId;
	/**
	 * 产品类别,
	 */
	int cateId;
	/**
	 * 单位,
	 */
	String unit;
	/**
	 * 是否被推荐0：false,1:true
	 */
	int isRecommended;
	/**
	 * 是否在线出售0：false,1:true
	 */
	int is_onsale;
	/**
	 * 详细,
	 */
	String description;
	/**
	 * 图片
	 * 
	 */
	String icon;
	/**
	 * 是否特价
	 */
	int is_active;
	/**
	 * 产品池中产品状态0未上架1已上架
	 */
	int status;
	/**
	 * 特价类型
	 */
	String special_type;
	// ArrayList<SpecialTypeBean> specialType;
	/**
	 * 产品类型服务类型 1：特殊服务，2：活动，3：优惠，4：团购，5：商品
	 */
	int product_type;
	/**
	 * 
	 */
	String type;

	String price;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	String end_date;

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProduct_pool_id() {
		return product_pool_id;
	}

	public void setProduct_pool_id(String product_pool_id) {
		this.product_pool_id = product_pool_id;
	}

	public String getProduct_pool_name() {
		return product_pool_name;
	}

	public void setProduct_pool_name(String product_pool_name) {
		this.product_pool_name = product_pool_name;
	}

	public int getProduct_type() {
		return product_type;
	}

	public void setProduct_type(int product_type) {
		this.product_type = product_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getMajorInfoId() {
		return majorInfoId;
	}

	public void setMajorInfoId(int majorInfoId) {
		this.majorInfoId = majorInfoId;
	}

	public String getSpecial_type() {
		return special_type;
	}

	public void setSpecial_type(String special_type) {
		this.special_type = special_type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getIndustryId() {
		return industryId;
	}

	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}

	public int getCateId() {
		return cateId;
	}

	public void setCateId(int cateId) {
		this.cateId = cateId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(int isRecommended) {
		this.isRecommended = isRecommended;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getIs_onsale() {
		return is_onsale;
	}

	public void setIs_onsale(int is_onsale) {
		this.is_onsale = is_onsale;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

}
