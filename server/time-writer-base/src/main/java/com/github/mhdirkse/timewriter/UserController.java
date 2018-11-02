package com.github.mhdirkse.timewriter;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("{id}")
    ResponseEntity<UserInfo> modifyUser(
            @PathVariable long id,
            @RequestBody UserInfo user,
            UserPrincipal loggedUser) {
        if(!isValid(id, user, loggedUser)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userInfoRepository.save(user), HttpStatus.OK);
    }

    private boolean isValid(long id, UserInfo user, UserPrincipal loggedUser) {
        boolean idMatch = (user.getId().longValue() == id);
        boolean rightUser = (user.getUsername().equals(loggedUser.getUsername()));
        return Boolean.logicalAnd(idMatch, rightUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserInfo> deleteUser(@PathVariable Long id, UserPrincipal loggedUser) {
        Optional<UserInfo> deletedUser = userInfoRepository.findById(id);
        if(deletedUser.isPresent() && deletedUser.get().getUsername().equals(loggedUser.getUsername())) {
            userInfoRepository.delete(deletedUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
