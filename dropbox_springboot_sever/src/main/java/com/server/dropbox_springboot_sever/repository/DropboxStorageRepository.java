package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DropboxStorageRepository extends CrudRepository<DropboxStorage, Long>{

    List<DropboxStorage> findByPath(String Path);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Transactional
    @Query("UPDATE DropboxStorage u SET u.starred = :status where u.id = :id")
    int updateStarredStatus(@Param("id") int id, @Param("status") boolean status);

    @Modifying
    @Transactional
    @Query("UPDATE DropboxStorage u SET u.sharedstatus = :status where u.id = :id")
    int updateSharedStatus(@Param("id") int id, @Param("status") boolean status);

    List<DropboxStorage> findByOwnerusernameAndStarred(String username, boolean status);

    List<DropboxStorage> findByOwnerusernameAndSharedstatus(String username, boolean status);

    DropboxStorage findById(int id);


}
