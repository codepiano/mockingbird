package com.codepiano.matchers;

import com.codepiano.models.Rule;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

public interface Matcher {

    Optional<Rule> match(@NonNull List<String> object);

    boolean addData(String text, Rule rule);
}
