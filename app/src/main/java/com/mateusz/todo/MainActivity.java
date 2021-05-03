package com.mateusz.todo;

import com.mateusz.todo.activity.AddNewTodo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mateusz.todo.activity.Mode;
import com.mateusz.todo.data.DataManager;
import com.mateusz.todo.model.ToDo;


public class MainActivity extends AppCompatActivity {

    private LinearLayout contentLayout;
    private DataManager dataManager = DataManager.getInstance();
    private FloatingActionButton addNewToDoButton;
    private TextView emptyListLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        initView();
    }

    private void initFields() {
        contentLayout = findViewById(R.id.contentLayout);
        emptyListLabel = findViewById(R.id.emptyListLabel);
        addNewToDoButton = findViewById(R.id.addNewToDoButton);
        addNewToDoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewTodo.class);
            intent.putExtra("mode", Mode.ADD);
            startActivity(intent);
        });
    }


    private void initView() {

        if(dataManager.getToDos().size() == 0 ){
            emptyListLabel.setVisibility(View.VISIBLE);
        }else {
            emptyListLabel.setVisibility(View.GONE);
        }

        int index = 0;
        contentLayout.removeAllViews();
        for (ToDo toDo : dataManager.getToDos()) {
            final View frame = getLayoutInflater().inflate(R.layout.todo_list_frame, null);
            frame.findViewById(R.id.todoPanel).setOnClickListener(v -> {
                Intent intent = new Intent(this, AddNewTodo.class);
                intent.putExtra("mode", Mode.EDIT);
                intent.putExtra("todo", toDo);
                startActivity(intent);
            });

            TextView toDoName = frame.findViewById(R.id.toDoName);
            if(toDo.isPriority()){
                toDoName.setTextColor(Color.BLACK);
                toDoName.setTypeface(null, Typeface.BOLD);
            }
            frame.findViewById(R.id.isDone).setOnClickListener(v -> {
                dataManager.markAsDone(toDo);
                initView();
                Toast.makeText(this, "Pomyślnie oznaczone zadanie jako wykonane", Toast.LENGTH_SHORT).show();

            });

            toDoName.setText(toDo.getName());
            frame.setId(index);
            contentLayout.addView(frame);
            index++;
        }
    }


}