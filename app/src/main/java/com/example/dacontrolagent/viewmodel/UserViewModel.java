package com.example.dacontrolagent.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.dacontrolagent.domain.repository.impl.InMemory;
import com.example.dacontrolagent.domain.service.UserService;
import com.example.dacontrolagent.domain.usecase.LoginUseCase;

public class UserViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;

    public UserViewModel() {
        this.loginUseCase = new UserService(new InMemory());

    }

    public boolean login(String email, String password) {
        return loginUseCase.login(email, password);
    }
}
