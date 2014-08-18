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

    public void removeItem(int position) {
        titles = loopAndKill(titles, position);
        urls = loopAndKill(urls, position);
        contents = loopAndKill(contents, position);
    }

    private String[] loopAndKill(String[] array, int position) {
        String[] newArray = new String[titles.length - 1];
        array[position] = "keine";
        int newCounter = 0;
        for (int i = 0; i < (titles.length - 1); i++) {
            if (!titles[i].contentEquals("Keine")) {
                newArray[newCounter] = titles[i];
                newCounter++;
            }
        }
        return newArray;
    }

}
