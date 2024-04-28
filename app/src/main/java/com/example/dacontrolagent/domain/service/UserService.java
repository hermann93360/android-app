package com.example.dacontrolagent.domain.service;

import com.example.dacontrolagent.domain.model.User;
import com.example.dacontrolagent.domain.repository.UserRepository;
import com.example.dacontrolagent.domain.usecase.LoginUseCase;

public class UserService implements LoginUseCase {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean login(String email, String password) {
        User user = this.repository.findUserByEmail(email);
        return user.getPassword().equalsIgnoreCase(password);
    }
}
