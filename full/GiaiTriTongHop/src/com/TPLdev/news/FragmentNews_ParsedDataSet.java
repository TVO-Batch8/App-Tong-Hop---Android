package com.TPLdev.news;

//1 doi tuong tin tuc (item) trong file rss
public class FragmentNews_ParsedDataSet {
	// tag cha
	private String parentTag = null;
	// thuoc tinh trong 1 item
	private String title = null;
	private String description = null;
	private String link = null;
	private String pubDate = null;
	private String summaryImg = null;

	public FragmentNews_ParsedDataSet() {

	}

	public FragmentNews_ParsedDataSet(String title, String description,
			String link, String pubdate, String summaryimg) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.pubDate = pubdate;
		this.summaryImg = summaryimg;
	}

	public String getParentTag() {
		return parentTag;
	}

	public void setParentTag(String parentTag) {
		this.parentTag = parentTag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSummaryImg() {
		return summaryImg;
	}

	public void setSummaryImg(String summaryImg) {
		this.summaryImg = summaryImg;
	}

}