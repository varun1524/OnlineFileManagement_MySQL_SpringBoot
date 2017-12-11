package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;


public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUsername(String username);
}
