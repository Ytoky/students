package com.example.students.activity;

import static android.widget.Toast.makeText;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booklibrary.R;
import com.example.students.common.enums.StudentsColumnName;
import com.example.students.dao.StudentDataBaseHelper;
import com.example.students.interfaces.Validable;

import org.apache.commons.lang3.StringUtils;

public class UpdateStudentActivity extends AppCompatActivity implements Validable {
    public static final int MAX_LENGTH_EDIT_TEXT = 20;

    private EditText nameInput;
    private EditText surnameInput;
    private EditText codInput;

    private String id;
    private String name;
    private String surname;
    private String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        nameInput = findViewById(R.id.firstname_update_input);
        surnameInput = findViewById(R.id.surname_update_input);
        codInput = findViewById(R.id.cod_update_input);
        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);

        getAndSetIntentData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }

        updateButton.setOnClickListener(view -> {
            if (validate()) {
                new UpdateStudentTask().execute();
            }
        });

        deleteButton.setOnClickListener(view -> confirmDialog());
    }

    private class UpdateStudentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try (StudentDataBaseHelper studentDataBaseHelper = new StudentDataBaseHelper(UpdateStudentActivity.this)) {
                name = nameInput.getText().toString().trim();
                surname = surnameInput.getText().toString().trim();
                cod = codInput.getText().toString().trim();
                studentDataBaseHelper.update(id, name, surname, cod);
            } catch (Exception e) {
                Log.d("error on connection to db", cod);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(UpdateStudentActivity.this, "Книга успешно обновлена!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean validate() {
        if (StringUtils.isEmpty(nameInput.getText().toString()) || StringUtils.isEmpty(surnameInput.getText().toString()) ||
                StringUtils.isEmpty(codInput.getText().toString())) {
            Toast.makeText(UpdateStudentActivity.this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!StringUtils.isNumeric(codInput.getText().toString())) {
            Toast.makeText(UpdateStudentActivity.this, "Код студента могут быть только числом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtils.isWhitespace(nameInput.getText().toString()) || StringUtils.isWhitespace(surnameInput.getText().toString()) ||
                StringUtils.isWhitespace(codInput.getText().toString())) {
            Toast.makeText(UpdateStudentActivity.this, "Поля не могут быть заполненны одним пробелом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nameInput.getText().toString().length() > MAX_LENGTH_EDIT_TEXT ||
                surnameInput.getText().toString().length() > MAX_LENGTH_EDIT_TEXT) {
            Toast.makeText(UpdateStudentActivity.this, "Длина поля  ИМЯ/ФАМАЛИЯ не может превышать 30 символов!", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

    public void getAndSetIntentData() {
        if (getIntent().hasExtra(StudentsColumnName.ID.getName()) && getIntent().hasExtra(StudentsColumnName.NAME.getName()) &&
                getIntent().hasExtra(StudentsColumnName.SURNAME.getName()) && getIntent().hasExtra(StudentsColumnName.COD.getName())) {

            id = getIntent().getStringExtra(StudentsColumnName.ID.getName());
            name = getIntent().getStringExtra(StudentsColumnName.NAME.getName());
            surname = getIntent().getStringExtra(StudentsColumnName.SURNAME.getName());
            cod = getIntent().getStringExtra(StudentsColumnName.COD.getName());

            nameInput.setText(name);
            surnameInput.setText(surname);
            codInput.setText(cod);
            Log.d("update", name + " " + surname + " " + cod);
        } else {
            makeText(this, "Произошла ошибка. Книга не обновлена", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить " + name + " ?");
        builder.setMessage("Вы уверены что хотите удалить " + surname + name + " ?");
        builder.setPositiveButton("Да", (dialogInterface, i) -> new DeleteStudentTask().execute());

        builder.setNegativeButton("Нет", (dialogInterface, i) -> {
        });
        builder.create().show();
    }

    private class DeleteStudentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try (StudentDataBaseHelper myDB = new StudentDataBaseHelper(UpdateStudentActivity.this)) {
                myDB.deleteOneRow(id);
                finish();
            } catch (Exception e) {
                Log.d("error on connection", "");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(UpdateStudentActivity.this, "Книга успешно удалена!", Toast.LENGTH_SHORT).show();
        }
    }
}