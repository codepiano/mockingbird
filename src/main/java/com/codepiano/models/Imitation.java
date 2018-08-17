package com.codepiano.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Imitation {

    public static final String DEFAULT_HOST = "mockingbird";

    private String host;
}
