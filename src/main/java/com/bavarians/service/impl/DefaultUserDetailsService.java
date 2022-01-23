package com.bavarians.service.impl;

import com.bavarians.graphql.model.Klient;
import com.bavarians.graphql.repository.KlientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    @Autowired
    private KlientRepository klientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Klient user = klientRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHaslo(),
                new ArrayList<>());
    }
}
