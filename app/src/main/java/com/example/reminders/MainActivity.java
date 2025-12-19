package com.example.reminders;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String PRAYER_EXTRA = "prayer_name";
    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    private Switch reminderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Load reminders from JSON on first launch
        ReminderLoader.loadRemindersFromJSON(this);

        // Initialize switch
        reminderSwitch = findViewById(R.id.reminderSwitch);

        // Load saved switch state
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false);
        reminderSwitch.setChecked(notificationsEnabled);

        // Set switch listener
        reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Check for notification permission on Android 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                                NOTIFICATION_PERMISSION_CODE);
                        return;
                    }
                }
                scheduleReminders();
                saveNotificationState(true);
                Toast.makeText(this, "Reminders enabled (every 90 minutes)", Toast.LENGTH_SHORT).show();
            } else {
                cancelReminders();
                saveNotificationState(false);
                Toast.makeText(this, "Reminders disabled", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void scheduleReminders() {
        PeriodicWorkRequest reminderWork =
                new PeriodicWorkRequest.Builder(
                        ReminderWorker.class,
                        90, TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "islamic_reminders",
                        ExistingPeriodicWorkPolicy.KEEP,
                        reminderWork);
    }

    private void cancelReminders() {
        WorkManager.getInstance(this).cancelUniqueWork("islamic_reminders");
    }

    private void saveNotificationState(boolean enabled) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled).apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleReminders();
                saveNotificationState(true);
                Toast.makeText(this, "Reminders enabled (every 90 minutes)", Toast.LENGTH_SHORT).show();
            } else {
                reminderSwitch.setChecked(false);
                Toast.makeText(this, "Notification permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Generic method to launch task list for any prayer
    private void launchPrayerTasks(String prayerName) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra(PRAYER_EXTRA, prayerName);
        startActivity(intent);
    }

    public void launchFajrTasks(View v) {
        launchPrayerTasks("Fajr");
    }

    public void launchDhuhrTasks(View v) {
        launchPrayerTasks("Dhuhr");
    }

    public void launchAsrTasks(View v) {
        launchPrayerTasks("Asr");
    }

    public void launchMaghribTasks(View v) {
        launchPrayerTasks("Maghrib");
    }

    public void launchIshaTasks(View v) {
        launchPrayerTasks("Isha");
    }
}