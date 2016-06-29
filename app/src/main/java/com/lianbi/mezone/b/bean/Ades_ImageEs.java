package com.lianbi.mezone.b.bean;

import java.io.Serializable;

public class Ades_ImageEs implements Serializable {

	/**
	 * @discripes
	 */
	private static final long serialVersionUID = -1631199496247694919L;
	int id;
	String image;
	String url;
	String name;
	int type;
	int advertisement_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAdvertisement_id() {
		return advertisement_id;
	}

	public void setAdvertisement_id(int advertisement_id) {
		this.advertisement_id = advertisement_id;
	}

}
