package com.server.dropbox_springboot_sever.service;

import com.server.dropbox_springboot_sever.entity.UserProfile;
import com.server.dropbox_springboot_sever.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile findByUsername(String username){
        return  userProfileRepository.findByUsername(username);
    }

    public UserProfile addUser(UserProfile user){
        return userProfileRepository.save(user);
    }


}
