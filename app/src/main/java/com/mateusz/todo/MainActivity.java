package com.mateusz.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mateusz.todo.data.DataManager;
import com.mateusz.todo.model.ToDo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

     private LinearLayout contentLayout;
    private DataManager dataManager = DataManager.getInstance();
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
    }

    private void initData() {
        for(int i =0; i<=20 ; i++){
            dataManager.addToDo(ToDo.builder().name("Przykładowe zadanie numer " + i).build());
        }

    }

    private void initView() {
        int index = 0;
        contentLayout.removeAllViews();
        for(ToDo toDo: dataManager.getToDos()){
            final View frame = getLayoutInflater().inflate(R.layout.todo_list_frame, null);
            TextView toDoName = frame.findViewById(R.id.toDoName);
            toDoName.setText(toDo.getName());
            frame.setId(index);
            contentLayout.addView(frame);
            index++;
        }
    }



}