package com.martin.feeder;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * This is a very old class I wrote with very little experience. It has horrible redundancy in it,
 * most of which could be dealt with simply by implementing the factory pattern.
 * However, I haven't found the time to rewrite the whole thing yet, so please don't be too hard on the code.
 * 
 * @author Martin
 *	
 */
class NewsSources {
	private final Context context;

	public NewsSources(Context context) {
		super();
		this.context = context;
	}

	public NewsCollection getA3_News() {
		final String url = "http://www.arma3.com/news";
		final String classname = "col-sm-9";

		Document doc = null;
		Elements content = null;
		
		String[] titles = { "null" };
		String[] urls = { "null" };
		String[] contents = { "null" };
		
		NewsCollection nColl = new NewsCollection(titles, urls, contents);

		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			doc = null;
		}
		if (!(doc == null)) {
			content = doc.getElementsByClass(classname);
            // Remove last element which isn't a post
            content.remove(content.size() - 1);

			titles = new String[content.size()];
			urls = new String[content.size()];
			contents = new String[content.size()];

			for (int i = 0; i < content.size(); i++) {
				content.get(i).select("[src]").remove();
				titles[i] = content.get(i).select("header").text();
				urls[i] = content.get(i).select("header").select("a[href]").attr("abs:href");
				contents[i] = content.get(i).getElementsByClass("post-excerpt").text();
			}
			
			nColl = new NewsCollection(titles, urls, contents);
		}
		
