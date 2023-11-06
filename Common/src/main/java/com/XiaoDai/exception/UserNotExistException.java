package com.XiaoDai.exception;

public class UserNotExistException extends BaseException{
    public UserNotExistException() {
    }

    public UserNotExistException(String message) {
        super(message);
    }
}
