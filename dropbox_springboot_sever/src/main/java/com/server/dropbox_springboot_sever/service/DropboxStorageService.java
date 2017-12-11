package com.server.dropbox_springboot_sever.service;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import com.server.dropbox_springboot_sever.repository.DropboxStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DropboxStorageService {
    @Autowired
    private DropboxStorageRepository dropboxStorageRepository;

    public DropboxStorage addData(DropboxStorage data){
        return dropboxStorageRepository.save(data);
    }

    public List<DropboxStorage> findByPath(String path){
        return dropboxStorageRepository.findByPath(path);
    }

    public int changeStarredStatus(int itemId, boolean status){
        return dropboxStorageRepository.updateStarredStatus(itemId, status);
    }

    public int changeSharedStatus(int itemId, boolean status){
        return dropboxStorageRepository.updateSharedStatus(itemId, status);
    }

    public List<DropboxStorage> findByOwnerusernameAndStarred(String username, boolean status){
        return dropboxStorageRepository.findByOwnerusernameAndStarred(username, status);
    }

    public List<DropboxStorage> findByOwnerusernameAndSharedstatus(String username, boolean status){
        return dropboxStorageRepository.findByOwnerusernameAndSharedstatus(username, status);
    }

    public DropboxStorage findById(int id){
        return dropboxStorageRepository.findById(id);
    }

    public boolean createDirectory(String dirName, String path) {
        System.out.println("in create directory");
        File folder = null;
        String createDirPath = path + "/" + dirName;
        createDirPath.replace("//","/");
        if ("".equalsIgnoreCase(path)) {
            System.out.println("in if");
            folder = new File(dirName);
        } else {
            System.out.println("in else");
            folder = new File(createDirPath);
        }

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Done");
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public void deleteItem(DropboxStorage data){
        dropboxStorageRepository.delete(data);
    }

    public void deleteDirectory(File file){

        for (File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                deleteDirectory(childFile);
            } else {
                if (!childFile.delete()) {
                    System.out.println("error in recursion");
                }
            }
        }

        if (!file.delete()) {
            System.out.println("error in recursion 1");

        }
    }

    public void deleteFolderContent(String path){
        dropboxStorageRepository.deleteByPathLike(path);
    }
}
