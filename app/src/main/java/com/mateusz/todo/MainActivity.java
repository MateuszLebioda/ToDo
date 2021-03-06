package com.mateusz.todo;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mateusz.todo.activity.AddNewTodo;
import com.mateusz.todo.activity.Mode;
import com.mateusz.todo.chanels.Channels;
import com.mateusz.todo.comparators.CreateDataComparator;
import com.mateusz.todo.comparators.NameComparator;
import com.mateusz.todo.comparators.PriorityComparator;
import com.mateusz.todo.data.DataManager;
import com.mateusz.todo.model.ToDo;
import com.mateusz.todo.service.Mapper;

import org.threeten.bp.LocalDate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static  final String STORE = "store.txt";

    private LinearLayout contentLayout;
    private DataManager dataManager = DataManager.getInstance();
    private FloatingActionButton addNewToDoButton;
    private TextView emptyListLabel;
    private ImageButton shareButton;

    private Spinner sortSpinner;

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fetchData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        initFields();
        handleNotification();
        initView();
    }

    private void initFields() {
        contentLayout = findViewById(R.id.contentLayout);
        emptyListLabel = findViewById(R.id.emptyListLabel);
        sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filterOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        addNewToDoButton = findViewById(R.id.addNewToDoButton);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(this);
        addNewToDoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNewTodo.class);
            intent.putExtra("mode", Mode.ADD);
            startActivity(intent);
        });
        saveData();
    }

    private void handleNotification() {
        List<ToDo> todos = dataManager.getToDos();
        int counter = 0;
        LocalDate today = LocalDate.now();
        for (ToDo todo : todos) {
            if (todo.getTerm() != null && todo.getTerm().toLocalDate().equals(today)) {
                counter++;
            }
        }
        if (counter > 0) {
            Notification notification = new NotificationCompat.Builder(this, Channels.CHANEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_campaign_24)
                    .setContentTitle("Lista zada?? kt??re powiniene?? zrobi?? ostatecznie dzisiaj")
                    .setContentText("Na dzi?? masz zaplanowanych do zrobienia " + counter + " zada??.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH).build();

            notificationManagerCompat.notify(1, notification);
        } else {
            notificationManagerCompat.cancelAll();
        }

    }


    private void initView() {

        if (dataManager.getToDos().size() == 0) {
            emptyListLabel.setVisibility(View.VISIBLE);
        } else {
            emptyListLabel.setVisibility(View.GONE);
        }

        int index = 0;
        contentLayout.removeAllViews();
        for (ToDo toDo : dataManager.getToDos()) {
            final View frame = getLayoutInflater().inflate(R.layout.todo_list_frame, null);
            frame.findViewById(R.id.todoPanel).setOnClickListener(v -> {
                Intent intent = new Intent(this, AddNewTodo.class);
                intent.putExtra("mode", Mode.EDIT);
                intent.putExtra("todoId", toDo.getId());
                startActivity(intent);
            });

            TextView toDoName = frame.findViewById(R.id.toDoName);
            if (toDo.isPriority()) {
                toDoName.setTextColor(Color.BLACK);
                toDoName.setTypeface(null, Typeface.BOLD);
            }
            frame.findViewById(R.id.isDone).setOnClickListener(v -> {
                dataManager.markAsDone(toDo);
                initView();
                Toast.makeText(this, "Pomy??lnie oznaczone zadanie jako wykonane", Toast.LENGTH_SHORT).show();

            });

            toDoName.setText(toDo.getName());
            frame.setId(index);
            contentLayout.addView(frame);
            index++;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setText("");
        String option = parent.getItemAtPosition(position).toString();
        switch (option) {
            case "Priorytet":
                dataManager.sortList(new PriorityComparator());
                break;
            case "Data utworzenia":
                dataManager.sortList(new CreateDataComparator());
                break;
            case "Nazwa":
                dataManager.sortList(new NameComparator());
                break;
        }
        initView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveData(){
        try (FileOutputStream fos = openFileOutput(STORE, MODE_PRIVATE)) {
            fos.write(Mapper.mapToDosToText(dataManager.getToDos()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchData(){
        try (FileInputStream fis = openFileInput(STORE)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            String text;

            while((text=bufferedReader.readLine()) != null){
                sb.append(text).append("\n");
            }

            dataManager.initData(new ArrayList<>(Arrays.asList(Mapper.mapTextToToDo(sb.toString()))));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}