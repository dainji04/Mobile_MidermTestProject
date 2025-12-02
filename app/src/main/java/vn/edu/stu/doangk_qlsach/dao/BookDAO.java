package vn.edu.stu.doangk_qlsach.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.doangk_qlsach.model.Book;
import vn.edu.stu.doangk_qlsach.model.Category;

public class BookDAO extends SQLiteOpenHelper {
    private static final String DB_NAME = "dbsach.sqlite";
    private static final int DB_VERSION = 2;

    // column name
    private static final String TABLE_NAME = "Book";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATE_ID = "categoryId";
    private static final String COLUMN_IMAGE_PATH = "imagePath";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_PUBLISH_YEAR = "publishYear";

    public BookDAO(android.content.Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý khi có sự thay đổi trong phiên bản cơ sở dữ liệu
    }

    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            Book b = new Book();

            b.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            b.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            b.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATE_ID)));
            b.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
            b.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)));
            b.setPublishYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PUBLISH_YEAR)));
            books.add(b);
        }

        cursor.close();
        db.close();

        return books;
    }

    public Book getById(int id) {
        Book b;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            b = new Book();

            b.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            b.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            b.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATE_ID)));
            b.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
            b.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)));
            b.setPublishYear(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PUBLISH_YEAR)));
        } else {
            b = null;
        }
        cursor.close();
        db.close();

        return b;
    }

    public boolean insert(Book b) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, b.getTitle());
        cv.put(COLUMN_CATE_ID, b.getCategoryId());
        cv.put(COLUMN_IMAGE_PATH, b.getImagePath());
        cv.put(COLUMN_AUTHOR, b.getAuthor());
        cv.put(COLUMN_PUBLISH_YEAR, b.getPublishYear());

        long newID = database.insert(TABLE_NAME, null, cv);

        database.close();
        return newID != -1;
    }

    public int update(Book b) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, b.getTitle());
        cv.put(COLUMN_CATE_ID, b.getCategoryId());
        cv.put(COLUMN_IMAGE_PATH, b.getImagePath());
        cv.put(COLUMN_AUTHOR, b.getAuthor());
        cv.put(COLUMN_PUBLISH_YEAR, b.getPublishYear());

        int rows = db.update(
                TABLE_NAME,
                cv,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(b.getId())}
        );

        db.close();
        return rows;
    }

    public boolean delete(String ma) {
        String deleteQuery = "DELETE FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = ?";

        // Chỉ cần tham số cho điều kiện WHERE
        String[] bindArgs = new String[] { ma };

        getWritableDatabase().execSQL(deleteQuery, bindArgs);
        return true;
    }
}
