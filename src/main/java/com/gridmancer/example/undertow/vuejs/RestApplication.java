package com.gridmancer.example.undertow.vuejs;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.gridmancer.example.undertow.vuejs.api.HealthCheckApi;

public class RestApplication extends Application {
    private final Set<Object> singletons;

    RestApplication() {
        singletons = Set.of(
            new HealthCheckApi()
        );
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}