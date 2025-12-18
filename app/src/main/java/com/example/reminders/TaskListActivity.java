package com.example.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TaskListActivity extends AppCompatActivity {
    private ListView taskListView;
    private String prayerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_list);

        // Get the prayer name from the intent
        prayerName = getIntent().getStringExtra(MainActivity.PRAYER_EXTRA);
        if (prayerName == null) {
            prayerName = "Fajr"; // Default fallback
        }

        // Set the title dynamically
        setTitle(prayerName + " Tasks");

        initWidgets();
        loadFromDBToMemory();
        setTaskAdapter();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_task_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        taskListView = findViewById(R.id.tasks_listview);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateTaskListArray();
    }

    private void setTaskAdapter() {
        // Filter tasks by prayer name
        TaskAdapter taskAdapter = new TaskAdapter(getApplicationContext(),
                Task.nonDeletedTasksForPrayer(prayerName));
        taskListView.setAdapter(taskAdapter);
    }

    private void setOnClickListener() {
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Task selectedTask = (Task) taskListView.getItemAtPosition(position);
                Intent editTaskIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                editTaskIntent.putExtra(Task.TASK_EDIT_EXTRA, selectedTask.getId());
                editTaskIntent.putExtra(MainActivity.PRAYER_EXTRA, prayerName);
                startActivity(editTaskIntent);
            }
        });
    }

    public void newTask(View view) {
        Intent newTaskIntent = new Intent(this, TaskDetailActivity.class);
        newTaskIntent.putExtra(MainActivity.PRAYER_EXTRA, prayerName);
        startActivity(newTaskIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTaskAdapter();
    }

    public void goBack(View v) {
        finish(); // This will return to MainActivity
    }
}