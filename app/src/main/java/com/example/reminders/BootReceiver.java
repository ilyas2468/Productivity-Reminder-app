package com.example.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BootReceiver extends BroadcastReceiver {

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Check if notifications were enabled before reboot
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false);

            if (notificationsEnabled) {
                // Reschedule reminders
                PeriodicWorkRequest reminderWork =
                        new PeriodicWorkRequest.Builder(
                                ReminderWorker.class,
                                90, TimeUnit.MINUTES)
                                .build();

                WorkManager.getInstance(context)
                        .enqueueUniquePeriodicWork(
                                "islamic_reminders",
                                ExistingPeriodicWorkPolicy.KEEP,
                                reminderWork);
            }
        }
    }
}