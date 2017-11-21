package com.server.dropbox_springboot_sever.repository;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DropboxStorageRepository extends CrudRepository<DropboxStorage, Long>{

    List<DropboxStorage> findByPath(String Path);
}
