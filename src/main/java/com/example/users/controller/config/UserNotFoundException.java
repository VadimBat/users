package com.example.users.controller.config;

/**
 * UserNotFoundException describes exceptions while user is not found.
 */
public class UserNotFoundException extends Exception {
    private static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User is not found!";

    public UserNotFoundException() {
        super(USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }

}
