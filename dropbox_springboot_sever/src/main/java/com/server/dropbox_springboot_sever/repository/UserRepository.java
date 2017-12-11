package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
//    @Query("select u FROM user u WHERE u.email = :email")
//    List<User> findByUsername(@Param("email") String email);
    List<User> findByUsername(@Param("username") String username);
}
