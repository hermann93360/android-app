package com.example.dacontrolagent.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private DeliveryViewModel deliveryViewModel;

    private DrawerLayout drawerLayout;
    private TextView appBarText;

    private LocationManager locationManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_itinerary);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        appBarText = findViewById(R.id.appBarText);

        deliveryViewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);

        deliveryViewModel.getLiveDeliveries().observe(this, deliveries -> {
            Fragment fragment;
            if(deliveries.isEmpty()) {
                fragment = new NoRouteFragment();
            } else {
                fragment = new ListOfDeliveryFragment(deliveries, appBarText);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.itineraryActivity, fragment);
            transaction.commit();
        });


        deliveryViewModel.getDeliveryRoute(
                getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        .getString("emailOfLoggedPerson", ""), LocalDate.now());

    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA
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
    public void onLocationChanged(@NonNull Location location) {
        //double latitude = location.getLatitude();
        //double longitude = location.getLongitude();

        //Toast.makeText(this, "Location " + latitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}