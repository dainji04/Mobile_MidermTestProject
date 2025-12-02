package vn.edu.stu.doangk_qlsach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.doangk_qlsach.dao.BookDAO;
import vn.edu.stu.doangk_qlsach.dao.CateDAO;
import vn.edu.stu.doangk_qlsach.helper.MenuHelper;
import vn.edu.stu.doangk_qlsach.model.Book;
import vn.edu.stu.doangk_qlsach.model.Category;

public class BookForm extends AppCompatActivity {
    ImageView imgBook;
    Button btnChooseImage, btnExit, btnSave;
    TextInputEditText edtBookId, edtBookTitle, edtBookAuthor, edtBookYear;
    Spinner spinnerCategory;

    List<Category> categoryList;

    private BookDAO bookDAO;
    private String selectedImagePath = "";
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private int bookId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControls();
        loadCategories();
        getDataFromIntent();
        setUpImagePicker();
        addEvents();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("bookId")) {
            bookId = intent.getIntExtra("bookId", -1);
            Book currentBook = bookDAO.getById(bookId);

            imgBook.setImageURI(Uri.parse(currentBook.getImagePath()));
            selectedImagePath = currentBook.getImagePath();
            edtBookId.setText(String.valueOf(currentBook.getId()));
            edtBookTitle.setText(currentBook.getTitle());
            edtBookAuthor.setText(currentBook.getAuthor());
            edtBookYear.setText(String.valueOf(currentBook.getPublishYear()));

            // Set selected category in spinner
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == currentBook.getCategoryId()) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }
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
        imgBook = findViewById(R.id.imgBook);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnExit = findViewById(R.id.btnExit);
        btnSave = findViewById(R.id.btnSave);
        edtBookId = findViewById(R.id.edtBookId);
        edtBookTitle = findViewById(R.id.edtBookTitle);
        edtBookAuthor = findViewById(R.id.edtBookAuthor);
        edtBookYear = findViewById(R.id.edtBookYear);
        spinnerCategory = findViewById(R.id.spinner_category);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        bookDAO = new BookDAO(this);
    }

    private void setUpImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            imgBook.setImageURI(imageUri);
                            selectedImagePath = imageUri.toString();

                            // Grant persistent permission
                            try {
                                getContentResolver().takePersistableUriPermission(
                                        imageUri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    private void loadCategories() {
        CateDAO cateDAO = new CateDAO(this);
        categoryList = cateDAO.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void addEvents() {
        btnChooseImage. setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnExit.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String title = edtBookTitle.getText().toString(). trim();
        String author = edtBookAuthor.getText().toString(). trim();
        String yearStr = edtBookYear.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, R.string.please_fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        // validate year
        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R. string.invalid_year, Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedCategoryPosition = spinnerCategory.getSelectedItemPosition();
        if (selectedCategoryPosition < 0 || selectedCategoryPosition >= categoryList.size()) {
            Toast.makeText(this, R.string. please_select_category, Toast. LENGTH_SHORT).show();
            return;
        }

        Category selectedCategory = categoryList.get(selectedCategoryPosition);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublishYear(year);
        book.setCategoryId(selectedCategory.getId());
        book.setImagePath(selectedImagePath);

        // check edit mode
        if (bookId == -1) {
            // Add new book
            new Thread(() -> {
                boolean isAdded = bookDAO.insert(book);

                runOnUiThread(() -> {
                    if (isAdded) {
                        Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }).start();
        } else {
            new Thread(() -> {
                book.setId(bookId);
                Log.e("BUG", "ImagePath: " + book.getImagePath().toString());
                int result = bookDAO.update(book);


                runOnUiThread(() -> {
                    if (result > 0) {
                        bookId = -1;
                        Toast.makeText(this, R.string. book_updated_successfully, Toast. LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();

        }
    }

}