package com.martin.feeder;

import android.test.AndroidTestCase;

public class CollectionTest extends AndroidTestCase {

    private NewsCollection testCollection;

    private void prep() {
        String[] titles = { "Title 1", "Title 2", "Title 3", "Title 4" };
        String[] urls = { "Url1", "Url2", "Url3", "Url4" };
        String[] contents = { "C1", "C2", "C3", "C4" };
        testCollection = new NewsCollection(titles, urls, contents);
    }

    public void testRemoveFirst() {
        prep();
        testCollection.removeItem(0);
        assertEquals(testCollection.getTitles()[0], "Title 2");
        assertEquals(testCollection.getTitles()[testCollection.getTitles().length - 1], "Title 4");
    }

    public void testRemoveLast() {
        prep();
        testCollection.removeItem(3);
        assertEquals(testCollection.getTitles()[0], "Title 1");
        assertEquals(testCollection.getTitles()[testCollection.getTitles().length - 1], "Title 3");
    }

    public void testRemoveMiddle() {
        prep();
        testCollection.removeItem(1);
        assertEquals(testCollection.getTitles()[0], "Title 1");
        assertEquals(testCollection.getTitles()[testCollection.getTitles().length - 1], "Title 4");
        assertEquals(testCollection.getTitles()[1], "Title 2");
    }

    public void testRemoveTwo() {
        prep();
        testCollection.removeItem(0);
        testCollection.removeItem(1);
        assertEquals(testCollection.getTitles()[0], "Title 2");
        assertEquals(testCollection.getTitles()[1], "Title 4");
    }


}
