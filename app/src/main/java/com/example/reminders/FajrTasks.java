package com.example.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void goBack(View v){
        //go back to main
        Intent f = new Intent(this, MainActivity.class);
        startActivity(f);
    }

    private void initWidgets() {
        taskListView = findViewById(R.id.fajr_tasks_listview);
    }


    private void setTaskAdapter(){
        TaskAdapter taskAdapter = new TaskAdapter(getApplicationContext(), Task.taskArrayList);
        taskListView.setAdapter(taskAdapter);
    }

    public void newTask(View view) {
        Intent newTaskIntent = new Intent(this, TaskDetailActivity.class);
        startActivity(newTaskIntent);
    }
}