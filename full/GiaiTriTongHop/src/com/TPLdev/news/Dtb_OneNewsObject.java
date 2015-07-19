package com.TPLdev.news;

import java.io.Serializable;

//doi tuong 1 tin tuc
public class Dtb_OneNewsObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String title;
	public String link;

	public Dtb_OneNewsObject(String tit, String l) {
		this.title = tit;
		this.link = l;
	}

	public Dtb_OneNewsObject(int id, String tit, String l) {
		this.id = id;
		this.title = tit;
		this.link = l;
	}
}
