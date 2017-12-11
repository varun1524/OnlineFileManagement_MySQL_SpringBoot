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
@RequestMapping(path="/profile") // This means URL's start with /demo (after Application path)
public class ProfileController {
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

    @PostMapping(path = "/getprofile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(HttpSession session) {
        if(session.getAttribute("username")!=null){
            String username = session.getAttribute("username").toString();
            UserProfile userProfile = userProfileService.findByUsername(username);
            if(userProfile!=null){
                System.out.println(userProfile);
                return new ResponseEntity(userProfile, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
            }
        }
        else {
            return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PostMapping(path = "/changeProfile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserProfile userProfile, HttpSession session) {
        if(session.getAttribute("username")!=null){
            String username = session.getAttribute("username").toString();
            UserProfile dbUserProfile = userProfileService.findByUsername(username);
            userProfile.setId(dbUserProfile.getId());
            userProfile = userProfileService.addUser(userProfile);
            if(userProfile!=null){
                return new ResponseEntity(null, HttpStatus.CREATED);
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