package com.github.mhdirkse.timewriter.db;

import com.github.mhdirkse.timewriter.UserController;
import com.github.mhdirkse.timewriter.model.UserInfo;

class DataInitializerForH2Impl implements DataInitializerForH2 {
    @Override
    public void addData(UserController userController) {
        UserInfo user = new UserInfo();
        user.setUsername("username");
        user.setPassword("password");
        userController.addUser(user);
    }
}
