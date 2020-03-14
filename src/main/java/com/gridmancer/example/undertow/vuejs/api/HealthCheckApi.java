package com.gridmancer.example.undertow.vuejs.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HealthCheckApi {
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthCheck() {
        // TODO: Add real health checking and build/service version information api
        return Response.ok().entity("ok").build();
    }
}