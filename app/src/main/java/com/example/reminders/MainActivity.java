package com.example.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String PRAYER_EXTRA = "prayer_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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