package com.codepiano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Code(1004)
public class NoMatchedMockException extends MockingbirdException {

    public NoMatchedMockException() {
        super("no matched mock");
    }

    public NoMatchedMockException(String reason) {
        super(reason);
    }
}
