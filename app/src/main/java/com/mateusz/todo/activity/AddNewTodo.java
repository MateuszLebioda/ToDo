package com.mateusz.todo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mateusz.todo.MainActivity;
import com.mateusz.todo.R;
import com.mateusz.todo.adapters.MyImageAdapter;
import com.mateusz.todo.data.DataManager;
import com.mateusz.todo.model.ToDo;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class AddNewTodo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private List<Bitmap> capturedBitmaps = new ArrayList<>();
    private GridView gridAttachmentView;

    private final static int PERMISSION_CODE = 101;

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

    private FloatingActionButton addAttachment;

    private Mode mode;
    private ToDo editingToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_todo);

        mode = (Mode) getIntent().getSerializableExtra("mode");

        if (mode == Mode.EDIT) {
            int id = (int) getIntent().getSerializableExtra("todoId");
            editingToDo = dataManager.getToDoById(id);
            capturedBitmaps = editingToDo.getAttachment();
        }

        initFields();

        if (mode == Mode.EDIT) {
            initEditingModeData();
        }
    }

    private void initEditingModeData() {
        if (editingToDo.getTerm() != null) {
            deadlineTime = editingToDo.getTerm().toLocalTime();
            editTextHour.setText(deadlineTime.toString());

            deadlineDate = editingToDo.getTerm().toLocalDate();
            editTextDate.setText(deadlineDate.toString());
        }

        priorityCheckBox.setChecked(editingToDo.isPriority());

        nameText.setText(editingToDo.getName());

    }

    private void initFields() {

        gridAttachmentView = findViewById(R.id.gridAttachmentView);
        gridAttachmentView.setAdapter(new MyImageAdapter(this, capturedBitmaps));

        gridAttachmentView.setOnItemLongClickListener((parent, view, position, id) -> {
            capturedBitmaps.remove(position);
            Toast.makeText(this, "Pomyślnie usunięto załącznik", Toast.LENGTH_SHORT).show();
            initFields();
            return false;
        });

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

        if (mode == Mode.ADD) {
            submitNewToDoButton = findViewById(R.id.submitNewToDoButton);
            submitNewToDoButton.setVisibility(View.VISIBLE);
            submitNewToDoButton.setOnClickListener(v -> {
                submitElement();
            });
        }

        if (mode == Mode.EDIT) {
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

        addAttachment = findViewById(R.id.addAttachment);
        addAttachment.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);

        });

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
            if (deadlineDate == null) {
                Toast.makeText(this, "Najpierw uzupełnij pole Data!", Toast.LENGTH_SHORT).show();
            } else {
                LocalTime time = LocalTime.now();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, time.getHour() + 2, time.getMinute(), true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_CODE) {
            Bitmap bInput = (Bitmap) data.getExtras().get("data");
            float degrees = 270;
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees);
            this.capturedBitmaps.add(Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true));

            initFields();
        }
    }

    private void validSubmitButton(CharSequence s) {
        FloatingActionButton button = mode == Mode.ADD ? submitNewToDoButton : submitEditedToDoButton;
        if (s.length() > 0) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
    }

    private void submitElement() {

        LocalDateTime term = null;

        if (deadlineDate != null && deadlineTime != null) {
            term = LocalDateTime.of(deadlineDate, deadlineTime);
        } else if (deadlineDate != null) {
            term = LocalDateTime.of(deadlineDate, LocalTime.of(0, 0, 0));
        }
        ToDo toDo = ToDo.builder().name(nameText.getText().toString()).priority(priorityCheckBox.isChecked()).term(term).attachment(this.capturedBitmaps).build();
        if (editingToDo != null) {
            dataManager.removeToDo(editingToDo);
        }
        dataManager.addToDo(toDo);


        if (mode == Mode.ADD) {
            Toast.makeText(this, "Pomyślnie dodano element", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pomyślnie edytowano element", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        deadlineDate = LocalDate.of(year, month + 1, dayOfMonth);
        editTextDate.setText(LocalDate.of(year, month + 1, dayOfMonth).toString());
        clearDate.setClickable(true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        deadlineTime = LocalTime.of(hourOfDay, minute, 0);
        editTextHour.setText(LocalTime.of(hourOfDay, minute, 0).toString());
        clearHour.setClickable(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PERMISSION_CODE);
                }
            }

        }
    }


}