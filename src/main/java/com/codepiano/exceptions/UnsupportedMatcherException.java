package com.codepiano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Code(1002)
public class UnsupportedMatcherException extends MockingbirdException {

    public UnsupportedMatcherException() {
        super("unsupport matcher");
    }

    public UnsupportedMatcherException(String reason) {
        super(reason);
    }
}
