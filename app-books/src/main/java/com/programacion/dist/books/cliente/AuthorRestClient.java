package com.programacion.dist.books.cliente;

import com.programacion.dist.books.dtos.AuthorDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

// copia del sercico original pero sin implementaciones
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@RegisterRestClient(baseUri = "http://localhost:8080")
//@RegisterRestClient(configKey = "authors.api")
@RegisterRestClient(baseUri = "stork://authors-api")
public interface AuthorRestClient {

    @GET
    @Path("/find/{isbn}")
    List<AuthorDto> findByBookIsbn(@PathParam("isbn") String isbn);
}
