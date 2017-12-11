package com.server.dropbox_springboot_sever;

import com.server.dropbox_springboot_sever.entity.DropboxStorage;
import com.server.dropbox_springboot_sever.entity.SharedDetails;
import com.server.dropbox_springboot_sever.entity.User;
import com.server.dropbox_springboot_sever.service.DropboxStorageService;
import com.server.dropbox_springboot_sever.service.SharedDetailsService;
import com.server.dropbox_springboot_sever.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
		int result = dropboxStorageService.changeStarredStatus(1, true);
		System.out.println("result : "+result);
		if(result==1){
			assert true;
		}
		else {
			assert false;
		}
//		assert
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
		if(dropboxStorageService.createDirectory("test","./test")) {
			assert true;
		}
		else {
			assert false;
		}
	}


}
