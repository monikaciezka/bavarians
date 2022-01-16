package com.bavarians.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.bavarians.graphql.repository.RoleRepository;
import com.bavarians.graphql.model.Role;
import com.bavarians.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bavarians.graphql.repository.KlientRepository;
import com.bavarians.graphql.model.Klient;
import com.bavarians.dto.UserDto;

@Service
public class DefaultUserDetailsService implements UserDetailsService, UserService {
    @Autowired
    private KlientRepository klientRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Klient user = klientRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHaslo(),
                new ArrayList<>());
    }

    @Override
    public Klient findByEmail(String email) {
        return klientRepository.findByEmail(email);
    }
    public Iterable<Klient> findAll() {
        return klientRepository.findAll();
    }

    @Override
    public void save(Klient klient) {
        klient.setHaslo(bCryptPasswordEncoder.encode(klient.getHaslo()));
        klient.setRoles(new HashSet<>((Collection<? extends Role>) roleRepository.findAll()));
        klientRepository.save(klient);
    }

    @Override
    public Klient save(UserDto user) {
        Klient klient = new Klient();
        klient.setEmail(user.getUsername());
        klient.setHaslo(bCryptPasswordEncoder.encode(user.getPassword()));
        klient.setRoles(new HashSet<>((Collection<? extends Role>) roleRepository.findAll()));
        return klientRepository.save(klient);
    }
}
