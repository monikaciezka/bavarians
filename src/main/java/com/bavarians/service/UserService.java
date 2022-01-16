package com.bavarians.service;


import com.bavarians.graphql.model.Klient;
import com.bavarians.dto.UserDto;

public interface UserService {
    void save(Klient user);

    Klient findByEmail(String username);

    Klient save(UserDto user);
}