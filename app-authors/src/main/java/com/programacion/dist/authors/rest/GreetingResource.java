package com.programacion.dist.authors.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/hello")
public class GreetingResource {
    //http://127.0.0.1:8080/contexto/hello?txt=holamundo

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(){
        return "Hello from quarkus REST";
    }
}
