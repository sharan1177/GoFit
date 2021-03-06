package com.example.fitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiverDinner extends BroadcastReceiver {

    String TAG = "AlarmReceiverDinner";

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalDataDinner localDataDinner = new LocalDataDinner(context);
                NotificationSchedulerDinner.setReminder(context, AlarmReceiverDinner.class, localDataDinner.get_hourd(), localDataDinner.get_mind());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        NotificationSchedulerDinner.showNotificationDinner(context, remind_pg.class,
                "Dinner Reminder", "it's time to take your Dinner");

    }
}
