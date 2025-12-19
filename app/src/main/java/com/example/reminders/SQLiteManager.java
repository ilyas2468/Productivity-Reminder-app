package com.example.reminders;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "TaskDB";
    private static final int DATABASE_VERSION = 3; // Increment for reminders table
    private static final String TABLE_NAME = "Task";
    private static final String REMINDERS_TABLE = "Reminders";
    private static final String COUNTER = "Counter";

    // Task table fields
    private static final String ID_FIELD = "id";
    private static final String TASK_FIELD = "taskName";
    private static final String DESC_FIELD = "desc";
    private static final String DELETED_FIELD = "deleted";
    private static final String PRAYER_FIELD = "prayer";

    // Reminders table fields
    private static final String REMINDER_ID = "id";
    private static final String REMINDER_TEXT = "text";
    private static final String REMINDER_SOURCE = "source";
    private static final String REMINDER_REFERENCE = "reference";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);
        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create Task table
        StringBuilder sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TASK_FIELD)
                .append(" TEXT, ")
                .append(DESC_FIELD)
                .append(" TEXT, ")
                .append(DELETED_FIELD)
                .append(" TEXT, ")
                .append(PRAYER_FIELD)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(sql.toString());

        // Create Reminders table
        StringBuilder remindersSql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(REMINDERS_TABLE)
                .append("(")
                .append(REMINDER_ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(REMINDER_TEXT)
                .append(" TEXT NOT NULL, ")
                .append(REMINDER_SOURCE)
                .append(" TEXT, ")
                .append(REMINDER_REFERENCE)
                .append(" TEXT)");
        sqLiteDatabase.execSQL(remindersSql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add prayer column to existing table
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + PRAYER_FIELD + " TEXT");
        }
        if (oldVersion < 3) {
            // Create Reminders table
            StringBuilder remindersSql = new StringBuilder()
                    .append("CREATE TABLE ")
                    .append(REMINDERS_TABLE)
                    .append("(")
                    .append(REMINDER_ID)
                    .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                    .append(REMINDER_TEXT)
                    .append(" TEXT NOT NULL, ")
                    .append(REMINDER_SOURCE)
                    .append(" TEXT, ")
                    .append(REMINDER_REFERENCE)
                    .append(" TEXT)");
            sqLiteDatabase.execSQL(remindersSql.toString());
        }
    }

    // ==================== TASK METHODS ====================

    public void addTaskToDatabase(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, task.getId());
        contentValues.put(TASK_FIELD, task.getTaskName());
        contentValues.put(DESC_FIELD, task.getDescription());
        contentValues.put(DELETED_FIELD, getStringFromDate(task.getDeleted()));
        contentValues.put(PRAYER_FIELD, task.getPrayer());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateTaskListArray(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Task.taskArrayList.clear();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String taskName = result.getString(2);
                    String desc = result.getString(3);
                    String stringDeleted = result.getString(4);
                    Date deleted = getDateFromString(stringDeleted);
                    String prayer = result.getString(5);

                    Task task = new Task(id, taskName, desc, deleted, prayer);
                    Task.taskArrayList.add(task);
                }
            }
        }
    }

    public void updateTaskInDB(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, task.getId());
        contentValues.put(TASK_FIELD, task.getTaskName());
        contentValues.put(DESC_FIELD, task.getDescription());
        contentValues.put(DELETED_FIELD, getStringFromDate(task.getDeleted()));
        contentValues.put(PRAYER_FIELD, task.getPrayer());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ",
                new String[]{String.valueOf(task.getId())});
    }

    // ==================== REMINDER METHODS ====================

    public void addReminderToDatabase(Reminder reminder){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_TEXT, reminder.getText());
        contentValues.put(REMINDER_SOURCE, reminder.getSource());
        contentValues.put(REMINDER_REFERENCE, reminder.getReference());

        sqLiteDatabase.insert(REMINDERS_TABLE, null, contentValues);
    }

    public int getRemindersCount(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + REMINDERS_TABLE, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Reminder getRandomReminder(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Reminder reminder = null;

        try (Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + REMINDERS_TABLE + " ORDER BY RANDOM() LIMIT 1", null)){
            if(cursor.moveToFirst()){
                int id = cursor.getInt(0);
                String text = cursor.getString(1);
                String source = cursor.getString(2);
                String reference = cursor.getString(3);
                reminder = new Reminder(id, text, source, reference);
            }
        }
        return reminder;
    }

    public void clearAllReminders(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(REMINDERS_TABLE, null, null);
    }

    // ==================== UTILITY METHODS ====================

    private String getStringFromDate(Date date) {
        if(date == null)
            return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string){
        try{
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e){
            return null;
        }
    }
}