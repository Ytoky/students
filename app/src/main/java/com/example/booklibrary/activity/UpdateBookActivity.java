package com.example.booklibrary.activity;

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
import com.example.booklibrary.common.enums.BookColumnName;
import com.example.booklibrary.dao.BookDataBaseHelper;
import com.example.booklibrary.interfaces.Validable;

import org.apache.commons.lang3.StringUtils;

public class UpdateBookActivity extends AppCompatActivity implements Validable {
    private EditText titleInput;
    private EditText authorInput;
    private EditText pagesInput;

    private String id;
    private String title;
    private String author;
    private String pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        titleInput = findViewById(R.id.title_update_input);
        authorInput = findViewById(R.id.author_update_input);
        pagesInput = findViewById(R.id.pages_update_input);
        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);

        getAndSetIntentData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }

        updateButton.setOnClickListener(view -> {
            if (validate()) {
                new UpdateBookTask().execute();
            }
        });

        deleteButton.setOnClickListener(view -> confirmDialog());
    }

    private class UpdateBookTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try (BookDataBaseHelper bookDataBaseHelper = new BookDataBaseHelper(UpdateBookActivity.this)) {
                title = titleInput.getText().toString().trim();
                author = authorInput.getText().toString().trim();
                pages = pagesInput.getText().toString().trim();
                bookDataBaseHelper.update(id, title, author, pages);
            } catch (Exception e) {
                Log.d("error on connection to db", pages);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(UpdateBookActivity.this, "Книга успешно обновлена!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean validate() {
        if (StringUtils.isEmpty(titleInput.getText().toString()) || StringUtils.isEmpty(authorInput.getText().toString()) ||
                StringUtils.isEmpty(pagesInput.getText().toString())) {
            Toast.makeText(UpdateBookActivity.this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!StringUtils.isNumeric(pagesInput.getText().toString())) {
            Toast.makeText(UpdateBookActivity.this, "Страницы могут быть только числом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtils.isWhitespace(titleInput.getText().toString()) || StringUtils.isWhitespace(authorInput.getText().toString()) ||
                StringUtils.isWhitespace(pagesInput.getText().toString())) {
            Toast.makeText(UpdateBookActivity.this, "Поля не могут быть пробелом!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void getAndSetIntentData() {
        if (getIntent().hasExtra(BookColumnName.ID.getName()) && getIntent().hasExtra(BookColumnName.TITLE.getName()) &&
                getIntent().hasExtra(BookColumnName.AUTHOR.getName()) && getIntent().hasExtra(BookColumnName.PAGES.getName())) {

            id = getIntent().getStringExtra(BookColumnName.ID.getName());
            title = getIntent().getStringExtra(BookColumnName.TITLE.getName());
            author = getIntent().getStringExtra(BookColumnName.AUTHOR.getName());
            pages = getIntent().getStringExtra(BookColumnName.PAGES.getName());

            titleInput.setText(title);
            authorInput.setText(author);
            pagesInput.setText(pages);
            Log.d("update", title + " " + author + " " + pages);
        } else {
            makeText(this, "Произошла ошибка. Книга не обновлена", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить " + title + " ?");
        builder.setMessage("Вы уверены что хотите удалить " + title + " ?");
        builder.setPositiveButton("Да", (dialogInterface, i) -> new DeleteBookTask().execute());

        builder.setNegativeButton("Нет", (dialogInterface, i) -> {
        });
        builder.create().show();
    }

    private class DeleteBookTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try (BookDataBaseHelper myDB = new BookDataBaseHelper(UpdateBookActivity.this)) {
                myDB.deleteOneRow(id);
                finish();
            } catch (Exception e) {
                Log.d("error on connection", "");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(UpdateBookActivity.this, "Книга успешно удалена!", Toast.LENGTH_SHORT).show();
        }
    }
}