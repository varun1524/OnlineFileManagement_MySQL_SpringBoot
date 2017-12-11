package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.StorageActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageActivityRepository extends JpaRepository<StorageActivity, Long> {
    List<StorageActivity> findByUsername(String username);
}
