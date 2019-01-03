package com.github.mhdirkse.timewriter.h2;

import java.time.Instant;

import org.springframework.http.ResponseEntity;

import com.github.mhdirkse.timewriter.TimeNoteController;
import com.github.mhdirkse.timewriter.UserController;
import com.github.mhdirkse.timewriter.UserPrincipal;
import com.github.mhdirkse.timewriter.model.TimeNote;
import com.github.mhdirkse.timewriter.model.UserInfo;

class DataInitializerForH2Impl implements DataInitializerForH2 {
    private static final Instant FIRST = Instant.parse("2018-11-24T09:00:00Z");
    private static final Instant SECOND = Instant.parse("2018-11-24T11:00:00Z");
    private static final Instant THIRD = Instant.parse("2018-11-24T13:00:00Z");
    
    @Override
    public void addData(UserController userController, TimeNoteController timeNoteController) {
        UserInfo userToAdd = new UserInfo();
        userToAdd.setUsername("username");
        userToAdd.setPassword("password");
        ResponseEntity<UserInfo> savedUserEntity = userController.addUser(userToAdd);
        UserInfo user = savedUserEntity.getBody();
        UserPrincipal principal = new UserPrincipal(user);
        TimeNote first = new TimeNote();
        first.setUserId(user.getId());
        first.setTimestamp(FIRST);
        first.setMessage("First");
        timeNoteController.add(first, principal);
        TimeNote second = new TimeNote();
        second.setUserId(user.getId());
        second.setTimestamp(SECOND);
        second.setMessage("Second");
        timeNoteController.add(second, principal);
        TimeNote third = new TimeNote();
        third.setUserId(user.getId());
        third.setTimestamp(THIRD);
        third.setMessage("Third");
        timeNoteController.add(third, principal);
    }
}
