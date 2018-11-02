package com.github.mhdirkse.timewriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.mhdirkse.timewriter.model.UserInfo;

import org.junit.Assert;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
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
        when(userInfoRepository.findByUsername("username")).thenReturn(existingUser);
        ResponseEntity<UserInfo> result = instance.addUser(newUser);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void whenUserDoesNotExistThenUserAdded() {
        UserInfo newUser = getUserInfo();
        UserInfo addedUser = getUserInfo();
        when(userInfoRepository.findByUsername("username")).thenReturn(null);
        when(userInfoRepository.save(newUser)).thenReturn(addedUser);
        ResponseEntity<UserInfo> response = instance.addUser(newUser);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(addedUser, response.getBody());
        verify(userInfoRepository).save(newUser);
    }

    private UserInfo getUserInfo() {
        UserInfo result = new UserInfo();
        result.setUsername("username");
        result.setPassword("password");
        return result;
    }
}
