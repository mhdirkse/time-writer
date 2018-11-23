package com.github.mhdirkse.timewriter;

import static org.mockito.Mockito.never;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.mhdirkse.timewriter.model.UserInfo;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private static final String USERNAME = "username";

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserController instance;

    @Before
    public void setUp() {
        instance = new UserController(passwordEncoder, userInfoRepository);
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
        UserInfo savedUser = getUserInfo();
        savedUser.setPassword("encrypted");
        UserInfo addedUser = getUserInfo();
        addedUser.setPassword("encrypted");
        addedUser.setId(1L);
        when(userInfoRepository.findByUsername(USERNAME)).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encrypted");
        when(userInfoRepository.save(savedUser)).thenReturn(addedUser);
        ResponseEntity<UserInfo> response = instance.addUser(newUser);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(addedUser, response.getBody());
        verify(userInfoRepository).save(savedUser);
    }

    private UserInfo getUserInfo() {
        UserInfo result = new UserInfo();
        result.setUsername(USERNAME);
        result.setPassword("password");
        return result;
    }

    @Test
    public void whenPathVariableDoesNotMatchUserThenUpdateFails() {
        UserInfo orig = getUserInfo();
        orig.setId(1L);
        UserInfo modification = getUserInfo();
        modification.setUsername("other");
        ResponseEntity<UserInfo> response = instance.modifyUser(1L, modification, getPrincipal(USERNAME));
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
        UserInfo original = getUserInfoWithId(id);
        UserInfo modification = getUserInfo();
        UserInfo savedModification = getUserInfoWithId(id);
        savedModification.setPassword("encrypted");
        UserInfo addedModification = getUserInfoWithId(id);
        addedModification.setPassword("encrypted");
        when(userInfoRepository.findById(id)).thenReturn(Optional.of(original));
        when(passwordEncoder.encode("password")).thenReturn("encrypted");
        when(userInfoRepository.save(savedModification)).thenReturn(addedModification);
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, getPrincipal(USERNAME));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(addedModification, response.getBody());
        verify(userInfoRepository).save(savedModification);
    }

    @Test
    public void whenLoggedOutThenUpdateFails() {
        long id = 1L;
        UserInfo original = getUserInfoWithId(id);
        when(userInfoRepository.findById(id)).thenReturn(Optional.of(original));
        UserInfo modification = getUserInfo();
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, new UserPrincipal());
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void whenUpdateUserDiffersFromPrincipalThenUpdateFails() {
        long id = 1L;
        UserInfo original = getUserInfoWithId(id);
        UserInfo modification = getUserInfo();
        when(userInfoRepository.findById(id)).thenReturn(Optional.<UserInfo>of(original));
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, getPrincipal("different"));
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void whenAdminLoggedInThenAnyUserCanBeUpdated() {
        long id = 1L;
        UserInfo original = getUserInfoWithId(id);
        UserInfo modification = getUserInfo();
        UserInfo savedModification = getUserInfoWithId(id);
        savedModification.setPassword("encrypted");
        UserInfo addedModification = getUserInfoWithId(id);
        addedModification.setPassword("encrypted");
        when(userInfoRepository.findById(id)).thenReturn(Optional.of(original));
        when(passwordEncoder.encode("password")).thenReturn("encrypted");
        when(userInfoRepository.save(savedModification)).thenReturn(addedModification);
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, getPrincipal(UserPrincipal.ADMIN));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(addedModification, response.getBody());
        verify(userInfoRepository).save(savedModification);        
    }

    @Test
    public void whenAdminTriesToUpdateNonexistentUserThenBadRequest() {
        long id = 1L;
        UserInfo modification = getUserInfo();
        when(userInfoRepository.findById(id)).thenReturn(Optional.<UserInfo>empty());
        ResponseEntity<UserInfo> response = instance.modifyUser(id, modification, getPrincipal(UserPrincipal.ADMIN));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void whenLoggedUserIsAdminThenAnyUserCanBeDeleted() {
        long id = 1L;
        UserInfo userToDelete = getUserInfoWithId(id);
        when(userInfoRepository.findById(id)).thenReturn(Optional.<UserInfo>of(userToDelete));
        ResponseEntity<UserInfo> response = instance.deleteUser(id, getPrincipal(UserPrincipal.ADMIN));
        verify(userInfoRepository).delete(userToDelete);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void whenAdminTriesToDeleteNonexistentUserThenBadRequest() {
        long id = 1L;
        when(userInfoRepository.findById(id)).thenReturn(Optional.<UserInfo>empty());
        ResponseEntity<UserInfo> response = instance.deleteUser(id,  getPrincipal(UserPrincipal.ADMIN));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenAdminUserTriesToDeleteHimSelfThenError() {
        long id = 1L;
        UserInfo userToDelete = getAdminUser(id);
        UserPrincipal principal = new UserPrincipal(userToDelete);
        when(userInfoRepository.findById(id)).thenReturn(Optional.<UserInfo>of(userToDelete));
        ResponseEntity<UserInfo> response = instance.deleteUser(id, principal);
        verify(userInfoRepository, never()).delete(userToDelete);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private UserInfo getAdminUser(long id) {
        UserInfo user = new UserInfo();
        user.setUsername(UserPrincipal.ADMIN);
        user.setPassword(UserPrincipal.ADMIN);
        user.setId(id);
        return user;
    }
}
