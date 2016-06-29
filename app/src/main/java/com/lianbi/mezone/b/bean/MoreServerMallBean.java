package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 服务商城
 * 
 * @time 上午10:04:44
 * @date 2016-1-21
 * @author hongyu.yang
 * 
 */
public class MoreServerMallBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7476678877164900674L;
	/**
	 * 服务商城ID
	 */
	int mall_id;
	/**
	 * 服务商城名
	 */
	String mall_name;
	/**
	 * 服务商城详细
	 */
	String detail;
	/**
	 * 下载次数
	 */
	int download_count;
	/**
	 * 是否被推荐
	 */
	int is_commoned;
	/**
	 * 显示位置
	 */
	int position;
	int id;
	
	int isVaild;
	int is_creati;
	int is_flag;
	String appCode;
	String appName;
	String url;
	/**
	 * 是否更多
	 */
	boolean isJ = false;
	String icon;
	//到店服务已下载的logo图标地址
	String icoUrl;
	//到店服务未下载的logo图标地址
	String darkIcoUrl;
	public String getIcoUrl() {
		return icoUrl;
	}

	public String getDarkIcoUrl() {
		return darkIcoUrl;
	}

	public void setDarkIcoUrl(String darkIcoUrl) {
		this.darkIcoUrl = darkIcoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		this.icoUrl = icoUrl;
	}

	public int getIsVaild() {
		return isVaild;
	}

	public void setIsVaild(int isVaild) {
		this.isVaild = isVaild;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isJ() {
		return isJ;
	}

	public void setJ(boolean isJ) {
		this.isJ = isJ;
	}

	public String getMall_name() {
		return mall_name;
	}

	public void setMall_name(String mall_name) {
		this.mall_name = mall_name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getIs_commoned() {
		return is_commoned;
	}

	public void setIs_commoned(int is_commoned) {
		this.is_commoned = is_commoned;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMall_id() {
		return mall_id;
	}

	public void setMall_id(int mall_id) {
		this.mall_id = mall_id;
	}

	public int getDownload_count() {
		return download_count;
	}

	public void setDownload_count(int download_count) {
		this.download_count = download_count;
	}

	public int getIs_creati() {
		return is_creati;
	}

	public void setIs_creati(int is_creati) {
		this.is_creati = is_creati;
	}

	public int getIs_flag() {
		return is_flag;
	}

	public void setIs_flag(int is_flag) {
		this.is_flag = is_flag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
