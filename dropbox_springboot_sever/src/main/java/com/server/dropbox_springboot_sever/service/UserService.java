package com.server.dropbox_springboot_sever.service;


import com.server.dropbox_springboot_sever.entity.User;
import com.server.dropbox_springboot_sever.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> login(String username){
        return userRepository.findByUsername(username);
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public static String generateHashPassword(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length;idx++) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (Exception e) {
            System.out.println("Some error occured in hash");
        }

        return hash.toString();
    }

}
