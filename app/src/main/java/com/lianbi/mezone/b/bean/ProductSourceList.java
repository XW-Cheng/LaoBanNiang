package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 下单记录
 * 
 * @time 下午5:00:47
 * @date 2016-1-24
 * @author hongyu.yang
 * 
 */
public class ProductSourceList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8706598388261341989L;
	String address;
	String business_id;
	String buy_business_id;
	String id;
	String order_id;
	String phone;
	int plantform_id;
	double price;
	String product_id;
	String user_name;
	String create_time;
	String product_source_title;
	String num;
	String business_name;
	String vendorPhone;

	public String getVendorPhone() {
		return vendorPhone;
	}

	public void setVendorPhone(String vendorPhone) {
		this.vendorPhone = vendorPhone;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProduct_source_title() {
		return product_source_title;
	}

	public void setProduct_source_title(String product_source_title) {
		this.product_source_title = product_source_title;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	String image;

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getBuy_business_id() {
		return buy_business_id;
	}

	public void setBuy_business_id(String buy_business_id) {
		this.buy_business_id = buy_business_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPlantform_id() {
		return plantform_id;
	}

	public void setPlantform_id(int plantform_id) {
		this.plantform_id = plantform_id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
