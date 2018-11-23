package com.github.mhdirkse.timewriter;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.mhdirkse.timewriter.model.UserInfo;

@RestController
@RequestMapping("/api/users")
@Transactional(
        isolation = Isolation.SERIALIZABLE,
        rollbackFor = Throwable.class)
public class UserController {
    private UserInfoRepository userInfoRepository;
    private PasswordEncoder passwordEncoder;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(
            PasswordEncoder passwordEncoder,
            UserInfoRepository userInfoRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
    }

    @PostMapping
    public ResponseEntity<UserInfo> addUser(@RequestBody UserInfo user) {
        UserInfo existingUser = userInfoRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return saveUserWithEncryptedPwd(user);
        }
    }

    private ResponseEntity<UserInfo> saveUserWithEncryptedPwd(UserInfo user) {
        UserInfo toSave = new UserInfo();
        toSave.setId(user.getId());
        toSave.setUsername(user.getUsername());
        toSave.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userInfoRepository.save(toSave), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> modifyUser(
            @PathVariable long id,
            @RequestBody UserInfo user,
            @AuthenticationPrincipal UserPrincipal loggedUser) {
        if(!isValid(id, user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!isAuthorized(id, user, loggedUser)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return saveUserWithEncryptedPwd(addId(user, id));
    }

    private boolean isValid(long id, UserInfo user) {
        return userInfoRepository.findById(id)
                .map(userOfId -> (userOfId.getUsername().equals(user.getUsername())))
                .orElse(false);
    }

    private UserInfo addId(UserInfo user, long id) {
        UserInfo toSave = new UserInfo();
        toSave.setId(id);
        toSave.setUsername(user.getUsername());
        toSave.setPassword(user.getPassword());
        return toSave;
    }

    private boolean isAuthorized(long id, UserInfo user, UserPrincipal loggedUser) {
        if(!loggedUser.hasUser()) {
            return false;
        }
        if(loggedUser.getAuthorities().contains(new SimpleGrantedAuthority(UserPrincipal.ROLE_ADMIN))) {
            return true;
        }
        return (user.getUsername().equals(loggedUser.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserInfo> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal loggedUser) {
        Optional<UserInfo> deletedUser = userInfoRepository.findById(id);
        if(!deletedUser.isPresent()) {
            logger.info(String.format("Refused to delete user %d because it is not present", id));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!isAuthorized(id, deletedUser.get(), loggedUser)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(isDeletedUserAdmin(deletedUser.get())) {
            logger.info("Refused to delete admin user");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userInfoRepository.delete(deletedUser.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isDeletedUserAdmin(UserInfo deletedUser) {
        UserPrincipal user = new UserPrincipal(deletedUser);
        return user.getAuthorities().contains(new SimpleGrantedAuthority(UserPrincipal.ROLE_ADMIN));
    }
}

