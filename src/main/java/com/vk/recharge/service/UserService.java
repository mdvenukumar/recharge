package com.vk.recharge.service;

import com.vk.recharge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isPhoneNumberExists(String ph) {
        return userRepository.existsByPh(ph);
    }
}