package com.server.dropbox_springboot_sever.service;

import com.server.dropbox_springboot_sever.entity.GroupStorage;
import com.server.dropbox_springboot_sever.repository.GroupStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupStorageService {
    @Autowired
    private GroupStorageRepository groupStorageRepository;

    public GroupStorage addData(GroupStorage data){
        return groupStorageRepository.save(data);
    }

    public List<GroupStorage> findByGroupowner(String username){
        return groupStorageRepository.findByGroupowner(username);
    }
}
