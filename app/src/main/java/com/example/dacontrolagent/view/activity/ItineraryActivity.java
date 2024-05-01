package com.example.dacontrolagent.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.view.fragment.DeliveryDetailFragment;
import com.example.dacontrolagent.view.fragment.ListOfDeliveryFragment;
import com.example.dacontrolagent.view.fragment.NoRouteFragment;
import com.example.dacontrolagent.R;
import com.example.dacontrolagent.viewmodel.DeliveryViewModel;
import com.example.dacontrolagent.viewmodel.UserViewModel;
import com.example.dacontrolagent.viewmodel.sqlLite.UserLoggedManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DeliveryViewModel deliveryViewModel;
    private UserLoggedManager userLoggedManager;

    private DrawerLayout drawerLayout;
    private TextView appBarText;

    private LocationManager locationManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_itinerary);
        checkPermissions();

        userLoggedManager = new UserLoggedManager(this);
        deliveryViewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));

        appBarText = findViewById(R.id.appBarText);

        deliveryViewModel.getLiveDeliveries().observe(this, deliveries -> {
            Fragment fragment;
            if(deliveries.isEmpty()) {
                fragment = new NoRouteFragment();
            } else {
                fragment = new ListOfDeliveryFragment(deliveries, appBarText);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        });


        deliveryViewModel.getDeliveryRoute(
                getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        .getString("emailOfLoggedPerson", ""), LocalDate.now());

    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE,
            }, 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10) {
            checkPermissions();
        }
    }

    public static void addChildInLayout(ViewGroup layout, View...viewGroupsToAdd) {
        for (View viewGroup : viewGroupsToAdd) {
            layout.addView(viewGroup);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.itineraryNav) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListOfDeliveryFragment(deliveryViewModel.getLiveDeliveries().getValue(), appBarText)).commit();
        } else if (item.getItemId() == R.id.myAccountNav) {
            Toast.makeText(this, R.string.page_not_created, Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.logoutNav) {
            userLoggedManager.deleteUser();
            getSharedPreferences("AppPref", MODE_PRIVATE).edit().remove("isLoggedIn").remove("emailOfLoggedPerson").apply();
            Intent intentToLogin = new Intent(ItineraryActivity.this, MainActivity.class);
            startActivity(intentToLogin);
        }
        else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.itineraryNav) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListOfDeliveryFragment(deliveryViewModel.getLiveDeliveries().getValue(), appBarText)).commit();
        } else if (menuItem.getItemId() == R.id.myAccountNav) {
            Toast.makeText(this, R.string.page_not_created, Toast.LENGTH_LONG).show();
        } else if (menuItem.getItemId() == R.id.logoutNav) {
            userLoggedManager.deleteUser();
            getSharedPreferences("AppPref", MODE_PRIVATE).edit().remove("isLoggedIn").remove("emailOfLoggedPerson").apply();
            Intent intentToLogin = new Intent(ItineraryActivity.this, MainActivity.class);
            startActivity(intentToLogin);
        }
        else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}