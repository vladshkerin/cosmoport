package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Validation ship error")
public class ValidationShipException extends RuntimeException {

    public ValidationShipException(String message) {
        super(message);
    }
}
