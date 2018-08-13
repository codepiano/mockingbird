package com.codepiano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoHostRuleException extends MockingbirdException {

    public NoHostRuleException() {
        super("no rule of host");
    }

    public NoHostRuleException(String reason) {
        super(reason);
    }
}
