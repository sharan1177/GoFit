package com.example.fitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiverBF extends BroadcastReceiver {

    String TAG = "AlarmReceiverBF";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalDataBF localDataBF = new LocalDataBF(context);
                NotificationSchedulerBF.setReminder(context, AlarmReceiverBF.class, localDataBF.get_hour(), localDataBF.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        NotificationSchedulerBF.showNotificationBF(context, remind_pg.class,
                "BreakFast Reminder", "it's time to take your Breakfast");

    }

}
