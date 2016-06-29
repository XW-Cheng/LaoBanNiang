package com.lianbi.mezone.b.bean;

import java.io.Serializable;

/**
 * 消息
 * 
 * @author guanghui.han
 * 
 */
public class MessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6476774425259686012L;
	/**
	 * 是否选择
	 */
	boolean isS;

	String messageCreateTime;

	public String getMessageCreateTime() {
		return messageCreateTime;
	}

	public void setMessageCreateTime(String messageCreateTime) {
		this.messageCreateTime = messageCreateTime;
	}

	String message_id;
	/**
	 * 0系统消息1订单消息2定制消息
	 */
	int message_type;
	String message_title;
	String message_content;
	String business_id;
	/**
	 * 是否未读 0/1
	 */
	int is_read;

	public boolean isS() {
		return isS;
	}

	public void setS(boolean isS) {
		this.isS = isS;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public int getMessage_type() {
		return message_type;
	}

	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}

	public String getMessage_title() {
		return message_title;
	}

	public void setMessage_title(String message_title) {
		this.message_title = message_title;
	}

	public String getMessage_content() {
		return message_content;
	}

	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public int getIs_read() {
		return is_read;
	}

	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}

}
