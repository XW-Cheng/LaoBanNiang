package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 消息
 * 
 * @author guanghui.han
 * 
 */
public  class InfoMessageBean implements Serializable {

	/**
	 * "id": 9,
            "storeId": "BDP200eWiZ16cbs041217820",
            "content": "蓝色的解放路口",
            "sourceType": "tss",
            "createTime": 1463575898000

	 */
	private static final long serialVersionUID = -7811121407012587884L;
	String tablenum;
	String order;
	String time;
	int status;
	String content;
	
	/////////
	int id;
	String storeId;
	String createTime;
	String isRead;
	String modifyTime;
	String msgContent;
	int msgType;
	int pushId;
	String tableId;
	String tableName;
	String delFlag;
	String auditStatus;
	
	
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getPushId() {
		return pushId;
	}
	public void setPushId(int pushId) {
		this.pushId = pushId;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	boolean iss;
	
	/**
	 * 是否选择
	 */
	boolean isS;
	
	public boolean isS() {
		return isS;
	}
	public void setS(boolean isS) {
		this.isS = isS;
	}
	public boolean isIss() {
		return iss;
	}
	public void setIss(boolean iss) {
		this.iss = iss;
	}
	public String getTablenum() {
		return tablenum;
	}
	public void setTablenum(String tablenum) {
		this.tablenum = tablenum;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
