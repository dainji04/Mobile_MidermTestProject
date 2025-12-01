package vn.edu.stu.doangk_qlsach.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.doangk_qlsach.R;
import vn.edu.stu.doangk_qlsach.model.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {
    Activity context;
    int resource;
    List<Category> objects;

    public CategoryAdapter(Activity context, int resource, List<Category> objects) {
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

        TextView tvCateId = item.findViewById(R.id.tvCateId);
        TextView tvCateName = item.findViewById(R.id.tvCateName);

        Category cate = this.objects.get(position);
        tvCateId.setText("Category id: " + cate.getId());
        tvCateName.setText(cate.getName());

        return item;
    }
}
