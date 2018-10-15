package com.github.mhdirkse.timewriter;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7403610100769827085L;

    UserNotFoundException(String msg) {
        super(msg);
    }

    UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
