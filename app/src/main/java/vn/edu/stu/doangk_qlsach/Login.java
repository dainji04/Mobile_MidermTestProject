package vn.edu.stu.doangk_qlsach;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    TextInputLayout tilUsername, tilPwd;
    MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControls();
        addEvents();

    }

    private void addEvents() {
        btnLogin.setOnClickListener(view -> {
            if (tilUsername.getEditText().getText().toString().equals("admin") &&
                tilPwd.getEditText().getText().toString().equals("123")) {
                Intent intent = new Intent(Login.this, CategoryManagement.class);
                startActivity(intent);
            } else {
                tilUsername.setError("Invalid username or password");
                tilPwd.setError("Invalid username or password");
            }
        });
    }

    private void addControls() {
        tilUsername = findViewById(R.id.tilUsername);
        tilPwd = findViewById(R.id.tilPwd);
        btnLogin = findViewById(R.id.btnLogin);
    }
}