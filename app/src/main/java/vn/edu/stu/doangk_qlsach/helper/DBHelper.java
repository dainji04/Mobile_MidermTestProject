package vn.edu.stu.doangk_qlsach.helper;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper {
    public static final String DB_NAME = "dbsach.sqlite";
    public static final String DB_PATH_SUFFIX = "/databases/";

    public static void copyDBFromAssets(Context context) {
        File dbfile = context.getDatabasePath(DB_NAME);
        if (!dbfile.exists()) {
            File dbDir = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!dbDir.exists()) {
                dbDir.mkdir();
            }
            try {
                InputStream is = context.getAssets().open(DB_NAME); // open file DB
                String outputFilePath = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
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
}
