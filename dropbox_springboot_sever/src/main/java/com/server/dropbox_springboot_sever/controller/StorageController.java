package com.server.dropbox_springboot_sever.controller;

import com.server.dropbox_springboot_sever.ActivityType;
import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import com.server.dropbox_springboot_sever.entity.StorageActivity;
import com.server.dropbox_springboot_sever.service.DropboxStorageService;
import com.server.dropbox_springboot_sever.service.SharedDetailsService;
import com.server.dropbox_springboot_sever.service.StorageActivityService;
import com.server.dropbox_springboot_sever.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/file") // This means URL's start with /demo (after Application path)
public class StorageController {
    @Autowired
    UserService userService;
    @Autowired
    DropboxStorageService dropboxStorageService;
    @Autowired
    SharedDetailsService sharedDetailsService;
    @Autowired
    StorageActivityService storageActivityService;

    @PostMapping(path = "/upload")
    public ResponseEntity<?> uploadfile(@RequestParam("myfile") MultipartFile file, @RequestParam("path") String uploadPath, HttpSession session) {
        System.out.println("abc");
//        return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            System.out.println("i am in upload file" + file.getOriginalFilename());
            System.out.println("uploadPath" + uploadPath);
            String uploadFilePath = "./dropboxstorage/"+username+"/"+uploadPath;
            uploadFilePath.replace("//","/");
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes(); // converting file into bytes
                    Path path = Paths.get(uploadFilePath + file.getOriginalFilename()); // giving path
                    Files.write(path, bytes);  // this line will create a file from the request
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            DropboxStorage fileDetails = new DropboxStorage();
            fileDetails.setName(file.getOriginalFilename());
            fileDetails.setPath(uploadFilePath);
            fileDetails.setCreationtime(new Date());
            fileDetails.setType("f");
            fileDetails.setOwnerusername(username);


            DropboxStorage storage = dropboxStorageService.addData(fileDetails);
            if(storage!=null){
                StorageActivity storageActivity = new StorageActivity();
                storageActivity.setActivityTime(new Date());
                storageActivity.setItemId(storage.getId());
                storageActivity.setItemType("f");
                storageActivity.setUsername(username);
                storageActivity.setItemName(storage.getName());
                storageActivity.setActivityType(ActivityType.INSERT);
                storageActivityService.addUser(storageActivity);
                return new ResponseEntity(null, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }
        }
        else
        {
            System.out.println("Session Expired");
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping(path = "/deleteContent", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@RequestBody DropboxStorage storage,HttpSession session) {
        System.out.println("in delete file ");
        try {
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                JSONObject jsonObject = new JSONObject(storage);
                int id = jsonObject.getInt("id");

                System.out.println("received file for detetion is "+jsonObject);
                String receivedPath = jsonObject.getString("path");
                String receivedName = jsonObject.getString("name");

                String userDirpath = receivedPath+receivedName;
                userDirpath.replace("//","/");
                System.out.println("Delete Directory Path: "+userDirpath);

                File deleteFile = new File(userDirpath);
                String type = "";
                if(deleteFile.isDirectory()){
                    type = "d";
                    dropboxStorageService.deleteDirectory(deleteFile);
                    dropboxStorageService.deleteFolderContent(userDirpath);
                    DropboxStorage dropboxStorage = new DropboxStorage();
                    dropboxStorage.setId(jsonObject.getInt("id"));
                    dropboxStorageService.deleteItem(dropboxStorage);
                }
                else{
                    type = "f";
                    if(deleteFile.delete()){
                        System.out.println(deleteFile.getName() + " is deleted!");
                        DropboxStorage dropboxStorage = new DropboxStorage();
                        dropboxStorage.setId(jsonObject.getInt("id"));
                        dropboxStorageService.deleteItem(dropboxStorage);
                    }else{
                        System.out.println("Delete operation is failed.");
                    }
                }

                StorageActivity storageActivity = new StorageActivity();
                storageActivity.setUsername(username);
                storageActivity.setItemType(type);
                storageActivity.setActivityType(ActivityType.DELETE);
                storageActivity.setItemName(receivedName);
                storageActivity.setActivityTime(new Date());
                storageActivity.setItemId(id);
                storageActivityService.addUser(storageActivity);

                return new ResponseEntity(null, HttpStatus.OK);

            } else {
                System.out.println("Session Expired");
                return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
    }
}
