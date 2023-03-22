package com.example.students;

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

import com.example.booklibrary.R;
import com.example.students.activity.AddStudentActivity;
import com.example.students.adapter.StudentAdapter;
import com.example.students.common.enums.StudentsColumnNumber;
import com.example.students.dao.StudentDataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String QUESTION_MESSAGE_DELETE_ALL = "Вы уверены что хотите удалить всех студентов?";
    public static final String TITLE_DELETE_ALL_STUDENT = "Удалить всех студентов?";
    public static final String BUTTON_NAME_YES = "Да";
    public static final String BUTTON_NAME_NO = "Нет";

    private ImageView emptyImageview;
    private TextView noDataText;
    private StudentDataBaseHelper studentDataBaseHelper;
    private List<String> studentIds;
    private List<String> studentName;
    private List<String> studentSurname;
    private List<String> studentCod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        emptyImageview = findViewById(R.id.empty_imageview);
        noDataText = findViewById(R.id.no_data);
        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                    startActivity(intent);
                }
        );

        studentDataBaseHelper = new StudentDataBaseHelper(MainActivity.this);
        studentIds = new ArrayList<>();
        studentName = new ArrayList<>();
        studentSurname = new ArrayList<>();
        studentCod = new ArrayList<>();

        StudentAdapter studentAdapter = new StudentAdapter(MainActivity.this, studentIds, studentName, studentSurname, studentCod, this);
        recyclerView.setAdapter(studentAdapter);
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
        alertDialog.setTitle(TITLE_DELETE_ALL_STUDENT);
        alertDialog.setMessage(QUESTION_MESSAGE_DELETE_ALL);
        alertDialog.setPositiveButton(BUTTON_NAME_YES, (dialogInterface, i) -> {
            try (StudentDataBaseHelper studentDataBaseHelper = new StudentDataBaseHelper(MainActivity.this)) {
                studentDataBaseHelper.deleteAll();
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
        Cursor cursor = studentDataBaseHelper.getAllBook();
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
                studentIds.add(cursor.getString(StudentsColumnNumber.ID.getColumn()));
                studentName.add(cursor.getString(StudentsColumnNumber.NAME.getColumn()));
                studentSurname.add(cursor.getString(StudentsColumnNumber.SURNAME.getColumn()));
                studentCod.add(cursor.getString(StudentsColumnNumber.COD.getColumn()));
            }
            emptyImageview.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);
        });
    }

    private boolean isEmptyBook(int countBook) {
        return countBook == 0;
    }
}