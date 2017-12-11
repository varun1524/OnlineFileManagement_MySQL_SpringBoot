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

    //TODO : COnvert it to GetMapping Later
    @PostMapping(path = "/getSession", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpSession getSession(HttpSession session) {
        // This returns a JSON with the users
        JSONObject jsonObject = new JSONObject();
        String status;
        System.out.println(session.getAttribute("username"));
        if (session.getAttribute("username") == null) {
            status = "301";
        } else {
            jsonObject.append("username", session.getAttribute("username"));
            status = "201";
        }
//        System.out.println(jsonObject);
        return session;
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

    @PostMapping(value = "/doLogout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpSession session) {
        System.out.println(session.getAttribute("username"));
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
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
//            res.status(301).send({"message" : e});
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