		return nColl;
	}
	
	public NewsCollection getA3_Devhub() {
		final String url = "http://dev.arma3.com/";
		final String classname = "post-preview";

		Document doc = null;
		Elements content = null;
		
		String[] titles = { "null" };
		String[] urls = { "null" };
		String[] contents = { "null" };
		
		NewsCollection nColl = new NewsCollection(titles, urls, contents);

		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			doc = null;
		}
		if (!(doc == null)) {
			content = doc.getElementsByClass(classname);
            // Remove last element which isn't a post
            content.remove(content.size() - 1);

			titles = new String[content.size()];
			urls = new String[content.size()];
			contents = new String[content.size()];

			for (int i = 0; i < content.size(); i++) {
				content.get(i).select("[src]").remove();
				titles[i] = content.get(i).select("header").text();
				urls[i] = content.get(i).select("header").select("a[href]").attr("abs:href");
				contents[i] = content.get(i).getElementsByClass("dev-post-excerpt").text();
			}
			
			nColl = new NewsCollection(titles, urls, contents);
		}
		
		return nColl;
	}
	
	public NewsCollection getSpaceEngineers_News() {
		final String url = "http://www.spaceengineersgame.com/news.html";
		final String classname = "paragraph";

		Document doc = null;
		Elements content = null;
		
		String[] titles = { "null" };
		String[] urls = { "null" };
		String[] contents = { "null" };
		
		NewsCollection nColl = new NewsCollection(titles, urls, contents);

		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			doc = null;
		}
		if (!(doc == null)) {
			content = doc.getElementsByClass(classname);

			titles = new String[content.size()];
			urls = new String[content.size()];
			contents = new String[content.size()];

			for (int i = 0; i < content.size(); i++) {
				content.get(i).select("[src]").remove();
				titles[i] = content.get(i).select("strong").text();
				urls[i] = content.get(i).select("a[href]").attr("abs:href");
				content.get(i).select("strong").remove();
				content.get(i).select("font[size]").remove();
				contents[i] = content.get(i).text();
			}
			
			nColl = new NewsCollection(titles, urls, contents);
		}
		
		return nColl;
	}
	
	public NewsCollection getLayer_News() {
		final String url = "http://blog.layer.com/";
		final String classname = "post";

		Document doc = null;
		Elements content = null;
		
		String[] titles = { "null" };
		String[] urls = { "null" };
		String[] contents = { "null" };
		
		NewsCollection nColl = new NewsCollection(titles, urls, contents);

		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
			doc = null;
		}
		if (!(doc == null)) {
			content = doc.getElementsByClass(classname);

			titles = new String[content.size()];
			urls = new String[content.size()];
			contents = new String[content.size()];

			for (int i = 0; i < content.size(); i++) {
				content.get(i).select("[src]").remove();
                titles[i] = content.get(i).getElementsByClass("post-title").text();
                urls[i] = content.get(i).getElementsByClass("post-title").select("a[href]").attr("abs:href");
                contents[i] = content.get(i).getElementsByClass("post-excerpt").text();
			}
			
			nColl = new NewsCollection(titles, urls, contents);
		}
		
		return nColl;
	}
	
	public NewsCollection getAllUnread() {
		NewsCollection ncA3_News = getSingleUnread(getA3_News());
		NewsCollection ncA3_Devhub = getSingleUnread(getA3_Devhub());
		NewsCollection ncSpaceEngineers_News = getSingleUnread(getSpaceEngineers_News());
		NewsCollection ncLayer_News = getSingleUnread(getLayer_News());
		
		int unreadCounter = ncA3_News.getTitles().length + ncA3_Devhub.getTitles().length + ncSpaceEngineers_News.getTitles().length + ncLayer_News.getTitles().length;
		
		String[] unreadTitles = new String[unreadCounter];
		String[] unreadUrls = new String[unreadCounter];
		String[] unreadContents = new String[unreadCounter];
		
		int z = 0;
		for (int i = 0; i < ncA3_News.getTitles().length; i++) {
			unreadTitles[z] = ncA3_News.getTitles()[i];
			unreadUrls[z] = ncA3_News.getUrls()[i];
			unreadContents[z] = ncA3_News.getContents()[i];
			z++;
		}
		for (int i = 0; i < ncA3_Devhub.getTitles().length; i++) {
			unreadTitles[z] = ncA3_Devhub.getTitles()[i];
			unreadUrls[z] = ncA3_Devhub.getUrls()[i];
			unreadContents[z] = ncA3_Devhub.getContents()[i];
			z++;
		}
		for (int i = 0; i < ncSpaceEngineers_News.getTitles().length; i++) {
			unreadTitles[z] = ncSpaceEngineers_News.getTitles()[i];
			unreadUrls[z] = ncSpaceEngineers_News.getUrls()[i];
			unreadContents[z] = ncSpaceEngineers_News.getContents()[i];
			z++;
		}
		for (int i = 0; i < ncLayer_News.getTitles().length; i++) {
			unreadTitles[z] = ncLayer_News.getTitles()[i];
			unreadUrls[z] = ncLayer_News.getUrls()[i];
			unreadContents[z] = ncLayer_News.getContents()[i];
			z++;
		}
		
		return new NewsCollection(unreadTitles, unreadUrls, unreadContents);
	}
	
	public NewsCollection getAllUnnotified() {
		NewsCollection ncA3_News = getSingleUnnotified(getA3_News());
		NewsCollection ncA3_Devhub = getSingleUnnotified(getA3_Devhub());
		NewsCollection ncSpaceEngineers_News = getSingleUnnotified(getSpaceEngineers_News());
		NewsCollection ncLayer_News = getSingleUnnotified(getLayer_News());
		
		int unreadCounter = ncA3_News.getTitles().length + ncA3_Devhub.getTitles().length + ncSpaceEngineers_News.getTitles().length + ncLayer_News.getTitles().length;
		
		String[] unreadTitles = new String[unreadCounter];
		String[] unreadUrls = new String[unreadCounter];
		String[] unreadContents = new String[unreadCounter];
		
		int z = 0;
		for (int i = 0; i < ncA3_News.getTitles().length; i++) {
			unreadTitles[z] = ncA3_News.getTitles()[i];
			unreadUrls[z] = ncA3_News.getUrls()[i];
			unreadContents[z] = ncA3_News.getContents()[i];
			z++;
		}
		for (int i = 0; i < ncA3_Devhub.getTitles().length; i++) {
			unreadTitles[z] = ncA3_Devhub.getTitles()[i];
			unreadUrls[z] = ncA3_Devhub.getUrls()[i];
			unreadContents[z] = ncA3_Devhub.getContents()[i];
			z++;
		}
		for (int i = 0; i < ncSpaceEngineers_News.getTitles().length; i++) {
			unreadTitles[z] = ncSpaceEngineers_News.getTitles()[i];
			unreadUrls[z] = ncSpaceEngineers_News.getUrls()[i];
			unreadContents[z] = ncSpaceEngineers_News.getContents()[i];
			z++;
		}
		for (int i = 0; i < ncLayer_News.getTitles().length; i++) {
			unreadTitles[z] = ncLayer_News.getTitles()[i];
			unreadUrls[z] = ncLayer_News.getUrls()[i];
			unreadContents[z] = ncLayer_News.getContents()[i];
			z++;
		}
		
		return new NewsCollection(unreadTitles, unreadUrls, unreadContents);
	}
	
	private NewsCollection getSingleUnread(NewsCollection nColl) {
		SharedPreferences spLibrary = context.getSharedPreferences("Library", Context.MODE_PRIVATE);
		
		String[] tempTitles = new String[nColl.getTitles().length];
		String[] tempUrls = new String[nColl.getTitles().length];
		String[] tempContents = new String[nColl.getTitles().length];
		
		int unreadCounter = 0;
		
		// Package
		for (int i = 0; i < nColl.getTitles().length; i++) {
			if (!spLibrary.getBoolean(nColl.getTitles()[i], false)) {
				tempTitles[unreadCounter] = nColl.getTitles()[i];
				tempUrls[unreadCounter] = nColl.getUrls()[i];
				tempContents[unreadCounter] = nColl.getUrls()[i];
				unreadCounter++;
			}
		}
		
		String[] unreadTitles = new String[unreadCounter];
		String[] unreadUrls = new String[unreadCounter];
		String[] unreadContents = new String[unreadCounter];
		
		// Repackage
		for (int i = 0; i < unreadCounter; i++) {
			unreadTitles[i] = tempTitles[i];
			unreadUrls[i] = tempUrls[i];
			unreadContents[i] = tempContents[i];
		}
		
		return new NewsCollection(unreadTitles, unreadUrls, unreadContents);
	}
	
	private NewsCollection getSingleUnnotified(NewsCollection nColl) {
		SharedPreferences spLibrary = context.getSharedPreferences("Notified", Context.MODE_PRIVATE);
		
		String[] tempTitles = new String[nColl.getTitles().length];
		String[] tempUrls = new String[nColl.getTitles().length];
		String[] tempContents = new String[nColl.getTitles().length];
		
		int unreadCounter = 0;
		
		// Package
		for (int i = 0; i < nColl.getTitles().length; i++) {
			if (!spLibrary.getBoolean(nColl.getTitles()[i], false)) {
				tempTitles[unreadCounter] = nColl.getTitles()[i];
				tempUrls[unreadCounter] = nColl.getUrls()[i];
				tempContents[unreadCounter] = nColl.getUrls()[i];
				unreadCounter++;
			}
		}
		
		String[] unreadTitles = new String[unreadCounter];
		String[] unreadUrls = new String[unreadCounter];
		String[] unreadContents = new String[unreadCounter];
		
		// Repackage
		for (int i = 0; i < unreadCounter; i++) {
			unreadTitles[i] = tempTitles[i];
			unreadUrls[i] = tempUrls[i];
			unreadContents[i] = tempContents[i];
		}
		
		return new NewsCollection(unreadTitles, unreadUrls, unreadContents);
	}

    public void setAllNotified(NewsCollection nColl) {
        String[] filtered = nColl.getTitles();
        SharedPreferences spLibrary = context.getSharedPreferences("Notified", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spLibrary.edit();
        for (int i = 0; i < filtered.length; i++) {
            editor.putBoolean(filtered[i], true);
        }
        editor.apply();
    }
	
	public void setAllRead() {
		setSingleRead(getA3_News());
		setSingleRead(getA3_Devhub());
		setSingleRead(getSpaceEngineers_News());
		setSingleRead(getLayer_News());
	}
	
	private void setSingleRead(NewsCollection nColl) {
		SharedPreferences spLibrary = context.getSharedPreferences("Library", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = spLibrary.edit();
		for (int i = 0; i < nColl.getTitles().length; i++) {
			editor.putBoolean(nColl.getTitles()[i], true);
		}
		editor.apply();
	}

}
