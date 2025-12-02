package vn.edu.stu.doangk_qlsach.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import vn.edu.stu.doangk_qlsach.BookList;
import vn.edu.stu.doangk_qlsach.CategoryManagement;
import vn.edu.stu.doangk_qlsach.Login;
import vn.edu.stu.doangk_qlsach.R;
import vn.edu.stu.doangk_qlsach.About;

public class MenuHelper {
    public static boolean handleMenuClick(Activity activity, MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnuCate) {
            activity.startActivity(new Intent(activity, CategoryManagement.class));
            return true;
        } else if (id == R.id.mnuBooks) {
            activity.startActivity(new Intent(activity, BookList.class));
            return true;
        } else if (id == R.id.mnuAbout) {
            activity.startActivity(new Intent(activity, About.class));
            return true;
        } else if (id == R.id.mnuLogout) {
            activity.startActivity(new Intent(activity, Login.class));
            return true;
        }

        return false;
    }
}