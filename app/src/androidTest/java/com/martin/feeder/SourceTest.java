package com.martin.feeder;

import android.test.AndroidTestCase;
import android.util.Log;

public class SourceTest extends AndroidTestCase {

    private NewsSources nSources;
    private NewsCollection nColl;
    String tag;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        nSources = new NewsSources(getContext());
    }

    public void testA3News() {
        tag = "A3N";
        nColl = nSources.getA3_News();
        Log.d(tag, nColl.getTitles()[0]);
        Log.d(tag, nColl.getContents()[0]);
        Log.d(tag, nColl.getUrls()[0]);
    }
    public void testA3Devhub() {
        tag = "A3D";
        nColl = nSources.getA3_Devhub();
        Log.d(tag, nColl.getTitles()[0]);
        Log.d(tag, nColl.getContents()[0]);
        Log.d(tag, nColl.getUrls()[0]);

    }
    public void testSpaceEngineers() {
        tag = "SEng";
        nColl = nSources.getSpaceEngineers_News();
        Log.d(tag, nColl.getTitles()[0]);
        Log.d(tag, nColl.getContents()[0]);
        Log.d(tag, nColl.getUrls()[0]);

    }
    public void testLayer() {
        tag = "Layer";
        nColl = nSources.getLayer_News();
        Log.d(tag, nColl.getTitles()[0]);
        Log.d(tag, nColl.getContents()[0]);
        Log.d(tag, nColl.getUrls()[0]);
    }
}
