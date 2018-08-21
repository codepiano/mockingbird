package com.codepiano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Code(1005)
public class NoRequestDataException extends MockingbirdException {

    public NoRequestDataException() {
        super("no request data");
    }

    public NoRequestDataException(String reason) {
        super(reason);
    }
}
