package com.codepiano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Code(1001)
public class NoHostRuleException extends MockingbirdException {

    public NoHostRuleException() {
        super("no rule of host");
    }

    public NoHostRuleException(String reason) {
        super(reason);
    }
}
