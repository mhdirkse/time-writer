package com.github.mhdirkse.timewriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.mhdirkse.timewriter.db.DataInitializerForH2;
import com.github.mhdirkse.timewriter.model.UserInfo;

@RunWith(MockitoJUnitRunner.class)
public class AdminUserInitializerTest {
    @Mock
    private UserInfoRepository userRepository;

    @Mock
    private UserController userController;

    @Mock
    private DataInitializerForH2 dataInitializerForH2;

    @InjectMocks
    private AdminUserInitializer instance;

    @Test
    public void whenNoUsersThenAdminAdded() throws Exception {
        when(userRepository.count()).thenReturn(0L);
        when(userController.addUser(any())).thenReturn(new ResponseEntity<UserInfo>(HttpStatus.OK));
        instance.run();
        verify(userController).addUser(any());
        verify(dataInitializerForH2).addData(userController);
    }

    @Test
    public void whenAlreadyUsersThenNoAdminUserAdded() throws Exception {
        when(userRepository.count()).thenReturn(1L);
        instance.run();
        verify(userController, never()).addUser(any());
        verify(dataInitializerForH2, never()).addData(any());
    }
}
