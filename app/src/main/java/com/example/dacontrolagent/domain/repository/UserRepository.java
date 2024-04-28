package com.example.dacontrolagent.domain.repository;

import com.example.dacontrolagent.domain.model.User;

public interface UserRepository {
    User findUserByEmail(String email);

}
