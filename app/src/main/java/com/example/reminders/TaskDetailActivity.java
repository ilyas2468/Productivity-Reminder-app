package com.example.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText taskEditName, additionalNotesEditText;

    private Button deleteTask;

    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_detail);
        initWidgets();
        checkForEditNote();
    }



    private void initWidgets() {
        taskEditName = findViewById(R.id.taskName);
        additionalNotesEditText = findViewById(R.id.additionalNotes);
        deleteTask = findViewById(R.id.deletedTask);
    }

    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedTaskID = previousIntent.getIntExtra(Task.TASK_EDIT_EXTRA, -1);
        selectedTask = Task.getTaskForID(passedTaskID);

        if (selectedTask != null){
            taskEditName.setText(selectedTask.getTaskName());
            additionalNotesEditText.setText((selectedTask.getDescription()));
        }
        else{
            deleteTask.setVisibility(View.INVISIBLE);
        }
    }

    public void addTask(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String taskName = String.valueOf(taskEditName.getText());
        String additionalNotes = String.valueOf(additionalNotesEditText.getText());

        if(selectedTask == null) {
            int id = Task.taskArrayList.size();
            Task newTask = new Task(id, taskName, additionalNotes);
            Task.taskArrayList.add(newTask);
            sqLiteManager.addTaskToDatabase(newTask);
        }
        else{
            selectedTask.setTaskName(taskName);
            selectedTask.setDescription(additionalNotes);
            sqLiteManager.updateTaskInDB(selectedTask);
        }
        finish();
    }

    public void deleteTask(View view) {
        selectedTask.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateTaskInDB(selectedTask);
        finish();
    }
}