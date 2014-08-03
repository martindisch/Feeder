package com.martin.feeder;

public class NewsCollection {
	private String[] titles;
	private String[] urls;
	private String[] contents;
	
	public NewsCollection(String[] titles, String[] urls, String[] contents) {
		super();
		this.titles = titles;
		this.urls = urls;
		this.contents = contents;
	}
	
	public String[] getTitles() {
		return this.titles;
	}
	
	public String[] getUrls() {
		return this.urls;
	}
	
	public String[] getContents() {
		return this.contents;
	}

}
