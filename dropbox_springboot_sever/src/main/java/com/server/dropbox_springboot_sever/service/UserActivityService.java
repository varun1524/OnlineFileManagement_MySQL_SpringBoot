package com.server.dropbox_springboot_sever.service;


import com.server.dropbox_springboot_sever.entity.UserActivity;
import com.server.dropbox_springboot_sever.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserActivityService {
    @Autowired
    private UserActivityRepository userActivityRepository;

    public UserActivity addActivity(UserActivity userActivity){
        return userActivityRepository.save(userActivity);
    }

    public List<UserActivity> fetchUserActivity(String username){
        return userActivityRepository.findByUsername(username);
    }
}
