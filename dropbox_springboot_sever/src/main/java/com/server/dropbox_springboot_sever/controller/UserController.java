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
@RequestMapping(path="/users") // This means URL's start with /demo (after Application path)
public class UserController {
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

    @PostMapping(path = "/doSignUp", consumes = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        user = userService.addUser(user);
        user.setPassword(userService.generateHashPassword(user.getPassword()));
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(user.getUsername());
        userProfile = userProfileService.addUser(userProfile);
        System.out.println(userProfile);
        UserActivity userActivity = new UserActivity();
        userActivity.setUsername(user.getUsername());
        userActivity.setActivityType(ActivityType.SIGNUP);
        userActivity.setActivityTime(new Date());
        userActivityService.addActivity(userActivity);
        System.out.println("user is " + user.getUsername());
        System.out.println("Saved");
        dropboxStorageService.createDirectory(user.getUsername(), "./dropboxstorage");
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @PostMapping(path = "/doLogin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody String user, HttpSession session) {
        JSONObject jsonObject = new JSONObject(user);
        System.out.println(jsonObject);
        String username = jsonObject.getString("username");
        System.out.println(jsonObject.getString("username"));
        List<User> userList = userService.login(jsonObject.getString("username"));
        System.out.println(userList);
        String pswd = userList.get(0).getPassword();
        String hashedPassword = userService.generateHashPassword(jsonObject.getString("password"));


        if (hashedPassword.equals(pswd)) {
            UserActivity userActivity = new UserActivity();
            userActivity.setUsername(username);
            userActivity.setActivityType(ActivityType.LOGIN);
            userActivity.setActivityTime(new Date());
            userActivityService.addActivity(userActivity);
            session.setAttribute("username", jsonObject.getString("username"));
            return new ResponseEntity((userList.get(0).getUsername()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity((userList.get(0).getUsername()), HttpStatus.UNAUTHORIZED);
        }
    }

    //TODO : COnvert it to GetMapping Later
    @PostMapping(path = "/getSession", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSession(HttpSession session) {
        // This returns a JSON with the users
        JSONObject jsonObject = new JSONObject();
        String status;
        System.out.println(session.getAttribute("username"));
        if (session.getAttribute("username") == null) {
            status = "301";
            return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
        } else {
            jsonObject.append("username", session.getAttribute("username"));
            return new ResponseEntity(null, HttpStatus.CREATED);
        }
    }

    @PostMapping(value = "/doLogout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpSession session) {
        System.out.println(session.getAttribute("username"));
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }
}