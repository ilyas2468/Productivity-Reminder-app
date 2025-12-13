package com.example.reminders;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TaskDetailActivity extends AppCompatActivity {

    private EditText taskEditName, additionalNotesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_detail);
        initWidgets();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidgets() {
        taskEditName = findViewById(R.id.taskName);
        additionalNotesEditText = findViewById(R.id.additionalNotes);
    }

    public void addTask(View view) {
        String taskName = String.valueOf(taskEditName.getText());
        String additionalNotes = String.valueOf(additionalNotesEditText.getText());

        int id = Task.taskArrayList.size();
        Task newTask = new Task(id,taskName,additionalNotes);
        Task.taskArrayList.add(newTask);
        finish();
    }
}