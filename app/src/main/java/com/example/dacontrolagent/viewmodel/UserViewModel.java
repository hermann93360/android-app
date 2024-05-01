package com.example.dacontrolagent.viewmodel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.ViewModel;

import com.example.dacontrolagent.domain.model.User;
import com.example.dacontrolagent.domain.repository.impl.InMemory;
import com.example.dacontrolagent.domain.service.UserService;
import com.example.dacontrolagent.domain.usecase.LoginUseCase;
import com.example.dacontrolagent.viewmodel.sqlLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;


    public UserViewModel() {
        this.loginUseCase = new UserService(new InMemory());

    }

    public boolean login(String email, String password) {
        return loginUseCase.login(email, password);
    }



}
