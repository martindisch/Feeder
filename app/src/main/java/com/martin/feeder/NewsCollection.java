package com.martin.feeder;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsCollection {
	private ArrayList<String> titles;
	private ArrayList<String> urls;
	private ArrayList<String> contents;
	
	public NewsCollection(String[] titles, String[] urls, String[] contents) {
		super();
		this.titles = new ArrayList<String>(Arrays.asList(titles));
		this.urls = new ArrayList<String>(Arrays.asList(urls));
		this.contents = new ArrayList<String>(Arrays.asList(contents));
	}
	
	public String[] getTitles() {
        String[] arr = new String[titles.size()];
		return titles.toArray(arr);
	}
	
	public String[] getUrls() {
        String[] arr = new String[titles.size()];
		return urls.toArray(arr);
	}
	
	public String[] getContents() {
        String[] arr = new String[titles.size()];
		return contents.toArray(arr);
	}

    public void removeItem(int position) {
        titles.remove(position);
        urls.remove(position);
        contents.remove(position);
    }

}
