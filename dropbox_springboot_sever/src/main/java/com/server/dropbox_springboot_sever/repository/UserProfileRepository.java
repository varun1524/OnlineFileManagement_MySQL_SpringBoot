package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

//import org.springframework.data.repository.CrudRepository;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
//    @Query("select u FROM user u WHERE u.email = :email")
//    List<User> findByUsername(@Param("email") String email);
    UserProfile findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE DropboxStorage u SET u.starred = :status where u.id = :id")
    int updateUserProfile(@Param("id") int id, @Param("status") boolean status);
}
