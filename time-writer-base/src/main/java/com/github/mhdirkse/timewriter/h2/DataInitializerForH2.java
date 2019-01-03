package com.github.mhdirkse.timewriter.h2;

import com.github.mhdirkse.timewriter.TimeNoteController;
import com.github.mhdirkse.timewriter.UserController;

public interface DataInitializerForH2 {
    void addData(UserController userController, TimeNoteController timeNoteController);
}
