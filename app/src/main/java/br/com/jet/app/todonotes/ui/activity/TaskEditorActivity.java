package br.com.jet.app.todonotes.ui.activity;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.jet.app.todonotes.R;
import br.com.jet.app.todonotes.database.TaskDatabase;
import br.com.jet.app.todonotes.model.Task;

public class TaskEditorActivity extends AppCompatActivity {

    private Spinner taskPrioritySelector;

    private ArrayAdapter<String> adapter;
    private List<String> priorities;

    private EditText taskTitle, taskContent;
    private Button saveTaskButton, cancelButton;

    private Task task;

    private TaskDatabase database;

    private CardView cardViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);

        task = (Task) getIntent().getSerializableExtra("task");

        database = new TaskDatabase(this);

        priorities = new ArrayList<>();
        priorities.add(getResources().getString(R.string.priority_high));
        priorities.add(getResources().getString(R.string.priority_medium));
        priorities.add(getResources().getString(R.string.priority_low));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorities);
        taskPrioritySelector = findViewById(R.id.task_priority_spinner);
        taskPrioritySelector.setAdapter(adapter);
        taskPrioritySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO - Change this switch by putting this on Task Object
                switch (position) {
                    case 0:
                        cardViewLayout.setCardBackgroundColor(ContextCompat.getColor(TaskEditorActivity.this, R.color.highPriority));
                        break;

                    case 1:
                        cardViewLayout.setCardBackgroundColor(ContextCompat.getColor(TaskEditorActivity.this, R.color.mediumPriority));
                        break;

                    case 2:
                        cardViewLayout.setCardBackgroundColor(ContextCompat.getColor(TaskEditorActivity.this, R.color.lowPriority));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        taskTitle = findViewById(R.id.task_title);
        taskContent = findViewById(R.id.task_content);
        saveTaskButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        cardViewLayout = findViewById(R.id.editor_layout_cardview);

        if (task != null) {
            taskTitle.setText(task.getTitle());
            taskContent.setText(task.getContent());
            taskPrioritySelector.setSelection(task.getPriority());
            //TODO - Change this switch by putting this on Task Object
            switch (task.getPriority()) {
                case 0:
                    cardViewLayout.setCardBackgroundColor(ContextCompat.getColor(this, R.color.highPriority));
                    break;

                case 1:
                    cardViewLayout.setCardBackgroundColor(ContextCompat.getColor(this, R.color.mediumPriority));
                    break;

                case 2:
                    cardViewLayout.setCardBackgroundColor(ContextCompat.getColor(this, R.color.lowPriority));
                    break;
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!taskTitle.getText().toString().isEmpty() || !taskContent.getText().toString().isEmpty()) {
                    if (task != null) {
                        Toast.makeText(TaskEditorActivity.this, R.string.updating, Toast.LENGTH_SHORT).show();
                        database.update(new Task(task.getId(), taskPrioritySelector.getSelectedItemPosition(), taskTitle.getText().toString(), taskContent.getText().toString()));
                        finish();
                    } else {
                        database.insert(new Task(taskPrioritySelector.getSelectedItemPosition(), taskTitle.getText().toString(), taskContent.getText().toString()));
                        Toast.makeText(TaskEditorActivity.this, R.string.saving, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TaskEditorActivity.this);
                    alert.setTitle(R.string.message_title);
                    alert.setMessage(R.string.message_content);
                    alert.setNeutralButton(R.string.neutral_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.create();
                    alert.show();
                }
            }
        });


    }
}
