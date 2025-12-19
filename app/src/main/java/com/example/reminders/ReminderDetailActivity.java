package com.example.reminders;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReminderDetailActivity extends AppCompatActivity {

    public static final String EXTRA_REMINDER_TEXT = "reminder_text";
    public static final String EXTRA_REMINDER_SOURCE = "reminder_source";
    public static final String EXTRA_REMINDER_REFERENCE = "reminder_reference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);

        // Get data from intent
        String text = getIntent().getStringExtra(EXTRA_REMINDER_TEXT);
        String source = getIntent().getStringExtra(EXTRA_REMINDER_SOURCE);
        String reference = getIntent().getStringExtra(EXTRA_REMINDER_REFERENCE);

        // Set up views
        TextView titleTextView = findViewById(R.id.reminderTitle);
        TextView contentTextView = findViewById(R.id.reminderContent);
        TextView referenceTextView = findViewById(R.id.reminderReference);

        titleTextView.setText(source + " Reminder");
        contentTextView.setText(text);
        referenceTextView.setText("Source: " + reference);
    }
}