package vn.edu.stu.doangk_qlsach;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import vn.edu.stu.doangk_qlsach.helper.MenuHelper;

    public class About extends AppCompatActivity implements OnMapReadyCallback {
        private GoogleMap mMap;
        private static final LatLng STU_LOCATION = new LatLng(10.7385, 106.6799);
        private Button btnDial, btnCall;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_about);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            addControls();
            addEvents();
            setupMap();
        }

        private void addEvents() {
            String phoneNumber = "0907165254";
            btnDial.setOnClickListener(v -> openDialer(phoneNumber));

            // Button Call - Gọi trực tiếp
            btnCall.setOnClickListener(v -> checkPermissionAndCall());
        }

        private void openDialer(String phoneNumber) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, R.string.cannot_open_dialer, Toast.LENGTH_SHORT).show();
            }
        }

        private void checkPermissionAndCall() {
            try {
                if (ActivityCompat.checkSelfPermission(
                        About.this,
                        Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            About.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            100
                    );
                }

                String phone = "0907165254";
                Intent intent = new Intent(
                        Intent.ACTION_CALL,
                        Uri.parse("tel:"+phone)
                );

                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setupMap() {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
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
            MaterialToolbar toolbar = findViewById(R.id.topAppBar);
            setSupportActionBar(toolbar);
            btnDial = findViewById(R.id.btn_dial);
            btnCall = findViewById(R.id.btn_call);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Thêm marker tại STU
            mMap.addMarker(new MarkerOptions()
                    .position(STU_LOCATION)
                    .title(getString(R.string.stu_name))
                    .snippet(getString(R.string.stu_address)));

            // Di chuyển camera đến STU với zoom level 15
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(STU_LOCATION, 20));

            // Bật zoom controls
            mMap. getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings(). setZoomGesturesEnabled(true);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 100) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionAndCall();
                }
            }
        }
    }