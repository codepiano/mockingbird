package com.codepiano.models;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rule {

    private String                        matchType;
    private String                        matchText;
    private String                        mockResult;
    private Function<MockRequest, Object> access;

}
