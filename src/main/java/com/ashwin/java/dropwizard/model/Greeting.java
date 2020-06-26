package com.ashwin.java.dropwizard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *   "id": 1,
 *   "content": "Hello and welcome!"
 * }
 */
public class Greeting {
    private long id;
    private String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
