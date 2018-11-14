package com.github.mhdirkse.timewriter.db;

import com.github.mhdirkse.timewriter.UserController;

public interface DataInitializerForH2 {
    void addData(UserController userController);
}
