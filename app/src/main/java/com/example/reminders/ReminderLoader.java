package com.example.reminders;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReminderLoader {

    private static final String PREFS_NAME = "ReminderPrefs";
    private static final String KEY_REMINDERS_LOADED = "reminders_loaded";

    public static void loadRemindersFromJSON(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Check if reminders are already loaded
        boolean remindersLoaded = prefs.getBoolean(KEY_REMINDERS_LOADED, false);
        if (remindersLoaded) {
            return; // Already loaded, skip
        }

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);

        try {
            // Read JSON file from assets
            InputStream inputStream = context.getAssets().open("reminders.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            reader.close();

            // Parse JSON
            JSONArray remindersArray = new JSONArray(jsonString.toString());

            // Insert each reminder into database
            for (int i = 0; i < remindersArray.length(); i++) {
                JSONObject reminderObj = remindersArray.getJSONObject(i);

                String text = reminderObj.getString("text");
                String source = reminderObj.getString("source");
                String reference = reminderObj.getString("reference");

                Reminder reminder = new Reminder(0, text, source, reference);
                sqLiteManager.addReminderToDatabase(reminder);
            }

            // Mark as loaded
            prefs.edit().putBoolean(KEY_REMINDERS_LOADED, true).apply();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean areRemindersLoaded(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_REMINDERS_LOADED, false);
    }
}