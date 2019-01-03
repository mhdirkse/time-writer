package com.github.mhdirkse.timewriter.h2;

import com.github.mhdirkse.timewriter.TimeNoteController;
import com.github.mhdirkse.timewriter.UserController;

class DataInitializerForH2Noop implements DataInitializerForH2 {
    @Override
    public void addData(UserController userController, TimeNoteController timeNoteController) {
    }
}
