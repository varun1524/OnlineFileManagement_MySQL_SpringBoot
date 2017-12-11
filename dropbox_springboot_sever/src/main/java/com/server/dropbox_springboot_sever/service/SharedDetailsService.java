package com.server.dropbox_springboot_sever.service;


import com.server.dropbox_springboot_sever.entity.SharedDetails;
import com.server.dropbox_springboot_sever.repository.SharedDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedDetailsService {
    @Autowired
    private SharedDetailsRepository sharedDetailsRepository;

    public List<SharedDetails> findBySharedItemIdAndSharedwith(int sharedItemId, String sharedWith){
        return sharedDetailsRepository.findBySharedItemIdAndSharedwith(sharedItemId, sharedWith);
    }

    public List<SharedDetails> findBySharedwith(String sharedWith){
        return sharedDetailsRepository.findBySharedwith(sharedWith);
    }

    public void shareData(SharedDetails sharedDetails){
        sharedDetailsRepository.save(sharedDetails);
    }
}
