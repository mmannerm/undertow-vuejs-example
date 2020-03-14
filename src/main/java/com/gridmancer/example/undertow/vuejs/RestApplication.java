package com.gridmancer.example.undertow.vuejs;

import com.gridmancer.example.undertow.vuejs.api.HealthCheckApi;
import java.util.Set;
import javax.ws.rs.core.Application;

public class RestApplication extends Application {
  private final Set<Object> singletons;

  RestApplication() {
    singletons = Set.of(new HealthCheckApi());
  }

  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }
}
