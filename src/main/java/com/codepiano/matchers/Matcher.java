package com.codepiano.matchers;

import com.codepiano.models.MockRequest;
import com.codepiano.models.MockResponse;
import com.codepiano.models.Rule;
import java.util.Optional;
import lombok.NonNull;

public interface Matcher {

    Optional<MockResponse> match(@NonNull Rule rule, @NonNull MockRequest mockRequest);
}
