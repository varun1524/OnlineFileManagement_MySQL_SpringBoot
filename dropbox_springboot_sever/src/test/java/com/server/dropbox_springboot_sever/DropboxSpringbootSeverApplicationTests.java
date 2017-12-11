package com.server.dropbox_springboot_sever;

import com.server.dropbox_springboot_sever.entity.*;
import com.server.dropbox_springboot_sever.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DropboxSpringbootSeverApplicationTests {

	@Autowired
	UserService userService;
	@Autowired
	DropboxStorageService dropboxStorageService;
	@Autowired
	SharedDetailsService sharedDetailsService;
	@Autowired
	UserActivityService userActivityService;
	@Autowired
	StorageActivityService storageActivityService;

	public void setUp(){

	}

	public void tearDown(){

	}

	@Test
	public void UserLoginPositive() {
		List<User> userList = userService.login("varun@yahoo.com");
		if(userList.size()==1){
			assert true;
		}
		else {
			assert false;
		}
//		assert
	}

	@Test
	public void UserLoginNegative() {
		List<User> userList = userService.login("var324@yahoo.com");
		System.out.println(userList);
		if(userList.size()==0){
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void findDataByPath() {
		List<DropboxStorage> dropboxStorageList = dropboxStorageService.findByPath("./dropboxstorage/varun@yahoo.com/");
		System.out.println(dropboxStorageList);
		if(dropboxStorageList!=null){
			assert true;
		}
		else {
			assert false;
		}
//		assert
	}

	@Test
	public void changeStarredStatus() {
		int result = dropboxStorageService.changeStarredStatus(1, !(dropboxStorageService.findById(1).isStarred()));
		System.out.println("result : "+result);
		if(result==1){
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void findByOwnerusernameAndStarred() {
		List<DropboxStorage> dropboxStorageList = dropboxStorageService.findByOwnerusernameAndStarred("varun@yahoo.com", true);
		System.out.println("result : "+dropboxStorageList.size());
		if(dropboxStorageList!=null){
			assert true;
		}
		else {
			assert false;
		}
//		assert
	}

	@Test
	public void findById() {
		List<DropboxStorage> dropboxStorageList = dropboxStorageService.findByOwnerusernameAndStarred("varun@yahoo.com", true);
		System.out.println("result : "+dropboxStorageList.size());
		if(dropboxStorageList!=null){
			assert true;
		}
		else {
			assert false;
		}
//		assert
	}

	@Test
	public void findByOwnerusernameAndSharedstatus() {
		List<DropboxStorage> dropboxStorageList = dropboxStorageService.findByOwnerusernameAndSharedstatus("varun@yahoo.com", true);
		System.out.println("result : "+dropboxStorageList.size());
		if(dropboxStorageList!=null){
			assert true;
		}
		else {
			assert false;
		}
//		assert
	}

	@Test
	public void findBySharedItemIdAndSharedwith() {
		List<SharedDetails> sharedDetailsList = sharedDetailsService.findBySharedItemIdAndSharedwith(1, "varun@yahoo.com");
		System.out.println("result : "+sharedDetailsList.size());
		if(sharedDetailsList!=null){
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void findBySharedwith() {
		List<SharedDetails> sharedDetailsList = sharedDetailsService.findBySharedwith("varun@yahoo.com");
		System.out.println("result : "+sharedDetailsList.size());
		if(sharedDetailsList!=null){
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void createDirectory() {
		if(dropboxStorageService.createDirectory("testDir","./test")) {
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void deleteDirectory() {
		File deleteFile = new File("./test/testDir");
		dropboxStorageService.deleteDirectory(deleteFile);
		if(!deleteFile.exists()) {
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void fetchUserActivity(){
		List<UserActivity> userActivityList =  userActivityService.fetchUserActivity("varun@yahoo.com");
		System.out.println(userActivityList.size());
		if(userActivityList!=null){
			assert true;
		}
		else {
			assert false;
		}
	}

	@Test
	public void fetchStorageActivity(){
		List<StorageActivity> storageActivityList =  storageActivityService.fetchStorageActivity("varun@yahoo.com");
		System.out.println(storageActivityList.size());
		if(storageActivityList!=null){
			assert true;
		}
		else {
			assert false;
		}
	}
}
