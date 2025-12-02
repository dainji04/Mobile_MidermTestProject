package vn.edu.stu.doangk_qlsach;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import vn.edu.stu.doangk_qlsach.adapter.CategoryAdapter;
import vn.edu.stu.doangk_qlsach.dao.CateDAO;
import vn.edu.stu.doangk_qlsach.helper.DBHelper;
import vn.edu.stu.doangk_qlsach.helper.MenuHelper;
import vn.edu.stu.doangk_qlsach.model.Category;

public class CategoryManagement extends AppCompatActivity {
    ListView lvCategory;
    CategoryAdapter adapterCategory;
    Button btnSave;
    TextInputEditText edtCateId, edtCateName;
    CateDAO cateDAO;
    DBHelper dbHelper;

    private boolean isEditing = false;

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

        addControls();
        addEvents();
        loadDsSvFromDb();
    }

    private void loadDsSvFromDb() {
        adapterCategory.clear();
        adapterCategory.addAll(cateDAO.getAllCategories());
        adapterCategory.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true; // must return true to show the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (MenuHelper.handleMenuClick(this, item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        btnSave.setOnClickListener(view -> {
            if (isEditing) {
                Category c = new Category(
                        Integer.parseInt(edtCateId.getText().toString()),
                        edtCateName.getText().toString()
                );
                int updated = cateDAO.update(c);
                if (updated > 0) {
                    loadDsSvFromDb();
                    resetForm();
                    Toast.makeText(this, "Sửa thành công item "+c.getId(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Sửa thất bại", Toast.LENGTH_LONG).show();
                }
                isEditing=true;
            } else {
                // adding
                String name = edtCateName.getText().toString();
                boolean isAdded = cateDAO.insert(new Category(name));
                if (isAdded) {
                    loadDsSvFromDb();
                    resetForm();
                } else {
                    edtCateName.setError("Failed to add category");
                }
            }
        });

        lvCategory.setOnItemLongClickListener((adapterView, view, i, l) -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CategoryManagement.this);
            builder.setTitle("What do you want?");
            builder.setMessage("Choose an action to perform on this category.");
            builder.setPositiveButton("Delete", (dialogInterface, j) -> {
                boolean isDeleted = cateDAO.delete(String.valueOf(adapterCategory.getItem(i).getId()));
                if (isDeleted) {
                    loadDsSvFromDb();
                    Toast.makeText(this, "Xóa thành công", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNeutralButton("Cancel", (dialogInterface, j) -> {});
            builder.setNegativeButton("Update", (dialogInterface, j) -> {
                edtCateId.setText(String.valueOf(adapterCategory.getItem(i).getId()));
                edtCateName.setText(adapterCategory.getItem(i).getName());
                isEditing=true;
                edtCateName.requestFocus();
                btnSave.setText("Update");
            });
            builder.show();
            return true;
        });
    }

    private void resetForm() {
        edtCateId.setText("");
        edtCateName.setText("");
        edtCateName.requestFocus();
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
        cateDAO = new CateDAO(CategoryManagement.this);
        dbHelper = new DBHelper();
        dbHelper.copyDBFromAssets(this);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
    }

}