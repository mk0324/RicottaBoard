package com.hungrybird.back.AuthApp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UserInvitationException extends RuntimeException {

    private final List<String> user;
    private final String message;

    public UserInvitationException(List<String> user, String message) {
        super(String.format("Failed to register User[%s] : '%s'", user, message));
        this.user = user;
        this.message = message;
    }

}