package com.ashwin.java.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotEmpty;

public class RedisDemoConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName;

    @JsonProperty
    private RedisConfiguration masterRedis;

    @JsonProperty
    private RedisConfiguration readRedis;

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    public RedisConfiguration getMasterRedis() {
        return masterRedis;
    }

    public void setMasterRedis(RedisConfiguration masterRedis) {
        this.masterRedis = masterRedis;
    }

    public RedisConfiguration getReadRedis() {
        return readRedis;
    }

    public void setReadRedis(RedisConfiguration readRedis) {
        this.readRedis = readRedis;
    }
}
