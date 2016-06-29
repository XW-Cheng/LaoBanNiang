package com.lianbi.mezone.b.httpresponse;

/***
 * 服务器通用的响应码
 */
public enum ResponseCode {
	CRFTOKEN_ERROR("错误", 10000), INVALID_PLATFORM_("平台标识无效", 10001);

	private String msg;
	private int status;

	private ResponseCode(String msg, int status) {
		this.msg = msg;
		this.status = status;
	}

	/**
	 * 获取状态码信息
	 * 
	 * @param status
	 * @return
	 */
	public static String getMessage(int status) {
		String msg = "";
		for (ResponseCode reCode : ResponseCode.values()) {
			if (reCode.getStatus() == status) {
				msg = reCode.getMsg();
			}
		}
		return msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
