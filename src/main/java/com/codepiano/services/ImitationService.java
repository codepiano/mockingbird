package com.codepiano.services;

import com.codepiano.models.Imitation;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

@Service
public class ImitationService {

    public Imitation initImitation(JsonObject jsonObject) {
        return new Imitation();
    }
}
