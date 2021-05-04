package com.mateusz.todo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

    private FloatingActionButton submitEditedToDoButton;
    private FloatingActionButton submitDeleteToDoButton;

    private Mode mode;
    private ToDo editingToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo);

        mode = (Mode)getIntent().getSerializableExtra("mode");

        initFields();

        if(mode == Mode.EDIT){
            editingToDo = (ToDo) getIntent().getSerializableExtra("todo");
            initEditingModeData();
        }
    }

    private void initEditingModeData(){
        if(editingToDo.getTerm() != null){
            deadlineTime = editingToDo.getTerm().toLocalTime();
            editTextHour.setText(deadlineTime.toString());

            deadlineDate = editingToDo.getTerm().toLocalDate();
            editTextDate.setText(deadlineDate.toString());
        }

        priorityCheckBox.setChecked(editingToDo.isPriority());

        nameText.setText(editingToDo.getName());

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
            editTextHour.setText("");
            deadlineTime = null;
            clearHour.setClickable(false);
            editTextDate.setText("");
            deadlineDate = null;
            clearDate.setClickable(false);
        });

        if(mode == Mode.ADD) {
            submitNewToDoButton = findViewById(R.id.submitNewToDoButton);
            submitNewToDoButton.setVisibility(View.VISIBLE);
            submitNewToDoButton.setOnClickListener(v -> {
                submitElement();
            });
        }

        if(mode == Mode.EDIT) {
            submitEditedToDoButton = findViewById(R.id.submitEditedToDoButton);
            submitEditedToDoButton.setVisibility(View.VISIBLE);
            submitEditedToDoButton.setOnClickListener(v -> {
                submitElement();
            });
            submitDeleteToDoButton = findViewById(R.id.submitDeleteToDoButton);
            submitDeleteToDoButton.setVisibility(View.VISIBLE);
            submitDeleteToDoButton.setOnClickListener(v -> {
                dataManager.removeToDo(editingToDo);
                Toast.makeText(this, "Pomyślnie usunięto element", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        }


        priorityCheckBox = findViewById(R.id.priorityCheckBox);

        nameText = findViewById(R.id.nameText);
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validSubmitButton(s);
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
            if(deadlineDate == null){
                Toast.makeText(this, "Najpierw uzupełnij pole Data!", Toast.LENGTH_SHORT).show();
            }else {
                LocalTime time = LocalTime.now();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, time.getHour() + 2, time.getMinute(), true);
                timePickerDialog.show();
            }
        });
    }

    private void validSubmitButton(CharSequence s){
        FloatingActionButton button = mode == Mode.ADD ? submitNewToDoButton : submitEditedToDoButton;
        if(s.length()>0){
            button.setEnabled(true);
        }else{
            button.setEnabled(false);
        }
    }

    private void submitElement(){

        LocalDateTime term = null;

        if(deadlineDate != null && deadlineTime != null){
            term = LocalDateTime.of(deadlineDate, deadlineTime);
        }else if(deadlineDate != null){
            term = LocalDateTime.of(deadlineDate, LocalTime.of(0,0,0));
        }
        ToDo toDo = ToDo.builder().name(nameText.getText().toString()).priority(priorityCheckBox.isChecked()).term(term).build();
        dataManager.addToDo(toDo);
        if(editingToDo != null){
            dataManager.removeToDo(editingToDo);
        }

        if(mode == Mode.ADD){
            Toast.makeText(this, "Pomyślnie dodano element", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Pomyślnie edytowano element", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        deadlineDate = LocalDate.of(year,month+1,dayOfMonth);
        editTextDate.setText(LocalDate.of(year, month+1, dayOfMonth).toString());
        clearDate.setClickable(true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        deadlineTime = LocalTime.of(hourOfDay, minute,0);
        editTextHour.setText(LocalTime.of(hourOfDay, minute, 0).toString());
        clearHour.setClickable(true);
    }
}