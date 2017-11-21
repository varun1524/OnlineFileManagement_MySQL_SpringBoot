package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Users, Long>{
    List<Users> findUserByUsername(String username);
}
