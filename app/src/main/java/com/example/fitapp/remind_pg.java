package com.example.fitapp;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class remind_pg extends AppCompatActivity {

    String TAG = "Reminder";
    LocalDataBF localDataBF;
    LocalDataLunch localDataLunch;
    LocalDataSnacks localDataSnacks;
    LocalDataDinner localDataDinner;

    Switch reminderSwitch;
    TextView tvTime,tvTime_lunch,tvTime_snacks ,tvTime_dinner;

    LinearLayout ll_set_time, ll_set_time_lunch, ll_set_time_snacks ,ll_set_time_dinner;

    int hour, min,hourl,minl, hours,mins ,hourd,mind;

    ClipboardManager myClipboard;

    private long backPressedTime = 0;

    private BottomNavigationView btnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_pg);

        Toolbar toolbar =  findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        localDataDinner = new LocalDataDinner(getApplicationContext());
        localDataSnacks = new LocalDataSnacks(getApplicationContext());
        localDataLunch = new LocalDataLunch(getApplicationContext());
        localDataBF = new LocalDataBF(getApplicationContext());

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ll_set_time = (LinearLayout) findViewById(R.id.time_pic);
        ll_set_time_lunch = (LinearLayout) findViewById(R.id.time_piclunch);
        ll_set_time_snacks = (LinearLayout) findViewById(R.id.time_picsnacks);
        ll_set_time_dinner = (LinearLayout) findViewById(R.id.time_picdinner);

        tvTime = (TextView) findViewById(R.id.bftime);
        tvTime_lunch = (TextView) findViewById(R.id.lctime);
        tvTime_snacks = (TextView) findViewById(R.id.sncktime);
        tvTime_dinner = (TextView) findViewById(R.id.dntime);

        reminderSwitch = findViewById(R.id.mrtstart);

        hour = localDataBF.get_hour();
        min = localDataBF.get_min();
        hourl = localDataLunch.get_hourl();
        minl = localDataLunch.get_minl();
        hours = localDataSnacks.get_hours();
        mins = localDataSnacks.get_mins();
        hourd = localDataDinner.get_hourd();
        mind = localDataDinner.get_mind();

        tvTime.setText(getFormatedTime(hour, min));
        tvTime_lunch.setText(getFormatedTime(hourl, minl));
        tvTime_snacks.setText(getFormatedTime(hours, mins));
        tvTime_dinner.setText(getFormatedTime(hourd, mind));

        reminderSwitch.setChecked(localDataBF.getReminderStatus());
        reminderSwitch.setChecked(localDataLunch.getReminderStatus());
        reminderSwitch.setChecked(localDataSnacks.getReminderStatus());
        reminderSwitch.setChecked(localDataDinner.getReminderStatus());

        if (!localDataBF.getReminderStatus())
            ll_set_time.setAlpha(0.4f);

        if (!localDataLunch.getReminderStatus())
            ll_set_time_lunch.setAlpha(0.4f);

        if (!localDataSnacks.getReminderStatus())
            ll_set_time_snacks.setAlpha(0.4f);

        if (!localDataDinner.getReminderStatus())
            ll_set_time_dinner.setAlpha(0.4f);

        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localDataBF.setReminderStatus(isChecked);
                localDataLunch.setReminderStatus(isChecked);
                localDataSnacks.setReminderStatus(isChecked);
                localDataDinner.setReminderStatus(isChecked);

                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: true");
                    NotificationSchedulerBF.setReminder(remind_pg.this, AlarmReceiverBF.class, localDataBF.get_hour(), localDataBF.get_min());
                    ll_set_time.setAlpha(1f);
                }
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: true");
                    NotificationSchedulerLunch.setReminder(remind_pg.this, AlarmReceiverLunch.class, localDataLunch.get_hourl(), localDataLunch.get_minl());
                    ll_set_time_lunch.setAlpha(1f);
                }
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: true");
                    NotificationSchedulerSnacks.setReminder(remind_pg.this, AlarmReceiverSnacks.class, localDataSnacks.get_hours(), localDataSnacks.get_mins());
                    ll_set_time_snacks.setAlpha(1f);
                }
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: true");
                    NotificationSchedulerDinner.setReminder(remind_pg.this, AlarmReceiverDinner.class, localDataDinner.get_hourd(), localDataDinner.get_mind());
                    ll_set_time_dinner.setAlpha(1f);
                }

                if (!isChecked) {
                    Log.d(TAG, "onCheckedChanged: false");
                    NotificationSchedulerBF.cancelReminder(remind_pg.this, AlarmReceiverBF.class);
                    ll_set_time.setAlpha(0.4f);
                }
                if (!isChecked) {
                    Log.d(TAG, "onCheckedChanged: false");
                    NotificationSchedulerLunch.cancelReminder(remind_pg.this, AlarmReceiverLunch.class);
                    ll_set_time_lunch.setAlpha(0.4f);
                }
                if (!isChecked) {
                    Log.d(TAG, "onCheckedChanged: false");
                    NotificationSchedulerSnacks.cancelReminder(remind_pg.this, AlarmReceiverSnacks.class);
                    ll_set_time_snacks.setAlpha(0.4f);
                }
                if (!isChecked) {
                    Log.d(TAG, "onCheckedChanged: false");
                    NotificationSchedulerDinner.cancelReminder(remind_pg.this, AlarmReceiverDinner.class);
                    ll_set_time_dinner.setAlpha(0.4f);
                }

            }
        });

        ll_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localDataBF.getReminderStatus())
                    showTimePickerDialog(localDataBF.get_hour(), localDataBF.get_min());
            }
        });
        ll_set_time_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localDataLunch.getReminderStatus())
                    showTimePickerDialogLunch(localDataLunch.get_hourl(), localDataLunch.get_minl());
            }
        });
        ll_set_time_snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localDataSnacks.getReminderStatus())
                    showTimePickerDialogSnacks(localDataSnacks.get_hours(), localDataSnacks.get_mins());
            }
        });
        ll_set_time_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localDataDinner.getReminderStatus())
                    showTimePickerDialogDinner(localDataDinner.get_hourd(), localDataDinner.get_mind());
            }
        });





    }


    private void showTimePickerDialog(int h, int m) {



        TimePickerDialog builder = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        Log.d(TAG, "onTimeSet: hour " + hour);
                        Log.d(TAG, "onTimeSet: min " + min);
                        localDataBF.set_hour(hour);
                        localDataBF.set_min(min);
                        tvTime.setText(getFormatedTime(hour, min));
                        NotificationSchedulerBF.setReminder(remind_pg.this, AlarmReceiverBF.class, localDataBF.get_hour(), localDataBF.get_min());


                    }
                }, h, m, false);

        builder.show();

    }

    private void showTimePickerDialogLunch(int hl, int ml) {
        TimePickerDialog builder = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourl, int minl) {
                        Log.d(TAG, "onTimeSet: hourl " + hourl);
                        Log.d(TAG, "onTimeSet: minl " + minl);
                        localDataLunch.set_hourl(hourl);
                        localDataLunch.set_minl(minl);
                        tvTime_lunch.setText(getFormatedTimeLunch(hourl, minl));
                        NotificationSchedulerLunch.setReminder(remind_pg.this, AlarmReceiverLunch.class, localDataLunch.get_hourl(), localDataLunch.get_minl());

                    }
                }, hl, ml, false);

        builder.show();
    }

    private void showTimePickerDialogSnacks(int hs, int ms) {
        TimePickerDialog builder = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                        Log.d(TAG, "onTimeSet: hours " + hours);
                        Log.d(TAG, "onTimeSet: mins " + mins);
                        localDataSnacks.set_hours(hours);
                        localDataSnacks.set_mins(mins);
                        tvTime_snacks.setText(getFormatedTimeSnacks(hours, mins));
                        NotificationSchedulerSnacks.setReminder(remind_pg.this, AlarmReceiverSnacks.class, localDataSnacks.get_hours(), localDataSnacks.get_mins());

                    }
                }, hs, ms, false);

        builder.show();
    }

    private void showTimePickerDialogDinner(int hd, int md) {
        TimePickerDialog builder = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourd, int mind) {
                        Log.d(TAG, "onTimeSet: hourd " + hourd);
                        Log.d(TAG, "onTimeSet: mind " + mind);
                        localDataDinner.set_hourd(hourd);
                        localDataDinner.set_mind(mind);
                        tvTime_dinner.setText(getFormatedTimeDinner(hourd, mind));
                        NotificationSchedulerDinner.setReminder(remind_pg.this, AlarmReceiverDinner.class, localDataDinner.get_hourd(), localDataDinner.get_mind());

                    }
                }, hd, md, false);

        builder.show();
    }

    private String getFormatedTimeLunch(int hl, int ml) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = hl + ":" + ml;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }


    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;

    }

    public String getFormatedTimeSnacks(int hs, int ms) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = hs + ":" + ms;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;

    }

    public String getFormatedTimeDinner(int hd, int md) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = hd + ":" + md;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;

    }

    @TargetApi(Build.VERSION_CODES.O)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            return getResources().getConfiguration().locale;
        }
    }


@Override
public boolean onOptionsItemSelected(MenuItem item) //toolbar back
{
    if(item.getItemId() == android.R.id.home){
        finish();
    }
    return super.onOptionsItemSelected(item);
}
}
