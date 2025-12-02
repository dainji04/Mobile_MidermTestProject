package vn.edu.stu.doangk_qlsach;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vn.edu.stu.doangk_qlsach.adapter.BookAdapter;
import vn.edu.stu.doangk_qlsach.dao.BookDAO;
import vn.edu.stu.doangk_qlsach.helper.MenuHelper;
import vn.edu.stu.doangk_qlsach.model.Book;

public class BookList extends AppCompatActivity {
    ListView lvBook;
    BookAdapter bookAdapter;
    FloatingActionButton btnAddBook;
    private static final int REQUEST_CODE_ADD_BOOK = 31;
    private BookDAO bookDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControls();
        addEvents();
        loadData();
    }

    private void loadData() {
        bookAdapter.clear();
        bookAdapter.addAll(bookDAO.getAll());
        bookAdapter.notifyDataSetChanged();
    }

    private void addEvents() {
        btnAddBook.setOnClickListener(view -> {
            Intent intent = new Intent(BookList.this, BookForm.class);
            startActivity(intent);
        });

        lvBook.setOnItemLongClickListener((adapterView, view, i, l) -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(BookList.this);
            builder.setTitle("What do you want?");
            builder.setMessage("Choose an action to perform on this category.");
            builder.setPositiveButton("Delete", (dialogInterface, j) -> {
                boolean isDeleted = bookDAO.delete(String.valueOf(bookAdapter.getItem(i).getId()));
                if (isDeleted) {
                    loadData();
                    Toast.makeText(this, "Xóa thành công", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNeutralButton("Cancel", (dialogInterface, j) -> {});
            builder.setNegativeButton("Update", (dialogInterface, j) -> {
                Intent intent = new Intent(BookList.this, BookForm.class);
                intent.putExtra("bookId", bookAdapter.getItem(i).getId());
                startActivity(intent);
            });
            builder.show();
            return true;
        });
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

    private void addControls() {
        lvBook = findViewById(R.id.lvBook);
        bookAdapter = new BookAdapter(
                BookList.this,
                R.layout.item_book,
                new ArrayList<>()
        );
        lvBook.setAdapter(bookAdapter);

        btnAddBook = findViewById(R.id.btnAddBook);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        bookDAO = new BookDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}