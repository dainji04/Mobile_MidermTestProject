package vn.edu.stu.doangk_qlsach.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.stu.doangk_qlsach.R;
import vn.edu.stu.doangk_qlsach.model.Book;
import java.util.List;

public class BookAdapter extends RecyclerView. Adapter<BookAdapter.ViewHolder> {

    private List<Book> books;
    private OnBookListener listener;

    public interface OnBookListener {
        void onBookClick(Book book);
        void onBookLongClick(Book book);
    }

    public BookAdapter(List<Book> books, OnBookListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateData(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView. ViewHolder {
        ImageView imageBook;
        TextView textBookId, textBookTitle, textBookCategory;

        ViewHolder(View itemView) {
            super(itemView);
            imageBook = itemView.findViewById(R. id.image_book);
            textBookId = itemView.findViewById(R.id.text_book_id);
            textBookTitle = itemView.findViewById(R. id.text_book_title);
            textBookCategory = itemView.findViewById(R.id.text_book_category);
        }

        void bind(Book book) {
            textBookId.setText(itemView.getContext().getString(R.string.book_id_format, book.getId()));
            textBookTitle.setText(book. getTitle());
            textBookCategory.setText(itemView.getContext().getString(R.string.category_format, "Category name"));

            // Load image
            if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
                try {
                    imageBook.setImageURI(Uri.parse(book.getImagePath()));
                } catch (Exception e) {
                    imageBook. setImageResource(R.drawable. ic_book_placeholder);
                }
            } else {
                imageBook.setImageResource(R.drawable.ic_book_placeholder);
            }

            // Click to edit
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });

            // Long click to delete
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onBookLongClick(book);
                }
                return true;
            });
        }
    }
}