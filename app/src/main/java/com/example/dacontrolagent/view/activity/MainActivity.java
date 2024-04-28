package com.example.dacontrolagent.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dacontrolagent.R;
import com.example.dacontrolagent.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button submitButton;

    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = new UserViewModel();

        emailInput = findViewById( R.id.emailInput );
        passwordInput = findViewById( R.id.passwordInput );
        submitButton = findViewById( R.id.submitButton );

        submitButton.setOnClickListener(v -> {
            boolean resultLogin = userViewModel.login(emailInput.getText().toString(), passwordInput.getText().toString());

            if(resultLogin) {
                getSharedPreferences("AppPrefs", MODE_PRIVATE).edit()
                        .putBoolean("isLoggedIn", true)
                        .putString("emailOfLoggedPerson", emailInput.getText().toString())
                        .apply();

                Intent intentToItinerary = new Intent(MainActivity.this, ItineraryActivity.class);
                startActivity(intentToItinerary);

            } else {
                Toast.makeText(MainActivity.this, R.string.authentication_unsuccessful,
                        Toast.LENGTH_SHORT).show();
            }

        });
    }
}