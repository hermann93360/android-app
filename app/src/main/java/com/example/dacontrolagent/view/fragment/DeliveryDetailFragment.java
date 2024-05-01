package com.example.dacontrolagent.view.fragment;

import static android.Manifest.permission_group.CAMERA;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.VERTICAL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dacontrolagent.R;
import com.example.dacontrolagent.domain.model.Delivery;
import com.example.dacontrolagent.view.activity.CaptureAct;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;


public class DeliveryDetailFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private Delivery delivery;
    private TextView appBarText;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private Circle currentCircle;
    private RelativeLayout deliveryDataContainer;
    private Button goButton;
    private ActivityResultLauncher<Void> cameraLauncher;
    private Button callButton;

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null && result.getContents().equalsIgnoreCase(delivery.getPackageNumber())) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                cameraLauncher.launch(null);
            } else {
                // demander d'activé
            }
        } else {
            Toast.makeText(getActivity(), R.string.invalid_packages, Toast.LENGTH_LONG).show();
        }
    });

    public DeliveryDetailFragment(Delivery delivery, TextView appBarText) {
        this.delivery = delivery;
        this.appBarText = appBarText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        subscriptionToLocationService();

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {

            if (result != null) {
                delivery.setPhotoOfPackage(bitmapToBytes(result));
                FinalDeliveryCheckFragment fragment = new FinalDeliveryCheckFragment(delivery, appBarText);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        });

        return inflater.inflate(R.layout.fragment_delivery_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConfigureGoogleMap();
        initViews(view);
        callButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + "+33760405709"));
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.CALL_PHONE,
                }, 10);
            } else {
                startActivity(intent);
            }
        });

        CustomizationView.of(appBarText)
                .text(R.string.detailsBar);

        TextView nameTextView = new TextView(getActivity());
        CustomizationView.of(nameTextView)
                .text(delivery.getName())
                .font(getActivity(), R.font.poppins_extrabold)
                .size(20);

        TextView addressTextView = new TextView(getActivity());
        CustomizationView.of(addressTextView)
                .text(delivery.getAddress())
                .font(getActivity(), R.font.poppins_italic)
                .size(15);

        TextView packageNumberTextView = new TextView(getActivity());
        CustomizationView.of(packageNumberTextView)
                .text(delivery.getPackageNumber())
                .font(getActivity(), R.font.poppins_italic)
                .size(10);

        LinearLayout textContainer = new LinearLayout(getActivity());
        CustomizationView.of(textContainer)
                .orientation(VERTICAL)
                .withChild(nameTextView, addressTextView, packageNumberTextView);

        CustomizationView.of(deliveryDataContainer)
                .width(MATCH_PARENT)
                .height(MATCH_PARENT)
                .padding(20, 20, 20, 20)
                .withChild(textContainer);

    }

    private void initViews(@NonNull View view) {
        goButton = view.findViewById(R.id.goButton);
        callButton = view.findViewById(R.id.callButton);
        deliveryDataContainer = view.findViewById(R.id.deliveryDetails);
    }

    private void ConfigureGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onPause() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        super.onPause();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updateMap(currentLatLng);

        Location locationOfDelivery = new Location("");
        locationOfDelivery.setLatitude(delivery.getLatitude());
        locationOfDelivery.setLongitude(delivery.getLongitude());

        float distanceWithDeliveryPoint = location.distanceTo(locationOfDelivery);

        if (distanceWithDeliveryPoint > 200) {
            goButton.setText(R.string.go);

            goButton.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + location.getLatitude() + "," + location.getLongitude() + "&destination=" + delivery.getLatitude() + "," + delivery.getLongitude());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getActivity(), R.string.gg_maps_not_found, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            goButton.setText(R.string.scan);
            goButton.setOnClickListener(v -> {
                scanCode();
            });
        }
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scan_data));
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);
    }


    private void updateMap(LatLng currentLatLng) {
        if (googleMap != null) {
            if (currentCircle != null) {
                currentCircle.remove();
            }

            currentCircle = googleMap.addCircle(new CircleOptions()
                    .center(currentLatLng)
                    .radius(15)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.argb(70, 0, 0, 255)));
        }
    }

    @SuppressLint("MissingPermission")
    private void subscriptionToLocationService() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        DeliveryDetailFragment.this.googleMap = googleMap;

        LatLng position = new LatLng(delivery.getLatitude(), delivery.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        MarkerOptions marker = new MarkerOptions().position(position).title(delivery.getName());
        googleMap.addMarker(marker);
    }

    private static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}