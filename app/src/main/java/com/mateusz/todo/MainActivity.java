package com.mateusz.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mateusz.todo.model.ToDo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ToDo> todos = new ArrayList<>();
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        initData();
        initView();
    }

    private void initFields() {
        linearLayout = findViewById(R.id.contentLayout);
    }

    private void initData() {
        todos = Arrays.asList(
                ToDo.builder().name("Przykładowe todo1").build(),
                ToDo.builder().name("Przykładowe todo2").build(),
                ToDo.builder().name("Przykładowe todo3").build(),
                ToDo.builder().name("Przykładowe todo4").build(),
                ToDo.builder().name("Przykładowe todo5").build(),
                ToDo.builder().name("Przykładowe todo6").build(),
                ToDo.builder().name("Przykładowe todo7").build(),
                ToDo.builder().name("Przykładowe todo8").build(),
                ToDo.builder().name("Przykładowe todo9").build(),
                ToDo.builder().name("Przykładowe todo10").build(),
                ToDo.builder().name("Przykładowe todo11").build(),
                ToDo.builder().name("Przykładowe todo12").build(),
                ToDo.builder().name("Przykładowe todo13").build(),
                ToDo.builder().name("Przykładowe todo14").build(),
                ToDo.builder().name("Przykładowe todo15").build(),
                ToDo.builder().name("Przykładowe todo16").build(),
                ToDo.builder().name("Przykładowe todo17").build(),
                ToDo.builder().name("Przykładowe todo18").build(),
                ToDo.builder().name("Przykładowe todo19").build(),
                ToDo.builder().name("Przykładowe todo20").build()

        );
    }

    private void initView() {
        int index = 0;
        linearLayout.removeAllViews();
        for(ToDo toDo: todos){
            final View frame = getLayoutInflater().inflate(R.layout.activity_main, null);
            TextView toDoName = frame.findViewById(R.id.toDoName);
            toDoName.setText(toDo.getName());
            frame.setId(index);
            linearLayout.addView(frame);
            index++;
        }
    }



}