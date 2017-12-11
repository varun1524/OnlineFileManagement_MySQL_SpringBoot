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
@RequestMapping(path="/share") // This means URL's start with /demo (after Application path)
public class ShareController {
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

    @PostMapping(path = "/share", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareData(@RequestBody String userData, HttpSession session) {
        try {
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                JSONObject jsonObject = new JSONObject(userData);
                System.out.println(jsonObject);
                int sharedItemId = jsonObject.getInt("itemid");
                System.out.println(sharedItemId);
                JSONArray userdataArray = jsonObject.getJSONArray("userdata");
                System.out.println(userdataArray);
                System.out.println(userdataArray.length());
                boolean status = false;
                for (int i = 0; i < userdataArray.length(); i++) {
                    String user = userdataArray.getString(i);
                    if (!user.equals(username)) {
                        List<User> userList = userService.login(user);
                        System.out.println(userList);
                        if (userList.size() == 1) {
                            List<SharedDetails> sharedDetailsList = sharedDetailsService.findBySharedItemIdAndSharedwith(sharedItemId, user);
                            if (sharedDetailsList.size() == 0) {
                                SharedDetails sharedDetails = new SharedDetails();
                                sharedDetails.setSharedItemId(sharedItemId);
                                sharedDetails.setSharedwith(user);
                                sharedDetailsService.shareData(sharedDetails);
                                int sharedStatus = dropboxStorageService.changeSharedStatus(sharedItemId, true);
                                if (sharedStatus == 1) {
                                    System.out.println("Shared Successfully");
                                    StorageActivity storageActivity = new StorageActivity();
                                    storageActivity.setActivityTime(new Date());
                                    storageActivity.setItemId(sharedItemId);
                                    storageActivity.setItemType(dropboxStorageService.findById(sharedItemId).getType());
                                    storageActivity.setUsername(username);
                                    storageActivity.setItemName(dropboxStorageService.findById(sharedItemId).getName());
                                    storageActivity.setActivityType(ActivityType.SHARE);
                                    status = true;
                                } else {
                                    System.out.println("Error");
                                }
                            } else if (userList.size() == 1) {
                                System.out.println("Data already shared with user");
                            } else {
                                System.out.println("Error");
                            }
                        } else {
                            System.out.println("User does not exist with given username : " + user);
                        }
                    }
                }
                if (status) {
                    return new ResponseEntity(null, HttpStatus.OK);
                } else {
                    return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
                }

            } else {
                System.out.println("Session Expired");
                return new ResponseEntity(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
        }
    }

    @PostMapping(path = "/getDataSharedByUser")
    public ResponseEntity<?> getDataSharedByUser(HttpSession session) {
        try {
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                List<DropboxStorage> storageList = dropboxStorageService.findByOwnerusernameAndSharedstatus(username, true);
                if (storageList.size() > 0) {
                    return new ResponseEntity(storageList, HttpStatus.OK);
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

    @PostMapping(path = "/fetchDataSharedWithUser")
    public ResponseEntity<?> getDataSharedWithUser(HttpSession session) {
        try {
            if (session.getAttribute("username") != null) {

                String username = session.getAttribute("username").toString();
                System.out.println("In get DataSharedWithUser: " + username);
                List<SharedDetails> sharedDetailsList = sharedDetailsService.findBySharedwith(username);
                if (sharedDetailsList.size() > 0) {
                    System.out.println(sharedDetailsList);
                    List<DropboxStorage> dropboxStoragesList = new ArrayList<DropboxStorage>();
                    for (SharedDetails shared : sharedDetailsList) {
                        dropboxStoragesList.add(dropboxStorageService.findById(shared.getSharedItemId()));
                    }
                    System.out.println("dropboxStoragesList");
                    System.out.println(dropboxStoragesList);
                    return new ResponseEntity(dropboxStoragesList, HttpStatus.OK);
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

    @PostMapping(path = "/accessSharedData", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> accessSharedData(@RequestBody DropboxStorage data, HttpSession session) {
        try {
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                System.out.println(data);
                String path = data.getPath() + data.getName() + "/";
                System.out.println("Path  : " + path);
                List<DropboxStorage> storageList = dropboxStorageService.findByPath(path);
                if (storageList.size() > 0) {
                    return new ResponseEntity(storageList, HttpStatus.OK);
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