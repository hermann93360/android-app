package com.example.dacontrolagent.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dacontrolagent.R;
import com.example.dacontrolagent.domain.model.User;
import com.example.dacontrolagent.viewmodel.UserViewModel;
import com.example.dacontrolagent.viewmodel.sqlLite.UserLoggedManager;

import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button submitButton;

    private UserViewModel userViewModel;
    private UserLoggedManager userLoggedManager;

    @Override
    protected void onRestart() {
        super.onRestart();
        Optional<User> userLogged = userLoggedManager.getUserLogged();
        userLogged.ifPresent(user -> loginAndGo(user.getEmail()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLoggedManager = new UserLoggedManager(this);
        userViewModel = new UserViewModel();

        Optional<User> userLogged = userLoggedManager.getUserLogged();
        userLogged.ifPresent(user -> loginAndGo(user.getEmail()));

        emailInput = findViewById( R.id.emailInput );
        passwordInput = findViewById( R.id.passwordInput );
        submitButton = findViewById( R.id.submitButton );

        submitButton.setOnClickListener(v -> {
            boolean resultLogin = userViewModel.login(emailInput.getText().toString(), passwordInput.getText().toString());

            if(resultLogin) {
                userLoggedManager.saveUserLogged(emailInput.getText().toString(), passwordInput.getText().toString());
                loginAndGo(emailInput.getText().toString());

            } else {
                Toast.makeText(MainActivity.this, R.string.authentication_unsuccessful,
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void loginAndGo(String email) {
        getSharedPreferences("AppPrefs", MODE_PRIVATE).edit()
                .putBoolean("isLoggedIn", true)
                .putString("emailOfLoggedPerson", email)
                .apply();

        Intent intentToItinerary = new Intent(MainActivity.this, ItineraryActivity.class);
        startActivity(intentToItinerary);
    }
}