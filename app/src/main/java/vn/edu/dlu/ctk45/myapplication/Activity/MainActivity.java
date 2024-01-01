package vn.edu.dlu.ctk45.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import vn.edu.dlu.ctk45.myapplication.Fragment.Home;
import vn.edu.dlu.ctk45.myapplication.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        updateSwitchText();

        Intent intent = getIntent();
        Boolean isSignIn = intent.getBooleanExtra("isSignIn",false);
        if (isSignIn){
            token = intent.getStringExtra("token");
        }

        updateSignInState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();

        } else if (id == R.id.nav_switch) {
            toggleNightMode();
        } else if (id == R.id.nav_login) {
            if (token.isEmpty()){
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }else {
                token = "";
                updateSignInState();
                Toast.makeText(MainActivity.this,"Đã đăng xuất",Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivity.this, FavoritesManga.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_genre){
            Intent intent = new Intent(MainActivity.this, GenreManga.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    private void toggleNightMode() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    private void updateSwitchText() {
        MenuItem switchMenuItem = navigationView.getMenu().findItem(R.id.nav_switch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchMenuItem.setTitle("Day Mode");
            switchMenuItem.setIcon(R.drawable.ic_sunny);
        } else {
            switchMenuItem.setTitle("Night Mode");
            switchMenuItem.setIcon(R.drawable.ic_nightlight);
        }
    }

    private void updateSignInState() {
        MenuItem switchMenuItem = navigationView.getMenu().findItem(R.id.nav_login);
        if (token.isEmpty()) {
            switchMenuItem.setTitle("Đăng nhập");
            switchMenuItem.setIcon(R.drawable.nav_login);
        } else {
            switchMenuItem.setTitle("Đăng Xuất");
            switchMenuItem.setIcon(R.drawable.nav_logout);
        }
    }

}