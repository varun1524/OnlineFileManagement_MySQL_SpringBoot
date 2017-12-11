package com.server.dropbox_springboot_sever.controller;

import com.server.dropbox_springboot_sever.ActivityType;
import com.server.dropbox_springboot_sever.entity.*;
import com.server.dropbox_springboot_sever.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/group") // This means URL's start with /demo (after Application path)
public class GroupController {
    @Autowired
    UserService userService;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    DropboxStorageService dropboxStorageService;
    @Autowired
    SharedDetailsService sharedDetailsService;
    @Autowired
    UserActivityService userActivityService;
    @Autowired
    StorageActivityService storageActivityService;
    @Autowired
    GroupStorageService groupStorageService;

    @PostMapping(path = "/creategroup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGroup(@RequestBody String group, HttpSession session) {
        if (session.getAttribute("username") != null) {
            JSONObject jsonObject = new JSONObject(group);
            System.out.println(jsonObject);
            System.out.println(jsonObject.getString("groupName"));
            String groupName = jsonObject.getString("groupName");
            String username = session.getAttribute("username").toString();

            GroupStorage groupStorage = new GroupStorage();
            groupStorage.setGroupname(groupName);
            groupStorage.setGroupowner(username);
            groupStorage.setCreationtime(new Date());

            groupStorage = groupStorageService.addData(groupStorage);
            System.out.println(groupStorage);
            if(groupStorage!=null){
                return new ResponseEntity(null, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }

        } else {
            System.out.println("Session Expired");
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping(path = "/getgroups", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGroups(HttpSession session) {
        if (session.getAttribute("username") != null) {
            String username = session.getAttribute("username").toString();
            List<GroupStorage> groupStorageList = groupStorageService.findByGroupowner(username);
            System.out.println("groupStorageList");
            System.out.println("Size : " + groupStorageList.size());
            if(groupStorageList.size()>0){
                return new ResponseEntity(groupStorageList, HttpStatus.CREATED);
            }
            else if(groupStorageList.size()==0){
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }
        } else {
            System.out.println("Session Expired");
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }
}
