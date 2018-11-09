package com.github.mhdirkse.timewriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.mhdirkse.timewriter.model.UserInfo;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private static final String USERNAME = "username";

    @Mock
    private UserInfoRepository userInfoRepository;

    private UserController instance;

    @Before
    public void setUp() {
        instance = new UserController(userInfoRepository);
    }

    @Test
    public void whenUserExistsThenAddUserFails() {
        UserInfo newUser = getUserInfo();
        UserInfo existingUser = getUserInfo();
        when(userInfoRepository.findByUsername(USERNAME)).thenReturn(existingUser);
        ResponseEntity<UserInfo> result = instance.addUser(newUser);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void whenUserDoesNotExistThenUserAdded() {
        UserInfo newUser = getUserInfo();
        UserInfo addedUser = getUserInfo();
        when(userInfoRepository.findByUsername(USERNAME)).thenReturn(null);
        when(userInfoRepository.save(newUser)).thenReturn(addedUser);
        ResponseEntity<UserInfo> response = instance.addUser(newUser);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(addedUser, response.getBody());
        verify(userInfoRepository).save(newUser);
    }

    private UserInfo getUserInfo() {
        UserInfo result = new UserInfo();
        result.setUsername(USERNAME);
        result.setPassword("password");
        return result;
    }

    @Test
    public void whenPathVariableDoesNotMatchUserThenUpdateFails() {
        UserInfo modification = getUserInfo();
        modification.setId(1L);
        ResponseEntity<UserInfo> response = instance.modifyUser(2L, modification, getPrincipal(USERNAME));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private UserPrincipal getPrincipal(String username) {
        UserInfo user = new UserInfo();
        user.setUsername(username);
        return new UserPrincipal(user);
    }

    @Test
    public void whenUpdateRequestOkThenUpdated() {
        long id = 1L;
        UserInfo modification = getUserInfoWithId(id);
        UserInfo savedModification = getUserInfoWithId(id);
        when(userInfoRepository.save(modification)).thenReturn(savedModification);
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, getPrincipal(USERNAME));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(savedModification, response.getBody());
        verify(userInfoRepository).save(modification);
    }

    @Test
    public void whenLoggedOutThenUpdateFails() {
        long id = 1L;
        UserInfo modification = getUserInfoWithId(id);
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, new UserPrincipal());
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void whenUpdateUserDiffersFromPrincipalThenUpdateFails() {
        long id = 1L;
        UserInfo modification = getUserInfoWithId(id);
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, getPrincipal("different"));
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    private UserInfo getUserInfoWithId(long id) {
        UserInfo userInfo = getUserInfo();
        userInfo.setId(id);
        return userInfo;
    }

    @Test
    public void whenDeletedUserMatchesLoggedUserThenDeleteSucceeds() {
        long id = 1L;
        UserInfo userToDelete = getUserInfoWithId(id);
        UserPrincipal principal = new UserPrincipal(userToDelete);
        when(userInfoRepository.findById(id)).thenReturn(Optional.<UserInfo>of(userToDelete));
        ResponseEntity<UserInfo> response = instance.deleteUser(id, principal);
        verify(userInfoRepository).delete(userToDelete);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenDeletedUserNotLoggedInThenDeleteFails() {
        UserInfo userToDelete = getUserInfoWithId(1L);
        UserInfo loggedUser = getUserInfoWithId(2L);
        loggedUser.setUsername("different");
        when(userInfoRepository.findById(1L)).thenReturn(Optional.of(userToDelete));
        ResponseEntity<UserInfo> response = instance.deleteUser(1L, new UserPrincipal(loggedUser));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
