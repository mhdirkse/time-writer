package com.github.mhdirkse.timewriter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.mhdirkse.timewriter.model.UserInfo;

import lombok.Setter;

@RestController
public class UserInfoController {
    @Autowired
    @Setter
    private UserInfoRepository userInfoRepository;

    @GetMapping("/users")
    List<UserInfo> getUsers() {
        return userInfoRepository.findAll();
    }
}
