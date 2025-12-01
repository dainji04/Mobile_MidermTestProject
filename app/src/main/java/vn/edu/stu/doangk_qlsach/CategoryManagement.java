package vn.edu.stu.doangk_qlsach;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vn.edu.stu.doangk_qlsach.adapter.CategoryAdapter;
import vn.edu.stu.doangk_qlsach.model.Category;

public class CategoryManagement extends AppCompatActivity {
    public static final String DB_NAME = "dbsach.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";

    ListView lvCategory;
    CategoryAdapter adapterCategory;
    Button btnSave;
    TextInputEditText edtCateId, edtCateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
		
		copyDBFromAssets();
        addControls();
        addEvents();
        loadDsSvFromDb();
    }
	
	private void loadDsSvFromDb() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );

        Cursor cursor = database.rawQuery("Select * From Category", null);
        adapterCategory.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
			Category category = new Category(id, name);
            adapterCategory.add(category);
        }
        cursor.close();
        database.close();
        adapterCategory.notifyDataSetChanged();
    }

    private void addEvents() {
        btnSave.setOnClickListener(view -> {
            String name = edtCateName.getText().toString();

            SQLiteDatabase database = openOrCreateDatabase(
                    DB_NAME,
                    MODE_PRIVATE,
                    null
            );
            String sql = "Insert into Category (Name) values (?)";
            Object[] args = new Object[]{name};
            database.execSQL(sql, args);
            database.close();
            resetForm();
            loadDsSvFromDb();
        });
    }

    private void resetForm() {
        edtCateId.setText("");
        edtCateName.setText("");
    }

    private void copyDBFromAssets() {
        File dbfile = getDatabasePath(DB_NAME);
        if (!dbfile.exists()) {
            File dbDir = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!dbDir.exists()) {
                dbDir.mkdir();
            }
            try {
                InputStream is = getAssets().open(DB_NAME); // open file DB
                String outputFilePath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
                OutputStream os = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024]; // mảng nhị phan để chép
                int length; // nếu 1028 -> lấy ra 1024 dư 4 --> lưu vào length

                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                os.flush();
                os.close();
                is.close();

            } catch (IOException e) {
                Log.e("LOI", e.toString());
            }
        }
    }

    private void addControls() {
        lvCategory = findViewById(R.id.lvCategory);
        btnSave = findViewById(R.id.btnSave);
        edtCateId = findViewById(R.id.edtCateId);
        edtCateName = findViewById(R.id.edtCateName);
        adapterCategory = new CategoryAdapter(
                CategoryManagement.this,
                R.layout.item_category,
                new ArrayList<>()
        );
        lvCategory.setAdapter(adapterCategory);
    }

}