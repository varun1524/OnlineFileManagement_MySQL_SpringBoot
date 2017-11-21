package com.server.dropbox_springboot_sever.service;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import com.server.dropbox_springboot_sever.repository.DropboxStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropboxStorageService {
    @Autowired
    private DropboxStorageRepository dropboxStorageRepository;


    public void addData(DropboxStorage data){
        dropboxStorageRepository.save(data);
    }

    public List<DropboxStorage> findByPath(String username){
        return dropboxStorageRepository.findByPath(username);
    }
}
