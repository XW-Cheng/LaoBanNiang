package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class Goods_SumbitBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2136830992612576568L;
	/**
	 * 货源ID
	 */
	String 
	productSrouceId;	
	/**
	 * 购买店铺ID 
	 */
	String 
	  buyBusinessId;
	/**
	 * 订单总价 
	 */
	String	
		orderPrice;	
	/**
	 * 货源总价
	 */
	String		
		amount;	
	/**
	 * 货源价格
	 */
	String		
		price;	
	/**
	 * 货源数量
	 */
	String		
		num;
	/**
	 * 商品图片
	 * 
	 */
	String imageUrl;
	/**
	 * 商品名稱
	 * 
	 */
	String pName;
	/**
	 * 
	 * @param productSrouceId
	 * @param buyBusinessId
	 * @param orderPrice
	 * @param amount
	 * @param price
	 * @param num
	 * @param imageUrl
	 * @param pName
	 */
	public Goods_SumbitBean(String productSrouceId, String buyBusinessId,
			String orderPrice, String amount, String price, String num,
			String imageUrl, String pName) {
		super();
		this.productSrouceId = productSrouceId;
		this.buyBusinessId = buyBusinessId;
		this.orderPrice = orderPrice;
		this.amount = amount;
		this.price = price;
		this.num = num;
		this.imageUrl = imageUrl;
		this.pName = pName;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getProductSrouceId() {
		return productSrouceId;
	}
	public void setProductSrouceId(String productSrouceId) {
		this.productSrouceId = productSrouceId;
	}
	public String getBuyBusinessId() {
		return buyBusinessId;
	}
	public void setBuyBusinessId(String buyBusinessId) {
		this.buyBusinessId = buyBusinessId;
	}
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}	
	
}
