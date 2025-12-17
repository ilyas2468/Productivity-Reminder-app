package com.example.reminders;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class FajrTasks extends AppCompatActivity {
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fajr_tasks);
        initWidgets();
        loadFromDBToMemory();
        setTaskAdapter();
        setOnClickListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_fajr_tasks), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }
    private void initWidgets() {

        taskListView = findViewById(R.id.fajr_tasks_listview);
    }
    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateTaskListArray();
    }

    private void setTaskAdapter(){
        TaskAdapter taskAdapter = new TaskAdapter(getApplicationContext(), Task.nonDeletedTasks());
        taskListView.setAdapter(taskAdapter);
    }

    private void setOnClickListener(){
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                Task selectedTask = (Task) taskListView.getItemAtPosition(position);
                Intent editTaskIntent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                editTaskIntent.putExtra(Task.TASK_EDIT_EXTRA, selectedTask.getId());
                startActivity(editTaskIntent);
            }
        });
    }

    public void newTask(View view) {
        Intent newTaskIntent = new Intent(this, TaskDetailActivity.class);
        startActivity(newTaskIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTaskAdapter();
    }

    public void goBack(View v){
        //go back to main
        Intent f = new Intent(this, MainActivity.class);
        startActivity(f);
    }
}