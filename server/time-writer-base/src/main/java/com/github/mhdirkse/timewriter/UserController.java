package com.github.mhdirkse.timewriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.mhdirkse.timewriter.model.UserInfo;

@RestController
@RequestMapping("/api/users/")
public class UserController {
    private UserInfoRepository userInfoRepository;

    UserController(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @PostMapping
    ResponseEntity<UserInfo> addUser(@RequestBody UserInfo user) {
        UserInfo existingUser = userInfoRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(userInfoRepository.save(user), HttpStatus.OK);
        }
    }
}
