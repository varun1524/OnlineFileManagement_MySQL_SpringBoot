package com.server.dropbox_springboot_sever.service;


import com.server.dropbox_springboot_sever.entity.StorageActivity;
import com.server.dropbox_springboot_sever.repository.StorageActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageActivityService {
    @Autowired
    private StorageActivityRepository storageActivityRepository;

    public StorageActivity addUser(StorageActivity storageActivity){
        return storageActivityRepository.save(storageActivity);
    }

    public List<StorageActivity> fetchStorageActivity(String username){
        return storageActivityRepository.findByUsername(username);
    }

}
