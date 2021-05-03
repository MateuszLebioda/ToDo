package com.mateusz.todo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mateusz.todo.MainActivity;
import com.mateusz.todo.R;
import com.mateusz.todo.data.DataManager;
import com.mateusz.todo.model.ToDo;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public class AddNewTodo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private DataManager dataManager = DataManager.getInstance();

    private LocalDate deadlineDate;
    private LocalTime deadlineTime;

    private EditText editTextDate;
    private EditText editTextHour;
    private TextView nameText;
    private FloatingActionButton submitNewToDoButton;
    private ImageButton clearDate;
    private ImageButton clearHour;
    private CheckBox priorityCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo);

        initFields();
    }

    private void initFields() {

        clearHour = findViewById(R.id.clearHour);
        clearHour.setOnClickListener(v -> {
            editTextHour.setText("");
            deadlineTime = null;
            clearHour.setClickable(false);
        });

        clearDate = findViewById(R.id.clearDate);
        clearDate.setOnClickListener(v -> {
            editTextDate.setText("");
            deadlineDate = null;
            clearDate.setClickable(false);
        });

        submitNewToDoButton = findViewById(R.id.submitNewToDoButton);
        submitNewToDoButton.setOnClickListener(v -> {

            LocalDateTime term = null;

            if(deadlineDate != null && deadlineTime != null){
                term = LocalDateTime.of(deadlineDate, deadlineTime);
            }else if(deadlineDate != null){
                term = LocalDateTime.of(deadlineDate, LocalTime.of(0,0,0));
            }

            ToDo toDo = ToDo.builder().name(nameText.getText().toString()).priority(priorityCheckBox.isChecked()).term(term).build();
            dataManager.addToDo(toDo);

            Toast.makeText(this, "Pomyślnie dodano zadanie", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        });

        priorityCheckBox = findViewById(R.id.priorityCheckBox);

        nameText = findViewById(R.id.nameText);
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    submitNewToDoButton.setEnabled(true);
                }else{
                    submitNewToDoButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setShowSoftInputOnFocus(false);

        editTextDate.setOnClickListener(v -> {

            LocalDate date = LocalDate.now();
            DatePickerDialog dataDialog = new DatePickerDialog(this, this,
                    date.getYear(), date.getMonthValue() - 1,
                    date.getDayOfMonth());
            dataDialog.show();
        });

        editTextHour = findViewById(R.id.editTextHour);
        editTextHour.setShowSoftInputOnFocus(false);

        editTextHour.setOnClickListener(v -> {
            LocalTime time = LocalTime.now();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, time.getHour() + 2 , time.getMinute(), true);
            timePickerDialog.show();
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        deadlineDate = LocalDate.of(year,month-1,dayOfMonth);
        editTextDate.setText(LocalDate.of(year, month-1, dayOfMonth).toString());
        clearDate.setClickable(true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        deadlineTime = LocalTime.of(hourOfDay, minute,0);
        editTextHour.setText(LocalTime.of(hourOfDay, minute, 0).toString());
        clearHour.setClickable(true);
    }
}