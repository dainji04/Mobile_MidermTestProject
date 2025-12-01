package vn.edu.stu.doangk_qlsach.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.doangk_qlsach.model.Category;

public class CateDAO extends SQLiteOpenHelper {
    private static final String DB_NAME = "dbsach.sqlite";
    private static final int DB_VERSION = 2;

    // column name
    private static final String TABLE_NAME = "Category";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    public CateDAO(android.content.Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý khi có sự thay đổi trong phiên bản cơ sở dữ liệu
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CateDAO.TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            Category cate = new Category();

            cate.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CateDAO.COLUMN_ID)));
            cate.setName(cursor.getString(cursor.getColumnIndexOrThrow(CateDAO.COLUMN_NAME)));

            categories.add(cate);
        }

        cursor.close();
        db.close();

        return categories;
    }

    public boolean insert(Category category) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, category.getName());

        long newID = database.insert(TABLE_NAME, null, cv);

        database.close();
        return newID != -1;
    }

    public int update(Category category) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, category.getName());

        int rows = db.update(
                TABLE_NAME,
                cv,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(category.getId())}
        );

        db.close();
        return rows;
    }

    public boolean delete(String maCate) {
        String deleteQuery = "DELETE FROM " + CateDAO.TABLE_NAME +
                " WHERE " + CateDAO.COLUMN_ID + " = ?";

        // kiểm tra xem có bản ghi nào bị ràng buộc không
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM Book WHERE categoryId = ?",
                new String[] { maCate }
        );
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                // Có bản ghi bị ràng buộc, không thể xóa
                cursor.close();
                return false;
            }
        }
        cursor.close();

        // Chỉ cần tham số cho điều kiện WHERE
        String[] bindArgs = new String[] { maCate };

        getWritableDatabase().execSQL(deleteQuery, bindArgs);
        return true;
    }
}
