package com.martin.feeder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.text.format.Time;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Schedule Service
        SharedPreferences spSettings = context.getSharedPreferences("Settings",
                Context.MODE_PRIVATE);
        Intent intent1 = new Intent(context, NewsCheck.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        long currentTimeMillis = System.currentTimeMillis();
        long nextUpdateTimeMillis = currentTimeMillis
                + spSettings.getInt("interval", 240) * DateUtils.MINUTE_IN_MILLIS;
        Time nextUpdateTime = new Time();
        nextUpdateTime.set(nextUpdateTimeMillis);

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                nextUpdateTimeMillis, spSettings.getInt("interval", 240)
                        * DateUtils.MINUTE_IN_MILLIS, pendingIntent);
    }

}
