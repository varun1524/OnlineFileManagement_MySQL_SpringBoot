package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.GroupStorage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupStorageRepository extends CrudRepository<GroupStorage, Long>{

    List<GroupStorage> findByGroupowner(String username);
}
