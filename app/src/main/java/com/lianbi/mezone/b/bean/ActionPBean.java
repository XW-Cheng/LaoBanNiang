package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class ActionPBean implements Serializable {

	public ActionPBean(String name, String content) {
		this.name = name;
		this.content = content;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3847313000650681771L;

	String name;
	String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
