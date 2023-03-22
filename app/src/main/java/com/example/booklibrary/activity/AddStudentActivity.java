package com.example.students.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklibrary.R;
import com.example.students.dao.StudentDataBaseHelper;
import com.example.students.interfaces.Validable;

import org.apache.commons.lang3.StringUtils;

public class AddStudentActivity extends AppCompatActivity implements Validable {
    public static final int MAX_LENGTH_EDIT_TEXT = 20;

    private EditText nameInput;
    private EditText surnameInput;
    private EditText codInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        nameInput = findViewById(R.id.firstname_input);
        surnameInput = findViewById(R.id.surname_input);
        codInput = findViewById(R.id.cod_input);
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> {
                    if (validate()) {
                        new AddStudentTask().execute();
                    }
                }
        );
    }

    @Override
    public boolean validate() {
        if (StringUtils.isEmpty(nameInput.getText().toString()) || StringUtils.isEmpty(surnameInput.getText().toString()) ||
                StringUtils.isEmpty(codInput.getText().toString())) {
            Toast.makeText(AddStudentActivity.this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!StringUtils.isNumeric(codInput.getText().toString())) {
            Toast.makeText(AddStudentActivity.this, "Код студента могут быть только числом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtils.isWhitespace(nameInput.getText().toString()) || StringUtils.isWhitespace(surnameInput.getText().toString()) ||
                StringUtils.isWhitespace(codInput.getText().toString())) {
            Toast.makeText(AddStudentActivity.this, "Поля не могут быть пробелом!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nameInput.getText().toString().length() > MAX_LENGTH_EDIT_TEXT ||
                surnameInput.getText().toString().length() > MAX_LENGTH_EDIT_TEXT) {
            Toast.makeText(AddStudentActivity.this, "Длина поля ИМЯ/ФАМИЛИЯ не может превышать 30 символов!", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class AddStudentTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try (StudentDataBaseHelper studentDataBaseHelper = new StudentDataBaseHelper(AddStudentActivity.this)) {
                studentDataBaseHelper.addBook(nameInput.getText().toString().trim(),
                        surnameInput.getText().toString().trim(),
                        Integer.parseInt(codInput.getText().toString().trim()));
            } catch (Exception e) {
                Log.d("error on connection to database", "");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(AddStudentActivity.this, "Студент успешно добавлена!", Toast.LENGTH_SHORT).show();
        }
    }
}