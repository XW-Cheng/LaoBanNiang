package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 商铺介绍图片信息
 * 
 * @time 上午11:17:01
 * @date 2016-2-19
 * @author hongyu.yang
 * 
 */
public class ShopIntroduceImageBean implements Serializable {
	private static final long serialVersionUID = -3676086912133509827L;
	String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
