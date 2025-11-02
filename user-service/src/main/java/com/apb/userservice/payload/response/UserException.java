package com.apb.userservice.payload.response;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }
}
