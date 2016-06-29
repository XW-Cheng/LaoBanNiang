package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class TableSetBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8927806972434126948L;

	int tableId;//主键唯一标识
	String createTime;//创建时间
	int tradeStatus;//店铺营业标识 0休息 1 营业
	int tableStatus;//桌位状态 0灰色 1绿色 2红色 3黄色 4蓝色
	String callTime;//呼叫时间
	String tableName;//桌位名称
	String codeUrl;//二维码相对路径
	String modifyTime;//修改时间
	String storeId;//店铺id
	/**
	 * 是否选择
	 */
	public boolean isS;
	
	
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public int getTableId() {
		return tableId;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public int getTableStatus() {
		return tableStatus;
	}
	public void setTableStatus(int tableStatus) {
		this.tableStatus = tableStatus;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public boolean isS() {
		return isS;
	}
	public void setS(boolean isS) {
		this.isS = isS;
	}
	
}
