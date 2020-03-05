package com.gridmancer.example.undertow.vuejs;

import java.util.Set;

import javax.ws.rs.core.Application;

public class RestApplication extends Application {
    private final Set<Object> singletons;

    RestApplication() {
        singletons = Set.of(

        );
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}