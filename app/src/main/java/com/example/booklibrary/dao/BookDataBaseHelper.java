package com.example.booklibrary.dao;

import static android.widget.Toast.makeText;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BookDataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BookLibrary.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Library";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "book_title";
    public static final String COLUMN_AUTHOR = "book_author";
    public static final String COLUMN_PAGES = "book_pages";

    private Context context;

    public BookDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " INTEGER);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addBook(String title, String author, int pages) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_AUTHOR, author);
        contentValues.put(COLUMN_PAGES, pages);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (isError(result)) {
            makeText(context, "Произошла ошибка при добавлении книги. Повторите еще раз", Toast.LENGTH_SHORT).show();
        } else {
            makeText(context, "Книга добавлена!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getAllBook() {
        String querySelectAllFromBook = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(querySelectAllFromBook, null);
        }

        return cursor;
    }

    public void update(String rowId, String title, String author, String pages) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_AUTHOR, author);
        contentValues.put(COLUMN_PAGES, pages);

        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, "_id=?", new String[]{rowId});
        if (isError(result)) {
            makeText(context, "Произошла ошибка при обновлении. Повторите еще раз", Toast.LENGTH_SHORT).show();
        } else {
            makeText(context, "Книга обновлена!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(String rowId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TABLE_NAME, "_id=?", new String[]{rowId});
        if (isError(result)) {
            makeText(context, "Ошибка при удалении книги. Повторите еще раз", Toast.LENGTH_SHORT).show();
        } else {
            makeText(context, "Книга удалена.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isError(long result) {
        return result == -1;
    }

    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
