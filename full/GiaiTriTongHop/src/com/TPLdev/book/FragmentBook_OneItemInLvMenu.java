package com.TPLdev.book;

import java.io.Serializable;

public class FragmentBook_OneItemInLvMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	// anh dai dien cua trang bao sliding drawer
	public int mPicture;
	// chuyen muc
	public String mTit;
	// ten chuyen muc
	public String mMenuTit;

	public FragmentBook_OneItemInLvMenu(int picture, String menuTit, String tit) {
		this.mPicture = picture;
		this.mMenuTit = menuTit;
		this.mTit = tit;
	}

	public int getmPicture() {
		return mPicture;
	}

	public void setmPicture(int mPicture) {
		this.mPicture = mPicture;
	}

	public String getmTit() {
		return mTit;
	}

	public void setmTit(String mTit) {
		this.mTit = mTit;
	}

	public String getmMenuTit() {
		return mMenuTit;
	}

	public void setmMenuTit(String mMenuTit) {
		this.mMenuTit = mMenuTit;
	}
}
