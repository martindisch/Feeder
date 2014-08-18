package com.martin.feeder;

import android.test.AndroidTestCase;

public class CollectionTest extends AndroidTestCase {

    private NewsCollection testCollection;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        String[] titles = { "Title 1", "Title 2", "Title 3", "Title 4" };
        String[] urls = { "Url1", "Url2", "Url3", "Url4" };
        String[] contents = { "C1", "C2", "C3", "C4" };
        testCollection = new NewsCollection(titles, urls, contents);
    }
}
