package com.server.dropbox_springboot_sever.controller;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import com.server.dropbox_springboot_sever.entity.Users;
import com.server.dropbox_springboot_sever.service.DropboxStorageService;
import com.server.dropbox_springboot_sever.service.UserService;
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
@RequestMapping(path="/users") // This means URL's start with /demo (after Application path)
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    DropboxStorageService dropboxStorageService;

    @PostMapping(path="/doLogin",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody String user, HttpSession session)
    {
        JSONObject jsonObject = new JSONObject(user);
        System.out.println("Hello World");
        System.out.println(jsonObject.getString("username"));
        List<Users> userList = userService.login(jsonObject.getString("username"));
//        System.out.println(userList);
//        System.out.println(userList.get(0).hashpassword);
        String pswd = userList.get(0).hashpassword;
        if(userList.get(0).hashpassword.equals(pswd)){
            session.setAttribute("username",jsonObject.getString("username"));
            return new ResponseEntity((userList.get(0).username), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity((userList.get(0).username), HttpStatus.UNAUTHORIZED);
        }

    }

//    @GetMapping(path="/all",produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody Iterable<User> getAllUsers() {
//        // This returns a JSON with the users
//        return userService.getAllUsers();
//    }


    //TODO : COnvert it to GetMapping Later
    @PostMapping(path="/getSession",consumes = MediaType.APPLICATION_JSON_VALUE)
    public  HttpSession getSession(HttpSession session) {
        // This returns a JSON with the users
        JSONObject jsonObject = new JSONObject();
        String status;
        System.out.println(session.getAttribute("username"));
        if(session.getAttribute("username")==null){
            status = "301";
        }
        else {
            jsonObject.append("username", session.getAttribute("username"));
            status = "201";
        }

//        System.out.println(jsonObject);
        return session;
    }


    @PostMapping(path = "/createDir", consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> createDirectory(@RequestBody String user, HttpSession session){
        System.out.println(session.getAttribute("username"));
        if(session.getAttribute("username")!=null){
            JSONObject jsonObject = new JSONObject(user);
            System.out.println(jsonObject);
//            console.log("in create directory");
//            console.log(req.session.username);
//            console.log(req.body.directoryName);
            String receivedPath = jsonObject.getString("dirpath");
            String receivedName = jsonObject.getString("directoryName");

            String username = session.getAttribute("username").toString();
            String userDirpath = "./dropboxstorage/"+username+"/"+receivedPath;
//                if(fs.existsSync(userDirpath)){
            String createDirpath = userDirpath;
            System.out.println("Create Directory Path: "+createDirpath);


            DropboxStorage dropboxStorage = new DropboxStorage();
            dropboxStorage.setName(receivedName);
            dropboxStorage.setPath(createDirpath);
            dropboxStorage.setCreationtime(new Date());
            dropboxStorage.setType("d");

            dropboxStorageService.addData(dropboxStorage);
            return new ResponseEntity(null, HttpStatus.CREATED);
//                    System.out.println("Parent Directory Path: "+userDirpath);
//                    if(!fs.existsSync(createDirpath)) {

//                        insertIntoStorage(function (err, result) {
//                            if(err){
//                                res.status(301).send({message: "Error while adding directory data into database"});
//                            }
//                            if(result){
//                                fs.mkdir(createDirpath, null, function (err) {
//                                    console.log(err);
//                                    if (err) {
//                                        throw ("failed to create directory" + err);
//                                    }
//                                    console.log("Directory Created Successfully");
//                                    res.status(201).send({message: "Directory Created Successfully"});
//                                });
//                            }
//                            else {
//                                res.status(301).send({message: "Error while adding directory data into database"});
//                            }
//                        },req.body.directoryName, userDirpath, "d", username);

//                    }
//                    else {
//                        res.status(301).send({message: "Directory already exists"});
//                    }
//                }
//                else{
//                    throw "Error while creating directory";
//                }
        }
        else {
            System.out.println("Session Expired");
        }
        return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(path="/getDirData",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDirectoryData(@RequestBody String user, HttpSession session)
    {
        System.out.println("Hello");
        System.out.println(session.getAttribute("username"));

        JSONObject jsonObject = new JSONObject(user);
        System.out.println(jsonObject);
        System.out.println(jsonObject.getString("path"));
        System.out.println("Path : " + jsonObject.getString("path"));
//        List<DropboxStorage> storageList;
        if(session.getAttribute("username")!=null) {
            String username = session.getAttribute("username").toString();
            String clientPath = jsonObject.getString("path");
            String dirpath;
            if (clientPath.equals("") || clientPath == null || clientPath.equals("/")) {
                dirpath = ("./dropboxstorage/" + username + "/" );
            }
            else {
                dirpath = ("./dropboxstorage/" + username + "/" + clientPath);
            }
            System.out.println("Directory Path : " + dirpath);

            // let files = fs.readdirSync(dirpath);
            // console.log(files);
            JSONObject jsonObj;
//            let i = 0;
            dirpath=dirpath.replace("//","/");

            List<DropboxStorage> storageList = dropboxStorageService.findByPath(dirpath);
            System.out.println(storageList.size());
            System.out.println(storageList);

            if(storageList.size()>0){
                return new ResponseEntity(storageList, HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);
            }
//            let fetchQuery="select * from dropboxstorage where path = '" + dirpath+"'";
//            console.log("fetch Query : " + fetchQuery);
//
//            mysql.fetchData(function(err,results){
//                // console.log(results);
//                if(err){
//                    throw err;
//                }
//                else
//                {
//                    if(results.length>0) {
//                        for (i = 0; i < results.length; i++) {
//                            let tempObj = {};
//                            console.log(results[i].path);
//                            tempObj["id"] = results[i].id;
//                            tempObj["name"] = results[i].name;
//                            tempObj["type"] = results[i].type;
//                            tempObj["ctime"] = results[i].creationtime;
//                            // tempObj["mtime"] = results[i].modifiedtime;
//                            tempObj["path"] = results[i].path;
//                            tempObj["size"] = results[i].size;
//                            tempObj["starred"] = results[i].starred;
//                            tempObj["sharedstatus"] = results[i].sharedstatus;
//                            jsonObj.push(tempObj);
//                        }
//                        res.status(201).send(jsonObj);
//                    }
//                    else {
//                        res.status(204).send({"message":"Directory is Empty"});
//                    }
//                }
//            },fetchQuery);
        }
        else{
            System.out.println("Session Expired");
//            res.status(203).send({"message":"Session Expired. Please Login Again"});
        }


//        if(userList.get(0).hashpassword.equals(pswd)){
//            session.setAttribute("username",jsonObject.getString("username"));
//            return new ResponseEntity((userList.get(0).username), HttpStatus.CREATED);
//        }
//        else {
//            return new ResponseEntity((userList.get(0).username), HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity(null, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(value = "/doLogout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpSession session) {
        System.out.println(session.getAttribute("username"));
        session.invalidate();
        return  new ResponseEntity(HttpStatus.OK);
    }

}
