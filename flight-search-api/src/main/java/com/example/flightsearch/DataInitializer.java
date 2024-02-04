package com.example.flightsearch;



import com.example.flightsearch.repository.dao.UserEntity;
import com.example.flightsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void initializeData() {
        UserEntity user = new UserEntity();
        user.setUsername("jane_doe");
        String encodedPassword = passwordEncoder.encode("pass");
        user.setPassword(encodedPassword);

        userService.addUser(user);
    }
}
