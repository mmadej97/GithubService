package com.mmadej.entities;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Languages {

    private Map<String, Long> languagesMap = new HashMap<>();

    @JsonAnySetter
    public void add(String key, Long value) {
        languagesMap.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Long> getLanguagesMap() {
        return languagesMap;
    }
}
