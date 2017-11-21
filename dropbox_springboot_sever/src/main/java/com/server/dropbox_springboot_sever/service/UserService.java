package com.server.dropbox_springboot_sever.service;

import com.server.dropbox_springboot_sever.entity.Users;
import com.server.dropbox_springboot_sever.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<Users> login(String username){
        return userRepository.findUserByUsername(username);
    }
}
