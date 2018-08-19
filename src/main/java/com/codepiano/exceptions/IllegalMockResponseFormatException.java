package com.codepiano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Code(1003)
public class IllegalMockResponseFormatException extends MockingbirdException {

    public IllegalMockResponseFormatException() {
        super("no rule of host");
    }

    public IllegalMockResponseFormatException(String reason) {
        super(reason);
    }
}
