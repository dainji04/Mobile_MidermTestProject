package vn.edu.stu.doangk_qlsach.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.stu.doangk_qlsach.R;
import vn.edu.stu.doangk_qlsach.dao.CateDAO;
import vn.edu.stu.doangk_qlsach.model.Book;
import vn.edu.stu.doangk_qlsach.model.Category;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    Activity context;
    int resource;
    List<Book> objects;
    private List<Category> categoryList;

    public BookAdapter(Activity context, int resource, List<Book> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        if (categoryList == null) {
            categoryList = new ArrayList<>();
        }
        CateDAO cateDAO = new CateDAO(context);
        categoryList = cateDAO.getAllCategories();

        ImageView image_book = item.findViewById(R.id.image_book);
        TextView tvbookId = item.findViewById(R.id.tvBookId);
        TextView tvBookTitle = item.findViewById(R.id.tvBookTitle);
        TextView tvBookCategory = item.findViewById(R.id.tvBookCategory);

        Book b = this.objects.get(position);
        image_book.setImageURI(Uri.parse(b.getImagePath()));
        tvbookId.setText("Id: " + b.getId());
        tvBookTitle.setText(b.getTitle());
        Category c = new Category();

        for (Category category : categoryList) {
            if (category.getId() == b.getCategoryId()) {
                c = category;
                break;
            }
        }

        tvBookCategory.setText("Category: " + c.getName());

        return item;
    }
}