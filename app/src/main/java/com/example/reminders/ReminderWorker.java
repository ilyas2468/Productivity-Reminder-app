package com.example.reminders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {

    private static final String CHANNEL_ID = "reminder_channel";
    private static final int NOTIFICATION_ID = 1001;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get a random reminder from database
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getApplicationContext());
        Reminder reminder = sqLiteManager.getRandomReminder();

        if (reminder != null) {
            showNotification(reminder);
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    private void showNotification(Reminder reminder) {
        Context context = getApplicationContext();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Islamic Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Reminders from Quran and Sunnah");
            notificationManager.createNotificationChannel(channel);
        }

        // Create intent to open detail activity with full text
        Intent intent = new Intent(context, ReminderDetailActivity.class);
        intent.putExtra(ReminderDetailActivity.EXTRA_REMINDER_TEXT, reminder.getText());
        intent.putExtra(ReminderDetailActivity.EXTRA_REMINDER_SOURCE, reminder.getSource());
        intent.putExtra(ReminderDetailActivity.EXTRA_REMINDER_REFERENCE, reminder.getReference());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Truncate text for notification preview (first 150 characters)
        String previewText = reminder.getText();
        if (previewText.length() > 150) {
            previewText = previewText.substring(0, 150) + "...";
        }

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.star_on) // Use your app icon here
                .setContentTitle(reminder.getSource() + " Reminder")
                .setContentText(previewText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(previewText + "\n\nTap to read full reminder")
                        .setBigContentTitle(reminder.getSource() + " Reminder")
                        .setSummaryText(reminder.getReference()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}