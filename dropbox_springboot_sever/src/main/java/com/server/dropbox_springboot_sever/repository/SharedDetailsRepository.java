package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.SharedDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//import org.springframework.data.repository.CrudRepository;


public interface SharedDetailsRepository extends JpaRepository<SharedDetails, Long> {
//    @Query("select u FROM user u WHERE u.email = :email")
//    List<User> findByUsername(@Param("email") String email);
    List<SharedDetails> findBySharedItemIdAndSharedwith(int sharedItemId, String sharedWith);

    List<SharedDetails> findBySharedwith(String sharedWith);
}
