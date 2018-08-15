package com.codepiano.matchers;

import com.codepiano.models.Rule;
import java.util.Optional;
import lombok.NonNull;

public interface Matcher {

    Optional<Rule> match(@NonNull Object object);
}
