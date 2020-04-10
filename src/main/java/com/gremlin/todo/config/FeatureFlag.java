package com.gremlin.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class FeatureFlag {

    private final Environment environment;


    public FeatureFlag(Environment environment) {
        this.environment = environment;
    }

    public boolean isAlfiEnabled() {
        return  Objects.requireNonNull(environment.getProperty("GREMLIN_ALFI_ENABLED")).equalsIgnoreCase("true");
    }
}
