package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 行业分类
 * 
 * @time 下午7:12:16
 * @date 2016-1-26
 * @author hongyu.yang
 * 
 */
public class IndustryListBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8339527053508387505L;
	/**
	 * 分类Id
	 */
	String industryId;
	/**
	 * 主Id
	 */
	String parentId;
	/**
	 * 分类名称
	 */
	String industryName;
	/**
	 * 主名字
	 */
	String parentName;

	/**
	 * 是否选择
	 */
	boolean isSelect;

	
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getMaj_name() {
		return industryName;
	}

	public void setMaj_name(String industryName) {
		this.industryName = industryName;
	}

	public String getMajor_id() {
		return industryId;
	}

	public void setMajor_id(String industryId) {
		this.industryId = industryId;
	}

	public String getParant_id() {
		return parentId;
	}

	public void setParant_id(String parentId) {
		this.parentId = parentId;
	}

	public String getParant_name() {
		return parentName;
	}

	public void setParant_name(String parentName) {
		this.parentName = parentName;
	}

}
