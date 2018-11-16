package com.github.mhdirkse.timewriter.h2;

import com.github.mhdirkse.timewriter.UserController;
import com.github.mhdirkse.timewriter.h2.DataInitializerForH2;

class DataInitializerForH2Noop implements DataInitializerForH2 {
    @Override
    public void addData(UserController userController) {
    }
}
