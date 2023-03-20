package com.example.booklibrary.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklibrary.R;
import com.example.booklibrary.dao.BookDataBaseHelper;
import com.example.booklibrary.interfaces.Validable;

import org.apache.commons.lang3.StringUtils;

public class AddBookActivity extends AppCompatActivity implements Validable {
    private EditText titleInput;
    private EditText authorInput;
    private EditText pagesInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleInput = findViewById(R.id.title_input);
        authorInput = findViewById(R.id.author_input);
        pagesInput = findViewById(R.id.pages_input);
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> {
                    if (validate()) {
                        new AddBookTask().execute();
                    }
                }
        );
    }

    @Override
    public boolean validate() {
        if (StringUtils.isEmpty(titleInput.getText().toString()) || StringUtils.isEmpty(authorInput.getText().toString()) ||
                StringUtils.isEmpty(pagesInput.getText().toString())) {
            Toast.makeText(AddBookActivity.this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!StringUtils.isNumeric(pagesInput.getText().toString())) {
            Toast.makeText(AddBookActivity.this, "Страницы могут быть только числом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtils.isWhitespace(titleInput.getText().toString()) || StringUtils.isWhitespace(authorInput.getText().toString()) ||
                StringUtils.isWhitespace(pagesInput.getText().toString())) {
            Toast.makeText(AddBookActivity.this, "Поля не могут быть пробелом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class AddBookTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try (BookDataBaseHelper bookDataBaseHelper = new BookDataBaseHelper(AddBookActivity.this)) {
                bookDataBaseHelper.addBook(titleInput.getText().toString().trim(),
                        authorInput.getText().toString().trim(),
                        Integer.parseInt(pagesInput.getText().toString().trim()));
            } catch (Exception e) {
                Log.d("error on connection to database", "");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(AddBookActivity.this, "Книга успешно добавлена!", Toast.LENGTH_SHORT).show();
        }
    }
}