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
import java.util.List;

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

    @PostMapping(path = "/createDir", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDirectory(@RequestBody String user, HttpSession session) {
        System.out.println(session.getAttribute("username"));
        if (session.getAttribute("username") != null) {
            JSONObject jsonObject = new JSONObject(user);
            System.out.println(jsonObject);
            String receivedPath = jsonObject.getString("dirpath");
            String receivedName = jsonObject.getString("directoryName");
            System.out.println("receivedPath : " + receivedPath);
            String username = session.getAttribute("username").toString();
            String userDirpath = "./dropboxstorage/" + username + "/" + receivedPath;
//                if(fs.existsSync(userDirpath)){
            String createDirpath = userDirpath;
            System.out.println("Create Directory Path: " + createDirpath);


            DropboxStorage dropboxStorage = new DropboxStorage();
            dropboxStorage.setName(receivedName);
            dropboxStorage.setPath(createDirpath);
            dropboxStorage.setCreationtime(new Date());
            dropboxStorage.setType("d");
            dropboxStorage.setOwnerusername(username);

            dropboxStorage = dropboxStorageService.addData(dropboxStorage);
            if(dropboxStorage!=null){
                if(dropboxStorageService.createDirectory(receivedName, userDirpath)){
                    StorageActivity storageActivity = new StorageActivity();
                    storageActivity.setActivityTime(new Date());
                    storageActivity.setItemId(dropboxStorage.getId());
                    storageActivity.setItemName(dropboxStorage.getName());
                    storageActivity.setActivityType(ActivityType.INSERT);
                    storageActivity.setItemType("d");
                    storageActivity.setUsername(username);
                    storageActivityService.addUser(storageActivity);
                    return new ResponseEntity(null, HttpStatus.CREATED);
                }
                else {
                    return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
                }
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }
        } else {
            System.out.println("Session Expired");
        }
        return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(path = "/getDirData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDirectoryData(@RequestBody String user, HttpSession session) {
        System.out.println(session.getAttribute("username"));

        JSONObject jsonObject = new JSONObject(user);
        System.out.println(jsonObject);
        System.out.println(jsonObject.getString("path"));
        System.out.println("Path : " + jsonObject.getString("path"));
//        List<DropboxStorage> storageList;
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            String clientPath = jsonObject.getString("path");
            String dirpath;
            if (clientPath.equals("") || clientPath == null || clientPath.equals("/")) {
                dirpath = ("./dropboxstorage/" + username + "/");
            } else {
                dirpath = ("./dropboxstorage/" + username + "/" + clientPath);
            }
            System.out.println("Directory Path : " + dirpath);

            // let files = fs.readdirSync(dirpath);
            // console.log(files);
            JSONObject jsonObj;
//            let i = 0;
            dirpath = dirpath.replace("//", "/");

            List<DropboxStorage> storageList = dropboxStorageService.findByPath(dirpath);
            System.out.println(storageList.size());
            System.out.println(storageList);

            if (storageList.size() > 0) {
                return new ResponseEntity(storageList, HttpStatus.CREATED);
            } else {
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);
            }
        } else {
            System.out.println("Session Expired");
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping(path = "/changestarredstatus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeStarredStatus(@RequestBody String storage, HttpSession session) {
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
//            console.log(JSON.stringify(req.body));
            JSONObject jsonObject = new JSONObject(storage);
            System.out.println(jsonObject);
            int itemid = jsonObject.getInt("id");
            boolean status = jsonObject.getBoolean("changeStatusTo");

            int updateStatus = dropboxStorageService.changeStarredStatus(itemid, status);
            System.out.println("Update Status : " + updateStatus);
            if (updateStatus == 1) {
                StorageActivity storageActivity = new StorageActivity();
                storageActivity.setActivityTime(new Date());
                storageActivity.setItemId(itemid);
                storageActivity.setItemName(dropboxStorageService.findById(itemid).getName());
                storageActivity.setItemType(dropboxStorageService.findById(itemid).getType());
                storageActivity.setUsername(username);
                if(status){
                    storageActivity.setActivityType(ActivityType.STARRED);
                }
                else {
                    storageActivity.setActivityType(ActivityType.UNSTARRED);
                }
                storageActivityService.addUser(storageActivity);

                return new ResponseEntity(null, HttpStatus.CREATED);
            } else {
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);
            }

        } else {
            System.out.println("Session Expired");
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping(path = "/getStarredData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> fetchStarredData(@RequestBody String storage, HttpSession session) {
        try {
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                JSONObject jsonObject = new JSONObject(storage);
                System.out.println(jsonObject);
                String clientPath = jsonObject.getString("path");
                String dirpath;
                if (clientPath == "" || clientPath == null || clientPath == "/") {
                    dirpath = ("./dropboxstorage/" + username + "/");
                } else {
                    dirpath = ("./dropboxstorage/" + username + "/" + clientPath);
                }
                System.out.println(dirpath);

                /*let files = fs.readdirSync(dirpath);
                console.log(files);
                let jsonObj = [];
                let i = 0;*/

                dirpath = dirpath.replace("//", "/");
                List<DropboxStorage> storageList = dropboxStorageService.findByOwnerusernameAndStarred(username, true);
                if (storageList.size() > 0) {
                    return new ResponseEntity(storageList, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity(null, HttpStatus.NO_CONTENT);
                }
            } else {
                System.out.println("Session Expired");
                return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
//            res.status(301).send({"message" : e});
        }
    }

}
