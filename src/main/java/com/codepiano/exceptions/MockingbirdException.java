package com.codepiano.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Code(1000)
public class MockingbirdException extends RuntimeException {
    private String reason;
}
