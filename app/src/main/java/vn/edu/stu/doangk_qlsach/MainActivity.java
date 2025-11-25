package vn.edu.stu.doangk_qlsach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static int SPLASH_SCREEN = 4000;
    // variables
    Animation topAnim, botAnim;
    ImageView ivLogo;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addAnims();
        addControls();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }

    private void addAnims() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
    }

    private void addControls() {
        ivLogo = findViewById(R.id.ivLogo);
        tvTitle = findViewById(R.id.tvTitle);

        ivLogo.setAnimation(topAnim);
        tvTitle.setAnimation(botAnim);
    }
}