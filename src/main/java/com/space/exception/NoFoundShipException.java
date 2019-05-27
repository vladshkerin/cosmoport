package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found ship")
public class NoFoundShipException extends RuntimeException {

    public NoFoundShipException(String s) {
        super(s);
    }
}
