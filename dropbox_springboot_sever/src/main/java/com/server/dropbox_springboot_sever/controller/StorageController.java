package com.server.dropbox_springboot_sever.controller;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import com.server.dropbox_springboot_sever.service.DropboxStorageService;
import com.server.dropbox_springboot_sever.service.SharedDetailsService;
import com.server.dropbox_springboot_sever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
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
            System.out.println(storage);
            /*FileOperationLogs fileOperationLogs = new FileOperationLogs();
            fileOperationLogs.setAction_type("File Uploaded");
            fileOperationLogs.setCreation_time(new Date());
            fileOperationLogs.setFile_id(insertFolder.getId());
            fileOperationLogs.setFile_name(file.getOriginalFilename());
            fileOperationLogs.setUserid(Integer.parseInt(username));

            fileLogService.addData(fileOperationLogs);*/
            return new ResponseEntity(null, HttpStatus.CREATED);
        }
        else
        {
            System.out.println("Session Expired");
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }
}
