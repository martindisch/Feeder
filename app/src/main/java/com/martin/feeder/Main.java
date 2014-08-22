package com.martin.feeder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.NumberPicker;


public class Main extends Activity implements ActionBar.TabListener, OnProgressChangeListener {

    private int actionsInProgress;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        actionsInProgress = 0;

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        ScheduleService();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                AlertDialog.Builder dg = new AlertDialog.Builder(this);
                final NumberPicker np = new NumberPicker(this);
                final SharedPreferences spSettings = getSharedPreferences(
                        "Settings", MODE_PRIVATE);
                np.setMinValue(1);
                np.setMaxValue(9000);
                np.setValue(spSettings.getInt("interval", 240));
                dg.setView(np);
                dg.setMessage("The interval to check for news in the background.");
                dg.setNegativeButton("Cancel", null);
                dg.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = spSettings.edit();
                        editor.putInt("interval", np.getValue());
                        editor.apply();
                        ScheduleService();
                    }

                });
                dg.show();
                break;
            case R.id.mark_read:
                final NewsSources nSources = new NewsSources(this);
                setProgressBarIndeterminateVisibility(true);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        nSources.setAllRead();
                        ((NewsFragment) findFragmentByPosition(0)).loadUnread();
                    }

                }).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("SameParameterValue")
    public Fragment findFragmentByPosition(int position) {
        SectionsPagerAdapter fragmentPagerAdapter = mSectionsPagerAdapter;
        return getFragmentManager().findFragmentByTag(
                "android:switcher:" + mViewPager.getId() + ":"
                        + fragmentPagerAdapter.getItemId(position));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void actionStarted() {
        actionsInProgress++;
    }

    @Override
    public void actionFinished() {
        actionsInProgress--;
    }

    @Override
    public boolean changeVisibility() {
        return actionsInProgress <= 0;
    }

    private void ScheduleService() {
        SharedPreferences spSettings = this.getSharedPreferences("Settings",
                Context.MODE_PRIVATE);
        Intent intent = new Intent(this, NewsCheck.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        long currentTimeMillis = System.currentTimeMillis();
        long nextUpdateTimeMillis = currentTimeMillis
                + spSettings.getInt("interval", 240) * DateUtils.MINUTE_IN_MILLIS;
        Time nextUpdateTime = new Time();
        nextUpdateTime.set(nextUpdateTimeMillis);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                nextUpdateTimeMillis, spSettings.getInt("interval", 240)
                        * DateUtils.MINUTE_IN_MILLIS, pendingIntent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch (position) {
                case 0:
                    f = new NewsFragment();
                    break;
                case 1:
                    f = new SourcesFragment();
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.titles)[position];
        }
    }

}
