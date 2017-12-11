package com.server.dropbox_springboot_sever.controller;

import com.server.dropbox_springboot_sever.ActivityType;
import com.server.dropbox_springboot_sever.entity.*;
import com.server.dropbox_springboot_sever.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/activity") // This means URL's start with /demo (after Application path)
public class ActivityController {
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

    @PostMapping(path = "/getUserActivityData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserActivityData(HttpSession session) {
        if(session.getAttribute("username")!=null){
            String username = session.getAttribute("username").toString();
            List<UserActivity> userActivityList = userActivityService.fetchUserActivity(username);
            if(userActivityList.size()>0){
                return new ResponseEntity(userActivityList, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }
        }
        else {
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping(path = "/getStorageActivityData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStorageActivityData(HttpSession session) {
        if(session.getAttribute("username")!=null){
            String username = session.getAttribute("username").toString();
            List<StorageActivity> storageActivityList = storageActivityService.fetchStorageActivity(username);
            System.out.println("storageActivityList : "+storageActivityList.size());
            if(storageActivityList.size()>0){
                return new ResponseEntity(storageActivityList, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }
        }
        else {
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }
}