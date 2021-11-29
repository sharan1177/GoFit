package com.example.fitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiverSnacks extends BroadcastReceiver {

    String TAG = "AlarmReceiverSnacks";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalDataSnacks localDataSnacks = new LocalDataSnacks(context);
                NotificationSchedulerSnacks.setReminder(context, AlarmReceiverSnacks.class, localDataSnacks.get_hours(), localDataSnacks.get_mins());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        NotificationSchedulerSnacks.showNotificationSnacks(context, remind_pg.class,
                "Snacks Reminder", "it's time to take your Snacks");

    }
}
