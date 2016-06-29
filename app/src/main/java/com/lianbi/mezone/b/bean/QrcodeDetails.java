package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 二维码
 * 
 * @author hongyu.yang
 * 
 */
public class QrcodeDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7682250052689142766L;

 	 int id;
	 String tableName;
	 String storeId;
 	 int tradeStatus;
 	 int delFlag;
 	 String codeUrl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	

}
