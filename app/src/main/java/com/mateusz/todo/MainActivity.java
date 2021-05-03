package com.mateusz.todo;

import com.mateusz.todo.activity.AddNewTodo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mateusz.todo.data.DataManager;
import com.mateusz.todo.model.ToDo;


public class MainActivity extends AppCompatActivity {

    private LinearLayout contentLayout;
    private DataManager dataManager = DataManager.getInstance();
    private FloatingActionButton addNewToDoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        initData();
        initView();
    }

    private void initFields() {
        contentLayout = findViewById(R.id.contentLayout);
        addNewToDoButton = findViewById(R.id.addNewToDoButton);
        addNewToDoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewTodo.class);
            startActivity(intent);
        });
    }

    private void initData() {


    }

    private void initView() {
        int index = 0;
        contentLayout.removeAllViews();
        for (ToDo toDo : dataManager.getToDos()) {
            final View frame = getLayoutInflater().inflate(R.layout.todo_list_frame, null);
            TextView toDoName = frame.findViewById(R.id.toDoName);
            toDoName.setText(toDo.getName());
            frame.setId(index);
            contentLayout.addView(frame);
            index++;
        }
    }


}