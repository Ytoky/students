package com.example.booklibrary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booklibrary.activity.AddBookActivity;
import com.example.booklibrary.adapter.BookAdapter;
import com.example.booklibrary.common.enums.BookColumnNumber;
import com.example.booklibrary.dao.BookDataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String QUESTION_MESSAGE_DELETE_ALL = "Вы уверены что хотите удалить все книги?";
    public static final String TITLE_DELETE_ALL_BOOK = "Удалить все книги?";
    public static final String BUTTON_NAME_YES = "Да";
    public static final String BUTTON_NAME_NO = "Нет";

    private ImageView emptyImageview;
    private TextView noDataText;
    private BookDataBaseHelper bookDataBaseHelper;
    private List<String> bookIds;
    private List<String> bookTitles;
    private List<String> bookAuthors;
    private List<String> bookPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        emptyImageview = findViewById(R.id.empty_imageview);
        noDataText = findViewById(R.id.no_data);
        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                    startActivity(intent);
                }
        );

        bookDataBaseHelper = new BookDataBaseHelper(MainActivity.this);
        bookIds = new ArrayList<>();
        bookTitles = new ArrayList<>();
        bookAuthors = new ArrayList<>();
        bookPages = new ArrayList<>();

        BookAdapter bookAdapter = new BookAdapter(MainActivity.this, bookIds, bookTitles, bookAuthors, bookPages, this);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        storeDataInArrays();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isButtonDeleteAll(item.getItemId())) {
            confirmDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isButtonDeleteAll(int item) {
        return item == R.id.delete_all;
    }

    void confirmDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(TITLE_DELETE_ALL_BOOK);
        alertDialog.setMessage(QUESTION_MESSAGE_DELETE_ALL);
        alertDialog.setPositiveButton(BUTTON_NAME_YES, (dialogInterface, i) -> {
            try (BookDataBaseHelper bookDataBaseHelper = new BookDataBaseHelper(MainActivity.this)) {
                bookDataBaseHelper.deleteAll();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.d("error on delete all books", "");
            }
        });

        alertDialog.setNegativeButton(BUTTON_NAME_NO, (dialogInterface, i) -> {});
        alertDialog.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isUpdate(requestCode)) {
            recreate();
        }
    }

    private boolean isUpdate(int requestCode) {
        return requestCode == 1;
    }

    void storeDataInArrays() {
        Cursor cursor = bookDataBaseHelper.getAllBook();
        if (isEmptyBook(cursor.getCount())) {
            showEmptyImage();
        } else {
            showBooks(cursor);
        }
    }

    private void showEmptyImage() {
        emptyImageview.setVisibility(View.VISIBLE);
        noDataText.setVisibility(View.VISIBLE);
    }

    private void showBooks(Cursor cursor) {
        AsyncTask.execute(() -> {
            while (cursor.moveToNext()) {
                bookIds.add(cursor.getString(BookColumnNumber.ID.getColumn()));
                bookTitles.add(cursor.getString(BookColumnNumber.TITLE.getColumn()));
                bookAuthors.add(cursor.getString(BookColumnNumber.AUTHOR.getColumn()));
                bookPages.add(cursor.getString(BookColumnNumber.PAGES.getColumn()));
            }
            emptyImageview.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        });
    }

    private boolean isEmptyBook(int countBook) {
        return countBook == 0;
    }
}