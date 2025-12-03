package vn.edu.stu.doangk_qlsach;

import android.app.AlertDialog;
import android.content.Intent;
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
            btnCall.setOnClickListener(v -> makePhoneCall(phoneNumber));
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

        private void makePhoneCall(String phoneNumber) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            try {
                startActivity(intent);
            } catch (SecurityException e) {
                // Nếu không có permission, hiện thông báo và fallback về DIAL
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_required)
                        .setMessage(R.string.call_permission_denied)
                        .setPositiveButton(R.string.open_dialer, (dialog, which) -> openDialer(phoneNumber))
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.cannot_make_call, Toast.LENGTH_SHORT).show();
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
}