package com.programacion.dist.authors.rest;

import com.programacion.dist.authors.db.Author;
import com.programacion.dist.authors.repo.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthorsRest {

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    @Inject
    AuthorRepository authorRepository;

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {

        var obj = authorRepository.findByIdOptional(id);

        if(obj.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(obj.get()).build();
    }

    @GET
    public List<Author> findAll() {
        return authorRepository.listAll();
    }

    @GET
    @Path("/find/{bookIsbn}")
    public List<Author> findByBookIsbn(@PathParam("bookIsbn") String isbn) {

//        Config config =  ConfigProvider.getConfig();
//        var puerto = config.getOptionalValue("quarkus.http.port", Integer.class).orElse(appPort);
//
        Config config = ConfigProvider.getConfig();
        config.getConfigSources().forEach(
                obj -> {
                    System.out.printf("%d -> %s\n", obj.getOrdinal(), obj.getName());
                }
        );

        var ret = authorRepository.findByBook(isbn);

        return ret.stream()
                .map(obj ->{
                    String newName = String.format("%s  (%d)", obj.getName(), appPort);
                    obj.setName(newName);
                    return obj;
                })
                .toList();

    }
}
